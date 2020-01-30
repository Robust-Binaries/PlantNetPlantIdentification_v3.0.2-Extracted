package com.yalantis.ucrop;

import com.yalantis.ucrop.UCropFragment.UCropResult;

public interface UCropFragmentCallback {
    void loadingProgress(boolean z);

    void onCropFinish(UCropResult uCropResult);
}
