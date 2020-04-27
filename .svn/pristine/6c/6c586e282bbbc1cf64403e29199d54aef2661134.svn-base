package com.xiezhiai.wechatplugin.model.xiezhi;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.annotation.JSONField;
import com.xiezhiai.wechatplugin.func.transfer.PluginClient;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.func.transfer.PluginMessasge;

/**
 * Created by shijiwei on 2018/11/30.
 *
 * @Desc:
 */
public class Cookie {

    public static final String PLUGIN_SP_DIR_PREFEFENCE = "prefefence";
    public static final String PLUGIN_SP_KEY_inServiceWXUserId = "inServiceWXUserId";

    /* Hook进程判断app是否登录*/
    private boolean isPluginLogin = false;
    /* 插件是否完成初始化 */
    private boolean isInitialize = false;
    /* Hook进程判断 当前服务的 wx_id */
    private String inServiceWXUserId = "";
    /* header cookie */
    /* 登录之后服务器返回的认证签名 */
    private String sign;
    /* 当前选中服务的微信id */
    private String tenantID;
    /* App登录账户的id */
    private String userID;
    /* 用户权限 */
    private Permission permission = new Permission();


    public Cookie() {
    }

    public Cookie(boolean isPluginLogin, boolean isInitialize, String inServiceWXUserId, String sign, String tenantID, String userID, Permission permission) {
        this.isPluginLogin = isPluginLogin;
        this.isInitialize = isInitialize;
        this.inServiceWXUserId = inServiceWXUserId;
        this.sign = sign;
        this.tenantID = tenantID;
        this.userID = userID;
        this.permission = permission;
    }

    public boolean isPluginLogin() {
        return isPluginLogin;
    }

    public void setPluginLogin(boolean pluginLogin) {
        isPluginLogin = pluginLogin;
    }

    public void updatePluginLogin(boolean login) {
        this.isPluginLogin = login;
        if (login) {
            PluginClient.tansferMessage(new PluginMessasge(
                    PluginMessasge.NOTIFY_PLUGIN_LOGINED,
                    "======== [ 插件登录 ] ======== "
            ));
        } else {
            notifyWechatCookieUpdate(this);
        }
    }

    public boolean isInitialize() {
        return isInitialize;
    }

    public void setInitialize(boolean initialize) {
        isInitialize = initialize;
    }


    public String getCacheInServiceWXUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PLUGIN_SP_DIR_PREFEFENCE, Context.MODE_PRIVATE);
        return sp.getString(PLUGIN_SP_KEY_inServiceWXUserId, "");
    }

    public void setCacheInServiceWXUserId(Context ctx, String inServiceWXUserId) {
        setInServiceWXUserId(inServiceWXUserId);
        SharedPreferences.Editor editor = ctx.getSharedPreferences(PLUGIN_SP_DIR_PREFEFENCE, Context.MODE_PRIVATE).edit();
        editor.putString(PLUGIN_SP_KEY_inServiceWXUserId, inServiceWXUserId);
        editor.commit();
    }

    public String getInServiceWXUserId() {
        return inServiceWXUserId;
    }

    public void setInServiceWXUserId(String inServiceWXUserId) {
        this.inServiceWXUserId = inServiceWXUserId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void copy(Cookie cookie) {
        this.isPluginLogin = cookie.isPluginLogin;
        this.inServiceWXUserId = cookie.inServiceWXUserId;
        this.userID = cookie.userID;
        this.tenantID = cookie.tenantID;
        this.sign = cookie.sign;
        this.permission.copy(cookie.permission);
    }

    public void logout() {
        updatePluginLogin(false);
        isInitialize = false;
        inServiceWXUserId = "";
        sign = "";
        tenantID = "";
        userID = "";
        permission.clear();
    }

    public static void notifyWechatCookieUpdate(Cookie cookie) {
        PluginClient.tansferMessage(new PluginMessasge(
                PluginMessasge.NOTIFY_UPDATE_COOKIE,
                cookie
        ));
    }

    /**
     * 用户权限
     */
    public static class Permission {

        /*自动回复好友*/
        @JSONField(name = "auto_reply")
        private boolean isAutoReplyUser;
        /* 自动回复好友闲聊 */
        @JSONField(name = "chat_auto_reply")
        private boolean isAutoReplyUserChatting;
        /* 自动回复聊天组 */
        @JSONField(name = "group_auto_reply")
        private boolean isAutoReplyChatroom;
        /* 自动回复聊天组闲聊 */
        @JSONField(name = "group_chat_auto_reply")
        private boolean isAutoReplyChatroomChatting;
        /* 自动收红包 */
        @JSONField(name = "auto_rec_red")
        private boolean isAutoReceiveLuckMoney;
        /* 自动收转账 */
        @JSONField(name = "auto_rec_transfer")
        private boolean isAutoReceiveTransfer;

        public Permission() {
        }

        public Permission(boolean isAutoReplyUser, boolean isAutoReplyUserChatting, boolean isAutoReplyChatroom, boolean isAutoReplyChatroomChatting, boolean isAutoReceiveLuckMoney, boolean isAutoReceiveTransfer) {
            this.isAutoReplyUser = isAutoReplyUser;
            this.isAutoReplyUserChatting = isAutoReplyUserChatting;
            this.isAutoReplyChatroom = isAutoReplyChatroom;
            this.isAutoReplyChatroomChatting = isAutoReplyChatroomChatting;
            this.isAutoReceiveLuckMoney = isAutoReceiveLuckMoney;
            this.isAutoReceiveTransfer = isAutoReceiveTransfer;
        }

        public boolean isAutoReplyUser() {
            return isAutoReplyUser;
        }

        public void setAutoReplyUser(boolean autoReplyUser) {
            isAutoReplyUser = autoReplyUser;
        }

        public boolean isAutoReplyUserChatting() {
            return isAutoReplyUserChatting;
        }

        public void setAutoReplyUserChatting(boolean autoReplyUserChatting) {
            isAutoReplyUserChatting = autoReplyUserChatting;
        }

        public boolean isAutoReplyChatroom() {
            return isAutoReplyChatroom;
        }

        public void setAutoReplyChatroom(boolean autoReplyChatroom) {
            isAutoReplyChatroom = autoReplyChatroom;
        }

        public boolean isAutoReplyChatroomChatting() {
            return isAutoReplyChatroomChatting;
        }

        public void setAutoReplyChatroomChatting(boolean autoReplyChatroomChatting) {
            isAutoReplyChatroomChatting = autoReplyChatroomChatting;
        }

        public boolean isAutoReceiveLuckMoney() {
            return isAutoReceiveLuckMoney;
        }

        public void setAutoReceiveLuckMoney(boolean autoReceiveLuckMoney) {
            isAutoReceiveLuckMoney = autoReceiveLuckMoney;
        }

        public boolean isAutoReceiveTransfer() {
            return isAutoReceiveTransfer;
        }

        public void setAutoReceiveTransfer(boolean autoReceiveTransfer) {
            isAutoReceiveTransfer = autoReceiveTransfer;
        }

        public void copy(Permission p) {
            this.isAutoReplyUser = p.isAutoReplyUser;
            this.isAutoReplyUserChatting = p.isAutoReplyUserChatting;
            this.isAutoReplyChatroom = p.isAutoReplyChatroom;
            this.isAutoReplyChatroomChatting = p.isAutoReplyChatroomChatting;
            this.isAutoReceiveLuckMoney = p.isAutoReceiveLuckMoney;
            this.isAutoReceiveTransfer = p.isAutoReceiveTransfer;
        }

        public void clear() {
            isAutoReceiveTransfer = false;
            isAutoReceiveLuckMoney = false;
            isAutoReplyUser = false;
            isAutoReplyUserChatting = false;
            isAutoReplyChatroom = false;
            isAutoReplyChatroomChatting = false;
        }
    }
}
