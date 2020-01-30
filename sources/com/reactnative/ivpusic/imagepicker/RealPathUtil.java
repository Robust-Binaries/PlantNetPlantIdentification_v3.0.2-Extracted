package com.reactnative.ivpusic.imagepicker;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import com.facebook.common.util.UriUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class RealPathUtil {
    RealPathUtil() {
    }

    static String getRealPathFromURI(Context context, Uri uri) throws IOException {
        Uri uri2 = null;
        if (!(VERSION.SDK_INT == 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }
                return getDataColumn(context, uri, null, null);
            } else if (UriUtil.LOCAL_FILE_SCHEME.equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else if (isExternalStorageDocument(uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            String[] split = documentId.split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                StringBuilder sb = new StringBuilder();
                sb.append(Environment.getExternalStorageDirectory());
                sb.append("/");
                sb.append(split[1]);
                return sb.toString();
            }
            int indexOf = documentId.indexOf(58, 1);
            String substring = documentId.substring(0, indexOf);
            String substring2 = documentId.substring(indexOf + 1);
            String pathToNonPrimaryVolume = getPathToNonPrimaryVolume(context, substring);
            if (pathToNonPrimaryVolume != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(pathToNonPrimaryVolume);
                sb2.append("/");
                sb2.append(substring2);
                String sb3 = sb2.toString();
                File file = new File(sb3);
                if (!file.exists() || !file.canRead()) {
                    return null;
                }
                return sb3;
            }
        } else if (isDownloadsDocument(uri)) {
            return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
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

    private static File writeToFile(Context context, String str, Uri uri) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getCacheDir());
        sb.append("/react-native-image-crop-picker");
        String sb2 = sb.toString();
        Boolean.valueOf(new File(sb2).mkdir());
        File file = new File(new File(sb2), str.substring(str.lastIndexOf(47) + 1));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[8192];
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            while (true) {
                int read = openInputStream.read(bArr, 0, bArr.length);
                if (read <= 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            openInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x005f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getDataColumn(android.content.Context r7, android.net.Uri r8, java.lang.String r9, java.lang.String[] r10) {
        /*
            r0 = 2
            java.lang.String[] r3 = new java.lang.String[r0]
            java.lang.String r0 = "_data"
            r1 = 0
            r3[r1] = r0
            java.lang.String r0 = "_display_name"
            r1 = 1
            r3[r1] = r0
            r0 = 0
            android.content.ContentResolver r1 = r7.getContentResolver()     // Catch:{ all -> 0x005b }
            r6 = 0
            r2 = r8
            r4 = r9
            r5 = r10
            android.database.Cursor r9 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ all -> 0x005b }
            if (r9 == 0) goto L_0x0055
            boolean r10 = r9.moveToFirst()     // Catch:{ all -> 0x0053 }
            if (r10 == 0) goto L_0x0055
            java.lang.String r10 = "_data"
            int r10 = r9.getColumnIndex(r10)     // Catch:{ all -> 0x0053 }
            r1 = -1
            if (r10 <= r1) goto L_0x002f
            java.lang.String r0 = r9.getString(r10)     // Catch:{ all -> 0x0053 }
        L_0x002f:
            if (r0 == 0) goto L_0x003b
            java.lang.String r7 = r9.getString(r10)     // Catch:{ all -> 0x0053 }
            if (r9 == 0) goto L_0x003a
            r9.close()
        L_0x003a:
            return r7
        L_0x003b:
            java.lang.String r10 = "_display_name"
            int r10 = r9.getColumnIndexOrThrow(r10)     // Catch:{ all -> 0x0053 }
            java.lang.String r10 = r9.getString(r10)     // Catch:{ all -> 0x0053 }
            java.io.File r7 = writeToFile(r7, r10, r8)     // Catch:{ all -> 0x0053 }
            java.lang.String r7 = r7.getAbsolutePath()     // Catch:{ all -> 0x0053 }
            if (r9 == 0) goto L_0x0052
            r9.close()
        L_0x0052:
            return r7
        L_0x0053:
            r7 = move-exception
            goto L_0x005d
        L_0x0055:
            if (r9 == 0) goto L_0x005a
            r9.close()
        L_0x005a:
            return r0
        L_0x005b:
            r7 = move-exception
            r9 = r0
        L_0x005d:
            if (r9 == 0) goto L_0x0062
            r9.close()
        L_0x0062:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.reactnative.ivpusic.imagepicker.RealPathUtil.getDataColumn(android.content.Context, android.net.Uri, java.lang.String, java.lang.String[]):java.lang.String");
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private static String getPathToNonPrimaryVolume(Context context, String str) {
        File[] externalCacheDirs = context.getExternalCacheDirs();
        if (externalCacheDirs != null) {
            for (File file : externalCacheDirs) {
                if (file != null) {
                    String absolutePath = file.getAbsolutePath();
                    if (absolutePath != null) {
                        int indexOf = absolutePath.indexOf(str);
                        if (indexOf != -1) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(absolutePath.substring(0, indexOf));
                            sb.append(str);
                            return sb.toString();
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return null;
    }
}
