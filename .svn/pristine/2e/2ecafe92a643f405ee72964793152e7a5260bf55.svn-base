package com.xiezhiai.wechatplugin.utils;

import android.os.Handler;

/**
 * Created by shijiwei on 2018/11/5.
 *
 * @Desc:
 */
public class AccessControl {

    private long lastTimeMillis;

    /**
     * 秒级控制
     *
     * @return
     */
    public boolean isAccess() {
        return isAccess(1000);
    }

    /**
     * 指定时间控制
     *
     * @param time
     * @return
     */
    public boolean isAccess(long time) {
        boolean ret;
        long l = System.currentTimeMillis();
        ret = l - lastTimeMillis >= time;
        lastTimeMillis = l;
        return ret;
    }


    // https://blog.csdn.net/tianshanaoxue/article/details/80244231
}
