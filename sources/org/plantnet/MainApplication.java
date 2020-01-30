package org.plantnet;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import com.AlexanderZaytsev.RNI18n.RNI18nPackage;
import com.RNFetchBlob.RNFetchBlobPackage;
import com.airbnb.android.react.maps.MapsPackage;
import com.azendoo.reactnativesnackbar.SnackbarPackage;
import com.bebnev.RNUserAgentPackage;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.floristicreactbackwardlibrary.RNBackwardPlantnetPackage;
import com.floristicreactlibrary.RNReadWriteExifPackage;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import com.learnium.RNDeviceInfo.C1168RNDeviceInfo;
import com.oblador.vectoricons.VectorIconsPackage;
import com.opensettings.OpenSettingsPackage;
import com.reactnative.ivpusic.imagepicker.PickerPackage;
import com.reactnativecommunity.asyncstorage.AsyncStoragePackage;
import com.reactnativecommunity.netinfo.NetInfoPackage;
import com.reactnativecommunity.webview.RNCWebViewPackage;
import com.rnfs.RNFSPackage;
import com.swmansion.gesturehandler.react.RNGestureHandlerPackage;
import com.swmansion.reanimated.ReanimatedPackage;
import java.util.Arrays;
import java.util.List;
import org.pgsqlite.SQLitePluginPackage;
import p004br.com.classapp.RNSensitiveInfo.RNSensitiveInfoPackage;
import p006fr.bamlab.rnimageresizer.ImageResizerPackage;
import p007io.amarcruz.photoview.PhotoViewPackage;
import p007io.invertase.firebase.RNFirebasePackage;
import p007io.invertase.firebase.analytics.RNFirebaseAnalyticsPackage;

public class MainApplication extends MultiDexApplication implements ReactApplication {
    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        /* access modifiers changed from: protected */
        public String getJSMainModuleName() {
            return Param.INDEX;
        }

        public boolean getUseDeveloperSupport() {
            return false;
        }

        /* access modifiers changed from: protected */
        public List<ReactPackage> getPackages() {
            return Arrays.asList(new ReactPackage[]{new MainReactPackage(), new ReanimatedPackage(), new C1168RNDeviceInfo(), new RNCWebViewPackage(), new AsyncStoragePackage(), new SQLitePluginPackage(), new RNFirebasePackage(), new RNFirebaseAnalyticsPackage(), new NetInfoPackage(), new RNUserAgentPackage(), new MapsPackage(), new RNSensitiveInfoPackage(), new OpenSettingsPackage(), new PhotoViewPackage(), new PickerPackage(), new ImageResizerPackage(), new RNFetchBlobPackage(), new SnackbarPackage(), new RNBackwardPlantnetPackage(), new RNReadWriteExifPackage(), new RNFSPackage(), new VectorIconsPackage(), new RNI18nPackage(), new RNGestureHandlerPackage()});
        }
    };

    public ReactNativeHost getReactNativeHost() {
        return this.mReactNativeHost;
    }

    public void onCreate() {
        super.onCreate();
        SoLoader.init((Context) this, false);
    }
}
