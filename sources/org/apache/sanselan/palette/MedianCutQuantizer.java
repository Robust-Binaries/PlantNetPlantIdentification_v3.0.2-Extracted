package org.apache.sanselan.palette;

import android.support.p000v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.util.Debug;

public class MedianCutQuantizer {
    private static final int ALPHA = 0;
    private static final int BLUE = 3;
    private static final int GREEN = 2;
    private static final int RED = 1;
    /* access modifiers changed from: private */
    public final boolean ignoreAlpha;

    private static class ColorCount {
        public final int alpha;
        public final int argb;
        public final int blue;
        public int count = 0;
        public final int green;
        public final int red;

        public ColorCount(int i) {
            this.argb = i;
            this.alpha = (i >> 24) & 255;
            this.red = (i >> 16) & 255;
            this.green = (i >> 8) & 255;
            this.blue = (i >> 0) & 255;
        }

        public int hashCode() {
            return this.argb;
        }

        public boolean equals(Object obj) {
            return ((ColorCount) obj).argb == this.argb;
        }
    }

    private class ColorGroup {
        public final int alpha_diff;
        public final int blue_diff;
        public final ArrayList color_counts;
        public ColorGroupCut cut = null;
        public final int diff_total;
        public final int green_diff;
        public int max_alpha = Integer.MIN_VALUE;
        public int max_blue = Integer.MIN_VALUE;
        public final int max_diff;
        public int max_green = Integer.MIN_VALUE;
        public int max_red = Integer.MIN_VALUE;
        public int min_alpha = Integer.MAX_VALUE;
        public int min_blue = Integer.MAX_VALUE;
        public int min_green = Integer.MAX_VALUE;
        public int min_red = Integer.MAX_VALUE;
        public int palette_index = -1;
        public final int red_diff;

        public ColorGroup(ArrayList arrayList) throws ImageWriteException {
            this.color_counts = arrayList;
            if (arrayList.size() >= 1) {
                int i = 0;
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    ColorCount colorCount = (ColorCount) arrayList.get(i2);
                    this.min_alpha = Math.min(this.min_alpha, colorCount.alpha);
                    this.max_alpha = Math.max(this.max_alpha, colorCount.alpha);
                    this.min_red = Math.min(this.min_red, colorCount.red);
                    this.max_red = Math.max(this.max_red, colorCount.red);
                    this.min_green = Math.min(this.min_green, colorCount.green);
                    this.max_green = Math.max(this.max_green, colorCount.green);
                    this.min_blue = Math.min(this.min_blue, colorCount.blue);
                    this.max_blue = Math.max(this.max_blue, colorCount.blue);
                }
                this.alpha_diff = this.max_alpha - this.min_alpha;
                this.red_diff = this.max_red - this.min_red;
                this.green_diff = this.max_green - this.min_green;
                this.blue_diff = this.max_blue - this.min_blue;
                this.max_diff = Math.max(MedianCutQuantizer.this.ignoreAlpha ? this.red_diff : Math.max(this.alpha_diff, this.red_diff), Math.max(this.green_diff, this.blue_diff));
                if (!MedianCutQuantizer.this.ignoreAlpha) {
                    i = this.alpha_diff;
                }
                this.diff_total = i + this.red_diff + this.green_diff + this.blue_diff;
                return;
            }
            throw new ImageWriteException("empty color_group");
        }

        public boolean contains(int i) {
            int i2 = (i >> 24) & 255;
            int i3 = (i >> 16) & 255;
            int i4 = (i >> 8) & 255;
            int i5 = (i >> 0) & 255;
            if ((MedianCutQuantizer.this.ignoreAlpha || (i2 >= this.min_alpha && i2 <= this.max_alpha)) && i3 >= this.min_red && i3 <= this.max_red && i4 >= this.min_green && i4 <= this.max_green && i5 >= this.min_blue && i5 <= this.max_blue) {
                return true;
            }
            return false;
        }

        public int getMedianValue() {
            int i;
            long j = 0;
            long j2 = 0;
            long j3 = 0;
            long j4 = 0;
            long j5 = 0;
            for (int i2 = 0; i2 < this.color_counts.size(); i2++) {
                ColorCount colorCount = (ColorCount) this.color_counts.get(i2);
                j += (long) colorCount.count;
                j2 += (long) (colorCount.count * colorCount.alpha);
                j3 += (long) (colorCount.count * colorCount.red);
                j4 += (long) (colorCount.count * colorCount.green);
                j5 += (long) (colorCount.count * colorCount.blue);
            }
            if (MedianCutQuantizer.this.ignoreAlpha) {
                i = 255;
            } else {
                double d = (double) j2;
                double d2 = (double) j;
                Double.isNaN(d);
                Double.isNaN(d2);
                i = (int) Math.round(d / d2);
            }
            double d3 = (double) j3;
            double d4 = (double) j;
            Double.isNaN(d3);
            Double.isNaN(d4);
            int round = (int) Math.round(d3 / d4);
            double d5 = (double) j4;
            Double.isNaN(d5);
            Double.isNaN(d4);
            int round2 = (int) Math.round(d5 / d4);
            double d6 = (double) j5;
            Double.isNaN(d6);
            Double.isNaN(d4);
            return ((int) Math.round(d6 / d4)) | (i << 24) | (round << 16) | (round2 << 8);
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("{ColorGroup. min_red: ");
            stringBuffer.append(Integer.toHexString(this.min_red));
            stringBuffer.append(", max_red: ");
            stringBuffer.append(Integer.toHexString(this.max_red));
            stringBuffer.append(", min_green: ");
            stringBuffer.append(Integer.toHexString(this.min_green));
            stringBuffer.append(", max_green: ");
            stringBuffer.append(Integer.toHexString(this.max_green));
            stringBuffer.append(", min_blue: ");
            stringBuffer.append(Integer.toHexString(this.min_blue));
            stringBuffer.append(", max_blue: ");
            stringBuffer.append(Integer.toHexString(this.max_blue));
            stringBuffer.append(", min_alpha: ");
            stringBuffer.append(Integer.toHexString(this.min_alpha));
            stringBuffer.append(", max_alpha: ");
            stringBuffer.append(Integer.toHexString(this.max_alpha));
            stringBuffer.append(", max_diff: ");
            stringBuffer.append(Integer.toHexString(this.max_diff));
            stringBuffer.append(", diff_total: ");
            stringBuffer.append(this.diff_total);
            stringBuffer.append("}");
            return stringBuffer.toString();
        }
    }

    private class ColorGroupCut {
        public final ColorGroup less;
        public final int limit;
        public final int mode;
        public final ColorGroup more;

        public ColorGroupCut(ColorGroup colorGroup, ColorGroup colorGroup2, int i, int i2) {
            this.less = colorGroup;
            this.more = colorGroup2;
            this.mode = i;
            this.limit = i2;
        }

        public ColorGroup getColorGroup(int i) {
            int i2;
            switch (this.mode) {
                case 0:
                    i2 = (i >> 24) & 255;
                    break;
                case 1:
                    i2 = (i >> 16) & 255;
                    break;
                case 2:
                    i2 = (i >> 8) & 255;
                    break;
                case 3:
                    i2 = (i >> 0) & 255;
                    break;
                default:
                    throw new Error("bad mode.");
            }
            if (i2 <= this.limit) {
                return this.less;
            }
            return this.more;
        }
    }

    public class MedianCutPalette extends SimplePalette {
        private final ColorGroup root;

        public MedianCutPalette(ColorGroup colorGroup, int[] iArr) {
            super(iArr);
            this.root = colorGroup;
        }

        public int getPaletteIndex(int i) {
            ColorGroup colorGroup = this.root;
            while (colorGroup.cut != null) {
                colorGroup = colorGroup.cut.getColorGroup(i);
            }
            return colorGroup.palette_index;
        }
    }

    public MedianCutQuantizer(boolean z) {
        this.ignoreAlpha = z;
    }

    public Map groupColors1(BufferedImage bufferedImage, int i, int i2) throws ImageWriteException {
        HashMap hashMap = new HashMap();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[] iArr = new int[width];
        for (int i3 = 0; i3 < height; i3++) {
            bufferedImage.getRGB(0, i3, width, 1, iArr, 0, width);
            for (int i4 = 0; i4 < width; i4++) {
                int i5 = iArr[i4];
                if (this.ignoreAlpha) {
                    i5 &= ViewCompat.MEASURED_SIZE_MASK;
                }
                int i6 = i5 & i2;
                ColorCount colorCount = (ColorCount) hashMap.get(new Integer(i6));
                if (colorCount == null) {
                    colorCount = new ColorCount(i6);
                    hashMap.put(new Integer(i6), colorCount);
                    if (hashMap.keySet().size() > i) {
                        return null;
                    }
                } else {
                    int i7 = i;
                }
                colorCount.count++;
            }
            int i8 = i;
        }
        return hashMap;
    }

    public Map groupColors(BufferedImage bufferedImage, int i) throws ImageWriteException {
        for (int i2 = 0; i2 < 8; i2++) {
            int i3 = 255 & (255 << i2);
            int i4 = (i3 << 24) | (i3 << 8) | i3 | (i3 << 16);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("mask(");
            stringBuffer.append(i2);
            stringBuffer.append(")");
            String stringBuffer2 = stringBuffer.toString();
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(i4);
            stringBuffer3.append(" (");
            stringBuffer3.append(Integer.toHexString(i4));
            stringBuffer3.append(")");
            Debug.debug(stringBuffer2, stringBuffer3.toString());
            Map groupColors1 = groupColors1(bufferedImage, Integer.MAX_VALUE, i4);
            if (groupColors1 != null) {
                return groupColors1;
            }
        }
        throw new Error("");
    }

    public Palette process(BufferedImage bufferedImage, int i, boolean z) throws ImageWriteException {
        Map groupColors = groupColors(bufferedImage, i);
        int size = groupColors.keySet().size();
        int i2 = 0;
        if (size <= i) {
            if (z) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("lossless palette: ");
                stringBuffer.append(size);
                Debug.debug(stringBuffer.toString());
            }
            int[] iArr = new int[size];
            ArrayList arrayList = new ArrayList(groupColors.values());
            while (i2 < arrayList.size()) {
                iArr[i2] = ((ColorCount) arrayList.get(i2)).argb;
                if (this.ignoreAlpha) {
                    iArr[i2] = iArr[i2] | ViewCompat.MEASURED_STATE_MASK;
                }
                i2++;
            }
            return new SimplePalette(iArr);
        }
        if (z) {
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append("discrete colors: ");
            stringBuffer2.append(size);
            Debug.debug(stringBuffer2.toString());
        }
        ArrayList arrayList2 = new ArrayList();
        ColorGroup colorGroup = new ColorGroup(new ArrayList(groupColors.values()));
        arrayList2.add(colorGroup);
        C15751 r9 = new Comparator() {
            public int compare(Object obj, Object obj2) {
                ColorGroup colorGroup = (ColorGroup) obj;
                ColorGroup colorGroup2 = (ColorGroup) obj2;
                if (colorGroup.max_diff == colorGroup2.max_diff) {
                    return colorGroup2.diff_total - colorGroup.diff_total;
                }
                return colorGroup2.max_diff - colorGroup.max_diff;
            }
        };
        while (arrayList2.size() < i) {
            Collections.sort(arrayList2, r9);
            ColorGroup colorGroup2 = (ColorGroup) arrayList2.get(0);
            if (colorGroup2.max_diff == 0) {
                break;
            } else if (!this.ignoreAlpha && colorGroup2.alpha_diff > colorGroup2.red_diff && colorGroup2.alpha_diff > colorGroup2.green_diff && colorGroup2.alpha_diff > colorGroup2.blue_diff) {
                doCut(colorGroup2, 0, arrayList2);
            } else if (colorGroup2.red_diff > colorGroup2.green_diff && colorGroup2.red_diff > colorGroup2.blue_diff) {
                doCut(colorGroup2, 1, arrayList2);
            } else if (colorGroup2.green_diff > colorGroup2.blue_diff) {
                doCut(colorGroup2, 2, arrayList2);
            } else {
                doCut(colorGroup2, 3, arrayList2);
            }
        }
        int size2 = arrayList2.size();
        if (z) {
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("palette size: ");
            stringBuffer3.append(size2);
            Debug.debug(stringBuffer3.toString());
        }
        int[] iArr2 = new int[size2];
        while (i2 < arrayList2.size()) {
            ColorGroup colorGroup3 = (ColorGroup) arrayList2.get(i2);
            iArr2[i2] = colorGroup3.getMedianValue();
            colorGroup3.palette_index = i2;
            if (colorGroup3.color_counts.size() >= 1) {
                i2++;
            } else {
                StringBuffer stringBuffer4 = new StringBuffer();
                stringBuffer4.append("empty color_group: ");
                stringBuffer4.append(colorGroup3);
                throw new ImageWriteException(stringBuffer4.toString());
            }
        }
        if (size2 <= size) {
            return new MedianCutPalette(colorGroup, iArr2);
        }
        throw new ImageWriteException("palette_size>discrete_colors");
    }

    private void doCut(ColorGroup colorGroup, final int i, ArrayList arrayList) throws ImageWriteException {
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < colorGroup.color_counts.size(); i4++) {
            i3 += ((ColorCount) colorGroup.color_counts.get(i4)).count;
        }
        Collections.sort(colorGroup.color_counts, new Comparator() {
            public int compare(Object obj, Object obj2) {
                ColorCount colorCount = (ColorCount) obj;
                ColorCount colorCount2 = (ColorCount) obj2;
                switch (i) {
                    case 0:
                        return colorCount.alpha - colorCount2.alpha;
                    case 1:
                        return colorCount.red - colorCount2.red;
                    case 2:
                        return colorCount.green - colorCount2.green;
                    case 3:
                        return colorCount.blue - colorCount2.blue;
                    default:
                        return 0;
                }
            }
        });
        double d = (double) i3;
        Double.isNaN(d);
        int round = (int) Math.round(d / 2.0d);
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (i5 < colorGroup.color_counts.size()) {
            i6 += ((ColorCount) colorGroup.color_counts.get(i5)).count;
            if (i6 >= round) {
                break;
            }
            i5++;
            i7 = i6;
        }
        if (i5 == colorGroup.color_counts.size() - 1) {
            i5--;
        } else if (i5 > 0 && Math.abs(round - i7) < Math.abs(i6 - round)) {
            i5--;
        }
        arrayList.remove(colorGroup);
        int i8 = i5 + 1;
        ArrayList arrayList2 = new ArrayList(colorGroup.color_counts.subList(0, i8));
        ArrayList arrayList3 = new ArrayList(colorGroup.color_counts.subList(i8, colorGroup.color_counts.size()));
        ColorGroup colorGroup2 = new ColorGroup(new ArrayList(arrayList2));
        arrayList.add(colorGroup2);
        ColorGroup colorGroup3 = new ColorGroup(new ArrayList(arrayList3));
        arrayList.add(colorGroup3);
        ColorCount colorCount = (ColorCount) colorGroup.color_counts.get(i5);
        switch (i) {
            case 0:
                i2 = colorCount.alpha;
                break;
            case 1:
                i2 = colorCount.red;
                break;
            case 2:
                i2 = colorCount.green;
                break;
            case 3:
                i2 = colorCount.blue;
                break;
            default:
                throw new Error("Bad mode.");
        }
        ColorGroupCut colorGroupCut = new ColorGroupCut(colorGroup2, colorGroup3, i, i2);
        colorGroup.cut = colorGroupCut;
    }
}
