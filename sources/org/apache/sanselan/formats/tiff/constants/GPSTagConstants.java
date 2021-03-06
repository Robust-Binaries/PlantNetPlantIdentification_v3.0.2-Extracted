package org.apache.sanselan.formats.tiff.constants;

import org.apache.sanselan.formats.tiff.constants.TagInfo.Text;

public interface GPSTagConstants extends TiffDirectoryConstants, TiffFieldTypeConstants {
    public static final TagInfo[] ALL_GPS_TAGS = {GPS_TAG_GPS_VERSION_ID, GPS_TAG_GPS_LATITUDE_REF, GPS_TAG_GPS_LATITUDE, GPS_TAG_GPS_LONGITUDE_REF, GPS_TAG_GPS_LONGITUDE, GPS_TAG_GPS_ALTITUDE_REF, GPS_TAG_GPS_ALTITUDE, GPS_TAG_GPS_TIME_STAMP, GPS_TAG_GPS_SATELLITES, GPS_TAG_GPS_STATUS, GPS_TAG_GPS_MEASURE_MODE, GPS_TAG_GPS_DOP, GPS_TAG_GPS_SPEED_REF, GPS_TAG_GPS_SPEED, GPS_TAG_GPS_TRACK_REF, GPS_TAG_GPS_TRACK, GPS_TAG_GPS_IMG_DIRECTION_REF, GPS_TAG_GPS_IMG_DIRECTION, GPS_TAG_GPS_MAP_DATUM, GPS_TAG_GPS_DEST_LATITUDE_REF, GPS_TAG_GPS_DEST_LATITUDE, GPS_TAG_GPS_DEST_LONGITUDE_REF, GPS_TAG_GPS_DEST_LONGITUDE, GPS_TAG_GPS_DEST_BEARING_REF, GPS_TAG_GPS_DEST_BEARING, GPS_TAG_GPS_DEST_DISTANCE_REF, GPS_TAG_GPS_DEST_DISTANCE, GPS_TAG_GPS_PROCESSING_METHOD, GPS_TAG_GPS_AREA_INFORMATION, GPS_TAG_GPS_DATE_STAMP, GPS_TAG_GPS_DIFFERENTIAL};
    public static final TagInfo GPS_TAG_GPS_ALTITUDE;
    public static final TagInfo GPS_TAG_GPS_ALTITUDE_REF;
    public static final int GPS_TAG_GPS_ALTITUDE_REF_VALUE_ABOVE_SEA_LEVEL = 0;
    public static final int GPS_TAG_GPS_ALTITUDE_REF_VALUE_BELOW_SEA_LEVEL = 1;
    public static final TagInfo GPS_TAG_GPS_AREA_INFORMATION;
    public static final TagInfo GPS_TAG_GPS_DATE_STAMP;
    public static final TagInfo GPS_TAG_GPS_DEST_BEARING;
    public static final TagInfo GPS_TAG_GPS_DEST_BEARING_REF;
    public static final String GPS_TAG_GPS_DEST_BEARING_REF_VALUE_MAGNETIC_NORTH = "M";
    public static final String GPS_TAG_GPS_DEST_BEARING_REF_VALUE_TRUE_NORTH = "T";
    public static final TagInfo GPS_TAG_GPS_DEST_DISTANCE;
    public static final TagInfo GPS_TAG_GPS_DEST_DISTANCE_REF;
    public static final String GPS_TAG_GPS_DEST_DISTANCE_REF_VALUE_KILOMETERS = "K";
    public static final String GPS_TAG_GPS_DEST_DISTANCE_REF_VALUE_MILES = "M";
    public static final String GPS_TAG_GPS_DEST_DISTANCE_REF_VALUE_NAUTICAL_MILES = "N";
    public static final TagInfo GPS_TAG_GPS_DEST_LATITUDE;
    public static final TagInfo GPS_TAG_GPS_DEST_LATITUDE_REF;
    public static final String GPS_TAG_GPS_DEST_LATITUDE_REF_VALUE_NORTH = "N";
    public static final String GPS_TAG_GPS_DEST_LATITUDE_REF_VALUE_SOUTH = "S";
    public static final TagInfo GPS_TAG_GPS_DEST_LONGITUDE;
    public static final TagInfo GPS_TAG_GPS_DEST_LONGITUDE_REF;
    public static final String GPS_TAG_GPS_DEST_LONGITUDE_REF_VALUE_EAST = "E";
    public static final String GPS_TAG_GPS_DEST_LONGITUDE_REF_VALUE_WEST = "W";
    public static final TagInfo GPS_TAG_GPS_DIFFERENTIAL;
    public static final int GPS_TAG_GPS_DIFFERENTIAL_VALUE_DIFFERENTIAL_CORRECTED = 1;
    public static final int GPS_TAG_GPS_DIFFERENTIAL_VALUE_NO_CORRECTION = 0;
    public static final TagInfo GPS_TAG_GPS_DOP;
    public static final TagInfo GPS_TAG_GPS_IMG_DIRECTION;
    public static final TagInfo GPS_TAG_GPS_IMG_DIRECTION_REF;
    public static final String GPS_TAG_GPS_IMG_DIRECTION_REF_VALUE_MAGNETIC_NORTH = "M";
    public static final String GPS_TAG_GPS_IMG_DIRECTION_REF_VALUE_TRUE_NORTH = "T";
    public static final TagInfo GPS_TAG_GPS_LATITUDE;
    public static final TagInfo GPS_TAG_GPS_LATITUDE_REF;
    public static final String GPS_TAG_GPS_LATITUDE_REF_VALUE_NORTH = "N";
    public static final String GPS_TAG_GPS_LATITUDE_REF_VALUE_SOUTH = "S";
    public static final TagInfo GPS_TAG_GPS_LONGITUDE;
    public static final TagInfo GPS_TAG_GPS_LONGITUDE_REF;
    public static final String GPS_TAG_GPS_LONGITUDE_REF_VALUE_EAST = "E";
    public static final String GPS_TAG_GPS_LONGITUDE_REF_VALUE_WEST = "W";
    public static final TagInfo GPS_TAG_GPS_MAP_DATUM;
    public static final TagInfo GPS_TAG_GPS_MEASURE_MODE;
    public static final int GPS_TAG_GPS_MEASURE_MODE_VALUE_2_DIMENSIONAL_MEASUREMENT = 2;
    public static final int GPS_TAG_GPS_MEASURE_MODE_VALUE_3_DIMENSIONAL_MEASUREMENT = 3;
    public static final TagInfo GPS_TAG_GPS_PROCESSING_METHOD;
    public static final TagInfo GPS_TAG_GPS_SATELLITES;
    public static final TagInfo GPS_TAG_GPS_SPEED;
    public static final TagInfo GPS_TAG_GPS_SPEED_REF;
    public static final String GPS_TAG_GPS_SPEED_REF_VALUE_KMPH = "K";
    public static final String GPS_TAG_GPS_SPEED_REF_VALUE_KNOTS = "N";
    public static final String GPS_TAG_GPS_SPEED_REF_VALUE_MPH = "M";
    public static final TagInfo GPS_TAG_GPS_STATUS;
    public static final String GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_INTEROPERABILITY = "V";
    public static final String GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_IN_PROGRESS = "A";
    public static final TagInfo GPS_TAG_GPS_TIME_STAMP;
    public static final TagInfo GPS_TAG_GPS_TRACK;
    public static final TagInfo GPS_TAG_GPS_TRACK_REF;
    public static final String GPS_TAG_GPS_TRACK_REF_VALUE_MAGNETIC_NORTH = "M";
    public static final String GPS_TAG_GPS_TRACK_REF_VALUE_TRUE_NORTH = "T";
    public static final TagInfo GPS_TAG_GPS_VERSION_ID;

    static {
        TagInfo tagInfo = new TagInfo("GPS Version ID", 0, FIELD_TYPE_DESCRIPTION_BYTE, 4, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_VERSION_ID = tagInfo;
        TagInfo tagInfo2 = new TagInfo("GPS Latitude Ref", 1, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_LATITUDE_REF = tagInfo2;
        TagInfo tagInfo3 = new TagInfo("GPS Latitude", 2, FIELD_TYPE_DESCRIPTION_RATIONAL, 3, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_LATITUDE = tagInfo3;
        TagInfo tagInfo4 = new TagInfo("GPS Longitude Ref", 3, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_LONGITUDE_REF = tagInfo4;
        TagInfo tagInfo5 = new TagInfo("GPS Longitude", 4, FIELD_TYPE_DESCRIPTION_RATIONAL, 3, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_LONGITUDE = tagInfo5;
        TagInfo tagInfo6 = new TagInfo("GPS Altitude Ref", 5, FIELD_TYPE_DESCRIPTION_BYTE, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_ALTITUDE_REF = tagInfo6;
        TagInfo tagInfo7 = new TagInfo("GPS Altitude", 6, FIELD_TYPE_DESCRIPTION_RATIONAL, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_ALTITUDE = tagInfo7;
        TagInfo tagInfo8 = new TagInfo("GPS Time Stamp", 7, FIELD_TYPE_DESCRIPTION_RATIONAL, 3, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_TIME_STAMP = tagInfo8;
        TagInfo tagInfo9 = new TagInfo("GPS Satellites", 8, FIELD_TYPE_DESCRIPTION_ASCII, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_SATELLITES = tagInfo9;
        TagInfo tagInfo10 = new TagInfo("GPS Status", 9, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_STATUS = tagInfo10;
        TagInfo tagInfo11 = new TagInfo("GPS Measure Mode", 10, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_MEASURE_MODE = tagInfo11;
        TagInfo tagInfo12 = new TagInfo("GPS DOP", 11, FIELD_TYPE_DESCRIPTION_RATIONAL, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DOP = tagInfo12;
        TagInfo tagInfo13 = new TagInfo("GPS Speed Ref", 12, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_SPEED_REF = tagInfo13;
        TagInfo tagInfo14 = new TagInfo("GPS Speed", 13, FIELD_TYPE_DESCRIPTION_RATIONAL, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_SPEED = tagInfo14;
        TagInfo tagInfo15 = new TagInfo("GPS Track Ref", 14, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_TRACK_REF = tagInfo15;
        TagInfo tagInfo16 = new TagInfo("GPS Track", 15, FIELD_TYPE_DESCRIPTION_RATIONAL, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_TRACK = tagInfo16;
        TagInfo tagInfo17 = new TagInfo("GPS Img Direction Ref", 16, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_IMG_DIRECTION_REF = tagInfo17;
        TagInfo tagInfo18 = new TagInfo("GPS Img Direction", 17, FIELD_TYPE_DESCRIPTION_RATIONAL, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_IMG_DIRECTION = tagInfo18;
        TagInfo tagInfo19 = new TagInfo("GPS Map Datum", 18, FIELD_TYPE_DESCRIPTION_ASCII, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_MAP_DATUM = tagInfo19;
        TagInfo tagInfo20 = new TagInfo("GPS Dest Latitude Ref", 19, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_LATITUDE_REF = tagInfo20;
        TagInfo tagInfo21 = new TagInfo("GPS Dest Latitude", 20, FIELD_TYPE_DESCRIPTION_RATIONAL, 3, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_LATITUDE = tagInfo21;
        TagInfo tagInfo22 = new TagInfo("GPS Dest Longitude Ref", 21, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_LONGITUDE_REF = tagInfo22;
        TagInfo tagInfo23 = new TagInfo("GPS Dest Longitude", 22, FIELD_TYPE_DESCRIPTION_RATIONAL, 3, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_LONGITUDE = tagInfo23;
        TagInfo tagInfo24 = new TagInfo("GPS Dest Bearing Ref", 23, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_BEARING_REF = tagInfo24;
        TagInfo tagInfo25 = new TagInfo("GPS Dest Bearing", 24, FIELD_TYPE_DESCRIPTION_RATIONAL, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_BEARING = tagInfo25;
        TagInfo tagInfo26 = new TagInfo("GPS Dest Distance Ref", 25, FIELD_TYPE_DESCRIPTION_ASCII, 2, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_DISTANCE_REF = tagInfo26;
        TagInfo tagInfo27 = new TagInfo("GPS Dest Distance", 26, FIELD_TYPE_DESCRIPTION_RATIONAL, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DEST_DISTANCE = tagInfo27;
        Text text = new Text("GPS Processing Method", 27, FIELD_TYPE_DESCRIPTION_UNKNOWN, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_PROCESSING_METHOD = text;
        Text text2 = new Text("GPS Area Information", 28, FIELD_TYPE_DESCRIPTION_UNKNOWN, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_AREA_INFORMATION = text2;
        TagInfo tagInfo28 = new TagInfo("GPS Date Stamp", 29, FIELD_TYPE_DESCRIPTION_ASCII, 11, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DATE_STAMP = tagInfo28;
        TagInfo tagInfo29 = new TagInfo("GPS Differential", 30, FIELD_TYPE_DESCRIPTION_SHORT, -1, EXIF_DIRECTORY_GPS);
        GPS_TAG_GPS_DIFFERENTIAL = tagInfo29;
    }
}
