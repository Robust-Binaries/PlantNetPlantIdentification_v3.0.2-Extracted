package com.google.maps.android.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.p000v4.util.LruCache;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.google.maps.android.C1147R;
import com.google.maps.android.data.geojson.BiMultiMap;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLineString;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;
import com.google.maps.android.data.geojson.GeoJsonMultiLineString;
import com.google.maps.android.data.geojson.GeoJsonMultiPoint;
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlGroundOverlay;
import com.google.maps.android.data.kml.KmlMultiGeometry;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Renderer {
    private static final Object FEATURE_NOT_ON_MAP = null;
    private static final int LRU_CACHE_SIZE = 50;
    private BiMultiMap<Feature> mContainerFeatures;
    private ArrayList<KmlContainer> mContainers;
    /* access modifiers changed from: private */
    public Context mContext;
    private final GeoJsonLineStringStyle mDefaultLineStringStyle;
    private final GeoJsonPointStyle mDefaultPointStyle;
    private final GeoJsonPolygonStyle mDefaultPolygonStyle;
    private final BiMultiMap<Feature> mFeatures = new BiMultiMap<>();
    private HashMap<KmlGroundOverlay, GroundOverlay> mGroundOverlays;
    private final LruCache<String, Bitmap> mImagesCache;
    private boolean mLayerOnMap;
    private GoogleMap mMap;
    private final ArrayList<String> mMarkerIconUrls;
    private HashMap<String, String> mStyleMaps;
    private HashMap<String, KmlStyle> mStyles;
    private HashMap<String, KmlStyle> mStylesRenderer;

    public Renderer(GoogleMap googleMap, Context context) {
        this.mMap = googleMap;
        this.mContext = context;
        this.mLayerOnMap = false;
        this.mImagesCache = new LruCache<>(50);
        this.mMarkerIconUrls = new ArrayList<>();
        this.mStylesRenderer = new HashMap<>();
        this.mDefaultPointStyle = null;
        this.mDefaultLineStringStyle = null;
        this.mDefaultPolygonStyle = null;
        this.mContainerFeatures = new BiMultiMap<>();
    }

    public Renderer(GoogleMap googleMap, HashMap<? extends Feature, Object> hashMap) {
        this.mMap = googleMap;
        this.mFeatures.putAll(hashMap);
        this.mLayerOnMap = false;
        this.mMarkerIconUrls = null;
        this.mDefaultPointStyle = new GeoJsonPointStyle();
        this.mDefaultLineStringStyle = new GeoJsonLineStringStyle();
        this.mDefaultPolygonStyle = new GeoJsonPolygonStyle();
        this.mImagesCache = null;
        this.mContainerFeatures = null;
    }

    public boolean isLayerOnMap() {
        return this.mLayerOnMap;
    }

    /* access modifiers changed from: protected */
    public void setLayerVisibility(boolean z) {
        this.mLayerOnMap = z;
    }

    public GoogleMap getMap() {
        return this.mMap;
    }

    public void setMap(GoogleMap googleMap) {
        this.mMap = googleMap;
    }

    /* access modifiers changed from: protected */
    public void putContainerFeature(Object obj, Feature feature) {
        this.mContainerFeatures.put(feature, obj);
    }

    public Set<Feature> getFeatures() {
        return this.mFeatures.keySet();
    }

    public Feature getFeature(Object obj) {
        return (Feature) this.mFeatures.getKey(obj);
    }

    public Feature getContainerFeature(Object obj) {
        BiMultiMap<Feature> biMultiMap = this.mContainerFeatures;
        if (biMultiMap != null) {
            return (Feature) biMultiMap.getKey(obj);
        }
        return null;
    }

    public Collection<Object> getValues() {
        return this.mFeatures.values();
    }

    /* access modifiers changed from: protected */
    public HashMap<? extends Feature, Object> getAllFeatures() {
        return this.mFeatures;
    }

    public ArrayList<String> getMarkerIconUrls() {
        return this.mMarkerIconUrls;
    }

    public HashMap<String, KmlStyle> getStylesRenderer() {
        return this.mStylesRenderer;
    }

    public HashMap<String, String> getStyleMaps() {
        return this.mStyleMaps;
    }

    public LruCache<String, Bitmap> getImagesCache() {
        return this.mImagesCache;
    }

    public HashMap<KmlGroundOverlay, GroundOverlay> getGroundOverlayMap() {
        return this.mGroundOverlays;
    }

    public ArrayList<KmlContainer> getContainerList() {
        return this.mContainers;
    }

    /* access modifiers changed from: protected */
    public KmlStyle getPlacemarkStyle(String str) {
        return this.mStylesRenderer.get(str) != null ? (KmlStyle) this.mStylesRenderer.get(str) : (KmlStyle) this.mStylesRenderer.get(null);
    }

    public GeoJsonPointStyle getDefaultPointStyle() {
        return this.mDefaultPointStyle;
    }

    public GeoJsonLineStringStyle getDefaultLineStringStyle() {
        return this.mDefaultLineStringStyle;
    }

    public GeoJsonPolygonStyle getDefaultPolygonStyle() {
        return this.mDefaultPolygonStyle;
    }

    public void putFeatures(Feature feature, Object obj) {
        this.mFeatures.put(feature, obj);
    }

    public void putStyles() {
        this.mStylesRenderer.putAll(this.mStyles);
    }

    public void putStyles(HashMap<String, KmlStyle> hashMap) {
        this.mStylesRenderer.putAll(hashMap);
    }

    public void putImagesCache(String str, Bitmap bitmap) {
        this.mImagesCache.put(str, bitmap);
    }

    public boolean hasFeatures() {
        return this.mFeatures.size() > 0;
    }

    protected static void removeFeatures(HashMap<Feature, Object> hashMap) {
        for (Object next : hashMap.values()) {
            if (next instanceof Marker) {
                ((Marker) next).remove();
            } else if (next instanceof Polyline) {
                ((Polyline) next).remove();
            } else if (next instanceof Polygon) {
                ((Polygon) next).remove();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void removeFeature(Feature feature) {
        if (this.mFeatures.containsKey(feature)) {
            removeFromMap(this.mFeatures.remove(feature));
        }
    }

    private void setFeatureDefaultStyles(GeoJsonFeature geoJsonFeature) {
        if (geoJsonFeature.getPointStyle() == null) {
            geoJsonFeature.setPointStyle(this.mDefaultPointStyle);
        }
        if (geoJsonFeature.getLineStringStyle() == null) {
            geoJsonFeature.setLineStringStyle(this.mDefaultLineStringStyle);
        }
        if (geoJsonFeature.getPolygonStyle() == null) {
            geoJsonFeature.setPolygonStyle(this.mDefaultPolygonStyle);
        }
    }

    public void clearStylesRenderer() {
        this.mStylesRenderer.clear();
    }

    /* access modifiers changed from: protected */
    public void storeData(HashMap<String, KmlStyle> hashMap, HashMap<String, String> hashMap2, HashMap<KmlPlacemark, Object> hashMap3, ArrayList<KmlContainer> arrayList, HashMap<KmlGroundOverlay, GroundOverlay> hashMap4) {
        this.mStyles = hashMap;
        this.mStyleMaps = hashMap2;
        this.mFeatures.putAll(hashMap3);
        this.mContainers = arrayList;
        this.mGroundOverlays = hashMap4;
    }

    public void addFeature(Feature feature) {
        Object obj = FEATURE_NOT_ON_MAP;
        if (feature instanceof GeoJsonFeature) {
            setFeatureDefaultStyles((GeoJsonFeature) feature);
        }
        if (this.mLayerOnMap) {
            if (this.mFeatures.containsKey(feature)) {
                removeFromMap(this.mFeatures.get(feature));
            }
            if (feature.hasGeometry()) {
                if (feature instanceof KmlPlacemark) {
                    KmlPlacemark kmlPlacemark = (KmlPlacemark) feature;
                    obj = addKmlPlacemarkToMap(kmlPlacemark, feature.getGeometry(), getPlacemarkStyle(feature.getId()), kmlPlacemark.getInlineStyle(), getPlacemarkVisibility(feature));
                } else {
                    obj = addGeoJsonFeatureToMap(feature, feature.getGeometry());
                }
            }
        }
        this.mFeatures.put(feature, obj);
    }

    public static void removeFromMap(Object obj) {
        if (obj instanceof Marker) {
            ((Marker) obj).remove();
        } else if (obj instanceof Polyline) {
            ((Polyline) obj).remove();
        } else if (obj instanceof Polygon) {
            ((Polygon) obj).remove();
        } else if (obj instanceof ArrayList) {
            Iterator it = ((ArrayList) obj).iterator();
            while (it.hasNext()) {
                removeFromMap(it.next());
            }
        }
    }

    /* JADX WARNING: type inference failed for: r1v1 */
    /* JADX WARNING: type inference failed for: r1v2, types: [com.google.android.gms.maps.model.MarkerOptions] */
    /* JADX WARNING: type inference failed for: r1v3, types: [com.google.android.gms.maps.model.MarkerOptions] */
    /* JADX WARNING: type inference failed for: r1v4, types: [com.google.android.gms.maps.model.MarkerOptions] */
    /* JADX WARNING: type inference failed for: r1v5, types: [com.google.android.gms.maps.model.PolylineOptions] */
    /* JADX WARNING: type inference failed for: r1v6, types: [com.google.android.gms.maps.model.PolylineOptions] */
    /* JADX WARNING: type inference failed for: r1v7, types: [com.google.android.gms.maps.model.PolylineOptions] */
    /* JADX WARNING: type inference failed for: r1v8, types: [com.google.android.gms.maps.model.PolygonOptions] */
    /* JADX WARNING: type inference failed for: r1v9, types: [com.google.android.gms.maps.model.PolygonOptions] */
    /* JADX WARNING: type inference failed for: r1v10, types: [com.google.android.gms.maps.model.PolygonOptions] */
    /* JADX WARNING: type inference failed for: r1v18 */
    /* JADX WARNING: type inference failed for: r1v19 */
    /* JADX WARNING: type inference failed for: r1v20 */
    /* JADX WARNING: type inference failed for: r1v21 */
    /* JADX WARNING: type inference failed for: r1v22 */
    /* JADX WARNING: type inference failed for: r1v23 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v1
      assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], com.google.android.gms.maps.model.PolylineOptions, com.google.android.gms.maps.model.MarkerOptions, com.google.android.gms.maps.model.PolygonOptions]
      uses: [com.google.android.gms.maps.model.MarkerOptions, com.google.android.gms.maps.model.PolylineOptions, com.google.android.gms.maps.model.PolygonOptions]
      mth insns count: 94
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 7 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object addGeoJsonFeatureToMap(com.google.maps.android.data.Feature r3, com.google.maps.android.data.Geometry r4) {
        /*
            r2 = this;
            java.lang.String r0 = r4.getGeometryType()
            int r1 = r0.hashCode()
            switch(r1) {
                case -2116761119: goto L_0x0048;
                case -1065891849: goto L_0x003e;
                case -627102946: goto L_0x0034;
                case 77292912: goto L_0x002a;
                case 1267133722: goto L_0x0020;
                case 1806700869: goto L_0x0016;
                case 1950410960: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x0052
        L_0x000c:
            java.lang.String r1 = "GeometryCollection"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 6
            goto L_0x0053
        L_0x0016:
            java.lang.String r1 = "LineString"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 1
            goto L_0x0053
        L_0x0020:
            java.lang.String r1 = "Polygon"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 2
            goto L_0x0053
        L_0x002a:
            java.lang.String r1 = "Point"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 0
            goto L_0x0053
        L_0x0034:
            java.lang.String r1 = "MultiLineString"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 4
            goto L_0x0053
        L_0x003e:
            java.lang.String r1 = "MultiPoint"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 3
            goto L_0x0053
        L_0x0048:
            java.lang.String r1 = "MultiPolygon"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0052
            r0 = 5
            goto L_0x0053
        L_0x0052:
            r0 = -1
        L_0x0053:
            r1 = 0
            switch(r0) {
                case 0: goto L_0x00c4;
                case 1: goto L_0x00a8;
                case 2: goto L_0x008c;
                case 3: goto L_0x007f;
                case 4: goto L_0x0072;
                case 5: goto L_0x0065;
                case 6: goto L_0x0058;
                default: goto L_0x0057;
            }
        L_0x0057:
            return r1
        L_0x0058:
            com.google.maps.android.data.geojson.GeoJsonFeature r3 = (com.google.maps.android.data.geojson.GeoJsonFeature) r3
            com.google.maps.android.data.geojson.GeoJsonGeometryCollection r4 = (com.google.maps.android.data.geojson.GeoJsonGeometryCollection) r4
            java.util.List r4 = r4.getGeometries()
            java.util.ArrayList r3 = r2.addGeometryCollectionToMap(r3, r4)
            return r3
        L_0x0065:
            com.google.maps.android.data.geojson.GeoJsonFeature r3 = (com.google.maps.android.data.geojson.GeoJsonFeature) r3
            com.google.maps.android.data.geojson.GeoJsonPolygonStyle r3 = r3.getPolygonStyle()
            com.google.maps.android.data.geojson.GeoJsonMultiPolygon r4 = (com.google.maps.android.data.geojson.GeoJsonMultiPolygon) r4
            java.util.ArrayList r3 = r2.addMultiPolygonToMap(r3, r4)
            return r3
        L_0x0072:
            com.google.maps.android.data.geojson.GeoJsonFeature r3 = (com.google.maps.android.data.geojson.GeoJsonFeature) r3
            com.google.maps.android.data.geojson.GeoJsonLineStringStyle r3 = r3.getLineStringStyle()
            com.google.maps.android.data.geojson.GeoJsonMultiLineString r4 = (com.google.maps.android.data.geojson.GeoJsonMultiLineString) r4
            java.util.ArrayList r3 = r2.addMultiLineStringToMap(r3, r4)
            return r3
        L_0x007f:
            com.google.maps.android.data.geojson.GeoJsonFeature r3 = (com.google.maps.android.data.geojson.GeoJsonFeature) r3
            com.google.maps.android.data.geojson.GeoJsonPointStyle r3 = r3.getPointStyle()
            com.google.maps.android.data.geojson.GeoJsonMultiPoint r4 = (com.google.maps.android.data.geojson.GeoJsonMultiPoint) r4
            java.util.ArrayList r3 = r2.addMultiPointToMap(r3, r4)
            return r3
        L_0x008c:
            boolean r0 = r3 instanceof com.google.maps.android.data.geojson.GeoJsonFeature
            if (r0 == 0) goto L_0x0097
            com.google.maps.android.data.geojson.GeoJsonFeature r3 = (com.google.maps.android.data.geojson.GeoJsonFeature) r3
            com.google.android.gms.maps.model.PolygonOptions r1 = r3.getPolygonOptions()
            goto L_0x00a1
        L_0x0097:
            boolean r0 = r3 instanceof com.google.maps.android.data.kml.KmlPlacemark
            if (r0 == 0) goto L_0x00a1
            com.google.maps.android.data.kml.KmlPlacemark r3 = (com.google.maps.android.data.kml.KmlPlacemark) r3
            com.google.android.gms.maps.model.PolygonOptions r1 = r3.getPolygonOptions()
        L_0x00a1:
            com.google.maps.android.data.DataPolygon r4 = (com.google.maps.android.data.DataPolygon) r4
            com.google.android.gms.maps.model.Polygon r3 = r2.addPolygonToMap(r1, r4)
            return r3
        L_0x00a8:
            boolean r0 = r3 instanceof com.google.maps.android.data.geojson.GeoJsonFeature
            if (r0 == 0) goto L_0x00b3
            com.google.maps.android.data.geojson.GeoJsonFeature r3 = (com.google.maps.android.data.geojson.GeoJsonFeature) r3
            com.google.android.gms.maps.model.PolylineOptions r1 = r3.getPolylineOptions()
            goto L_0x00bd
        L_0x00b3:
            boolean r0 = r3 instanceof com.google.maps.android.data.kml.KmlPlacemark
            if (r0 == 0) goto L_0x00bd
            com.google.maps.android.data.kml.KmlPlacemark r3 = (com.google.maps.android.data.kml.KmlPlacemark) r3
            com.google.android.gms.maps.model.PolylineOptions r1 = r3.getPolylineOptions()
        L_0x00bd:
            com.google.maps.android.data.geojson.GeoJsonLineString r4 = (com.google.maps.android.data.geojson.GeoJsonLineString) r4
            com.google.android.gms.maps.model.Polyline r3 = r2.addLineStringToMap(r1, r4)
            return r3
        L_0x00c4:
            boolean r0 = r3 instanceof com.google.maps.android.data.geojson.GeoJsonFeature
            if (r0 == 0) goto L_0x00cf
            com.google.maps.android.data.geojson.GeoJsonFeature r3 = (com.google.maps.android.data.geojson.GeoJsonFeature) r3
            com.google.android.gms.maps.model.MarkerOptions r1 = r3.getMarkerOptions()
            goto L_0x00d9
        L_0x00cf:
            boolean r0 = r3 instanceof com.google.maps.android.data.kml.KmlPlacemark
            if (r0 == 0) goto L_0x00d9
            com.google.maps.android.data.kml.KmlPlacemark r3 = (com.google.maps.android.data.kml.KmlPlacemark) r3
            com.google.android.gms.maps.model.MarkerOptions r1 = r3.getMarkerOptions()
        L_0x00d9:
            com.google.maps.android.data.geojson.GeoJsonPoint r4 = (com.google.maps.android.data.geojson.GeoJsonPoint) r4
            com.google.android.gms.maps.model.Marker r3 = r2.addPointToMap(r1, r4)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.maps.android.data.Renderer.addGeoJsonFeatureToMap(com.google.maps.android.data.Feature, com.google.maps.android.data.Geometry):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0058, code lost:
        if (r0.equals("Point") != false) goto L_0x005c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x005f  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x006e  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0098  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00c2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object addKmlPlacemarkToMap(com.google.maps.android.data.kml.KmlPlacemark r11, com.google.maps.android.data.Geometry r12, com.google.maps.android.data.kml.KmlStyle r13, com.google.maps.android.data.kml.KmlStyle r14, boolean r15) {
        /*
            r10 = this;
            java.lang.String r0 = r12.getGeometryType()
            java.lang.String r1 = "drawOrder"
            boolean r1 = r11.hasProperty(r1)
            r2 = 0
            r3 = 0
            if (r1 == 0) goto L_0x001a
            java.lang.String r4 = "drawOrder"
            java.lang.String r4 = r11.getProperty(r4)     // Catch:{ NumberFormatException -> 0x0019 }
            float r3 = java.lang.Float.parseFloat(r4)     // Catch:{ NumberFormatException -> 0x0019 }
            goto L_0x001a
        L_0x0019:
            r1 = 0
        L_0x001a:
            r4 = -1
            int r5 = r0.hashCode()
            r6 = 77292912(0x49b6570, float:3.653348E-36)
            if (r5 == r6) goto L_0x0052
            r2 = 89139371(0x55028ab, float:9.7875825E-36)
            if (r5 == r2) goto L_0x0048
            r2 = 1267133722(0x4b86ed1a, float:1.7685044E7)
            if (r5 == r2) goto L_0x003e
            r2 = 1806700869(0x6bb01145, float:4.25705E26)
            if (r5 == r2) goto L_0x0034
            goto L_0x005b
        L_0x0034:
            java.lang.String r2 = "LineString"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x005b
            r2 = 1
            goto L_0x005c
        L_0x003e:
            java.lang.String r2 = "Polygon"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x005b
            r2 = 2
            goto L_0x005c
        L_0x0048:
            java.lang.String r2 = "MultiGeometry"
            boolean r0 = r0.equals(r2)
            if (r0 == 0) goto L_0x005b
            r2 = 3
            goto L_0x005c
        L_0x0052:
            java.lang.String r5 = "Point"
            boolean r0 = r0.equals(r5)
            if (r0 == 0) goto L_0x005b
            goto L_0x005c
        L_0x005b:
            r2 = -1
        L_0x005c:
            switch(r2) {
                case 0: goto L_0x00c2;
                case 1: goto L_0x0098;
                case 2: goto L_0x006e;
                case 3: goto L_0x0061;
                default: goto L_0x005f;
            }
        L_0x005f:
            r11 = 0
            return r11
        L_0x0061:
            r6 = r12
            com.google.maps.android.data.kml.KmlMultiGeometry r6 = (com.google.maps.android.data.kml.KmlMultiGeometry) r6
            r4 = r10
            r5 = r11
            r7 = r13
            r8 = r14
            r9 = r15
            java.util.ArrayList r11 = r4.addMultiGeometryToMap(r5, r6, r7, r8, r9)
            return r11
        L_0x006e:
            com.google.android.gms.maps.model.PolygonOptions r11 = r13.getPolygonOptions()
            if (r14 == 0) goto L_0x0078
            r10.setInlinePolygonStyle(r11, r14)
            goto L_0x0089
        L_0x0078:
            boolean r13 = r13.isPolyRandomColorMode()
            if (r13 == 0) goto L_0x0089
            int r13 = r11.getFillColor()
            int r13 = com.google.maps.android.data.kml.KmlStyle.computeRandomColor(r13)
            r11.fillColor(r13)
        L_0x0089:
            com.google.maps.android.data.DataPolygon r12 = (com.google.maps.android.data.DataPolygon) r12
            com.google.android.gms.maps.model.Polygon r11 = r10.addPolygonToMap(r11, r12)
            r11.setVisible(r15)
            if (r1 == 0) goto L_0x0097
            r11.setZIndex(r3)
        L_0x0097:
            return r11
        L_0x0098:
            com.google.android.gms.maps.model.PolylineOptions r11 = r13.getPolylineOptions()
            if (r14 == 0) goto L_0x00a2
            r10.setInlineLineStringStyle(r11, r14)
            goto L_0x00b3
        L_0x00a2:
            boolean r13 = r13.isLineRandomColorMode()
            if (r13 == 0) goto L_0x00b3
            int r13 = r11.getColor()
            int r13 = com.google.maps.android.data.kml.KmlStyle.computeRandomColor(r13)
            r11.color(r13)
        L_0x00b3:
            com.google.maps.android.data.LineString r12 = (com.google.maps.android.data.LineString) r12
            com.google.android.gms.maps.model.Polyline r11 = r10.addLineStringToMap(r11, r12)
            r11.setVisible(r15)
            if (r1 == 0) goto L_0x00c1
            r11.setZIndex(r3)
        L_0x00c1:
            return r11
        L_0x00c2:
            com.google.android.gms.maps.model.MarkerOptions r0 = r13.getMarkerOptions()
            if (r14 == 0) goto L_0x00d0
            java.lang.String r2 = r13.getIconUrl()
            r10.setInlinePointStyle(r0, r14, r2)
            goto L_0x00dd
        L_0x00d0:
            java.lang.String r14 = r13.getIconUrl()
            if (r14 == 0) goto L_0x00dd
            java.lang.String r14 = r13.getIconUrl()
            r10.addMarkerIcons(r14, r0)
        L_0x00dd:
            com.google.maps.android.data.kml.KmlPoint r12 = (com.google.maps.android.data.kml.KmlPoint) r12
            com.google.android.gms.maps.model.Marker r12 = r10.addPointToMap(r0, r12)
            r12.setVisible(r15)
            r10.setMarkerInfoWindow(r13, r12, r11)
            if (r1 == 0) goto L_0x00ee
            r12.setZIndex(r3)
        L_0x00ee:
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.maps.android.data.Renderer.addKmlPlacemarkToMap(com.google.maps.android.data.kml.KmlPlacemark, com.google.maps.android.data.Geometry, com.google.maps.android.data.kml.KmlStyle, com.google.maps.android.data.kml.KmlStyle, boolean):java.lang.Object");
    }

    /* access modifiers changed from: protected */
    public Marker addPointToMap(MarkerOptions markerOptions, Point point) {
        markerOptions.position(point.getGeometryObject());
        return this.mMap.addMarker(markerOptions);
    }

    private void setInlinePointStyle(MarkerOptions markerOptions, KmlStyle kmlStyle, String str) {
        MarkerOptions markerOptions2 = kmlStyle.getMarkerOptions();
        if (kmlStyle.isStyleSet("heading")) {
            markerOptions.rotation(markerOptions2.getRotation());
        }
        if (kmlStyle.isStyleSet("hotSpot")) {
            markerOptions.anchor(markerOptions2.getAnchorU(), markerOptions2.getAnchorV());
        }
        if (kmlStyle.isStyleSet("markerColor")) {
            markerOptions.icon(markerOptions2.getIcon());
        }
        if (kmlStyle.isStyleSet("iconUrl")) {
            addMarkerIcons(kmlStyle.getIconUrl(), markerOptions);
        } else if (str != null) {
            addMarkerIcons(str, markerOptions);
        }
    }

    /* access modifiers changed from: protected */
    public Polyline addLineStringToMap(PolylineOptions polylineOptions, LineString lineString) {
        polylineOptions.addAll(lineString.getGeometryObject());
        Polyline addPolyline = this.mMap.addPolyline(polylineOptions);
        addPolyline.setClickable(true);
        return addPolyline;
    }

    private void setInlineLineStringStyle(PolylineOptions polylineOptions, KmlStyle kmlStyle) {
        PolylineOptions polylineOptions2 = kmlStyle.getPolylineOptions();
        if (kmlStyle.isStyleSet("outlineColor")) {
            polylineOptions.color(polylineOptions2.getColor());
        }
        if (kmlStyle.isStyleSet("width")) {
            polylineOptions.width(polylineOptions2.getWidth());
        }
        if (kmlStyle.isLineRandomColorMode()) {
            polylineOptions.color(KmlStyle.computeRandomColor(polylineOptions2.getColor()));
        }
    }

    /* access modifiers changed from: protected */
    public Polygon addPolygonToMap(PolygonOptions polygonOptions, DataPolygon dataPolygon) {
        polygonOptions.addAll(dataPolygon.getOuterBoundaryCoordinates());
        for (List addHole : dataPolygon.getInnerBoundaryCoordinates()) {
            polygonOptions.addHole(addHole);
        }
        Polygon addPolygon = this.mMap.addPolygon(polygonOptions);
        addPolygon.setClickable(true);
        return addPolygon;
    }

    private void setInlinePolygonStyle(PolygonOptions polygonOptions, KmlStyle kmlStyle) {
        PolygonOptions polygonOptions2 = kmlStyle.getPolygonOptions();
        if (kmlStyle.hasFill() && kmlStyle.isStyleSet("fillColor")) {
            polygonOptions.fillColor(polygonOptions2.getFillColor());
        }
        if (kmlStyle.hasOutline()) {
            if (kmlStyle.isStyleSet("outlineColor")) {
                polygonOptions.strokeColor(polygonOptions2.getStrokeColor());
            }
            if (kmlStyle.isStyleSet("width")) {
                polygonOptions.strokeWidth(polygonOptions2.getStrokeWidth());
            }
        }
        if (kmlStyle.isPolyRandomColorMode()) {
            polygonOptions.fillColor(KmlStyle.computeRandomColor(polygonOptions2.getFillColor()));
        }
    }

    private ArrayList<Object> addGeometryCollectionToMap(GeoJsonFeature geoJsonFeature, List<Geometry> list) {
        ArrayList<Object> arrayList = new ArrayList<>();
        for (Geometry addGeoJsonFeatureToMap : list) {
            arrayList.add(addGeoJsonFeatureToMap(geoJsonFeature, addGeoJsonFeatureToMap));
        }
        return arrayList;
    }

    protected static boolean getPlacemarkVisibility(Feature feature) {
        return !feature.hasProperty("visibility") || Integer.parseInt(feature.getProperty("visibility")) != 0;
    }

    public void assignStyleMap(HashMap<String, String> hashMap, HashMap<String, KmlStyle> hashMap2) {
        for (String str : hashMap.keySet()) {
            String str2 = (String) hashMap.get(str);
            if (hashMap2.containsKey(str2)) {
                hashMap2.put(str, hashMap2.get(str2));
            }
        }
    }

    private ArrayList<Object> addMultiGeometryToMap(KmlPlacemark kmlPlacemark, KmlMultiGeometry kmlMultiGeometry, KmlStyle kmlStyle, KmlStyle kmlStyle2, boolean z) {
        ArrayList<Object> arrayList = new ArrayList<>();
        Iterator it = kmlMultiGeometry.getGeometryObject().iterator();
        while (it.hasNext()) {
            arrayList.add(addKmlPlacemarkToMap(kmlPlacemark, (Geometry) it.next(), kmlStyle, kmlStyle2, z));
        }
        return arrayList;
    }

    private ArrayList<Marker> addMultiPointToMap(GeoJsonPointStyle geoJsonPointStyle, GeoJsonMultiPoint geoJsonMultiPoint) {
        ArrayList<Marker> arrayList = new ArrayList<>();
        for (GeoJsonPoint addPointToMap : geoJsonMultiPoint.getPoints()) {
            arrayList.add(addPointToMap(geoJsonPointStyle.toMarkerOptions(), addPointToMap));
        }
        return arrayList;
    }

    private ArrayList<Polyline> addMultiLineStringToMap(GeoJsonLineStringStyle geoJsonLineStringStyle, GeoJsonMultiLineString geoJsonMultiLineString) {
        ArrayList<Polyline> arrayList = new ArrayList<>();
        for (GeoJsonLineString addLineStringToMap : geoJsonMultiLineString.getLineStrings()) {
            arrayList.add(addLineStringToMap(geoJsonLineStringStyle.toPolylineOptions(), addLineStringToMap));
        }
        return arrayList;
    }

    private ArrayList<Polygon> addMultiPolygonToMap(GeoJsonPolygonStyle geoJsonPolygonStyle, GeoJsonMultiPolygon geoJsonMultiPolygon) {
        ArrayList<Polygon> arrayList = new ArrayList<>();
        for (GeoJsonPolygon addPolygonToMap : geoJsonMultiPolygon.getPolygons()) {
            arrayList.add(addPolygonToMap(geoJsonPolygonStyle.toPolygonOptions(), addPolygonToMap));
        }
        return arrayList;
    }

    private void addMarkerIcons(String str, MarkerOptions markerOptions) {
        if (this.mImagesCache.get(str) != null) {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap((Bitmap) this.mImagesCache.get(str)));
        } else if (!this.mMarkerIconUrls.contains(str)) {
            this.mMarkerIconUrls.add(str);
        }
    }

    public GroundOverlay attachGroundOverlay(GroundOverlayOptions groundOverlayOptions) {
        return this.mMap.addGroundOverlay(groundOverlayOptions);
    }

    private void setMarkerInfoWindow(KmlStyle kmlStyle, Marker marker, KmlPlacemark kmlPlacemark) {
        boolean hasProperty = kmlPlacemark.hasProperty(ConditionalUserProperty.NAME);
        boolean hasProperty2 = kmlPlacemark.hasProperty("description");
        boolean hasBalloonStyle = kmlStyle.hasBalloonStyle();
        boolean containsKey = kmlStyle.getBalloonOptions().containsKey("text");
        if (hasBalloonStyle && containsKey) {
            marker.setTitle((String) kmlStyle.getBalloonOptions().get("text"));
            createInfoWindow();
        } else if (hasBalloonStyle && hasProperty) {
            marker.setTitle(kmlPlacemark.getProperty(ConditionalUserProperty.NAME));
            createInfoWindow();
        } else if (hasProperty && hasProperty2) {
            marker.setTitle(kmlPlacemark.getProperty(ConditionalUserProperty.NAME));
            marker.setSnippet(kmlPlacemark.getProperty("description"));
            createInfoWindow();
        } else if (hasProperty2) {
            marker.setTitle(kmlPlacemark.getProperty("description"));
            createInfoWindow();
        } else if (hasProperty) {
            marker.setTitle(kmlPlacemark.getProperty(ConditionalUserProperty.NAME));
            createInfoWindow();
        }
    }

    private void createInfoWindow() {
        this.mMap.setInfoWindowAdapter(new InfoWindowAdapter() {
            public View getInfoWindow(Marker marker) {
                return null;
            }

            public View getInfoContents(Marker marker) {
                View inflate = LayoutInflater.from(Renderer.this.mContext).inflate(C1147R.layout.amu_info_window, null);
                TextView textView = (TextView) inflate.findViewById(C1147R.C1150id.window);
                if (marker.getSnippet() != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(marker.getTitle());
                    sb.append("<br>");
                    sb.append(marker.getSnippet());
                    textView.setText(Html.fromHtml(sb.toString()));
                } else {
                    textView.setText(Html.fromHtml(marker.getTitle()));
                }
                return inflate;
            }
        });
    }
}
