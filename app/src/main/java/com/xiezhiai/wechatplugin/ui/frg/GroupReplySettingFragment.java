package com.xiezhiai.wechatplugin.ui.frg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.adapter.SendMsgSelectGroupAdapter;
import com.xiezhiai.wechatplugin.func.transfer.PluginClient;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.func.transfer.PluginMessasge;
import com.xiezhiai.wechatplugin.model.wechat.ChatRoom;
import com.xiezhiai.wechatplugin.model.xiezhi.Cookie;
import com.xiezhiai.wechatplugin.model.xiezhi.MultipleSendMessage;
import com.xiezhiai.wechatplugin.ui.aty.MainActivity;
import com.xiezhiai.wechatplugin.ui.base.BaseFragment;
import com.xiezhiai.wechatplugin.ui.interf.PluginUIHooker;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.ArrayList;

/**
 * Created by shijiwei on 2018/10/20.
 *
 * @Desc:
 */
public class GroupReplySettingFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        SendMsgSelectGroupAdapter.SelectChatroomListener, PluginUIHooker {

    private ListView lvChatroom;
    private SendMsgSelectGroupAdapter adapter;

    private CheckBox cbAutoReplyChatroom;
    private CheckBox cbChattingAutoReplyChatroom;
    private CheckBox cbAllChatrom;
    private CheckBox cbPartChatroom;
    private View chattingSettingLayer;
    private LinearLayout layoutAllChatroom;
    private LinearLayout layoutPartChatroom;

    private ArrayList<ChatRoom> allChatRooms = new ArrayList<>();
    private boolean isVisibleToUser;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_group_reply_setting;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {
        PluginHandler.registerPluginUIHooker(this);
    }

    @Override
    public void initialView(View view) {

        cbAutoReplyChatroom = view.findViewById(R.id.cb_auto_reply_chatroom);
        cbChattingAutoReplyChatroom = view.findViewById(R.id.cb_chatting_auto_reply_chatroom);
        cbAllChatrom = view.findViewById(R.id.cb_all_chatroom);
        cbPartChatroom = view.findViewById(R.id.cb_part_chatroom);
        chattingSettingLayer = view.findViewById(R.id.chatting_setting_layer);
        layoutAllChatroom = view.findViewById(R.id.layout_all_chatroom);
        layoutPartChatroom = view.findViewById(R.id.layout_part_chatroom);

        lvChatroom = view.findViewById(R.id.lv_chatroom);
        adapter = new SendMsgSelectGroupAdapter(getActivity(), allChatRooms);
        lvChatroom.setAdapter(adapter);
    }

    @Override
    public void initialEvn() {
        cbAutoReplyChatroom.setOnCheckedChangeListener(this);
        cbChattingAutoReplyChatroom.setOnCheckedChangeListener(this);
        cbAllChatrom.setOnCheckedChangeListener(this);
        cbPartChatroom.setOnCheckedChangeListener(this);
        layoutAllChatroom.setOnClickListener(this);
        layoutPartChatroom.setOnClickListener(this);
        adapter.setSelectChatroomListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUIPernmission(PluginHandler.cookie.getPermission());
        loadWechatChatromms();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }

    @Override
    public void onHttpStart(int what) {

    }

    @Override
    public void onHttpSucceed(int what, Response response) {

    }

    @Override
    public void onHttpFinish(int what) {

    }

    @Override
    public void onAllUnselected(boolean all) {
        cbPartChatroom.setChecked(!all);
        selectAutoReplyChatrooms(cbAllChatrom.isChecked());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_all_chatroom:
                cbAllChatrom.setChecked(true);
                cbPartChatroom.setChecked(false);
                adapter.cancelSelected();
                break;
            case R.id.layout_part_chatroom:
                cbAllChatrom.setChecked(false);
                cbPartChatroom.setChecked(true);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.cb_auto_reply_chatroom:
                chattingSettingLayer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                if (!isChecked) cbChattingAutoReplyChatroom.setChecked(false);
                PluginHandler.cookie.getPermission().setAutoReplyChatroom(isChecked);
                ((MainActivity) getActivity()).updateSettingConfig(PluginHandler.cookie.getInServiceWXUserId());
                break;
            case R.id.cb_chatting_auto_reply_chatroom:
                PluginHandler.cookie.getPermission().setAutoReplyChatroomChatting(isChecked);
                ((MainActivity) getActivity()).updateSettingConfig(PluginHandler.cookie.getInServiceWXUserId());
                break;
            case R.id.cb_all_chatroom:
                if (isChecked) {
                    cbPartChatroom.setChecked(false);
                }
                selectAutoReplyChatrooms(isChecked);
                break;
            case R.id.cb_part_chatroom:
                if (isChecked) {
                    cbAllChatrom.setChecked(false);
                } else {
                    adapter.cancelSelected();
                }
                selectAutoReplyChatrooms(false);
                break;
        }
        switch (buttonView.getId()) {
            case R.id.cb_auto_reply_chatroom:
            case R.id.cb_chatting_auto_reply_chatroom:

                break;
            case R.id.cb_all_chatroom:
            case R.id.cb_part_chatroom:

                break;
        }

    }


    /**
     * 初始化界面权限
     *
     * @param permission
     */
    private void initUIPernmission(Cookie.Permission permission) {
        cbAutoReplyChatroom.setChecked(permission.isAutoReplyChatroom());
        cbChattingAutoReplyChatroom.setChecked(permission.isAutoReplyChatroomChatting());
    }

    /**
     * 加载微信的聊天组
     */
    private void loadWechatChatromms() {
        ArrayList<ChatRoom> wxChatrooms = PluginHandler.getWXChatrooms();
        if (wxChatrooms.size() < allChatRooms.size()) {
            /* 微信会话列表的聊天组少于plugin展示列表 */
            for (int i = 0; i < allChatRooms.size(); i++) {
                ChatRoom displayEntity = allChatRooms.get(i);
                boolean contains = false;
                for (int j = 0; j < wxChatrooms.size(); j++) {
                    if (wxChatrooms.get(j).getId().equals(displayEntity.getId())) {
                        displayEntity.setName(wxChatrooms.get(j).getName());
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    allChatRooms.remove(displayEntity);
                }
            }
        } else {
            for (int i = 0; i < wxChatrooms.size(); i++) {
                ChatRoom entity = wxChatrooms.get(i);
                boolean contains = false;
                for (int j = 0; j < allChatRooms.size(); j++) {
                    if (allChatRooms.get(j).getId().equals(entity.getId())) {
                        allChatRooms.get(j).setName(entity.getName());
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    allChatRooms.add(entity);
                }
            }
        }
        selectAutoReplyChatrooms(cbAllChatrom.isChecked());
    }

    /**
     * 选择自动回复的聊天组
     *
     * @param all
     */
    private void selectAutoReplyChatrooms(boolean all) {
        ArrayList<ChatRoom> select = new ArrayList<>();
        for (ChatRoom room : allChatRooms) {
            if (room.isChecked()) select.add(room);
        }
        PluginClient.tansferMessage(new PluginMessasge(PluginMessasge.NOTIFY_UPDATE_AUTO_REPLY_CHATROOM,
                new MultipleSendMessage<ChatRoom>(all, null, select)));
    }

    @Override
    public void onWechatSwitchAccount() {
        allChatRooms.clear();
        selectAutoReplyChatrooms(false);
    }

    @Override
    public void onPluginSwichInServiceWecahtAccount() {
        allChatRooms.clear();
        selectAutoReplyChatrooms(false);
    }

    @Override
    public void onWechatChatroomsChanged() {
        if (isVisibleToUser){
            loadWechatChatromms();
            adapter.notifyDataSetChanged();
        }
    }
}
