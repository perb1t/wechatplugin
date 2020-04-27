package com.xiezhiai.wechatplugin.ui.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cache.DiskCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/19.
 *
 * @Desc:
 */
public class WechatPluginApplication extends Application {

    private static WechatPluginApplication application = new WechatPluginApplication();
    private static List<Activity> actStack = new ArrayList<>();


    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.float_transparent, android.R.color.holo_blue_dark);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
                return new BallPulseFooter(context);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /* 初始化数据库 */
        LitePal.initialize(this);
        /* 初始化网络框架 */
        initialNoHttp();
        DensityUtils.setDensity(this);
    }

    /**
     * Get a singleton for your application
     *
     * @return
     */
    public static WechatPluginApplication getApplication() {
        if (application == null) application = new WechatPluginApplication();
        return application;
    }

    /**
     * Add activities to stack
     *
     * @param act
     */
    public void addAct2Stack(Activity act) {
        if (!actStack.contains(act))
            actStack.add(act);
    }

    /**
     * Remove activities from stack
     *
     * @param act
     */
    public void removeActFromStack(Activity act) {
        if (actStack.contains(act)) {
            actStack.remove(act);
        }
    }

    /**
     * Exit the application
     */
    public void exit() {
        for (Activity act : actStack) if (act != null && !act.isFinishing()) act.finish();
        System.exit(0);
    }

    /**
     * @param cls
     */
    public void backToSpecificActivity(Class cls) {
        for (int i = actStack.size() - 1; i >= 0; i--) {
            Activity activity = actStack.get(i);
            if (activity != null) {
                if (activity.getClass().getName().equals(cls.getName())) {
                    break;
                }
                activity.finish();
                actStack.remove(activity);
            }
        }
    }

    /**
     * Initialize nohttp
     */
    private void initialNoHttp() {

        NoHttp.initialize(
                InitializationConfig.newBuilder(this)
                        // 设置全局连接超时时间，单位毫秒
                        .connectionTimeout(10 * 1000)
                        // 设置全局服务器响应超时时间，单位毫秒
                        .readTimeout(10 * 1000)
                        // 保存到数据库,如果不使用缓存，设置false禁用
                        .cacheStore(new DBCacheStore(this).setEnable(true))
                        // 或者保存到SD卡
                        .cacheStore(new DiskCacheStore(this))
                        // 默认保存数据库DBCookieStore，开发者可以自己实现; 如果不维护cookie，设置false禁用。)
                        .cookieStore(new DBCookieStore(this).setEnable(false))
                        // 使用HttpURLConnection
//                        .networkExecutor(new URLConnectionNetworkExecutor())
                        // 使用OkHttp
//                        .networkExecutor(new OkHttpNetworkExecutor())
                        .build()
        );

        // 开启NoHttp的调试模式, 配置后可看到请求过程、日志和错误信息。
        Logger.setDebug(true);
        // 设置NoHttp打印Log的tag
        Logger.setTag("XizZhiAI_NoHttp");
    }

}
