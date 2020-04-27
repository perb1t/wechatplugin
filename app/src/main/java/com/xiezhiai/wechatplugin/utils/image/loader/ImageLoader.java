package com.xiezhiai.wechatplugin.utils.image.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.image.SSL.TrustAllTrustManager;
import com.xiezhiai.wechatplugin.utils.image.cache.Cache;
import com.xiezhiai.wechatplugin.utils.image.cache.MemoryCache;
import com.xiezhiai.wechatplugin.utils.image.cache.disk.DiskLruCacheHelper;
import com.xiezhiai.wechatplugin.utils.image.cache.disk.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by shijiwei on 2018/11/1.
 *
 * @Desc:
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";

    /* image cache */
    private static Cache memoryCache;
    private static DiskLruCacheHelper diskCache;

    private ExecutorService loaderPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static Context ctx;
    private static ImageLoader imageLoader;

    public static ImageLoader with(Context context) {
        ctx = context;
        if (imageLoader == null) {
            imageLoader = new ImageLoader(ctx);
        }
        return imageLoader;
    }

    private ImageLoader(Context ctx) {
        this.ctx = ctx;
        try {
            memoryCache = new MemoryCache();
            diskCache = new DiskLruCacheHelper(ctx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据key从缓存中拿图片
     *
     * @param key
     * @return
     */
    public static Bitmap get(String key) {
        Bitmap iamge = memoryCache == null ? null : memoryCache.get(key);
        if (iamge == null) {
            iamge = diskCache == null ? null : diskCache.getAsBitmap(key);
            if (iamge != null) memoryCache.put(key, iamge);
        }
        return iamge;
    }

    /**
     * 往缓存中存图片
     *
     * @param key
     * @param bitmap
     */
    public static void put(String key, Bitmap bitmap) {
        if (memoryCache != null)
            memoryCache.put(key, bitmap);
        if (diskCache != null)
            diskCache.put(key, bitmap);
    }

    /**
     * 移除key对应的缓存
     *
     * @param key
     */
    public static void remove(String key) {
        if (key == null) return;
        if (memoryCache != null)
            memoryCache.remove(key);
        if (diskCache != null)
            diskCache.remove(key);
    }

    public Bitmap get(int resId) {
        return get(formatResource(ctx, resId));
    }

    public void put(int resId, Bitmap bitmap) {
        put(formatResource(ctx, resId), bitmap);
    }

    /**
     * 格式 RES 资源文件ID 为字符串类型
     *
     * @param ctx
     * @param resId
     * @return
     */
    private String formatResource(Context ctx, int resId) {
        String resType = ctx.getResources().getResourceTypeName(resId);
        String resName = ctx.getResources().getResourceEntryName(resId);
        return "R." + resType + "." + resName;
    }


    private boolean isHttpUri(String path) {
        return path.startsWith("http");
    }

    /**
     * @param path
     * @param iv
     */

    public void load(String path, ImageView iv) {
        load(path, iv, R.mipmap.ic_photo_placeholder);
    }

    public void load(String path, ImageView iv, int placeHolder) {
        if (iv == null) throw new NullPointerException("ImageView is null");
        if (path == null) path = "";
        loaderPool.execute(new LoaderExecutor(path, iv, placeHolder));
    }


    class LoaderExecutor implements Runnable {

        private String path;
        private ImageView iv;
        private int placeHolder;

        public LoaderExecutor(String path, ImageView iv, int placeHolder) {
            this.path = path;
            this.iv = iv;
            this.placeHolder = placeHolder;
        }

        @Override
        public void run() {
            Bitmap bm = get(path);
            if (bm != null) {
                imageLoaderHandler.sendMessage(imageLoaderHandler.obtainMessage(SUCCESS, new ImageMessage(bm, path, "image/png", iv, placeHolder)));
                ImageLoader.put(path, bm);
            } else {
                if (isHttpUri(path)) {
                    try {
                        TrustManager[] trustAllCerts = new TrustManager[1];
                        trustAllCerts[0] = new TrustAllTrustManager();
                        SSLContext sc = SSLContext.getInstance("SSL");
                        sc.init(null, trustAllCerts, null);
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                        URL url = new URL(path);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.connect();
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            String mimeType = conn.getHeaderField("Content-Type");
                            if (mimeType != null && mimeType.startsWith("image/")) {
                                InputStream is = conn.getInputStream();
                                ByteArrayOutputStream os = new ByteArrayOutputStream();
                                byte[] buf = new byte[1024];
                                int len;
                                while ((len = is.read(buf)) != -1) {
                                    os.write(buf, 0, len);
                                }
                                byte[] buffer = os.toByteArray();
                                BitmapFactory.Options opt = new BitmapFactory.Options();
                                opt.outMimeType = "image/png";
                                Bitmap image = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, opt);
                                imageLoaderHandler.sendMessage(imageLoaderHandler.obtainMessage(SUCCESS, new ImageMessage(image, path, mimeType, iv, placeHolder)));
                                ImageLoader.put(path, image);
                                if (is != null) is.close();
                                if (os != null) os.close();
                                if (conn != null) conn.disconnect();
                            } else {
                                imageLoaderHandler.sendMessage(imageLoaderHandler.obtainMessage(FAILED, new ImageMessage(null, null, null, iv, placeHolder)));
                            }
                            LogUtil.e(TAG, " Content-Type : " + mimeType);
                        } else {
                            imageLoaderHandler.sendMessage(imageLoaderHandler.obtainMessage(FAILED, new ImageMessage(null, null, null, iv, placeHolder)));
                        }
                    } catch (Exception e) {
                        imageLoaderHandler.sendMessage(imageLoaderHandler.obtainMessage(FAILED, new ImageMessage(null, null, null, iv, placeHolder)));
                    }
                } else {
                    File imageFile = new File(path);
                    if (imageFile.exists()) {
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(path, opts);
                        int sampleSize = Utils.calculateInSampleSize(opts, iv.getWidth(), iv.getHeight());
                        opts.inSampleSize = sampleSize;
                        opts.inJustDecodeBounds = false;
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        if (bitmap != null) {
                            imageLoaderHandler.sendMessage(imageLoaderHandler.obtainMessage(SUCCESS, new ImageMessage(bitmap, path, "image/png", iv, placeHolder)));
                            ImageLoader.put(path, bitmap);
                        } else {
                            imageLoaderHandler.sendMessage(imageLoaderHandler.obtainMessage(FAILED, new ImageMessage(null, null, null, iv, placeHolder)));
                        }
                    } else {
                        imageLoaderHandler.sendMessage(imageLoaderHandler.obtainMessage(FAILED, new ImageMessage(null, null, null, iv, placeHolder)));
                    }

                }
            }

        }
    }


    public static final int SUCCESS = 1;
    public static final int FAILED = 0;

    private static Handler imageLoaderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ImageMessage image = (ImageMessage) msg.obj;
            switch (msg.what) {
                case SUCCESS:
                    if (image != null) {
                        if (image.image != null) {
                            image.iv.setImageBitmap(image.image);
                        } else {
                            image.iv.setImageResource(image.placeHolder);
                        }
                    }
                    break;
                case FAILED:
                    image.iv.setImageResource(image.placeHolder);
                    break;
            }


        }
    };


    public static class ImageMessage {
        public Bitmap image;
        public String path;
        public String mimeType;
        public ImageView iv;
        public int placeHolder;


        public ImageMessage(Bitmap image, String path, String mimeType, ImageView iv, int placeHolder) {
            this.image = image;
            this.path = path;
            this.mimeType = mimeType;
            this.iv = iv;
            this.placeHolder = placeHolder;
        }
    }

}
