package org.apache.sanselan.formats.jpeg;

import java.util.Collections;
import java.util.List;
import org.apache.sanselan.common.ImageMetadata;
import org.apache.sanselan.formats.jpeg.iptc.IPTCConstants;
import org.apache.sanselan.formats.jpeg.iptc.IPTCRecord;
import org.apache.sanselan.formats.jpeg.iptc.PhotoshopApp13Data;
import org.apache.sanselan.util.Debug;

public class JpegPhotoshopMetadata extends ImageMetadata implements IPTCConstants {
    public final PhotoshopApp13Data photoshopApp13Data;

    public JpegPhotoshopMetadata(PhotoshopApp13Data photoshopApp13Data2) {
        this.photoshopApp13Data = photoshopApp13Data2;
        List records = photoshopApp13Data2.getRecords();
        Collections.sort(records, IPTCRecord.COMPARATOR);
        for (int i = 0; i < records.size(); i++) {
            IPTCRecord iPTCRecord = (IPTCRecord) records.get(i);
            if (iPTCRecord.iptcType.type != IPTC_TYPE_RECORD_VERSION.type) {
                add(iPTCRecord.getIptcTypeName(), iPTCRecord.getValue());
            }
        }
    }

    public void dump() {
        Debug.debug(toString());
    }
}
