package org.apache.sanselan.color;

import android.support.p000v4.internal.view.SupportMenu;
import android.support.p000v4.view.ViewCompat;
import org.apache.sanselan.util.Debug;

public abstract class ColorConversions {
    private static final double ref_X = 95.047d;
    private static final double ref_Y = 100.0d;
    private static final double ref_Z = 108.883d;

    private static double convertHuetoRGB(double d, double d2, double d3) {
        if (d3 < 0.0d) {
            d3 += 1.0d;
        }
        if (d3 > 1.0d) {
            d3 -= 1.0d;
        }
        return d3 * 6.0d < 1.0d ? d + ((d2 - d) * 6.0d * d3) : d3 * 2.0d < 1.0d ? d2 : 3.0d * d3 < 2.0d ? d + ((d2 - d) * (0.6666666666666666d - d3) * 6.0d) : d;
    }

    private static double cube(double d) {
        return d * d * d;
    }

    public static double degree_2_radian(double d) {
        return (d * 3.141592653589793d) / 180.0d;
    }

    public static double radian_2_degree(double d) {
        return (d * 180.0d) / 3.141592653589793d;
    }

    private static double square(double d) {
        return d * d;
    }

    public static final void main(String[] strArr) {
        for (int i = 0; i <= 256; i += 64) {
            for (int i2 = 0; i2 <= 256; i2 += 64) {
                for (int i3 = 0; i3 <= 256; i3 += 64) {
                    int i4 = 0;
                    while (i4 <= 256) {
                        try {
                            int convertCMYKtoRGB = convertCMYKtoRGB(Math.min(255, i), Math.min(255, i2), Math.min(255, i3), Math.min(255, i4));
                            int convertCMYKtoRGB_old = convertCMYKtoRGB_old(Math.min(255, i), Math.min(255, i2), Math.min(255, i3), Math.min(255, i4));
                            if (convertCMYKtoRGB != convertCMYKtoRGB_old) {
                                Debug.debug();
                                Debug.debug("C", i);
                                Debug.debug("M", i2);
                                Debug.debug("Y", i3);
                                Debug.debug("K", i4);
                                StringBuffer stringBuffer = new StringBuffer();
                                stringBuffer.append(convertCMYKtoRGB);
                                stringBuffer.append(" (");
                                stringBuffer.append(Integer.toHexString(convertCMYKtoRGB));
                                stringBuffer.append(")");
                                Debug.debug("rgb1", stringBuffer.toString());
                                StringBuffer stringBuffer2 = new StringBuffer();
                                stringBuffer2.append(convertCMYKtoRGB_old);
                                stringBuffer2.append(" (");
                                stringBuffer2.append(Integer.toHexString(convertCMYKtoRGB_old));
                                stringBuffer2.append(")");
                                Debug.debug("rgb2", stringBuffer2.toString());
                            }
                            i4 += 64;
                        } catch (Throwable th) {
                            Debug.debug(th);
                            return;
                        }
                    }
                }
            }
        }
        int[] iArr = {-1, ViewCompat.MEASURED_STATE_MASK, SupportMenu.CATEGORY_MASK, -16711936, -16776961, -65281, -983296, -16711681, 0, -8421505};
        for (int i5 : iArr) {
            ColorXYZ convertRGBtoXYZ = convertRGBtoXYZ(i5);
            int convertXYZtoRGB = convertXYZtoRGB(convertRGBtoXYZ);
            Debug.debug();
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(i5);
            stringBuffer3.append(" (");
            stringBuffer3.append(Integer.toHexString(i5));
            stringBuffer3.append(")");
            Debug.debug("rgb", stringBuffer3.toString());
            Debug.debug("xyz", (Object) convertRGBtoXYZ);
            StringBuffer stringBuffer4 = new StringBuffer();
            stringBuffer4.append(convertXYZtoRGB);
            stringBuffer4.append(" (");
            stringBuffer4.append(Integer.toHexString(convertXYZtoRGB));
            stringBuffer4.append(")");
            Debug.debug("xyz_rgb", stringBuffer4.toString());
            int i6 = i5 & ViewCompat.MEASURED_SIZE_MASK;
            if ((convertXYZtoRGB & ViewCompat.MEASURED_SIZE_MASK) != i6) {
                Debug.debug("!!!!!!!!!!!!!!!!!!!!!!!");
            }
            ColorCIELab convertXYZtoCIELab = convertXYZtoCIELab(convertRGBtoXYZ);
            ColorXYZ convertCIELabtoXYZ = convertCIELabtoXYZ(convertXYZtoCIELab);
            int convertXYZtoRGB2 = convertXYZtoRGB(convertCIELabtoXYZ);
            Debug.debug("cielab", (Object) convertXYZtoCIELab);
            Debug.debug("cielab_xyz", (Object) convertCIELabtoXYZ);
            StringBuffer stringBuffer5 = new StringBuffer();
            stringBuffer5.append(convertXYZtoRGB2);
            stringBuffer5.append(" (");
            stringBuffer5.append(Integer.toHexString(convertXYZtoRGB2));
            stringBuffer5.append(")");
            Debug.debug("cielab_xyz_rgb", stringBuffer5.toString());
            if ((convertXYZtoRGB2 & ViewCompat.MEASURED_SIZE_MASK) != i6) {
                Debug.debug("!!!!!!!!!!!!!!!!!!!!!!!");
            }
            ColorHunterLab convertXYZtoHunterLab = convertXYZtoHunterLab(convertRGBtoXYZ);
            ColorXYZ convertHunterLabtoXYZ = convertHunterLabtoXYZ(convertXYZtoHunterLab);
            int convertXYZtoRGB3 = convertXYZtoRGB(convertHunterLabtoXYZ);
            Debug.debug("hunterlab", (Object) convertXYZtoHunterLab);
            Debug.debug("hunterlab_xyz", (Object) convertHunterLabtoXYZ);
            StringBuffer stringBuffer6 = new StringBuffer();
            stringBuffer6.append(convertXYZtoRGB3);
            stringBuffer6.append(" (");
            stringBuffer6.append(Integer.toHexString(convertXYZtoRGB3));
            stringBuffer6.append(")");
            Debug.debug("hunterlab_xyz_rgb", stringBuffer6.toString());
            if ((convertXYZtoRGB3 & ViewCompat.MEASURED_SIZE_MASK) != i6) {
                Debug.debug("!!!!!!!!!!!!!!!!!!!!!!!");
            }
            ColorCMY convertRGBtoCMY = convertRGBtoCMY(i5);
            ColorCMYK convertCMYtoCMYK = convertCMYtoCMYK(convertRGBtoCMY);
            ColorCMY convertCMYKtoCMY = convertCMYKtoCMY(convertCMYtoCMYK);
            int convertCMYtoRGB = convertCMYtoRGB(convertCMYKtoCMY);
            Debug.debug("cmy", (Object) convertRGBtoCMY);
            Debug.debug("cmyk", (Object) convertCMYtoCMYK);
            Debug.debug("cmyk_cmy", (Object) convertCMYKtoCMY);
            StringBuffer stringBuffer7 = new StringBuffer();
            stringBuffer7.append(convertCMYtoRGB);
            stringBuffer7.append(" (");
            stringBuffer7.append(Integer.toHexString(convertCMYtoRGB));
            stringBuffer7.append(")");
            Debug.debug("cmyk_cmy_rgb", stringBuffer7.toString());
            if ((convertCMYtoRGB & ViewCompat.MEASURED_SIZE_MASK) != i6) {
                Debug.debug("!!!!!!!!!!!!!!!!!!!!!!!");
            }
            ColorHSL convertRGBtoHSL = convertRGBtoHSL(i5);
            int convertHSLtoRGB = convertHSLtoRGB(convertRGBtoHSL);
            Debug.debug("hsl", (Object) convertRGBtoHSL);
            StringBuffer stringBuffer8 = new StringBuffer();
            stringBuffer8.append(convertHSLtoRGB);
            stringBuffer8.append(" (");
            stringBuffer8.append(Integer.toHexString(convertHSLtoRGB));
            stringBuffer8.append(")");
            Debug.debug("hsl_rgb", stringBuffer8.toString());
            if ((convertHSLtoRGB & ViewCompat.MEASURED_SIZE_MASK) != i6) {
                Debug.debug("!!!!!!!!!!!!!!!!!!!!!!!");
            }
            ColorHSV convertRGBtoHSV = convertRGBtoHSV(i5);
            int convertHSVtoRGB = convertHSVtoRGB(convertRGBtoHSV);
            Debug.debug("hsv", (Object) convertRGBtoHSV);
            StringBuffer stringBuffer9 = new StringBuffer();
            stringBuffer9.append(convertHSVtoRGB);
            stringBuffer9.append(" (");
            stringBuffer9.append(Integer.toHexString(convertHSVtoRGB));
            stringBuffer9.append(")");
            Debug.debug("hsv_rgb", stringBuffer9.toString());
            if ((convertHSVtoRGB & ViewCompat.MEASURED_SIZE_MASK) != i6) {
                Debug.debug("!!!!!!!!!!!!!!!!!!!!!!!");
            }
            ColorCIELCH convertCIELabtoCIELCH = convertCIELabtoCIELCH(convertXYZtoCIELab);
            ColorCIELab convertCIELCHtoCIELab = convertCIELCHtoCIELab(convertCIELabtoCIELCH);
            Debug.debug("cielch", (Object) convertCIELabtoCIELCH);
            Debug.debug("cielch_cielab", (Object) convertCIELCHtoCIELab);
            ColorCIELuv convertXYZtoCIELuv = convertXYZtoCIELuv(convertRGBtoXYZ);
            ColorXYZ convertCIELuvtoXYZ = convertCIELuvtoXYZ(convertXYZtoCIELuv);
            Debug.debug("cieluv", (Object) convertXYZtoCIELuv);
            Debug.debug("cieluv_xyz", (Object) convertCIELuvtoXYZ);
        }
    }

    public static final ColorCIELab convertXYZtoCIELab(ColorXYZ colorXYZ) {
        return convertXYZtoCIELab(colorXYZ.f154X, colorXYZ.f155Y, colorXYZ.f156Z);
    }

    public static final ColorCIELab convertXYZtoCIELab(double d, double d2, double d3) {
        double d4 = d / ref_X;
        double d5 = d2 / ref_Y;
        double d6 = d3 / ref_Z;
        double pow = d4 > 0.008856d ? Math.pow(d4, 0.3333333333333333d) : (d4 * 7.787d) + 0.13793103448275862d;
        double pow2 = d5 > 0.008856d ? Math.pow(d5, 0.3333333333333333d) : (d5 * 7.787d) + 0.13793103448275862d;
        ColorCIELab colorCIELab = new ColorCIELab((116.0d * pow2) - 16.0d, (pow - pow2) * 500.0d, (pow2 - (d6 > 0.008856d ? Math.pow(d6, 0.3333333333333333d) : (d6 * 7.787d) + 0.13793103448275862d)) * 200.0d);
        return colorCIELab;
    }

    public static final ColorXYZ convertCIELabtoXYZ(ColorCIELab colorCIELab) {
        return convertCIELabtoXYZ(colorCIELab.f132L, colorCIELab.f133a, colorCIELab.f134b);
    }

    public static final ColorXYZ convertCIELabtoXYZ(double d, double d2, double d3) {
        double d4 = (d + 16.0d) / 116.0d;
        double d5 = (d2 / 500.0d) + d4;
        double d6 = d4 - (d3 / 200.0d);
        ColorXYZ colorXYZ = new ColorXYZ((Math.pow(d5, 3.0d) > 0.008856d ? Math.pow(d5, 3.0d) : (d5 - 0.13793103448275862d) / 7.787d) * ref_X, (Math.pow(d4, 3.0d) > 0.008856d ? Math.pow(d4, 3.0d) : (d4 - 0.13793103448275862d) / 7.787d) * ref_Y, (Math.pow(d6, 3.0d) > 0.008856d ? Math.pow(d6, 3.0d) : (d6 - 0.13793103448275862d) / 7.787d) * ref_Z);
        return colorXYZ;
    }

    public static final ColorHunterLab convertXYZtoHunterLab(ColorXYZ colorXYZ) {
        return convertXYZtoHunterLab(colorXYZ.f154X, colorXYZ.f155Y, colorXYZ.f156Z);
    }

    public static final ColorHunterLab convertXYZtoHunterLab(double d, double d2, double d3) {
        ColorHunterLab colorHunterLab = new ColorHunterLab(Math.sqrt(d2) * 10.0d, (((1.02d * d) - d2) / Math.sqrt(d2)) * 17.5d, ((d2 - (0.847d * d3)) / Math.sqrt(d2)) * 7.0d);
        return colorHunterLab;
    }

    public static final ColorXYZ convertHunterLabtoXYZ(ColorHunterLab colorHunterLab) {
        return convertHunterLabtoXYZ(colorHunterLab.f151L, colorHunterLab.f152a, colorHunterLab.f153b);
    }

    public static final ColorXYZ convertHunterLabtoXYZ(double d, double d2, double d3) {
        double d4 = ((d2 / 17.5d) * d) / 10.0d;
        double d5 = ((d3 / 7.0d) * d) / 10.0d;
        double pow = Math.pow(d / 10.0d, 2.0d);
        ColorXYZ colorXYZ = new ColorXYZ((d4 + pow) / 1.02d, pow, (-(d5 - pow)) / 0.847d);
        return colorXYZ;
    }

    public static final int convertXYZtoRGB(ColorXYZ colorXYZ) {
        return convertXYZtoRGB(colorXYZ.f154X, colorXYZ.f155Y, colorXYZ.f156Z);
    }

    public static final int convertXYZtoRGB(double d, double d2, double d3) {
        double d4 = d / ref_Y;
        double d5 = d2 / ref_Y;
        double d6 = d3 / ref_Y;
        double d7 = (3.2406d * d4) + (-1.5372d * d5) + (-0.4986d * d6);
        double d8 = (-0.9689d * d4) + (1.8758d * d5) + (0.0415d * d6);
        double d9 = (d4 * 0.0557d) + (d5 * -0.204d) + (d6 * 1.057d);
        return convertRGBtoRGB((d7 > 0.0031308d ? (Math.pow(d7, 0.4166666666666667d) * 1.055d) - 0.055d : d7 * 12.92d) * 255.0d, (d8 > 0.0031308d ? (Math.pow(d8, 0.4166666666666667d) * 1.055d) - 0.055d : d8 * 12.92d) * 255.0d, 255.0d * (d9 > 0.0031308d ? (Math.pow(d9, 0.4166666666666667d) * 1.055d) - 0.055d : d9 * 12.92d));
    }

    public static final ColorXYZ convertRGBtoXYZ(int i) {
        double d;
        int i2 = (i >> 8) & 255;
        int i3 = (i >> 0) & 255;
        double d2 = (double) ((i >> 16) & 255);
        Double.isNaN(d2);
        double d3 = d2 / 255.0d;
        double d4 = (double) i2;
        Double.isNaN(d4);
        double d5 = d4 / 255.0d;
        double d6 = (double) i3;
        Double.isNaN(d6);
        double d7 = d6 / 255.0d;
        double pow = d3 > 0.04045d ? Math.pow((d3 + 0.055d) / 1.055d, 2.4d) : d3 / 12.92d;
        double pow2 = d5 > 0.04045d ? Math.pow((d5 + 0.055d) / 1.055d, 2.4d) : d5 / 12.92d;
        if (d7 > 0.04045d) {
            d = Math.pow((d7 + 0.055d) / 1.055d, 2.4d);
        } else {
            d = d7 / 12.92d;
        }
        double d8 = pow * ref_Y;
        double d9 = pow2 * ref_Y;
        double d10 = d * ref_Y;
        ColorXYZ colorXYZ = new ColorXYZ((0.4124d * d8) + (0.3576d * d9) + (0.1805d * d10), (0.2126d * d8) + (0.7152d * d9) + (0.0722d * d10), (d8 * 0.0193d) + (d9 * 0.1192d) + (d10 * 0.9505d));
        return colorXYZ;
    }

    public static final ColorCMY convertRGBtoCMY(int i) {
        int i2 = (i >> 8) & 255;
        int i3 = (i >> 0) & 255;
        double d = (double) ((i >> 16) & 255);
        Double.isNaN(d);
        double d2 = 1.0d - (d / 255.0d);
        double d3 = (double) i2;
        Double.isNaN(d3);
        double d4 = 1.0d - (d3 / 255.0d);
        double d5 = (double) i3;
        Double.isNaN(d5);
        ColorCMY colorCMY = new ColorCMY(d2, d4, 1.0d - (d5 / 255.0d));
        return colorCMY;
    }

    public static final int convertCMYtoRGB(ColorCMY colorCMY) {
        return convertRGBtoRGB((1.0d - colorCMY.f138C) * 255.0d, (1.0d - colorCMY.f139M) * 255.0d, (1.0d - colorCMY.f140Y) * 255.0d);
    }

    public static final ColorCMYK convertCMYtoCMYK(ColorCMY colorCMY) {
        double d;
        double d2;
        double d3;
        ColorCMY colorCMY2 = colorCMY;
        double d4 = colorCMY2.f138C;
        double d5 = colorCMY2.f139M;
        double d6 = colorCMY2.f140Y;
        double d7 = d4 < 1.0d ? d4 : 1.0d;
        if (d5 < d7) {
            d7 = d5;
        }
        double d8 = d6 < d7 ? d6 : d7;
        if (d8 == 1.0d) {
            d3 = 0.0d;
            d2 = 0.0d;
            d = 0.0d;
        } else {
            double d9 = 1.0d - d8;
            d3 = (d4 - d8) / d9;
            d2 = (d5 - d8) / d9;
            d = (d6 - d8) / d9;
        }
        ColorCMYK colorCMYK = new ColorCMYK(d3, d2, d, d8);
        return colorCMYK;
    }

    public static final ColorCMY convertCMYKtoCMY(ColorCMYK colorCMYK) {
        return convertCMYKtoCMY(colorCMYK.f141C, colorCMYK.f143M, colorCMYK.f144Y, colorCMYK.f142K);
    }

    public static final ColorCMY convertCMYKtoCMY(double d, double d2, double d3, double d4) {
        double d5 = 1.0d - d4;
        ColorCMY colorCMY = new ColorCMY((d * d5) + d4, (d2 * d5) + d4, (d5 * d3) + d4);
        return colorCMY;
    }

    public static final int convertCMYKtoRGB(int i, int i2, int i3, int i4) {
        double d = (double) i;
        Double.isNaN(d);
        double d2 = d / 255.0d;
        double d3 = (double) i2;
        Double.isNaN(d3);
        double d4 = d3 / 255.0d;
        double d5 = (double) i3;
        Double.isNaN(d5);
        double d6 = d5 / 255.0d;
        double d7 = (double) i4;
        Double.isNaN(d7);
        return convertCMYtoRGB(convertCMYKtoCMY(d2, d4, d6, d7 / 255.0d));
    }

    public static final ColorHSL convertRGBtoHSL(int i) {
        double d;
        double d2;
        double d3;
        int i2 = (i >> 8) & 255;
        int i3 = (i >> 0) & 255;
        double d4 = (double) ((i >> 16) & 255);
        Double.isNaN(d4);
        double d5 = d4 / 255.0d;
        double d6 = (double) i2;
        Double.isNaN(d6);
        double d7 = d6 / 255.0d;
        double d8 = (double) i3;
        Double.isNaN(d8);
        double d9 = d8 / 255.0d;
        double min = Math.min(d5, Math.min(d7, d9));
        double max = Math.max(d5, Math.max(d7, d9));
        double d10 = max - min;
        double d11 = max + min;
        double d12 = d11 / 2.0d;
        if (d10 == 0.0d) {
            d = 0.0d;
            d2 = 0.0d;
        } else {
            double d13 = d12 < 0.5d ? d10 / d11 : d10 / ((2.0d - max) - min);
            double d14 = d10 / 2.0d;
            double d15 = (((max - d5) / 6.0d) + d14) / d10;
            double d16 = (((max - d7) / 6.0d) + d14) / d10;
            double d17 = (((max - d9) / 6.0d) + d14) / d10;
            if (d5 == max) {
                d3 = d17 - d16;
            } else if (d7 == max) {
                d3 = (d15 + 0.3333333333333333d) - d17;
            } else if (d9 == max) {
                d3 = (d16 + 0.6666666666666666d) - d15;
            } else {
                Debug.debug("uh oh");
                d3 = 0.0d;
            }
            if (d3 < 0.0d) {
                d3 += 1.0d;
            }
            if (d3 > 1.0d) {
                d2 = d3 - 1.0d;
                d = d13;
            } else {
                d2 = d3;
                d = d13;
            }
        }
        ColorHSL colorHSL = new ColorHSL(d2, d, d12);
        return colorHSL;
    }

    public static int convertHSLtoRGB(ColorHSL colorHSL) {
        return convertHSLtoRGB(colorHSL.f145H, colorHSL.f147S, colorHSL.f146L);
    }

    public static int convertHSLtoRGB(double d, double d2, double d3) {
        double d4;
        double d5;
        double d6;
        if (d2 == 0.0d) {
            d6 = d3 * 255.0d;
            d5 = d6;
            d4 = d5;
        } else {
            double d7 = d3 < 0.5d ? (d2 + 1.0d) * d3 : (d3 + d2) - (d2 * d3);
            double d8 = (2.0d * d3) - d7;
            d4 = convertHuetoRGB(d8, d7, d - 0.3333333333333333d) * 255.0d;
            d5 = convertHuetoRGB(d8, d7, d) * 255.0d;
            d6 = convertHuetoRGB(d8, d7, d + 0.3333333333333333d) * 255.0d;
        }
        return convertRGBtoRGB(d6, d5, d4);
    }

    public static final ColorHSV convertRGBtoHSV(int i) {
        double d;
        double d2;
        double d3;
        int i2 = (i >> 8) & 255;
        int i3 = (i >> 0) & 255;
        double d4 = (double) ((i >> 16) & 255);
        Double.isNaN(d4);
        double d5 = d4 / 255.0d;
        double d6 = (double) i2;
        Double.isNaN(d6);
        double d7 = d6 / 255.0d;
        double d8 = (double) i3;
        Double.isNaN(d8);
        double d9 = d8 / 255.0d;
        double min = Math.min(d5, Math.min(d7, d9));
        double max = Math.max(d5, Math.max(d7, d9));
        double d10 = max - min;
        if (d10 == 0.0d) {
            d2 = 0.0d;
            d = 0.0d;
        } else {
            double d11 = d10 / max;
            double d12 = d10 / 2.0d;
            double d13 = (((max - d5) / 6.0d) + d12) / d10;
            double d14 = (((max - d7) / 6.0d) + d12) / d10;
            double d15 = (((max - d9) / 6.0d) + d12) / d10;
            if (d5 == max) {
                d3 = d15 - d14;
            } else if (d7 == max) {
                d3 = (d13 + 0.3333333333333333d) - d15;
            } else if (d9 == max) {
                d3 = (d14 + 0.6666666666666666d) - d13;
            } else {
                Debug.debug("uh oh");
                d3 = 0.0d;
            }
            if (d3 < 0.0d) {
                d3 += 1.0d;
            }
            if (d3 > 1.0d) {
                d = d11;
                d2 = d3 - 1.0d;
            } else {
                d = d11;
                d2 = d3;
            }
        }
        ColorHSV colorHSV = new ColorHSV(d2, d, max);
        return colorHSV;
    }

    public static int convertHSVtoRGB(ColorHSV colorHSV) {
        return convertHSVtoRGB(colorHSV.f148H, colorHSV.f149S, colorHSV.f150V);
    }

    public static int convertHSVtoRGB(double d, double d2, double d3) {
        double d4;
        double d5;
        double d6;
        if (d2 == 0.0d) {
            d6 = d3 * 255.0d;
            d5 = d6;
            d4 = d5;
        } else {
            double d7 = d * 6.0d;
            if (d7 == 6.0d) {
                d7 = 0.0d;
            }
            double floor = Math.floor(d7);
            double d8 = (1.0d - d2) * d3;
            double d9 = d7 - floor;
            double d10 = (1.0d - (d2 * d9)) * d3;
            double d11 = (1.0d - ((1.0d - d9) * d2)) * d3;
            if (floor == 0.0d) {
                d10 = d11;
                d11 = d8;
                d8 = d3;
            } else if (floor == 1.0d) {
                d11 = d8;
                d8 = d10;
                d10 = d3;
            } else if (floor == 2.0d) {
                d10 = d3;
            } else if (floor == 3.0d) {
                d11 = d3;
            } else if (floor == 4.0d) {
                d10 = d8;
                d8 = d11;
                d11 = d3;
            } else {
                d11 = d10;
                d10 = d8;
                d8 = d3;
            }
            d6 = d8 * 255.0d;
            d4 = d10 * 255.0d;
            d5 = 255.0d * d11;
        }
        return convertRGBtoRGB(d6, d4, d5);
    }

    public static final int convertCMYKtoRGB_old(int i, int i2, int i3, int i4) {
        return convertRGBtoRGB(255 - (i + i4), 255 - (i2 + i4), 255 - (i3 + i4));
    }

    public static final int convertCIELabtoARGBTest(int i, int i2, int i3) {
        double d = (double) i;
        Double.isNaN(d);
        double d2 = (((d * ref_Y) / 255.0d) + 16.0d) / 116.0d;
        double d3 = (double) i2;
        Double.isNaN(d3);
        double d4 = (d3 / 500.0d) + d2;
        double d5 = (double) i3;
        Double.isNaN(d5);
        double d6 = d2 - (d5 / 200.0d);
        double cube = cube(d4);
        double cube2 = cube(d2);
        double cube3 = cube(d6);
        if (cube2 <= 0.008856d) {
            cube2 = (d2 - 0.13793103448275862d) / 7.787d;
        }
        if (cube <= 0.008856d) {
            cube = (d4 - 0.13793103448275862d) / 7.787d;
        }
        if (cube3 <= 0.008856d) {
            cube3 = (d6 - 0.13793103448275862d) / 7.787d;
        }
        double d7 = (cube * ref_X) / ref_Y;
        double d8 = (cube2 * ref_Y) / ref_Y;
        double d9 = (cube3 * ref_Z) / ref_Y;
        double d10 = (3.2406d * d7) + (-1.5372d * d8) + (-0.4986d * d9);
        double d11 = (-0.9689d * d7) + (1.8758d * d8) + (0.0415d * d9);
        double d12 = (d7 * 0.0557d) + (d8 * -0.204d) + (d9 * 1.057d);
        return convertRGBtoRGB((d10 > 0.0031308d ? (Math.pow(d10, 0.4166666666666667d) * 1.055d) - 0.055d : d10 * 12.92d) * 255.0d, (d11 > 0.0031308d ? (Math.pow(d11, 0.4166666666666667d) * 1.055d) - 0.055d : d11 * 12.92d) * 255.0d, (d12 > 0.0031308d ? (Math.pow(d12, 0.4166666666666667d) * 1.055d) - 0.055d : d12 * 12.92d) * 255.0d);
    }

    private static final int convertRGBtoRGB(double d, double d2, double d3) {
        int round = (int) Math.round(d2);
        int round2 = (int) Math.round(d3);
        return (Math.min(255, Math.max(0, (int) Math.round(d))) << 16) | ViewCompat.MEASURED_STATE_MASK | (Math.min(255, Math.max(0, round)) << 8) | (Math.min(255, Math.max(0, round2)) << 0);
    }

    private static final int convertRGBtoRGB(int i, int i2, int i3) {
        return (Math.min(255, Math.max(0, i)) << 16) | ViewCompat.MEASURED_STATE_MASK | (Math.min(255, Math.max(0, i2)) << 8) | (Math.min(255, Math.max(0, i3)) << 0);
    }

    public static ColorCIELCH convertCIELabtoCIELCH(ColorCIELab colorCIELab) {
        return convertCIELabtoCIELCH(colorCIELab.f132L, colorCIELab.f133a, colorCIELab.f134b);
    }

    public static ColorCIELCH convertCIELabtoCIELCH(double d, double d2, double d3) {
        double d4;
        double atan2 = Math.atan2(d3, d2);
        if (atan2 > 0.0d) {
            d4 = (atan2 / 3.141592653589793d) * 180.0d;
        } else {
            d4 = 360.0d - radian_2_degree(Math.abs(atan2));
        }
        ColorCIELCH colorCIELCH = new ColorCIELCH(d, Math.sqrt(square(d2) + square(d3)), d4);
        return colorCIELCH;
    }

    public static ColorCIELab convertCIELCHtoCIELab(ColorCIELCH colorCIELCH) {
        return convertCIELCHtoCIELab(colorCIELCH.f131L, colorCIELCH.f129C, colorCIELCH.f130H);
    }

    public static ColorCIELab convertCIELCHtoCIELab(double d, double d2, double d3) {
        ColorCIELab colorCIELab = new ColorCIELab(d, Math.cos(degree_2_radian(d3)) * d2, Math.sin(degree_2_radian(d3)) * d2);
        return colorCIELab;
    }

    public static ColorCIELuv convertXYZtoCIELuv(ColorXYZ colorXYZ) {
        return convertXYZtoCIELuv(colorXYZ.f154X, colorXYZ.f155Y, colorXYZ.f156Z);
    }

    public static ColorCIELuv convertXYZtoCIELuv(double d, double d2, double d3) {
        double d4;
        double d5 = d + (15.0d * d2) + (3.0d * d3);
        double d6 = (4.0d * d) / d5;
        double d7 = (9.0d * d2) / d5;
        double d8 = d2 / ref_Y;
        if (d8 > 0.008856d) {
            d4 = Math.pow(d8, 0.3333333333333333d);
        } else {
            d4 = (d8 * 7.787d) + 0.13793103448275862d;
        }
        double d9 = (d4 * 116.0d) - 16.0d;
        double d10 = 13.0d * d9;
        ColorCIELuv colorCIELuv = new ColorCIELuv(d9, d10 * (d6 - 0.19783982482140777d), d10 * (d7 - 0.46833630293240974d));
        return colorCIELuv;
    }

    public static ColorXYZ convertCIELuvtoXYZ(ColorCIELuv colorCIELuv) {
        return convertCIELuvtoXYZ(colorCIELuv.f135L, colorCIELuv.f136u, colorCIELuv.f137v);
    }

    public static ColorXYZ convertCIELuvtoXYZ(double d, double d2, double d3) {
        double d4;
        double d5 = (d + 16.0d) / 116.0d;
        if (Math.pow(d5, 3.0d) > 0.008856d) {
            d4 = Math.pow(d5, 3.0d);
        } else {
            d4 = (d5 - 0.0d) / 7.787d;
        }
        double d6 = 13.0d * d;
        double d7 = (d2 / d6) + 0.19783982482140777d;
        double d8 = (d3 / d6) + 0.46833630293240974d;
        double d9 = d4 * ref_Y;
        double d10 = 9.0d * d9;
        double d11 = (-(d10 * d7)) / (((d7 - 4.0d) * d8) - (d7 * d8));
        ColorXYZ colorXYZ = new ColorXYZ(d11, d9, ((d10 - ((15.0d * d8) * d9)) - (d8 * d11)) / (d8 * 3.0d));
        return colorXYZ;
    }
}
