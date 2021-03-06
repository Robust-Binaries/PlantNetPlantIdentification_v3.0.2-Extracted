package com.google.firebase.analytics.connector;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.annotation.Size;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.DataCollectionDefaultChange;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.connector.AnalyticsConnector.AnalyticsConnectorHandle;
import com.google.firebase.analytics.connector.AnalyticsConnector.AnalyticsConnectorListener;
import com.google.firebase.analytics.connector.AnalyticsConnector.ConditionalUserProperty;
import com.google.firebase.analytics.connector.internal.zza;
import com.google.firebase.analytics.connector.internal.zzc;
import com.google.firebase.analytics.connector.internal.zzd;
import com.google.firebase.analytics.connector.internal.zzf;
import com.google.firebase.events.Event;
import com.google.firebase.events.Subscriber;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AnalyticsConnectorImpl implements AnalyticsConnector {
    private static volatile AnalyticsConnector zzaat;
    @VisibleForTesting
    private final AppMeasurement zzaau;
    @VisibleForTesting
    final Map<String, zza> zzaav = new ConcurrentHashMap();

    private AnalyticsConnectorImpl(AppMeasurement appMeasurement) {
        Preconditions.checkNotNull(appMeasurement);
        this.zzaau = appMeasurement;
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WAKE_LOCK"})
    @KeepForSdk
    public static AnalyticsConnector getInstance(FirebaseApp firebaseApp, Context context, Subscriber subscriber) {
        Preconditions.checkNotNull(firebaseApp);
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(subscriber);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzaat == null) {
            synchronized (AnalyticsConnectorImpl.class) {
                if (zzaat == null) {
                    Bundle bundle = new Bundle(1);
                    if (firebaseApp.isDefaultApp()) {
                        subscriber.subscribe(DataCollectionDefaultChange.class, zza.zzaaw, zzb.zzaax);
                        bundle.putBoolean("dataCollectionDefaultEnabled", firebaseApp.isDataCollectionDefaultEnabled());
                    }
                    zzaat = new AnalyticsConnectorImpl(AppMeasurement.zza(context, bundle));
                }
            }
        }
        return zzaat;
    }

    @KeepForSdk
    public static AnalyticsConnector getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    @KeepForSdk
    public static AnalyticsConnector getInstance(FirebaseApp firebaseApp) {
        return (AnalyticsConnector) firebaseApp.get(AnalyticsConnector.class);
    }

    @KeepForSdk
    public void logEvent(@NonNull String str, @NonNull String str2, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (zzc.zzcg(str) && zzc.zza(str2, bundle) && zzc.zzb(str, str2, bundle)) {
            this.zzaau.logEventInternal(str, str2, bundle);
        }
    }

    @KeepForSdk
    public void setUserProperty(@NonNull String str, @NonNull String str2, Object obj) {
        if (zzc.zzcg(str) && zzc.zzu(str, str2)) {
            this.zzaau.setUserPropertyInternal(str, str2, obj);
        }
    }

    @WorkerThread
    @KeepForSdk
    public Map<String, Object> getUserProperties(boolean z) {
        return this.zzaau.getUserProperties(z);
    }

    @WorkerThread
    @KeepForSdk
    public AnalyticsConnectorHandle registerAnalyticsConnectorListener(@NonNull final String str, AnalyticsConnectorListener analyticsConnectorListener) {
        Preconditions.checkNotNull(analyticsConnectorListener);
        if (!zzc.zzcg(str) || zzcf(str)) {
            return null;
        }
        AppMeasurement appMeasurement = this.zzaau;
        Object obj = "fiam".equals(str) ? new zzd(appMeasurement, analyticsConnectorListener) : "crash".equals(str) ? new zzf(appMeasurement, analyticsConnectorListener) : null;
        if (obj == null) {
            return null;
        }
        this.zzaav.put(str, obj);
        return new AnalyticsConnectorHandle() {
            public void unregister() {
                if (AnalyticsConnectorImpl.this.zzcf(str)) {
                    AnalyticsConnectorListener zzju = ((zza) AnalyticsConnectorImpl.this.zzaav.get(str)).zzju();
                    if (zzju != null) {
                        zzju.onMessageTriggered(0, null);
                    }
                    AnalyticsConnectorImpl.this.zzaav.remove(str);
                }
            }

            @KeepForSdk
            public void registerEventNames(Set<String> set) {
                if (AnalyticsConnectorImpl.this.zzcf(str) && str.equals("fiam") && set != null && !set.isEmpty()) {
                    ((zza) AnalyticsConnectorImpl.this.zzaav.get(str)).registerEventNames(set);
                }
            }

            @KeepForSdk
            public void unregisterEventNames() {
                if (AnalyticsConnectorImpl.this.zzcf(str) && str.equals("fiam")) {
                    ((zza) AnalyticsConnectorImpl.this.zzaav.get(str)).unregisterEventNames();
                }
            }
        };
    }

    @KeepForSdk
    public void setConditionalUserProperty(@NonNull ConditionalUserProperty conditionalUserProperty) {
        if (zzc.zza(conditionalUserProperty)) {
            this.zzaau.setConditionalUserProperty(zzc.zzb(conditionalUserProperty));
        }
    }

    @KeepForSdk
    public void clearConditionalUserProperty(@Size(max = 24, min = 1) @NonNull String str, @Nullable String str2, @Nullable Bundle bundle) {
        if (str2 == null || zzc.zza(str2, bundle)) {
            this.zzaau.clearConditionalUserProperty(str, str2, bundle);
        }
    }

    @WorkerThread
    @KeepForSdk
    public List<ConditionalUserProperty> getConditionalUserProperties(@NonNull String str, @Nullable @Size(max = 23, min = 1) String str2) {
        ArrayList arrayList = new ArrayList();
        for (AppMeasurement.ConditionalUserProperty zzb : this.zzaau.getConditionalUserProperties(str, str2)) {
            arrayList.add(zzc.zzb(zzb));
        }
        return arrayList;
    }

    @WorkerThread
    @KeepForSdk
    public int getMaxUserProperties(@Size(min = 1) @NonNull String str) {
        return this.zzaau.getMaxUserProperties(str);
    }

    /* access modifiers changed from: private */
    public final boolean zzcf(@NonNull String str) {
        return !str.isEmpty() && this.zzaav.containsKey(str) && this.zzaav.get(str) != null;
    }

    static final /* synthetic */ void zza(Event event) {
        boolean z = ((DataCollectionDefaultChange) event.getPayload()).enabled;
        synchronized (AnalyticsConnectorImpl.class) {
            ((AnalyticsConnectorImpl) zzaat).zzaau.zza(z);
        }
    }
}
