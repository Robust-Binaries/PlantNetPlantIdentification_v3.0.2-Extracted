package android.support.p000v4.media;

import android.media.VolumeProvider;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
/* renamed from: android.support.v4.media.VolumeProviderCompatApi21 */
class VolumeProviderCompatApi21 {

    /* renamed from: android.support.v4.media.VolumeProviderCompatApi21$Delegate */
    public interface Delegate {
        void onAdjustVolume(int i);

        void onSetVolumeTo(int i);
    }

    public static Object createVolumeProvider(int i, int i2, int i3, final Delegate delegate) {
        return new VolumeProvider(i, i2, i3) {
            public void onSetVolumeTo(int i) {
                delegate.onSetVolumeTo(i);
            }

            public void onAdjustVolume(int i) {
                delegate.onAdjustVolume(i);
            }
        };
    }

    public static void setCurrentVolume(Object obj, int i) {
        ((VolumeProvider) obj).setCurrentVolume(i);
    }

    private VolumeProviderCompatApi21() {
    }
}
