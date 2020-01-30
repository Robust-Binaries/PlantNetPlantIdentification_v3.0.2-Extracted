package com.facebook.react.uimanager;

import android.content.res.Resources;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.PopupMenu.OnMenuItemClickListener;
import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.C0788R;
import com.facebook.react.animation.Animation;
import com.facebook.react.animation.AnimationListener;
import com.facebook.react.animation.AnimationRegistry;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.SoftAssertions;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.touch.JSResponderHandler;
import com.facebook.react.uimanager.common.SizeMonitoringFrameLayout;
import com.facebook.react.uimanager.layoutanimation.LayoutAnimationController;
import com.facebook.systrace.Systrace;
import com.facebook.systrace.SystraceMessage;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class NativeViewHierarchyManager {
    private static final String TAG = "NativeViewHierarchyManager";
    /* access modifiers changed from: private */
    public final AnimationRegistry mAnimationRegistry;
    private final JSResponderHandler mJSResponderHandler;
    private boolean mLayoutAnimationEnabled;
    private final LayoutAnimationController mLayoutAnimator;
    private PopupMenu mPopupMenu;
    private final SparseBooleanArray mRootTags;
    private final RootViewManager mRootViewManager;
    private final SparseArray<ViewManager> mTagsToViewManagers;
    private final SparseArray<View> mTagsToViews;
    private final ViewManagerRegistry mViewManagers;

    private static class PopupMenuCallbackHandler implements OnMenuItemClickListener, OnDismissListener {
        boolean mConsumed;
        final Callback mSuccess;

        private PopupMenuCallbackHandler(Callback callback) {
            this.mConsumed = false;
            this.mSuccess = callback;
        }

        public void onDismiss(PopupMenu popupMenu) {
            if (!this.mConsumed) {
                this.mSuccess.invoke(UIManagerModuleConstants.ACTION_DISMISSED);
                this.mConsumed = true;
            }
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            if (this.mConsumed) {
                return false;
            }
            this.mSuccess.invoke(UIManagerModuleConstants.ACTION_ITEM_SELECTED, Integer.valueOf(menuItem.getOrder()));
            this.mConsumed = true;
            return true;
        }
    }

    public NativeViewHierarchyManager(ViewManagerRegistry viewManagerRegistry) {
        this(viewManagerRegistry, new RootViewManager());
    }

    public NativeViewHierarchyManager(ViewManagerRegistry viewManagerRegistry, RootViewManager rootViewManager) {
        this.mJSResponderHandler = new JSResponderHandler();
        this.mLayoutAnimator = new LayoutAnimationController();
        this.mAnimationRegistry = new AnimationRegistry();
        this.mViewManagers = viewManagerRegistry;
        this.mTagsToViews = new SparseArray<>();
        this.mTagsToViewManagers = new SparseArray<>();
        this.mRootTags = new SparseBooleanArray();
        this.mRootViewManager = rootViewManager;
    }

    public final synchronized View resolveView(int i) {
        View view;
        view = (View) this.mTagsToViews.get(i);
        if (view == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Trying to resolve view with tag ");
            sb.append(i);
            sb.append(" which doesn't exist");
            throw new IllegalViewOperationException(sb.toString());
        }
        return view;
    }

    public final synchronized ViewManager resolveViewManager(int i) {
        ViewManager viewManager;
        viewManager = (ViewManager) this.mTagsToViewManagers.get(i);
        if (viewManager == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("ViewManager for tag ");
            sb.append(i);
            sb.append(" could not be found");
            throw new IllegalViewOperationException(sb.toString());
        }
        return viewManager;
    }

    public AnimationRegistry getAnimationRegistry() {
        return this.mAnimationRegistry;
    }

    public void setLayoutAnimationEnabled(boolean z) {
        this.mLayoutAnimationEnabled = z;
    }

    public synchronized void updateInstanceHandle(int i, long j) {
        UiThreadUtil.assertOnUiThread();
        try {
            updateInstanceHandle(resolveView(i), j);
        } catch (IllegalViewOperationException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to update properties for view tag ");
            sb.append(i);
            FLog.m49e(str, sb.toString(), (Throwable) e);
        }
        return;
    }

    public synchronized void updateProperties(int i, ReactStylesDiffMap reactStylesDiffMap) {
        UiThreadUtil.assertOnUiThread();
        try {
            ViewManager resolveViewManager = resolveViewManager(i);
            View resolveView = resolveView(i);
            if (reactStylesDiffMap != null) {
                resolveViewManager.updateProperties(resolveView, reactStylesDiffMap);
            }
        } catch (IllegalViewOperationException e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to update properties for view tag ");
            sb.append(i);
            FLog.m49e(str, sb.toString(), (Throwable) e);
        }
        return;
    }

    public synchronized void updateViewExtraData(int i, Object obj) {
        UiThreadUtil.assertOnUiThread();
        resolveViewManager(i).updateExtraData(resolveView(i), obj);
    }

    public synchronized void updateLayout(int i, int i2, int i3, int i4, int i5, int i6) {
        UiThreadUtil.assertOnUiThread();
        SystraceMessage.beginSection(0, "NativeViewHierarchyManager_updateLayout").arg("parentTag", i).arg("tag", i2).flush();
        try {
            View resolveView = resolveView(i2);
            resolveView.measure(MeasureSpec.makeMeasureSpec(i5, 1073741824), MeasureSpec.makeMeasureSpec(i6, 1073741824));
            ViewParent parent = resolveView.getParent();
            if (parent instanceof RootView) {
                parent.requestLayout();
            }
            if (!this.mRootTags.get(i)) {
                ViewManager viewManager = (ViewManager) this.mTagsToViewManagers.get(i);
                if (viewManager instanceof ViewGroupManager) {
                    ViewGroupManager viewGroupManager = (ViewGroupManager) viewManager;
                    if (viewGroupManager != null && !viewGroupManager.needsCustomLayoutForChildren()) {
                        updateLayout(resolveView, i3, i4, i5, i6);
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Trying to use view with tag ");
                    sb.append(i);
                    sb.append(" as a parent, but its Manager doesn't extends ViewGroupManager");
                    throw new IllegalViewOperationException(sb.toString());
                }
            } else {
                updateLayout(resolveView, i3, i4, i5, i6);
            }
        } finally {
            Systrace.endSection(0);
        }
    }

    private void updateInstanceHandle(View view, long j) {
        UiThreadUtil.assertOnUiThread();
        view.setTag(C0788R.C0791id.view_tag_instance_handle, Long.valueOf(j));
    }

    @Nullable
    public long getInstanceHandle(int i) {
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            Long l = (Long) view.getTag(C0788R.C0791id.view_tag_instance_handle);
            if (l != null) {
                return l.longValue();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to find instanceHandle for tag: ");
            sb.append(i);
            throw new IllegalViewOperationException(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unable to find view for tag: ");
        sb2.append(i);
        throw new IllegalViewOperationException(sb2.toString());
    }

    private void updateLayout(View view, int i, int i2, int i3, int i4) {
        if (!this.mLayoutAnimationEnabled || !this.mLayoutAnimator.shouldAnimateLayout(view)) {
            view.layout(i, i2, i3 + i, i4 + i2);
        } else {
            this.mLayoutAnimator.applyLayoutUpdate(view, i, i2, i3, i4);
        }
    }

    public synchronized void createView(ThemedReactContext themedReactContext, int i, String str, @Nullable ReactStylesDiffMap reactStylesDiffMap) {
        UiThreadUtil.assertOnUiThread();
        SystraceMessage.beginSection(0, "NativeViewHierarchyManager_createView").arg("tag", i).arg("className", (Object) str).flush();
        try {
            ViewManager viewManager = this.mViewManagers.get(str);
            View createView = viewManager.createView(themedReactContext, this.mJSResponderHandler);
            this.mTagsToViews.put(i, createView);
            this.mTagsToViewManagers.put(i, viewManager);
            createView.setId(i);
            if (reactStylesDiffMap != null) {
                viewManager.updateProperties(createView, reactStylesDiffMap);
            }
        } finally {
            Systrace.endSection(0);
        }
    }

    private static String constructManageChildrenErrorMessage(ViewGroup viewGroup, ViewGroupManager viewGroupManager, @Nullable int[] iArr, @Nullable ViewAtIndex[] viewAtIndexArr, @Nullable int[] iArr2) {
        StringBuilder sb = new StringBuilder();
        if (viewGroup != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("View tag:");
            sb2.append(viewGroup.getId());
            sb2.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
            sb.append(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("  children(");
            sb3.append(viewGroupManager.getChildCount(viewGroup));
            sb3.append("): [\n");
            sb.append(sb3.toString());
            for (int i = 0; i < viewGroupManager.getChildCount(viewGroup); i += 16) {
                int i2 = 0;
                while (true) {
                    int i3 = i + i2;
                    if (i3 >= viewGroupManager.getChildCount(viewGroup) || i2 >= 16) {
                        sb.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
                    } else {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(viewGroupManager.getChildAt(viewGroup, i3).getId());
                        sb4.append(",");
                        sb.append(sb4.toString());
                        i2++;
                    }
                }
                sb.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
            }
            sb.append(" ],\n");
        }
        if (iArr != null) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("  indicesToRemove(");
            sb5.append(iArr.length);
            sb5.append("): [\n");
            sb.append(sb5.toString());
            for (int i4 = 0; i4 < iArr.length; i4 += 16) {
                int i5 = 0;
                while (true) {
                    int i6 = i4 + i5;
                    if (i6 >= iArr.length || i5 >= 16) {
                        sb.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
                    } else {
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append(iArr[i6]);
                        sb6.append(",");
                        sb.append(sb6.toString());
                        i5++;
                    }
                }
                sb.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
            }
            sb.append(" ],\n");
        }
        if (viewAtIndexArr != null) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append("  viewsToAdd(");
            sb7.append(viewAtIndexArr.length);
            sb7.append("): [\n");
            sb.append(sb7.toString());
            for (int i7 = 0; i7 < viewAtIndexArr.length; i7 += 16) {
                int i8 = 0;
                while (true) {
                    int i9 = i7 + i8;
                    if (i9 >= viewAtIndexArr.length || i8 >= 16) {
                        sb.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
                    } else {
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("[");
                        sb8.append(viewAtIndexArr[i9].mIndex);
                        sb8.append(",");
                        sb8.append(viewAtIndexArr[i9].mTag);
                        sb8.append("],");
                        sb.append(sb8.toString());
                        i8++;
                    }
                }
                sb.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
            }
            sb.append(" ],\n");
        }
        if (iArr2 != null) {
            StringBuilder sb9 = new StringBuilder();
            sb9.append("  tagsToDelete(");
            sb9.append(iArr2.length);
            sb9.append("): [\n");
            sb.append(sb9.toString());
            for (int i10 = 0; i10 < iArr2.length; i10 += 16) {
                int i11 = 0;
                while (true) {
                    int i12 = i10 + i11;
                    if (i12 >= iArr2.length || i11 >= 16) {
                        sb.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
                    } else {
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(iArr2[i12]);
                        sb10.append(",");
                        sb.append(sb10.toString());
                        i11++;
                    }
                }
                sb.append(ReactEditTextInputConnectionWrapper.NEWLINE_RAW_VALUE);
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0172, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void manageChildren(int r7, @javax.annotation.Nullable int[] r8, @javax.annotation.Nullable com.facebook.react.uimanager.ViewAtIndex[] r9, @javax.annotation.Nullable int[] r10) {
        /*
            r6 = this;
            monitor-enter(r6)
            com.facebook.react.bridge.UiThreadUtil.assertOnUiThread()     // Catch:{ all -> 0x0196 }
            android.util.SparseArray<android.view.View> r0 = r6.mTagsToViews     // Catch:{ all -> 0x0196 }
            java.lang.Object r0 = r0.get(r7)     // Catch:{ all -> 0x0196 }
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0     // Catch:{ all -> 0x0196 }
            com.facebook.react.uimanager.ViewManager r1 = r6.resolveViewManager(r7)     // Catch:{ all -> 0x0196 }
            com.facebook.react.uimanager.ViewGroupManager r1 = (com.facebook.react.uimanager.ViewGroupManager) r1     // Catch:{ all -> 0x0196 }
            if (r0 == 0) goto L_0x0173
            int r2 = r1.getChildCount(r0)     // Catch:{ all -> 0x0196 }
            if (r8 == 0) goto L_0x00de
            int r3 = r8.length     // Catch:{ all -> 0x0196 }
            int r3 = r3 + -1
        L_0x001d:
            if (r3 < 0) goto L_0x00de
            r4 = r8[r3]     // Catch:{ all -> 0x0196 }
            if (r4 < 0) goto L_0x00b3
            int r5 = r1.getChildCount(r0)     // Catch:{ all -> 0x0196 }
            if (r4 < r5) goto L_0x0064
            android.util.SparseBooleanArray r2 = r6.mRootTags     // Catch:{ all -> 0x0196 }
            boolean r2 = r2.get(r7)     // Catch:{ all -> 0x0196 }
            if (r2 == 0) goto L_0x0039
            int r2 = r1.getChildCount(r0)     // Catch:{ all -> 0x0196 }
            if (r2 != 0) goto L_0x0039
            monitor-exit(r6)
            return
        L_0x0039:
            com.facebook.react.uimanager.IllegalViewOperationException r2 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x0196 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0196 }
            r3.<init>()     // Catch:{ all -> 0x0196 }
            java.lang.String r5 = "Trying to remove a view index above child count "
            r3.append(r5)     // Catch:{ all -> 0x0196 }
            r3.append(r4)     // Catch:{ all -> 0x0196 }
            java.lang.String r4 = " view tag: "
            r3.append(r4)     // Catch:{ all -> 0x0196 }
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = "\n detail: "
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = constructManageChildrenErrorMessage(r0, r1, r8, r9, r10)     // Catch:{ all -> 0x0196 }
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = r3.toString()     // Catch:{ all -> 0x0196 }
            r2.<init>(r7)     // Catch:{ all -> 0x0196 }
            throw r2     // Catch:{ all -> 0x0196 }
        L_0x0064:
            if (r4 >= r2) goto L_0x0088
            android.view.View r2 = r1.getChildAt(r0, r4)     // Catch:{ all -> 0x0196 }
            boolean r5 = r6.mLayoutAnimationEnabled     // Catch:{ all -> 0x0196 }
            if (r5 == 0) goto L_0x0081
            com.facebook.react.uimanager.layoutanimation.LayoutAnimationController r5 = r6.mLayoutAnimator     // Catch:{ all -> 0x0196 }
            boolean r5 = r5.shouldAnimateLayout(r2)     // Catch:{ all -> 0x0196 }
            if (r5 == 0) goto L_0x0081
            int r2 = r2.getId()     // Catch:{ all -> 0x0196 }
            boolean r2 = r6.arrayContains(r10, r2)     // Catch:{ all -> 0x0196 }
            if (r2 == 0) goto L_0x0081
            goto L_0x0084
        L_0x0081:
            r1.removeViewAt(r0, r4)     // Catch:{ all -> 0x0196 }
        L_0x0084:
            int r3 = r3 + -1
            r2 = r4
            goto L_0x001d
        L_0x0088:
            com.facebook.react.uimanager.IllegalViewOperationException r2 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x0196 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0196 }
            r3.<init>()     // Catch:{ all -> 0x0196 }
            java.lang.String r5 = "Trying to remove an out of order view index:"
            r3.append(r5)     // Catch:{ all -> 0x0196 }
            r3.append(r4)     // Catch:{ all -> 0x0196 }
            java.lang.String r4 = " view tag: "
            r3.append(r4)     // Catch:{ all -> 0x0196 }
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = "\n detail: "
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = constructManageChildrenErrorMessage(r0, r1, r8, r9, r10)     // Catch:{ all -> 0x0196 }
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = r3.toString()     // Catch:{ all -> 0x0196 }
            r2.<init>(r7)     // Catch:{ all -> 0x0196 }
            throw r2     // Catch:{ all -> 0x0196 }
        L_0x00b3:
            com.facebook.react.uimanager.IllegalViewOperationException r2 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x0196 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0196 }
            r3.<init>()     // Catch:{ all -> 0x0196 }
            java.lang.String r5 = "Trying to remove a negative view index:"
            r3.append(r5)     // Catch:{ all -> 0x0196 }
            r3.append(r4)     // Catch:{ all -> 0x0196 }
            java.lang.String r4 = " view tag: "
            r3.append(r4)     // Catch:{ all -> 0x0196 }
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = "\n detail: "
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = constructManageChildrenErrorMessage(r0, r1, r8, r9, r10)     // Catch:{ all -> 0x0196 }
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = r3.toString()     // Catch:{ all -> 0x0196 }
            r2.<init>(r7)     // Catch:{ all -> 0x0196 }
            throw r2     // Catch:{ all -> 0x0196 }
        L_0x00de:
            r7 = 0
            if (r9 == 0) goto L_0x0120
            r2 = 0
        L_0x00e2:
            int r3 = r9.length     // Catch:{ all -> 0x0196 }
            if (r2 >= r3) goto L_0x0120
            r3 = r9[r2]     // Catch:{ all -> 0x0196 }
            android.util.SparseArray<android.view.View> r4 = r6.mTagsToViews     // Catch:{ all -> 0x0196 }
            int r5 = r3.mTag     // Catch:{ all -> 0x0196 }
            java.lang.Object r4 = r4.get(r5)     // Catch:{ all -> 0x0196 }
            android.view.View r4 = (android.view.View) r4     // Catch:{ all -> 0x0196 }
            if (r4 == 0) goto L_0x00fb
            int r3 = r3.mIndex     // Catch:{ all -> 0x0196 }
            r1.addView(r0, r4, r3)     // Catch:{ all -> 0x0196 }
            int r2 = r2 + 1
            goto L_0x00e2
        L_0x00fb:
            com.facebook.react.uimanager.IllegalViewOperationException r7 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x0196 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0196 }
            r2.<init>()     // Catch:{ all -> 0x0196 }
            java.lang.String r4 = "Trying to add unknown view tag: "
            r2.append(r4)     // Catch:{ all -> 0x0196 }
            int r3 = r3.mTag     // Catch:{ all -> 0x0196 }
            r2.append(r3)     // Catch:{ all -> 0x0196 }
            java.lang.String r3 = "\n detail: "
            r2.append(r3)     // Catch:{ all -> 0x0196 }
            java.lang.String r8 = constructManageChildrenErrorMessage(r0, r1, r8, r9, r10)     // Catch:{ all -> 0x0196 }
            r2.append(r8)     // Catch:{ all -> 0x0196 }
            java.lang.String r8 = r2.toString()     // Catch:{ all -> 0x0196 }
            r7.<init>(r8)     // Catch:{ all -> 0x0196 }
            throw r7     // Catch:{ all -> 0x0196 }
        L_0x0120:
            if (r10 == 0) goto L_0x0171
        L_0x0122:
            int r2 = r10.length     // Catch:{ all -> 0x0196 }
            if (r7 >= r2) goto L_0x0171
            r2 = r10[r7]     // Catch:{ all -> 0x0196 }
            android.util.SparseArray<android.view.View> r3 = r6.mTagsToViews     // Catch:{ all -> 0x0196 }
            java.lang.Object r3 = r3.get(r2)     // Catch:{ all -> 0x0196 }
            android.view.View r3 = (android.view.View) r3     // Catch:{ all -> 0x0196 }
            if (r3 == 0) goto L_0x014e
            boolean r2 = r6.mLayoutAnimationEnabled     // Catch:{ all -> 0x0196 }
            if (r2 == 0) goto L_0x0148
            com.facebook.react.uimanager.layoutanimation.LayoutAnimationController r2 = r6.mLayoutAnimator     // Catch:{ all -> 0x0196 }
            boolean r2 = r2.shouldAnimateLayout(r3)     // Catch:{ all -> 0x0196 }
            if (r2 == 0) goto L_0x0148
            com.facebook.react.uimanager.layoutanimation.LayoutAnimationController r2 = r6.mLayoutAnimator     // Catch:{ all -> 0x0196 }
            com.facebook.react.uimanager.NativeViewHierarchyManager$1 r4 = new com.facebook.react.uimanager.NativeViewHierarchyManager$1     // Catch:{ all -> 0x0196 }
            r4.<init>(r1, r0, r3)     // Catch:{ all -> 0x0196 }
            r2.deleteView(r3, r4)     // Catch:{ all -> 0x0196 }
            goto L_0x014b
        L_0x0148:
            r6.dropView(r3)     // Catch:{ all -> 0x0196 }
        L_0x014b:
            int r7 = r7 + 1
            goto L_0x0122
        L_0x014e:
            com.facebook.react.uimanager.IllegalViewOperationException r7 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x0196 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0196 }
            r3.<init>()     // Catch:{ all -> 0x0196 }
            java.lang.String r4 = "Trying to destroy unknown view tag: "
            r3.append(r4)     // Catch:{ all -> 0x0196 }
            r3.append(r2)     // Catch:{ all -> 0x0196 }
            java.lang.String r2 = "\n detail: "
            r3.append(r2)     // Catch:{ all -> 0x0196 }
            java.lang.String r8 = constructManageChildrenErrorMessage(r0, r1, r8, r9, r10)     // Catch:{ all -> 0x0196 }
            r3.append(r8)     // Catch:{ all -> 0x0196 }
            java.lang.String r8 = r3.toString()     // Catch:{ all -> 0x0196 }
            r7.<init>(r8)     // Catch:{ all -> 0x0196 }
            throw r7     // Catch:{ all -> 0x0196 }
        L_0x0171:
            monitor-exit(r6)
            return
        L_0x0173:
            com.facebook.react.uimanager.IllegalViewOperationException r2 = new com.facebook.react.uimanager.IllegalViewOperationException     // Catch:{ all -> 0x0196 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0196 }
            r3.<init>()     // Catch:{ all -> 0x0196 }
            java.lang.String r4 = "Trying to manageChildren view with tag "
            r3.append(r4)     // Catch:{ all -> 0x0196 }
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = " which doesn't exist\n detail: "
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = constructManageChildrenErrorMessage(r0, r1, r8, r9, r10)     // Catch:{ all -> 0x0196 }
            r3.append(r7)     // Catch:{ all -> 0x0196 }
            java.lang.String r7 = r3.toString()     // Catch:{ all -> 0x0196 }
            r2.<init>(r7)     // Catch:{ all -> 0x0196 }
            throw r2     // Catch:{ all -> 0x0196 }
        L_0x0196:
            r7 = move-exception
            monitor-exit(r6)
            throw r7
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.react.uimanager.NativeViewHierarchyManager.manageChildren(int, int[], com.facebook.react.uimanager.ViewAtIndex[], int[]):void");
    }

    private boolean arrayContains(@Nullable int[] iArr, int i) {
        if (iArr == null) {
            return false;
        }
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private static String constructSetChildrenErrorMessage(ViewGroup viewGroup, ViewGroupManager viewGroupManager, ReadableArray readableArray) {
        ViewAtIndex[] viewAtIndexArr = new ViewAtIndex[readableArray.size()];
        for (int i = 0; i < readableArray.size(); i++) {
            viewAtIndexArr[i] = new ViewAtIndex(readableArray.getInt(i), i);
        }
        return constructManageChildrenErrorMessage(viewGroup, viewGroupManager, null, viewAtIndexArr, null);
    }

    public synchronized void setChildren(int i, ReadableArray readableArray) {
        UiThreadUtil.assertOnUiThread();
        ViewGroup viewGroup = (ViewGroup) this.mTagsToViews.get(i);
        ViewGroupManager viewGroupManager = (ViewGroupManager) resolveViewManager(i);
        int i2 = 0;
        while (i2 < readableArray.size()) {
            View view = (View) this.mTagsToViews.get(readableArray.getInt(i2));
            if (view != null) {
                viewGroupManager.addView(viewGroup, view, i2);
                i2++;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Trying to add unknown view tag: ");
                sb.append(readableArray.getInt(i2));
                sb.append("\n detail: ");
                sb.append(constructSetChildrenErrorMessage(viewGroup, viewGroupManager, readableArray));
                throw new IllegalViewOperationException(sb.toString());
            }
        }
    }

    public synchronized void addRootView(int i, SizeMonitoringFrameLayout sizeMonitoringFrameLayout, ThemedReactContext themedReactContext) {
        addRootViewGroup(i, sizeMonitoringFrameLayout, themedReactContext);
    }

    /* access modifiers changed from: protected */
    public final synchronized void addRootViewGroup(int i, ViewGroup viewGroup, ThemedReactContext themedReactContext) {
        if (viewGroup.getId() != -1) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Trying to add a root view with an explicit id (");
            sb.append(viewGroup.getId());
            sb.append(") already set. React Native uses the id field to track react tags and will overwrite this field. If that is fine, explicitly overwrite the id field to View.NO_ID before calling addRootView.");
            FLog.m48e(str, sb.toString());
        }
        this.mTagsToViews.put(i, viewGroup);
        this.mTagsToViewManagers.put(i, this.mRootViewManager);
        this.mRootTags.put(i, true);
        viewGroup.setId(i);
    }

    /* access modifiers changed from: protected */
    public synchronized void dropView(View view) {
        UiThreadUtil.assertOnUiThread();
        if (this.mTagsToViewManagers.get(view.getId()) != null) {
            if (!this.mRootTags.get(view.getId())) {
                resolveViewManager(view.getId()).onDropViewInstance(view);
            }
            ViewManager viewManager = (ViewManager) this.mTagsToViewManagers.get(view.getId());
            if ((view instanceof ViewGroup) && (viewManager instanceof ViewGroupManager)) {
                ViewGroup viewGroup = (ViewGroup) view;
                ViewGroupManager viewGroupManager = (ViewGroupManager) viewManager;
                for (int childCount = viewGroupManager.getChildCount(viewGroup) - 1; childCount >= 0; childCount--) {
                    View childAt = viewGroupManager.getChildAt(viewGroup, childCount);
                    if (childAt == null) {
                        FLog.m48e(TAG, "Unable to drop null child view");
                    } else if (this.mTagsToViews.get(childAt.getId()) != null) {
                        dropView(childAt);
                    }
                }
                viewGroupManager.removeAllViews(viewGroup);
            }
            this.mTagsToViews.remove(view.getId());
            this.mTagsToViewManagers.remove(view.getId());
        }
    }

    public synchronized void removeRootView(int i) {
        UiThreadUtil.assertOnUiThread();
        if (!this.mRootTags.get(i)) {
            StringBuilder sb = new StringBuilder();
            sb.append("View with tag ");
            sb.append(i);
            sb.append(" is not registered as a root view");
            SoftAssertions.assertUnreachable(sb.toString());
        }
        dropView((View) this.mTagsToViews.get(i));
        this.mRootTags.delete(i);
    }

    public synchronized void measure(int i, int[] iArr) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            View view2 = (View) RootViewUtil.getRootView(view);
            if (view2 != null) {
                view2.getLocationInWindow(iArr);
                int i2 = iArr[0];
                int i3 = iArr[1];
                view.getLocationInWindow(iArr);
                iArr[0] = iArr[0] - i2;
                iArr[1] = iArr[1] - i3;
                iArr[2] = view.getWidth();
                iArr[3] = view.getHeight();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Native view ");
                sb.append(i);
                sb.append(" is no longer on screen");
                throw new NoSuchNativeViewException(sb.toString());
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("No native view for ");
            sb2.append(i);
            sb2.append(" currently exists");
            throw new NoSuchNativeViewException(sb2.toString());
        }
    }

    public synchronized void measureInWindow(int i, int[] iArr) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            view.getLocationOnScreen(iArr);
            Resources resources = view.getContext().getResources();
            int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (identifier > 0) {
                iArr[1] = iArr[1] - ((int) resources.getDimension(identifier));
            }
            iArr[2] = view.getWidth();
            iArr[3] = view.getHeight();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("No native view for ");
            sb.append(i);
            sb.append(" currently exists");
            throw new NoSuchNativeViewException(sb.toString());
        }
    }

    public synchronized int findTargetTagForTouch(int i, float f, float f2) {
        View view;
        UiThreadUtil.assertOnUiThread();
        view = (View) this.mTagsToViews.get(i);
        if (view != null) {
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Could not find view with tag ");
            sb.append(i);
            throw new JSApplicationIllegalArgumentException(sb.toString());
        }
        return TouchTargetHelper.findTargetTagForTouch(f, f2, (ViewGroup) view);
    }

    public synchronized void setJSResponder(int i, int i2, boolean z) {
        if (!z) {
            this.mJSResponderHandler.setJSResponder(i2, null);
            return;
        }
        View view = (View) this.mTagsToViews.get(i);
        if (i2 == i || !(view instanceof ViewParent)) {
            if (this.mRootTags.get(i)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot block native responder on ");
                sb.append(i);
                sb.append(" that is a root view");
                SoftAssertions.assertUnreachable(sb.toString());
            }
            this.mJSResponderHandler.setJSResponder(i2, view.getParent());
            return;
        }
        this.mJSResponderHandler.setJSResponder(i2, (ViewParent) view);
    }

    public void clearJSResponder() {
        this.mJSResponderHandler.clearJSResponder();
    }

    /* access modifiers changed from: 0000 */
    public void configureLayoutAnimation(ReadableMap readableMap) {
        this.mLayoutAnimator.initializeFromConfig(readableMap);
    }

    /* access modifiers changed from: 0000 */
    public void clearLayoutAnimation() {
        this.mLayoutAnimator.reset();
    }

    /* access modifiers changed from: 0000 */
    public synchronized void startAnimationForNativeView(int i, Animation animation, @Nullable final Callback callback) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        final int animationID = animation.getAnimationID();
        if (view != null) {
            animation.setAnimationListener(new AnimationListener() {
                public void onFinished() {
                    Assertions.assertNotNull(NativeViewHierarchyManager.this.mAnimationRegistry.removeAnimation(animationID), "Animation was already removed somehow!");
                    Callback callback = callback;
                    if (callback != null) {
                        callback.invoke(Boolean.valueOf(true));
                    }
                }

                public void onCancel() {
                    Assertions.assertNotNull(NativeViewHierarchyManager.this.mAnimationRegistry.removeAnimation(animationID), "Animation was already removed somehow!");
                    Callback callback = callback;
                    if (callback != null) {
                        callback.invoke(Boolean.valueOf(false));
                    }
                }
            });
            animation.start(view);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("View with tag ");
            sb.append(i);
            sb.append(" not found");
            throw new IllegalViewOperationException(sb.toString());
        }
    }

    public synchronized void dispatchCommand(int i, int i2, @Nullable ReadableArray readableArray) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            resolveViewManager(i).receiveCommand(view, i2, readableArray);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Trying to send command to a non-existing view with tag ");
            sb.append(i);
            throw new IllegalViewOperationException(sb.toString());
        }
    }

    public synchronized void showPopupMenu(int i, ReadableArray readableArray, Callback callback, Callback callback2) {
        UiThreadUtil.assertOnUiThread();
        View view = (View) this.mTagsToViews.get(i);
        if (view == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't display popup. Could not find view with tag ");
            sb.append(i);
            callback2.invoke(sb.toString());
            return;
        }
        this.mPopupMenu = new PopupMenu(getReactContextForView(i), view);
        Menu menu = this.mPopupMenu.getMenu();
        for (int i2 = 0; i2 < readableArray.size(); i2++) {
            menu.add(0, 0, i2, readableArray.getString(i2));
        }
        PopupMenuCallbackHandler popupMenuCallbackHandler = new PopupMenuCallbackHandler(callback);
        this.mPopupMenu.setOnMenuItemClickListener(popupMenuCallbackHandler);
        this.mPopupMenu.setOnDismissListener(popupMenuCallbackHandler);
        this.mPopupMenu.show();
    }

    public void dismissPopupMenu() {
        PopupMenu popupMenu = this.mPopupMenu;
        if (popupMenu != null) {
            popupMenu.dismiss();
        }
    }

    private ThemedReactContext getReactContextForView(int i) {
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            return (ThemedReactContext) view.getContext();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Could not find view with tag ");
        sb.append(i);
        throw new JSApplicationIllegalArgumentException(sb.toString());
    }

    public void sendAccessibilityEvent(int i, int i2) {
        View view = (View) this.mTagsToViews.get(i);
        if (view != null) {
            AccessibilityHelper.sendAccessibilityEvent(view, i2);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Could not find view with tag ");
        sb.append(i);
        throw new JSApplicationIllegalArgumentException(sb.toString());
    }
}
