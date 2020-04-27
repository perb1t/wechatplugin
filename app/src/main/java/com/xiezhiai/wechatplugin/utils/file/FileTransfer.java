package com.xiezhiai.wechatplugin.utils.file;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xiezhiai.wechatplugin.core.Config;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.encrypt.MD5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shijiwei on 2018/11/19.
 *
 * @Desc:
 */
public class FileTransfer {

    public static final String dest = Environment.getExternalStorageDirectory().getAbsolutePath() + Config.EXTERNAL_DIR;
    private static ExecutorService looper = Executors.newSingleThreadExecutor();

    public static final int FLAG_COPYING = 100;
    public static final int FLAG_COPY_COMPLETE = 101;
    public static final int FLAG_WRITE_COMPLETE = 201;
    public static final int FLAG_READ_COMPLETE = 301;

    public static final String KEY_CALL_BACK = "callback";
    public static final String KEY_PROGRESS = "progress";
    public static final String KEY_SUCCESS = "success";

    private static Handler transferHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = (Bundle) msg.obj;
            FileTransferListener callback = (FileTransferListener) bundle.getSerializable(KEY_CALL_BACK);
            switch (msg.what) {
                case FLAG_COPY_COMPLETE:
                    boolean success = bundle.getBoolean(KEY_SUCCESS);
                    if (callback != null) callback.complete(success, null);
                    break;
                case FLAG_COPYING:
                    int progress = bundle.getInt(KEY_PROGRESS);
                    if (callback != null) callback.copy(progress);
                    break;
                case FLAG_WRITE_COMPLETE:
                    boolean writeSuccess = bundle.getBoolean(KEY_SUCCESS);
                    if (callback != null) callback.complete(writeSuccess, null);
                    break;
                case FLAG_READ_COMPLETE:
                    break;

            }
        }
    };

    /**
     * 从 Assets 复制文件到 Sdcard
     *
     * @param context
     * @param source
     * @param dest
     * @param callback
     */
    public static void assetsTransfer(Context context, String source, String dest, FileTransferListener callback, boolean deleteOld) {
        looper.execute(new AssetsTransfer(context, dest, source, callback, deleteOld));
    }

    public interface FileTransferListener extends Serializable {

        void copy(int progress);

        void complete(boolean success, Object bundle);
    }

    static class AssetsTransfer implements Runnable {

        private Context context;
        private String dest;
        private String source;
        private FileTransferListener callback;
        private boolean deleteOld;

        public AssetsTransfer(Context context, String dest, String source, FileTransferListener callback, boolean deleteOld) {
            this.context = context;
            this.dest = dest;
            this.source = source;
            this.callback = callback;
            this.deleteOld = deleteOld;
        }

        @Override
        public void run() {
            File dest = new File(this.dest);
            File source = new File(this.dest, this.source);
            if (!dest.exists()) {
                dest.mkdirs();
            }

            if (this.deleteOld && source.exists()) source.delete();

            if (!source.exists()) {
                byte[] buffer = new byte[1024];
                InputStream ins;
                FileOutputStream fouts;
                try {
                    ins = context.getAssets().open(this.source);
                    fouts = new FileOutputStream(source);
                    int len;
                    long totalLength = ins.available();
                    float copyLength = 0;
                    while ((len = ins.read(buffer)) != -1) {
                        fouts.write(buffer, 0, len);
                        copyLength += len;

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(KEY_CALL_BACK, callback);
                        bundle.putInt(KEY_PROGRESS, (int) (copyLength / totalLength * 100));
                        transferHandler.sendMessage(transferHandler.obtainMessage(FLAG_COPYING, bundle));

                    }
                    ins.close();
                    fouts.close();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_CALL_BACK, callback);
                    bundle.putBoolean(KEY_SUCCESS, true);
                    transferHandler.sendMessage(transferHandler.obtainMessage(FLAG_COPY_COMPLETE, bundle));

                } catch (IOException e) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_CALL_BACK, callback);
                    bundle.putBoolean(KEY_SUCCESS, false);
                    transferHandler.sendMessage(transferHandler.obtainMessage(FLAG_COPY_COMPLETE, bundle));

                }
            } else {

                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_CALL_BACK, callback);
                bundle.putBoolean(KEY_SUCCESS, true);
                transferHandler.sendMessage(transferHandler.obtainMessage(FLAG_COPY_COMPLETE, bundle));

            }
        }
    }


    /**
     * @param content
     * @param path
     * @param callback
     */
    public static void write(String content, String path, FileTransferListener callback) {
        looper.execute(new Writer(content, path, callback));
    }

    public static void read() {
        looper.execute(new Reader(WechatFile.contacts.getPath(), null));
    }

    public enum WechatFile {
        chatrooms,
        contacts,
        contactsLabels;

        public String getPath() {
            return dest + "/" + MD5.getMD5Str(name());
        }
    }

    /**
     * 文件存写
     */
    static class Writer implements Runnable {

        private String path;
        private String content;
        private FileTransferListener callback;

        public Writer(String content, String path, FileTransferListener callback) {
            this.path = path;
            this.content = content;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                File target = new File(path);
                FileWriter fw = new FileWriter(target);
                fw.write(content);
                fw.flush();
                fw.close();

                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_CALL_BACK, callback);
                bundle.putBoolean(KEY_SUCCESS, true);
                transferHandler.sendMessage(transferHandler.obtainMessage(FLAG_WRITE_COMPLETE, bundle));

            } catch (IOException e) {

                Bundle bundle = new Bundle();
                bundle.putSerializable(KEY_CALL_BACK, callback);
                bundle.putBoolean(KEY_SUCCESS, false);
                transferHandler.sendMessage(transferHandler.obtainMessage(FLAG_WRITE_COMPLETE, bundle));

            }
        }
    }


    /**
     * 文件读取
     */
    static class Reader implements Runnable {

        private String path;
        private FileTransferListener callback;

        public Reader(String path, FileTransferListener callback) {
            this.path = path;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                File target = new File(path);
                char[] buf = new char[1024];
                StringBuffer buffer = new StringBuffer();
                FileReader reader = new FileReader(target);
                int len;
                while ((len = reader.read(buf)) != -1) {
                    buffer.append(buf, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public static boolean copyFile(String oldPathName, String newPathName) {
        try {
            File oldFile = new File(oldPathName);
            if (!oldFile.exists()) {
                LogUtil.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                LogUtil.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            }
            /* 如果不需要打log，可以使用下面的语句
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            */
            oldFile.setReadable(true,false);
            FileInputStream fileInputStream = new FileInputStream(oldPathName);
            FileOutputStream fileOutputStream = new FileOutputStream(newPathName);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
