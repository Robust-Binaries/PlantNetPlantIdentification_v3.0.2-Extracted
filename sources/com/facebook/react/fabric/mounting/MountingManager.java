package com.facebook.react.fabric.mounting;

import android.support.annotation.AnyThread;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.SoftAssertions;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.fabric.jsi.EventEmitterWrapper;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.RootView;
import com.facebook.react.uimanager.RootViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.uimanager.ViewManagerRegistry;
import com.facebook.react.uimanager.common.SizeMonitoringFrameLayout;
import com.facebook.yoga.YogaMeasureMode;
import java.util.concurrent.ConcurrentHashMap;

public class MountingManager {
    private final RootViewManager mRootViewManager = new RootViewManager();
    private final ConcurrentHashMap<Integer, ViewState> mTagToViewState = new ConcurrentHashMap<>();
    private final ViewManagerRegistry mViewManagerRegistry;
    private final ContextBasedViewPool mViewPool;

    private static class ViewState {
        public ReadableMap mCurrentLocalData;
        public ReactStylesDiffMap mCurrentProps;
        public EventEmitterWrapper mEventEmitter;
        final boolean mIsRoot;
        final int mReactTag;
        @Nullable
        final View mView;
        @Nullable
        final ViewManager mViewManager;

        private ViewState(int i, @Nullable View view, @Nullable ViewManager viewManager) {
            this(i, view, viewManager, false);
        }

        private ViewState(int i, @Nullable View view, ViewManager viewManager, boolean z) {
            this.mReactTag = i;
            this.mView = view;
            this.mIsRoot = z;
            this.mViewManager = viewManager;
        }
    }

    public MountingManager(ViewManagerRegistry viewManagerRegistry) {
        this.mViewManagerRegistry = viewManagerRegistry;
        this.mViewPool = new ContextBasedViewPool(viewManagerRegistry);
    }

    @UiThread
    public void addRootView(int i, SizeMonitoringFrameLayout sizeMonitoringFrameLayout) {
        if (sizeMonitoringFrameLayout.getId() == -1) {
            ConcurrentHashMap<Integer, ViewState> concurrentHashMap = this.mTagToViewState;
            Integer valueOf = Integer.valueOf(i);
            ViewState viewState = new ViewState(i, sizeMonitoringFrameLayout, this.mRootViewManager, true);
            concurrentHashMap.put(valueOf, viewState);
            sizeMonitoringFrameLayout.setId(i);
            return;
        }
        throw new IllegalViewOperationException("Trying to add a root view with an explicit id already set. React Native uses the id field to track react tags and will overwrite this field. If that is fine, explicitly overwrite the id field to View.NO_ID before calling addRootView.");
    }

    @UiThread
    private void dropView(View view) {
        UiThreadUtil.assertOnUiThread();
        int id = view.getId();
        ViewState viewState = getViewState(id);
        ViewManager viewManager = viewState.mViewManager;
        if (!viewState.mIsRoot && viewManager != null) {
            viewManager.onDropViewInstance(view);
        }
        if ((view instanceof ViewGroup) && (viewManager instanceof ViewGroupManager)) {
            ViewGroup viewGroup = (ViewGroup) view;
            ViewGroupManager viewGroupManager = getViewGroupManager(viewState);
            for (int childCount = viewGroupManager.getChildCount(viewGroup) - 1; childCount >= 0; childCount--) {
                View childAt = viewGroupManager.getChildAt(viewGroup, childCount);
                if (this.mTagToViewState.get(Integer.valueOf(childAt.getId())) != null) {
                    dropView(childAt);
                }
                viewGroupManager.removeViewAt(viewGroup, childCount);
            }
        }
        this.mTagToViewState.remove(Integer.valueOf(id));
        this.mViewPool.returnToPool((ThemedReactContext) view.getContext(), ((ViewManager) Assertions.assertNotNull(viewManager)).getName(), view);
    }

    @UiThread
    public void removeRootView(int i) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = (ViewState) this.mTagToViewState.get(Integer.valueOf(i));
        if (viewState == null || !viewState.mIsRoot) {
            StringBuilder sb = new StringBuilder();
            sb.append("View with tag ");
            sb.append(i);
            sb.append(" is not registered as a root view");
            SoftAssertions.assertUnreachable(sb.toString());
        }
        if (viewState.mView != null) {
            dropView(viewState.mView);
        }
    }

    @UiThread
    public void addViewAt(int i, int i2, int i3) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = getViewState(i);
        ViewGroup viewGroup = (ViewGroup) viewState.mView;
        View view = getViewState(i2).mView;
        if (view != null) {
            getViewGroupManager(viewState).addView(viewGroup, view, i3);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find view for tag ");
        sb.append(i2);
        throw new IllegalStateException(sb.toString());
    }

    private ViewState getViewState(int i) {
        ViewState viewState = (ViewState) this.mTagToViewState.get(Integer.valueOf(i));
        if (viewState != null) {
            return viewState;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find viewState for tag ");
        sb.append(i);
        throw new IllegalStateException(sb.toString());
    }

    public void receiveCommand(int i, int i2, @Nullable ReadableArray readableArray) {
        ViewState viewState = getViewState(i);
        if (viewState.mViewManager == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find viewState manager for tag ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        } else if (viewState.mView != null) {
            viewState.mViewManager.receiveCommand(viewState.mView, i2, readableArray);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to find viewState view for tag ");
            sb2.append(i);
            throw new IllegalStateException(sb2.toString());
        }
    }

    private static ViewGroupManager<ViewGroup> getViewGroupManager(ViewState viewState) {
        if (viewState.mViewManager != null) {
            return (ViewGroupManager) viewState.mViewManager;
        }
        throw new IllegalStateException("Unable to find ViewManager");
    }

    @UiThread
    public void removeViewAt(int i, int i2) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = getViewState(i);
        ViewGroup viewGroup = (ViewGroup) viewState.mView;
        if (viewGroup != null) {
            getViewGroupManager(viewState).removeViewAt(viewGroup, i2);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find view for tag ");
        sb.append(i);
        throw new IllegalStateException(sb.toString());
    }

    @UiThread
    public void createView(ThemedReactContext themedReactContext, String str, int i, boolean z) {
        ViewManager viewManager;
        View view;
        UiThreadUtil.assertOnUiThread();
        if (!z) {
            viewManager = this.mViewManagerRegistry.get(str);
            view = this.mViewPool.getOrCreateView(str, themedReactContext);
            view.setId(i);
        } else {
            view = null;
            viewManager = null;
        }
        this.mTagToViewState.put(Integer.valueOf(i), new ViewState(i, view, viewManager));
    }

    @UiThread
    public void updateProps(int i, ReadableMap readableMap) {
        if (readableMap != null) {
            UiThreadUtil.assertOnUiThread();
            ViewState viewState = getViewState(i);
            viewState.mCurrentProps = new ReactStylesDiffMap(readableMap);
            View view = viewState.mView;
            if (view != null) {
                ((ViewManager) Assertions.assertNotNull(viewState.mViewManager)).updateProperties(view, viewState.mCurrentProps);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find view for tag ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        }
    }

    @UiThread
    public void updateLayout(int i, int i2, int i3, int i4, int i5) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = getViewState(i);
        if (!viewState.mIsRoot) {
            View view = viewState.mView;
            if (view != null) {
                view.measure(MeasureSpec.makeMeasureSpec(i4, 1073741824), MeasureSpec.makeMeasureSpec(i5, 1073741824));
                ViewParent parent = view.getParent();
                if (parent instanceof RootView) {
                    parent.requestLayout();
                }
                view.layout(i2, i3, i4 + i2, i5 + i3);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find View for tag: ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        }
    }

    @UiThread
    public void deleteView(int i) {
        UiThreadUtil.assertOnUiThread();
        View view = getViewState(i).mView;
        if (view != null) {
            dropView(view);
        } else {
            this.mTagToViewState.remove(Integer.valueOf(i));
        }
    }

    @UiThread
    public void updateLocalData(int i, ReadableMap readableMap) {
        UiThreadUtil.assertOnUiThread();
        ViewState viewState = getViewState(i);
        if (viewState.mCurrentProps == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can not update local data to view without props: ");
            sb.append(i);
            throw new IllegalStateException(sb.toString());
        } else if (viewState.mCurrentLocalData == null || !readableMap.hasKey("hash") || viewState.mCurrentLocalData.getDouble("hash") != readableMap.getDouble("hash") || !viewState.mCurrentLocalData.toString().equals(readableMap.toString())) {
            viewState.mCurrentLocalData = readableMap;
            ViewManager viewManager = viewState.mViewManager;
            if (viewManager != null) {
                Object updateLocalData = viewManager.updateLocalData(viewState.mView, viewState.mCurrentProps, new ReactStylesDiffMap(viewState.mCurrentLocalData));
                if (updateLocalData != null) {
                    viewManager.updateExtraData(viewState.mView, updateLocalData);
                }
                return;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to find ViewManager for tag: ");
            sb2.append(i);
            throw new IllegalStateException(sb2.toString());
        }
    }

    @UiThread
    public void preallocateView(ThemedReactContext themedReactContext, String str) {
        this.mViewPool.createView(themedReactContext, str);
    }

    @UiThread
    public void updateEventEmitter(int i, EventEmitterWrapper eventEmitterWrapper) {
        UiThreadUtil.assertOnUiThread();
        getViewState(i).mEventEmitter = eventEmitterWrapper;
    }

    @AnyThread
    public long measure(ReactContext reactContext, String str, ReadableNativeMap readableNativeMap, ReadableNativeMap readableNativeMap2, float f, YogaMeasureMode yogaMeasureMode, float f2, YogaMeasureMode yogaMeasureMode2) {
        String str2 = str;
        return this.mViewManagerRegistry.get(str).measure(reactContext, readableNativeMap, readableNativeMap2, f, yogaMeasureMode, f2, yogaMeasureMode2);
    }

    @Nullable
    @AnyThread
    public EventEmitterWrapper getEventEmitter(int i) {
        ViewState viewState = (ViewState) this.mTagToViewState.get(Integer.valueOf(i));
        if (viewState == null) {
            return null;
        }
        return viewState.mEventEmitter;
    }
}
