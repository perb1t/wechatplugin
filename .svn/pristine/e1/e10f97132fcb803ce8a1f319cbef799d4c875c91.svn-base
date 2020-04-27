//package com.xiezhiai.wechatplugin.func.autoreply;
//
//
//import android.text.TextUtils;
//
//import com.xiezhiai.wechatplugin.hooker.IHooker;
//import com.xiezhiai.wechatplugin.model.wechat.Message;
//import com.xiezhiai.wechatplugin.utils.LogUtil;
//
//import de.robv.android.xposed.XC_MethodHook;
//import de.robv.android.xposed.XposedBridge;
//import de.robv.android.xposed.XposedHelpers;
//import de.robv.android.xposed.callbacks.XC_LoadPackage;
//
///**
// * Created by shijiwei on 2018/12/5.
// *
// * @Desc:
// */
//public class AutoReplyHandler implements IHooker {
//
//    private static final String TAG = "AutoReplyHandler";
//    public static final String wxMsgSplitStr = "\r\n\t";
//
//    public static Object chatting_o;
//
//
//
//    @Override
//    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
//        final Class<?> cls_chatting_o = XposedHelpers.findClass("com.tencent.mm.ui.chatting.o", lpparam.classLoader);
//        XposedBridge.hookAllConstructors(cls_chatting_o, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                chatting_o = param.thisObject;
//                LogUtil.e(TAG + "  chatting_o CALL Constructor " + chatting_o);
//                LogUtil.logMethodParams(TAG + "chatting_o CALL Constructor", param);
//                LogUtil.logStackTraces(TAG + " chatting_o  ", 15, 3);
//            }
//        });
//
//        Class<?> cls_modelmulti_i = XposedHelpers.findClass("com.tencent.mm.modelmulti.i", lpparam.classLoader);
//        XposedHelpers.findAndHookConstructor(cls_modelmulti_i,
//                String.class, String.class, int.class, int.class, Object.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//
//                        String content = (String) param.args[1];
//                        LogUtil.e(TAG + "  cls_modelmulti_i CALL Constructor [ origin_content = " + content);
//
//                        if (!TextUtils.isEmpty(content) && (content.indexOf(wxMsgSplitStr) != -1)) {
//                            String[] split = content.split(wxMsgSplitStr);
//                            param.args[0] = split[0];
//                            param.args[1] = split[1];
//                        }
//                    }
//                });
//
//        Class<?> cls_g_c_cg = XposedHelpers.findClass("com.tencent.mm.g.c.cg", lpparam.classLoader);
//        XposedHelpers.findAndHookMethod(cls_g_c_cg, "ed", String.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//                LogUtil.logStackTraces(TAG + " ed ", 15, 3);
//            }
//
//        });
//
//        XposedHelpers.findAndHookMethod(cls_chatting_o, "FZ", String.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//
//                LogUtil.logStackTraces(TAG + " FZ  ", 15, 3);
//            }
//        });
//    }
//
//}
