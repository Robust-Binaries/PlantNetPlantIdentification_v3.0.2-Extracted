package p010me.relex.photodraweeview;

import android.graphics.RectF;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import com.facebook.drawee.view.DraweeView;

/* renamed from: me.relex.photodraweeview.DefaultOnDoubleTapListener */
public class DefaultOnDoubleTapListener implements OnDoubleTapListener {
    protected Attacher mAttacher;

    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    public DefaultOnDoubleTapListener(Attacher attacher) {
        setPhotoDraweeViewAttacher(attacher);
    }

    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        Attacher attacher = this.mAttacher;
        if (attacher == null) {
            return false;
        }
        DraweeView draweeView = attacher.getDraweeView();
        if (draweeView == null) {
            return false;
        }
        if (this.mAttacher.getOnPhotoTapListener() != null) {
            RectF displayRect = this.mAttacher.getDisplayRect();
            if (displayRect != null) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (displayRect.contains(x, y)) {
                    this.mAttacher.getOnPhotoTapListener().onPhotoTap(draweeView, (x - displayRect.left) / displayRect.width(), (y - displayRect.top) / displayRect.height());
                    return true;
                }
            }
        }
        if (this.mAttacher.getOnViewTapListener() == null) {
            return false;
        }
        this.mAttacher.getOnViewTapListener().onViewTap(draweeView, motionEvent.getX(), motionEvent.getY());
        return true;
    }

    public boolean onDoubleTap(MotionEvent motionEvent) {
        Attacher attacher = this.mAttacher;
        if (attacher == null) {
            return false;
        }
        try {
            float scale = attacher.getScale();
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (scale < this.mAttacher.getMediumScale()) {
                this.mAttacher.setScale(this.mAttacher.getMediumScale(), x, y, true);
            } else if (scale < this.mAttacher.getMediumScale() || scale >= this.mAttacher.getMaximumScale()) {
                this.mAttacher.setScale(this.mAttacher.getMinimumScale(), x, y, true);
            } else {
                this.mAttacher.setScale(this.mAttacher.getMaximumScale(), x, y, true);
            }
        } catch (Exception unused) {
        }
        return true;
    }

    public void setPhotoDraweeViewAttacher(Attacher attacher) {
        this.mAttacher = attacher;
    }
}
