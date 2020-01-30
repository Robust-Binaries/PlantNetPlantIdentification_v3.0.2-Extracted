package org.apache.sanselan.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.sanselan.ImageReadException;

public class PackBits {
    public byte[] decompress(byte[] bArr, int i) throws ImageReadException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            if (i3 < bArr.length) {
                int i4 = i3 + 1;
                byte b = bArr[i3];
                if (b >= 0 && b <= Byte.MAX_VALUE) {
                    int i5 = b + 1;
                    i2 += i5;
                    int i6 = i4;
                    int i7 = 0;
                    while (i7 < i5) {
                        int i8 = i6 + 1;
                        byteArrayOutputStream.write(bArr[i6]);
                        i7++;
                        i6 = i8;
                    }
                    i3 = i6;
                } else if (b >= -127 && b <= -1) {
                    int i9 = i4 + 1;
                    byte b2 = bArr[i4];
                    int i10 = (-b) + 1;
                    i2 += i10;
                    for (int i11 = 0; i11 < i10; i11++) {
                        byteArrayOutputStream.write(b2);
                    }
                    i3 = i9;
                } else if (b != Byte.MIN_VALUE) {
                    i3 = i4;
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("Packbits: ");
                    stringBuffer.append(b);
                    throw new ImageReadException(stringBuffer.toString());
                }
            } else {
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("Tiff: Unpack bits source exhausted: ");
                stringBuffer2.append(i3);
                stringBuffer2.append(", done + ");
                stringBuffer2.append(i2);
                stringBuffer2.append(", expected + ");
                stringBuffer2.append(i);
                throw new ImageReadException(stringBuffer2.toString());
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private int findNextDuplicate(byte[] bArr, int i) {
        if (i >= bArr.length) {
            return -1;
        }
        byte b = bArr[i];
        int i2 = i + 1;
        while (i2 < bArr.length) {
            byte b2 = bArr[i2];
            if (b2 == b) {
                return i2 - 1;
            }
            i2++;
            b = b2;
        }
        return -1;
    }

    private int findRunLength(byte[] bArr, int i) {
        byte b = bArr[i];
        int i2 = i + 1;
        while (i2 < bArr.length && bArr[i2] == b) {
            i2++;
        }
        return i2 - i;
    }

    public byte[] compress(byte[] bArr) throws IOException {
        MyByteArrayOutputStream myByteArrayOutputStream = new MyByteArrayOutputStream(bArr.length * 2);
        int i = 0;
        while (i < bArr.length) {
            int findNextDuplicate = findNextDuplicate(bArr, i);
            if (findNextDuplicate == i) {
                int min = Math.min(findRunLength(bArr, findNextDuplicate), 128);
                myByteArrayOutputStream.write(-(min - 1));
                myByteArrayOutputStream.write(bArr[i]);
                i += min;
            } else {
                int i2 = findNextDuplicate - i;
                if (findNextDuplicate > 0) {
                    int findRunLength = findRunLength(bArr, findNextDuplicate);
                    if (findRunLength < 3) {
                        int i3 = i + i2 + findRunLength;
                        int findNextDuplicate2 = findNextDuplicate(bArr, i3);
                        if (findNextDuplicate2 != i3) {
                            i2 = findNextDuplicate2 - i;
                            findNextDuplicate = findNextDuplicate2;
                        }
                    }
                }
                if (findNextDuplicate < 0) {
                    i2 = bArr.length - i;
                }
                int min2 = Math.min(i2, 128);
                myByteArrayOutputStream.write(min2 - 1);
                int i4 = i;
                for (int i5 = 0; i5 < min2; i5++) {
                    myByteArrayOutputStream.write(bArr[i4]);
                    i4++;
                }
                i = i4;
            }
        }
        return myByteArrayOutputStream.toByteArray();
    }
}
