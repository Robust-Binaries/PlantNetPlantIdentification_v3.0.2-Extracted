package org.apache.sanselan.formats.pnm;

import java.io.IOException;
import java.io.InputStream;

class WhiteSpaceReader {
    int count = 0;

    /* renamed from: is */
    private final InputStream f169is;

    public WhiteSpaceReader(InputStream inputStream) {
        this.f169is = inputStream;
    }

    private char read() throws IOException {
        int read = this.f169is.read();
        if (read >= 0) {
            return (char) read;
        }
        throw new IOException("PNM: Unexpected EOF");
    }

    public char nextChar() throws IOException {
        char read = read();
        if (read == '#') {
            while (read != 10 && read != 13) {
                read = read();
            }
        }
        return read;
    }

    public String readtoWhiteSpace() throws IOException {
        char nextChar = nextChar();
        while (Character.isWhitespace(nextChar)) {
            nextChar = nextChar();
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (!Character.isWhitespace(nextChar)) {
            stringBuffer.append(nextChar);
            nextChar = nextChar();
        }
        return stringBuffer.toString();
    }
}
