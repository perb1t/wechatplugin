package com.xiezhiai.wechatplugin.core;


import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.func.transfer.PluginMessasge;
import com.xiezhiai.wechatplugin.func.transfer.PluginServer;
import com.xiezhiai.wechatplugin.model.wechat.Wechat;
import com.xiezhiai.wechatplugin.utils.AppUtil;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.ReflectionUtil;
import com.xiezhiai.wechatplugin.utils.ShellUtils;
import com.xiezhiai.wechatplugin.utils.encrypt.MD5;
import com.xiezhiai.wechatplugin.utils.file.FileTransfer;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.DexClass;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by shijiwei on 2018/10/12.
 *
 * @Desc:
 */
public class Config {

    public static final String EXTERNAL_DIR = "/XieZhiAI_WechatPlugin";
    public static Wechat xWechat;
    private static ExecutorService loader = Executors.newSingleThreadExecutor();

    static {
        xWechat = new Wechat();
    }

    public static boolean init(XC_LoadPackage.LoadPackageParam lpparam) {

        xWechat.versionName = AppUtil.getVersionName(AppUtil.getSystemContext(), lpparam.packageName);
        LogUtil.log("系统检测到微信版本 V_" + xWechat.versionName + "  " + lpparam.processName);
        if (!xWechat.versionName.equals("6.6.6")) {
            return false;
        }
        loadWXClass(lpparam);
//        loadWXLastLoginUser();
        int versionNumber = AppUtil.getVersionNumber(xWechat.versionName);
        /* 初始化数据库相关类 */
        if (versionNumber < AppUtil.getVersionNumber("6.5.8")) {
            xWechat.WECHAT_PACKAGE_SQLITE = "com.tencent.mmdb";
        } else {
            xWechat.WECHAT_PACKAGE_SQLITE = "com.tencent.wcdb";
        }
        xWechat.SQLiteDatabase = XposedHelpers.findClass(xWechat.WECHAT_PACKAGE_SQLITE + ".database.SQLiteDatabase", lpparam.classLoader);
        xWechat.SQLiteCursorFactory = XposedHelpers.findClass(xWechat.WECHAT_PACKAGE_SQLITE + ".database.SQLiteDatabase$CursorFactory", lpparam.classLoader);
        xWechat.DatabaseErrorHandler = XposedHelpers.findClass(xWechat.WECHAT_PACKAGE_SQLITE + ".DatabaseErrorHandler", lpparam.classLoader);
        xWechat.CancellationSignal = XposedHelpers.findClass(xWechat.WECHAT_PACKAGE_SQLITE + ".support.CancellationSignal", lpparam.classLoader);

        /* 初始化红包相关类 */
        if (versionNumber >= AppUtil.getVersionNumber("6.5.6") && versionNumber <= AppUtil.getVersionNumber("6.5.23")) {
            xWechat.LuckyMoneyReceiveUI = XposedHelpers.findClass("com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f", lpparam.classLoader);
        } else {
            xWechat.LuckyMoneyReceiveUI = XposedHelpers.findClass("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI", lpparam.classLoader);
        }
        xWechat.ReceiveUIParamName = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, xWechat.wxClasses, "com.tencent.mm", 1)
                .filterByMethod(String.class, "getInfo")
                .filterByMethod(int.class, "getType")
                .filterByMethod(void.class, "reset")
                .firstOrNull();

        xWechat.ReceiveUIMethod = ReflectionUtil.findMethodsByExactParameters(xWechat.LuckyMoneyReceiveUI,
                boolean.class, int.class, int.class, String.class, xWechat.ReceiveUIParamName)
                .getName();

        xWechat.RequestCaller = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, xWechat.wxClasses, "com.tencent.mm", 1)
                .filterByField("foreground", "boolean")
                .filterByMethod(void.class, int.class, String.class, int.class, boolean.class)
                .filterByMethod(void.class, "cancel", int.class)
                .filterByMethod(void.class, "reset")
                .firstOrNull();
        xWechat.RequestCallerMethod = ReflectionUtil.findMethodsByExactParameters(xWechat.RequestCaller,
                void.class, xWechat.RequestCaller, int.class)
                .getName();

        xWechat.LuckyMoneyRequest = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, xWechat.wxClasses, "com.tencent.mm.plugin.luckymoney", 1)
                .filterByField("talker", "java.lang.String")
                .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                .filterByMethod(int.class, "getType")
                .filterByNoMethod(boolean.class)
                .firstOrNull();


        xWechat.ReceiveLuckyMoneyRequest = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, xWechat.wxClasses, "com.tencent.mm.plugin.luckymoney", 1)
                .filterByField("msgType", "int")
                .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                .firstOrNull();
        xWechat.ReceiveLuckyMoneyRequestMethod = ReflectionUtil.findMethodsByExactParameters(xWechat.ReceiveLuckyMoneyRequest,
                void.class, int.class, String.class, JSONObject.class).getName();

        xWechat.GetTransferRequest = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, xWechat.wxClasses, "com.tencent.mm.plugin.remittance", 1)
                .filterByField("java.lang.String")
                .filterByNoField("int")
                .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                .filterByMethod(String.class, "getUri")
                .firstOrNull();

        xWechat.NetworkRequest = ReflectionUtil.findClassesFromPackage(lpparam.classLoader, xWechat.wxClasses, "com.tencent.mm", 1)
                .filterByMethod(void.class, "unhold")
                .filterByMethod(xWechat.RequestCaller)
                .firstOrNull();
        xWechat.GetNetworkByModelMethod = ReflectionUtil.findMethodsByExactParameters(xWechat.NetworkRequest,
                xWechat.RequestCaller)
                .getName();

        if (versionNumber < AppUtil.getVersionNumber("6.5.4"))
            xWechat.hasTimingIdentifier = false;


        /*通讯录*/
        xWechat.AddressAdapter = XposedHelpers.findClass(Wechat.PACKAGE_NAME + ".ui.contact.a", lpparam.classLoader);
        /*会话列表*/
        xWechat.ConversationWithCacheAdapter = XposedHelpers.findClass(Wechat.PACKAGE_NAME + ".ui.conversation.g", lpparam.classLoader);

//        loaderHandler.sendEmptyMessageDelayed(0, 0);
        return true;
    }


    /**
     * 导入微信最后一次登录的用户信息
     */
    public static void loadWXLastLoginUser() {

        XSharedPreferences xsp = new XSharedPreferences(xWechat.PACKAGE_NAME, xWechat.MM_PREFERENCES);
        xsp.makeWorldReadable();
        xWechat.loginUser.setUserNickName(xsp.getString("last_login_nick_name", "NULL_error"));
        xWechat.loginUser.setUserName(xsp.getString("login_weixin_username", "NULL_error"));
        xWechat.loginUser.setAvatarPath(xsp.getString("last_avatar_path", "NULL_error"));
        xWechat.loginUser.setLogin(xsp.getBoolean("isLogin", false));

//        File avatar = new File(xWechat.loginUser.getAvatarPath());
//        if (avatar.exists()) {
//            avatar.setReadable(true, false);
//        }
//        xWechat.loginUser.setAvatarName(avatar.getName());
//        LogUtil.log("微信登录用户头像文件是否存在： " + avatar.exists());

        XSharedPreferences authXsp = new XSharedPreferences(xWechat.PACKAGE_NAME, xWechat.AUTH_INFO_KEY_PREFS);
        authXsp.makeWorldReadable();
        xWechat.loginUser.setUinEnc(MD5.getMD5Str("mm" + authXsp.getInt("_auth_uin", -1)));

        generateCacheDir();
//        copyWechatFile2SDCard(xWechat.loginUser.getAvatarPath(), " /sdcard" + EXTERNAL_DIR);
//        FileTransfer.copyFile(xWechat.loginUser.getAvatarPath(), Environment.getExternalStorageDirectory() + EXTERNAL_DIR + "/" + avatar.getName());
        PluginHandler.wxLastLoginUser.copy(xWechat.loginUser);

        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            PluginServer.tansferMessage(new PluginMessasge(PluginMessasge.NOTIFY_WECHAT_LOGINED, xWechat.loginUser));
        }

        LogUtil.log("微信登录用户："
                + xWechat.loginUser.getUserNickName() + " "
                + xWechat.loginUser.getUserName() + " "
                + xWechat.loginUser.getUinEnc() + " "
                + xWechat.loginUser.getAvatarPath()
        );


    }


    /**
     * 加载微信apk文件class列表
     *
     * @param lpparam
     */
    public static void loadWXClass(final XC_LoadPackage.LoadPackageParam lpparam) {

        ApkFile apkFile = null;
        try {
            apkFile = new ApkFile(lpparam.appInfo.sourceDir);
            DexClass[] dexClasses = apkFile.getDexClasses();
            for (int i = 0; i < dexClasses.length; i++) {
                xWechat.wxClasses.add(ReflectionUtil.getClassName(dexClasses[i]));
            }
        } catch (Error | Exception e) {
        } finally {
            try {
                apkFile.close();
            } catch (Exception e) {
            }
        }

    }

    /**
     * @param source
     * @param destination
     */
    private static void copyWechatFile2SDCard(String source, String destination) {
        File sourceFile = new File(source);
        if (sourceFile.exists()) {
            sourceFile.setReadable(true, false);
            ShellUtils.execCmd("cp " + source + destination, false);
        }
    }

    /**
     * 创建缓存文件
     */
    private static void generateCacheDir() {
        File cacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + EXTERNAL_DIR);
        if (!cacheDir.exists()) cacheDir.mkdirs();
    }

}
