package com.google.android.gms.iid;

import android.util.Base64;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.util.VisibleForTesting;
import java.security.KeyPair;

final class zzo {
    private final KeyPair zzcb;
    /* access modifiers changed from: private */
    public final long zzcc;

    @VisibleForTesting
    zzo(KeyPair keyPair, long j) {
        this.zzcb = keyPair;
        this.zzcc = j;
    }

    /* access modifiers changed from: 0000 */
    public final KeyPair getKeyPair() {
        return this.zzcb;
    }

    /* access modifiers changed from: 0000 */
    public final long getCreationTime() {
        return this.zzcc;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzo)) {
            return false;
        }
        zzo zzo = (zzo) obj;
        if (this.zzcc != zzo.zzcc || !this.zzcb.getPublic().equals(zzo.zzcb.getPublic()) || !this.zzcb.getPrivate().equals(zzo.zzcb.getPrivate())) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzcb.getPublic(), this.zzcb.getPrivate(), Long.valueOf(this.zzcc));
    }

    /* access modifiers changed from: private */
    public final String zzq() {
        return Base64.encodeToString(this.zzcb.getPublic().getEncoded(), 11);
    }

    /* access modifiers changed from: private */
    public final String zzr() {
        return Base64.encodeToString(this.zzcb.getPrivate().getEncoded(), 11);
    }
}
