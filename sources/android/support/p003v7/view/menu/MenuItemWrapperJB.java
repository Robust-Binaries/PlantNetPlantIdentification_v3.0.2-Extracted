package android.support.p003v7.view.menu;

import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p000v4.internal.view.SupportMenuItem;
import android.support.p000v4.view.ActionProvider;
import android.view.ActionProvider.VisibilityListener;
import android.view.MenuItem;
import android.view.View;

@RequiresApi(16)
@RestrictTo({Scope.LIBRARY_GROUP})
/* renamed from: android.support.v7.view.menu.MenuItemWrapperJB */
class MenuItemWrapperJB extends MenuItemWrapperICS {

    /* renamed from: android.support.v7.view.menu.MenuItemWrapperJB$ActionProviderWrapperJB */
    class ActionProviderWrapperJB extends ActionProviderWrapper implements VisibilityListener {
        ActionProvider.VisibilityListener mListener;

        public ActionProviderWrapperJB(Context context, android.view.ActionProvider actionProvider) {
            super(context, actionProvider);
        }

        public View onCreateActionView(MenuItem menuItem) {
            return this.mInner.onCreateActionView(menuItem);
        }

        public boolean overridesItemVisibility() {
            return this.mInner.overridesItemVisibility();
        }

        public boolean isVisible() {
            return this.mInner.isVisible();
        }

        public void refreshVisibility() {
            this.mInner.refreshVisibility();
        }

        public void setVisibilityListener(ActionProvider.VisibilityListener visibilityListener) {
            this.mListener = visibilityListener;
            this.mInner.setVisibilityListener(visibilityListener != null ? this : null);
        }

        public void onActionProviderVisibilityChanged(boolean z) {
            ActionProvider.VisibilityListener visibilityListener = this.mListener;
            if (visibilityListener != null) {
                visibilityListener.onActionProviderVisibilityChanged(z);
            }
        }
    }

    MenuItemWrapperJB(Context context, SupportMenuItem supportMenuItem) {
        super(context, supportMenuItem);
    }

    /* access modifiers changed from: 0000 */
    public ActionProviderWrapper createActionProviderWrapper(android.view.ActionProvider actionProvider) {
        return new ActionProviderWrapperJB(this.mContext, actionProvider);
    }
}
