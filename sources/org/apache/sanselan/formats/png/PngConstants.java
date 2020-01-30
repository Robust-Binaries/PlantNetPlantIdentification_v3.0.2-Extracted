package org.apache.sanselan.formats.png;

import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.formats.pnm.PNMConstants;

public interface PngConstants extends SanselanConstants {
    public static final int COLOR_TYPE_GREYSCALE = 0;
    public static final int COLOR_TYPE_GREYSCALE_WITH_ALPHA = 4;
    public static final int COLOR_TYPE_INDEXED_COLOR = 3;
    public static final int COLOR_TYPE_TRUE_COLOR = 2;
    public static final int COLOR_TYPE_TRUE_COLOR_WITH_ALPHA = 6;
    public static final int COMPRESSION_DEFLATE_INFLATE = 0;
    public static final byte COMPRESSION_TYPE_INFLATE_DEFLATE = 0;
    public static final byte FILTER_METHOD_ADAPTIVE = 0;
    public static final byte FILTER_TYPE_AVERAGE = 3;
    public static final byte FILTER_TYPE_NONE = 0;
    public static final byte FILTER_TYPE_PAETH = 4;
    public static final byte FILTER_TYPE_SUB = 1;
    public static final byte FILTER_TYPE_UP = 2;
    public static final int IDAT = PngImageParser.CharsToQuad('I', 'D', 'A', 'T');
    public static final byte[] IDAT_CHUNK_TYPE = {73, 68, 65, 84};
    public static final int IEND = PngImageParser.CharsToQuad('I', 'E', 'N', 'D');
    public static final byte[] IEND_CHUNK_TYPE = {73, 69, 78, 68};
    public static final int IHDR = PngImageParser.CharsToQuad('I', 'H', 'D', 'R');
    public static final byte[] IHDR_CHUNK_TYPE = {73, 72, 68, 82};
    public static final byte INTERLACE_METHOD_ADAM7 = 1;
    public static final byte INTERLACE_METHOD_NONE = 0;
    public static final String PARAM_KEY_PNG_BIT_DEPTH = "PNG_BIT_DEPTH";
    public static final String PARAM_KEY_PNG_FORCE_INDEXED_COLOR = "PNG_FORCE_INDEXED_COLOR";
    public static final String PARAM_KEY_PNG_FORCE_TRUE_COLOR = "PNG_FORCE_TRUE_COLOR";
    public static final String PARAM_KEY_PNG_TEXT_CHUNKS = "PNG_TEXT_CHUNKS";
    public static final int PLTE = PngImageParser.CharsToQuad('P', 'L', 'T', 'E');
    public static final byte[] PLTE_CHUNK_TYPE = {PNMConstants.PNM_PREFIX_BYTE, 76, 84, 69};
    public static final byte[] PNG_Signature = {-119, PNMConstants.PNM_PREFIX_BYTE, 78, 71, 13, 10, 26, 10};
    public static final String XMP_KEYWORD = "XML:com.adobe.xmp";
    public static final int gAMA = PngImageParser.CharsToQuad('g', 'A', 'M', 'A');
    public static final int iCCP = PngImageParser.CharsToQuad('i', 'C', 'C', 'P');
    public static final int iTXt = PngImageParser.CharsToQuad('i', 'T', 'X', 't');
    public static final byte[] iTXt_CHUNK_TYPE = {105, 84, 88, 116};
    public static final int pHYs = PngImageParser.CharsToQuad('p', 'H', 'Y', 's');
    public static final int sRGB = PngImageParser.CharsToQuad('s', 'R', 'G', 'B');
    public static final int tEXt = PngImageParser.CharsToQuad('t', 'E', 'X', 't');
    public static final byte[] tEXt_CHUNK_TYPE = {116, 69, 88, 116};
    public static final int tRNS = PngImageParser.CharsToQuad('t', 'R', 'N', 'S');
    public static final int zTXt = PngImageParser.CharsToQuad('z', 'T', 'X', 't');
    public static final byte[] zTXt_CHUNK_TYPE = {122, 84, 88, 116};
}
