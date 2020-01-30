package p006fr.bamlab.rnimageresizer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;
import com.facebook.imagepipeline.common.RotationOptions;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/* renamed from: fr.bamlab.rnimageresizer.ImageResizer */
public class ImageResizer {
    private static final String IMAGE_JPEG = "image/jpeg";
    private static final String IMAGE_PNG = "image/png";
    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_DATA = "data";
    private static final String SCHEME_FILE = "file";

    private static Bitmap resizeImage(Bitmap bitmap, int i, int i2) {
        Bitmap bitmap2 = null;
        if (bitmap == null) {
            return null;
        }
        if (i2 > 0 && i > 0) {
            float width = (float) bitmap.getWidth();
            float height = (float) bitmap.getHeight();
            float min = Math.min(((float) i) / width, ((float) i2) / height);
            try {
                bitmap2 = Bitmap.createScaledBitmap(bitmap, (int) (width * min), (int) (height * min), true);
            } catch (OutOfMemoryError unused) {
                return null;
            }
        }
        return bitmap2;
    }

    public static Bitmap rotateImage(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        try {
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError unused) {
            return null;
        }
    }

    private static File saveImage(Bitmap bitmap, File file, String str, CompressFormat compressFormat, int i) throws IOException {
        if (bitmap != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(".");
            sb.append(compressFormat.name());
            File file2 = new File(file, sb.toString());
            if (file2.createNewFile()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(compressFormat, i, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(byteArray);
                fileOutputStream.flush();
                fileOutputStream.close();
                return file2;
            }
            throw new IOException("The file already exists");
        }
        throw new IOException("The bitmap couldn't be resized");
    }

    private static File getFileFromUri(Context context, Uri uri) {
        File file;
        File file2 = new File(uri.getPath());
        if (file2.exists()) {
            return file2;
        }
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            int columnIndexOrThrow = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            file = new File(cursor.getString(columnIndexOrThrow));
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception unused) {
            if (cursor != null) {
                cursor.close();
            }
            file = file2;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        return file;
    }

    public static int getOrientation(Context context, Uri uri) {
        try {
            File fileFromUri = getFileFromUri(context, uri);
            if (fileFromUri.exists()) {
                return getOrientation(new ExifInterface(fileFromUri.getAbsolutePath()));
            }
        } catch (Exception unused) {
        }
        return 0;
    }

    public static int getOrientation(ExifInterface exifInterface) {
        int attributeInt = exifInterface.getAttributeInt("Orientation", 1);
        if (attributeInt == 3) {
            return RotationOptions.ROTATE_180;
        }
        if (attributeInt == 6) {
            return 90;
        }
        if (attributeInt != 8) {
            return 0;
        }
        return RotationOptions.ROTATE_270;
    }

    private static int calculateInSampleSize(Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            int i6 = i3 / 2;
            int i7 = i4 / 2;
            while (i6 / i5 >= i2 && i7 / i5 >= i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    private static Bitmap loadBitmap(Context context, Uri uri, Options options) throws IOException {
        String scheme = uri.getScheme();
        if (scheme == null || !scheme.equalsIgnoreCase("content")) {
            try {
                return BitmapFactory.decodeFile(uri.getPath(), options);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Error decoding image file");
            }
        } else {
            InputStream openInputStream = context.getContentResolver().openInputStream(uri);
            if (openInputStream == null) {
                return null;
            }
            Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream, null, options);
            openInputStream.close();
            return decodeStream;
        }
    }

    private static Bitmap loadBitmapFromFile(Context context, Uri uri, int i, int i2) throws IOException {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        loadBitmap(context, uri, options);
        options.inSampleSize = calculateInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        System.out.println(options.inSampleSize);
        return loadBitmap(context, uri, options);
    }

    private static Bitmap loadBitmapFromBase64(Uri uri) {
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        int indexOf = schemeSpecificPart.indexOf(44);
        if (indexOf != -1) {
            String lowerCase = schemeSpecificPart.substring(0, indexOf).replace('\\', '/').toLowerCase();
            boolean startsWith = lowerCase.startsWith(IMAGE_JPEG);
            boolean z = !startsWith && lowerCase.startsWith(IMAGE_PNG);
            if (startsWith || z) {
                byte[] decode = Base64.decode(schemeSpecificPart.substring(indexOf + 1), 0);
                return BitmapFactory.decodeByteArray(decode, 0, decode.length);
            }
        }
        return null;
    }

    public static File createResizedImage(Context context, Uri uri, int i, int i2, CompressFormat compressFormat, int i3, int i4, String str) throws IOException {
        String scheme = uri.getScheme();
        Bitmap bitmap = (scheme == null || scheme.equalsIgnoreCase("file") || scheme.equalsIgnoreCase("content")) ? loadBitmapFromFile(context, uri, i, i2) : scheme.equalsIgnoreCase("data") ? loadBitmapFromBase64(uri) : null;
        if (bitmap != null) {
            Bitmap resizeImage = resizeImage(bitmap, i, i2);
            if (bitmap != resizeImage) {
                bitmap.recycle();
            }
            Bitmap rotateImage = rotateImage(resizeImage, (float) (getOrientation(context, uri) + i4));
            if (resizeImage != rotateImage) {
                resizeImage.recycle();
            }
            File cacheDir = context.getCacheDir();
            if (str != null) {
                cacheDir = new File(str);
            }
            File saveImage = saveImage(rotateImage, cacheDir, Long.toString(new Date().getTime()), compressFormat, i3);
            rotateImage.recycle();
            return saveImage;
        }
        throw new IOException("Unable to load source image from path");
    }
}
