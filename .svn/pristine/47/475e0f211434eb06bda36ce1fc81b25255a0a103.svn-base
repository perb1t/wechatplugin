//package com.xiezhiai.wechatplugin.core;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.xiezhiai.wechatplugin.model.xiezhi.Incoming;
//import com.xiezhiai.wechatplugin.model.xiezhi.WXUser;
//import com.xiezhiai.wechatplugin.receiver.CommandReceiver;
//import com.xiezhiai.wechatplugin.receiver.HookDataReceiver;
//import com.xiezhiai.wechatplugin.ui.aty.LoginActivity;
//import com.xiezhiai.wechatplugin.ui.aty.UserAccountManageActivity;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by shijiwei on 2018/10/29.
// *
// * @Desc: 微信插件App全局变量
// */
//public class GlobalVariable {
//
//    /* App是否登录,否则为访客状态 */
//    public static boolean isLogin = false;
//    public static boolean isInitialize = false;
//    /* App登录账户的id */
//    public static String userID;
//    /* 登录账号 */
//    public static String loginPhoneNumber;
//    /* 登录之后服务器返回的认证签名 */
//    public static String sign;
//    /* 当前选中服务的微信id */
//    public static String tenantID;
//
//    public static List<WXUser> bindedWXUserList = new ArrayList<>();
//
//
//    /**
//     * 检测用户的操作是否为有效
//     *
//     * @param context
//     * @return
//     */
//    public static boolean checkOperationVaild(Context context) {
//
//        if (!isLogin) {
//            context.startActivity(new Intent(context, LoginActivity.class));
//            return false;
//        }
//
//        if (bindedWXUserList.size() == 0) {
//            Intent intent = new Intent(context, UserAccountManageActivity.class);
//            context.startActivity(intent);
//            return false;
//        }
//
////        if (HookDataReceiver.wxLastLoginUser == null || TextUtils.isEmpty(HookDataReceiver.wxLastLoginUser.getUserName()) || !HookDataReceiver.wxLastLoginUser.isLogin()) {
////            Toast.makeText(context, "请先登录微信", Toast.LENGTH_SHORT).show();
////            return false;
////        }
//
//        return true;
//    }
//
//    /**
//     * 更新<全局>绑定微信列表
//     *
//     * @param wxUsers
//     * @param context
//     */
//    public static void updateBindedWXusers(List<WXUser> wxUsers, Context context) {
//        updateBindedWXusers(wxUsers, SettingConfig.getInServiceWXUserId(context), context);
//    }
//
//
//    public static void updateBindedWXusers(List<WXUser> wxUsers, String id, Context context) {
//        if (wxUsers == null) return;
//        bindedWXUserList.clear();
//        bindedWXUserList.addAll(wxUsers);
//        switchInServiceWXUser(context, id);
//    }
//
//    public static void switchInServiceWXUser(Context context, String id) {
//        Collections.reverse(bindedWXUserList);
//        if (bindedWXUserList.size() != 0) {
//            String inServiceWXUserId = TextUtils.isEmpty(id) ? "" : id;
//            boolean shot = false;
//            for (WXUser user : bindedWXUserList) {
//                user.setInServic(shot = user.getId().equals(inServiceWXUserId));
//                if (shot) {
//                    setTenantID(context, user.getTenantID());
//                    SettingConfig.updateInServiceWXUserId(context, user.getId());
//                    break;
//                }
//            }
//            if (!shot) {
//                bindedWXUserList.get(0).setInServic(true);
//                setTenantID(context, bindedWXUserList.get(0).getTenantID());
//                SettingConfig.updateInServiceWXUserId(context, bindedWXUserList.get(0).getId());
//            }
//        }
//    }
//
//    /**
//     * 获取正在服务中的微信账号
//     *
//     * @return
//     */
//    public static WXUser getInServiceWXUser() {
//        for (WXUser user : bindedWXUserList) {
//            if (user.isInServic()) return user;
//        }
//        return null;
//    }
//
//    public static void setUserID(Context context, String userID) {
//        GlobalVariable.userID = userID;
//        CommandReceiver.updateCookieAppUserID(context, userID);
//    }
//
//    public static void setSign(Context context, String sign) {
//        GlobalVariable.sign = sign;
//        CommandReceiver.updateCookieSign(context, sign);
//    }
//
//    public static void setTenantID(Context context, String tenantID) {
//        GlobalVariable.tenantID = tenantID;
//        CommandReceiver.updateCookieTenantID(context, tenantID);
//    }
//
//    /**
//     * 退出登录
//     *
//     * @param context
//     */
//    public static void logout(Context context) {
//        isLogin = false;
//        isInitialize = false;
//        sign = null;
//        tenantID = null;
//        userID = null;
//        loginPhoneNumber = null;
//        bindedWXUserList.clear();
//        SettingConfig.permission.clear();
//        CommandReceiver.updateLoginState(context, false);
//        CommandReceiver.updateCookieSign(context, null);
//        CommandReceiver.updateCookieAppUserID(context, null);
//        CommandReceiver.updateCookieTenantID(context, null);
//        context.startActivity(new Intent(context, LoginActivity.class));
//    }
//
//}
