package p007io.invertase.firebase;

import android.support.annotation.Keep;
import com.google.firebase.components.Component;
import com.google.firebase.components.ComponentRegistrar;
import com.google.firebase.platforminfo.LibraryVersionComponent;
import java.util.Collections;
import java.util.List;

@Keep
/* renamed from: io.invertase.firebase.ReactNativeFirebaseAppRegistrar */
public class ReactNativeFirebaseAppRegistrar implements ComponentRegistrar {
    public List<Component<?>> getComponents() {
        return Collections.singletonList(LibraryVersionComponent.create("react-native-firebase", "5.3.0"));
    }
}
