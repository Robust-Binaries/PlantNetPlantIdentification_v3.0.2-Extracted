package p007io.invertase.firebase.firestore;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;

/* renamed from: io.invertase.firebase.firestore.RNFirebaseFirestoreTransactionHandler */
class RNFirebaseFirestoreTransactionHandler {
    boolean aborted = false;
    private String appName;
    private ReadableArray commandBuffer;
    private final Condition condition;
    private Transaction firestoreTransaction;
    private final ReentrantLock lock;
    boolean timeout = false;
    private long timeoutAt;
    private int transactionId;

    RNFirebaseFirestoreTransactionHandler(String str, int i) {
        this.appName = str;
        this.transactionId = i;
        updateInternalTimeout();
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
    }

    /* access modifiers changed from: 0000 */
    public void abort() {
        this.aborted = true;
        safeUnlock();
    }

    /* access modifiers changed from: 0000 */
    public void resetState(Transaction transaction) {
        this.commandBuffer = null;
        this.firestoreTransaction = transaction;
    }

    /* access modifiers changed from: 0000 */
    public void signalBufferReceived(ReadableArray readableArray) {
        this.lock.lock();
        try {
            this.commandBuffer = readableArray;
            this.condition.signalAll();
        } finally {
            safeUnlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public void await() {
        this.lock.lock();
        updateInternalTimeout();
        while (!this.aborted && !this.timeout && !this.condition.await(10, TimeUnit.MILLISECONDS)) {
            try {
                if (System.currentTimeMillis() > this.timeoutAt) {
                    this.timeout = true;
                }
            } catch (InterruptedException unused) {
            } catch (Throwable th) {
                safeUnlock();
                throw th;
            }
        }
        safeUnlock();
    }

    /* access modifiers changed from: 0000 */
    public ReadableArray getCommandBuffer() {
        return this.commandBuffer;
    }

    /* access modifiers changed from: 0000 */
    public void getDocument(DocumentReference documentReference, Promise promise) {
        updateInternalTimeout();
        try {
            promise.resolve(FirestoreSerialize.snapshotToWritableMap(this.firestoreTransaction.get(documentReference)));
        } catch (FirebaseFirestoreException e) {
            WritableMap jSError = RNFirebaseFirestore.getJSError(e);
            promise.reject(jSError.getString("code"), jSError.getString("message"));
        }
    }

    /* access modifiers changed from: 0000 */
    public WritableMap createEventMap(@Nullable FirebaseFirestoreException firebaseFirestoreException, String str) {
        WritableMap createMap = Arguments.createMap();
        createMap.putInt("id", this.transactionId);
        createMap.putString("appName", this.appName);
        if (firebaseFirestoreException != null) {
            createMap.putString("type", "error");
            createMap.putMap("error", RNFirebaseFirestore.getJSError(firebaseFirestoreException));
        } else {
            createMap.putString("type", str);
        }
        return createMap;
    }

    private void safeUnlock() {
        if (this.lock.isLocked()) {
            this.lock.unlock();
        }
    }

    private void updateInternalTimeout() {
        this.timeoutAt = System.currentTimeMillis() + 15000;
    }
}
