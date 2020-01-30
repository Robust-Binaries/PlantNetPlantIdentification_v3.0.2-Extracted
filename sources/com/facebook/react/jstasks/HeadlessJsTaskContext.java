package com.facebook.react.jstasks;

import android.os.Handler;
import android.util.SparseArray;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.appregistry.AppRegistry;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

public class HeadlessJsTaskContext {
    private static final WeakHashMap<ReactContext, HeadlessJsTaskContext> INSTANCES = new WeakHashMap<>();
    private final Set<Integer> mActiveTasks = new CopyOnWriteArraySet();
    private final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public final Set<HeadlessJsTaskEventListener> mHeadlessJsTaskEventListeners = new CopyOnWriteArraySet();
    private final AtomicInteger mLastTaskId = new AtomicInteger(0);
    private final WeakReference<ReactContext> mReactContext;
    private final SparseArray<Runnable> mTaskTimeouts = new SparseArray<>();

    public static HeadlessJsTaskContext getInstance(ReactContext reactContext) {
        HeadlessJsTaskContext headlessJsTaskContext = (HeadlessJsTaskContext) INSTANCES.get(reactContext);
        if (headlessJsTaskContext != null) {
            return headlessJsTaskContext;
        }
        HeadlessJsTaskContext headlessJsTaskContext2 = new HeadlessJsTaskContext(reactContext);
        INSTANCES.put(reactContext, headlessJsTaskContext2);
        return headlessJsTaskContext2;
    }

    private HeadlessJsTaskContext(ReactContext reactContext) {
        this.mReactContext = new WeakReference<>(reactContext);
    }

    public void addTaskEventListener(HeadlessJsTaskEventListener headlessJsTaskEventListener) {
        this.mHeadlessJsTaskEventListeners.add(headlessJsTaskEventListener);
    }

    public void removeTaskEventListener(HeadlessJsTaskEventListener headlessJsTaskEventListener) {
        this.mHeadlessJsTaskEventListeners.remove(headlessJsTaskEventListener);
    }

    public boolean hasActiveTasks() {
        return this.mActiveTasks.size() > 0;
    }

    public synchronized int startTask(HeadlessJsTaskConfig headlessJsTaskConfig) {
        int incrementAndGet;
        UiThreadUtil.assertOnUiThread();
        ReactContext reactContext = (ReactContext) Assertions.assertNotNull(this.mReactContext.get(), "Tried to start a task on a react context that has already been destroyed");
        if (reactContext.getLifecycleState() == LifecycleState.RESUMED) {
            if (!headlessJsTaskConfig.isAllowedInForeground()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Tried to start task ");
                sb.append(headlessJsTaskConfig.getTaskKey());
                sb.append(" while in foreground, but this is not allowed.");
                throw new IllegalStateException(sb.toString());
            }
        }
        incrementAndGet = this.mLastTaskId.incrementAndGet();
        this.mActiveTasks.add(Integer.valueOf(incrementAndGet));
        ((AppRegistry) reactContext.getJSModule(AppRegistry.class)).startHeadlessTask(incrementAndGet, headlessJsTaskConfig.getTaskKey(), headlessJsTaskConfig.getData());
        if (headlessJsTaskConfig.getTimeout() > 0) {
            scheduleTaskTimeout(incrementAndGet, headlessJsTaskConfig.getTimeout());
        }
        for (HeadlessJsTaskEventListener onHeadlessJsTaskStart : this.mHeadlessJsTaskEventListeners) {
            onHeadlessJsTaskStart.onHeadlessJsTaskStart(incrementAndGet);
        }
        return incrementAndGet;
    }

    public synchronized void finishTask(final int i) {
        boolean remove = this.mActiveTasks.remove(Integer.valueOf(i));
        StringBuilder sb = new StringBuilder();
        sb.append("Tried to finish non-existent task with id ");
        sb.append(i);
        sb.append(".");
        Assertions.assertCondition(remove, sb.toString());
        Runnable runnable = (Runnable) this.mTaskTimeouts.get(i);
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
            this.mTaskTimeouts.remove(i);
        }
        UiThreadUtil.runOnUiThread(new Runnable() {
            public void run() {
                for (HeadlessJsTaskEventListener onHeadlessJsTaskFinish : HeadlessJsTaskContext.this.mHeadlessJsTaskEventListeners) {
                    onHeadlessJsTaskFinish.onHeadlessJsTaskFinish(i);
                }
            }
        });
    }

    public synchronized boolean isTaskRunning(int i) {
        return this.mActiveTasks.contains(Integer.valueOf(i));
    }

    private void scheduleTaskTimeout(final int i, long j) {
        C09412 r0 = new Runnable() {
            public void run() {
                HeadlessJsTaskContext.this.finishTask(i);
            }
        };
        this.mTaskTimeouts.append(i, r0);
        this.mHandler.postDelayed(r0, j);
    }
}
