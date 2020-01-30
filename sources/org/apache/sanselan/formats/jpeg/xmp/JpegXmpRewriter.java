package org.apache.sanselan.formats.jpeg.xmp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.common.byteSources.ByteSourceFile;
import org.apache.sanselan.common.byteSources.ByteSourceInputStream;
import org.apache.sanselan.formats.jpeg.JpegConstants;

public class JpegXmpRewriter extends JpegRewriter {
    public void removeXmpXml(File file, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeXmpXml((ByteSource) new ByteSourceFile(file), outputStream);
    }

    public void removeXmpXml(byte[] bArr, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeXmpXml((ByteSource) new ByteSourceArray(bArr), outputStream);
    }

    public void removeXmpXml(InputStream inputStream, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        removeXmpXml((ByteSource) new ByteSourceInputStream(inputStream, null), outputStream);
    }

    public void removeXmpXml(ByteSource byteSource, OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        writeSegments(outputStream, removeXmpSegments(analyzeJFIF(byteSource).pieces));
    }

    public void updateXmpXml(byte[] bArr, OutputStream outputStream, String str) throws ImageReadException, IOException, ImageWriteException {
        updateXmpXml((ByteSource) new ByteSourceArray(bArr), outputStream, str);
    }

    public void updateXmpXml(InputStream inputStream, OutputStream outputStream, String str) throws ImageReadException, IOException, ImageWriteException {
        updateXmpXml((ByteSource) new ByteSourceInputStream(inputStream, null), outputStream, str);
    }

    public void updateXmpXml(File file, OutputStream outputStream, String str) throws ImageReadException, IOException, ImageWriteException {
        updateXmpXml((ByteSource) new ByteSourceFile(file), outputStream, str);
    }

    public void updateXmpXml(ByteSource byteSource, OutputStream outputStream, String str) throws ImageReadException, IOException, ImageWriteException {
        List removeXmpSegments = removeXmpSegments(analyzeJFIF(byteSource).pieces);
        ArrayList arrayList = new ArrayList();
        byte[] bytes = str.getBytes("utf-8");
        int i = 0;
        while (i < bytes.length) {
            int min = Math.min(bytes.length, 65535);
            arrayList.add(new JFIFPieceSegment(JpegConstants.JPEG_APP1_Marker, writeXmpSegment(bytes, i, min)));
            i += min;
        }
        writeSegments(outputStream, insertAfterLastAppSegments(removeXmpSegments, arrayList));
    }

    private byte[] writeXmpSegment(byte[] bArr, int i, int i2) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(XMP_IDENTIFIER);
        byteArrayOutputStream.write(bArr, i, i2);
        return byteArrayOutputStream.toByteArray();
    }
}
