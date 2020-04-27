package com.xiezhiai.wechatplugin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shijiwei on 2016/5/7.
 * <p>
 * **用于处理图片的工具类
 */
public class BitmapUtil {

    /**
     * 将Bitmap转换成流
     */
    public static ByteArrayOutputStream Bitmap2Stream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos;
    }


    /**
     * 将Bitmap转换成数组
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        return Bitmap2Stream(bm).toByteArray();
    }

    /**
     * 将Bitmap保存到本地
     */

    public static File Bitmap2File(Bitmap bm, String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            file.mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(Bitmap2Bytes(bm));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 将数组转换为Bitmap
     */
    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 将文件转换成Bitmap
     */

    public static Bitmap File2Bitmap(File file) {
        if (file != null) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            return null;
        }
    }

    /**
     * 将流转换成Bitmap
     */

    public static Bitmap Stram2Bitmap(InputStream is) {
        if (is != null) {
            return BitmapFactory.decodeStream(is);
        } else {
            return null;
        }
    }


    /**
     * * * 压缩本地图片，用于上传网络
     */
    public static InputStream compress(File file) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  // 将图片读入内存的时候只读取边框信息
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int height = options.outHeight;
        int width = options.outWidth;
        float h = 800f;
        float w = 480f;
        int inSampleSize = 1; //等比例压缩倍数
        if (height > h || width > w) {
            int inSampleSizeH = (int) (height / h) + 1;
            int inSampleSizeW = (int) (width / w) + 1;
            inSampleSize = Math.max(inSampleSizeH, inSampleSizeW);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, bos);
        int size = 100;
        float zoom = (float) Math.sqrt(size * 1024 / (float) bos.toByteArray().length);
        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);
        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bos.reset();
        result.compress(Bitmap.CompressFormat.PNG, 85, bos);
        while (bos.toByteArray().length > size * 1024) {
            matrix.setScale(0.9f, 0.9f);
            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
            bos.reset();
            result.compress(Bitmap.CompressFormat.PNG, 85, bos);
        }
        InputStream resultIs = new ByteArrayInputStream(bos.toByteArray());
        Log.d("compress", "压缩图片大小：" + bos.toByteArray().length / 1024 + "K");
        return resultIs;
    }


    /**
     * 切成圆形图片
     */
    public static Bitmap circleCrop(Bitmap source, boolean isCircleBackgound) {

        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        Paint bg = new Paint();
        bg.setColor(Color.parseColor("#ffffff"));
        bg.setAntiAlias(true);
        float r = size / 2f;
        if (isCircleBackgound) {
            canvas.drawCircle(r, r, r, bg);
            canvas.drawCircle(r, r, r - r / 25, paint);
        } else {
            canvas.drawCircle(r, r, r, paint);
        }
        return result;
    }

    /**
     * 转换为圆角图片
     */
    public static Bitmap roundCorner(Bitmap source, int scale) {

        if (source == null) return source;

        Bitmap result = Bitmap.createBitmap(source.getWidth(),
                source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, source.getWidth(), source.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = source.getWidth() / scale;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, rect, rect, paint);

        return result;
    }


    /**
     * 读取图片属性：旋转的角度
     */
    public static int getPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     */
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

}