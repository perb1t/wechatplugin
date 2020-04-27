package com.xiezhiai.wechatplugin.func.transfer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.xiezhiai.wechatplugin.core.Config;
import com.xiezhiai.wechatplugin.func.transfer.dao.WXCommonDao;
import com.xiezhiai.wechatplugin.hooker.AutoReplyHooker;
import com.xiezhiai.wechatplugin.model.wechat.ChatRoom;
import com.xiezhiai.wechatplugin.model.wechat.ContactLabel;
import com.xiezhiai.wechatplugin.model.wechat.UserInfo;
import com.xiezhiai.wechatplugin.model.xiezhi.Cookie;
import com.xiezhiai.wechatplugin.model.xiezhi.MultipleSendMessage;
import com.xiezhiai.wechatplugin.model.xiezhi.WXUser;
import com.xiezhiai.wechatplugin.ui.aty.LoginActivity;
import com.xiezhiai.wechatplugin.ui.aty.UserAccountManageActivity;
import com.xiezhiai.wechatplugin.ui.interf.PluginUIHooker;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.sns.bean.SnsInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by shijiwei on 2018/11/30.
 *
 * @Desc:
 */
public class PluginHandler extends Handler implements ITransferPluginMessageHandler {

    private static final String TAG = "PluginHandler";

    public static UserInfo wxLastLoginUser = new UserInfo();

    public static WXCommonDao<String, UserInfo> contactsDao = new WXCommonDao<>();
    private static WXCommonDao<String, ContactLabel> contactLabelDao = new WXCommonDao<>();
    private static WXCommonDao<String, ChatRoom> chatroomDao = new WXCommonDao<>();
    private static WXCommonDao<String, SnsInfo> snsDao = new WXCommonDao<>();

    public static ArrayList<ChatRoom> autoReplyChatrooms = new ArrayList<>();
    public static boolean isAllChatroomsAutoReply;


    public static ArrayList<WXUser> bindWXUserList = new ArrayList<>();
    public static ArrayList<PluginUIHooker> pluginUIHookers = new ArrayList<>();
    public static Cookie cookie = new Cookie();

    private Context context;

    public PluginHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            String _msg = (String) msg.obj;
            LogUtil.e(TAG + " _Mgs : " + _msg);
            if (!TextUtils.isEmpty(_msg)) {
                PluginMessasge pMsg = JSON.toJavaObject(JSON.parseObject(_msg), PluginMessasge.class);
                if (pMsg != null) {
                    int action = pMsg.getAction();
                    String data = JSON.toJSONString(pMsg.getData());
                    switch (action) {
                        case PluginMessasge.HEART_BEAT:
                            break;
                        case PluginMessasge.NOTIFY_WECHAT_LOGINED:
                            handleWechatLogin(data);
                            break;
                        case PluginMessasge.NOTIFY_UPDATE_CONTACTS:
                            handleUpdateContacts(data);
                            break;
                        case PluginMessasge.NOTIFY_UPDATE_CONTACTSLABELS:
                            handleUpdateContactsLabels(data);
                            break;
                        case PluginMessasge.NOTIFY_UPDATE_CHATROOMS:
                            handleUpdateChatrooms(data);
                            break;
                        case PluginMessasge.NOTIFY_UPDATE_SNS:
                            handleUpdateSns(data);
                            break;
                        case PluginMessasge.NOTIFY_PLUGIN_LOGINED:
                            handlePluginLogin(data);
                            break;
                        case PluginMessasge.NOTIFY_MULTIPLE_SEND_MESSAGE_TO_USER:
                            handleMultipleSendMessageToUser(data);
                            break;
                        case PluginMessasge.NOTIFY_MULTIPLE_SEND_MESSAGE_TO_CHATROOM:
                            handleMultipleSendMessageToChatroom(data);
                            break;
                        case PluginMessasge.NOTIFY_UPDATE_COOKIE:
                            handleUpdateCookie(data);
                            break;
                        case PluginMessasge.NOTIFY_UPDATE_AUTO_REPLY_CHATROOM:
                            handleUpdateAutoReplyChatrooms(data);
                            break;

                    }
                }
            }

        } catch (Exception e) {
            LogUtil.e(TAG + "  " + e.getMessage());
        }
    }


    @Override
    public void handleWechatLogin(String _msg) {

        UserInfo wxLastLoginUser = JSON.toJavaObject(JSON.parseObject(_msg), UserInfo.class);
        String userName = PluginHandler.wxLastLoginUser.getUserName();
        if (!TextUtils.isEmpty(userName) && !userName.equals(wxLastLoginUser.getUserName())) {
            /* 微信切换账号 */
            for (int i = 0; i < pluginUIHookers.size(); i++) {
                PluginUIHooker pluginUIHooker = pluginUIHookers.get(i);
                if (pluginUIHooker != null) pluginUIHooker.onWechatSwitchAccount();
            }
        }
        if (wxLastLoginUser != null) {
            PluginHandler.wxLastLoginUser.copy(wxLastLoginUser);
        }

        /*监听到微信登录，向hooker进程发送 cookie*/
        PluginClient.tansferMessage(
                new PluginMessasge(
                        PluginMessasge.NOTIFY_UPDATE_COOKIE,
                        cookie
                )
        );

    }

    @Override
    public void handlePluginLogin(String _msg) {
        PluginServer.tansferMessage(new PluginMessasge(PluginMessasge.NOTIFY_WECHAT_LOGINED, Config.xWechat.loginUser));
        updateChatrooms(chatroomDao.get(wxLastLoginUser.getUserName()));
        updateContactsLabels(contactLabelDao.get(wxLastLoginUser.getUserName()));
    }

    @Override
    public void handleUpdateCookie(String _msg) {
        Cookie _cookie = JSON.toJavaObject(JSON.parseObject(_msg), Cookie.class);
        cookie.copy(_cookie);
    }

    @Override
    public void handleUpdateContacts(String _msg) {

    }

    @Override
    public void handleUpdateContactsLabels(String _msg) {

        ArrayList<ContactLabel> contactLabels = (ArrayList<ContactLabel>) JSONArray.parseArray(_msg, ContactLabel.class);
        if (contactLabels != null && contactLabels.size() != 0 && !TextUtils.isEmpty(wxLastLoginUser.getUserName())) {
            contactLabelDao.put(wxLastLoginUser.getUserName(), contactLabels);
        }
    }

    @Override
    public void handleUpdateChatrooms(String _msg) {

        ArrayList<ChatRoom> chatRooms = (ArrayList<ChatRoom>) JSONArray.parseArray(_msg, ChatRoom.class);
        if (chatRooms != null && chatRooms.size() != 0 && !TextUtils.isEmpty(wxLastLoginUser.getUserName())) {
            chatroomDao.put(wxLastLoginUser.getUserName(), chatRooms);
        }

        for (PluginUIHooker uiHooker : pluginUIHookers) {
            if (uiHooker != null) uiHooker.onWechatChatroomsChanged();
        }
    }

    @Override
    public void handleUpdateSns(String _msg) {
        String key = wxLastLoginUser.getUserName();
        SharedPreferences.Editor editor = context.getSharedPreferences(key, Context.MODE_PRIVATE).edit();
        editor.putString(key, _msg);
        editor.commit();
    }


    @Override
    public void handleMultipleSendMessageToUser(String _msg) {

        ArrayList<UserInfo> contacsts = contactsDao.get(cookie.getInServiceWXUserId());
        if (contacsts != null) {
            MultipleSendMessage<ContactLabel> multipleSendMessage = JSON.parseObject(_msg, new TypeReference<MultipleSendMessage<ContactLabel>>() {
            });
            String content = multipleSendMessage.getContent();
            ArrayList<ContactLabel> contactLabels = multipleSendMessage.getTargets();
            if (multipleSendMessage.isAll()) {
                /* 全部好友 */
                for (UserInfo user : contacsts) {
                    AutoReplyHooker.putMessage(new com.xiezhiai.wechatplugin.model.wechat.Message(user.getUserName(), content));
                }
            } else {
                /* 部分好友 */
                if (contactLabels != null) {
                    for (UserInfo user : contacsts) {
                        for (ContactLabel label : contactLabels) {
                            if (user.getLabels().contains(label.getLabelID())) {
                                AutoReplyHooker.putMessage(new com.xiezhiai.wechatplugin.model.wechat.Message(user.getUserName(), content));
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public void handleMultipleSendMessageToChatroom(String _msg) {

        ArrayList<ChatRoom> allChatRooms = chatroomDao.get(cookie.getInServiceWXUserId());
        if (allChatRooms != null) {
            MultipleSendMessage<ChatRoom> multipleSendMessage = JSON.parseObject(_msg, new TypeReference<MultipleSendMessage<ChatRoom>>() {
            });
            String content = multipleSendMessage.getContent();
            if (multipleSendMessage.isAll()) {
                /* 所有群组 */
                for (ChatRoom room : allChatRooms) {
                    AutoReplyHooker.putMessage(new com.xiezhiai.wechatplugin.model.wechat.Message(room.getId(), content));
                }
            } else {
                /* 部分群组 */
                ArrayList<ChatRoom> chatRooms = multipleSendMessage.getTargets();
                if (chatRooms != null) {
                    for (ChatRoom room : allChatRooms) {
                        for (ChatRoom select : chatRooms) {
                            if (room.getId().equals(select.getId())) {
                                AutoReplyHooker.putMessage(new com.xiezhiai.wechatplugin.model.wechat.Message(room.getId(), content));
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public void handleUpdateAutoReplyChatrooms(String _msg) {
        MultipleSendMessage<ChatRoom> multipleSendMessage = JSON.parseObject(_msg, new TypeReference<MultipleSendMessage<ChatRoom>>() {
        });
        isAllChatroomsAutoReply = multipleSendMessage.isAll();
        autoReplyChatrooms.clear();
        autoReplyChatrooms.addAll(multipleSendMessage.getTargets());
    }

    /**
     * 更新朋友圈列表
     *
     * @param snsInfos
     */
    public static void updateSnses(ArrayList<SnsInfo> snsInfos) {
        snsDao.put(wxLastLoginUser.getUserName(), snsInfos);
        PluginServer.tansferMessage(
                new PluginMessasge(
                        PluginMessasge.NOTIFY_UPDATE_SNS,
                        snsInfos
                ));
    }

    /**
     * 更新通讯录列表
     *
     * @param contacts
     */
    public static void updateContacts(ArrayList<UserInfo> contacts) {
        contactsDao.put(wxLastLoginUser.getUserName(), contacts);
    }

    /**
     * 更新好友标签列表
     *
     * @param contactsLabels
     */
    public static void updateContactsLabels(ArrayList<ContactLabel> contactsLabels) {
        contactLabelDao.put(wxLastLoginUser.getUserName(), contactsLabels);
        PluginServer.tansferMessage(new PluginMessasge(
                        PluginMessasge.NOTIFY_UPDATE_CONTACTSLABELS,
                        contactsLabels
                )
        );
    }

    /**
     * 更新聊天组列表
     *
     * @param chatRooms
     */
    public static void updateChatrooms(ArrayList<ChatRoom> chatRooms) {
        chatroomDao.put(wxLastLoginUser.getUserName(), chatRooms);
        PluginServer.tansferMessage(new PluginMessasge(
                        PluginMessasge.NOTIFY_UPDATE_CHATROOMS,
                        chatRooms
                )
        );
    }


    /**
     * 退出登录，释放资源
     */
    public static void logout() {
        bindWXUserList.clear();
        cookie.logout();
    }

    /**
     * 判断是否处于微信服务状态
     *
     * @return
     */
    public static boolean isWechatPluginInService() {
        try {
            return PluginServer.pluginKeepAlive() && cookie.isPluginLogin() && (cookie.getInServiceWXUserId().equals(wxLastLoginUser.getUserName()));
        } catch (Exception e) {
            return false;
        }

    }


    /**
     * 检测用户的操作是否为有效
     *
     * @param context
     * @return
     */
    public static boolean checkOperationVaild(Context context) {

        if (!cookie.isPluginLogin()) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }

        if (bindWXUserList.size() == 0) {
            Intent intent = new Intent(context, UserAccountManageActivity.class);
            context.startActivity(intent);
            return false;
        }

        return true;
    }


    /**
     * 更新绑定的微信列表
     *
     * @param wxUsers
     */
    public static void updateBindWXUsers(Context context, List<WXUser> wxUsers) {
        if (wxUsers != null) {
            updateBindWXUsers(context, wxUsers, cookie.getInServiceWXUserId());
        }
    }

    public static void updateBindWXUsers(Context context, List<WXUser> wxUsers, String id) {
        if (wxUsers != null) {
            bindWXUserList.clear();
            bindWXUserList.addAll(wxUsers);
            switchInServiceWXUser(context, id);
        }
    }

    /**
     * 切换服务的微信号
     *
     * @param id
     */
    public static void switchInServiceWXUser(Context context, String id) {
//        Collections.reverse(bindWXUserList);
        if (bindWXUserList.size() != 0) {
            String inServiceWXUserId = TextUtils.isEmpty(id) ? "" : id;
            boolean shot = false;
            for (WXUser user : bindWXUserList) {
                if (user.getId().equals(inServiceWXUserId)) {
                    if (shot) continue;
                    user.setInServic(true);
                    cookie.setTenantID(user.getTenantID());
                    cookie.setCacheInServiceWXUserId(context, id);
                    cookie.notifyWechatCookieUpdate(cookie);
                    shot = true;
                } else {
                    user.setInServic(false);
                }
            }
            if (!shot) {
                WXUser defaultWXUser = bindWXUserList.get(0);
                defaultWXUser.setInServic(true);
                cookie.setTenantID(defaultWXUser.getTenantID());
                cookie.setCacheInServiceWXUserId(context, defaultWXUser.getId());
                cookie.notifyWechatCookieUpdate(cookie);
            }
        }
    }

    /**
     * 获取当前选中服务中的微信号
     *
     * @return
     */
    public static WXUser getInServiceWXUser() {
        for (WXUser wxUser : bindWXUserList) {
            if (wxUser != null && wxUser.isInServic()) {
                return wxUser;
            }
        }
        return null;
    }


    /**
     * 注册通知界面回调
     *
     * @param hooker
     */
    public static void registerPluginUIHooker(PluginUIHooker hooker) {
        if (hooker != null && !pluginUIHookers.contains(hooker)) {
            pluginUIHookers.add(hooker);
        }
    }

    /**
     * 获取微信聊天组列表
     *
     * @return
     */
    public static ArrayList<ChatRoom> getWXChatrooms() {
        ArrayList<ChatRoom> chatRooms = chatroomDao.get(cookie.getInServiceWXUserId());
        return chatRooms == null ? new ArrayList<ChatRoom>() : chatRooms;
    }

    /**
     * 将聊天组列表全部设置为未选中
     */
    public static void resetWXChatrooms() {
        ArrayList<ChatRoom> chatRooms = chatroomDao.get(cookie.getInServiceWXUserId());
        if (chatRooms != null) {
            for (ChatRoom room : chatRooms) {
                room.setChecked(false);
            }
        }
    }

    /**
     * 获取微信好友标签列表
     *
     * @return
     */
    public static ArrayList<ContactLabel> getWXContactsLabels() {
        ArrayList<ContactLabel> contactLabels = contactLabelDao.get(cookie.getInServiceWXUserId());
        return contactLabels == null ? new ArrayList<ContactLabel>() : contactLabels;
    }

    /**
     * 将好友标签列表全部设置为未选中
     */
    public static void resetWXContactsLabels() {
        ArrayList<ContactLabel> contactLabels = contactLabelDao.get(cookie.getInServiceWXUserId());
        if (contactLabels != null) {
            for (ContactLabel label : contactLabels) {
                label.setChecked(false);
            }
        }
    }

    /**
     * 获取微信朋友圈列表
     *
     * @return
     */
    public static ArrayList<SnsInfo> getWXSnses(Context context) {
        String key = cookie.getInServiceWXUserId();
        SharedPreferences sp = context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String value = sp.getString(key, "");
        ArrayList<SnsInfo> localSnsInfos = (ArrayList<SnsInfo>) JSONArray.parseArray(value, SnsInfo.class);
        return localSnsInfos == null ? new ArrayList<SnsInfo>() : localSnsInfos;

    }

}
