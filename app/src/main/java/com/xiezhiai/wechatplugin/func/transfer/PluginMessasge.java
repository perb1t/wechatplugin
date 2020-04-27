package com.xiezhiai.wechatplugin.func.transfer;

import com.alibaba.fastjson.JSON;

/**
 * Created by shijiwei on 2018/11/30.
 *
 * @Desc:
 */

public class PluginMessasge {

    /**
     * wechat to plugin
     */
    public static final int HEART_BEAT = 100;
    public static final int NOTIFY_WECHAT_LOGINED = 101;
    public static final int NOTIFY_UPDATE_CONTACTS = 102;
    public static final int NOTIFY_UPDATE_CONTACTSLABELS = 103;
    public static final int NOTIFY_UPDATE_CHATROOMS = 104;
    public static final int NOTIFY_UPDATE_SNS = 105;

    /**
     * plugin to wechat
     */
    public static final int NOTIFY_PLUGIN_LOGINED = 201;
    public static final int NOTIFY_MULTIPLE_SEND_MESSAGE_TO_USER = 202;
    public static final int NOTIFY_MULTIPLE_SEND_MESSAGE_TO_CHATROOM = 203;
    public static final int NOTIFY_UPDATE_COOKIE = 204;
    public static final int NOTIFY_UPDATE_AUTO_REPLY_CHATROOM = 205;

    private int action;
    private Object data;

    public PluginMessasge() {
    }

    public PluginMessasge(int action, Object data) {
        this.action = action;
        this.data = data;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toJSONString() {
        return JSON.toJSONString(this) + PluginServer.SUFFIX;
    }

    public static PluginMessasge obtainHeartBeatMessage() {
        return new PluginMessasge(HEART_BEAT, "heart beat");
    }
}
