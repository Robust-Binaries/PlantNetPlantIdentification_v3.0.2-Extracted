package org.apache.sanselan.icc;

import android.support.p000v4.p002os.EnvironmentCompat;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.BinaryConstants;
import org.apache.sanselan.common.BinaryInputStream;

public class IccTag implements BinaryConstants, IccConstants {
    public byte[] data = null;
    private int data_type_signature;
    public final IccTagType fIccTagType;
    private IccTagDataType itdt = null;
    public final int length;
    public final int offset;
    public final int signature;

    public IccTag(int i, int i2, int i3, IccTagType iccTagType) {
        this.signature = i;
        this.offset = i2;
        this.length = i3;
        this.fIccTagType = iccTagType;
    }

    public void setData(byte[] bArr) throws ImageReadException, IOException {
        this.data = bArr;
        this.data_type_signature = new BinaryInputStream((InputStream) new ByteArrayInputStream(bArr), 77).read4Bytes("data type signature", "ICC: corrupt tag data");
        this.itdt = getIccTagDataType(this.data_type_signature);
    }

    private IccTagDataType getIccTagDataType(int i) {
        for (int i2 = 0; i2 < IccTagDataTypes.length; i2++) {
            if (IccTagDataTypes[i2].signature == i) {
                return IccTagDataTypes[i2];
            }
        }
        return null;
    }

    public void dump(String str) throws ImageReadException, IOException {
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out));
        dump(printWriter, str);
        printWriter.flush();
    }

    public void dump(PrintWriter printWriter, String str) throws ImageReadException, IOException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append("tag signature: ");
        stringBuffer.append(Integer.toHexString(this.signature));
        stringBuffer.append(" (");
        int i = this.signature;
        stringBuffer.append(new String(new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) ((i >> 0) & 255)}));
        stringBuffer.append(")");
        printWriter.println(stringBuffer.toString());
        if (this.data == null) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append("data: ");
            stringBuffer2.append(this.data);
            printWriter.println(stringBuffer2.toString());
        } else {
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(str);
            stringBuffer3.append("data: ");
            stringBuffer3.append(this.data.length);
            printWriter.println(stringBuffer3.toString());
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append(str);
            stringBuffer4.append("data type signature: ");
            stringBuffer4.append(Integer.toHexString(this.data_type_signature));
            stringBuffer4.append(" (");
            int i2 = this.data_type_signature;
            stringBuffer4.append(new String(new byte[]{(byte) ((i2 >> 24) & 255), (byte) ((i2 >> 16) & 255), (byte) ((i2 >> 8) & 255), (byte) ((i2 >> 0) & 255)}));
            stringBuffer4.append(")");
            printWriter.println(stringBuffer4.toString());
            if (this.itdt == null) {
                StringBuffer stringBuffer5 = new StringBuffer();
                stringBuffer5.append(str);
                stringBuffer5.append("IccTagType : ");
                stringBuffer5.append(EnvironmentCompat.MEDIA_UNKNOWN);
                printWriter.println(stringBuffer5.toString());
            } else {
                StringBuffer stringBuffer6 = new StringBuffer();
                stringBuffer6.append(str);
                stringBuffer6.append("IccTagType : ");
                stringBuffer6.append(this.itdt.name);
                printWriter.println(stringBuffer6.toString());
                this.itdt.dump(str, this.data);
            }
        }
        printWriter.println("");
        printWriter.flush();
    }
}
