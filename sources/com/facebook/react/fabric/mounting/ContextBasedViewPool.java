package com.facebook.react.fabric.mounting;

import android.support.annotation.UiThread;
import android.view.View;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerRegistry;
import java.util.WeakHashMap;

public final class ContextBasedViewPool {
    private final WeakHashMap<ThemedReactContext, ViewPool> mContextViewPoolHashMap = new WeakHashMap<>();
    private final ViewManagerRegistry mViewManagerRegistry;

    ContextBasedViewPool(ViewManagerRegistry viewManagerRegistry) {
        this.mViewManagerRegistry = viewManagerRegistry;
    }

    /* access modifiers changed from: 0000 */
    @UiThread
    public void createView(ThemedReactContext themedReactContext, String str) {
        UiThreadUtil.assertOnUiThread();
        getViewPool(themedReactContext).createView(str, themedReactContext);
    }

    /* access modifiers changed from: 0000 */
    @UiThread
    public View getOrCreateView(String str, ThemedReactContext themedReactContext) {
        UiThreadUtil.assertOnUiThread();
        return getViewPool(themedReactContext).getOrCreateView(str, themedReactContext);
    }

    /* access modifiers changed from: 0000 */
    @UiThread
    public void returnToPool(ThemedReactContext themedReactContext, String str, View view) {
        UiThreadUtil.assertOnUiThread();
        getViewPool(themedReactContext).returnToPool(str, view);
    }

    @UiThread
    private ViewPool getViewPool(ThemedReactContext themedReactContext) {
        ViewPool viewPool = (ViewPool) this.mContextViewPoolHashMap.get(themedReactContext);
        if (viewPool != null) {
            return viewPool;
        }
        ViewPool viewPool2 = new ViewPool(this.mViewManagerRegistry);
        this.mContextViewPoolHashMap.put(themedReactContext, viewPool2);
        return viewPool2;
    }
}
