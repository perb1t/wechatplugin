package com.xiezhiai.wechatplugin.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;


/**
 * Created by shijiwei on 2018/11/7.
 *
 * @Desc:
 */
public class PluginService extends Service {

    private static final String TAG = "PluginService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        PluginHandler.cookie.updatePluginLogin(false);
        super.onDestroy();
    }

    /**
     * 启动服务
     *
     * @param context
     */
    public static void startServer(Context context) {
        context.startService(new Intent(context, PluginService.class));
    }

    /**
     * 退出服务
     *
     * @param context
     */
    public static void stopServer(Context context) {
        context.stopService(new Intent(context, PluginService.class));
    }


}
