package com.reactnative.ivpusic.imagepicker;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.FileProvider;
import android.util.Base64;
import android.webkit.MimeTypeMap;
import com.RNFetchBlob.RNFetchBlobConst;
import com.facebook.common.util.UriUtil;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.PromiseImpl;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.yalantis.ucrop.UCrop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

class PickerModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final int CAMERA_PICKER_REQUEST = 61111;
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_CALLBACK_ERROR = "E_CALLBACK_ERROR";
    private static final String E_CAMERA_IS_NOT_AVAILABLE = "E_CAMERA_IS_NOT_AVAILABLE";
    private static final String E_CANNOT_LAUNCH_CAMERA = "E_CANNOT_LAUNCH_CAMERA";
    private static final String E_ERROR_WHILE_CLEANING_FILES = "E_ERROR_WHILE_CLEANING_FILES";
    private static final String E_FAILED_TO_OPEN_CAMERA = "E_FAILED_TO_OPEN_CAMERA";
    private static final String E_FAILED_TO_SHOW_PICKER = "E_FAILED_TO_SHOW_PICKER";
    private static final String E_NO_IMAGE_DATA_FOUND = "E_NO_IMAGE_DATA_FOUND";
    private static final String E_PERMISSIONS_MISSING = "E_PERMISSION_MISSING";
    private static final String E_PICKER_CANCELLED_KEY = "E_PICKER_CANCELLED";
    private static final String E_PICKER_CANCELLED_MSG = "User cancelled image selection";
    private static final int IMAGE_PICKER_REQUEST = 61110;
    private final String DEFAULT_TINT = "#424242";
    private final String DEFAULT_WIDGET_COLOR = "#03A9F4";
    /* access modifiers changed from: private */
    public Compression compression = new Compression();
    private String cropperActiveWidgetColor = "#424242";
    private boolean cropperCircleOverlay = false;
    private String cropperStatusBarColor = "#424242";
    private String cropperToolbarColor = "#424242";
    private String cropperToolbarTitle = null;
    private boolean cropping = false;
    private boolean disableCropperColorSetters = false;
    private boolean enableRotationGesture = false;
    private boolean freeStyleCropEnabled = false;
    private int height = 0;
    private boolean hideBottomControls = false;
    private boolean includeBase64 = false;
    private boolean includeExif = false;
    private Uri mCameraCaptureURI;
    private String mCurrentMediaPath;
    private String mediaType = "any";
    private boolean multiple = false;
    /* access modifiers changed from: private */
    public ReadableMap options;
    private ReactApplicationContext reactContext;
    /* access modifiers changed from: private */
    public ResultCollector resultCollector = new ResultCollector();
    private boolean showCropGuidelines = true;
    private boolean useFrontCamera = false;
    private int width = 0;

    public String getName() {
        return "ImageCropPicker";
    }

    public void onNewIntent(Intent intent) {
    }

    PickerModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        reactApplicationContext.addActivityEventListener(this);
        this.reactContext = reactApplicationContext;
    }

    /* access modifiers changed from: private */
    public String getTmpDir(Activity activity) {
        StringBuilder sb = new StringBuilder();
        sb.append(activity.getCacheDir());
        sb.append("/react-native-image-crop-picker");
        String sb2 = sb.toString();
        new File(sb2).mkdir();
        return sb2;
    }

    private void setConfiguration(ReadableMap readableMap) {
        this.mediaType = readableMap.hasKey("mediaType") ? readableMap.getString("mediaType") : "any";
        boolean z = true;
        this.multiple = readableMap.hasKey("multiple") && readableMap.getBoolean("multiple");
        this.includeBase64 = readableMap.hasKey("includeBase64") && readableMap.getBoolean("includeBase64");
        this.includeExif = readableMap.hasKey("includeExif") && readableMap.getBoolean("includeExif");
        this.width = readableMap.hasKey("width") ? readableMap.getInt("width") : 0;
        this.height = readableMap.hasKey("height") ? readableMap.getInt("height") : 0;
        this.cropping = readableMap.hasKey("cropping") && readableMap.getBoolean("cropping");
        this.cropperActiveWidgetColor = readableMap.hasKey("cropperActiveWidgetColor") ? readableMap.getString("cropperActiveWidgetColor") : "#424242";
        this.cropperStatusBarColor = readableMap.hasKey("cropperStatusBarColor") ? readableMap.getString("cropperStatusBarColor") : "#424242";
        this.cropperToolbarColor = readableMap.hasKey("cropperToolbarColor") ? readableMap.getString("cropperToolbarColor") : "#424242";
        this.cropperToolbarTitle = readableMap.hasKey("cropperToolbarTitle") ? readableMap.getString("cropperToolbarTitle") : null;
        this.cropperCircleOverlay = readableMap.hasKey("cropperCircleOverlay") && readableMap.getBoolean("cropperCircleOverlay");
        this.freeStyleCropEnabled = readableMap.hasKey("freeStyleCropEnabled") && readableMap.getBoolean("freeStyleCropEnabled");
        this.showCropGuidelines = !readableMap.hasKey("showCropGuidelines") || readableMap.getBoolean("showCropGuidelines");
        this.hideBottomControls = readableMap.hasKey("hideBottomControls") && readableMap.getBoolean("hideBottomControls");
        this.enableRotationGesture = readableMap.hasKey("enableRotationGesture") && readableMap.getBoolean("enableRotationGesture");
        this.disableCropperColorSetters = readableMap.hasKey("disableCropperColorSetters") && readableMap.getBoolean("disableCropperColorSetters");
        if (!readableMap.hasKey("useFrontCamera") || !readableMap.getBoolean("useFrontCamera")) {
            z = false;
        }
        this.useFrontCamera = z;
        this.options = readableMap;
    }

    /* access modifiers changed from: private */
    public void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File deleteRecursive : file.listFiles()) {
                deleteRecursive(deleteRecursive);
            }
        }
        file.delete();
    }

    @ReactMethod
    public void clean(final Promise promise) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
        } else {
            permissionsCheck(currentActivity, promise, Collections.singletonList("android.permission.WRITE_EXTERNAL_STORAGE"), new Callable<Void>() {
                public Void call() {
                    try {
                        File file = new File(this.getTmpDir(currentActivity));
                        if (file.exists()) {
                            this.deleteRecursive(file);
                            promise.resolve(null);
                            return null;
                        }
                        throw new Exception("File does not exist");
                    } catch (Exception e) {
                        e.printStackTrace();
                        promise.reject(PickerModule.E_ERROR_WHILE_CLEANING_FILES, e.getMessage());
                    }
                }
            });
        }
    }

    @ReactMethod
    public void cleanSingle(final String str, final Promise promise) {
        if (str == null) {
            promise.reject(E_ERROR_WHILE_CLEANING_FILES, "Cannot cleanup empty path");
            return;
        }
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
        } else {
            permissionsCheck(currentActivity, promise, Collections.singletonList("android.permission.WRITE_EXTERNAL_STORAGE"), new Callable<Void>() {
                public Void call() throws Exception {
                    try {
                        String str = str;
                        if (str.startsWith("file://")) {
                            str = str.substring(7);
                        }
                        File file = new File(str);
                        if (file.exists()) {
                            this.deleteRecursive(file);
                            promise.resolve(null);
                            return null;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("File does not exist. Path: ");
                        sb.append(str);
                        throw new Exception(sb.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        promise.reject(PickerModule.E_ERROR_WHILE_CLEANING_FILES, e.getMessage());
                    }
                }
            });
        }
    }

    private void permissionsCheck(Activity activity, final Promise promise, List<String> list, final Callable<Void> callable) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            if (ActivityCompat.checkSelfPermission(activity, str) != 0) {
                arrayList.add(str);
            }
        }
        if (!arrayList.isEmpty()) {
            ((PermissionAwareActivity) activity).requestPermissions((String[]) arrayList.toArray(new String[arrayList.size()]), 1, new PermissionListener() {
                public boolean onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
                    if (i == 1) {
                        for (int i2 : iArr) {
                            if (i2 == -1) {
                                promise.reject(PickerModule.E_PERMISSIONS_MISSING, "Required permission missing");
                                return true;
                            }
                        }
                        try {
                            callable.call();
                        } catch (Exception e) {
                            promise.reject(PickerModule.E_CALLBACK_ERROR, "Unknown error", (Throwable) e);
                        }
                    }
                    return true;
                }
            });
            return;
        }
        try {
            callable.call();
        } catch (Exception e) {
            promise.reject(E_CALLBACK_ERROR, "Unknown error", (Throwable) e);
        }
    }

    @ReactMethod
    public void openCamera(ReadableMap readableMap, Promise promise) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
        } else if (!isCameraAvailable(currentActivity)) {
            promise.reject(E_CAMERA_IS_NOT_AVAILABLE, "Camera not available");
        } else {
            setConfiguration(readableMap);
            this.resultCollector.setup(promise, false);
            permissionsCheck(currentActivity, promise, Arrays.asList(new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"}), new Callable<Void>() {
                public Void call() {
                    PickerModule.this.initiateCamera(currentActivity);
                    return null;
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void initiateCamera(Activity activity) {
        File file;
        String str;
        try {
            if (this.mediaType.equals("video")) {
                str = "android.media.action.VIDEO_CAPTURE";
                file = createVideoFile();
            } else {
                str = "android.media.action.IMAGE_CAPTURE";
                file = createImageFile();
            }
            Intent intent = new Intent(str);
            if (VERSION.SDK_INT < 21) {
                this.mCameraCaptureURI = Uri.fromFile(file);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(activity.getApplicationContext().getPackageName());
                sb.append(".provider");
                this.mCameraCaptureURI = FileProvider.getUriForFile(activity, sb.toString(), file);
            }
            intent.putExtra("output", this.mCameraCaptureURI);
            if (this.useFrontCamera) {
                intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            }
            if (intent.resolveActivity(activity.getPackageManager()) == null) {
                this.resultCollector.notifyProblem(E_CANNOT_LAUNCH_CAMERA, "Cannot launch camera");
            } else {
                activity.startActivityForResult(intent, CAMERA_PICKER_REQUEST);
            }
        } catch (Exception e) {
            this.resultCollector.notifyProblem(E_FAILED_TO_OPEN_CAMERA, (Throwable) e);
        }
    }

    /* access modifiers changed from: private */
    public void initiatePicker(Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            if (!this.cropping) {
                if (!this.mediaType.equals("photo")) {
                    if (this.mediaType.equals("video")) {
                        intent.setType("video/*");
                    } else {
                        intent.setType("*/*");
                        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{"image/*", "video/*"});
                    }
                    intent.setFlags(67108864);
                    intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", this.multiple);
                    intent.addCategory("android.intent.category.OPENABLE");
                    activity.startActivityForResult(Intent.createChooser(intent, "Pick an image"), IMAGE_PICKER_REQUEST);
                }
            }
            intent.setType("image/*");
            intent.setFlags(67108864);
            intent.putExtra("android.intent.extra.ALLOW_MULTIPLE", this.multiple);
            intent.addCategory("android.intent.category.OPENABLE");
            activity.startActivityForResult(Intent.createChooser(intent, "Pick an image"), IMAGE_PICKER_REQUEST);
        } catch (Exception e) {
            this.resultCollector.notifyProblem(E_FAILED_TO_SHOW_PICKER, (Throwable) e);
        }
    }

    @ReactMethod
    public void openPicker(ReadableMap readableMap, Promise promise) {
        final Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }
        setConfiguration(readableMap);
        this.resultCollector.setup(promise, this.multiple);
        permissionsCheck(currentActivity, promise, Collections.singletonList("android.permission.WRITE_EXTERNAL_STORAGE"), new Callable<Void>() {
            public Void call() {
                PickerModule.this.initiatePicker(currentActivity);
                return null;
            }
        });
    }

    @ReactMethod
    public void openCropper(ReadableMap readableMap, Promise promise) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }
        setConfiguration(readableMap);
        this.resultCollector.setup(promise, false);
        startCropping(currentActivity, Uri.parse(readableMap.getString(RNFetchBlobConst.RNFB_RESPONSE_PATH)));
    }

    private String getBase64StringFromFile(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            byte[] bArr = new byte[8192];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                try {
                    int read = fileInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private String getMimeType(String str) {
        Uri fromFile = Uri.fromFile(new File(str));
        if (fromFile.getScheme().equals("content")) {
            return this.reactContext.getContentResolver().getType(fromFile);
        }
        String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(fromFile.toString());
        if (fileExtensionFromUrl != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl.toLowerCase());
        }
        return null;
    }

    private WritableMap getSelection(Activity activity, Uri uri, boolean z) throws Exception {
        String resolveRealPath = resolveRealPath(activity, uri, z);
        if (resolveRealPath == null || resolveRealPath.isEmpty()) {
            throw new Exception("Cannot resolve asset path.");
        }
        String mimeType = getMimeType(resolveRealPath);
        if (mimeType == null || !mimeType.startsWith("video/")) {
            return getImage(activity, resolveRealPath);
        }
        getVideo(activity, resolveRealPath, mimeType);
        return null;
    }

    private void getAsyncSelection(Activity activity, Uri uri, boolean z) throws Exception {
        String resolveRealPath = resolveRealPath(activity, uri, z);
        if (resolveRealPath == null || resolveRealPath.isEmpty()) {
            this.resultCollector.notifyProblem(E_NO_IMAGE_DATA_FOUND, "Cannot resolve asset path.");
            return;
        }
        String mimeType = getMimeType(resolveRealPath);
        if (mimeType == null || !mimeType.startsWith("video/")) {
            this.resultCollector.notifySuccess(getImage(activity, resolveRealPath));
        } else {
            getVideo(activity, resolveRealPath, mimeType);
        }
    }

    /* access modifiers changed from: private */
    public Bitmap validateVideo(String str) throws Exception {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(str);
        Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime();
        if (frameAtTime != null) {
            return frameAtTime;
        }
        throw new Exception("Cannot retrieve video data");
    }

    private void getVideo(Activity activity, String str, String str2) throws Exception {
        validateVideo(str);
        StringBuilder sb = new StringBuilder();
        sb.append(getTmpDir(activity));
        sb.append("/");
        sb.append(UUID.randomUUID().toString());
        sb.append(".mp4");
        final String sb2 = sb.toString();
        final Activity activity2 = activity;
        final String str3 = str;
        final String str4 = str2;
        C12076 r2 = new Runnable() {
            public void run() {
                PickerModule.this.compression.compressVideo(activity2, PickerModule.this.options, str3, sb2, new PromiseImpl(new Callback() {
                    public void invoke(Object... objArr) {
                        String str = objArr[0];
                        try {
                            Bitmap access$500 = PickerModule.this.validateVideo(str);
                            long lastModified = new File(str).lastModified();
                            WritableNativeMap writableNativeMap = new WritableNativeMap();
                            writableNativeMap.putInt("width", access$500.getWidth());
                            writableNativeMap.putInt("height", access$500.getHeight());
                            writableNativeMap.putString("mime", str4);
                            writableNativeMap.putInt("size", (int) new File(str).length());
                            String str2 = RNFetchBlobConst.RNFB_RESPONSE_PATH;
                            StringBuilder sb = new StringBuilder();
                            sb.append("file://");
                            sb.append(str);
                            writableNativeMap.putString(str2, sb.toString());
                            writableNativeMap.putString("modificationDate", String.valueOf(lastModified));
                            PickerModule.this.resultCollector.notifySuccess(writableNativeMap);
                        } catch (Exception e) {
                            PickerModule.this.resultCollector.notifyProblem(PickerModule.E_NO_IMAGE_DATA_FOUND, (Throwable) e);
                        }
                    }
                }, new Callback() {
                    public void invoke(Object... objArr) {
                        WritableNativeMap writableNativeMap = objArr[0];
                        PickerModule.this.resultCollector.notifyProblem(writableNativeMap.getString("code"), writableNativeMap.getString("message"));
                    }
                }));
            }
        };
        new Thread(r2).run();
    }

    private String resolveRealPath(Activity activity, Uri uri, boolean z) throws IOException {
        if (VERSION.SDK_INT < 21) {
            return RealPathUtil.getRealPathFromURI(activity, uri);
        }
        if (z) {
            return Uri.parse(this.mCurrentMediaPath).getPath();
        }
        return RealPathUtil.getRealPathFromURI(activity, uri);
    }

    private Options validateImage(String str) throws Exception {
        Options options2 = new Options();
        options2.inJustDecodeBounds = true;
        options2.inPreferredConfig = Config.RGB_565;
        options2.inDither = true;
        BitmapFactory.decodeFile(str, options2);
        if (options2.outMimeType != null && options2.outWidth != 0 && options2.outHeight != 0) {
            return options2;
        }
        throw new Exception("Invalid image selected");
    }

    private WritableMap getImage(Activity activity, String str) throws Exception {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        if (str.startsWith("http://") || str.startsWith("https://")) {
            throw new Exception("Cannot select remote files");
        }
        String path = this.compression.compressImage(this.options, str, validateImage(str)).getPath();
        Options validateImage = validateImage(path);
        long lastModified = new File(str).lastModified();
        String str2 = RNFetchBlobConst.RNFB_RESPONSE_PATH;
        StringBuilder sb = new StringBuilder();
        sb.append("file://");
        sb.append(path);
        writableNativeMap.putString(str2, sb.toString());
        writableNativeMap.putInt("width", validateImage.outWidth);
        writableNativeMap.putInt("height", validateImage.outHeight);
        writableNativeMap.putString("mime", validateImage.outMimeType);
        writableNativeMap.putInt("size", (int) new File(path).length());
        writableNativeMap.putString("modificationDate", String.valueOf(lastModified));
        if (this.includeBase64) {
            writableNativeMap.putString(UriUtil.DATA_SCHEME, getBase64StringFromFile(path));
        }
        if (this.includeExif) {
            try {
                writableNativeMap.putMap("exif", ExifExtractor.extract(str));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return writableNativeMap;
    }

    private void configureCropperColors(UCrop.Options options2) {
        int parseColor = Color.parseColor(this.cropperActiveWidgetColor);
        int parseColor2 = Color.parseColor(this.cropperToolbarColor);
        int parseColor3 = Color.parseColor(this.cropperStatusBarColor);
        options2.setToolbarColor(parseColor2);
        options2.setStatusBarColor(parseColor3);
        if (parseColor == Color.parseColor("#424242")) {
            options2.setActiveWidgetColor(Color.parseColor("#03A9F4"));
        } else {
            options2.setActiveWidgetColor(parseColor);
        }
    }

    private void startCropping(Activity activity, Uri uri) {
        UCrop.Options options2 = new UCrop.Options();
        options2.setCompressionFormat(CompressFormat.JPEG);
        options2.setCompressionQuality(100);
        options2.setCircleDimmedLayer(this.cropperCircleOverlay);
        options2.setFreeStyleCropEnabled(this.freeStyleCropEnabled);
        options2.setShowCropGrid(this.showCropGuidelines);
        options2.setHideBottomControls(this.hideBottomControls);
        String str = this.cropperToolbarTitle;
        if (str != null) {
            options2.setToolbarTitle(str);
        }
        if (this.enableRotationGesture) {
            options2.setAllowedGestures(3, 3, 3);
        }
        if (!this.disableCropperColorSetters) {
            configureCropperColors(options2);
        }
        String tmpDir = getTmpDir(activity);
        StringBuilder sb = new StringBuilder();
        sb.append(UUID.randomUUID().toString());
        sb.append(".jpg");
        UCrop withOptions = UCrop.m164of(uri, Uri.fromFile(new File(tmpDir, sb.toString()))).withOptions(options2);
        int i = this.width;
        if (i > 0) {
            int i2 = this.height;
            if (i2 > 0) {
                withOptions.withAspectRatio((float) i, (float) i2);
            }
        }
        withOptions.start(activity);
    }

    private void imagePickerResult(Activity activity, int i, int i2, Intent intent) {
        if (i2 == 0) {
            this.resultCollector.notifyProblem(E_PICKER_CANCELLED_KEY, E_PICKER_CANCELLED_MSG);
        } else if (i2 == -1) {
            if (this.multiple) {
                ClipData clipData = intent.getClipData();
                if (clipData == null) {
                    try {
                        this.resultCollector.setWaitCount(1);
                        getAsyncSelection(activity, intent.getData(), false);
                    } catch (Exception e) {
                        this.resultCollector.notifyProblem(E_NO_IMAGE_DATA_FOUND, e.getMessage());
                    }
                } else {
                    this.resultCollector.setWaitCount(clipData.getItemCount());
                    for (int i3 = 0; i3 < clipData.getItemCount(); i3++) {
                        getAsyncSelection(activity, clipData.getItemAt(i3).getUri(), false);
                    }
                }
            } else {
                Uri data = intent.getData();
                if (data == null) {
                    this.resultCollector.notifyProblem(E_NO_IMAGE_DATA_FOUND, "Cannot resolve image url");
                } else if (this.cropping) {
                    startCropping(activity, data);
                } else {
                    try {
                        getAsyncSelection(activity, data, false);
                    } catch (Exception e2) {
                        this.resultCollector.notifyProblem(E_NO_IMAGE_DATA_FOUND, e2.getMessage());
                    }
                }
            }
        }
    }

    private void cameraPickerResult(Activity activity, int i, int i2, Intent intent) {
        if (i2 == 0) {
            this.resultCollector.notifyProblem(E_PICKER_CANCELLED_KEY, E_PICKER_CANCELLED_MSG);
        } else if (i2 == -1) {
            Uri uri = this.mCameraCaptureURI;
            if (uri == null) {
                this.resultCollector.notifyProblem(E_NO_IMAGE_DATA_FOUND, "Cannot resolve image url");
            } else if (this.cropping) {
                new UCrop.Options().setCompressionFormat(CompressFormat.JPEG);
                startCropping(activity, uri);
            } else {
                try {
                    this.resultCollector.setWaitCount(1);
                    WritableMap selection = getSelection(activity, uri, true);
                    if (selection != null) {
                        this.resultCollector.notifySuccess(selection);
                    }
                } catch (Exception e) {
                    this.resultCollector.notifyProblem(E_NO_IMAGE_DATA_FOUND, e.getMessage());
                }
            }
        }
    }

    private void croppingResult(Activity activity, int i, int i2, Intent intent) {
        if (intent != null) {
            Uri output = UCrop.getOutput(intent);
            if (output != null) {
                try {
                    if (this.width > 0 && this.height > 0) {
                        output = Uri.fromFile(this.compression.resize(output.getPath(), this.width, this.height, 100));
                    }
                    WritableMap selection = getSelection(activity, output, false);
                    if (selection != null) {
                        selection.putMap("cropRect", getCroppedRectMap(intent));
                        this.resultCollector.setWaitCount(1);
                        this.resultCollector.notifySuccess(selection);
                        return;
                    }
                    throw new Exception("Cannot crop video files");
                } catch (Exception e) {
                    this.resultCollector.notifyProblem(E_NO_IMAGE_DATA_FOUND, e.getMessage());
                }
            } else {
                this.resultCollector.notifyProblem(E_NO_IMAGE_DATA_FOUND, "Cannot find image data");
            }
        } else {
            this.resultCollector.notifyProblem(E_PICKER_CANCELLED_KEY, E_PICKER_CANCELLED_MSG);
        }
    }

    public void onActivityResult(Activity activity, int i, int i2, Intent intent) {
        if (i == IMAGE_PICKER_REQUEST) {
            imagePickerResult(activity, i, i2, intent);
        } else if (i == CAMERA_PICKER_REQUEST) {
            cameraPickerResult(activity, i, i2, intent);
        } else if (i == 69) {
            croppingResult(activity, i, i2, intent);
        }
    }

    private boolean isCameraAvailable(Activity activity) {
        return activity.getPackageManager().hasSystemFeature("android.hardware.camera") || activity.getPackageManager().hasSystemFeature("android.hardware.camera.any");
    }

    private File createImageFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("image-");
        sb.append(UUID.randomUUID().toString());
        String sb2 = sb.toString();
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!externalStoragePublicDirectory.exists() && !externalStoragePublicDirectory.isDirectory()) {
            externalStoragePublicDirectory.mkdirs();
        }
        File createTempFile = File.createTempFile(sb2, ".jpg", externalStoragePublicDirectory);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("file:");
        sb3.append(createTempFile.getAbsolutePath());
        this.mCurrentMediaPath = sb3.toString();
        return createTempFile;
    }

    private File createVideoFile() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("video-");
        sb.append(UUID.randomUUID().toString());
        String sb2 = sb.toString();
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!externalStoragePublicDirectory.exists() && !externalStoragePublicDirectory.isDirectory()) {
            externalStoragePublicDirectory.mkdirs();
        }
        File createTempFile = File.createTempFile(sb2, ".mp4", externalStoragePublicDirectory);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("file:");
        sb3.append(createTempFile.getAbsolutePath());
        this.mCurrentMediaPath = sb3.toString();
        return createTempFile;
    }

    private static WritableMap getCroppedRectMap(Intent intent) {
        WritableNativeMap writableNativeMap = new WritableNativeMap();
        writableNativeMap.putInt("x", intent.getIntExtra(UCrop.EXTRA_OUTPUT_OFFSET_X, -1));
        writableNativeMap.putInt("y", intent.getIntExtra(UCrop.EXTRA_OUTPUT_OFFSET_Y, -1));
        writableNativeMap.putInt("width", intent.getIntExtra(UCrop.EXTRA_OUTPUT_IMAGE_WIDTH, -1));
        writableNativeMap.putInt("height", intent.getIntExtra(UCrop.EXTRA_OUTPUT_IMAGE_HEIGHT, -1));
        return writableNativeMap;
    }
}
