package com.facebook.react.fabric;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.support.annotation.GuardedBy;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.infer.annotation.ThreadConfined;
import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.GuardedRunnable;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.NativeMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.fabric.jsi.Binding;
import com.facebook.react.fabric.jsi.EventBeatManager;
import com.facebook.react.fabric.jsi.EventEmitterWrapper;
import com.facebook.react.fabric.jsi.FabricSoLoader;
import com.facebook.react.fabric.mounting.LayoutMetricsConversions;
import com.facebook.react.fabric.mounting.MountingManager;
import com.facebook.react.fabric.mounting.mountitems.BatchMountItem;
import com.facebook.react.fabric.mounting.mountitems.CreateMountItem;
import com.facebook.react.fabric.mounting.mountitems.DeleteMountItem;
import com.facebook.react.fabric.mounting.mountitems.DispatchCommandMountItem;
import com.facebook.react.fabric.mounting.mountitems.InsertMountItem;
import com.facebook.react.fabric.mounting.mountitems.MountItem;
import com.facebook.react.fabric.mounting.mountitems.PreAllocateViewMountItem;
import com.facebook.react.fabric.mounting.mountitems.RemoveMountItem;
import com.facebook.react.fabric.mounting.mountitems.UpdateEventEmitterMountItem;
import com.facebook.react.fabric.mounting.mountitems.UpdateLayoutMountItem;
import com.facebook.react.fabric.mounting.mountitems.UpdateLocalDataMountItem;
import com.facebook.react.fabric.mounting.mountitems.UpdatePropsMountItem;
import com.facebook.react.modules.core.ReactChoreographer;
import com.facebook.react.modules.core.ReactChoreographer.CallbackType;
import com.facebook.react.uimanager.ReactRootViewTagGenerator;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerPropertyUpdater;
import com.facebook.react.uimanager.ViewManagerRegistry;
import com.facebook.react.uimanager.common.MeasureSpecProvider;
import com.facebook.react.uimanager.common.SizeMonitoringFrameLayout;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.views.image.ReactImageManager;
import com.facebook.react.views.progressbar.ReactProgressBarViewManager;
import com.facebook.react.views.scroll.ReactScrollViewManager;
import com.facebook.react.views.text.ReactRawTextManager;
import com.facebook.react.views.text.ReactTextViewManager;
import com.facebook.systrace.Systrace;
import com.facebook.yoga.YogaMeasureMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressLint({"MissingNativeLoadLibrary"})
public class FabricUIManager implements UIManager, LifecycleEventListener {
    private static final String TAG = "FabricUIManager";
    private static final Map<String, String> sComponentNames = new HashMap();
    private long mBatchedExecutionTime = 0;
    /* access modifiers changed from: private */
    public Binding mBinding;
    private long mCommitStartTime = 0;
    /* access modifiers changed from: private */
    @ThreadConfined("UI")
    public final DispatchUIFrameCallback mDispatchUIFrameCallback;
    private long mDispatchViewUpdatesTime = 0;
    private final EventBeatManager mEventBeatManager;
    private final EventDispatcher mEventDispatcher;
    private long mFinishTransactionTime = 0;
    /* access modifiers changed from: private */
    @ThreadConfined("UI")
    public boolean mIsMountingEnabled = true;
    private long mLayoutTime = 0;
    @GuardedBy("mMountItemsLock")
    private List<MountItem> mMountItems = new ArrayList();
    private final Object mMountItemsLock = new Object();
    private final MountingManager mMountingManager;
    private long mNonBatchedExecutionTime = 0;
    @GuardedBy("mPreMountItemsLock")
    private List<MountItem> mPreMountItems = new ArrayList();
    private final Object mPreMountItemsLock = new Object();
    private final ReactApplicationContext mReactApplicationContext;
    private final ConcurrentHashMap<Integer, ThemedReactContext> mReactContextForRootTag = new ConcurrentHashMap<>();
    private long mRunStartTime = 0;

    private class DispatchUIFrameCallback extends GuardedFrameCallback {
        private DispatchUIFrameCallback(ReactContext reactContext) {
            super(reactContext);
        }

        public void doFrameGuarded(long j) {
            if (!FabricUIManager.this.mIsMountingEnabled) {
                FLog.m88w(ReactConstants.TAG, "Not flushing pending UI operations because of previously thrown Exception");
                return;
            }
            try {
                FabricUIManager.this.flushMountItems();
                ReactChoreographer.getInstance().postFrameCallback(CallbackType.DISPATCH_UI, FabricUIManager.this.mDispatchUIFrameCallback);
            } catch (Exception e) {
                FLog.m65i(ReactConstants.TAG, "Exception thrown when executing UIFrameGuarded", (Throwable) e);
                FabricUIManager.this.mIsMountingEnabled = false;
                throw e;
            } catch (Throwable th) {
                ReactChoreographer.getInstance().postFrameCallback(CallbackType.DISPATCH_UI, FabricUIManager.this.mDispatchUIFrameCallback);
                throw th;
            }
        }
    }

    public void clearJSResponder() {
    }

    public void onHostDestroy() {
    }

    public void profileNextBatch() {
    }

    public void setJSResponder(int i, boolean z) {
    }

    static {
        FabricSoLoader.staticInit();
        sComponentNames.put("View", "RCTView");
        sComponentNames.put("Image", ReactImageManager.REACT_CLASS);
        sComponentNames.put("ScrollView", ReactScrollViewManager.REACT_CLASS);
        sComponentNames.put("ReactPerformanceLoggerFlag", "ReactPerformanceLoggerFlag");
        sComponentNames.put("Paragraph", ReactTextViewManager.REACT_CLASS);
        sComponentNames.put("Text", "RCText");
        sComponentNames.put("RawText", ReactRawTextManager.REACT_CLASS);
        sComponentNames.put("ActivityIndicatorView", ReactProgressBarViewManager.REACT_CLASS);
        sComponentNames.put("ShimmeringView", "RKShimmeringView");
        sComponentNames.put("TemplateView", "RCTTemplateView");
    }

    public FabricUIManager(ReactApplicationContext reactApplicationContext, ViewManagerRegistry viewManagerRegistry, EventDispatcher eventDispatcher, EventBeatManager eventBeatManager) {
        this.mDispatchUIFrameCallback = new DispatchUIFrameCallback(reactApplicationContext);
        this.mReactApplicationContext = reactApplicationContext;
        this.mMountingManager = new MountingManager(viewManagerRegistry);
        this.mEventDispatcher = eventDispatcher;
        this.mEventBeatManager = eventBeatManager;
        this.mReactApplicationContext.addLifecycleEventListener(this);
    }

    public <T extends SizeMonitoringFrameLayout & MeasureSpecProvider> int addRootView(T t, WritableMap writableMap, @Nullable String str) {
        int nextRootViewTag = ReactRootViewTagGenerator.getNextRootViewTag();
        ThemedReactContext themedReactContext = new ThemedReactContext(this.mReactApplicationContext, t.getContext());
        this.mMountingManager.addRootView(nextRootViewTag, t);
        this.mReactContextForRootTag.put(Integer.valueOf(nextRootViewTag), themedReactContext);
        this.mBinding.startSurface(nextRootViewTag, (NativeMap) writableMap);
        MeasureSpecProvider measureSpecProvider = (MeasureSpecProvider) t;
        updateRootLayoutSpecs(nextRootViewTag, measureSpecProvider.getWidthMeasureSpec(), measureSpecProvider.getHeightMeasureSpec());
        if (str != null) {
            this.mBinding.renderTemplateToSurface(nextRootViewTag, str);
        }
        return nextRootViewTag;
    }

    @DoNotStrip
    public void onRequestEventBeat() {
        this.mEventDispatcher.dispatchAllEvents();
    }

    public void removeRootView(int i) {
        this.mMountingManager.removeRootView(i);
        this.mReactContextForRootTag.remove(Integer.valueOf(i));
    }

    @DoNotStrip
    private MountItem createMountItem(String str, int i, int i2, boolean z) {
        String str2 = (String) sComponentNames.get(str);
        if (str2 != null) {
            ThemedReactContext themedReactContext = (ThemedReactContext) this.mReactContextForRootTag.get(Integer.valueOf(i));
            if (themedReactContext != null) {
                return new CreateMountItem(themedReactContext, str2, i2, z);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find ReactContext for root: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unable to find component with name ");
        sb2.append(str);
        throw new IllegalArgumentException(sb2.toString());
    }

    public void initialize() {
        this.mEventDispatcher.registerEventEmitter(2, new FabricEventEmitter(this));
        this.mEventDispatcher.addBatchEventDispatchedListener(this.mEventBeatManager);
    }

    public void onCatalystInstanceDestroy() {
        this.mEventDispatcher.removeBatchEventDispatchedListener(this.mEventBeatManager);
        this.mEventDispatcher.unregisterEventEmitter(2);
        this.mBinding.unregister();
        ViewManagerPropertyUpdater.clear();
    }

    @DoNotStrip
    private void preallocateView(int i, String str) {
        if (!UiThreadUtil.isOnUiThread()) {
            synchronized (this.mPreMountItemsLock) {
                ThemedReactContext themedReactContext = (ThemedReactContext) Assertions.assertNotNull(this.mReactContextForRootTag.get(Integer.valueOf(i)));
                String str2 = (String) sComponentNames.get(str);
                Assertions.assertNotNull(str2);
                this.mPreMountItems.add(new PreAllocateViewMountItem(themedReactContext, i, str2));
            }
        }
    }

    @DoNotStrip
    private MountItem removeMountItem(int i, int i2, int i3) {
        return new RemoveMountItem(i, i2, i3);
    }

    @DoNotStrip
    private MountItem insertMountItem(int i, int i2, int i3) {
        return new InsertMountItem(i, i2, i3);
    }

    @DoNotStrip
    private MountItem deleteMountItem(int i) {
        return new DeleteMountItem(i);
    }

    @DoNotStrip
    private MountItem updateLayoutMountItem(int i, int i2, int i3, int i4, int i5) {
        UpdateLayoutMountItem updateLayoutMountItem = new UpdateLayoutMountItem(i, i2, i3, i4, i5);
        return updateLayoutMountItem;
    }

    @DoNotStrip
    private MountItem updatePropsMountItem(int i, ReadableNativeMap readableNativeMap) {
        return new UpdatePropsMountItem(i, readableNativeMap);
    }

    @DoNotStrip
    private MountItem updateLocalDataMountItem(int i, ReadableNativeMap readableNativeMap) {
        return new UpdateLocalDataMountItem(i, readableNativeMap);
    }

    @DoNotStrip
    private MountItem updateEventEmitterMountItem(int i, Object obj) {
        return new UpdateEventEmitterMountItem(i, (EventEmitterWrapper) obj);
    }

    @DoNotStrip
    private MountItem createBatchMountItem(MountItem[] mountItemArr, int i) {
        return new BatchMountItem(mountItemArr, i);
    }

    @DoNotStrip
    private long measure(String str, ReadableNativeMap readableNativeMap, ReadableNativeMap readableNativeMap2, int i, int i2, int i3, int i4) {
        MountingManager mountingManager = this.mMountingManager;
        ReactApplicationContext reactApplicationContext = this.mReactApplicationContext;
        float f = (float) i;
        float f2 = (float) i2;
        float yogaSize = LayoutMetricsConversions.getYogaSize(f, f2);
        YogaMeasureMode yogaMeasureMode = LayoutMetricsConversions.getYogaMeasureMode(f, f2);
        float f3 = (float) i3;
        float f4 = (float) i4;
        return mountingManager.measure(reactApplicationContext, str, readableNativeMap, readableNativeMap2, yogaSize, yogaMeasureMode, LayoutMetricsConversions.getYogaSize(f3, f4), LayoutMetricsConversions.getYogaMeasureMode(f3, f4));
    }

    @DoNotStrip
    private void scheduleMountItems(MountItem mountItem, long j, long j2, long j3) {
        this.mCommitStartTime = j;
        this.mLayoutTime = j2;
        this.mFinishTransactionTime = SystemClock.uptimeMillis() - j3;
        this.mDispatchViewUpdatesTime = SystemClock.uptimeMillis();
        synchronized (this.mMountItemsLock) {
            this.mMountItems.add(mountItem);
        }
        if (UiThreadUtil.isOnUiThread()) {
            flushMountItems();
        }
    }

    /* access modifiers changed from: private */
    @UiThread
    public void flushMountItems() {
        List<MountItem> list;
        List<MountItem> list2;
        if (!this.mIsMountingEnabled) {
            FLog.m88w(ReactConstants.TAG, "Not flushing pending UI operations because of previously thrown Exception");
            return;
        }
        try {
            synchronized (this.mPreMountItemsLock) {
                list = this.mPreMountItems;
                this.mPreMountItems = new ArrayList();
            }
            this.mRunStartTime = SystemClock.uptimeMillis();
            synchronized (this.mMountItemsLock) {
                list2 = this.mMountItems;
                this.mMountItems = new ArrayList();
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append("FabricUIManager::premountViews (");
            sb.append(list.size());
            sb.append(" batches)");
            Systrace.beginSection(0, sb.toString());
            for (MountItem execute : list) {
                execute.execute(this.mMountingManager);
            }
            this.mNonBatchedExecutionTime = SystemClock.uptimeMillis() - uptimeMillis;
            Systrace.endSection(0);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("FabricUIManager::mountViews (");
            sb2.append(list2.size());
            sb2.append(" batches)");
            Systrace.beginSection(0, sb2.toString());
            long uptimeMillis2 = SystemClock.uptimeMillis();
            for (MountItem execute2 : list2) {
                execute2.execute(this.mMountingManager);
            }
            this.mBatchedExecutionTime = SystemClock.uptimeMillis() - uptimeMillis2;
            Systrace.endSection(0);
        } catch (Exception e) {
            FLog.m49e(ReactConstants.TAG, "Exception thrown when executing UIFrameGuarded", (Throwable) e);
            this.mIsMountingEnabled = false;
            throw e;
        }
    }

    public void setBinding(Binding binding) {
        this.mBinding = binding;
    }

    public void updateRootLayoutSpecs(int i, int i2, int i3) {
        ReactApplicationContext reactApplicationContext = this.mReactApplicationContext;
        final int i4 = i;
        final int i5 = i2;
        final int i6 = i3;
        C09371 r0 = new GuardedRunnable(reactApplicationContext) {
            public void runGuarded() {
                FabricUIManager.this.mBinding.setConstraints(i4, LayoutMetricsConversions.getMinSize(i5), LayoutMetricsConversions.getMaxSize(i5), LayoutMetricsConversions.getMinSize(i6), LayoutMetricsConversions.getMaxSize(i6));
            }
        };
        reactApplicationContext.runOnJSQueueThread(r0);
    }

    public void receiveEvent(int i, String str, @Nullable WritableMap writableMap) {
        EventEmitterWrapper eventEmitter = this.mMountingManager.getEventEmitter(i);
        if (eventEmitter == null) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to invoke event: ");
            sb.append(str);
            sb.append(" for reactTag: ");
            sb.append(i);
            FLog.m36d(str2, sb.toString());
            return;
        }
        eventEmitter.invoke(str, writableMap);
    }

    public void onHostResume() {
        ReactChoreographer.getInstance().postFrameCallback(CallbackType.DISPATCH_UI, this.mDispatchUIFrameCallback);
    }

    public void onHostPause() {
        ReactChoreographer.getInstance().removeFrameCallback(CallbackType.DISPATCH_UI, this.mDispatchUIFrameCallback);
    }

    public void dispatchCommand(int i, int i2, ReadableArray readableArray) {
        synchronized (this.mMountItemsLock) {
            this.mMountItems.add(new DispatchCommandMountItem(i, i2, readableArray));
        }
    }

    public Map<String, Long> getPerformanceCounters() {
        HashMap hashMap = new HashMap();
        hashMap.put("CommitStartTime", Long.valueOf(this.mCommitStartTime));
        hashMap.put("LayoutTime", Long.valueOf(this.mLayoutTime));
        hashMap.put("DispatchViewUpdatesTime", Long.valueOf(this.mDispatchViewUpdatesTime));
        hashMap.put("RunStartTime", Long.valueOf(this.mRunStartTime));
        hashMap.put("BatchedExecutionTime", Long.valueOf(this.mBatchedExecutionTime));
        hashMap.put("NonBatchedExecutionTime", Long.valueOf(this.mNonBatchedExecutionTime));
        hashMap.put("FinishFabricTransactionTime", Long.valueOf(this.mFinishTransactionTime));
        return hashMap;
    }
}
