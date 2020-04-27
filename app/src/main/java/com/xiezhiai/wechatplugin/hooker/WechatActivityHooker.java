package com.xiezhiai.wechatplugin.hooker;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSON;
import com.xiezhiai.wechatplugin.core.Config;
import com.xiezhiai.wechatplugin.func.transfer.PluginServer;
import com.xiezhiai.wechatplugin.utils.AccessControl;
import com.xiezhiai.wechatplugin.utils.AppUtil;
import com.xiezhiai.wechatplugin.utils.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * Created by shijiwei on 2018/10/12.
 *
 * @Desc:
 */
public class WechatActivityHooker implements IHooker {

    /**
     * com.tencent.mm.ui.LauncherUI -> com.tencent.mm.plugin.account.ui.LoginPasswordUI
     * <p>
     * com.tencent.mm.plugin.account.ui.MobileInputUI
     * <p>
     * com.tencent.mm.plugin.setting.ui.setting.SettingsSwitchAccountUI
     * <p>
     * com.tencent.mm.plugin.setting.ui.setting.SettingsUI
     * <p>
     * com.tencent.mm.ui.chatting.ChattingUI$a
     * com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI
     */

    private static final String TAG = "WechatActivityHooker";
    private static Object ImageGalleryUI = null;

    private AccessControl loadWXLastLoginUserAccessControl = new AccessControl();
    private static ExecutorService hookTaskPool = Executors.newSingleThreadExecutor();

    private static final int LOAD_WECHAT_PREFERENCE = 100;

    private static Handler hookTaskHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LOAD_WECHAT_PREFERENCE:
                    ContactsHooker.resetFlag();
                    Config.loadWXLastLoginUser();
                    break;
            }
        }
    };

    @Override
    public void hook(final XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtil.log("微信当前界面_Application：" + param.thisObject.getClass().getName());
                /* 启动微信的时候调用 */
            }
        });

        XposedHelpers.findAndHookMethod(Activity.class, "onStart", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                String uiPath = param.thisObject.getClass().getName();
                Class<?> acls = param.thisObject.getClass();
                LogUtil.log("微信当前界面 onStart ：" + uiPath + "  cls: " + acls.getName() + "   superclas: " + acls.getSuperclass().getName());
                if (uiPath.equals("com.tencent.mm.ui.LauncherUI")) {
                    WechatHooker.initialNoHttp(AndroidAppHelper.currentApplication());
                    PluginServer.start();
                } else if (uiPath.equals("com.tencent.mm.plugin.sns.ui.SnsUserUI")) {
                    /* 获取朋友圈info */
                    SnsHooker.getSns();
//                    Class<?> cls_am_n = XposedHelpers.findClass("com.tencent.mm.am.n", lpparam.classLoader);
//                    ArrayList<String> photoURLs = new ArrayList<>();
//                    photoURLs.add("/storage/emulated/0/DCIM/Screenshots/Screenshot_20180928-164029.jpg");
//                    photoURLs.add("/storage/emulated/0/DCIM/Screenshots/Screenshot_20180928-093310_WeChat.jpg");
//                    XposedHelpers.callStaticMethod(cls_am_n, "a", photoURLs, true, 0, 0, "wxid_j2nzug3sjt0t22",2130837989);

                    ArrayList<String> sendToUsers = new ArrayList<>();
                    sendToUsers.add("wxid_j2nzug3sjt0t22");
//                    SendImageHooker.put(new SendImageHooker.Entity(sendToUsers,"/storage/emulated/0/DCIM/Screenshots/Screenshot_20180927-151808_Chrome.jpg"));
//                    SendVoiceHooker.sendVoice3("5817251225183b4d15b488b102", 852);
                } else if (uiPath.equals("com.tencent.mm.ui.chatting.gallery.ImageGalleryUI")) {
                    ImageGalleryUI = param.thisObject;
                } else if (uiPath.equals("com.tencent.mm.plugin.gallery.ui.AlbumPreviewUI")
                        || uiPath.equals("com.tencent.mm.plugin.gallery.ui.ImagePreviewUI")
                        || uiPath.equals("com.tencent.mm.ui.chatting.SendImgProxyUI")
                        || uiPath.equals("com.tencent.mm.ui.chatting.gallery.ImageGalleryUI")
                        || uiPath.equals("com.tencent.mm.ui.chatting.ChattingUI$a")
                        ) {

                }

                LogUtil.logActivityIntentExtras((Activity) param.thisObject);

            }
        });

        XposedHelpers.findAndHookMethod(Activity.class, "onDestroy", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String uiPath = param.thisObject.getClass().getName();
                Class<?> acls = param.thisObject.getClass();
                LogUtil.log("微信当前界面  onDestroy ：" + uiPath + "  cls: " + acls.getName() + "   superclas: " + acls.getSuperclass().getName());
                if (uiPath.equals("com.tencent.mm.ui.LauncherUI")) {
                    PluginServer.stop();
                }
            }
        });


        XposedHelpers.findAndHookMethod(Service.class, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                LogUtil.log("微信当前界面_Service：" + param.thisObject.getClass().getName());
            }
        });


        XposedBridge.hookAllConstructors(BaseAdapter.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String name = param.thisObject.getClass().getName();
                LogUtil.log("微信当前界面_Adapter：" + name);
                if (name.equals("com.tencent.mm.ui.base.preference.h")) {
                    hookTaskHandler.removeMessages(LOAD_WECHAT_PREFERENCE);
                    hookTaskHandler.sendEmptyMessageDelayed(LOAD_WECHAT_PREFERENCE, 200);
                }
            }
        });


    }

    @Override
    public void release() {

    }


    /**
     * 退出 ImageGalleryUI 界面
     */
    public static void finishImageGalleryUI() {
        if (ImageGalleryUI != null) {
            XposedHelpers.callMethod(ImageGalleryUI, "finish");
        }
    }


}
