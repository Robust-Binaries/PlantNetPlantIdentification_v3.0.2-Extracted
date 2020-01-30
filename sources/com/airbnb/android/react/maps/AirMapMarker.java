package com.airbnb.android.react.maps;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Property;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils.ScaleType;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ViewProps;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AirMapMarker extends AirMapFeature {
    private boolean anchorIsSet;
    private float anchorX;
    private float anchorY;
    private boolean calloutAnchorIsSet;
    private float calloutAnchorX;
    private float calloutAnchorY;
    private AirMapCallout calloutView;
    private final Context context;
    /* access modifiers changed from: private */
    public DataSource<CloseableReference<CloseableImage>> dataSource;
    private boolean draggable = false;
    private boolean flat = false;
    private boolean hasCustomMarkerView = false;
    private boolean hasViewChanges = true;
    private int height;
    /* access modifiers changed from: private */
    public Bitmap iconBitmap;
    /* access modifiers changed from: private */
    public BitmapDescriptor iconBitmapDescriptor;
    private String identifier;
    private final DraweeHolder<?> logoHolder;
    private Bitmap mLastBitmapCreated = null;
    private final ControllerListener<ImageInfo> mLogoControllerListener = new BaseControllerListener<ImageInfo>() {
        /* JADX WARNING: Removed duplicated region for block: B:23:0x005e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onFinalImageSet(java.lang.String r3, @javax.annotation.Nullable com.facebook.imagepipeline.image.ImageInfo r4, @javax.annotation.Nullable android.graphics.drawable.Animatable r5) {
            /*
                r2 = this;
                r3 = 0
                com.airbnb.android.react.maps.AirMapMarker r4 = com.airbnb.android.react.maps.AirMapMarker.this     // Catch:{ all -> 0x004f }
                com.facebook.datasource.DataSource r4 = r4.dataSource     // Catch:{ all -> 0x004f }
                java.lang.Object r4 = r4.getResult()     // Catch:{ all -> 0x004f }
                com.facebook.common.references.CloseableReference r4 = (com.facebook.common.references.CloseableReference) r4     // Catch:{ all -> 0x004f }
                r3 = 1
                if (r4 == 0) goto L_0x003b
                java.lang.Object r5 = r4.get()     // Catch:{ all -> 0x0039 }
                com.facebook.imagepipeline.image.CloseableImage r5 = (com.facebook.imagepipeline.image.CloseableImage) r5     // Catch:{ all -> 0x0039 }
                if (r5 == 0) goto L_0x003b
                boolean r0 = r5 instanceof com.facebook.imagepipeline.image.CloseableStaticBitmap     // Catch:{ all -> 0x0039 }
                if (r0 == 0) goto L_0x003b
                com.facebook.imagepipeline.image.CloseableStaticBitmap r5 = (com.facebook.imagepipeline.image.CloseableStaticBitmap) r5     // Catch:{ all -> 0x0039 }
                android.graphics.Bitmap r5 = r5.getUnderlyingBitmap()     // Catch:{ all -> 0x0039 }
                if (r5 == 0) goto L_0x003b
                android.graphics.Bitmap$Config r0 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ all -> 0x0039 }
                android.graphics.Bitmap r5 = r5.copy(r0, r3)     // Catch:{ all -> 0x0039 }
                com.airbnb.android.react.maps.AirMapMarker r0 = com.airbnb.android.react.maps.AirMapMarker.this     // Catch:{ all -> 0x0039 }
                r0.iconBitmap = r5     // Catch:{ all -> 0x0039 }
                com.airbnb.android.react.maps.AirMapMarker r0 = com.airbnb.android.react.maps.AirMapMarker.this     // Catch:{ all -> 0x0039 }
                com.google.android.gms.maps.model.BitmapDescriptor r5 = com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap(r5)     // Catch:{ all -> 0x0039 }
                r0.iconBitmapDescriptor = r5     // Catch:{ all -> 0x0039 }
                goto L_0x003b
            L_0x0039:
                r3 = move-exception
                goto L_0x0053
            L_0x003b:
                com.airbnb.android.react.maps.AirMapMarker r5 = com.airbnb.android.react.maps.AirMapMarker.this
                com.facebook.datasource.DataSource r5 = r5.dataSource
                r5.close()
                if (r4 == 0) goto L_0x0049
                com.facebook.common.references.CloseableReference.closeSafely(r4)
            L_0x0049:
                com.airbnb.android.react.maps.AirMapMarker r4 = com.airbnb.android.react.maps.AirMapMarker.this
                r4.update(r3)
                return
            L_0x004f:
                r4 = move-exception
                r1 = r4
                r4 = r3
                r3 = r1
            L_0x0053:
                com.airbnb.android.react.maps.AirMapMarker r5 = com.airbnb.android.react.maps.AirMapMarker.this
                com.facebook.datasource.DataSource r5 = r5.dataSource
                r5.close()
                if (r4 == 0) goto L_0x0061
                com.facebook.common.references.CloseableReference.closeSafely(r4)
            L_0x0061:
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.airbnb.android.react.maps.AirMapMarker.C05921.onFinalImageSet(java.lang.String, com.facebook.imagepipeline.image.ImageInfo, android.graphics.drawable.Animatable):void");
        }
    };
    private Marker marker;
    private float markerHue = 0.0f;
    private MarkerOptions markerOptions;
    private float opacity = 1.0f;
    private LatLng position;
    private float rotation = 0.0f;
    private String snippet;
    private String title;
    private boolean tracksViewChanges = true;
    private boolean tracksViewChangesActive = false;
    private int width;
    private View wrappedCalloutView;
    private int zIndex = 0;

    public AirMapMarker(Context context2) {
        super(context2);
        this.context = context2;
        this.logoHolder = DraweeHolder.create(createDraweeHierarchy(), context2);
        this.logoHolder.onAttach();
    }

    public AirMapMarker(Context context2, MarkerOptions markerOptions2) {
        super(context2);
        this.context = context2;
        this.logoHolder = DraweeHolder.create(createDraweeHierarchy(), context2);
        this.logoHolder.onAttach();
        this.position = markerOptions2.getPosition();
        setAnchor((double) markerOptions2.getAnchorU(), (double) markerOptions2.getAnchorV());
        setCalloutAnchor((double) markerOptions2.getInfoWindowAnchorU(), (double) markerOptions2.getInfoWindowAnchorV());
        setTitle(markerOptions2.getTitle());
        setSnippet(markerOptions2.getSnippet());
        setRotation(markerOptions2.getRotation());
        setFlat(markerOptions2.isFlat());
        setDraggable(markerOptions2.isDraggable());
        setZIndex(Math.round(markerOptions2.getZIndex()));
        setAlpha(markerOptions2.getAlpha());
        this.iconBitmapDescriptor = markerOptions2.getIcon();
    }

    private GenericDraweeHierarchy createDraweeHierarchy() {
        return new GenericDraweeHierarchyBuilder(getResources()).setActualImageScaleType(ScaleType.FIT_CENTER).setFadeDuration(0).build();
    }

    public void setCoordinate(ReadableMap readableMap) {
        this.position = new LatLng(readableMap.getDouble("latitude"), readableMap.getDouble("longitude"));
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setPosition(this.position);
        }
        update(false);
    }

    public void setIdentifier(String str) {
        this.identifier = str;
        update(false);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setTitle(String str) {
        this.title = str;
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setTitle(str);
        }
        update(false);
    }

    public void setSnippet(String str) {
        this.snippet = str;
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setSnippet(str);
        }
        update(false);
    }

    public void setRotation(float f) {
        this.rotation = f;
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setRotation(f);
        }
        update(false);
    }

    public void setFlat(boolean z) {
        this.flat = z;
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setFlat(z);
        }
        update(false);
    }

    public void setDraggable(boolean z) {
        this.draggable = z;
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setDraggable(z);
        }
        update(false);
    }

    public void setZIndex(int i) {
        this.zIndex = i;
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setZIndex((float) i);
        }
        update(false);
    }

    public void setOpacity(float f) {
        this.opacity = f;
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setAlpha(f);
        }
        update(false);
    }

    public void setMarkerHue(float f) {
        this.markerHue = f;
        update(false);
    }

    public void setAnchor(double d, double d2) {
        this.anchorIsSet = true;
        this.anchorX = (float) d;
        this.anchorY = (float) d2;
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setAnchor(this.anchorX, this.anchorY);
        }
        update(false);
    }

    public void setCalloutAnchor(double d, double d2) {
        this.calloutAnchorIsSet = true;
        this.calloutAnchorX = (float) d;
        this.calloutAnchorY = (float) d2;
        Marker marker2 = this.marker;
        if (marker2 != null) {
            marker2.setInfoWindowAnchor(this.calloutAnchorX, this.calloutAnchorY);
        }
        update(false);
    }

    public void setTracksViewChanges(boolean z) {
        this.tracksViewChanges = z;
        updateTracksViewChanges();
    }

    private void updateTracksViewChanges() {
        boolean z = this.tracksViewChanges && this.hasCustomMarkerView && this.marker != null;
        if (z != this.tracksViewChangesActive) {
            this.tracksViewChangesActive = z;
            if (z) {
                ViewChangesTracker.getInstance().addMarker(this);
            } else {
                ViewChangesTracker.getInstance().removeMarker(this);
                updateMarkerIcon();
            }
        }
    }

    public boolean updateCustomForTracking() {
        if (!this.tracksViewChangesActive) {
            return false;
        }
        updateMarkerIcon();
        return true;
    }

    public void updateMarkerIcon() {
        if (this.hasViewChanges) {
            if (!this.hasCustomMarkerView) {
                this.hasViewChanges = false;
            }
            Marker marker2 = this.marker;
            if (marker2 != null) {
                marker2.setIcon(getIcon());
            }
        }
    }

    public LatLng interpolate(float f, LatLng latLng, LatLng latLng2) {
        double d = latLng2.latitude - latLng.latitude;
        double d2 = (double) f;
        Double.isNaN(d2);
        double d3 = (d * d2) + latLng.latitude;
        double d4 = latLng2.longitude - latLng.longitude;
        Double.isNaN(d2);
        return new LatLng(d3, (d4 * d2) + latLng.longitude);
    }

    public void animateToCoodinate(LatLng latLng, Integer num) {
        C05932 r0 = new TypeEvaluator<LatLng>() {
            public LatLng evaluate(float f, LatLng latLng, LatLng latLng2) {
                return AirMapMarker.this.interpolate(f, latLng, latLng2);
            }
        };
        Property of = Property.of(Marker.class, LatLng.class, ViewProps.POSITION);
        ObjectAnimator ofObject = ObjectAnimator.ofObject(this.marker, of, r0, new LatLng[]{latLng});
        ofObject.setDuration((long) num.intValue());
        ofObject.start();
    }

    public void setImage(String str) {
        this.hasViewChanges = true;
        if (str == null) {
            this.iconBitmapDescriptor = null;
            update(true);
        } else if (str.startsWith("http://") || str.startsWith("https://") || str.startsWith("file://") || str.startsWith("asset://")) {
            ImageRequest build = ImageRequestBuilder.newBuilderWithSource(Uri.parse(str)).build();
            this.dataSource = Fresco.getImagePipeline().fetchDecodedImage(build, this);
            this.logoHolder.setController(((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) ((PipelineDraweeControllerBuilder) Fresco.newDraweeControllerBuilder().setImageRequest(build)).setControllerListener(this.mLogoControllerListener)).setOldController(this.logoHolder.getController())).build());
        } else {
            this.iconBitmapDescriptor = getBitmapDescriptorByName(str);
            if (this.iconBitmapDescriptor != null) {
                int drawableResourceByName = getDrawableResourceByName(str);
                this.iconBitmap = BitmapFactory.decodeResource(getResources(), drawableResourceByName);
                if (this.iconBitmap == null) {
                    Drawable drawable = getResources().getDrawable(drawableResourceByName);
                    this.iconBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    drawable.draw(new Canvas(this.iconBitmap));
                }
            }
            update(true);
        }
    }

    public MarkerOptions getMarkerOptions() {
        if (this.markerOptions == null) {
            this.markerOptions = new MarkerOptions();
        }
        fillMarkerOptions(this.markerOptions);
        return this.markerOptions;
    }

    public void addView(View view, int i) {
        super.addView(view, i);
        if (!(view instanceof AirMapCallout)) {
            this.hasCustomMarkerView = true;
            updateTracksViewChanges();
        }
        update(true);
    }

    public void requestLayout() {
        super.requestLayout();
        if (getChildCount() == 0 && this.hasCustomMarkerView) {
            this.hasCustomMarkerView = false;
            clearDrawableCache();
            updateTracksViewChanges();
            update(true);
        }
    }

    public Object getFeature() {
        return this.marker;
    }

    public void addToMap(GoogleMap googleMap) {
        this.marker = googleMap.addMarker(getMarkerOptions());
        updateTracksViewChanges();
    }

    public void removeFromMap(GoogleMap googleMap) {
        this.marker.remove();
        this.marker = null;
        updateTracksViewChanges();
    }

    private BitmapDescriptor getIcon() {
        if (!this.hasCustomMarkerView) {
            BitmapDescriptor bitmapDescriptor = this.iconBitmapDescriptor;
            if (bitmapDescriptor != null) {
                return bitmapDescriptor;
            }
            return BitmapDescriptorFactory.defaultMarker(this.markerHue);
        } else if (this.iconBitmapDescriptor == null) {
            return BitmapDescriptorFactory.fromBitmap(createDrawable());
        } else {
            Bitmap createDrawable = createDrawable();
            Bitmap createBitmap = Bitmap.createBitmap(Math.max(this.iconBitmap.getWidth(), createDrawable.getWidth()), Math.max(this.iconBitmap.getHeight(), createDrawable.getHeight()), this.iconBitmap.getConfig());
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawBitmap(this.iconBitmap, 0.0f, 0.0f, null);
            canvas.drawBitmap(createDrawable, 0.0f, 0.0f, null);
            return BitmapDescriptorFactory.fromBitmap(createBitmap);
        }
    }

    private MarkerOptions fillMarkerOptions(MarkerOptions markerOptions2) {
        markerOptions2.position(this.position);
        if (this.anchorIsSet) {
            markerOptions2.anchor(this.anchorX, this.anchorY);
        }
        if (this.calloutAnchorIsSet) {
            markerOptions2.infoWindowAnchor(this.calloutAnchorX, this.calloutAnchorY);
        }
        markerOptions2.title(this.title);
        markerOptions2.snippet(this.snippet);
        markerOptions2.rotation(this.rotation);
        markerOptions2.flat(this.flat);
        markerOptions2.draggable(this.draggable);
        markerOptions2.zIndex((float) this.zIndex);
        markerOptions2.alpha(this.opacity);
        markerOptions2.icon(getIcon());
        return markerOptions2;
    }

    public void update(boolean z) {
        if (this.marker != null) {
            if (z) {
                updateMarkerIcon();
            }
            if (this.anchorIsSet) {
                this.marker.setAnchor(this.anchorX, this.anchorY);
            } else {
                this.marker.setAnchor(0.5f, 1.0f);
            }
            if (this.calloutAnchorIsSet) {
                this.marker.setInfoWindowAnchor(this.calloutAnchorX, this.calloutAnchorY);
            } else {
                this.marker.setInfoWindowAnchor(0.5f, 0.0f);
            }
        }
    }

    public void update(int i, int i2) {
        this.width = i;
        this.height = i2;
        update(true);
    }

    private void clearDrawableCache() {
        this.mLastBitmapCreated = null;
    }

    private Bitmap createDrawable() {
        int i = this.width;
        int i2 = 100;
        if (i <= 0) {
            i = 100;
        }
        int i3 = this.height;
        if (i3 > 0) {
            i2 = i3;
        }
        buildDrawingCache();
        Bitmap bitmap = this.mLastBitmapCreated;
        if (bitmap == null || bitmap.isRecycled() || bitmap.getWidth() != i || bitmap.getHeight() != i2) {
            bitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
            this.mLastBitmapCreated = bitmap;
        } else {
            bitmap.eraseColor(0);
        }
        draw(new Canvas(bitmap));
        return bitmap;
    }

    public void setCalloutView(AirMapCallout airMapCallout) {
        this.calloutView = airMapCallout;
    }

    public AirMapCallout getCalloutView() {
        return this.calloutView;
    }

    public View getCallout() {
        if (this.calloutView == null) {
            return null;
        }
        if (this.wrappedCalloutView == null) {
            wrapCalloutView();
        }
        if (this.calloutView.getTooltip()) {
            return this.wrappedCalloutView;
        }
        return null;
    }

    public View getInfoContents() {
        if (this.calloutView == null) {
            return null;
        }
        if (this.wrappedCalloutView == null) {
            wrapCalloutView();
        }
        if (this.calloutView.getTooltip()) {
            return null;
        }
        return this.wrappedCalloutView;
    }

    private void wrapCalloutView() {
        AirMapCallout airMapCallout = this.calloutView;
        if (airMapCallout != null && airMapCallout.getChildCount() != 0) {
            LinearLayout linearLayout = new LinearLayout(this.context);
            linearLayout.setOrientation(1);
            linearLayout.setLayoutParams(new LayoutParams(this.calloutView.width, this.calloutView.height, 0.0f));
            LinearLayout linearLayout2 = new LinearLayout(this.context);
            linearLayout2.setOrientation(0);
            linearLayout2.setLayoutParams(new LayoutParams(this.calloutView.width, this.calloutView.height, 0.0f));
            linearLayout.addView(linearLayout2);
            linearLayout2.addView(this.calloutView);
            this.wrappedCalloutView = linearLayout;
        }
    }

    private int getDrawableResourceByName(String str) {
        return getResources().getIdentifier(str, "drawable", getContext().getPackageName());
    }

    private BitmapDescriptor getBitmapDescriptorByName(String str) {
        return BitmapDescriptorFactory.fromResource(getDrawableResourceByName(str));
    }
}
