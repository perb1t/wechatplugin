package com.xiezhiai.wechatplugin.hooker;

import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.xiezhiai.wechatplugin.utils.LogUtil;

import java.lang.reflect.Field;
import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by shijiwei on 2018/11/21.
 *
 * @Desc:
 */
public class SendVoiceHooker implements IHooker {

    private static final String TAG = "SendVoiceHooker";

    String class_modelvoice_p = "com.tencent.mm.modelvoice.p";
    String method_modelvoice_p_ww = "ww";

    String class_modelvoice_q = "com.tencent.mm.modelvoice.q";
    String method_modelvoice_q_nU = "nU";
    String method_modelvoice_q_getFullPath = "getFullPath";

    String class_modelvoice_t = "com.tencent.mm.modelvoice.t";
    String method_modelvoice_t_vU = "vU";

    String class_compatible_util_b = "com.tencent.mm.compatible.util.b";
    String method_compatible_util_b_zS = "zS";

    String class_ui_chatting_o = "com.tencent.mm.ui.chatting.o"; // dex4
    String method_ui_chatting_o_ctR = "ctR";

    // 发送音频的关键类
    String class_e_b_h = "com.tencent.mm.e.b.h";  // dex0
    String method_e_b_h_cR = "cR";  // start
    String method_e_b_h_wa = "wa";  // stop
    Object object_e_b_h;


    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {

        hook(lpparam, true, "com.tencent.mm.modelvoice.q", "m", "nu","getFullPath");
        hook(lpparam, false, "com.tencent.mm.modelvoice.u", "ob");
        hook(lpparam, true, "com.tencent.mm.ui.chatting.af", "aD");
        hook(lpparam, true, "com.tencent.mm.plugin.masssend.ui.b", "bce", "bcf");


        // kernel
        Class cls_masssend_ui_b = XposedHelpers.findClass("com.tencent.mm.plugin.masssend.ui.b", WechatHooker.wxllparam.classLoader);
        XposedBridge.hookAllConstructors(cls_masssend_ui_b, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                LogUtil.log("发送语音 com.tencent.mm.plugin.masssend.ui.b  CALL 构造函数 ");
                LogUtil.logMethodParams(param);
                massSendObj = param.thisObject;
            }
        });


        XposedHelpers.findAndHookMethod("com.tencent.mm.ui.LauncherUI", lpparam.classLoader,
                "onCreateOptionsMenu", Menu.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        LogUtil.log(" onCreateOptionsMenu ");
                        Menu menu = (Menu) param.args[0];
                        menu.add(0, 3, 0, "哈哈");
                        menu.add(0, 3, 0, "呵呵");
                        menu.add(0, 3, 0, "嘿嘿");

                        for (int i = 0; i < menu.size(); i++) {
                            menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    LogUtil.log(" onCreateOptionsMenu " + item.getTitle());
                                    return false;
                                }
                            });
                        }
                    }
                });


        Class cls_plugin_wear_model_e_m = XposedHelpers.findClass("com.tencent.mm.plugin.wear.model.e.m.a", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(cls_plugin_wear_model_e_m, "execute",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        LogUtil.logStackTraces("发送语音 com.tencent.mm.plugin.wear.model.e.m.execute()");

                    }
                });


        Class MassSendMsgUI = XposedHelpers.findClass("com.tencent.mm.plugin.masssend.ui.MassSendMsgUI", lpparam.classLoader);
        Class chatting_o = XposedHelpers.findClass("com.tencent.mm.ui.chatting.o", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(chatting_o, "k", chatting_o, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                LogUtil.logMethodParams(param);
                LogUtil.logStackTraces("com.tencent.mm.ui.chatting.o.k()");
            }
        });
    }

    @Override
    public void release() {

    }


    static boolean reSend = false;
    static Object massSendObj;

    public static void sendVoice() {

        LogUtil.log(" CALL sendVoice start");
        Class cls_modelvoice_q = XposedHelpers.findClass("com.tencent.mm.modelvoice.q", WechatHooker.wxllparam.classLoader);
        Class cls_modelvoice_m = XposedHelpers.findClass("com.tencent.mm.modelvoice.m", WechatHooker.wxllparam.classLoader);
        XposedHelpers.callStaticMethod(cls_modelvoice_q, "m", "1310291225183b4d15bbd1e100", 1043, 0);
//        XposedHelpers.callStaticMethod(cls, "iH", 86);
//        XposedHelpers.callStaticMethod(cls_modelvoice_q, "W", "1809251225183b4d15b3597102", 1088);
//        Object tl = XposedHelpers.callStaticMethod(cls_modelvoice_m, "Tl");
//        XposedHelpers.callMethod(tl, "run");
//        reSend = true;


        Class cls_masssend_ui_b = XposedHelpers.findClass("com.tencent.mm.plugin.masssend.ui.b", WechatHooker.wxllparam.classLoader);

        LogUtil.log(" CALL sendVoice End");

    }


    public static void sendVoice2() {

        Class cls_masssend_a_a = XposedHelpers.findClass("com.tencent.mm.plugin.masssend.a.a", WechatHooker.wxllparam.classLoader);
        Object obj_masssend_a_a = XposedHelpers.newInstance(cls_masssend_a_a);
        try {

            Field oJg = cls_masssend_a_a.getDeclaredField("oJg");
            oJg.setAccessible(true);
            oJg.set(obj_masssend_a_a, "wxid_j2nzug3sjt0t22;wxid_h7qzabq4jcs421");

            Field oJh = cls_masssend_a_a.getDeclaredField("oJh");
            oJh.setAccessible(true);
            oJh.set(obj_masssend_a_a, 2);

            Field filename = cls_masssend_a_a.getDeclaredField("filename");
            filename.setAccessible(true);
            filename.set(obj_masssend_a_a, "0014191225183b4d15b1ca4102");

            Field msgType = cls_masssend_a_a.getDeclaredField("msgType");
            msgType.setAccessible(true);
            msgType.set(obj_masssend_a_a, 34);


            Class cls_ac_i = XposedHelpers.findClass("com.tencent.mm.ac.i", WechatHooker.wxllparam.classLoader);
            Object obj_ac_i = XposedHelpers.newInstance(cls_ac_i, obj_masssend_a_a, true);

            Class cls_z_au = XposedHelpers.findClass("com.tencent.mm.z.au", WechatHooker.wxllparam.classLoader);
            Object dv = XposedHelpers.callStaticMethod(cls_z_au, "Dv");
            XposedHelpers.callMethod(dv, "a", obj_ac_i, 0);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void sendVoice3(String voiceFileName, long time) {

        try {

            Class cls_compatible_h_a = XposedHelpers.findClass("com.tencent.mm.compatible.h.a", WechatHooker.wxllparam.classLoader);
            Object obj_compatible_h_a = XposedHelpers.newInstance(cls_compatible_h_a);
            setFieldValue(cls_compatible_h_a, obj_compatible_h_a, "fLX", voiceFileName);
            setFieldValue(cls_compatible_h_a, obj_compatible_h_a, "fLY", time);
            setFieldValue(cls_compatible_h_a, obj_compatible_h_a, "fLZ", 2);
            setFieldValue(cls_compatible_h_a, obj_compatible_h_a, "esC", 2);
            LogUtil.log("发送语音 obj_compatible_h_a = " + JSON.toJSONString(obj_compatible_h_a));

            Class cls_modelvoice_q = XposedHelpers.findClass("com.tencent.mm.modelvoice.q", WechatHooker.wxllparam.classLoader);
            XposedHelpers.callStaticMethod(cls_modelvoice_q, "W", voiceFileName, time);
            Class cls_modelvoice_m = XposedHelpers.findClass("com.tencent.mm.modelvoice.m", WechatHooker.wxllparam.classLoader);
            Object tl = XposedHelpers.callStaticMethod(cls_modelvoice_m, "TI");
            XposedHelpers.callMethod(tl, "run");

        } catch (Exception e) {
            LogUtil.log("发送语音 Error ");
        }


    }

    public static void setFieldValue(Class cls, Object instance, String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = cls.getDeclaredField(field);
        f.setAccessible(true);
        f.set(instance, value);
    }

    private void hook(XC_LoadPackage.LoadPackageParam lpparam, final boolean logStackTraces, final String clsPath, final String... methods) {
        Class cls = XposedHelpers.findClass(clsPath, lpparam.classLoader);
        if (methods != null && methods.length != 0) {
            for (final String method : methods) {
                XposedBridge.hookAllMethods(cls, method,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                LogUtil.logMethodParams(param);
                                if (logStackTraces)
                                    LogUtil.logStackTraces(clsPath + "." + method + "()");
                            }

                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                LogUtil.log(clsPath + "." + method + "() Result = " + param.getResult());
                            }
                        });
            }
        }

    }

}
