package com.facebook.react.views.toolbar;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.support.p000v4.view.ViewCompat;
import android.support.p003v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.scroll.ReactScrollViewHelper;
import com.facebook.react.views.toolbar.events.ToolbarClickEvent;
import java.util.Map;
import javax.annotation.Nullable;

public class ReactToolbarManager extends ViewGroupManager<ReactToolbar> {
    private static final int COMMAND_DISMISS_POPUP_MENUS = 1;
    private static final String REACT_CLASS = "ToolbarAndroid";

    public String getName() {
        return REACT_CLASS;
    }

    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    /* access modifiers changed from: protected */
    public ReactToolbar createViewInstance(ThemedReactContext themedReactContext) {
        return new ReactToolbar(themedReactContext);
    }

    @ReactProp(name = "logo")
    public void setLogo(ReactToolbar reactToolbar, @Nullable ReadableMap readableMap) {
        reactToolbar.setLogoSource(readableMap);
    }

    @ReactProp(name = "navIcon")
    public void setNavIcon(ReactToolbar reactToolbar, @Nullable ReadableMap readableMap) {
        reactToolbar.setNavIconSource(readableMap);
    }

    @ReactProp(name = "overflowIcon")
    public void setOverflowIcon(ReactToolbar reactToolbar, @Nullable ReadableMap readableMap) {
        reactToolbar.setOverflowIconSource(readableMap);
    }

    @ReactProp(name = "rtl")
    public void setRtl(ReactToolbar reactToolbar, boolean z) {
        ViewCompat.setLayoutDirection(reactToolbar, z ? 1 : 0);
    }

    @ReactProp(name = "subtitle")
    public void setSubtitle(ReactToolbar reactToolbar, @Nullable String str) {
        reactToolbar.setSubtitle((CharSequence) str);
    }

    @ReactProp(customType = "Color", name = "subtitleColor")
    public void setSubtitleColor(ReactToolbar reactToolbar, @Nullable Integer num) {
        int[] defaultColors = getDefaultColors(reactToolbar.getContext());
        if (num != null) {
            reactToolbar.setSubtitleTextColor(num.intValue());
        } else {
            reactToolbar.setSubtitleTextColor(defaultColors[1]);
        }
    }

    @ReactProp(name = "title")
    public void setTitle(ReactToolbar reactToolbar, @Nullable String str) {
        reactToolbar.setTitle((CharSequence) str);
    }

    @ReactProp(customType = "Color", name = "titleColor")
    public void setTitleColor(ReactToolbar reactToolbar, @Nullable Integer num) {
        int[] defaultColors = getDefaultColors(reactToolbar.getContext());
        if (num != null) {
            reactToolbar.setTitleTextColor(num.intValue());
        } else {
            reactToolbar.setTitleTextColor(defaultColors[0]);
        }
    }

    @ReactProp(defaultFloat = Float.NaN, name = "contentInsetStart")
    public void setContentInsetStart(ReactToolbar reactToolbar, float f) {
        int i;
        if (Float.isNaN(f)) {
            i = getDefaultContentInsets(reactToolbar.getContext())[0];
        } else {
            i = Math.round(PixelUtil.toPixelFromDIP(f));
        }
        reactToolbar.setContentInsetsRelative(i, reactToolbar.getContentInsetEnd());
    }

    @ReactProp(defaultFloat = Float.NaN, name = "contentInsetEnd")
    public void setContentInsetEnd(ReactToolbar reactToolbar, float f) {
        int i;
        if (Float.isNaN(f)) {
            i = getDefaultContentInsets(reactToolbar.getContext())[1];
        } else {
            i = Math.round(PixelUtil.toPixelFromDIP(f));
        }
        reactToolbar.setContentInsetsRelative(reactToolbar.getContentInsetStart(), i);
    }

    @ReactProp(name = "nativeActions")
    public void setActions(ReactToolbar reactToolbar, @Nullable ReadableArray readableArray) {
        reactToolbar.setActions(readableArray);
    }

    /* access modifiers changed from: protected */
    public void addEventEmitters(ThemedReactContext themedReactContext, final ReactToolbar reactToolbar) {
        final EventDispatcher eventDispatcher = ((UIManagerModule) themedReactContext.getNativeModule(UIManagerModule.class)).getEventDispatcher();
        reactToolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                eventDispatcher.dispatchEvent(new ToolbarClickEvent(reactToolbar.getId(), -1));
            }
        });
        reactToolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                eventDispatcher.dispatchEvent(new ToolbarClickEvent(reactToolbar.getId(), menuItem.getOrder()));
                return true;
            }
        });
    }

    @Nullable
    public Map<String, Object> getExportedViewConstants() {
        return MapBuilder.m121of("ShowAsAction", MapBuilder.m123of(ReactScrollViewHelper.OVER_SCROLL_NEVER, Integer.valueOf(0), ReactScrollViewHelper.OVER_SCROLL_ALWAYS, Integer.valueOf(2), "ifRoom", Integer.valueOf(1)));
    }

    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.m121of("dismissPopupMenus", Integer.valueOf(1));
    }

    public void receiveCommand(ReactToolbar reactToolbar, int i, @Nullable ReadableArray readableArray) {
        if (i == 1) {
            reactToolbar.dismissPopupMenus();
        } else {
            throw new IllegalArgumentException(String.format("Unsupported command %d received by %s.", new Object[]{Integer.valueOf(i), getClass().getSimpleName()}));
        }
    }

    private int[] getDefaultContentInsets(Context context) {
        TypedArray typedArray;
        Theme theme = context.getTheme();
        TypedArray typedArray2 = null;
        try {
            typedArray = theme.obtainStyledAttributes(new int[]{getIdentifier(context, "toolbarStyle")});
            try {
                typedArray2 = theme.obtainStyledAttributes(typedArray.getResourceId(0, 0), new int[]{getIdentifier(context, "contentInsetStart"), getIdentifier(context, "contentInsetEnd")});
                int[] iArr = {typedArray2.getDimensionPixelSize(0, 0), typedArray2.getDimensionPixelSize(1, 0)};
                recycleQuietly(typedArray);
                recycleQuietly(typedArray2);
                return iArr;
            } catch (Throwable th) {
                th = th;
                recycleQuietly(typedArray);
                recycleQuietly(typedArray2);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            typedArray = null;
            recycleQuietly(typedArray);
            recycleQuietly(typedArray2);
            throw th;
        }
    }

    private static int[] getDefaultColors(Context context) {
        TypedArray typedArray;
        TypedArray typedArray2;
        TypedArray typedArray3;
        TypedArray obtainStyledAttributes;
        Theme theme = context.getTheme();
        TypedArray typedArray4 = null;
        try {
            typedArray3 = theme.obtainStyledAttributes(new int[]{getIdentifier(context, "toolbarStyle")});
            try {
                obtainStyledAttributes = theme.obtainStyledAttributes(typedArray3.getResourceId(0, 0), new int[]{getIdentifier(context, "titleTextAppearance"), getIdentifier(context, "subtitleTextAppearance")});
            } catch (Throwable th) {
                th = th;
                typedArray = null;
                typedArray2 = null;
                recycleQuietly(typedArray3);
                recycleQuietly(typedArray4);
                recycleQuietly(typedArray2);
                recycleQuietly(typedArray);
                throw th;
            }
            try {
                int resourceId = obtainStyledAttributes.getResourceId(0, 0);
                int resourceId2 = obtainStyledAttributes.getResourceId(1, 0);
                typedArray2 = theme.obtainStyledAttributes(resourceId, new int[]{16842904});
                try {
                    typedArray4 = theme.obtainStyledAttributes(resourceId2, new int[]{16842904});
                    int[] iArr = {typedArray2.getColor(0, ViewCompat.MEASURED_STATE_MASK), typedArray4.getColor(0, ViewCompat.MEASURED_STATE_MASK)};
                    recycleQuietly(typedArray3);
                    recycleQuietly(obtainStyledAttributes);
                    recycleQuietly(typedArray2);
                    recycleQuietly(typedArray4);
                    return iArr;
                } catch (Throwable th2) {
                    th = th2;
                    TypedArray typedArray5 = typedArray4;
                    typedArray4 = obtainStyledAttributes;
                    typedArray = typedArray5;
                    recycleQuietly(typedArray3);
                    recycleQuietly(typedArray4);
                    recycleQuietly(typedArray2);
                    recycleQuietly(typedArray);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                typedArray2 = null;
                typedArray4 = obtainStyledAttributes;
                typedArray = null;
                recycleQuietly(typedArray3);
                recycleQuietly(typedArray4);
                recycleQuietly(typedArray2);
                recycleQuietly(typedArray);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            typedArray = null;
            typedArray3 = null;
            typedArray2 = null;
            recycleQuietly(typedArray3);
            recycleQuietly(typedArray4);
            recycleQuietly(typedArray2);
            recycleQuietly(typedArray);
            throw th;
        }
    }

    private static void recycleQuietly(@Nullable TypedArray typedArray) {
        if (typedArray != null) {
            typedArray.recycle();
        }
    }

    private static int getIdentifier(Context context, String str) {
        return context.getResources().getIdentifier(str, "attr", context.getPackageName());
    }
}
