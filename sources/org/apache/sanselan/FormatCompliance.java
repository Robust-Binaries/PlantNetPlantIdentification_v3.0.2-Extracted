package org.apache.sanselan;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class FormatCompliance {
    private final ArrayList comments = new ArrayList();
    private final String description;
    private final boolean failOnError;

    public FormatCompliance(String str) {
        this.description = str;
        this.failOnError = false;
    }

    public FormatCompliance(String str, boolean z) {
        this.description = str;
        this.failOnError = z;
    }

    public static final FormatCompliance getDefault() {
        return new FormatCompliance("ignore", false);
    }

    public void addComment(String str) throws ImageReadException {
        this.comments.add(str);
        if (this.failOnError) {
            throw new ImageReadException(str);
        }
    }

    public void addComment(String str, int i) throws ImageReadException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append(getValueDescription(i));
        addComment(stringBuffer.toString());
    }

    public String toString() {
        StringWriter stringWriter = new StringWriter();
        dump(new PrintWriter(stringWriter));
        return stringWriter.getBuffer().toString();
    }

    public void dump() {
        dump(new PrintWriter(new OutputStreamWriter(System.out)));
    }

    public void dump(PrintWriter printWriter) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Format Compliance: ");
        stringBuffer.append(this.description);
        printWriter.println(stringBuffer.toString());
        if (this.comments.size() == 0) {
            printWriter.println("\tNo comments.");
        } else {
            int i = 0;
            while (i < this.comments.size()) {
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("\t");
                int i2 = i + 1;
                stringBuffer2.append(i2);
                stringBuffer2.append(": ");
                stringBuffer2.append(this.comments.get(i));
                printWriter.println(stringBuffer2.toString());
                i = i2;
            }
        }
        printWriter.println("");
        printWriter.flush();
    }

    private String getValueDescription(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(i);
        stringBuffer.append(" (");
        stringBuffer.append(Integer.toHexString(i));
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    public boolean compare_bytes(String str, byte[] bArr, byte[] bArr2) throws ImageReadException {
        if (bArr.length != bArr2.length) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str);
            stringBuffer.append(": ");
            stringBuffer.append("Unexpected length: (expected: ");
            stringBuffer.append(bArr.length);
            stringBuffer.append(", actual: ");
            stringBuffer.append(bArr2.length);
            stringBuffer.append(")");
            addComment(stringBuffer.toString());
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != bArr2[i]) {
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append(str);
                stringBuffer2.append(": ");
                stringBuffer2.append("Unexpected value: (expected: ");
                stringBuffer2.append(getValueDescription(bArr[i]));
                stringBuffer2.append(", actual: ");
                stringBuffer2.append(getValueDescription(bArr2[i]));
                stringBuffer2.append(")");
                addComment(stringBuffer2.toString());
                return false;
            }
        }
        return true;
    }

    public boolean checkBounds(String str, int i, int i2, int i3) throws ImageReadException {
        if (i3 >= i && i3 <= i2) {
            return true;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append(": ");
        stringBuffer.append("bounds check: ");
        stringBuffer.append(i);
        stringBuffer.append(" <= ");
        stringBuffer.append(i3);
        stringBuffer.append(" <= ");
        stringBuffer.append(i2);
        stringBuffer.append(": false");
        addComment(stringBuffer.toString());
        return false;
    }

    public boolean compare(String str, int i, int i2) throws ImageReadException {
        return compare(str, new int[]{i}, i2);
    }

    public boolean compare(String str, int[] iArr, int i) throws ImageReadException {
        for (int i2 : iArr) {
            if (i == i2) {
                return true;
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(str);
        stringBuffer2.append(": ");
        stringBuffer2.append("Unexpected value: (valid: ");
        stringBuffer.append(stringBuffer2.toString());
        if (iArr.length > 1) {
            stringBuffer.append("{");
        }
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (i3 > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(getValueDescription(iArr[i3]));
        }
        if (iArr.length > 1) {
            stringBuffer.append("}");
        }
        StringBuffer stringBuffer3 = new StringBuffer();
        stringBuffer3.append(", actual: ");
        stringBuffer3.append(getValueDescription(i));
        stringBuffer3.append(")");
        stringBuffer.append(stringBuffer3.toString());
        addComment(stringBuffer.toString());
        return false;
    }
}
