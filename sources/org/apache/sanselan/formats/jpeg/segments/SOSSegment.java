package org.apache.sanselan.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.util.Debug;

public class SOSSegment extends Segment {
    public SOSSegment(int i, int i2, InputStream inputStream) throws ImageReadException, IOException {
        super(i, i2);
        if (getDebug()) {
            PrintStream printStream = System.out;
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("SOSSegment marker_length: ");
            stringBuffer.append(i2);
            printStream.println(stringBuffer.toString());
        }
        Debug.debug("SOS", i2);
        byte readByte = readByte("number_of_components_in_scan", inputStream, "Not a Valid JPEG File");
        Debug.debug("number_of_components_in_scan", (int) readByte);
        for (int i3 = 0; i3 < readByte; i3++) {
            Debug.debug("scan_component_selector", (int) readByte("scan_component_selector", inputStream, "Not a Valid JPEG File"));
            Debug.debug("ac_dc_entrooy_coding_table_selector", (int) readByte("ac_dc_entrooy_coding_table_selector", inputStream, "Not a Valid JPEG File"));
        }
        Debug.debug("start_of_spectral_selection", (int) readByte("start_of_spectral_selection", inputStream, "Not a Valid JPEG File"));
        Debug.debug("end_of_spectral_selection", (int) readByte("end_of_spectral_selection", inputStream, "Not a Valid JPEG File"));
        Debug.debug("successive_approximation_bit_position", (int) readByte("successive_approximation_bit_position", inputStream, "Not a Valid JPEG File"));
        if (getDebug()) {
            System.out.println("");
        }
    }

    public String getDescription() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SOS (");
        stringBuffer.append(getSegmentType());
        stringBuffer.append(")");
        return stringBuffer.toString();
    }
}
