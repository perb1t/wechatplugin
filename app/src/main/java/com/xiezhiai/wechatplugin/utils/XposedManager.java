package com.xiezhiai.wechatplugin.utils;

import android.content.Context;
import android.os.Environment;

import com.xiezhiai.wechatplugin.core.Config;
import com.xiezhiai.wechatplugin.utils.file.FileTransfer;


/**
 * Created by shijiwei on 2018/11/19.
 *
 * @Desc:
 */
public class XposedManager {

    public static final String dest = Environment.getExternalStorageDirectory().getAbsolutePath() + Config.EXTERNAL_DIR;

    public static final String[] Xposed = new String[]{
            "de.robv.android.xposed.installer",
            "XposedInstaller.apk"

    };

    public static final String[] VirtualXposed = new String[]{
            "io.va.exposed",
            "VirtualXposed_0.16.1.apk"
    };


    /**
     * 检测设备是否安装了xposed
     *
     * @param context
     */
    public static boolean checkHasInstallXposed(Context context, String pkg) {
        return AppUtil.isInstalledApk(context, pkg);
    }

    /**
     * 安装 Xposed 环境
     *
     * @param context
     * @param apk
     */
    public static void installXposed(final Context context, final String apk, FileTransfer.FileTransferListener callback) {
//        AppUtil.installApk(context, dest + "/" + apk);
        FileTransfer.assetsTransfer(context, apk, dest, callback, true);
    }

}
