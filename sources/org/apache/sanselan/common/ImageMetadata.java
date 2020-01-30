package org.apache.sanselan.common;

import java.util.ArrayList;
import org.apache.sanselan.common.IImageMetadata.IImageMetadataItem;

public class ImageMetadata implements IImageMetadata {
    protected static final String newline = System.getProperty("line.separator");
    private final ArrayList items = new ArrayList();

    public static class Item implements IImageMetadataItem {
        private final String keyword;
        private final String text;

        public Item(String str, String str2) {
            this.keyword = str;
            this.text = str2;
        }

        public String getKeyword() {
            return this.keyword;
        }

        public String getText() {
            return this.text;
        }

        public String toString() {
            return toString(null);
        }

        public String toString(String str) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(this.keyword);
            stringBuffer.append(": ");
            stringBuffer.append(this.text);
            String stringBuffer2 = stringBuffer.toString();
            if (str == null) {
                return stringBuffer2;
            }
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append(str);
            stringBuffer3.append(stringBuffer2);
            return stringBuffer3.toString();
        }
    }

    public void add(String str, String str2) {
        add(new Item(str, str2));
    }

    public void add(IImageMetadataItem iImageMetadataItem) {
        this.items.add(iImageMetadataItem);
    }

    public ArrayList getItems() {
        return new ArrayList(this.items);
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String str) {
        if (str == null) {
            str = "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.items.size(); i++) {
            if (i > 0) {
                stringBuffer.append(newline);
            }
            IImageMetadataItem iImageMetadataItem = (IImageMetadataItem) this.items.get(i);
            StringBuffer stringBuffer2 = new StringBuffer();
            stringBuffer2.append(str);
            stringBuffer2.append("\t");
            stringBuffer.append(iImageMetadataItem.toString(stringBuffer2.toString()));
        }
        return stringBuffer.toString();
    }
}
