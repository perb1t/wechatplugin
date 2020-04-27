package com.xiezhiai.wechatplugin.hooker;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.xiezhiai.wechatplugin.core.Config;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.func.transfer.PluginMessasge;
import com.xiezhiai.wechatplugin.func.transfer.PluginServer;
import com.xiezhiai.wechatplugin.ui.aty.SnsWrapperListActivity;
import com.xiezhiai.wechatplugin.utils.sns.SnsParam;
import com.xiezhiai.wechatplugin.utils.sns.SnsParser;
import com.xiezhiai.wechatplugin.utils.sns.bean.SnsInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static de.robv.android.xposed.XposedHelpers.callMethod;

import dalvik.system.DexClassLoader;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by shijiwei on 2018/10/15.
 *
 * @Desc: 抓取微信朋友圈信息
 */
public class SnsHooker implements IHooker {

    private static final int GET_SNS = 202;

    private static ExecutorService hookTaskPool = Executors.newSingleThreadExecutor();
    private static Handler hookTaskHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_SNS:
                    hookTaskPool.execute(GetSnsRunnable);
                    break;
            }
        }
    };

    public static long getExecTaskDelayTime() {
        return 200;
    }

    /**
     * 获取微信朋友圈啊信息
     */
    public static void getSns() {
        hookTaskHandler.removeMessages(GET_SNS);
        hookTaskHandler.sendEmptyMessageDelayed(GET_SNS, getExecTaskDelayTime());
    }

    static Runnable GetSnsRunnable = new Runnable() {
        @Override
        public void run() {
            getSnsFromDB();
        }
    };

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        monitorSns("insert", "delete", "updateWithOnConflict");
    }

    @Override
    public void release() {

    }

    private void monitorSns(String... dbMethod) {
        if (dbMethod != null) {
            for (String method : dbMethod) {
                XposedBridge.hookAllMethods(Config.xWechat.SQLiteDatabase, method, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        if (param.args[0].equals("SnsInfo")) {
                            getSns();
                        }
                    }
                });
            }
        }


    }

    /**
     * 数据库拉取朋友圈数据
     */
    public static void getSnsFromDB() {

        try {
            ArrayList<SnsInfo> snsInfos = new ArrayList<>();
            Object db = WechatSQLiteDatabaseHooker.xSnsSQLiteDatabase;
            Object cursor = callMethod(
                    db, "query",
                    "SnsInfo", null, null, null, null, null, "createTime desc", null);

            while ((boolean) callMethod(cursor, "moveToNext")) {
                String userName = (String) callMethod(cursor, "getString", 1);
                if (!userName.equals(Config.xWechat.loginUser.getUserName())) continue;
                String snsId = (String) callMethod(cursor, "getString", 0);
                String head = (String) callMethod(cursor, "getString", 4);
                String type = (String) callMethod(cursor, "getString", 6);
                String sourceType = (String) callMethod(cursor, "getString", 7);
                byte[] content = (byte[]) callMethod(cursor, "getBlob", 11);
                byte[] attrBuf = (byte[]) callMethod(cursor, "getBlob", 12);
                SnsInfo sns = new SnsInfo(snsId, head, type, sourceType, content, attrBuf);
                snsInfos.add(sns);
            }
            PluginHandler.updateSnses(snsInfos);
        } catch (Exception e) {
        } catch (Throwable throwable) {
        }


    }

    private static ExecutorService copyPool = Executors.newSingleThreadExecutor();

    static class CopyApkFileThread implements Runnable {
        private Handler handler;
        private Context context;

        public CopyApkFileThread(Handler handler, Context context) {
            this.handler = handler;
            this.context = context;
        }

        @Override
        public void run() {
            try {
                String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Config.EXTERNAL_DIR;
                File dir = new File(dirPath);
                File apk = new File(dir, "wechat.apk");
                if (!dir.exists())
                    dir.mkdirs();
                if (!apk.exists()) {
                    byte[] buf = new byte[1024];
                    InputStream is = null;

                    is = context.getAssets().open("wechat.apk");
                    FileOutputStream fos = new FileOutputStream(apk);
                    int len;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    is.close();
                    fos.close();
                }
                SnsParser parser = generateSnsParser(context);
                if (handler != null)
                    handler.sendMessage(handler.obtainMessage(SnsWrapperListActivity.FLAG_SNS_PARSER_READY, parser));
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
            }
        }
    }

    public static void checkApkFile(Handler handler, Context context) {
        copyPool.execute(new CopyApkFileThread(handler, context));
    }

    /**
     * @param context
     * @return
     */
    public static SnsParser generateSnsParser(Context context) {
        try {
            File apk = new File(Environment.getExternalStorageDirectory() + Config.EXTERNAL_DIR + "/wechat.apk");
            SnsParam.version("6.3.13.64_r4488992");
            DexClassLoader cl = new DexClassLoader(
                    apk.getAbsolutePath(),
                    context.getDir("outdex", 0).getAbsolutePath(),
                    null,
                    ClassLoader.getSystemClassLoader());

            Class SnsDetailParser = cl.loadClass(SnsParam.SNS_XML_GENERATOR_CLASS);
            Class SnsDetail = cl.loadClass(SnsParam.PROTOCAL_SNS_DETAIL_CLASS);
            Class SnsObject = cl.loadClass(SnsParam.PROTOCAL_SNS_OBJECT_CLASS);
            return new SnsParser(SnsDetail, SnsDetailParser, SnsObject);
        } catch (Throwable e) {
            return null;
        }

    }


}
