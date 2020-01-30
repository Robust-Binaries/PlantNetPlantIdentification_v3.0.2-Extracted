package com.facebook.react.modules.blob;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.support.annotation.Nullable;
import com.facebook.react.ReactApplication;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class BlobProvider extends ContentProvider {
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Nullable
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        return true;
    }

    @Nullable
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        if (str.equals("r")) {
            Context applicationContext = getContext().getApplicationContext();
            BlobModule blobModule = applicationContext instanceof ReactApplication ? (BlobModule) ((ReactApplication) applicationContext).getReactNativeHost().getReactInstanceManager().getCurrentReactContext().getNativeModule(BlobModule.class) : null;
            if (blobModule != null) {
                byte[] resolve = blobModule.resolve(uri);
                if (resolve != null) {
                    try {
                        ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
                        ParcelFileDescriptor parcelFileDescriptor = createPipe[0];
                        AutoCloseOutputStream autoCloseOutputStream = new AutoCloseOutputStream(createPipe[1]);
                        try {
                            autoCloseOutputStream.write(resolve);
                            autoCloseOutputStream.close();
                            return parcelFileDescriptor;
                        } catch (IOException unused) {
                            return null;
                        }
                    } catch (IOException unused2) {
                        return null;
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Cannot open ");
                    sb.append(uri.toString());
                    sb.append(", blob not found.");
                    throw new FileNotFoundException(sb.toString());
                }
            } else {
                throw new RuntimeException("No blob module associated with BlobProvider");
            }
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot open ");
            sb2.append(uri.toString());
            sb2.append(" in mode '");
            sb2.append(str);
            sb2.append("'");
            throw new FileNotFoundException(sb2.toString());
        }
    }
}
