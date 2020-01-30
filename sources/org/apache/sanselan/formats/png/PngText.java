package org.apache.sanselan.formats.png;

public abstract class PngText {
    public final String keyword;
    public final String text;

    public static class iTXt extends PngText {
        public final String languageTag;
        public final String translatedKeyword;

        public iTXt(String str, String str2, String str3, String str4) {
            super(str, str2);
            this.languageTag = str3;
            this.translatedKeyword = str4;
        }
    }

    public static class tEXt extends PngText {
        public tEXt(String str, String str2) {
            super(str, str2);
        }
    }

    public static class zTXt extends PngText {
        public zTXt(String str, String str2) {
            super(str, str2);
        }
    }

    public PngText(String str, String str2) {
        this.keyword = str;
        this.text = str2;
    }
}
