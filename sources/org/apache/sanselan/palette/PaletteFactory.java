package org.apache.sanselan.palette;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import kotlin.UByte;

public class PaletteFactory {
    public static final int components = 3;
    private static final boolean debug = false;

    private static class DivisionCandidate {
        /* access modifiers changed from: private */
        public final ColorSpaceSubset dst_a;
        /* access modifiers changed from: private */
        public final ColorSpaceSubset dst_b;

        public DivisionCandidate(ColorSpaceSubset colorSpaceSubset, ColorSpaceSubset colorSpaceSubset2, ColorSpaceSubset colorSpaceSubset3) {
            this.dst_a = colorSpaceSubset2;
            this.dst_b = colorSpaceSubset3;
        }
    }

    private int pixelToQuantizationTableIndex(int i, int i2) {
        int i3 = (1 << i2) - 1;
        int i4 = i;
        int i5 = 0;
        for (int i6 = 0; i6 < 3; i6++) {
            int i7 = i4 & 255;
            i4 >>= 8;
            i5 = (i5 << i2) | ((i7 >> (8 - i2)) & i3);
        }
        return i5;
    }

    public void makePaletteFancy(BufferedImage bufferedImage) {
        byte[] bArr = new byte[2097152];
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = 0;
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int rgb = bufferedImage.getRGB(i3, i2);
                int i4 = 2097151 & rgb;
                bArr[i4] = (byte) ((1 << ((rgb >> 21) & 7)) | bArr[i4]);
            }
        }
        int i5 = 0;
        int i6 = 0;
        while (i5 < bArr.length) {
            byte b = bArr[i5] & UByte.MAX_VALUE;
            if (i5 >= 3) {
                int length = bArr.length;
            }
            int i7 = i6;
            for (int i8 = 0; i8 < 8; i8++) {
                if (((1 << (7 - i8)) & b) > 0) {
                    i7++;
                }
            }
            i5++;
            i6 = i7;
        }
        int[] iArr = new int[i6];
        int i9 = 0;
        int i10 = 0;
        while (i9 < bArr.length) {
            byte b2 = bArr[i9] & UByte.MAX_VALUE;
            int i11 = i10;
            for (int i12 = 0; i12 < 8; i12++) {
                int i13 = 7 - i12;
                if (((1 << i13) & b2) > 0) {
                    int i14 = (i13 << 21) | i9;
                    if (i11 < iArr.length) {
                        iArr[i11] = i14;
                    }
                    i11++;
                }
            }
            i9++;
            i10 = i11;
        }
    }

    private int getFrequencyTotal(int[] iArr, int[] iArr2, int[] iArr3, int i) {
        int i2 = 0;
        for (int i3 = iArr2[2]; i3 <= iArr3[2]; i3++) {
            int i4 = i3 << (i * 2);
            for (int i5 = iArr2[1]; i5 <= iArr3[1]; i5++) {
                int i6 = i5 << (i * 1);
                for (int i7 = iArr2[0]; i7 <= iArr3[0]; i7++) {
                    i2 += iArr[i4 | i6 | i7];
                }
            }
        }
        return i2;
    }

    private DivisionCandidate finishDivision(int[] iArr, ColorSpaceSubset colorSpaceSubset, int i, int i2, int i3, int i4) {
        ColorSpaceSubset colorSpaceSubset2 = colorSpaceSubset;
        int i5 = i3;
        int i6 = i4;
        int i7 = colorSpaceSubset2.total;
        if (i6 < colorSpaceSubset2.mins[i] || i6 >= colorSpaceSubset2.maxs[i] || i5 < 1 || i5 >= i7) {
            return null;
        }
        int i8 = i7 - i5;
        if (i8 < 1 || i8 >= i7) {
            return null;
        }
        int[] iArr2 = new int[colorSpaceSubset2.mins.length];
        System.arraycopy(colorSpaceSubset2.mins, 0, iArr2, 0, colorSpaceSubset2.mins.length);
        int[] iArr3 = new int[colorSpaceSubset2.maxs.length];
        System.arraycopy(colorSpaceSubset2.maxs, 0, iArr3, 0, colorSpaceSubset2.maxs.length);
        iArr3[i] = i6;
        iArr2[i] = i6 + 1;
        ColorSpaceSubset colorSpaceSubset3 = new ColorSpaceSubset(i3, i2, colorSpaceSubset2.mins, iArr3, iArr);
        ColorSpaceSubset colorSpaceSubset4 = new ColorSpaceSubset(i8, i2, iArr2, colorSpaceSubset2.maxs, iArr);
        return new DivisionCandidate(colorSpaceSubset, colorSpaceSubset3, colorSpaceSubset4);
    }

    private ArrayList divideSubset2(int[] iArr, ColorSpaceSubset colorSpaceSubset, int i, int i2) {
        int i3;
        ColorSpaceSubset colorSpaceSubset2 = colorSpaceSubset;
        int i4 = colorSpaceSubset2.total;
        int[] iArr2 = new int[colorSpaceSubset2.mins.length];
        int i5 = 0;
        System.arraycopy(colorSpaceSubset2.mins, 0, iArr2, 0, colorSpaceSubset2.mins.length);
        int[] iArr3 = new int[colorSpaceSubset2.maxs.length];
        System.arraycopy(colorSpaceSubset2.maxs, 0, iArr3, 0, colorSpaceSubset2.maxs.length);
        int i6 = colorSpaceSubset2.mins[i];
        int i7 = 0;
        while (true) {
            if (i6 == colorSpaceSubset2.maxs[i] + 1) {
                int[] iArr4 = iArr;
                int i8 = i2;
                i3 = i5;
                break;
            }
            iArr2[i] = i6;
            iArr3[i] = i6;
            int[] iArr5 = iArr;
            i7 = getFrequencyTotal(iArr, iArr2, iArr3, i2);
            i5 += i7;
            if (i5 >= i4 / 2) {
                i3 = i5;
                break;
            }
            i6++;
        }
        int i9 = i3 - i7;
        int i10 = i6 - 1;
        int[] iArr6 = iArr;
        ColorSpaceSubset colorSpaceSubset3 = colorSpaceSubset;
        int i11 = i;
        int i12 = i2;
        DivisionCandidate finishDivision = finishDivision(iArr6, colorSpaceSubset3, i11, i12, i3, i6);
        DivisionCandidate finishDivision2 = finishDivision(iArr6, colorSpaceSubset3, i11, i12, i9, i10);
        ArrayList arrayList = new ArrayList();
        if (finishDivision != null) {
            arrayList.add(finishDivision);
        }
        if (finishDivision2 != null) {
            arrayList.add(finishDivision2);
        }
        return arrayList;
    }

    private DivisionCandidate divideSubset2(int[] iArr, ColorSpaceSubset colorSpaceSubset, int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(divideSubset2(iArr, colorSpaceSubset, 0, i));
        arrayList.addAll(divideSubset2(iArr, colorSpaceSubset, 1, i));
        arrayList.addAll(divideSubset2(iArr, colorSpaceSubset, 2, i));
        DivisionCandidate divisionCandidate = null;
        double d = Double.MAX_VALUE;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            DivisionCandidate divisionCandidate2 = (DivisionCandidate) arrayList.get(i2);
            ColorSpaceSubset access$000 = divisionCandidate2.dst_a;
            ColorSpaceSubset access$100 = divisionCandidate2.dst_b;
            int i3 = access$000.total;
            int i4 = access$100.total;
            double abs = (double) Math.abs(i3 - i4);
            double max = (double) Math.max(i3, i4);
            Double.isNaN(abs);
            Double.isNaN(max);
            double d2 = abs / max;
            if (divisionCandidate == null || d2 < d) {
                divisionCandidate = divisionCandidate2;
                d = d2;
            }
        }
        return divisionCandidate;
    }

    private ArrayList divide(ArrayList arrayList, int i, int[] iArr, int i2) {
        ArrayList arrayList2 = new ArrayList();
        do {
            int i3 = -1;
            ColorSpaceSubset colorSpaceSubset = null;
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                ColorSpaceSubset colorSpaceSubset2 = (ColorSpaceSubset) arrayList.get(i4);
                if (!arrayList2.contains(colorSpaceSubset2)) {
                    int i5 = colorSpaceSubset2.total;
                    if (colorSpaceSubset == null || i5 > i3) {
                        colorSpaceSubset = colorSpaceSubset2;
                        i3 = i5;
                    }
                }
            }
            if (colorSpaceSubset == null) {
                return arrayList;
            }
            DivisionCandidate divideSubset2 = divideSubset2(iArr, colorSpaceSubset, i2);
            if (divideSubset2 != null) {
                arrayList.remove(colorSpaceSubset);
                arrayList.add(divideSubset2.dst_a);
                arrayList.add(divideSubset2.dst_b);
            } else {
                arrayList2.add(colorSpaceSubset);
            }
        } while (arrayList.size() != i);
        return arrayList;
    }

    public Palette makePaletteQuantized(BufferedImage bufferedImage, int i) {
        int[] iArr = new int[262144];
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ArrayList arrayList = new ArrayList();
        ColorSpaceSubset colorSpaceSubset = new ColorSpaceSubset(width * height, 6);
        arrayList.add(colorSpaceSubset);
        getFrequencyTotal(iArr, colorSpaceSubset.mins, colorSpaceSubset.maxs, 6);
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int pixelToQuantizationTableIndex = pixelToQuantizationTableIndex(bufferedImage.getRGB(i3, i2), 6);
                iArr[pixelToQuantizationTableIndex] = iArr[pixelToQuantizationTableIndex] + 1;
            }
        }
        getFrequencyTotal(iArr, colorSpaceSubset.mins, colorSpaceSubset.maxs, 6);
        ArrayList divide = divide(arrayList, 256, iArr, 6);
        for (int i4 = 0; i4 < divide.size(); i4++) {
            ((ColorSpaceSubset) divide.get(i4)).setAverageRGB(iArr);
        }
        Collections.sort(divide);
        return new QuantizedPalette(divide, 6);
    }

    public SimplePalette makePaletteSimple(BufferedImage bufferedImage, int i) {
        HashMap hashMap = new HashMap();
        int[] iArr = new int[i];
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int i2 = 0;
        int i3 = 0;
        while (i2 < height) {
            int i4 = i3;
            for (int i5 = 0; i5 < width; i5++) {
                int rgb = bufferedImage.getRGB(i5, i2) & ViewCompat.MEASURED_SIZE_MASK;
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("");
                stringBuffer.append(rgb);
                String stringBuffer2 = stringBuffer.toString();
                if (hashMap.get(stringBuffer2) == null) {
                    if (i4 == i) {
                        return null;
                    }
                    iArr[i4] = rgb;
                    hashMap.put(stringBuffer2, stringBuffer2);
                    i4++;
                }
            }
            i2++;
            i3 = i4;
        }
        int[] iArr2 = new int[i3];
        System.arraycopy(iArr, 0, iArr2, 0, i3);
        Arrays.sort(iArr2);
        return new SimplePalette(iArr2);
    }

    public boolean isGrayscale(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (6 == bufferedImage.getColorModel().getColorSpace().getType()) {
            return true;
        }
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int rgb = bufferedImage.getRGB(i2, i);
                int i3 = (rgb >> 16) & 255;
                int i4 = (rgb >> 8) & 255;
                int i5 = (rgb >> 0) & 255;
                if (i3 != i4 || i3 != i5) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasTransparency(BufferedImage bufferedImage) {
        return hasTransparency(bufferedImage, 255);
    }

    public boolean hasTransparency(BufferedImage bufferedImage, int i) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (!bufferedImage.getColorModel().hasAlpha()) {
            return false;
        }
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                if (((bufferedImage.getRGB(i3, i2) >> 24) & 255) < i) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countTrasparentColors(int[] iArr) {
        int i = -1;
        for (int i2 : iArr) {
            if (((i2 >> 24) & 255) < 255) {
                if (i < 0) {
                    i = i2;
                } else if (i2 != i) {
                    return 2;
                }
            }
        }
        if (i < 0) {
            return 0;
        }
        return 1;
    }

    public int countTransparentColors(BufferedImage bufferedImage) {
        if (!bufferedImage.getColorModel().hasAlpha()) {
            return 0;
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int i = 0;
        int i2 = -1;
        while (i < height) {
            int i3 = i2;
            for (int i4 = 0; i4 < width; i4++) {
                int rgb = bufferedImage.getRGB(i4, i);
                if (((rgb >> 24) & 255) < 255) {
                    if (i3 < 0) {
                        i3 = rgb;
                    } else if (rgb != i3) {
                        return 2;
                    }
                }
            }
            i++;
            i2 = i3;
        }
        if (i2 < 0) {
            return 0;
        }
        return 1;
    }
}
