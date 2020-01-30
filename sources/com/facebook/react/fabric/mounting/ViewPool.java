package com.facebook.react.fabric.mounting;

import android.support.annotation.UiThread;
import android.view.View;
import com.facebook.react.common.ClearableSynchronizedPool;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerRegistry;
import java.util.HashMap;
import java.util.Map;

public final class ViewPool {
    private static final int POOL_SIZE = 512;
    private final ViewManagerRegistry mViewManagerRegistry;
    private final Map<String, ClearableSynchronizedPool<View>> mViewPool = new HashMap();

    ViewPool(ViewManagerRegistry viewManagerRegistry) {
        this.mViewManagerRegistry = viewManagerRegistry;
    }

    /* access modifiers changed from: 0000 */
    @UiThread
    public void createView(String str, ThemedReactContext themedReactContext) {
        getViewPoolForComponent(str).release(this.mViewManagerRegistry.get(str).createView(themedReactContext, null));
    }

    /* access modifiers changed from: 0000 */
    @UiThread
    public View getOrCreateView(String str, ThemedReactContext themedReactContext) {
        ClearableSynchronizedPool viewPoolForComponent = getViewPoolForComponent(str);
        View view = (View) viewPoolForComponent.acquire();
        if (view != null) {
            return view;
        }
        createView(str, themedReactContext);
        return (View) viewPoolForComponent.acquire();
    }

    /* access modifiers changed from: 0000 */
    @UiThread
    public void returnToPool(String str, View view) {
        ClearableSynchronizedPool clearableSynchronizedPool = (ClearableSynchronizedPool) this.mViewPool.get(str);
        if (clearableSynchronizedPool != null) {
            clearableSynchronizedPool.release(view);
        }
    }

    private ClearableSynchronizedPool<View> getViewPoolForComponent(String str) {
        ClearableSynchronizedPool<View> clearableSynchronizedPool = (ClearableSynchronizedPool) this.mViewPool.get(str);
        if (clearableSynchronizedPool != null) {
            return clearableSynchronizedPool;
        }
        ClearableSynchronizedPool<View> clearableSynchronizedPool2 = new ClearableSynchronizedPool<>(512);
        this.mViewPool.put(str, clearableSynchronizedPool2);
        return clearableSynchronizedPool2;
    }
}
