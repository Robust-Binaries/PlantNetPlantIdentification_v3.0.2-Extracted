package org.apache.sanselan.icc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.sanselan.ImageReadException;

public class IccProfileInfo implements IccConstants {
    public final int CMMTypeSignature;
    public final int ColorSpace;
    public final int DeviceManufacturer;
    public final int DeviceModel;
    public final int PrimaryPlatformSignature;
    public final int ProfileConnectionSpace;
    public final int ProfileCreatorSignature;
    public final int ProfileDeviceClassSignature;
    public final int ProfileFileSignature;
    public final byte[] ProfileID;
    public final int ProfileSize;
    public final int ProfileVersion;
    public final int RenderingIntent;
    public final int VariousFlags;
    public final byte[] data;
    public final IccTag[] tags;

    public IccProfileInfo(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, byte[] bArr2, IccTag[] iccTagArr) {
        this.data = bArr;
        this.ProfileSize = i;
        this.CMMTypeSignature = i2;
        this.ProfileVersion = i3;
        this.ProfileDeviceClassSignature = i4;
        this.ColorSpace = i5;
        this.ProfileConnectionSpace = i6;
        this.ProfileFileSignature = i7;
        this.PrimaryPlatformSignature = i8;
        this.VariousFlags = i9;
        this.DeviceManufacturer = i10;
        this.DeviceModel = i11;
        this.RenderingIntent = i12;
        this.ProfileCreatorSignature = i13;
        this.ProfileID = bArr2;
        this.tags = iccTagArr;
    }

    public boolean issRGB() {
        return this.DeviceManufacturer == 1229275936 && this.DeviceModel == 1934772034;
    }

    private void printCharQuad(PrintWriter printWriter, String str, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": '");
        stringBuffer.append((char) ((i >> 24) & 255));
        stringBuffer.append((char) ((i >> 16) & 255));
        stringBuffer.append((char) ((i >> 8) & 255));
        stringBuffer.append((char) ((i >> 0) & 255));
        stringBuffer.append("'");
        printWriter.println(stringBuffer.toString());
    }

    public void dump(String str) throws IOException {
        System.out.print(toString());
    }

    public String toString() {
        try {
            return toString("");
        } catch (Exception unused) {
            return "IccProfileInfo: Error";
        }
    }

    public String toString(String str) throws ImageReadException, IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append("data length: ");
        stringBuffer.append(this.data.length);
        printWriter.println(stringBuffer.toString());
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(str);
        stringBuffer2.append(": ");
        stringBuffer2.append("ProfileDeviceClassSignature");
        printCharQuad(printWriter, stringBuffer2.toString(), this.ProfileDeviceClassSignature);
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(str);
        stringBuffer3.append(": ");
        stringBuffer3.append("CMMTypeSignature");
        printCharQuad(printWriter, stringBuffer3.toString(), this.CMMTypeSignature);
        StringBuffer stringBuffer4 = new StringBuffer();
        stringBuffer4.append(str);
        stringBuffer4.append(": ");
        stringBuffer4.append("ProfileDeviceClassSignature");
        printCharQuad(printWriter, stringBuffer4.toString(), this.ProfileDeviceClassSignature);
        StringBuffer stringBuffer5 = new StringBuffer();
        stringBuffer5.append(str);
        stringBuffer5.append(": ");
        stringBuffer5.append("ColorSpace");
        printCharQuad(printWriter, stringBuffer5.toString(), this.ColorSpace);
        StringBuffer stringBuffer6 = new StringBuffer();
        stringBuffer6.append(str);
        stringBuffer6.append(": ");
        stringBuffer6.append("ProfileConnectionSpace");
        printCharQuad(printWriter, stringBuffer6.toString(), this.ProfileConnectionSpace);
        StringBuffer stringBuffer7 = new StringBuffer();
        stringBuffer7.append(str);
        stringBuffer7.append(": ");
        stringBuffer7.append("ProfileFileSignature");
        printCharQuad(printWriter, stringBuffer7.toString(), this.ProfileFileSignature);
        StringBuffer stringBuffer8 = new StringBuffer();
        stringBuffer8.append(str);
        stringBuffer8.append(": ");
        stringBuffer8.append("PrimaryPlatformSignature");
        printCharQuad(printWriter, stringBuffer8.toString(), this.PrimaryPlatformSignature);
        StringBuffer stringBuffer9 = new StringBuffer();
        stringBuffer9.append(str);
        stringBuffer9.append(": ");
        stringBuffer9.append("ProfileFileSignature");
        printCharQuad(printWriter, stringBuffer9.toString(), this.ProfileFileSignature);
        StringBuffer stringBuffer10 = new StringBuffer();
        stringBuffer10.append(str);
        stringBuffer10.append(": ");
        stringBuffer10.append("DeviceManufacturer");
        printCharQuad(printWriter, stringBuffer10.toString(), this.DeviceManufacturer);
        StringBuffer stringBuffer11 = new StringBuffer();
        stringBuffer11.append(str);
        stringBuffer11.append(": ");
        stringBuffer11.append("DeviceModel");
        printCharQuad(printWriter, stringBuffer11.toString(), this.DeviceModel);
        StringBuffer stringBuffer12 = new StringBuffer();
        stringBuffer12.append(str);
        stringBuffer12.append(": ");
        stringBuffer12.append("RenderingIntent");
        printCharQuad(printWriter, stringBuffer12.toString(), this.RenderingIntent);
        StringBuffer stringBuffer13 = new StringBuffer();
        stringBuffer13.append(str);
        stringBuffer13.append(": ");
        stringBuffer13.append("ProfileCreatorSignature");
        printCharQuad(printWriter, stringBuffer13.toString(), this.ProfileCreatorSignature);
        int i = 0;
        while (true) {
            IccTag[] iccTagArr = this.tags;
            if (i < iccTagArr.length) {
                IccTag iccTag = iccTagArr[i];
                StringBuffer stringBuffer14 = new StringBuffer();
                stringBuffer14.append("\t");
                stringBuffer14.append(i);
                stringBuffer14.append(": ");
                iccTag.dump(printWriter, stringBuffer14.toString());
                i++;
            } else {
                StringBuffer stringBuffer15 = new StringBuffer();
                stringBuffer15.append(str);
                stringBuffer15.append(": ");
                stringBuffer15.append("issRGB: ");
                stringBuffer15.append(issRGB());
                printWriter.println(stringBuffer15.toString());
                printWriter.flush();
                return stringWriter.getBuffer().toString();
            }
        }
    }
}
