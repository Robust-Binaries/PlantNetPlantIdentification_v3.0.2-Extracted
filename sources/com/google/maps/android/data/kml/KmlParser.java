package com.google.maps.android.data.kml;

import com.google.android.gms.maps.model.GroundOverlay;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class KmlParser {
    private static final String CONTAINER_REGEX = "Folder|Document";
    private static final String GROUND_OVERLAY = "GroundOverlay";
    private static final String PLACEMARK = "Placemark";
    private static final String STYLE = "Style";
    private static final String STYLE_MAP = "StyleMap";
    private static final String UNSUPPORTED_REGEX = "altitude|altitudeModeGroup|altitudeMode|begin|bottomFov|cookie|displayName|displayMode|displayMode|end|expires|extrude|flyToView|gridOrigin|httpQuery|leftFov|linkDescription|linkName|linkSnippet|listItemType|maxSnippetLines|maxSessionLength|message|minAltitude|minFadeExtent|minLodPixels|minRefreshPeriod|maxAltitude|maxFadeExtent|maxLodPixels|maxHeight|maxWidth|near|NetworkLink|NetworkLinkControl|overlayXY|range|refreshMode|refreshInterval|refreshVisibility|rightFov|roll|rotationXY|screenXY|shape|sourceHref|state|targetHref|tessellate|tileSize|topFov|viewBoundScale|viewFormat|viewRefreshMode|viewRefreshTime|when";
    private final ArrayList<KmlContainer> mContainers = new ArrayList<>();
    private final HashMap<KmlGroundOverlay, GroundOverlay> mGroundOverlays = new HashMap<>();
    private final XmlPullParser mParser;
    private final HashMap<KmlPlacemark, Object> mPlacemarks = new HashMap<>();
    private final HashMap<String, String> mStyleMaps = new HashMap<>();
    private final HashMap<String, KmlStyle> mStyles = new HashMap<>();

    KmlParser(XmlPullParser xmlPullParser) {
        this.mParser = xmlPullParser;
    }

    /* access modifiers changed from: 0000 */
    public void parseKml() throws XmlPullParserException, IOException {
        int eventType = this.mParser.getEventType();
        while (eventType != 1) {
            if (eventType == 2) {
                if (this.mParser.getName().matches(UNSUPPORTED_REGEX)) {
                    skip(this.mParser);
                }
                if (this.mParser.getName().matches(CONTAINER_REGEX)) {
                    this.mContainers.add(KmlContainerParser.createContainer(this.mParser));
                }
                if (this.mParser.getName().equals(STYLE)) {
                    KmlStyle createStyle = KmlStyleParser.createStyle(this.mParser);
                    this.mStyles.put(createStyle.getStyleId(), createStyle);
                }
                if (this.mParser.getName().equals(STYLE_MAP)) {
                    this.mStyleMaps.putAll(KmlStyleParser.createStyleMap(this.mParser));
                }
                if (this.mParser.getName().equals(PLACEMARK)) {
                    this.mPlacemarks.put(KmlFeatureParser.createPlacemark(this.mParser), null);
                }
                if (this.mParser.getName().equals(GROUND_OVERLAY)) {
                    this.mGroundOverlays.put(KmlFeatureParser.createGroundOverlay(this.mParser), null);
                }
            }
            eventType = this.mParser.next();
        }
        this.mStyles.put(null, new KmlStyle());
    }

    /* access modifiers changed from: 0000 */
    public HashMap<String, KmlStyle> getStyles() {
        return this.mStyles;
    }

    /* access modifiers changed from: 0000 */
    public HashMap<KmlPlacemark, Object> getPlacemarks() {
        return this.mPlacemarks;
    }

    /* access modifiers changed from: 0000 */
    public HashMap<String, String> getStyleMaps() {
        return this.mStyleMaps;
    }

    /* access modifiers changed from: 0000 */
    public ArrayList<KmlContainer> getContainers() {
        return this.mContainers;
    }

    /* access modifiers changed from: 0000 */
    public HashMap<KmlGroundOverlay, GroundOverlay> getGroundOverlays() {
        return this.mGroundOverlays;
    }

    static void skip(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() == 2) {
            int i = 1;
            while (i != 0) {
                switch (xmlPullParser.next()) {
                    case 2:
                        i++;
                        break;
                    case 3:
                        i--;
                        break;
                }
            }
            return;
        }
        throw new IllegalStateException();
    }
}
