package org.apache.sanselan;

import android.support.p000v4.p002os.EnvironmentCompat;
import android.support.p000v4.view.PointerIconCompat;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.sanselan.icc.IccProfileParser;

public class ImageDump {
    private String colorSpaceTypeToName(ColorSpace colorSpace) {
        int type = colorSpace.getType();
        if (type == 5) {
            return "TYPE_RGB";
        }
        if (type == 9) {
            return "TYPE_CMYK";
        }
        switch (type) {
            case 1000:
                return "CS_sRGB";
            case 1001:
                return "CS_CIEXYZ";
            case 1002:
                return "CS_PYCC";
            case 1003:
                return "CS_GRAY";
            case PointerIconCompat.TYPE_WAIT /*1004*/:
                return "CS_LINEAR_RGB";
            default:
                return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }

    public void dumpColorSpace(String str, ColorSpace colorSpace) throws ImageReadException, IOException {
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append("type: ");
        stringBuffer.append(colorSpace.getType());
        stringBuffer.append(" (");
        stringBuffer.append(colorSpaceTypeToName(colorSpace));
        stringBuffer.append(")");
        printStream.println(stringBuffer.toString());
        if (!(colorSpace instanceof ICC_ColorSpace)) {
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append(": ");
            stringBuffer2.append("Unknown ColorSpace: ");
            stringBuffer2.append(colorSpace.getClass().getName());
            printStream2.println(stringBuffer2.toString());
            return;
        }
        new IccProfileParser().getICCProfileInfo(((ICC_ColorSpace) colorSpace).getProfile().getData()).dump(str);
    }

    public void dump(BufferedImage bufferedImage) throws ImageReadException, IOException {
        dump("", bufferedImage);
    }

    public void dump(String str, BufferedImage bufferedImage) throws ImageReadException, IOException {
        PrintStream printStream = System.out;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append("dump");
        printStream.println(stringBuffer.toString());
        dumpColorSpace(str, bufferedImage.getColorModel().getColorSpace());
        dumpBIProps(str, bufferedImage);
    }

    public void dumpBIProps(String str, BufferedImage bufferedImage) {
        String[] propertyNames = bufferedImage.getPropertyNames();
        if (propertyNames == null) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append(": no props");
            printStream.println(stringBuffer.toString());
            return;
        }
        for (String str2 : propertyNames) {
            PrintStream printStream2 = System.out;
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append(": ");
            stringBuffer2.append(str2);
            stringBuffer2.append(": ");
            stringBuffer2.append(bufferedImage.getProperty(str2));
            printStream2.println(stringBuffer2.toString());
        }
    }
}
