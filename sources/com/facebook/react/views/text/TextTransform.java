package com.facebook.react.views.text;

import java.text.BreakIterator;

public enum TextTransform {
    NONE,
    UPPERCASE,
    LOWERCASE,
    CAPITALIZE,
    UNSET;

    public static String apply(String str, TextTransform textTransform) {
        if (str == null) {
            return null;
        }
        switch (textTransform) {
            case UPPERCASE:
                str = str.toUpperCase();
                break;
            case LOWERCASE:
                str = str.toLowerCase();
                break;
            case CAPITALIZE:
                str = capitalize(str);
                break;
        }
        return str;
    }

    private static String capitalize(String str) {
        BreakIterator wordInstance = BreakIterator.getWordInstance();
        wordInstance.setText(str);
        StringBuilder sb = new StringBuilder(str.length());
        int first = wordInstance.first();
        while (true) {
            int i = first;
            first = wordInstance.next();
            if (first == -1) {
                return sb.toString();
            }
            String substring = str.substring(i, first);
            if (Character.isLetterOrDigit(substring.charAt(0))) {
                sb.append(Character.toUpperCase(substring.charAt(0)));
                sb.append(substring.substring(1).toLowerCase());
            } else {
                sb.append(substring);
            }
        }
    }
}
