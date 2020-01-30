package org.apache.sanselan.formats.jpeg.iptc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.common.byteSources.ByteSourceInputStream;
import org.apache.sanselan.formats.jpeg.JpegConstants;
import org.apache.sanselan.formats.jpeg.xmp.JpegRewriter;

public class JpegIptcRewriter extends JpegRewriter implements IPTCConstants {
    public void removeIPTC(File file, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeIPTC((ByteSource) new ByteSourceFile(file), outputStream);
    }

    public void removeIPTC(byte[] bArr, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeIPTC((ByteSource) new ByteSourceArray(bArr), outputStream);
    }

    public void removeIPTC(InputStream inputStream, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeIPTC((ByteSource) new ByteSourceInputStream(inputStream, null), outputStream);
    }

    public void removeIPTC(ByteSource byteSource, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        List list = analyzeJFIF(byteSource).pieces;
        List findPhotoshopApp13Segments = findPhotoshopApp13Segments(list);
        if (findPhotoshopApp13Segments.size() <= 1) {
            List removePhotoshopApp13Segments = removePhotoshopApp13Segments(list);
            if (findPhotoshopApp13Segments.size() == 1) {
                JFIFPieceSegment jFIFPieceSegment = (JFIFPieceSegment) findPhotoshopApp13Segments.get(0);
                removePhotoshopApp13Segments.add(list.indexOf(jFIFPieceSegment), new JFIFPieceSegment(jFIFPieceSegment.marker, new IPTCParser().writePhotoshopApp13Segment(new PhotoshopApp13Data(new ArrayList(), new IPTCParser().parsePhotoshopSegment(jFIFPieceSegment.segmentData, new HashMap()).getNonIptcBlocks()))));
            }
            writeSegments(outputStream, removePhotoshopApp13Segments);
            return;
        }
        throw new ImageReadException("Image contains more than one Photoshop App13 segment.");
    }

    public void writeIPTC(byte[] bArr, OutputStream outputStream, PhotoshopApp13Data photoshopApp13Data) throws ImageReadException, IOException, ImageWriteException {
        writeIPTC((ByteSource) new ByteSourceArray(bArr), outputStream, photoshopApp13Data);
    }

    public void writeIPTC(InputStream inputStream, OutputStream outputStream, PhotoshopApp13Data photoshopApp13Data) throws ImageReadException, IOException, ImageWriteException {
        writeIPTC((ByteSource) new ByteSourceInputStream(inputStream, null), outputStream, photoshopApp13Data);
    }

    public void writeIPTC(File file, OutputStream outputStream, PhotoshopApp13Data photoshopApp13Data) throws ImageReadException, IOException, ImageWriteException {
        writeIPTC((ByteSource) new ByteSourceFile(file), outputStream, photoshopApp13Data);
    }

    public void writeIPTC(ByteSource byteSource, OutputStream outputStream, PhotoshopApp13Data photoshopApp13Data) throws ImageReadException, IOException, ImageWriteException {
        List list = analyzeJFIF(byteSource).pieces;
        if (findPhotoshopApp13Segments(list).size() <= 1) {
            List removePhotoshopApp13Segments = removePhotoshopApp13Segments(list);
            List nonIptcBlocks = photoshopApp13Data.getNonIptcBlocks();
            nonIptcBlocks.add(new IPTCBlock(IPTCConstants.IMAGE_RESOURCE_BLOCK_IPTC_DATA, new byte[0], new IPTCParser().writeIPTCBlock(photoshopApp13Data.getRecords())));
            writeSegments(outputStream, insertAfterLastAppSegments(removePhotoshopApp13Segments, Arrays.asList(new JFIFPieceSegment[]{new JFIFPieceSegment(JpegConstants.JPEG_APP13_Marker, new IPTCParser().writePhotoshopApp13Segment(new PhotoshopApp13Data(photoshopApp13Data.getRecords(), nonIptcBlocks)))})));
            return;
        }
        throw new ImageReadException("Image contains more than one Photoshop App13 segment.");
    }
}
