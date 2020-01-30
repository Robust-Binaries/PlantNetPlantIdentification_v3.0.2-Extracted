package com.facebook.react.modules.camera;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore.Files;
import android.text.TextUtils;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.GuardedAsyncTask;
import com.facebook.react.bridge.JSApplicationIllegalArgumentException;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.ReactConstants;
import com.facebook.react.module.annotations.ReactModule;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import javax.annotation.Nullable;

@ReactModule(name = "CameraRollManager")
public class CameraRollManager extends ReactContextBaseJavaModule {
    private static final String ASSET_TYPE_ALL = "All";
    private static final String ASSET_TYPE_PHOTOS = "Photos";
    private static final String ASSET_TYPE_VIDEOS = "Videos";
    private static final String ERROR_UNABLE_TO_FILTER = "E_UNABLE_TO_FILTER";
    private static final String ERROR_UNABLE_TO_LOAD = "E_UNABLE_TO_LOAD";
    private static final String ERROR_UNABLE_TO_LOAD_PERMISSION = "E_UNABLE_TO_LOAD_PERMISSION";
    private static final String ERROR_UNABLE_TO_SAVE = "E_UNABLE_TO_SAVE";
    public static final String NAME = "CameraRollManager";
    /* access modifiers changed from: private */
    public static final String[] PROJECTION = {"_id", "mime_type", "bucket_display_name", "datetaken", "width", "height", "longitude", "latitude", "_data"};
    private static final String SELECTION_BUCKET = "bucket_display_name = ?";
    private static final String SELECTION_DATE_TAKEN = "datetaken < ?";

    private static class GetMediaTask extends GuardedAsyncTask<Void, Void> {
        @Nullable
        private final String mAfter;
        private final String mAssetType;
        private final Context mContext;
        private final int mFirst;
        @Nullable
        private final String mGroupName;
        @Nullable
        private final ReadableArray mMimeTypes;
        private final Promise mPromise;

        private GetMediaTask(ReactContext reactContext, int i, @Nullable String str, @Nullable String str2, @Nullable ReadableArray readableArray, String str3, Promise promise) {
            super(reactContext);
            this.mContext = reactContext;
            this.mFirst = i;
            this.mAfter = str;
            this.mGroupName = str2;
            this.mMimeTypes = readableArray;
            this.mPromise = promise;
            this.mAssetType = str3;
        }

        /* access modifiers changed from: protected */
        public void doInBackgroundGuarded(Void... voidArr) {
            Cursor query;
            StringBuilder sb = new StringBuilder("1");
            ArrayList arrayList = new ArrayList();
            if (!TextUtils.isEmpty(this.mAfter)) {
                sb.append(" AND datetaken < ?");
                arrayList.add(this.mAfter);
            }
            if (!TextUtils.isEmpty(this.mGroupName)) {
                sb.append(" AND bucket_display_name = ?");
                arrayList.add(this.mGroupName);
            }
            if (this.mAssetType.equals(CameraRollManager.ASSET_TYPE_PHOTOS)) {
                sb.append(" AND media_type = 1");
            } else if (this.mAssetType.equals(CameraRollManager.ASSET_TYPE_VIDEOS)) {
                sb.append(" AND media_type = 3");
            } else if (this.mAssetType.equals(CameraRollManager.ASSET_TYPE_ALL)) {
                sb.append(" AND media_type IN (3,1)");
            } else {
                Promise promise = this.mPromise;
                String str = CameraRollManager.ERROR_UNABLE_TO_FILTER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid filter option: '");
                sb2.append(this.mAssetType);
                sb2.append("'. Expected one of '");
                sb2.append(CameraRollManager.ASSET_TYPE_PHOTOS);
                sb2.append("', '");
                sb2.append(CameraRollManager.ASSET_TYPE_VIDEOS);
                sb2.append("' or '");
                sb2.append(CameraRollManager.ASSET_TYPE_ALL);
                sb2.append("'.");
                promise.reject(str, sb2.toString());
                return;
            }
            ReadableArray readableArray = this.mMimeTypes;
            if (readableArray != null && readableArray.size() > 0) {
                sb.append(" AND mime_type IN (");
                for (int i = 0; i < this.mMimeTypes.size(); i++) {
                    sb.append("?,");
                    arrayList.add(this.mMimeTypes.getString(i));
                }
                sb.replace(sb.length() - 1, sb.length(), ")");
            }
            WritableNativeMap writableNativeMap = new WritableNativeMap();
            ContentResolver contentResolver = this.mContext.getContentResolver();
            try {
                Uri contentUri = Files.getContentUri("external");
                String[] access$200 = CameraRollManager.PROJECTION;
                String sb3 = sb.toString();
                String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("datetaken DESC, date_modified DESC LIMIT ");
                sb4.append(this.mFirst + 1);
                query = contentResolver.query(contentUri, access$200, sb3, strArr, sb4.toString());
                if (query == null) {
                    this.mPromise.reject(CameraRollManager.ERROR_UNABLE_TO_LOAD, "Could not get media");
                } else {
                    CameraRollManager.putEdges(contentResolver, query, writableNativeMap, this.mFirst);
                    CameraRollManager.putPageInfo(query, writableNativeMap, this.mFirst);
                    query.close();
                    this.mPromise.resolve(writableNativeMap);
                }
            } catch (SecurityException e) {
                this.mPromise.reject(CameraRollManager.ERROR_UNABLE_TO_LOAD_PERMISSION, "Could not get media: need READ_EXTERNAL_STORAGE permission", (Throwable) e);
            } catch (Throwable th) {
                query.close();
                this.mPromise.resolve(writableNativeMap);
                throw th;
            }
        }
    }

    private static class SaveToCameraRoll extends GuardedAsyncTask<Void, Void> {
        private final Context mContext;
        /* access modifiers changed from: private */
        public final Promise mPromise;
        private final Uri mUri;

        public SaveToCameraRoll(ReactContext reactContext, Uri uri, Promise promise) {
            super(reactContext);
            this.mContext = reactContext;
            this.mUri = uri;
            this.mPromise = promise;
        }

        /* access modifiers changed from: protected */
        public void doInBackgroundGuarded(Void... voidArr) {
            FileChannel fileChannel;
            String str;
            int i;
            File file = new File(this.mUri.getPath());
            FileChannel fileChannel2 = null;
            try {
                File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                externalStoragePublicDirectory.mkdirs();
                if (!externalStoragePublicDirectory.isDirectory()) {
                    this.mPromise.reject(CameraRollManager.ERROR_UNABLE_TO_LOAD, "External media storage directory not available");
                    return;
                }
                File file2 = new File(externalStoragePublicDirectory, file.getName());
                String name = file.getName();
                if (name.indexOf(46) >= 0) {
                    String substring = name.substring(0, name.lastIndexOf(46));
                    i = 0;
                    String str2 = substring;
                    str = name.substring(name.lastIndexOf(46));
                    name = str2;
                } else {
                    str = "";
                    i = 0;
                }
                while (!file2.createNewFile()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(name);
                    sb.append("_");
                    int i2 = i + 1;
                    sb.append(i);
                    sb.append(str);
                    file2 = new File(externalStoragePublicDirectory, sb.toString());
                    i = i2;
                }
                FileChannel channel = new FileInputStream(file).getChannel();
                try {
                    fileChannel = new FileOutputStream(file2).getChannel();
                    try {
                        fileChannel.transferFrom(channel, 0, channel.size());
                        channel.close();
                        fileChannel.close();
                        MediaScannerConnection.scanFile(this.mContext, new String[]{file2.getAbsolutePath()}, null, new OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                                if (uri != null) {
                                    SaveToCameraRoll.this.mPromise.resolve(uri.toString());
                                } else {
                                    SaveToCameraRoll.this.mPromise.reject(CameraRollManager.ERROR_UNABLE_TO_SAVE, "Could not add image to gallery");
                                }
                            }
                        });
                        if (channel != null && channel.isOpen()) {
                            try {
                                channel.close();
                            } catch (IOException e) {
                                FLog.m49e(ReactConstants.TAG, "Could not close input channel", (Throwable) e);
                            }
                        }
                        if (fileChannel != null && fileChannel.isOpen()) {
                            try {
                                fileChannel.close();
                            } catch (IOException e2) {
                                FLog.m49e(ReactConstants.TAG, "Could not close output channel", (Throwable) e2);
                            }
                        }
                    } catch (IOException e3) {
                        Throwable th = e3;
                        fileChannel2 = channel;
                        e = th;
                        try {
                            this.mPromise.reject(e);
                            try {
                                fileChannel2.close();
                            } catch (IOException e4) {
                                FLog.m49e(ReactConstants.TAG, "Could not close input channel", (Throwable) e4);
                            }
                            fileChannel.close();
                        } catch (Throwable th2) {
                            th = th2;
                            if (fileChannel2 != null && fileChannel2.isOpen()) {
                                try {
                                    fileChannel2.close();
                                } catch (IOException e5) {
                                    FLog.m49e(ReactConstants.TAG, "Could not close input channel", (Throwable) e5);
                                }
                            }
                            if (fileChannel != null && fileChannel.isOpen()) {
                                try {
                                    fileChannel.close();
                                } catch (IOException e6) {
                                    FLog.m49e(ReactConstants.TAG, "Could not close output channel", (Throwable) e6);
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        Throwable th4 = th3;
                        fileChannel2 = channel;
                        th = th4;
                        fileChannel2.close();
                        fileChannel.close();
                        throw th;
                    }
                } catch (IOException e7) {
                    fileChannel2 = channel;
                    e = e7;
                    fileChannel = null;
                    this.mPromise.reject(e);
                    if (fileChannel2 != null && fileChannel2.isOpen()) {
                        fileChannel2.close();
                    }
                    if (fileChannel != null && fileChannel.isOpen()) {
                        fileChannel.close();
                    }
                } catch (Throwable th5) {
                    fileChannel2 = channel;
                    th = th5;
                    fileChannel = null;
                    fileChannel2.close();
                    fileChannel.close();
                    throw th;
                }
            } catch (IOException e8) {
                e = e8;
                fileChannel = null;
                this.mPromise.reject(e);
                fileChannel2.close();
                fileChannel.close();
            } catch (Throwable th6) {
                th = th6;
                fileChannel = null;
                fileChannel2.close();
                fileChannel.close();
                throw th;
            }
        }
    }

    public String getName() {
        return NAME;
    }

    public CameraRollManager(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public void saveToCameraRoll(String str, String str2, Promise promise) {
        new SaveToCameraRoll(getReactApplicationContext(), Uri.parse(str), promise).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @ReactMethod
    public void getPhotos(ReadableMap readableMap, Promise promise) {
        int i = readableMap.getInt("first");
        String string = readableMap.hasKey("after") ? readableMap.getString("after") : null;
        String string2 = readableMap.hasKey("groupName") ? readableMap.getString("groupName") : null;
        String string3 = readableMap.hasKey("assetType") ? readableMap.getString("assetType") : ASSET_TYPE_PHOTOS;
        ReadableArray array = readableMap.hasKey("mimeTypes") ? readableMap.getArray("mimeTypes") : null;
        if (!readableMap.hasKey("groupTypes")) {
            GetMediaTask getMediaTask = new GetMediaTask(getReactApplicationContext(), i, string, string2, array, string3, promise);
            getMediaTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            return;
        }
        throw new JSApplicationIllegalArgumentException("groupTypes is not supported on Android");
    }

    /* access modifiers changed from: private */
    public static void putPageInfo(Cursor cursor, WritableMap writableMap, int i) {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putBoolean("has_next_page", i < cursor.getCount());
        if (i < cursor.getCount()) {
            cursor.moveToPosition(i - 1);
            writableNativeMap.putString("end_cursor", cursor.getString(cursor.getColumnIndex("datetaken")));
        }
        writableMap.putMap("page_info", writableNativeMap);
    }

    /* access modifiers changed from: private */
    public static void putEdges(ContentResolver contentResolver, Cursor cursor, WritableMap writableMap, int i) {
        WritableNativeArray writableNativeArray;
        Cursor cursor2 = cursor;
        WritableNativeArray writableNativeArray2 = new WritableNativeArray();
        cursor.moveToFirst();
        int columnIndex = cursor2.getColumnIndex("_id");
        int columnIndex2 = cursor2.getColumnIndex("mime_type");
        int columnIndex3 = cursor2.getColumnIndex("bucket_display_name");
        int columnIndex4 = cursor2.getColumnIndex("datetaken");
        int columnIndex5 = cursor2.getColumnIndex("width");
        int columnIndex6 = cursor2.getColumnIndex("height");
        int columnIndex7 = cursor2.getColumnIndex("longitude");
        int columnIndex8 = cursor2.getColumnIndex("latitude");
        int columnIndex9 = cursor2.getColumnIndex("_data");
        int i2 = i;
        int i3 = 0;
        while (i3 < i2 && !cursor.isAfterLast()) {
            WritableNativeMap writableNativeMap = new WritableNativeMap();
            WritableNativeMap writableNativeMap2 = new WritableNativeMap();
            WritableNativeMap writableNativeMap3 = writableNativeMap2;
            WritableNativeArray writableNativeArray3 = writableNativeArray2;
            WritableNativeMap writableNativeMap4 = writableNativeMap;
            int i4 = i3;
            int i5 = columnIndex;
            int i6 = columnIndex8;
            if (putImageInfo(contentResolver, cursor, writableNativeMap2, columnIndex, columnIndex5, columnIndex6, columnIndex9)) {
                WritableNativeMap writableNativeMap5 = writableNativeMap3;
                putBasicNodeInfo(cursor2, writableNativeMap5, columnIndex2, columnIndex3, columnIndex4);
                putLocationInfo(cursor2, writableNativeMap5, columnIndex7, i6);
                writableNativeMap4.putMap("node", writableNativeMap5);
                writableNativeArray = writableNativeArray3;
                writableNativeArray.pushMap(writableNativeMap4);
            } else {
                writableNativeArray = writableNativeArray3;
                i4--;
            }
            cursor.moveToNext();
            i3 = i4 + 1;
            i2 = i;
            writableNativeArray2 = writableNativeArray;
            columnIndex8 = i6;
            columnIndex = i5;
        }
        WritableMap writableMap2 = writableMap;
        writableMap2.putArray("edges", writableNativeArray2);
    }

    private static void putBasicNodeInfo(Cursor cursor, WritableMap writableMap, int i, int i2, int i3) {
        writableMap.putString("type", cursor.getString(i));
        writableMap.putString("group_name", cursor.getString(i2));
        double d = (double) cursor.getLong(i3);
        Double.isNaN(d);
        writableMap.putDouble("timestamp", d / 1000.0d);
    }

    private static boolean putImageInfo(ContentResolver contentResolver, Cursor cursor, WritableMap writableMap, int i, int i2, int i3, int i4) {
        AssetFileDescriptor openAssetFileDescriptor;
        MediaMetadataRetriever mediaMetadataRetriever;
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        StringBuilder sb = new StringBuilder();
        sb.append("file://");
        sb.append(cursor.getString(i4));
        Uri parse = Uri.parse(sb.toString());
        writableNativeMap.putString(RNFetchBlobConst.DATA_ENCODE_URI, parse.toString());
        float f = (float) cursor.getInt(i2);
        float f2 = (float) cursor.getInt(i3);
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(parse.toString());
        if (guessContentTypeFromName != null && guessContentTypeFromName.startsWith("video")) {
            try {
                openAssetFileDescriptor = contentResolver.openAssetFileDescriptor(parse, "r");
                mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(openAssetFileDescriptor.getFileDescriptor());
                if (f <= 0.0f || f2 <= 0.0f) {
                    try {
                        f = (float) Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
                        f2 = (float) Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
                    } catch (NumberFormatException e) {
                        String str = ReactConstants.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Number format exception occurred while trying to fetch video metadata for ");
                        sb2.append(parse.toString());
                        FLog.m49e(str, sb2.toString(), (Throwable) e);
                        mediaMetadataRetriever.release();
                        openAssetFileDescriptor.close();
                        return false;
                    }
                }
                writableNativeMap.putInt("playableDuration", Integer.parseInt(mediaMetadataRetriever.extractMetadata(9)) / 1000);
                mediaMetadataRetriever.release();
                openAssetFileDescriptor.close();
            } catch (Exception e2) {
                String str2 = ReactConstants.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Could not get video metadata for ");
                sb3.append(parse.toString());
                FLog.m49e(str2, sb3.toString(), (Throwable) e2);
                return false;
            } catch (Throwable th) {
                mediaMetadataRetriever.release();
                openAssetFileDescriptor.close();
                throw th;
            }
        }
        if (f <= 0.0f || f2 <= 0.0f) {
            try {
                AssetFileDescriptor openAssetFileDescriptor2 = contentResolver.openAssetFileDescriptor(parse, "r");
                Options options = new Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(openAssetFileDescriptor2.getFileDescriptor(), null, options);
                f = (float) options.outWidth;
                f2 = (float) options.outHeight;
                openAssetFileDescriptor2.close();
            } catch (IOException e3) {
                String str3 = ReactConstants.TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Could not get width/height for ");
                sb4.append(parse.toString());
                FLog.m49e(str3, sb4.toString(), (Throwable) e3);
                return false;
            }
        }
        writableNativeMap.putDouble("width", (double) f);
        writableNativeMap.putDouble("height", (double) f2);
        writableMap.putMap("image", writableNativeMap);
        return true;
    }

    private static void putLocationInfo(Cursor cursor, WritableMap writableMap, int i, int i2) {
        double d = cursor.getDouble(i);
        double d2 = cursor.getDouble(i2);
        if (d > 0.0d || d2 > 0.0d) {
            WritableNativeMap writableNativeMap = new WritableNativeMap();
            writableNativeMap.putDouble("longitude", d);
            writableNativeMap.putDouble("latitude", d2);
            writableMap.putMap(Param.LOCATION, writableNativeMap);
        }
    }
}
