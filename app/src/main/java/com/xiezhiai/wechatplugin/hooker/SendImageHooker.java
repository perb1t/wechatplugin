package com.xiezhiai.wechatplugin.hooker;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.xiezhiai.wechatplugin.utils.AppUtil;
import com.xiezhiai.wechatplugin.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by shijiwei on 2018/11/26.
 *
 * @Desc:
 */
public class SendImageHooker implements IHooker {

    private static final String TAG = "SendImageHooker";
    private static LinkedBlockingQueue<Entity> massSendImageQueue = new LinkedBlockingQueue<>();
    private static ExecutorService massSendImagePool = Executors.newSingleThreadExecutor();

    /* Mass Send image Message */
    private static Class<?> cls_z_q;
    private static Class<?> cls_plugin_masssend_a_h;
    private static Class<?> cls_plugin_masssend_a_b;
    private static Class<?> cls_plugin_masssend_a_f;
    private static Class<?> cls_z_au;

    /* 单聊 */
    public static Object obj_chatting_b_v;

    static boolean isLoopMassSendQueue = true;

    static {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isLoopMassSendQueue) {
                    try {
                        final Entity e = massSendImageQueue.poll();
                        if (e == null) continue;
                        massSendImagePool.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(4000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                massSendImage(e.getSendToUsers(), e.getPhotoPath());
                            }
                        });

                    } catch (Exception e) {

                    }
                }

            }
        }).start();
    }

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {


        cls_z_q = XposedHelpers.findClass("com.tencent.mm.z.q", lpparam.classLoader);
        cls_plugin_masssend_a_h = XposedHelpers.findClass("com.tencent.mm.plugin.masssend.a.h", lpparam.classLoader);
        cls_plugin_masssend_a_b = XposedHelpers.findClass("com.tencent.mm.plugin.masssend.a.b", lpparam.classLoader);
        cls_plugin_masssend_a_f = XposedHelpers.findClass("com.tencent.mm.plugin.masssend.a.f", lpparam.classLoader);
        cls_z_au = XposedHelpers.findClass("com.tencent.mm.z.au", lpparam.classLoader);


        Class<?> cls_ChatFooter = XposedHelpers.findClass("com.tencent.mm.pluginsdk.ui.chat.ChatFooter", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(cls_ChatFooter, "a",
                cls_ChatFooter, int.class, String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.logMethodParams(param);
                    }
                });

        Class<?> cls_am_n = XposedHelpers.findClass("com.tencent.mm.am.n", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(cls_am_n, "a",
                ArrayList.class, boolean.class, int.class, int.class, String.class, int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.logMethodParams(param);
                        LogUtil.logStackTraces("com.tencent.mm.am.n.a()", 50, 0);
                    }
                });

        /* 重点 */
        Class<?> cls_chatting_b_v = XposedHelpers.findClass("com.tencent.mm.ui.chatting.b.v", lpparam.classLoader);
        Class<?> cls_chatting_b_p = XposedHelpers.findClass("com.tencent.mm.ui.chatting.b.p", lpparam.classLoader);
        XposedHelpers.findAndHookConstructor(cls_chatting_b_v, cls_chatting_b_p, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                obj_chatting_b_v = param.thisObject;

                LogUtil.e(" CALL cls_chatting_b_v 构造函数 " + obj_chatting_b_v);
            }
        });
        XposedHelpers.findAndHookMethod(cls_chatting_b_v, "u",
                int.class, int.class, String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.logMethodParams(param);
                    }
                });
        XposedHelpers.findAndHookMethod(cls_chatting_b_v, "h",
                int.class, Intent.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.logMethodParams(param);
                        Intent intent = (Intent) param.args[1];
                        LogUtil.logIntentExtras(intent);
                        LogUtil.logStackTraces("com.tencent.mm.ui.chatting.b.v.h()", 20, 0);
                    }
                });


        Class<?> cls_am_i = XposedHelpers.findClass("com.tencent.mm.am.i", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(cls_am_i, "a",
                String.class, String.class, ArrayList.class, int.class, boolean.class, int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.logMethodParams(param);
                        LogUtil.logStackTraces("com.tencent.mm.am.i.a()", 20, 0);

//                        arg[0] = wxid_q8k7cucfknml22
//                        arg[1] = wxid_j2nzug3sjt0t22
//                        arg[2] = [/storage/emulated/0/DCIM/Screenshots/Screenshot_20180927-151808_Chrome.jpg, /storage/emulated/0/DCIM/Screenshots/Screenshot_20180927-222901_MeiTuan.jpg]
//                        arg[3] = 0
//                        arg[4] = true
//                        arg[5] = 2130837989

                    }
                });


        /* 微信自带辅助群发功能 */
        Class<?> cls_MassSendMsgUI = XposedHelpers.findClass("com.tencent.mm.plugin.masssend.ui.MassSendMsgUI", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(cls_MassSendMsgUI, "onActivityResult",
                int.class, int.class, Intent.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        LogUtil.logMethodParams(param);
                        Intent intent = (Intent) param.args[2];
                        LogUtil.logIntentExtras(intent);
                        LogUtil.logStackTraces("com.tencent.mm.plugin.masssend.ui.MassSendMsgUI.onActivityResult()", 20, 0);
                    }
                }
        );


    }

    @Override
    public void release() {
        isLoopMassSendQueue = false;
    }


    /**
     * 群发图片[群发助手]
     *
     * @param sendToUsers
     * @param photoPath
     */
    public static void massSendImage(ArrayList<String> sendToUsers, String photoPath) {

        try {
            if (sendToUsers == null || sendToUsers.size() == 0) return;
            if (TextUtils.isEmpty(photoPath)) return;
            if (!new File(photoPath).exists()) return;
            StringBuilder sendToUserBuff = new StringBuilder();
            for (int i = 0; i < sendToUsers.size(); i++) {
                sendToUserBuff.append(";" + sendToUsers.get(i));
            }
            sendToUserBuff.delete(0, 1);
            boolean massSendAgain = false;
            int isCompress = (boolean) XposedHelpers.callStaticMethod(cls_z_q, "a", photoPath, null, true) ? 1 : 0;
            XposedHelpers.callStaticMethod(cls_plugin_masssend_a_h, "bcc");
            Object j = XposedHelpers.callStaticMethod(cls_plugin_masssend_a_b, "j", photoPath, sendToUserBuff.toString(), sendToUsers.size(), isCompress);
            Object msg = XposedHelpers.newInstance(cls_plugin_masssend_a_f, j, massSendAgain, isCompress);
            Object dv = XposedHelpers.callStaticMethod(cls_z_au, "Dv");
            XposedHelpers.callMethod(dv, "a", msg, 0);
        } catch (Exception e) {
            LogUtil.e(" CALL massSendImage Error !! " + e.getMessage());
        }

    }

    /**
     * 单聊发送图片
     */
    public static void singleChatSendImage() {
        try {
            ArrayList<String> CropImage_OutputPath_List = new ArrayList<>();
            CropImage_OutputPath_List.add("/storage/emulated/0/DCIM/Screenshots/Screenshot_20180927-151808_Chrome.jpg");
            CropImage_OutputPath_List.add("/storage/emulated/0/DCIM/Screenshots/Screenshot_20180927-222901_MeiTuan.jpg");

            ArrayList<Integer> GalleryUI_ImgIdList = new ArrayList<>();
            GalleryUI_ImgIdList.add(-1);

            Intent intent = new Intent();
            intent.putExtra("GalleryUI_IsSendImgBackground", true);
            intent.putExtra("key_select_video_list", new ArrayList<String>());
            intent.putExtra("CropImage_limit_Img_Size", 26214400);
            intent.putExtra("GalleryUI_FromUser", "wxid_q8k7cucfknml22");
            intent.putExtra("KSelectImgUseTime", 2943L);
            intent.putExtra("CropImage_OutputPath_List", CropImage_OutputPath_List);
            intent.putExtra("CropImage_Compress_Img", true);
            intent.putExtra("GalleryUI_ToUser", "wxid_j2nzug3sjt0t22");
            intent.putExtra("GalleryUI_ImgIdList", GalleryUI_ImgIdList);

            XposedHelpers.callMethod(obj_chatting_b_v, "h", 217, intent);
        } catch (Exception e) {
            LogUtil.e(" CALL singleChatSendImage Error !! " + e.getMessage());
        }
    }


    public static void sendImageXXX() {

        ArrayList<String> videos = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        images.add("/storage/emulated/0/tencent/MicroMsg/WeiXin/mmexport1542771396200.jpg");

        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.chatting.SendImgProxyUI");
        intent.setComponent(componentName);
        intent.putExtra("key_select_video_list", videos);
        intent.putExtra("CropImage_limit_Img_Size", 26214400);
        intent.putExtra("GalleryUI_FromUser", "wxid_x62kfuwlasxh22");
        intent.putExtra("KSelectImgUseTime", 21684L);
        intent.putExtra("CropImage_OutputPath_List", images);
        intent.putExtra("CropImage_Compress_Img", true);
        intent.putExtra("GalleryUI_ToUser", "wxid_j2nzug3sjt0t22");
        AppUtil.getSystemContext().startActivity(intent);
    }


    /**
     * 添加队列消息
     *
     * @param entity
     */
    public static void put(Entity entity) {
        try {
            massSendImageQueue.put(entity);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static class Entity {
        private ArrayList<String> sendToUsers;
        private String photoPath;

        public Entity() {
        }

        public Entity(ArrayList<String> sendToUsers, String photoPath) {
            this.sendToUsers = sendToUsers;
            this.photoPath = photoPath;
        }

        public ArrayList<String> getSendToUsers() {
            return sendToUsers;
        }

        public void setSendToUsers(ArrayList<String> sendToUsers) {
            this.sendToUsers = sendToUsers;
        }

        public String getPhotoPath() {
            return photoPath;
        }

        public void setPhotoPath(String photoPath) {
            this.photoPath = photoPath;
        }
    }

}
