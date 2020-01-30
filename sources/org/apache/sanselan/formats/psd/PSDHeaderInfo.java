package org.apache.sanselan.formats.psd;

import java.io.PrintWriter;

public class PSDHeaderInfo {
    public final int Channels;
    public final int Columns;
    public final int Depth;
    public final int Mode;
    public final byte[] Reserved;
    public final int Rows;
    public final int Version;

    public PSDHeaderInfo(int i, byte[] bArr, int i2, int i3, int i4, int i5, int i6) {
        this.Version = i;
        this.Reserved = bArr;
        this.Channels = i2;
        this.Rows = i3;
        this.Columns = i4;
        this.Depth = i5;
        this.Mode = i6;
    }

    public void dump() {
        PrintWriter printWriter = new PrintWriter(System.out);
        dump(printWriter);
        printWriter.flush();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("");
        printWriter.println("Header");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Version: ");
        stringBuffer.append(this.Version);
        stringBuffer.append(" (");
        stringBuffer.append(Integer.toHexString(this.Version));
        stringBuffer.append(")");
        printWriter.println(stringBuffer.toString());
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("Channels: ");
        stringBuffer2.append(this.Channels);
        stringBuffer2.append(" (");
        stringBuffer2.append(Integer.toHexString(this.Channels));
        stringBuffer2.append(")");
        printWriter.println(stringBuffer2.toString());
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append("Rows: ");
        stringBuffer3.append(this.Rows);
        stringBuffer3.append(" (");
        stringBuffer3.append(Integer.toHexString(this.Rows));
        stringBuffer3.append(")");
        printWriter.println(stringBuffer3.toString());
        StringBuffer stringBuffer4 = new StringBuffer();
        stringBuffer4.append("Columns: ");
        stringBuffer4.append(this.Columns);
        stringBuffer4.append(" (");
        stringBuffer4.append(Integer.toHexString(this.Columns));
        stringBuffer4.append(")");
        printWriter.println(stringBuffer4.toString());
        StringBuffer stringBuffer5 = new StringBuffer();
        stringBuffer5.append("Depth: ");
        stringBuffer5.append(this.Depth);
        stringBuffer5.append(" (");
        stringBuffer5.append(Integer.toHexString(this.Depth));
        stringBuffer5.append(")");
        printWriter.println(stringBuffer5.toString());
        StringBuffer stringBuffer6 = new StringBuffer();
        stringBuffer6.append("Mode: ");
        stringBuffer6.append(this.Mode);
        stringBuffer6.append(" (");
        stringBuffer6.append(Integer.toHexString(this.Mode));
        stringBuffer6.append(")");
        printWriter.println(stringBuffer6.toString());
        StringBuffer stringBuffer7 = new StringBuffer();
        stringBuffer7.append("Reserved: ");
        stringBuffer7.append(this.Reserved.length);
        printWriter.println(stringBuffer7.toString());
        printWriter.println("");
        printWriter.flush();
    }
}
