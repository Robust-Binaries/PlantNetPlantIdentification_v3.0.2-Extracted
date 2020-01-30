package com.facebook.react.views.webview;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.ContentSizeChangeEvent;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.TouchesHelper;
import com.facebook.react.views.scroll.ReactScrollViewHelper;
import com.facebook.react.views.webview.events.TopLoadingErrorEvent;
import com.facebook.react.views.webview.events.TopLoadingFinishEvent;
import com.facebook.react.views.webview.events.TopLoadingStartEvent;
import com.facebook.react.views.webview.events.TopMessageEvent;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

@ReactModule(name = "RCTWebView")
public class ReactWebViewManager extends SimpleViewManager<WebView> {
    protected static final String BLANK_URL = "about:blank";
    protected static final String BRIDGE_NAME = "__REACT_WEB_VIEW_BRIDGE";
    public static final int COMMAND_GO_BACK = 1;
    public static final int COMMAND_GO_FORWARD = 2;
    public static final int COMMAND_INJECT_JAVASCRIPT = 6;
    public static final int COMMAND_POST_MESSAGE = 5;
    public static final int COMMAND_RELOAD = 3;
    public static final int COMMAND_STOP_LOADING = 4;
    protected static final String HTML_ENCODING = "UTF-8";
    protected static final String HTML_MIME_TYPE = "text/html";
    protected static final String HTTP_METHOD_POST = "POST";
    private static final String INTENT_URL_PREFIX = "intent://";
    public static final String REACT_CLASS = "RCTWebView";
    @Nullable
    protected PictureListener mPictureListener;
    protected WebViewConfig mWebViewConfig;

    protected static class ReactWebView extends WebView implements LifecycleEventListener {
        @Nullable
        protected String injectedJS;
        @Nullable
        protected ReactWebViewClient mReactWebViewClient;
        protected boolean messagingEnabled = false;

        protected class ReactWebViewBridge {
            ReactWebView mContext;

            ReactWebViewBridge(ReactWebView reactWebView) {
                this.mContext = reactWebView;
            }

            @JavascriptInterface
            public void postMessage(String str) {
                this.mContext.onMessage(str);
            }
        }

        public void onHostPause() {
        }

        public void onHostResume() {
        }

        public ReactWebView(ThemedReactContext themedReactContext) {
            super(themedReactContext);
        }

        public void onHostDestroy() {
            cleanupCallbacksAndDestroy();
        }

        public void setWebViewClient(WebViewClient webViewClient) {
            super.setWebViewClient(webViewClient);
            this.mReactWebViewClient = (ReactWebViewClient) webViewClient;
        }

        @Nullable
        public ReactWebViewClient getReactWebViewClient() {
            return this.mReactWebViewClient;
        }

        public void setInjectedJavaScript(@Nullable String str) {
            this.injectedJS = str;
        }

        /* access modifiers changed from: protected */
        public ReactWebViewBridge createReactWebViewBridge(ReactWebView reactWebView) {
            return new ReactWebViewBridge(reactWebView);
        }

        public void setMessagingEnabled(boolean z) {
            if (this.messagingEnabled != z) {
                this.messagingEnabled = z;
                if (z) {
                    addJavascriptInterface(createReactWebViewBridge(this), ReactWebViewManager.BRIDGE_NAME);
                    linkBridge();
                } else {
                    removeJavascriptInterface(ReactWebViewManager.BRIDGE_NAME);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void evaluateJavascriptWithFallback(String str) {
            if (VERSION.SDK_INT >= 19) {
                evaluateJavascript(str, null);
                return;
            }
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("javascript:");
                sb.append(URLEncoder.encode(str, ReactWebViewManager.HTML_ENCODING));
                loadUrl(sb.toString());
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public void callInjectedJavaScript() {
            if (getSettings().getJavaScriptEnabled()) {
                String str = this.injectedJS;
                if (str != null && !TextUtils.isEmpty(str)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("(function() {\n");
                    sb.append(this.injectedJS);
                    sb.append(";\n})();");
                    evaluateJavascriptWithFallback(sb.toString());
                }
            }
        }

        public void linkBridge() {
            if (this.messagingEnabled) {
                evaluateJavascriptWithFallback("(window.originalPostMessage = window.postMessage,window.postMessage = function(data) {__REACT_WEB_VIEW_BRIDGE.postMessage(String(data));})");
            }
        }

        public void onMessage(String str) {
            ReactWebViewManager.dispatchEvent(this, new TopMessageEvent(getId(), str));
        }

        /* access modifiers changed from: protected */
        public void cleanupCallbacksAndDestroy() {
            setWebViewClient(null);
            destroy();
        }
    }

    protected static class ReactWebViewClient extends WebViewClient {
        protected boolean mLastLoadFailed = false;
        @Nullable
        protected List<Pattern> mOriginWhitelist;
        @Nullable
        protected ReadableArray mUrlPrefixesForDefaultIntent;

        protected ReactWebViewClient() {
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            if (!this.mLastLoadFailed) {
                ReactWebView reactWebView = (ReactWebView) webView;
                reactWebView.callInjectedJavaScript();
                reactWebView.linkBridge();
                emitFinishEvent(webView, str);
            }
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            this.mLastLoadFailed = false;
            ReactWebViewManager.dispatchEvent(webView, new TopLoadingStartEvent(webView.getId(), createWebViewEvent(webView, str)));
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (str.equals(ReactWebViewManager.BLANK_URL)) {
                return false;
            }
            ReadableArray readableArray = this.mUrlPrefixesForDefaultIntent;
            if (readableArray != null && readableArray.size() > 0) {
                Iterator it = this.mUrlPrefixesForDefaultIntent.toArrayList().iterator();
                while (it.hasNext()) {
                    if (str.startsWith((String) it.next())) {
                        launchIntent(webView.getContext(), str);
                        return true;
                    }
                }
            }
            List<Pattern> list = this.mOriginWhitelist;
            if (list != null && shouldHandleURL(list, str)) {
                return false;
            }
            launchIntent(webView.getContext(), str);
            return true;
        }

        /* JADX WARNING: Removed duplicated region for block: B:13:0x0048  */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x001a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void launchIntent(android.content.Context r5, java.lang.String r6) {
            /*
                r4 = this;
                java.lang.String r0 = "intent://"
                boolean r0 = r6.startsWith(r0)
                r1 = 0
                if (r0 == 0) goto L_0x0017
                r0 = 1
                android.content.Intent r0 = android.content.Intent.parseUri(r6, r0)     // Catch:{ URISyntaxException -> 0x000f }
                goto L_0x0018
            L_0x000f:
                r0 = move-exception
                java.lang.String r2 = "ReactNative"
                java.lang.String r3 = "Can't parse intent:// URI"
                com.facebook.common.logging.FLog.m49e(r2, r3, r0)
            L_0x0017:
                r0 = r1
            L_0x0018:
                if (r0 == 0) goto L_0x0048
                java.lang.String r2 = "android.intent.category.BROWSABLE"
                r0.addCategory(r2)
                r0.setComponent(r1)
                r0.setSelector(r1)
                android.content.pm.PackageManager r1 = r5.getPackageManager()
                r2 = 65536(0x10000, float:9.18355E-41)
                android.content.pm.ResolveInfo r1 = r1.resolveActivity(r0, r2)
                if (r1 == 0) goto L_0x0035
                r5.startActivity(r0)
                goto L_0x0053
            L_0x0035:
                java.lang.String r1 = "browser_fallback_url"
                java.lang.String r0 = r0.getStringExtra(r1)
                android.content.Intent r1 = new android.content.Intent
                java.lang.String r2 = "android.intent.action.VIEW"
                android.net.Uri r0 = android.net.Uri.parse(r0)
                r1.<init>(r2, r0)
                r0 = r1
                goto L_0x0053
            L_0x0048:
                android.content.Intent r0 = new android.content.Intent
                java.lang.String r1 = "android.intent.action.VIEW"
                android.net.Uri r2 = android.net.Uri.parse(r6)
                r0.<init>(r1, r2)
            L_0x0053:
                r1 = 268435456(0x10000000, float:2.5243549E-29)
                r0.setFlags(r1)     // Catch:{ ActivityNotFoundException -> 0x0061 }
                java.lang.String r1 = "android.intent.category.BROWSABLE"
                r0.addCategory(r1)     // Catch:{ ActivityNotFoundException -> 0x0061 }
                r5.startActivity(r0)     // Catch:{ ActivityNotFoundException -> 0x0061 }
                goto L_0x0078
            L_0x0061:
                r5 = move-exception
                java.lang.String r0 = "ReactNative"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "activity not found to handle uri scheme for: "
                r1.append(r2)
                r1.append(r6)
                java.lang.String r6 = r1.toString()
                com.facebook.common.logging.FLog.m89w(r0, r6, r5)
            L_0x0078:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.views.webview.ReactWebViewManager.ReactWebViewClient.launchIntent(android.content.Context, java.lang.String):void");
        }

        private boolean shouldHandleURL(List<Pattern> list, String str) {
            Uri parse = Uri.parse(str);
            String scheme = parse.getScheme() != null ? parse.getScheme() : "";
            String authority = parse.getAuthority() != null ? parse.getAuthority() : "";
            StringBuilder sb = new StringBuilder();
            sb.append(scheme);
            sb.append("://");
            sb.append(authority);
            String sb2 = sb.toString();
            for (Pattern matcher : list) {
                if (matcher.matcher(sb2).matches()) {
                    return true;
                }
            }
            return false;
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
            this.mLastLoadFailed = true;
            emitFinishEvent(webView, str2);
            WritableMap createWebViewEvent = createWebViewEvent(webView, str2);
            createWebViewEvent.putDouble("code", (double) i);
            createWebViewEvent.putString("description", str);
            ReactWebViewManager.dispatchEvent(webView, new TopLoadingErrorEvent(webView.getId(), createWebViewEvent));
        }

        /* access modifiers changed from: protected */
        public void emitFinishEvent(WebView webView, String str) {
            ReactWebViewManager.dispatchEvent(webView, new TopLoadingFinishEvent(webView.getId(), createWebViewEvent(webView, str)));
        }

        /* access modifiers changed from: protected */
        public WritableMap createWebViewEvent(WebView webView, String str) {
            WritableMap createMap = Arguments.createMap();
            createMap.putDouble(TouchesHelper.TARGET_KEY, (double) webView.getId());
            createMap.putString(ImagesContract.URL, str);
            createMap.putBoolean("loading", !this.mLastLoadFailed && webView.getProgress() != 100);
            createMap.putString("title", webView.getTitle());
            createMap.putBoolean("canGoBack", webView.canGoBack());
            createMap.putBoolean("canGoForward", webView.canGoForward());
            return createMap;
        }

        public void setUrlPrefixesForDefaultIntent(ReadableArray readableArray) {
            this.mUrlPrefixesForDefaultIntent = readableArray;
        }

        public void setOriginWhitelist(List<Pattern> list) {
            this.mOriginWhitelist = list;
        }
    }

    public String getName() {
        return REACT_CLASS;
    }

    public ReactWebViewManager() {
        this.mWebViewConfig = new WebViewConfig() {
            public void configWebView(WebView webView) {
            }
        };
    }

    public ReactWebViewManager(WebViewConfig webViewConfig) {
        this.mWebViewConfig = webViewConfig;
    }

    /* access modifiers changed from: protected */
    public ReactWebView createReactWebViewInstance(ThemedReactContext themedReactContext) {
        return new ReactWebView(themedReactContext);
    }

    /* access modifiers changed from: protected */
    @TargetApi(21)
    public WebView createViewInstance(ThemedReactContext themedReactContext) {
        ReactWebView createReactWebViewInstance = createReactWebViewInstance(themedReactContext);
        createReactWebViewInstance.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return true;
            }

            public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
                callback.invoke(str, true, false);
            }
        });
        themedReactContext.addLifecycleEventListener(createReactWebViewInstance);
        this.mWebViewConfig.configWebView(createReactWebViewInstance);
        WebSettings settings = createReactWebViewInstance.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        setAllowUniversalAccessFromFileURLs(createReactWebViewInstance, false);
        setMixedContentMode(createReactWebViewInstance, ReactScrollViewHelper.OVER_SCROLL_NEVER);
        createReactWebViewInstance.setLayoutParams(new LayoutParams(-1, -1));
        setGeolocationEnabled(createReactWebViewInstance, Boolean.valueOf(false));
        return createReactWebViewInstance;
    }

    @ReactProp(defaultBoolean = true, name = "hardwareAccelerationEnabledExperimental")
    public void sethardwareAccelerationEnabledExperimental(WebView webView, boolean z) {
        if (!z) {
            webView.setLayerType(1, null);
        }
    }

    @ReactProp(name = "javaScriptEnabled")
    public void setJavaScriptEnabled(WebView webView, boolean z) {
        webView.getSettings().setJavaScriptEnabled(z);
    }

    @ReactProp(name = "thirdPartyCookiesEnabled")
    public void setThirdPartyCookiesEnabled(WebView webView, boolean z) {
        if (VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, z);
        }
    }

    @ReactProp(name = "scalesPageToFit")
    public void setScalesPageToFit(WebView webView, boolean z) {
        webView.getSettings().setUseWideViewPort(!z);
    }

    @ReactProp(name = "domStorageEnabled")
    public void setDomStorageEnabled(WebView webView, boolean z) {
        webView.getSettings().setDomStorageEnabled(z);
    }

    @ReactProp(name = "userAgent")
    public void setUserAgent(WebView webView, @Nullable String str) {
        if (str != null) {
            webView.getSettings().setUserAgentString(str);
        }
    }

    @ReactProp(name = "mediaPlaybackRequiresUserAction")
    @TargetApi(17)
    public void setMediaPlaybackRequiresUserAction(WebView webView, boolean z) {
        webView.getSettings().setMediaPlaybackRequiresUserGesture(z);
    }

    @ReactProp(name = "allowUniversalAccessFromFileURLs")
    public void setAllowUniversalAccessFromFileURLs(WebView webView, boolean z) {
        webView.getSettings().setAllowUniversalAccessFromFileURLs(z);
    }

    @ReactProp(name = "saveFormDataDisabled")
    public void setSaveFormDataDisabled(WebView webView, boolean z) {
        webView.getSettings().setSaveFormData(!z);
    }

    @ReactProp(name = "injectedJavaScript")
    public void setInjectedJavaScript(WebView webView, @Nullable String str) {
        ((ReactWebView) webView).setInjectedJavaScript(str);
    }

    @ReactProp(name = "messagingEnabled")
    public void setMessagingEnabled(WebView webView, boolean z) {
        ((ReactWebView) webView).setMessagingEnabled(z);
    }

    @ReactProp(name = "source")
    public void setSource(WebView webView, @Nullable ReadableMap readableMap) {
        if (readableMap != null) {
            if (readableMap.hasKey("html")) {
                String string = readableMap.getString("html");
                if (readableMap.hasKey("baseUrl")) {
                    webView.loadDataWithBaseURL(readableMap.getString("baseUrl"), string, HTML_MIME_TYPE, HTML_ENCODING, null);
                } else {
                    webView.loadData(string, HTML_MIME_TYPE, HTML_ENCODING);
                }
                return;
            } else if (readableMap.hasKey(RNFetchBlobConst.DATA_ENCODE_URI)) {
                String string2 = readableMap.getString(RNFetchBlobConst.DATA_ENCODE_URI);
                String url = webView.getUrl();
                if (url != null && url.equals(string2)) {
                    return;
                }
                if (!readableMap.hasKey(Param.METHOD) || !readableMap.getString(Param.METHOD).equalsIgnoreCase(HTTP_METHOD_POST)) {
                    HashMap hashMap = new HashMap();
                    if (readableMap.hasKey("headers")) {
                        ReadableMap map = readableMap.getMap("headers");
                        ReadableMapKeySetIterator keySetIterator = map.keySetIterator();
                        while (keySetIterator.hasNextKey()) {
                            String nextKey = keySetIterator.nextKey();
                            if (!"user-agent".equals(nextKey.toLowerCase(Locale.ENGLISH))) {
                                hashMap.put(nextKey, map.getString(nextKey));
                            } else if (webView.getSettings() != null) {
                                webView.getSettings().setUserAgentString(map.getString(nextKey));
                            }
                        }
                    }
                    webView.loadUrl(string2, hashMap);
                    return;
                }
                byte[] bArr = null;
                if (readableMap.hasKey("body")) {
                    String string3 = readableMap.getString("body");
                    try {
                        bArr = string3.getBytes(HTML_ENCODING);
                    } catch (UnsupportedEncodingException unused) {
                        bArr = string3.getBytes();
                    }
                }
                if (bArr == null) {
                    bArr = new byte[0];
                }
                webView.postUrl(string2, bArr);
                return;
            }
        }
        webView.loadUrl(BLANK_URL);
    }

    @ReactProp(name = "onContentSizeChange")
    public void setOnContentSizeChange(WebView webView, boolean z) {
        if (z) {
            webView.setPictureListener(getPictureListener());
        } else {
            webView.setPictureListener(null);
        }
    }

    @ReactProp(name = "mixedContentMode")
    public void setMixedContentMode(WebView webView, @Nullable String str) {
        if (VERSION.SDK_INT < 21) {
            return;
        }
        if (str == null || ReactScrollViewHelper.OVER_SCROLL_NEVER.equals(str)) {
            webView.getSettings().setMixedContentMode(1);
        } else if (ReactScrollViewHelper.OVER_SCROLL_ALWAYS.equals(str)) {
            webView.getSettings().setMixedContentMode(0);
        } else if ("compatibility".equals(str)) {
            webView.getSettings().setMixedContentMode(2);
        }
    }

    @ReactProp(name = "urlPrefixesForDefaultIntent")
    public void setUrlPrefixesForDefaultIntent(WebView webView, @Nullable ReadableArray readableArray) {
        ReactWebViewClient reactWebViewClient = ((ReactWebView) webView).getReactWebViewClient();
        if (reactWebViewClient != null && readableArray != null) {
            reactWebViewClient.setUrlPrefixesForDefaultIntent(readableArray);
        }
    }

    @ReactProp(name = "allowFileAccess")
    public void setAllowFileAccess(WebView webView, @Nullable Boolean bool) {
        webView.getSettings().setAllowFileAccess(bool != null && bool.booleanValue());
    }

    @ReactProp(name = "geolocationEnabled")
    public void setGeolocationEnabled(WebView webView, @Nullable Boolean bool) {
        webView.getSettings().setGeolocationEnabled(bool != null && bool.booleanValue());
    }

    @ReactProp(name = "originWhitelist")
    public void setOriginWhitelist(WebView webView, @Nullable ReadableArray readableArray) {
        ReactWebViewClient reactWebViewClient = ((ReactWebView) webView).getReactWebViewClient();
        if (reactWebViewClient != null && readableArray != null) {
            LinkedList linkedList = new LinkedList();
            for (int i = 0; i < readableArray.size(); i++) {
                linkedList.add(Pattern.compile(readableArray.getString(i)));
            }
            reactWebViewClient.setOriginWhitelist(linkedList);
        }
    }

    /* access modifiers changed from: protected */
    public void addEventEmitters(ThemedReactContext themedReactContext, WebView webView) {
        webView.setWebViewClient(new ReactWebViewClient());
    }

    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.m126of("goBack", Integer.valueOf(1), "goForward", Integer.valueOf(2), "reload", Integer.valueOf(3), "stopLoading", Integer.valueOf(4), "postMessage", Integer.valueOf(5), "injectJavaScript", Integer.valueOf(6));
    }

    public void receiveCommand(WebView webView, int i, @Nullable ReadableArray readableArray) {
        switch (i) {
            case 1:
                webView.goBack();
                return;
            case 2:
                webView.goForward();
                return;
            case 3:
                webView.reload();
                return;
            case 4:
                webView.stopLoading();
                return;
            case 5:
                try {
                    ReactWebView reactWebView = (ReactWebView) webView;
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put(UriUtil.DATA_SCHEME, readableArray.getString(0));
                    StringBuilder sb = new StringBuilder();
                    sb.append("(function () {var event;var data = ");
                    sb.append(jSONObject.toString());
                    sb.append(";try {event = new MessageEvent('message', data);} catch (e) {event = document.createEvent('MessageEvent');event.initMessageEvent('message', true, true, data.data, data.origin, data.lastEventId, data.source);}document.dispatchEvent(event);})();");
                    reactWebView.evaluateJavascriptWithFallback(sb.toString());
                    return;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            case 6:
                ((ReactWebView) webView).evaluateJavascriptWithFallback(readableArray.getString(0));
                return;
            default:
                return;
        }
    }

    public void onDropViewInstance(WebView webView) {
        super.onDropViewInstance(webView);
        ReactWebView reactWebView = (ReactWebView) webView;
        ((ThemedReactContext) webView.getContext()).removeLifecycleEventListener(reactWebView);
        reactWebView.cleanupCallbacksAndDestroy();
    }

    /* access modifiers changed from: protected */
    public PictureListener getPictureListener() {
        if (this.mPictureListener == null) {
            this.mPictureListener = new PictureListener() {
                public void onNewPicture(WebView webView, Picture picture) {
                    ReactWebViewManager.dispatchEvent(webView, new ContentSizeChangeEvent(webView.getId(), webView.getWidth(), webView.getContentHeight()));
                }
            };
        }
        return this.mPictureListener;
    }

    protected static void dispatchEvent(WebView webView, Event event) {
        ((UIManagerModule) ((ReactContext) webView.getContext()).getNativeModule(UIManagerModule.class)).getEventDispatcher().dispatchEvent(event);
    }
}
