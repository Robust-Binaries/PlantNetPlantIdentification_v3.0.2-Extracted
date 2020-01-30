package com.yalantis.ucrop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yalantis.ucrop.UCrop.Options;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.util.SelectedStateListDrawable;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView.TransformImageListener;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.AspectRatioTextView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView.ScrollingListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class UCropFragment extends Fragment {
    public static final int ALL = 3;
    public static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final int NONE = 0;
    public static final int ROTATE = 2;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
    public static final int SCALE = 1;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int TABS_COUNT = 3;
    public static final String TAG = "UCropFragment";
    /* access modifiers changed from: private */
    public UCropFragmentCallback callback;
    private int mActiveWidgetColor;
    private int[] mAllowedGestures = {1, 2, 3};
    /* access modifiers changed from: private */
    public View mBlockingView;
    private CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
    private int mCompressQuality = 90;
    /* access modifiers changed from: private */
    public List<ViewGroup> mCropAspectRatioViews = new ArrayList();
    /* access modifiers changed from: private */
    public GestureCropImageView mGestureCropImageView;
    private TransformImageListener mImageListener = new TransformImageListener() {
        public void onRotate(float f) {
            UCropFragment.this.setAngleText(f);
        }

        public void onScale(float f) {
            UCropFragment.this.setScaleText(f);
        }

        public void onLoadComplete() {
            UCropFragment.this.mUCropView.animate().alpha(1.0f).setDuration(300).setInterpolator(new AccelerateInterpolator());
            UCropFragment.this.mBlockingView.setClickable(false);
            UCropFragment.this.callback.loadingProgress(false);
        }

        public void onLoadFailure(@NonNull Exception exc) {
            UCropFragment.this.callback.onCropFinish(UCropFragment.this.getError(exc));
        }
    };
    private ViewGroup mLayoutAspectRatio;
    private ViewGroup mLayoutRotate;
    private ViewGroup mLayoutScale;
    private int mLogoColor;
    private OverlayView mOverlayView;
    @ColorInt
    private int mRootViewBackgroundColor;
    private boolean mShowBottomControls;
    private final OnClickListener mStateClickListener = new OnClickListener() {
        public void onClick(View view) {
            if (!view.isSelected()) {
                UCropFragment.this.setWidgetState(view.getId());
            }
        }
    };
    private TextView mTextViewRotateAngle;
    private TextView mTextViewScalePercent;
    /* access modifiers changed from: private */
    public UCropView mUCropView;
    private ViewGroup mWrapperStateAspectRatio;
    private ViewGroup mWrapperStateRotate;
    private ViewGroup mWrapperStateScale;

    @Retention(RetentionPolicy.SOURCE)
    public @interface GestureTypes {
    }

    public class UCropResult {
        public int mResultCode;
        public Intent mResultData;

        public UCropResult(int i, Intent intent) {
            this.mResultCode = i;
            this.mResultData = intent;
        }
    }

    public static UCropFragment newInstance(Bundle bundle) {
        UCropFragment uCropFragment = new UCropFragment();
        uCropFragment.setArguments(bundle);
        return uCropFragment;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.callback = (UCropFragmentCallback) context;
        } catch (ClassCastException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.toString());
            sb.append(" must implement UCropFragmentCallback");
            throw new ClassCastException(sb.toString());
        }
    }

    public void setCallback(UCropFragmentCallback uCropFragmentCallback) {
        this.callback = uCropFragmentCallback;
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C1313R.layout.ucrop_fragment_photobox, viewGroup, false);
        Bundle arguments = getArguments();
        setupViews(inflate, arguments);
        setImageData(arguments);
        setInitialState();
        addBlockingView(inflate);
        return inflate;
    }

    public void setupViews(View view, Bundle bundle) {
        this.mActiveWidgetColor = bundle.getInt(Options.EXTRA_UCROP_COLOR_WIDGET_ACTIVE, ContextCompat.getColor(getContext(), C1313R.C1314color.ucrop_color_widget_active));
        this.mLogoColor = bundle.getInt(Options.EXTRA_UCROP_LOGO_COLOR, ContextCompat.getColor(getContext(), C1313R.C1314color.ucrop_color_default_logo));
        this.mShowBottomControls = !bundle.getBoolean(Options.EXTRA_HIDE_BOTTOM_CONTROLS, false);
        this.mRootViewBackgroundColor = bundle.getInt(Options.EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR, ContextCompat.getColor(getContext(), C1313R.C1314color.ucrop_color_crop_background));
        initiateRootViews(view);
        this.callback.loadingProgress(true);
        if (this.mShowBottomControls) {
            View.inflate(getContext(), C1313R.layout.ucrop_controls, (ViewGroup) view.findViewById(C1313R.C1316id.ucrop_photobox));
            this.mWrapperStateAspectRatio = (ViewGroup) view.findViewById(C1313R.C1316id.state_aspect_ratio);
            this.mWrapperStateAspectRatio.setOnClickListener(this.mStateClickListener);
            this.mWrapperStateRotate = (ViewGroup) view.findViewById(C1313R.C1316id.state_rotate);
            this.mWrapperStateRotate.setOnClickListener(this.mStateClickListener);
            this.mWrapperStateScale = (ViewGroup) view.findViewById(C1313R.C1316id.state_scale);
            this.mWrapperStateScale.setOnClickListener(this.mStateClickListener);
            this.mLayoutAspectRatio = (ViewGroup) view.findViewById(C1313R.C1316id.layout_aspect_ratio);
            this.mLayoutRotate = (ViewGroup) view.findViewById(C1313R.C1316id.layout_rotate_wheel);
            this.mLayoutScale = (ViewGroup) view.findViewById(C1313R.C1316id.layout_scale_wheel);
            setupAspectRatioWidget(bundle, view);
            setupRotateWidget(view);
            setupScaleWidget(view);
            setupStatesWrapper(view);
        }
    }

    private void setImageData(@NonNull Bundle bundle) {
        Uri uri = (Uri) bundle.getParcelable(UCrop.EXTRA_INPUT_URI);
        Uri uri2 = (Uri) bundle.getParcelable(UCrop.EXTRA_OUTPUT_URI);
        processOptions(bundle);
        if (uri == null || uri2 == null) {
            this.callback.onCropFinish(getError(new NullPointerException(getString(C1313R.string.ucrop_error_input_data_is_absent))));
            return;
        }
        try {
            this.mGestureCropImageView.setImageUri(uri, uri2);
        } catch (Exception e) {
            this.callback.onCropFinish(getError(e));
        }
    }

    private void processOptions(@NonNull Bundle bundle) {
        String string = bundle.getString(Options.EXTRA_COMPRESSION_FORMAT_NAME);
        CompressFormat valueOf = !TextUtils.isEmpty(string) ? CompressFormat.valueOf(string) : null;
        if (valueOf == null) {
            valueOf = DEFAULT_COMPRESS_FORMAT;
        }
        this.mCompressFormat = valueOf;
        this.mCompressQuality = bundle.getInt(Options.EXTRA_COMPRESSION_QUALITY, 90);
        int[] intArray = bundle.getIntArray(Options.EXTRA_ALLOWED_GESTURES);
        if (intArray != null && intArray.length == 3) {
            this.mAllowedGestures = intArray;
        }
        this.mGestureCropImageView.setMaxBitmapSize(bundle.getInt(Options.EXTRA_MAX_BITMAP_SIZE, 0));
        this.mGestureCropImageView.setMaxScaleMultiplier(bundle.getFloat(Options.EXTRA_MAX_SCALE_MULTIPLIER, 10.0f));
        this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration((long) bundle.getInt(Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION));
        this.mOverlayView.setFreestyleCropEnabled(bundle.getBoolean(Options.EXTRA_FREE_STYLE_CROP, false));
        this.mOverlayView.setDimmedColor(bundle.getInt(Options.EXTRA_DIMMED_LAYER_COLOR, getResources().getColor(C1313R.C1314color.ucrop_color_default_dimmed)));
        this.mOverlayView.setCircleDimmedLayer(bundle.getBoolean(Options.EXTRA_CIRCLE_DIMMED_LAYER, false));
        this.mOverlayView.setShowCropFrame(bundle.getBoolean(Options.EXTRA_SHOW_CROP_FRAME, true));
        this.mOverlayView.setCropFrameColor(bundle.getInt(Options.EXTRA_CROP_FRAME_COLOR, getResources().getColor(C1313R.C1314color.ucrop_color_default_crop_frame)));
        this.mOverlayView.setCropFrameStrokeWidth(bundle.getInt(Options.EXTRA_CROP_FRAME_STROKE_WIDTH, getResources().getDimensionPixelSize(C1313R.dimen.ucrop_default_crop_frame_stoke_width)));
        this.mOverlayView.setShowCropGrid(bundle.getBoolean(Options.EXTRA_SHOW_CROP_GRID, true));
        this.mOverlayView.setCropGridRowCount(bundle.getInt(Options.EXTRA_CROP_GRID_ROW_COUNT, 2));
        this.mOverlayView.setCropGridColumnCount(bundle.getInt(Options.EXTRA_CROP_GRID_COLUMN_COUNT, 2));
        this.mOverlayView.setCropGridColor(bundle.getInt(Options.EXTRA_CROP_GRID_COLOR, getResources().getColor(C1313R.C1314color.ucrop_color_default_crop_grid)));
        this.mOverlayView.setCropGridStrokeWidth(bundle.getInt(Options.EXTRA_CROP_GRID_STROKE_WIDTH, getResources().getDimensionPixelSize(C1313R.dimen.ucrop_default_crop_grid_stoke_width)));
        float f = bundle.getFloat(UCrop.EXTRA_ASPECT_RATIO_X, 0.0f);
        float f2 = bundle.getFloat(UCrop.EXTRA_ASPECT_RATIO_Y, 0.0f);
        int i = bundle.getInt(Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList parcelableArrayList = bundle.getParcelableArrayList(Options.EXTRA_ASPECT_RATIO_OPTIONS);
        if (f > 0.0f && f2 > 0.0f) {
            ViewGroup viewGroup = this.mWrapperStateAspectRatio;
            if (viewGroup != null) {
                viewGroup.setVisibility(8);
            }
            this.mGestureCropImageView.setTargetAspectRatio(f / f2);
        } else if (parcelableArrayList == null || i >= parcelableArrayList.size()) {
            this.mGestureCropImageView.setTargetAspectRatio(0.0f);
        } else {
            this.mGestureCropImageView.setTargetAspectRatio(((AspectRatio) parcelableArrayList.get(i)).getAspectRatioX() / ((AspectRatio) parcelableArrayList.get(i)).getAspectRatioY());
        }
        int i2 = bundle.getInt(UCrop.EXTRA_MAX_SIZE_X, 0);
        int i3 = bundle.getInt(UCrop.EXTRA_MAX_SIZE_Y, 0);
        if (i2 > 0 && i3 > 0) {
            this.mGestureCropImageView.setMaxResultImageSizeX(i2);
            this.mGestureCropImageView.setMaxResultImageSizeY(i3);
        }
    }

    private void initiateRootViews(View view) {
        this.mUCropView = (UCropView) view.findViewById(C1313R.C1316id.ucrop);
        this.mGestureCropImageView = this.mUCropView.getCropImageView();
        this.mOverlayView = this.mUCropView.getOverlayView();
        this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
        ((ImageView) view.findViewById(C1313R.C1316id.image_view_logo)).setColorFilter(this.mLogoColor, Mode.SRC_ATOP);
        view.findViewById(C1313R.C1316id.ucrop_frame).setBackgroundColor(this.mRootViewBackgroundColor);
    }

    private void setupStatesWrapper(View view) {
        ImageView imageView = (ImageView) view.findViewById(C1313R.C1316id.image_view_state_scale);
        ImageView imageView2 = (ImageView) view.findViewById(C1313R.C1316id.image_view_state_rotate);
        ImageView imageView3 = (ImageView) view.findViewById(C1313R.C1316id.image_view_state_aspect_ratio);
        imageView.setImageDrawable(new SelectedStateListDrawable(imageView.getDrawable(), this.mActiveWidgetColor));
        imageView2.setImageDrawable(new SelectedStateListDrawable(imageView2.getDrawable(), this.mActiveWidgetColor));
        imageView3.setImageDrawable(new SelectedStateListDrawable(imageView3.getDrawable(), this.mActiveWidgetColor));
    }

    private void setupAspectRatioWidget(@NonNull Bundle bundle, View view) {
        int i = bundle.getInt(Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList parcelableArrayList = bundle.getParcelableArrayList(Options.EXTRA_ASPECT_RATIO_OPTIONS);
        if (parcelableArrayList == null || parcelableArrayList.isEmpty()) {
            i = 2;
            parcelableArrayList = new ArrayList();
            parcelableArrayList.add(new AspectRatio(null, 1.0f, 1.0f));
            parcelableArrayList.add(new AspectRatio(null, 3.0f, 4.0f));
            parcelableArrayList.add(new AspectRatio(getString(C1313R.string.ucrop_label_original).toUpperCase(), 0.0f, 0.0f));
            parcelableArrayList.add(new AspectRatio(null, 3.0f, 2.0f));
            parcelableArrayList.add(new AspectRatio(null, 16.0f, 9.0f));
        }
        LinearLayout linearLayout = (LinearLayout) view.findViewById(C1313R.C1316id.layout_aspect_ratio);
        LayoutParams layoutParams = new LayoutParams(0, -1);
        layoutParams.weight = 1.0f;
        Iterator it = parcelableArrayList.iterator();
        while (it.hasNext()) {
            AspectRatio aspectRatio = (AspectRatio) it.next();
            FrameLayout frameLayout = (FrameLayout) getLayoutInflater().inflate(C1313R.layout.ucrop_aspect_ratio, null);
            frameLayout.setLayoutParams(layoutParams);
            AspectRatioTextView aspectRatioTextView = (AspectRatioTextView) frameLayout.getChildAt(0);
            aspectRatioTextView.setActiveColor(this.mActiveWidgetColor);
            aspectRatioTextView.setAspectRatio(aspectRatio);
            linearLayout.addView(frameLayout);
            this.mCropAspectRatioViews.add(frameLayout);
        }
        ((ViewGroup) this.mCropAspectRatioViews.get(i)).setSelected(true);
        for (ViewGroup onClickListener : this.mCropAspectRatioViews) {
            onClickListener.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    UCropFragment.this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView) ((ViewGroup) view).getChildAt(0)).getAspectRatio(view.isSelected()));
                    UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
                    if (!view.isSelected()) {
                        for (ViewGroup viewGroup : UCropFragment.this.mCropAspectRatioViews) {
                            viewGroup.setSelected(viewGroup == view);
                        }
                    }
                }
            });
        }
    }

    private void setupRotateWidget(View view) {
        this.mTextViewRotateAngle = (TextView) view.findViewById(C1313R.C1316id.text_view_rotate);
        ((HorizontalProgressWheelView) view.findViewById(C1313R.C1316id.rotate_scroll_wheel)).setScrollingListener(new ScrollingListener() {
            public void onScroll(float f, float f2) {
                UCropFragment.this.mGestureCropImageView.postRotate(f / 42.0f);
            }

            public void onScrollEnd() {
                UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            public void onScrollStart() {
                UCropFragment.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) view.findViewById(C1313R.C1316id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
        view.findViewById(C1313R.C1316id.wrapper_reset_rotate).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UCropFragment.this.resetRotation();
            }
        });
        view.findViewById(C1313R.C1316id.wrapper_rotate_by_angle).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                UCropFragment.this.rotateByAngle(90);
            }
        });
    }

    private void setupScaleWidget(View view) {
        this.mTextViewScalePercent = (TextView) view.findViewById(C1313R.C1316id.text_view_scale);
        ((HorizontalProgressWheelView) view.findViewById(C1313R.C1316id.scale_scroll_wheel)).setScrollingListener(new ScrollingListener() {
            public void onScroll(float f, float f2) {
                if (f > 0.0f) {
                    UCropFragment.this.mGestureCropImageView.zoomInImage(UCropFragment.this.mGestureCropImageView.getCurrentScale() + (f * ((UCropFragment.this.mGestureCropImageView.getMaxScale() - UCropFragment.this.mGestureCropImageView.getMinScale()) / 15000.0f)));
                } else {
                    UCropFragment.this.mGestureCropImageView.zoomOutImage(UCropFragment.this.mGestureCropImageView.getCurrentScale() + (f * ((UCropFragment.this.mGestureCropImageView.getMaxScale() - UCropFragment.this.mGestureCropImageView.getMinScale()) / 15000.0f)));
                }
            }

            public void onScrollEnd() {
                UCropFragment.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            public void onScrollStart() {
                UCropFragment.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) view.findViewById(C1313R.C1316id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
    }

    /* access modifiers changed from: private */
    public void setAngleText(float f) {
        TextView textView = this.mTextViewRotateAngle;
        if (textView != null) {
            textView.setText(String.format(Locale.getDefault(), "%.1f°", new Object[]{Float.valueOf(f)}));
        }
    }

    /* access modifiers changed from: private */
    public void setScaleText(float f) {
        TextView textView = this.mTextViewScalePercent;
        if (textView != null) {
            textView.setText(String.format(Locale.getDefault(), "%d%%", new Object[]{Integer.valueOf((int) (f * 100.0f))}));
        }
    }

    /* access modifiers changed from: private */
    public void resetRotation() {
        GestureCropImageView gestureCropImageView = this.mGestureCropImageView;
        gestureCropImageView.postRotate(-gestureCropImageView.getCurrentAngle());
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    /* access modifiers changed from: private */
    public void rotateByAngle(int i) {
        this.mGestureCropImageView.postRotate((float) i);
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void setInitialState() {
        if (!this.mShowBottomControls) {
            setAllowedGestures(0);
        } else if (this.mWrapperStateAspectRatio.getVisibility() == 0) {
            setWidgetState(C1313R.C1316id.state_aspect_ratio);
        } else {
            setWidgetState(C1313R.C1316id.state_scale);
        }
    }

    /* access modifiers changed from: private */
    public void setWidgetState(@IdRes int i) {
        if (this.mShowBottomControls) {
            this.mWrapperStateAspectRatio.setSelected(i == C1313R.C1316id.state_aspect_ratio);
            this.mWrapperStateRotate.setSelected(i == C1313R.C1316id.state_rotate);
            this.mWrapperStateScale.setSelected(i == C1313R.C1316id.state_scale);
            int i2 = 8;
            this.mLayoutAspectRatio.setVisibility(i == C1313R.C1316id.state_aspect_ratio ? 0 : 8);
            this.mLayoutRotate.setVisibility(i == C1313R.C1316id.state_rotate ? 0 : 8);
            ViewGroup viewGroup = this.mLayoutScale;
            if (i == C1313R.C1316id.state_scale) {
                i2 = 0;
            }
            viewGroup.setVisibility(i2);
            if (i == C1313R.C1316id.state_scale) {
                setAllowedGestures(0);
            } else if (i == C1313R.C1316id.state_rotate) {
                setAllowedGestures(1);
            } else {
                setAllowedGestures(2);
            }
        }
    }

    private void setAllowedGestures(int i) {
        GestureCropImageView gestureCropImageView = this.mGestureCropImageView;
        int[] iArr = this.mAllowedGestures;
        boolean z = false;
        gestureCropImageView.setScaleEnabled(iArr[i] == 3 || iArr[i] == 1);
        GestureCropImageView gestureCropImageView2 = this.mGestureCropImageView;
        int[] iArr2 = this.mAllowedGestures;
        if (iArr2[i] == 3 || iArr2[i] == 2) {
            z = true;
        }
        gestureCropImageView2.setRotateEnabled(z);
    }

    private void addBlockingView(View view) {
        if (this.mBlockingView == null) {
            this.mBlockingView = new View(getContext());
            this.mBlockingView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            this.mBlockingView.setClickable(true);
        }
        ((RelativeLayout) view.findViewById(C1313R.C1316id.ucrop_photobox)).addView(this.mBlockingView);
    }

    public void cropAndSaveImage() {
        this.mBlockingView.setClickable(true);
        this.callback.loadingProgress(true);
        this.mGestureCropImageView.cropAndSaveImage(this.mCompressFormat, this.mCompressQuality, new BitmapCropCallback() {
            public void onBitmapCropped(@NonNull Uri uri, int i, int i2, int i3, int i4) {
                UCropFragmentCallback access$400 = UCropFragment.this.callback;
                UCropFragment uCropFragment = UCropFragment.this;
                access$400.onCropFinish(uCropFragment.getResult(uri, uCropFragment.mGestureCropImageView.getTargetAspectRatio(), i, i2, i3, i4));
                UCropFragment.this.callback.loadingProgress(false);
            }

            public void onCropFailure(@NonNull Throwable th) {
                UCropFragment.this.callback.onCropFinish(UCropFragment.this.getError(th));
            }
        });
    }

    /* access modifiers changed from: protected */
    public UCropResult getResult(Uri uri, float f, int i, int i2, int i3, int i4) {
        return new UCropResult(-1, new Intent().putExtra(UCrop.EXTRA_OUTPUT_URI, uri).putExtra(UCrop.EXTRA_OUTPUT_CROP_ASPECT_RATIO, f).putExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, i3).putExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, i4).putExtra(UCrop.EXTRA_OUTPUT_OFFSET_X, i).putExtra(UCrop.EXTRA_OUTPUT_OFFSET_Y, i2));
    }

    /* access modifiers changed from: protected */
    public UCropResult getError(Throwable th) {
        return new UCropResult(96, new Intent().putExtra(UCrop.EXTRA_ERROR, th));
    }
}
