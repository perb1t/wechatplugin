package com.xiezhiai.wechatplugin.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shijiwei on 2018/11/15.
 *
 * @Desc:
 */
public class PluginLoginManager {

    public static final String PLUGIN_ACCOUNT = "plugin_account";
    public static final String PLUGIN_USER_NAME = "plugin_user_name";
    public static final String PLUGIN_PASSWORD = "plugin_password";
    public static final String PLUGIN_IS_LOGIN = "plugin_is_login";

    /**
     * 保存登录用户的用户信息
     *
     * @param user
     * @param psw
     */
    public static void saveLoginAccount(Context context, String user, String psw, boolean isLogin) {
        SharedPreferences sp = context.getSharedPreferences(PLUGIN_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(PLUGIN_USER_NAME, user);
        edit.putString(PLUGIN_PASSWORD, psw);
        edit.putBoolean(PLUGIN_IS_LOGIN, isLogin);
        edit.commit();
    }

    public static void updateLoginStatus(Context context, boolean isLogin) {
        SharedPreferences sp = context.getSharedPreferences(PLUGIN_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(PLUGIN_IS_LOGIN, isLogin);
        edit.commit();
    }

    /**
     * 获取最后一次登录用户的信息
     *
     * @param context
     * @return
     */
    public static Object[] getLastLoginAccount(Context context) {
        Object[] result = new Object[3];
        SharedPreferences sp = context.getSharedPreferences(PLUGIN_ACCOUNT, Context.MODE_PRIVATE);
        String user = sp.getString(PLUGIN_USER_NAME, "");
        String psw = sp.getString(PLUGIN_PASSWORD, "");
        boolean isLogin = sp.getBoolean(PLUGIN_IS_LOGIN, false);
        result[0] = user;
        result[1] = psw;
        result[2] = isLogin;
        return result;
    }

    /**
     * 最后一次登录的用户的登录状态
     *
     * @param context
     * @return
     */
    public static boolean getLastLoginAccountIsLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PLUGIN_ACCOUNT, Context.MODE_PRIVATE);
        return sp.getBoolean(PLUGIN_IS_LOGIN, false);
    }


}
