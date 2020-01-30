package org.apache.sanselan.formats.jpeg.iptc;

import java.util.ArrayList;
import java.util.List;

public class PhotoshopApp13Data implements IPTCConstants {
    private final List rawBlocks;
    private final List records;

    public PhotoshopApp13Data(List list, List list2) {
        this.rawBlocks = list2;
        this.records = list;
    }

    public List getRecords() {
        return new ArrayList(this.records);
    }

    public List getRawBlocks() {
        return new ArrayList(this.rawBlocks);
    }

    public List getNonIptcBlocks() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.rawBlocks.size(); i++) {
            IPTCBlock iPTCBlock = (IPTCBlock) this.rawBlocks.get(i);
            if (!iPTCBlock.isIPTCBlock()) {
                arrayList.add(iPTCBlock);
            }
        }
        return arrayList;
    }
}
