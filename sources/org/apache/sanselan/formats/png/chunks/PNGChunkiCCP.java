package org.apache.sanselan.formats.png.chunks;

import java.io.IOException;
import java.io.PrintStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.common.ZLibUtils;

public class PNGChunkiCCP extends PNGChunk {
    public final byte[] CompressedProfile;
    public final int CompressionMethod;
    public final String ProfileName;
    public final byte[] UncompressedProfile;

    public PNGChunkiCCP(int i, int i2, int i3, byte[] bArr) throws ImageReadException, IOException {
        String str;
        super(i, i2, i3, bArr);
        int findNull = findNull(bArr);
        if (findNull >= 0) {
            byte[] bArr2 = new byte[findNull];
            System.arraycopy(bArr, 0, bArr2, 0, findNull);
            this.ProfileName = new String(bArr2);
            int i4 = findNull + 1;
            this.CompressionMethod = bArr[i4];
            int i5 = i4 + 1;
            int length = bArr.length - i5;
            this.CompressedProfile = new byte[length];
            System.arraycopy(bArr, i5, this.CompressedProfile, 0, length);
            if (getDebug()) {
                PrintStream printStream = System.out;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("ProfileName: ");
                stringBuffer.append(this.ProfileName);
                printStream.println(stringBuffer.toString());
                PrintStream printStream2 = System.out;
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("ProfileName.length(): ");
                stringBuffer2.append(this.ProfileName.length());
                printStream2.println(stringBuffer2.toString());
                PrintStream printStream3 = System.out;
                StringBuffer stringBuffer3 = new StringBuffer();
                stringBuffer3.append("CompressionMethod: ");
                stringBuffer3.append(this.CompressionMethod);
                printStream3.println(stringBuffer3.toString());
                PrintStream printStream4 = System.out;
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append("CompressedProfileLength: ");
                stringBuffer4.append(length);
                printStream4.println(stringBuffer4.toString());
                PrintStream printStream5 = System.out;
                StringBuffer stringBuffer5 = new StringBuffer();
                stringBuffer5.append("bytes.length: ");
                stringBuffer5.append(bArr.length);
                printStream5.println(stringBuffer5.toString());
            }
            this.UncompressedProfile = new ZLibUtils().inflate(this.CompressedProfile);
            if (getDebug()) {
                PrintStream printStream6 = System.out;
                StringBuffer stringBuffer6 = new StringBuffer();
                stringBuffer6.append("UncompressedProfile: ");
                if (this.UncompressedProfile == null) {
                    str = "null";
                } else {
                    StringBuffer stringBuffer7 = new StringBuffer();
                    stringBuffer7.append("");
                    stringBuffer7.append(bArr.length);
                    str = stringBuffer7.toString();
                }
                stringBuffer6.append(str);
                printStream6.println(stringBuffer6.toString());
                return;
            }
            return;
        }
        throw new ImageReadException("PNGChunkiCCP: No Profile Name");
    }
}
