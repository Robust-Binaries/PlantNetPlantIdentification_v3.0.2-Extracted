package p007io.invertase.firebase.firestore;

import android.os.AsyncTask;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import java.lang.ref.WeakReference;

/* renamed from: io.invertase.firebase.firestore.DocumentSnapshotSerializeAsyncTask */
class DocumentSnapshotSerializeAsyncTask extends AsyncTask<Object, Void, WritableMap> {
    private WeakReference<ReactContext> reactContextWeakReference;
    private WeakReference<RNFirebaseFirestoreDocumentReference> referenceWeakReference;

    /* access modifiers changed from: protected */
    public void onPostExecute(WritableMap writableMap) {
    }

    DocumentSnapshotSerializeAsyncTask(ReactContext reactContext, RNFirebaseFirestoreDocumentReference rNFirebaseFirestoreDocumentReference) {
        this.referenceWeakReference = new WeakReference<>(rNFirebaseFirestoreDocumentReference);
        this.reactContextWeakReference = new WeakReference<>(reactContext);
    }

    /* access modifiers changed from: protected */
    public final WritableMap doInBackground(Object... objArr) {
        try {
            return FirestoreSerialize.snapshotToWritableMap(objArr[0]);
        } catch (RuntimeException e) {
            if (isAvailable().booleanValue()) {
                ((ReactContext) this.reactContextWeakReference.get()).handleException(e);
                return null;
            }
            throw e;
        }
    }

    private Boolean isAvailable() {
        return Boolean.valueOf((this.reactContextWeakReference.get() == null || this.referenceWeakReference.get() == null) ? false : true);
    }
}
