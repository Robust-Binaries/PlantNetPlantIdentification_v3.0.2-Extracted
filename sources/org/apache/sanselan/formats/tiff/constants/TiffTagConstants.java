package org.apache.sanselan.formats.tiff.constants;

import android.support.p000v4.view.InputDeviceCompat;
import com.facebook.imagepipeline.common.RotationOptions;
import org.apache.sanselan.formats.psd.PsdImageParser;
import org.apache.sanselan.formats.tiff.constants.TagInfo.Offset;
import org.apache.sanselan.formats.tiff.constants.TagInfo.Unknown;

public interface TiffTagConstants extends TiffDirectoryConstants, TiffFieldTypeConstants {
    public static final TagInfo[] ALL_TIFF_TAGS = {TIFF_TAG_NEW_SUBFILE_TYPE, TIFF_TAG_SUBFILE_TYPE, TIFF_TAG_IMAGE_WIDTH, TIFF_TAG_IMAGE_LENGTH, TIFF_TAG_BITS_PER_SAMPLE, TIFF_TAG_COMPRESSION, TIFF_TAG_PHOTOMETRIC_INTERPRETATION, TIFF_TAG_THRESHHOLDING, TIFF_TAG_CELL_WIDTH, TIFF_TAG_CELL_LENGTH, TIFF_TAG_FILL_ORDER, TIFF_TAG_DOCUMENT_NAME, TIFF_TAG_IMAGE_DESCRIPTION, TIFF_TAG_MAKE, TIFF_TAG_MODEL, TIFF_TAG_STRIP_OFFSETS, TIFF_TAG_ORIENTATION, TIFF_TAG_SAMPLES_PER_PIXEL, TIFF_TAG_ROWS_PER_STRIP, TIFF_TAG_STRIP_BYTE_COUNTS, TIFF_TAG_MIN_SAMPLE_VALUE, TIFF_TAG_MAX_SAMPLE_VALUE, TIFF_TAG_XRESOLUTION, TIFF_TAG_YRESOLUTION, TIFF_TAG_PLANAR_CONFIGURATION, TIFF_TAG_PAGE_NAME, TIFF_TAG_XPOSITION, TIFF_TAG_YPOSITION, TIFF_TAG_FREE_OFFSETS, TIFF_TAG_FREE_BYTE_COUNTS, TIFF_TAG_GRAY_RESPONSE_UNIT, TIFF_TAG_GRAY_RESPONSE_CURVE, TIFF_TAG_T4_OPTIONS, TIFF_TAG_T6_OPTIONS, TIFF_TAG_RESOLUTION_UNIT, TIFF_TAG_PAGE_NUMBER, TIFF_TAG_TRANSFER_FUNCTION, TIFF_TAG_SOFTWARE, TIFF_TAG_DATE_TIME, TIFF_TAG_ARTIST, TIFF_TAG_HOST_COMPUTER, TIFF_TAG_PREDICTOR, TIFF_TAG_WHITE_POINT, TIFF_TAG_PRIMARY_CHROMATICITIES, TIFF_TAG_COLOR_MAP, TIFF_TAG_HALFTONE_HINTS, TIFF_TAG_TILE_WIDTH, TIFF_TAG_TILE_LENGTH, TIFF_TAG_TILE_OFFSETS, TIFF_TAG_TILE_BYTE_COUNTS, TIFF_TAG_INK_SET, TIFF_TAG_INK_NAMES, TIFF_TAG_NUMBER_OF_INKS, TIFF_TAG_DOT_RANGE, TIFF_TAG_TARGET_PRINTER, TIFF_TAG_EXTRA_SAMPLES, TIFF_TAG_SAMPLE_FORMAT, TIFF_TAG_SMIN_SAMPLE_VALUE, TIFF_TAG_SMAX_SAMPLE_VALUE, TIFF_TAG_TRANSFER_RANGE, TIFF_TAG_JPEG_PROC, TIFF_TAG_JPEG_INTERCHANGE_FORMAT, TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, TIFF_TAG_JPEG_RESTART_INTERVAL, TIFF_TAG_JPEG_LOSSLESS_PREDICTORS, TIFF_TAG_JPEG_POINT_TRANSFORMS, TIFF_TAG_JPEG_QTABLES, TIFF_TAG_JPEG_DCTABLES, TIFF_TAG_JPEG_ACTABLES, TIFF_TAG_YCBCR_COEFFICIENTS, TIFF_TAG_YCBCR_SUB_SAMPLING, TIFF_TAG_YCBCR_POSITIONING, TIFF_TAG_REFERENCE_BLACK_WHITE, TIFF_TAG_COPYRIGHT, TIFF_TAG_XMP};
    public static final TagInfo TIFF_TAG_ARTIST;
    public static final TagInfo TIFF_TAG_BITS_PER_SAMPLE;
    public static final TagInfo TIFF_TAG_CELL_LENGTH;
    public static final TagInfo TIFF_TAG_CELL_WIDTH;
    public static final TagInfo TIFF_TAG_COLOR_MAP;
    public static final TagInfo TIFF_TAG_COMPRESSION;
    public static final TagInfo TIFF_TAG_COPYRIGHT;
    public static final TagInfo TIFF_TAG_DATE_TIME;
    public static final TagInfo TIFF_TAG_DOCUMENT_NAME;
    public static final TagInfo TIFF_TAG_DOT_RANGE;
    public static final TagInfo TIFF_TAG_EXTRA_SAMPLES;
    public static final TagInfo TIFF_TAG_FILL_ORDER;
    public static final TagInfo TIFF_TAG_FREE_BYTE_COUNTS;
    public static final TagInfo TIFF_TAG_FREE_OFFSETS;
    public static final TagInfo TIFF_TAG_GRAY_RESPONSE_CURVE;
    public static final TagInfo TIFF_TAG_GRAY_RESPONSE_UNIT;
    public static final TagInfo TIFF_TAG_HALFTONE_HINTS;
    public static final TagInfo TIFF_TAG_HOST_COMPUTER;
    public static final TagInfo TIFF_TAG_IMAGE_DESCRIPTION;
    public static final TagInfo TIFF_TAG_IMAGE_LENGTH;
    public static final TagInfo TIFF_TAG_IMAGE_WIDTH;
    public static final TagInfo TIFF_TAG_INK_NAMES;
    public static final TagInfo TIFF_TAG_INK_SET;
    public static final TagInfo TIFF_TAG_JPEG_ACTABLES;
    public static final TagInfo TIFF_TAG_JPEG_DCTABLES;
    public static final TagInfo TIFF_TAG_JPEG_INTERCHANGE_FORMAT;
    public static final TagInfo TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH;
    public static final TagInfo TIFF_TAG_JPEG_LOSSLESS_PREDICTORS;
    public static final TagInfo TIFF_TAG_JPEG_POINT_TRANSFORMS;
    public static final TagInfo TIFF_TAG_JPEG_PROC;
    public static final TagInfo TIFF_TAG_JPEG_QTABLES;
    public static final TagInfo TIFF_TAG_JPEG_RESTART_INTERVAL;
    public static final TagInfo TIFF_TAG_MAKE;
    public static final TagInfo TIFF_TAG_MAX_SAMPLE_VALUE;
    public static final TagInfo TIFF_TAG_MIN_SAMPLE_VALUE;
    public static final TagInfo TIFF_TAG_MODEL;
    public static final TagInfo TIFF_TAG_NEW_SUBFILE_TYPE;
    public static final TagInfo TIFF_TAG_NUMBER_OF_INKS;
    public static final TagInfo TIFF_TAG_ORIENTATION;
    public static final TagInfo TIFF_TAG_PAGE_NAME;
    public static final TagInfo TIFF_TAG_PAGE_NUMBER;
    public static final TagInfo TIFF_TAG_PHOTOMETRIC_INTERPRETATION;
    public static final TagInfo TIFF_TAG_PLANAR_CONFIGURATION;
    public static final TagInfo TIFF_TAG_PREDICTOR;
    public static final TagInfo TIFF_TAG_PRIMARY_CHROMATICITIES;
    public static final TagInfo TIFF_TAG_REFERENCE_BLACK_WHITE;
    public static final TagInfo TIFF_TAG_RESOLUTION_UNIT;
    public static final TagInfo TIFF_TAG_ROWS_PER_STRIP;
    public static final TagInfo TIFF_TAG_SAMPLES_PER_PIXEL;
    public static final TagInfo TIFF_TAG_SAMPLE_FORMAT;
    public static final TagInfo TIFF_TAG_SMAX_SAMPLE_VALUE;
    public static final TagInfo TIFF_TAG_SMIN_SAMPLE_VALUE;
    public static final TagInfo TIFF_TAG_SOFTWARE;
    public static final TagInfo TIFF_TAG_STRIP_BYTE_COUNTS;
    public static final TagInfo TIFF_TAG_STRIP_OFFSETS;
    public static final TagInfo TIFF_TAG_SUBFILE_TYPE;
    public static final TagInfo TIFF_TAG_T4_OPTIONS;
    public static final TagInfo TIFF_TAG_T6_OPTIONS;
    public static final TagInfo TIFF_TAG_TARGET_PRINTER;
    public static final TagInfo TIFF_TAG_THRESHHOLDING;
    public static final TagInfo TIFF_TAG_TILE_BYTE_COUNTS;
    public static final TagInfo TIFF_TAG_TILE_LENGTH;
    public static final TagInfo TIFF_TAG_TILE_OFFSETS;
    public static final TagInfo TIFF_TAG_TILE_WIDTH;
    public static final TagInfo TIFF_TAG_TRANSFER_FUNCTION;
    public static final TagInfo TIFF_TAG_TRANSFER_RANGE;
    public static final TagInfo TIFF_TAG_UNKNOWN;
    public static final TagInfo TIFF_TAG_WHITE_POINT;
    public static final TagInfo TIFF_TAG_XMP;
    public static final TagInfo TIFF_TAG_XPOSITION;
    public static final TagInfo TIFF_TAG_XRESOLUTION;
    public static final TagInfo TIFF_TAG_YCBCR_COEFFICIENTS;
    public static final TagInfo TIFF_TAG_YCBCR_POSITIONING;
    public static final TagInfo TIFF_TAG_YCBCR_SUB_SAMPLING;
    public static final TagInfo TIFF_TAG_YPOSITION;
    public static final TagInfo TIFF_TAG_YRESOLUTION;

    static {
        TagInfo tagInfo = new TagInfo("New Subfile Type", 254, FIELD_TYPE_DESCRIPTION_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_NEW_SUBFILE_TYPE = tagInfo;
        TagInfo tagInfo2 = new TagInfo("Subfile Type", 255, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_SUBFILE_TYPE = tagInfo2;
        TagInfo tagInfo3 = new TagInfo("Image Width", 256, FIELD_TYPE_DESCRIPTION_SHORT_OR_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_IMAGE_WIDTH = tagInfo3;
        TagInfo tagInfo4 = new TagInfo("Image Length", (int) InputDeviceCompat.SOURCE_KEYBOARD, FIELD_TYPE_DESCRIPTION_SHORT_OR_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_IMAGE_LENGTH = tagInfo4;
        TagInfo tagInfo5 = new TagInfo("Bits Per Sample", 258, FIELD_TYPE_DESCRIPTION_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_BITS_PER_SAMPLE = tagInfo5;
        TagInfo tagInfo6 = new TagInfo("Compression", 259, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_COMPRESSION = tagInfo6;
        TagInfo tagInfo7 = new TagInfo("Photometric Interpretation", 262, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_PHOTOMETRIC_INTERPRETATION = tagInfo7;
        TagInfo tagInfo8 = new TagInfo("Threshholding", 263, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_THRESHHOLDING = tagInfo8;
        TagInfo tagInfo9 = new TagInfo("Cell Width", 264, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_CELL_WIDTH = tagInfo9;
        TagInfo tagInfo10 = new TagInfo("Cell Length", 265, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_CELL_LENGTH = tagInfo10;
        TagInfo tagInfo11 = new TagInfo("Fill Order", 266, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_FILL_ORDER = tagInfo11;
        TagInfo tagInfo12 = new TagInfo("Document Name", 269, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_DOCUMENT_NAME = tagInfo12;
        TagInfo tagInfo13 = new TagInfo("Image Description", (int) RotationOptions.ROTATE_270, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_IMAGE_DESCRIPTION = tagInfo13;
        TagInfo tagInfo14 = new TagInfo("Make", 271, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_MAKE = tagInfo14;
        TagInfo tagInfo15 = new TagInfo("Model", 272, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_MODEL = tagInfo15;
        Offset offset = new Offset("Strip Offsets", 273, FIELD_TYPE_DESCRIPTION_SHORT_OR_LONG, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_STRIP_OFFSETS = offset;
        TagInfo tagInfo16 = new TagInfo("Orientation", (int) TiffUtil.TIFF_TAG_ORIENTATION, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_ORIENTATION = tagInfo16;
        TagInfo tagInfo17 = new TagInfo("Samples Per Pixel", 277, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_SAMPLES_PER_PIXEL = tagInfo17;
        TagInfo tagInfo18 = new TagInfo("Rows Per Strip", 278, FIELD_TYPE_DESCRIPTION_SHORT_OR_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_ROWS_PER_STRIP = tagInfo18;
        TagInfo tagInfo19 = new TagInfo("Strip Byte Counts", 279, FIELD_TYPE_DESCRIPTION_LONG_OR_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_STRIP_BYTE_COUNTS = tagInfo19;
        TagInfo tagInfo20 = new TagInfo("Min Sample Value", 280, FIELD_TYPE_DESCRIPTION_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_MIN_SAMPLE_VALUE = tagInfo20;
        TagInfo tagInfo21 = new TagInfo("Max Sample Value", 281, FIELD_TYPE_DESCRIPTION_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_MAX_SAMPLE_VALUE = tagInfo21;
        TagInfo tagInfo22 = new TagInfo("XResolution", 282, FIELD_TYPE_DESCRIPTION_RATIONAL, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_XRESOLUTION = tagInfo22;
        TagInfo tagInfo23 = new TagInfo("YResolution", 283, FIELD_TYPE_DESCRIPTION_RATIONAL, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_YRESOLUTION = tagInfo23;
        TagInfo tagInfo24 = new TagInfo("Planar Configuration", 284, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_PLANAR_CONFIGURATION = tagInfo24;
        TagInfo tagInfo25 = new TagInfo("Page Name", 285, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_PAGE_NAME = tagInfo25;
        TagInfo tagInfo26 = new TagInfo("XPosition", 286, FIELD_TYPE_DESCRIPTION_RATIONAL, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_XPOSITION = tagInfo26;
        TagInfo tagInfo27 = new TagInfo("YPosition", 287, FIELD_TYPE_DESCRIPTION_RATIONAL, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_YPOSITION = tagInfo27;
        TagInfo tagInfo28 = new TagInfo("Free Offsets", 288, FIELD_TYPE_DESCRIPTION_LONG, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_FREE_OFFSETS = tagInfo28;
        TagInfo tagInfo29 = new TagInfo("Free Byte Counts", 289, FIELD_TYPE_DESCRIPTION_LONG, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_FREE_BYTE_COUNTS = tagInfo29;
        TagInfo tagInfo30 = new TagInfo("Gray Response Unit", 290, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_GRAY_RESPONSE_UNIT = tagInfo30;
        TagInfo tagInfo31 = new TagInfo("Gray Response Curve", 291, FIELD_TYPE_DESCRIPTION_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_GRAY_RESPONSE_CURVE = tagInfo31;
        TagInfo tagInfo32 = new TagInfo("T4 Options", 292, FIELD_TYPE_DESCRIPTION_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_T4_OPTIONS = tagInfo32;
        TagInfo tagInfo33 = new TagInfo("T6 Options", 293, FIELD_TYPE_DESCRIPTION_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_T6_OPTIONS = tagInfo33;
        TagInfo tagInfo34 = new TagInfo("Resolution Unit", 296, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_RESOLUTION_UNIT = tagInfo34;
        TagInfo tagInfo35 = new TagInfo("Page Number", 297, FIELD_TYPE_DESCRIPTION_SHORT, 2, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_PAGE_NUMBER = tagInfo35;
        TagInfo tagInfo36 = new TagInfo("Transfer Function", 301, FIELD_TYPE_DESCRIPTION_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_TRANSFER_FUNCTION = tagInfo36;
        TagInfo tagInfo37 = new TagInfo("Software", 305, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_SOFTWARE = tagInfo37;
        TagInfo tagInfo38 = new TagInfo("Date Time", 306, FIELD_TYPE_DESCRIPTION_ASCII, 20, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_DATE_TIME = tagInfo38;
        TagInfo tagInfo39 = new TagInfo("Artist", 315, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_ARTIST = tagInfo39;
        TagInfo tagInfo40 = new TagInfo("Host Computer", 316, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_HOST_COMPUTER = tagInfo40;
        TagInfo tagInfo41 = new TagInfo("Predictor", 317, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_PREDICTOR = tagInfo41;
        TagInfo tagInfo42 = new TagInfo("White Point", 318, FIELD_TYPE_DESCRIPTION_RATIONAL, 2, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_WHITE_POINT = tagInfo42;
        TagInfo tagInfo43 = new TagInfo("Primary Chromaticities", 319, FIELD_TYPE_DESCRIPTION_RATIONAL, 6, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_PRIMARY_CHROMATICITIES = tagInfo43;
        TagInfo tagInfo44 = new TagInfo("Color Map", 320, FIELD_TYPE_DESCRIPTION_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_COLOR_MAP = tagInfo44;
        TagInfo tagInfo45 = new TagInfo("Halftone Hints", 321, FIELD_TYPE_DESCRIPTION_SHORT, 2, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_HALFTONE_HINTS = tagInfo45;
        TagInfo tagInfo46 = new TagInfo("Tile Width", 322, FIELD_TYPE_DESCRIPTION_SHORT_OR_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_TILE_WIDTH = tagInfo46;
        TagInfo tagInfo47 = new TagInfo("Tile Length", 323, FIELD_TYPE_DESCRIPTION_SHORT_OR_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_TILE_LENGTH = tagInfo47;
        Offset offset2 = new Offset("Tile Offsets", 324, FIELD_TYPE_DESCRIPTION_LONG, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_TILE_OFFSETS = offset2;
        TagInfo tagInfo48 = new TagInfo("Tile Byte Counts", 325, FIELD_TYPE_DESCRIPTION_SHORT_OR_LONG, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_TILE_BYTE_COUNTS = tagInfo48;
        TagInfo tagInfo49 = new TagInfo("Ink Set", 332, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_INK_SET = tagInfo49;
        TagInfo tagInfo50 = new TagInfo("Ink Names", 333, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_INK_NAMES = tagInfo50;
        TagInfo tagInfo51 = new TagInfo("Number Of Inks", 334, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_NUMBER_OF_INKS = tagInfo51;
        TagInfo tagInfo52 = new TagInfo("Dot Range", 336, FIELD_TYPE_DESCRIPTION_BYTE_OR_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_DOT_RANGE = tagInfo52;
        TagInfo tagInfo53 = new TagInfo("Target Printer", 337, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_TARGET_PRINTER = tagInfo53;
        TagInfo tagInfo54 = new TagInfo("Extra Samples", 338, FIELD_TYPE_DESCRIPTION_BYTE, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_EXTRA_SAMPLES = tagInfo54;
        TagInfo tagInfo55 = new TagInfo("Sample Format", 339, FIELD_TYPE_DESCRIPTION_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_SAMPLE_FORMAT = tagInfo55;
        TagInfo tagInfo56 = new TagInfo("SMin Sample Value", 340, FIELD_TYPE_DESCRIPTION_ANY, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_SMIN_SAMPLE_VALUE = tagInfo56;
        TagInfo tagInfo57 = new TagInfo("SMax Sample Value", 341, FIELD_TYPE_DESCRIPTION_ANY, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_SMAX_SAMPLE_VALUE = tagInfo57;
        TagInfo tagInfo58 = new TagInfo("Transfer Range", 342, FIELD_TYPE_DESCRIPTION_SHORT, 6, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_TRANSFER_RANGE = tagInfo58;
        TagInfo tagInfo59 = new TagInfo("JPEGProc", 512, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_JPEG_PROC = tagInfo59;
        Offset offset3 = new Offset("JPEGInterchange Format", (int) InputDeviceCompat.SOURCE_DPAD, FIELD_TYPE_DESCRIPTION_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_JPEG_INTERCHANGE_FORMAT = offset3;
        TagInfo tagInfo60 = new TagInfo("JPEGInterchange Format Length", 514, FIELD_TYPE_DESCRIPTION_LONG, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = tagInfo60;
        TagInfo tagInfo61 = new TagInfo("JPEGRestart Interval", 515, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_JPEG_RESTART_INTERVAL = tagInfo61;
        TagInfo tagInfo62 = new TagInfo("JPEGLossless Predictors", 517, FIELD_TYPE_DESCRIPTION_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_JPEG_LOSSLESS_PREDICTORS = tagInfo62;
        TagInfo tagInfo63 = new TagInfo("JPEGPoint Transforms", 518, FIELD_TYPE_DESCRIPTION_SHORT, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_JPEG_POINT_TRANSFORMS = tagInfo63;
        TagInfo tagInfo64 = new TagInfo("JPEGQTables", 519, FIELD_TYPE_DESCRIPTION_LONG, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_JPEG_QTABLES = tagInfo64;
        TagInfo tagInfo65 = new TagInfo("JPEGDCTables", 520, FIELD_TYPE_DESCRIPTION_LONG, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_JPEG_DCTABLES = tagInfo65;
        TagInfo tagInfo66 = new TagInfo("JPEGACTables", 521, FIELD_TYPE_DESCRIPTION_LONG, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_JPEG_ACTABLES = tagInfo66;
        TagInfo tagInfo67 = new TagInfo("YCbCr Coefficients", 529, FIELD_TYPE_DESCRIPTION_RATIONAL, 3, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_YCBCR_COEFFICIENTS = tagInfo67;
        TagInfo tagInfo68 = new TagInfo("YCbCr Sub Sampling", 530, FIELD_TYPE_DESCRIPTION_SHORT, 2, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_YCBCR_SUB_SAMPLING = tagInfo68;
        TagInfo tagInfo69 = new TagInfo("YCbCr Positioning", 531, FIELD_TYPE_DESCRIPTION_SHORT, 1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_YCBCR_POSITIONING = tagInfo69;
        TagInfo tagInfo70 = new TagInfo("Reference Black White", 532, FIELD_TYPE_DESCRIPTION_LONG, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_REFERENCE_BLACK_WHITE = tagInfo70;
        TagInfo tagInfo71 = new TagInfo("Copyright", 33432, FIELD_TYPE_DESCRIPTION_ASCII, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_COPYRIGHT = tagInfo71;
        TagInfo tagInfo72 = new TagInfo(PsdImageParser.BLOCK_NAME_XMP, 700, FIELD_TYPE_DESCRIPTION_BYTE, -1, TIFF_DIRECTORY_ROOT);
        TIFF_TAG_XMP = tagInfo72;
        Unknown unknown = new Unknown("Unknown Tag", -1, FIELD_TYPE_DESCRIPTION_UNKNOWN, -1, EXIF_DIRECTORY_UNKNOWN);
        TIFF_TAG_UNKNOWN = unknown;
    }
}
