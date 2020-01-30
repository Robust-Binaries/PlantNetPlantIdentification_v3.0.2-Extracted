package org.apache.sanselan.util;

import java.io.UnsupportedEncodingException;
import kotlin.UByte;
import org.apache.sanselan.common.BinaryConstants;

public abstract class UnicodeUtils implements BinaryConstants {
    public static final int CHAR_ENCODING_CODE_AMBIGUOUS = -1;
    public static final int CHAR_ENCODING_CODE_ISO_8859_1 = 0;
    public static final int CHAR_ENCODING_CODE_UTF_16_BIG_ENDIAN_NO_BOM = 3;
    public static final int CHAR_ENCODING_CODE_UTF_16_BIG_ENDIAN_WITH_BOM = 1;
    public static final int CHAR_ENCODING_CODE_UTF_16_LITTLE_ENDIAN_NO_BOM = 4;
    public static final int CHAR_ENCODING_CODE_UTF_16_LITTLE_ENDIAN_WITH_BOM = 2;
    public static final int CHAR_ENCODING_CODE_UTF_8 = 5;

    public static class UnicodeException extends Exception {
        public UnicodeException(String str) {
            super(str);
        }
    }

    private static class UnicodeMetricsASCII extends UnicodeUtils {
        private UnicodeMetricsASCII() {
            super();
        }

        public int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException {
            while (i < bArr.length) {
                if (bArr[i] == 0) {
                    if (z) {
                        i++;
                    }
                    return i;
                }
                i++;
            }
            return bArr.length;
        }
    }

    private static abstract class UnicodeMetricsUTF16 extends UnicodeUtils {
        protected static final int BYTE_ORDER_BIG_ENDIAN = 0;
        protected static final int BYTE_ORDER_LITTLE_ENDIAN = 1;
        protected int byteOrder = 0;

        public UnicodeMetricsUTF16(int i) {
            super();
            this.byteOrder = i;
        }

        public boolean isValid(byte[] bArr, int i, boolean z, boolean z2) throws UnicodeException {
            while (i != bArr.length) {
                if (i >= bArr.length - 1) {
                    return false;
                }
                int i2 = i + 1;
                byte b = bArr[i] & UByte.MAX_VALUE;
                int i3 = i2 + 1;
                byte b2 = bArr[i2] & UByte.MAX_VALUE;
                byte b3 = this.byteOrder == 0 ? b : b2;
                if (b == 0 && b2 == 0) {
                    return z;
                }
                if (b3 < 216) {
                    i = i3;
                } else if (b3 >= 220 || i3 >= bArr.length - 1) {
                    return false;
                } else {
                    int i4 = i3 + 1;
                    byte b4 = bArr[i3] & UByte.MAX_VALUE;
                    int i5 = i4 + 1;
                    byte b5 = bArr[i4] & UByte.MAX_VALUE;
                    if (this.byteOrder == 0) {
                        b5 = b4;
                    }
                    if (b5 < 220) {
                        return false;
                    }
                    i = i5;
                }
            }
            return !z2;
        }

        public int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException {
            int i2;
            while (true) {
                int i3 = i2;
                if (i3 == bArr.length) {
                    return bArr.length;
                }
                if (i3 <= bArr.length - 1) {
                    int i4 = i3 + 1;
                    byte b = bArr[i3] & UByte.MAX_VALUE;
                    i2 = i4 + 1;
                    byte b2 = bArr[i4] & UByte.MAX_VALUE;
                    byte b3 = this.byteOrder == 0 ? b : b2;
                    if (b == 0 && b2 == 0) {
                        if (!z) {
                            i2 -= 2;
                        }
                        return i2;
                    } else if (b3 >= 216) {
                        if (i2 <= bArr.length - 1) {
                            int i5 = i2 + 1;
                            byte b4 = bArr[i2] & UByte.MAX_VALUE;
                            i2 = i5 + 1;
                            byte b5 = bArr[i5] & UByte.MAX_VALUE;
                            if (this.byteOrder == 0) {
                                b5 = b4;
                            }
                            if (b5 < 220) {
                                throw new UnicodeException("Invalid code point.");
                            }
                        } else {
                            throw new UnicodeException("Terminator not found.");
                        }
                    }
                } else {
                    throw new UnicodeException("Terminator not found.");
                }
            }
        }
    }

    private static class UnicodeMetricsUTF16NoBOM extends UnicodeMetricsUTF16 {
        public UnicodeMetricsUTF16NoBOM(int i) {
            super(i);
        }
    }

    private static class UnicodeMetricsUTF16WithBOM extends UnicodeMetricsUTF16 {
        public UnicodeMetricsUTF16WithBOM() {
            super(0);
        }

        public int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException {
            if (i < bArr.length - 1) {
                int i2 = i + 1;
                byte b = bArr[i] & UByte.MAX_VALUE;
                int i3 = i2 + 1;
                byte b2 = bArr[i2] & UByte.MAX_VALUE;
                if (b == 255 && b2 == 254) {
                    this.byteOrder = 1;
                } else if (b == 254 && b2 == 255) {
                    this.byteOrder = 0;
                } else {
                    throw new UnicodeException("Invalid byte order mark.");
                }
                return super.findEnd(bArr, i3, z);
            }
            throw new UnicodeException("Missing BOM.");
        }
    }

    private static class UnicodeMetricsUTF8 extends UnicodeUtils {
        private UnicodeMetricsUTF8() {
            super();
        }

        public int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException {
            while (i != bArr.length) {
                if (i <= bArr.length) {
                    int i2 = i + 1;
                    byte b = bArr[i] & UByte.MAX_VALUE;
                    if (b == 0) {
                        if (!z) {
                            i2--;
                        }
                        return i2;
                    } else if (b <= Byte.MAX_VALUE) {
                        i = i2;
                    } else if (b <= 223) {
                        if (i2 < bArr.length) {
                            i = i2 + 1;
                            byte b2 = bArr[i2] & UByte.MAX_VALUE;
                            if (b2 < 128 || b2 > 191) {
                                throw new UnicodeException("Invalid code point.");
                            }
                        } else {
                            throw new UnicodeException("Invalid unicode.");
                        }
                    } else if (b <= 239) {
                        if (i2 < bArr.length - 1) {
                            int i3 = i2 + 1;
                            byte b3 = bArr[i2] & UByte.MAX_VALUE;
                            if (b3 < 128 || b3 > 191) {
                                throw new UnicodeException("Invalid code point.");
                            }
                            int i4 = i3 + 1;
                            byte b4 = bArr[i3] & UByte.MAX_VALUE;
                            if (b4 < 128 || b4 > 191) {
                                throw new UnicodeException("Invalid code point.");
                            }
                            i = i4;
                        } else {
                            throw new UnicodeException("Invalid unicode.");
                        }
                    } else if (b > 244) {
                        throw new UnicodeException("Invalid code point.");
                    } else if (i2 < bArr.length - 2) {
                        int i5 = i2 + 1;
                        byte b5 = bArr[i2] & UByte.MAX_VALUE;
                        if (b5 < 128 || b5 > 191) {
                            throw new UnicodeException("Invalid code point.");
                        }
                        int i6 = i5 + 1;
                        byte b6 = bArr[i5] & UByte.MAX_VALUE;
                        if (b6 < 128 || b6 > 191) {
                            throw new UnicodeException("Invalid code point.");
                        }
                        i = i6 + 1;
                        byte b7 = bArr[i6] & UByte.MAX_VALUE;
                        if (b7 < 128 || b7 > 191) {
                            throw new UnicodeException("Invalid code point.");
                        }
                    } else {
                        throw new UnicodeException("Invalid unicode.");
                    }
                } else {
                    throw new UnicodeException("Terminator not found.");
                }
            }
            return bArr.length;
        }
    }

    /* access modifiers changed from: protected */
    public abstract int findEnd(byte[] bArr, int i, boolean z) throws UnicodeException;

    private UnicodeUtils() {
    }

    public static final boolean isValidISO_8859_1(String str) {
        try {
            return str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error parsing string.", e);
        }
    }

    private static int findFirstDoubleByteTerminator(byte[] bArr, int i) {
        for (int i2 = i; i2 < bArr.length - 1; i2 += 2) {
            byte b = bArr[i + 1] & UByte.MAX_VALUE;
            if ((bArr[i] & UByte.MAX_VALUE) == 0 && b == 0) {
                return i2;
            }
        }
        return -1;
    }

    public final int findEndWithTerminator(byte[] bArr, int i) throws UnicodeException {
        return findEnd(bArr, i, true);
    }

    public final int findEndWithoutTerminator(byte[] bArr, int i) throws UnicodeException {
        return findEnd(bArr, i, false);
    }

    public static UnicodeUtils getInstance(int i) throws UnicodeException {
        switch (i) {
            case 0:
                return new UnicodeMetricsASCII();
            case 1:
            case 2:
                return new UnicodeMetricsUTF16WithBOM();
            case 3:
                return new UnicodeMetricsUTF16NoBOM(77);
            case 4:
                return new UnicodeMetricsUTF16NoBOM(73);
            case 5:
                return new UnicodeMetricsUTF8();
            default:
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("Unknown char encoding code: ");
                stringBuffer.append(i);
                throw new UnicodeException(stringBuffer.toString());
        }
    }
}
