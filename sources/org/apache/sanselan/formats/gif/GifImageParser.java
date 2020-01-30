package org.apache.sanselan.formats.gif;

import android.support.p000v4.view.ViewCompat;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import kotlin.UByte;
import kotlin.jvm.internal.ByteCompanionObject;
import org.apache.sanselan.FormatCompliance;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.ImageParser;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.SanselanConstants;
import org.apache.sanselan.common.BinaryOutputStream;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.mylzw.MyLZWCompressor;
import org.apache.sanselan.common.mylzw.MyLZWDecompressor;
import org.apache.sanselan.formats.pnm.PNMConstants;
import org.apache.sanselan.palette.Palette;
import org.apache.sanselan.palette.PaletteFactory;
import org.apache.sanselan.util.Debug;
import org.apache.sanselan.util.ParamMap;

public class GifImageParser extends ImageParser {
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};
    private static final int APPLICATION_EXTENSION_LABEL = 255;
    private static final int COMMENT_EXTENSION = 254;
    private static final String DEFAULT_EXTENSION = ".gif";
    private static final int EXTENSION_CODE = 33;
    private static final byte[] GIF_HEADER_SIGNATURE = {71, 73, 70};
    private static final int GRAPHIC_CONTROL_EXTENSION = 8697;
    private static final int IMAGE_SEPARATOR = 44;
    private static final int INTERLACE_FLAG_MASK = 64;
    private static final int LOCAL_COLOR_TABLE_FLAG_MASK = 128;
    private static final int PLAIN_TEXT_EXTENSION = 1;
    private static final int SORT_FLAG_MASK = 32;
    private static final int TERMINATOR_BYTE = 59;
    private static final byte[] XMP_APPLICATION_ID_AND_AUTH_CODE = {88, 77, PNMConstants.PNM_PREFIX_BYTE, PNMConstants.PNM_SEPARATOR, 68, 97, 116, 97, 88, 77, PNMConstants.PNM_PREFIX_BYTE};
    private static final int XMP_COMPLETE_CODE = 8703;
    private static final int XMP_EXTENSION = 255;

    private int simple_pow(int i, int i2) {
        int i3 = 1;
        for (int i4 = 0; i4 < i2; i4++) {
            i3 *= i;
        }
        return i3;
    }

    public boolean embedICCProfile(File file, File file2, byte[] bArr) {
        return false;
    }

    public byte[] embedICCProfile(byte[] bArr, byte[] bArr2) {
        return null;
    }

    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    public byte[] getICCProfileBytes(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public IImageMetadata getMetadata(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        return null;
    }

    public String getName() {
        return "Gif-Custom";
    }

    public GifImageParser() {
        super.setByteOrder(73);
    }

    /* access modifiers changed from: protected */
    public String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    /* access modifiers changed from: protected */
    public ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormat.IMAGE_FORMAT_GIF};
    }

    private GIFHeaderInfo readHeader(InputStream inputStream, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        byte b;
        int i;
        byte b2;
        boolean z;
        InputStream inputStream2 = inputStream;
        FormatCompliance formatCompliance2 = formatCompliance;
        byte readByte = readByte("identifier1", inputStream2, "Not a Valid GIF File");
        byte readByte2 = readByte("identifier2", inputStream2, "Not a Valid GIF File");
        byte readByte3 = readByte("identifier3", inputStream2, "Not a Valid GIF File");
        byte readByte4 = readByte("version1", inputStream2, "Not a Valid GIF File");
        byte readByte5 = readByte("version2", inputStream2, "Not a Valid GIF File");
        byte readByte6 = readByte("version3", inputStream2, "Not a Valid GIF File");
        if (formatCompliance2 != null) {
            formatCompliance2.compare_bytes("Signature", GIF_HEADER_SIGNATURE, new byte[]{readByte, readByte2, readByte3});
            formatCompliance2.compare("version", 56, (int) readByte4);
            formatCompliance2.compare("version", new int[]{55, 57}, (int) readByte5);
            formatCompliance2.compare("version", 97, (int) readByte6);
        }
        if (this.debug) {
            printCharQuad("identifier: ", (readByte << 16) | (readByte2 << 8) | (readByte3 << 0));
        }
        if (this.debug) {
            printCharQuad("version: ", (readByte4 << 16) | (readByte5 << 8) | (readByte6 << 0));
        }
        int read2Bytes = read2Bytes("Logical Screen Width", inputStream2, "Not a Valid GIF File");
        int read2Bytes2 = read2Bytes("Logical Screen Height", inputStream2, "Not a Valid GIF File");
        if (formatCompliance2 != null) {
            formatCompliance2.checkBounds("Width", 1, Integer.MAX_VALUE, read2Bytes);
            formatCompliance2.checkBounds("Height", 1, Integer.MAX_VALUE, read2Bytes2);
        }
        byte readByte7 = readByte("Packed Fields", inputStream2, "Not a Valid GIF File");
        byte readByte8 = readByte("Background Color Index", inputStream2, "Not a Valid GIF File");
        byte readByte9 = readByte("Pixel Aspect Ratio", inputStream2, "Not a Valid GIF File");
        if (this.debug) {
            printByteBits("PackedFields bits", readByte7);
        }
        boolean z2 = (readByte7 & ByteCompanionObject.MIN_VALUE) > 0;
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            b = readByte9;
            stringBuffer.append("GlobalColorTableFlag: ");
            stringBuffer.append(z2);
            printStream.println(stringBuffer.toString());
        } else {
            b = readByte9;
        }
        byte b3 = (byte) ((readByte7 >> 4) & 7);
        if (this.debug) {
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            i = read2Bytes2;
            stringBuffer2.append("ColorResolution: ");
            stringBuffer2.append(b3);
            printStream2.println(stringBuffer2.toString());
        } else {
            i = read2Bytes2;
        }
        boolean z3 = (readByte7 & 8) > 0;
        if (this.debug) {
            PrintStream printStream3 = System.out;
            StringBuffer stringBuffer3 = new StringBuffer();
            b2 = b3;
            stringBuffer3.append("SortFlag: ");
            stringBuffer3.append(z3);
            printStream3.println(stringBuffer3.toString());
        } else {
            b2 = b3;
        }
        byte b4 = (byte) (readByte7 & 7);
        if (this.debug) {
            PrintStream printStream4 = System.out;
            StringBuffer stringBuffer4 = new StringBuffer();
            z = z3;
            stringBuffer4.append("SizeofGlobalColorTable: ");
            stringBuffer4.append(b4);
            printStream4.println(stringBuffer4.toString());
        } else {
            z = z3;
        }
        if (!(formatCompliance2 == null || !z2 || readByte8 == -1)) {
            formatCompliance2.checkBounds("Background Color Index", 0, convertColorTableSize(b4), readByte8);
        }
        GIFHeaderInfo gIFHeaderInfo = new GIFHeaderInfo(readByte, readByte2, readByte3, readByte4, readByte5, readByte6, read2Bytes, i, readByte7, readByte8, b, z2, b2, z, b4);
        return gIFHeaderInfo;
    }

    private GraphicControlExtension readGraphicControlExtension(int i, InputStream inputStream) throws ImageReadException, IOException {
        readByte("block_size", inputStream, "GIF: corrupt GraphicControlExt");
        byte readByte = readByte("packed fields", inputStream, "GIF: corrupt GraphicControlExt");
        int i2 = (readByte & 28) >> 2;
        boolean z = (readByte & 1) != 0;
        int read2Bytes = read2Bytes("delay in milliseconds", inputStream, "GIF: corrupt GraphicControlExt");
        byte readByte2 = readByte("transparent color index", inputStream, "GIF: corrupt GraphicControlExt") & UByte.MAX_VALUE;
        readByte("block terminator", inputStream, "GIF: corrupt GraphicControlExt");
        GraphicControlExtension graphicControlExtension = new GraphicControlExtension(i, readByte, i2, z, read2Bytes, readByte2);
        return graphicControlExtension;
    }

    private byte[] readSubBlock(InputStream inputStream) throws ImageReadException, IOException {
        return readByteArray("block", readByte("block_size", inputStream, "GIF: corrupt block") & UByte.MAX_VALUE, inputStream, "GIF: corrupt block");
    }

    /* access modifiers changed from: protected */
    public GenericGIFBlock readGenericGIFBlock(InputStream inputStream, int i) throws ImageReadException, IOException {
        return readGenericGIFBlock(inputStream, i, null);
    }

    /* access modifiers changed from: protected */
    public GenericGIFBlock readGenericGIFBlock(InputStream inputStream, int i, byte[] bArr) throws ImageReadException, IOException {
        ArrayList arrayList = new ArrayList();
        if (bArr != null) {
            arrayList.add(bArr);
        }
        while (true) {
            byte[] readSubBlock = readSubBlock(inputStream);
            if (readSubBlock.length < 1) {
                return new GenericGIFBlock(i, arrayList);
            }
            arrayList.add(readSubBlock);
        }
    }

    private ArrayList readBlocks(GIFHeaderInfo gIFHeaderInfo, InputStream inputStream, boolean z, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        ArrayList arrayList = new ArrayList();
        while (true) {
            int read = inputStream.read();
            if (read == 33) {
                int read2 = inputStream.read();
                int i = ((read & 255) << 8) | (read2 & 255);
                if (read2 != 1) {
                    if (read2 != 249) {
                        switch (read2) {
                            case COMMENT_EXTENSION /*254*/:
                                break;
                            case 255:
                                byte[] readSubBlock = readSubBlock(inputStream);
                                if (formatCompliance != null) {
                                    StringBuffer stringBuffer = new StringBuffer();
                                    stringBuffer.append("Unknown Application Extension (");
                                    stringBuffer.append(new String(readSubBlock));
                                    stringBuffer.append(")");
                                    formatCompliance.addComment(stringBuffer.toString(), i);
                                }
                                if (readSubBlock != null && readSubBlock.length > 0) {
                                    GenericGIFBlock readGenericGIFBlock = readGenericGIFBlock(inputStream, i, readSubBlock);
                                    readGenericGIFBlock.appendSubBlocks();
                                    arrayList.add(readGenericGIFBlock);
                                    break;
                                }
                            default:
                                if (formatCompliance != null) {
                                    formatCompliance.addComment("Unknown block", i);
                                }
                                arrayList.add(readGenericGIFBlock(inputStream, i));
                                continue;
                        }
                    } else {
                        arrayList.add(readGraphicControlExtension(i, inputStream));
                    }
                }
                arrayList.add(readGenericGIFBlock(inputStream, i));
            } else if (read == 44) {
                arrayList.add(readImageDescriptor(gIFHeaderInfo, read, inputStream, z, formatCompliance));
            } else if (read == 59) {
                return arrayList;
            } else {
                switch (read) {
                    case -1:
                        throw new ImageReadException("GIF: unexpected end of data");
                    case 0:
                        break;
                    default:
                        StringBuffer stringBuffer2 = new StringBuffer();
                        stringBuffer2.append("GIF: unknown code: ");
                        stringBuffer2.append(read);
                        throw new ImageReadException(stringBuffer2.toString());
                }
            }
        }
    }

    private ImageDescriptor readImageDescriptor(GIFHeaderInfo gIFHeaderInfo, int i, InputStream inputStream, boolean z, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        byte[] bArr;
        GIFHeaderInfo gIFHeaderInfo2 = gIFHeaderInfo;
        InputStream inputStream2 = inputStream;
        FormatCompliance formatCompliance2 = formatCompliance;
        int read2Bytes = read2Bytes("Image Left Position", inputStream2, "Not a Valid GIF File");
        int read2Bytes2 = read2Bytes("Image Top Position", inputStream2, "Not a Valid GIF File");
        int read2Bytes3 = read2Bytes("Image Width", inputStream2, "Not a Valid GIF File");
        int read2Bytes4 = read2Bytes("Image Height", inputStream2, "Not a Valid GIF File");
        byte readByte = readByte("Packed Fields", inputStream2, "Not a Valid GIF File");
        if (formatCompliance2 != null) {
            formatCompliance2.checkBounds("Width", 1, gIFHeaderInfo2.logicalScreenWidth, read2Bytes3);
            formatCompliance2.checkBounds("Height", 1, gIFHeaderInfo2.logicalScreenHeight, read2Bytes4);
            formatCompliance2.checkBounds("Left Position", 0, gIFHeaderInfo2.logicalScreenWidth - read2Bytes3, read2Bytes);
            formatCompliance2.checkBounds("Top Position", 0, gIFHeaderInfo2.logicalScreenHeight - read2Bytes4, read2Bytes2);
        }
        if (this.debug) {
            printByteBits("PackedFields bits", readByte);
        }
        boolean z2 = ((readByte >> 7) & 1) > 0;
        if (this.debug) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("LocalColorTableFlag: ");
            stringBuffer.append(z2);
            printStream.println(stringBuffer.toString());
        }
        boolean z3 = ((readByte >> 6) & 1) > 0;
        if (this.debug) {
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("Interlace Flag: ");
            stringBuffer2.append(z3);
            printStream2.println(stringBuffer2.toString());
        }
        boolean z4 = ((readByte >> 5) & 1) > 0;
        if (this.debug) {
            PrintStream printStream3 = System.out;
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("Sort  Flag: ");
            stringBuffer3.append(z4);
            printStream3.println(stringBuffer3.toString());
        }
        byte b = (byte) (readByte & 7);
        if (this.debug) {
            PrintStream printStream4 = System.out;
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append("SizeofLocalColorTable: ");
            stringBuffer4.append(b);
            printStream4.println(stringBuffer4.toString());
        }
        byte[] readColorTable = z2 ? readColorTable(inputStream2, b, formatCompliance2) : null;
        if (!z) {
            bArr = new MyLZWDecompressor(inputStream.read(), 73).decompress(new ByteArrayInputStream(readGenericGIFBlock(inputStream2, -1).appendSubBlocks()), read2Bytes3 * read2Bytes4);
        } else {
            int read = inputStream.read();
            if (this.debug) {
                PrintStream printStream5 = System.out;
                StringBuffer stringBuffer5 = new StringBuffer();
                stringBuffer5.append("LZWMinimumCodeSize: ");
                stringBuffer5.append(read);
                printStream5.println(stringBuffer5.toString());
            }
            readGenericGIFBlock(inputStream2, -1);
            bArr = null;
        }
        ImageDescriptor imageDescriptor = new ImageDescriptor(i, read2Bytes, read2Bytes2, read2Bytes3, read2Bytes4, readByte, z2, z3, z4, b, readColorTable, bArr);
        return imageDescriptor;
    }

    private int convertColorTableSize(int i) {
        return simple_pow(2, i + 1) * 3;
    }

    private byte[] readColorTable(InputStream inputStream, int i, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        return readByteArray("block", convertColorTableSize(i), inputStream, "GIF: corrupt Color Table");
    }

    private GIFHeaderInfo readHeader(ByteSource byteSource) throws ImageReadException, IOException {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
            try {
                GIFHeaderInfo readHeader = readHeader(inputStream, FormatCompliance.getDefault());
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return readHeader;
            } catch (Throwable th) {
                th = th;
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            inputStream.close();
            throw th;
        }
    }

    private GIFBlock findBlock(ArrayList arrayList, int i) {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            GIFBlock gIFBlock = (GIFBlock) arrayList.get(i2);
            if (gIFBlock.blockCode == i) {
                return gIFBlock;
            }
        }
        return null;
    }

    private ImageContents readFile(ByteSource byteSource, boolean z) throws ImageReadException, IOException {
        return readFile(byteSource, z, FormatCompliance.getDefault());
    }

    private ImageContents readFile(ByteSource byteSource, boolean z, FormatCompliance formatCompliance) throws ImageReadException, IOException {
        InputStream inputStream;
        byte[] bArr = null;
        try {
            inputStream = byteSource.getInputStream();
            try {
                GIFHeaderInfo readHeader = readHeader(inputStream, formatCompliance);
                if (readHeader.globalColorTableFlag) {
                    bArr = readColorTable(inputStream, readHeader.sizeOfGlobalColorTable, formatCompliance);
                }
                ImageContents imageContents = new ImageContents(readHeader, bArr, readBlocks(readHeader, inputStream, z, formatCompliance));
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Debug.debug((Throwable) e);
                }
                return imageContents;
            } catch (Throwable th) {
                th = th;
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    Debug.debug((Throwable) e2);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            inputStream = null;
            inputStream.close();
            throw th;
        }
    }

    public Dimension getImageSize(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        GIFHeaderInfo readHeader = readHeader(byteSource);
        if (readHeader != null) {
            return new Dimension(readHeader.logicalScreenWidth, readHeader.logicalScreenHeight);
        }
        throw new ImageReadException("GIF: Couldn't read Header");
    }

    private ArrayList getComments(ArrayList arrayList) throws ImageReadException, IOException {
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            GIFBlock gIFBlock = (GIFBlock) arrayList.get(i);
            if (gIFBlock.blockCode == 8702) {
                arrayList2.add(new String(((GenericGIFBlock) gIFBlock).appendSubBlocks()));
            }
        }
        return arrayList2;
    }

    public ImageInfo getImageInfo(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        ImageContents readFile = readFile(byteSource, false);
        if (readFile != null) {
            GIFHeaderInfo gIFHeaderInfo = readFile.gifHeaderInfo;
            if (gIFHeaderInfo != null) {
                ImageDescriptor imageDescriptor = (ImageDescriptor) findBlock(readFile.blocks, 44);
                if (imageDescriptor != null) {
                    GraphicControlExtension graphicControlExtension = (GraphicControlExtension) findBlock(readFile.blocks, GRAPHIC_CONTROL_EXTENSION);
                    int i = gIFHeaderInfo.logicalScreenHeight;
                    int i2 = gIFHeaderInfo.logicalScreenWidth;
                    ArrayList comments = getComments(readFile.blocks);
                    int i3 = (gIFHeaderInfo.colorResolution + 1) * 3;
                    ImageFormat imageFormat = ImageFormat.IMAGE_FORMAT_GIF;
                    String str = "GIF Graphics Interchange Format";
                    String str2 = "image/gif";
                    boolean z = imageDescriptor.interlaceFlag;
                    double d = (double) i2;
                    int i4 = i2;
                    double d2 = (double) 72;
                    Double.isNaN(d);
                    Double.isNaN(d2);
                    float f = (float) (d / d2);
                    double d3 = (double) i;
                    Double.isNaN(d3);
                    Double.isNaN(d2);
                    float f2 = (float) (d3 / d2);
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("Gif ");
                    stringBuffer.append((char) readFile.gifHeaderInfo.version1);
                    stringBuffer.append((char) readFile.gifHeaderInfo.version2);
                    stringBuffer.append((char) readFile.gifHeaderInfo.version3);
                    ImageInfo imageInfo = new ImageInfo(stringBuffer.toString(), i3, comments, imageFormat, str, i, str2, -1, 72, f2, 72, f, i4, z, graphicControlExtension != null && graphicControlExtension.transparency, true, 2, ImageInfo.COMPRESSION_ALGORITHM_LZW);
                    return imageInfo;
                }
                throw new ImageReadException("GIF: Couldn't read ImageDescriptor");
            }
            throw new ImageReadException("GIF: Couldn't read Header");
        }
        throw new ImageReadException("GIF: Couldn't read blocks");
    }

    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("gif.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        ImageContents readFile = readFile(byteSource, false);
        if (readFile == null) {
            return false;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("gif.blocks: ");
        stringBuffer.append(readFile.blocks.size());
        printWriter.println(stringBuffer.toString());
        for (int i = 0; i < readFile.blocks.size(); i++) {
            GIFBlock gIFBlock = (GIFBlock) readFile.blocks.get(i);
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("\t");
            stringBuffer2.append(i);
            stringBuffer2.append(" (");
            stringBuffer2.append(gIFBlock.getClass().getName());
            stringBuffer2.append(")");
            debugNumber(printWriter, stringBuffer2.toString(), gIFBlock.blockCode, 4);
        }
        printWriter.println("");
        return true;
    }

    private int[] getColorTable(byte[] bArr) throws ImageReadException, IOException {
        if (bArr.length % 3 == 0) {
            int length = bArr.length / 3;
            int[] iArr = new int[length];
            for (int i = 0; i < length; i++) {
                int i2 = i * 3;
                byte b = bArr[i2 + 0] & UByte.MAX_VALUE;
                byte b2 = bArr[i2 + 1] & UByte.MAX_VALUE;
                iArr[i] = ((bArr[i2 + 2] & UByte.MAX_VALUE) << 0) | (b << 16) | ViewCompat.MEASURED_STATE_MASK | (b2 << 8);
            }
            return iArr;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Bad Color Table Length: ");
        stringBuffer.append(bArr.length);
        throw new ImageReadException(stringBuffer.toString());
    }

    public FormatCompliance getFormatCompliance(ByteSource byteSource) throws ImageReadException, IOException {
        FormatCompliance formatCompliance = new FormatCompliance(byteSource.getDescription());
        readFile(byteSource, false, formatCompliance);
        return formatCompliance;
    }

    public BufferedImage getBufferedImage(ByteSource byteSource, Map map) throws ImageReadException, IOException {
        boolean z;
        Map map2;
        int[] iArr;
        int i;
        ImageContents readFile = readFile(byteSource, false);
        if (readFile != null) {
            GIFHeaderInfo gIFHeaderInfo = readFile.gifHeaderInfo;
            if (gIFHeaderInfo != null) {
                ImageDescriptor imageDescriptor = (ImageDescriptor) findBlock(readFile.blocks, 44);
                if (imageDescriptor != null) {
                    GraphicControlExtension graphicControlExtension = (GraphicControlExtension) findBlock(readFile.blocks, GRAPHIC_CONTROL_EXTENSION);
                    int i2 = gIFHeaderInfo.logicalScreenWidth;
                    int i3 = gIFHeaderInfo.logicalScreenHeight;
                    int i4 = 1;
                    if (graphicControlExtension == null || !graphicControlExtension.transparency) {
                        map2 = map;
                        z = false;
                    } else {
                        map2 = map;
                        z = true;
                    }
                    BufferedImage colorBufferedImage = getBufferedImageFactory(map2).getColorBufferedImage(i2, i3, z);
                    if (imageDescriptor.localColorTable != null) {
                        iArr = getColorTable(imageDescriptor.localColorTable);
                    } else if (readFile.globalColorTable != null) {
                        iArr = getColorTable(readFile.globalColorTable);
                    } else {
                        throw new ImageReadException("Gif: No Color Table");
                    }
                    int i5 = -1;
                    if (z) {
                        i5 = graphicControlExtension.transparentColorIndex;
                    }
                    int i6 = (i3 + 7) / 8;
                    int i7 = (i3 + 3) / 8;
                    int i8 = (i3 + 1) / 4;
                    int i9 = i3 / 2;
                    DataBuffer dataBuffer = colorBufferedImage.getRaster().getDataBuffer();
                    int i10 = 0;
                    int i11 = 0;
                    while (i10 < i3) {
                        if (!imageDescriptor.interlaceFlag) {
                            i = i10;
                        } else if (i10 < i6) {
                            i = i10 * 8;
                        } else {
                            int i12 = i10 - i6;
                            if (i12 < i7) {
                                i = (i12 * 8) + 4;
                            } else {
                                int i13 = i12 - i7;
                                if (i13 < i8) {
                                    i = (i13 * 4) + 2;
                                } else {
                                    int i14 = i13 - i8;
                                    if (i14 < i9) {
                                        i = (i14 * 2) + i4;
                                    } else {
                                        throw new ImageReadException("Gif: Strange Row");
                                    }
                                }
                            }
                        }
                        int i15 = i11;
                        int i16 = 0;
                        while (i16 < i2) {
                            int i17 = i15 + 1;
                            byte b = imageDescriptor.imageData[i15] & UByte.MAX_VALUE;
                            dataBuffer.setElem((i * i2) + i16, i5 == b ? 0 : iArr[b]);
                            i16++;
                            i15 = i17;
                        }
                        i10++;
                        i11 = i15;
                        i4 = 1;
                    }
                    return colorBufferedImage;
                }
                throw new ImageReadException("GIF: Couldn't read Image Descriptor");
            }
            throw new ImageReadException("GIF: Couldn't read Header");
        }
        throw new ImageReadException("GIF: Couldn't read blocks");
    }

    private void writeAsSubBlocks(OutputStream outputStream, byte[] bArr) throws IOException {
        int i = 0;
        while (i < bArr.length) {
            int min = Math.min(bArr.length - i, 255);
            outputStream.write(min);
            outputStream.write(bArr, i, min);
            i += min;
        }
        outputStream.write(0);
    }

    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map map) throws ImageWriteException, IOException {
        int i;
        BufferedImage bufferedImage2 = bufferedImage;
        OutputStream outputStream2 = outputStream;
        HashMap hashMap = new HashMap(map);
        boolean paramBoolean = ParamMap.getParamBoolean(hashMap, SanselanConstants.PARAM_KEY_VERBOSE, false);
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_FORMAT)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_FORMAT);
        }
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_VERBOSE)) {
            hashMap.remove(SanselanConstants.PARAM_KEY_VERBOSE);
        }
        String str = null;
        if (hashMap.containsKey(SanselanConstants.PARAM_KEY_XMP_XML)) {
            str = (String) hashMap.get(SanselanConstants.PARAM_KEY_XMP_XML);
            hashMap.remove(SanselanConstants.PARAM_KEY_XMP_XML);
        }
        if (hashMap.size() <= 0) {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            boolean hasTransparency = new PaletteFactory().hasTransparency(bufferedImage2);
            int i2 = hasTransparency ? 255 : 256;
            Palette makePaletteSimple = new PaletteFactory().makePaletteSimple(bufferedImage2, i2);
            if (makePaletteSimple == null) {
                makePaletteSimple = new PaletteFactory().makePaletteQuantized(bufferedImage2, i2);
                if (paramBoolean) {
                    System.out.println("quantizing");
                }
            } else if (paramBoolean) {
                System.out.println("exact palette");
            }
            if (makePaletteSimple != null) {
                int length = makePaletteSimple.length() + (hasTransparency ? 1 : 0);
                BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream2, 73);
                outputStream2.write(71);
                outputStream2.write(73);
                outputStream2.write(70);
                outputStream2.write(56);
                outputStream2.write(57);
                outputStream2.write(97);
                binaryOutputStream.write2Bytes(width);
                binaryOutputStream.write2Bytes(height);
                int i3 = length > 128 ? 7 : length > 64 ? 6 : length > 32 ? 5 : length > 16 ? 4 : length > 8 ? 3 : length > 4 ? 2 : length > 2 ? 1 : 0;
                int i4 = i3 + 1;
                int i5 = 1 << i4;
                simple_pow(2, i4);
                binaryOutputStream.write(((((byte) i3) & 7) << 4) | 0 | 0);
                binaryOutputStream.write(0);
                binaryOutputStream.write(0);
                binaryOutputStream.write(33);
                binaryOutputStream.write(-7);
                binaryOutputStream.write(4);
                binaryOutputStream.write(hasTransparency ? (byte) 1 : 0);
                binaryOutputStream.write(0);
                binaryOutputStream.write(0);
                binaryOutputStream.write((byte) (hasTransparency ? makePaletteSimple.length() : 0));
                binaryOutputStream.write(0);
                if (str != null) {
                    binaryOutputStream.write(33);
                    binaryOutputStream.write(255);
                    binaryOutputStream.write(XMP_APPLICATION_ID_AND_AUTH_CODE.length);
                    binaryOutputStream.write(XMP_APPLICATION_ID_AND_AUTH_CODE);
                    binaryOutputStream.write(str.getBytes("utf-8"));
                    int i6 = 0;
                    for (int i7 = 255; i6 <= i7; i7 = 255) {
                        binaryOutputStream.write(255 - i6);
                        i6++;
                    }
                    binaryOutputStream.write(0);
                }
                binaryOutputStream.write(44);
                binaryOutputStream.write2Bytes(0);
                binaryOutputStream.write2Bytes(0);
                binaryOutputStream.write2Bytes(width);
                binaryOutputStream.write2Bytes(height);
                binaryOutputStream.write((i3 & 7) | 128);
                for (int i8 = 0; i8 < i5; i8++) {
                    if (i8 < makePaletteSimple.length()) {
                        int entry = makePaletteSimple.getEntry(i8);
                        int i9 = (entry >> 8) & 255;
                        int i10 = (entry >> 0) & 255;
                        binaryOutputStream.write((entry >> 16) & 255);
                        binaryOutputStream.write(i9);
                        binaryOutputStream.write(i10);
                    } else {
                        binaryOutputStream.write(0);
                        binaryOutputStream.write(0);
                        binaryOutputStream.write(0);
                    }
                }
                if (i4 < 2) {
                    i4 = 2;
                }
                binaryOutputStream.write(i4);
                MyLZWCompressor myLZWCompressor = new MyLZWCompressor(i4, 73, false);
                byte[] bArr = new byte[(width * height)];
                for (int i11 = 0; i11 < height; i11++) {
                    for (int i12 = 0; i12 < width; i12++) {
                        int rgb = bufferedImage2.getRGB(i12, i11);
                        int i13 = 16777215 & rgb;
                        if (!hasTransparency) {
                            i = makePaletteSimple.getPaletteIndex(i13);
                        } else if (((rgb >> 24) & 255) < 255) {
                            i = makePaletteSimple.length();
                        } else {
                            i = makePaletteSimple.getPaletteIndex(i13);
                        }
                        bArr[(i11 * width) + i12] = (byte) i;
                    }
                }
                byte[] compress = myLZWCompressor.compress(bArr);
                writeAsSubBlocks(binaryOutputStream, compress);
                int length2 = compress.length;
                binaryOutputStream.write(59);
                binaryOutputStream.close();
                outputStream.close();
                return;
            }
            throw new ImageWriteException("Gif: can't write images with more than 256 colors");
        }
        Object next = hashMap.keySet().iterator().next();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Unknown parameter: ");
        stringBuffer.append(next);
        throw new ImageWriteException(stringBuffer.toString());
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:28|29|31|32|33) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0091 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getXmpXml(org.apache.sanselan.common.byteSources.ByteSource r17, java.util.Map r18) throws org.apache.sanselan.ImageReadException, java.io.IOException {
        /*
            r16 = this;
            r7 = r16
            r8 = 0
            java.io.InputStream r9 = r17.getInputStream()     // Catch:{ all -> 0x00d3 }
            org.apache.sanselan.formats.gif.GIFHeaderInfo r0 = r7.readHeader(r9, r8)     // Catch:{ all -> 0x00d0 }
            boolean r1 = r0.globalColorTableFlag     // Catch:{ all -> 0x00d0 }
            if (r1 == 0) goto L_0x0014
            byte r1 = r0.sizeOfGlobalColorTable     // Catch:{ all -> 0x00d0 }
            r7.readColorTable(r9, r1, r8)     // Catch:{ all -> 0x00d0 }
        L_0x0014:
            r10 = 1
            java.util.ArrayList r0 = r7.readBlocks(r0, r9, r10, r8)     // Catch:{ all -> 0x00d0 }
            java.util.ArrayList r11 = new java.util.ArrayList     // Catch:{ all -> 0x00d0 }
            r11.<init>()     // Catch:{ all -> 0x00d0 }
            r12 = 0
            r13 = 0
        L_0x0020:
            int r1 = r0.size()     // Catch:{ all -> 0x00d0 }
            if (r13 >= r1) goto L_0x00a1
            java.lang.Object r1 = r0.get(r13)     // Catch:{ all -> 0x00d0 }
            org.apache.sanselan.formats.gif.GIFBlock r1 = (org.apache.sanselan.formats.gif.GIFBlock) r1     // Catch:{ all -> 0x00d0 }
            int r2 = r1.blockCode     // Catch:{ all -> 0x00d0 }
            r3 = 8703(0x21ff, float:1.2196E-41)
            if (r2 == r3) goto L_0x0033
            goto L_0x008e
        L_0x0033:
            org.apache.sanselan.formats.gif.GenericGIFBlock r1 = (org.apache.sanselan.formats.gif.GenericGIFBlock) r1     // Catch:{ all -> 0x00d0 }
            byte[] r14 = r1.appendSubBlocks(r10)     // Catch:{ all -> 0x00d0 }
            int r1 = r14.length     // Catch:{ all -> 0x00d0 }
            byte[] r2 = XMP_APPLICATION_ID_AND_AUTH_CODE     // Catch:{ all -> 0x00d0 }
            int r2 = r2.length     // Catch:{ all -> 0x00d0 }
            if (r1 >= r2) goto L_0x0040
            goto L_0x008e
        L_0x0040:
            r3 = 0
            byte[] r4 = XMP_APPLICATION_ID_AND_AUTH_CODE     // Catch:{ all -> 0x00d0 }
            r5 = 0
            byte[] r1 = XMP_APPLICATION_ID_AND_AUTH_CODE     // Catch:{ all -> 0x00d0 }
            int r6 = r1.length     // Catch:{ all -> 0x00d0 }
            r1 = r16
            r2 = r14
            boolean r1 = r1.compareByteArrays(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x00d0 }
            if (r1 != 0) goto L_0x0051
            goto L_0x008e
        L_0x0051:
            r1 = 256(0x100, float:3.59E-43)
            byte[] r15 = new byte[r1]     // Catch:{ all -> 0x00d0 }
            r1 = 0
        L_0x0056:
            r2 = 255(0xff, float:3.57E-43)
            if (r1 > r2) goto L_0x0061
            int r2 = r2 - r1
            byte r2 = (byte) r2     // Catch:{ all -> 0x00d0 }
            r15[r1] = r2     // Catch:{ all -> 0x00d0 }
            int r1 = r1 + 1
            goto L_0x0056
        L_0x0061:
            int r1 = r14.length     // Catch:{ all -> 0x00d0 }
            byte[] r2 = XMP_APPLICATION_ID_AND_AUTH_CODE     // Catch:{ all -> 0x00d0 }
            int r2 = r2.length     // Catch:{ all -> 0x00d0 }
            int r3 = r15.length     // Catch:{ all -> 0x00d0 }
            int r2 = r2 + r3
            if (r1 >= r2) goto L_0x006a
            goto L_0x008e
        L_0x006a:
            int r1 = r14.length     // Catch:{ all -> 0x00d0 }
            int r2 = r15.length     // Catch:{ all -> 0x00d0 }
            int r3 = r1 - r2
            r5 = 0
            int r6 = r15.length     // Catch:{ all -> 0x00d0 }
            r1 = r16
            r2 = r14
            r4 = r15
            boolean r1 = r1.compareByteArrays(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x00d0 }
            if (r1 == 0) goto L_0x0099
            java.lang.String r1 = new java.lang.String     // Catch:{ UnsupportedEncodingException -> 0x0091 }
            byte[] r2 = XMP_APPLICATION_ID_AND_AUTH_CODE     // Catch:{ UnsupportedEncodingException -> 0x0091 }
            int r2 = r2.length     // Catch:{ UnsupportedEncodingException -> 0x0091 }
            int r3 = r14.length     // Catch:{ UnsupportedEncodingException -> 0x0091 }
            byte[] r4 = XMP_APPLICATION_ID_AND_AUTH_CODE     // Catch:{ UnsupportedEncodingException -> 0x0091 }
            int r4 = r4.length     // Catch:{ UnsupportedEncodingException -> 0x0091 }
            int r5 = r15.length     // Catch:{ UnsupportedEncodingException -> 0x0091 }
            int r4 = r4 + r5
            int r3 = r3 - r4
            java.lang.String r4 = "utf-8"
            r1.<init>(r14, r2, r3, r4)     // Catch:{ UnsupportedEncodingException -> 0x0091 }
            r11.add(r1)     // Catch:{ UnsupportedEncodingException -> 0x0091 }
        L_0x008e:
            int r13 = r13 + 1
            goto L_0x0020
        L_0x0091:
            org.apache.sanselan.ImageReadException r0 = new org.apache.sanselan.ImageReadException     // Catch:{ all -> 0x00d0 }
            java.lang.String r1 = "Invalid XMP Block in GIF."
            r0.<init>(r1)     // Catch:{ all -> 0x00d0 }
            throw r0     // Catch:{ all -> 0x00d0 }
        L_0x0099:
            org.apache.sanselan.ImageReadException r0 = new org.apache.sanselan.ImageReadException     // Catch:{ all -> 0x00d0 }
            java.lang.String r1 = "XMP block in GIF missing magic trailer."
            r0.<init>(r1)     // Catch:{ all -> 0x00d0 }
            throw r0     // Catch:{ all -> 0x00d0 }
        L_0x00a1:
            int r0 = r11.size()     // Catch:{ all -> 0x00d0 }
            if (r0 >= r10) goto L_0x00b1
            r9.close()     // Catch:{ Exception -> 0x00ab }
            goto L_0x00b0
        L_0x00ab:
            r0 = move-exception
            r1 = r0
            org.apache.sanselan.util.Debug.debug(r1)
        L_0x00b0:
            return r8
        L_0x00b1:
            int r0 = r11.size()     // Catch:{ all -> 0x00d0 }
            if (r0 > r10) goto L_0x00c8
            java.lang.Object r0 = r11.get(r12)     // Catch:{ all -> 0x00d0 }
            r1 = r0
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ all -> 0x00d0 }
            r9.close()     // Catch:{ Exception -> 0x00c2 }
            goto L_0x00c7
        L_0x00c2:
            r0 = move-exception
            r2 = r0
            org.apache.sanselan.util.Debug.debug(r2)
        L_0x00c7:
            return r1
        L_0x00c8:
            org.apache.sanselan.ImageReadException r0 = new org.apache.sanselan.ImageReadException     // Catch:{ all -> 0x00d0 }
            java.lang.String r1 = "More than one XMP Block in GIF."
            r0.<init>(r1)     // Catch:{ all -> 0x00d0 }
            throw r0     // Catch:{ all -> 0x00d0 }
        L_0x00d0:
            r0 = move-exception
            r1 = r0
            goto L_0x00d6
        L_0x00d3:
            r0 = move-exception
            r1 = r0
            r9 = r8
        L_0x00d6:
            r9.close()     // Catch:{ Exception -> 0x00da }
            goto L_0x00df
        L_0x00da:
            r0 = move-exception
            r2 = r0
            org.apache.sanselan.util.Debug.debug(r2)
        L_0x00df:
            throw r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.sanselan.formats.gif.GifImageParser.getXmpXml(org.apache.sanselan.common.byteSources.ByteSource, java.util.Map):java.lang.String");
    }
}
