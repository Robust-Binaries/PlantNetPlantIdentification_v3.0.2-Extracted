package com.facebook.jni;

import com.facebook.proguard.annotations.DoNotStrip;
import org.apache.sanselan.ImageInfo;

@DoNotStrip
public class UnknownCppException extends CppException {
    @DoNotStrip
    public UnknownCppException() {
        super(ImageInfo.COMPRESSION_ALGORITHM_UNKNOWN);
    }

    @DoNotStrip
    public UnknownCppException(String str) {
        super(str);
    }
}
