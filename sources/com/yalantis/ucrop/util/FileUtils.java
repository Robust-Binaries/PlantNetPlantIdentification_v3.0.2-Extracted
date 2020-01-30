package com.yalantis.ucrop.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.common.util.UriUtil;

public class FileUtils {
    private static final String TAG = "FileUtils";

    private FileUtils() {
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002f, code lost:
        if (r9 != null) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0031, code lost:
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0051, code lost:
        if (r9 != null) goto L_0x0031;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0054, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0058  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getDataColumn(android.content.Context r9, android.net.Uri r10, java.lang.String r11, java.lang.String[] r12) {
        /*
            r0 = 1
            java.lang.String[] r3 = new java.lang.String[r0]
            java.lang.String r1 = "_data"
            r7 = 0
            r3[r7] = r1
            r8 = 0
            android.content.ContentResolver r1 = r9.getContentResolver()     // Catch:{ IllegalArgumentException -> 0x0038, all -> 0x0035 }
            r6 = 0
            r2 = r10
            r4 = r11
            r5 = r12
            android.database.Cursor r9 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ IllegalArgumentException -> 0x0038, all -> 0x0035 }
            if (r9 == 0) goto L_0x002f
            boolean r10 = r9.moveToFirst()     // Catch:{ IllegalArgumentException -> 0x002d }
            if (r10 == 0) goto L_0x002f
            java.lang.String r10 = "_data"
            int r10 = r9.getColumnIndexOrThrow(r10)     // Catch:{ IllegalArgumentException -> 0x002d }
            java.lang.String r10 = r9.getString(r10)     // Catch:{ IllegalArgumentException -> 0x002d }
            if (r9 == 0) goto L_0x002c
            r9.close()
        L_0x002c:
            return r10
        L_0x002d:
            r10 = move-exception
            goto L_0x003a
        L_0x002f:
            if (r9 == 0) goto L_0x0054
        L_0x0031:
            r9.close()
            goto L_0x0054
        L_0x0035:
            r10 = move-exception
            r9 = r8
            goto L_0x0056
        L_0x0038:
            r10 = move-exception
            r9 = r8
        L_0x003a:
            java.lang.String r11 = "FileUtils"
            java.util.Locale r12 = java.util.Locale.getDefault()     // Catch:{ all -> 0x0055 }
            java.lang.String r1 = "getDataColumn: _data - [%s]"
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0055 }
            java.lang.String r10 = r10.getMessage()     // Catch:{ all -> 0x0055 }
            r0[r7] = r10     // Catch:{ all -> 0x0055 }
            java.lang.String r10 = java.lang.String.format(r12, r1, r0)     // Catch:{ all -> 0x0055 }
            android.util.Log.i(r11, r10)     // Catch:{ all -> 0x0055 }
            if (r9 == 0) goto L_0x0054
            goto L_0x0031
        L_0x0054:
            return r8
        L_0x0055:
            r10 = move-exception
        L_0x0056:
            if (r9 == 0) goto L_0x005b
            r9.close()
        L_0x005b:
            throw r10
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yalantis.ucrop.util.FileUtils.getDataColumn(android.content.Context, android.net.Uri, java.lang.String, java.lang.String[]):java.lang.String");
    }

    @SuppressLint({"NewApi"})
    public static String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if (!(VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, null, null);
            } else if (UriUtil.LOCAL_FILE_SCHEME.equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else if (isExternalStorageDocument(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStorageDirectory());
                sb.append("/");
                sb.append(split[1]);
                return sb.toString();
            }
        } else if (isDownloadsDocument(uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (!TextUtils.isEmpty(documentId)) {
                try {
                    return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId).longValue()), null, null);
                } catch (NumberFormatException e) {
                    Log.i(TAG, e.getMessage());
                    return null;
                }
            }
        } else if (isMediaDocument(uri)) {
            String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
            String str = split2[0];
            if ("image".equals(str)) {
                uri2 = Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(str)) {
                uri2 = Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(str)) {
                uri2 = Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0051  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyFile(@android.support.annotation.NonNull java.lang.String r10, @android.support.annotation.NonNull java.lang.String r11) throws java.io.IOException {
        /*
            boolean r0 = r10.equalsIgnoreCase(r11)
            if (r0 == 0) goto L_0x0007
            return
        L_0x0007:
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x0048 }
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x0048 }
            r2.<init>(r10)     // Catch:{ all -> 0x0048 }
            r1.<init>(r2)     // Catch:{ all -> 0x0048 }
            java.nio.channels.FileChannel r10 = r1.getChannel()     // Catch:{ all -> 0x0048 }
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ all -> 0x0042 }
            java.io.File r2 = new java.io.File     // Catch:{ all -> 0x0042 }
            r2.<init>(r11)     // Catch:{ all -> 0x0042 }
            r1.<init>(r2)     // Catch:{ all -> 0x0042 }
            java.nio.channels.FileChannel r11 = r1.getChannel()     // Catch:{ all -> 0x0042 }
            r4 = 0
            long r6 = r10.size()     // Catch:{ all -> 0x003d }
            r3 = r10
            r8 = r11
            r3.transferTo(r4, r6, r8)     // Catch:{ all -> 0x003d }
            r10.close()     // Catch:{ all -> 0x003d }
            if (r10 == 0) goto L_0x0037
            r10.close()
        L_0x0037:
            if (r11 == 0) goto L_0x003c
            r11.close()
        L_0x003c:
            return
        L_0x003d:
            r0 = move-exception
            r9 = r0
            r0 = r10
            r10 = r9
            goto L_0x004a
        L_0x0042:
            r11 = move-exception
            r9 = r0
            r0 = r10
            r10 = r11
            r11 = r9
            goto L_0x004a
        L_0x0048:
            r10 = move-exception
            r11 = r0
        L_0x004a:
            if (r0 == 0) goto L_0x004f
            r0.close()
        L_0x004f:
            if (r11 == 0) goto L_0x0054
            r11.close()
        L_0x0054:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yalantis.ucrop.util.FileUtils.copyFile(java.lang.String, java.lang.String):void");
    }
}
