package com.azendoo.reactnativesnackbar;

import android.os.Build.VERSION;
import android.support.design.C0043R;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ViewProps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SnackbarModule extends ReactContextBaseJavaModule {
    private static final String REACT_NAME = "RNSnackbar";
    private List<Snackbar> mActiveSnackbars = new ArrayList();

    public String getName() {
        return REACT_NAME;
    }

    public SnackbarModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    public Map<String, Object> getConstants() {
        HashMap hashMap = new HashMap();
        hashMap.put("LENGTH_LONG", Integer.valueOf(0));
        hashMap.put("LENGTH_SHORT", Integer.valueOf(-1));
        hashMap.put("LENGTH_INDEFINITE", Integer.valueOf(-2));
        return hashMap;
    }

    @ReactMethod
    public void show(ReadableMap readableMap, Callback callback) {
        try {
            ViewGroup viewGroup = (ViewGroup) getCurrentActivity().getWindow().getDecorView().findViewById(16908290);
            if (viewGroup != null) {
                this.mActiveSnackbars.clear();
                if (!viewGroup.hasWindowFocus()) {
                    Iterator it = recursiveLoopChildren(viewGroup, new ArrayList()).iterator();
                    while (it.hasNext()) {
                        View view = (View) it.next();
                        if (view != null) {
                            displaySnackbar(view, readableMap, callback);
                        }
                    }
                    return;
                }
                displaySnackbar(viewGroup, readableMap, callback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void dismiss() {
        for (Snackbar snackbar : this.mActiveSnackbars) {
            if (snackbar != null) {
                snackbar.dismiss();
            }
        }
        this.mActiveSnackbars.clear();
    }

    private void displaySnackbar(View view, ReadableMap readableMap, final Callback callback) {
        Snackbar make = Snackbar.make(view, (CharSequence) readableMap.hasKey("title") ? readableMap.getString("title") : "", readableMap.hasKey("duration") ? readableMap.getInt("duration") : -1);
        View view2 = make.getView();
        TextView textView = (TextView) view2.findViewById(C0043R.C0047id.snackbar_text);
        this.mActiveSnackbars.add(make);
        if (readableMap.hasKey(ViewProps.BACKGROUND_COLOR)) {
            view2.setBackgroundColor(readableMap.getInt(ViewProps.BACKGROUND_COLOR));
        }
        if (readableMap.hasKey("action")) {
            C06271 r0 = new OnClickListener() {
                boolean callbackWasCalled = false;

                public void onClick(View view) {
                    if (!this.callbackWasCalled) {
                        this.callbackWasCalled = true;
                        callback.invoke(new Object[0]);
                    }
                }
            };
            ReadableMap map = readableMap.getMap("action");
            make.setAction((CharSequence) map.getString("title"), (OnClickListener) r0);
            make.setActionTextColor(map.getInt(ViewProps.COLOR));
        }
        if (readableMap.hasKey(ViewProps.COLOR)) {
            textView.setTextColor(readableMap.getInt(ViewProps.COLOR));
        } else if (VERSION.SDK_INT < 21) {
            textView.setTextColor(-1);
        }
        make.show();
    }

    private ArrayList<View> recursiveLoopChildren(ViewGroup viewGroup, ArrayList<View> arrayList) {
        if (viewGroup.getClass().getSimpleName().equalsIgnoreCase("ReactModalHostView")) {
            arrayList.add(viewGroup.getChildAt(0));
        }
        for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = viewGroup.getChildAt(childCount);
            if (childAt instanceof ViewGroup) {
                recursiveLoopChildren((ViewGroup) childAt, arrayList);
            }
        }
        return arrayList;
    }
}
