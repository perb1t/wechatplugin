package com.xiezhiai.wechatplugin.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.core.Config;
import com.xiezhiai.wechatplugin.func.transfer.PluginClient;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.func.transfer.PluginMessasge;
import com.xiezhiai.wechatplugin.hooker.WechatActivityHooker;
import com.xiezhiai.wechatplugin.hooker.WechatHooker;
import com.xiezhiai.wechatplugin.model.wechat.ChatRoom;
import com.xiezhiai.wechatplugin.model.wechat.ContactLabel;
import com.xiezhiai.wechatplugin.model.xiezhi.MultipleSendMessage;
import com.xiezhiai.wechatplugin.ui.app.WechatPluginApplication;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.ui.frg.GroupSendMessageFragment;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;
import com.xiezhiai.wechatplugin.widget.LabelWall;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/22.
 *
 * @Desc:
 */
public class SendMsgSelectUserTargetActivity extends BaseActivity implements CommonTopBar.CommonTopBarListener, View.OnClickListener, LabelWall.LabelWallListener {

    private static final String TAG = "SendMsgSelecUserTargetA";

    private CommonTopBar commonTopBar;
    private LinearLayout container;
    private ScrollView scrollView;
    private LabelWall labelWall;
    private CheckBox cbAllFriend;
    private CheckBox cbPartFriend;
    private View btnSend;
    private LinearLayout layoutAllFriend;
    private LinearLayout layoutPartFriend;
    private int containerHeight;
    private int scrollViewHeight;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_send_msg_select_user_target;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {
    }

    @Override
    public void initialView() {

        commonTopBar = findViewById(R.id.common_topbar);
        container = findViewById(R.id.container);
        scrollView = findViewById(R.id.scroll_view);
        labelWall = findViewById(R.id.label_wall);
        cbAllFriend = findViewById(R.id.cb_all_friend);
        cbPartFriend = findViewById(R.id.cb_part_friend);
        btnSend = findViewById(R.id.btn_send);
        layoutAllFriend = findViewById(R.id.layout_all_friend);
        layoutPartFriend = findViewById(R.id.layout_part_friend);

    }

    @Override
    public void initialEvn() {
        layoutAllFriend.setOnClickListener(this);
        layoutPartFriend.setOnClickListener(this);
        commonTopBar.setCommonTopBarListener(this);
        cbPartFriend.setOnClickListener(this);
        cbAllFriend.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        labelWall.setLabelWallListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        labelWall.setLabels(PluginHandler.getWXContactsLabels());
//        reMeasureLayout();
    }

    @Override
    public void onBackKeyPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        PluginHandler.resetWXContactsLabels();
        super.onDestroy();
    }

    @Override
    public void onNetworkStateChanged(int type, boolean isAvailable) {

    }

    @Override
    public void onTopLeftButtonClick(View v) {
        finish();
    }

    @Override
    public void onTopRightButtonClick(View v) {

    }

    @Override
    public void onAllUnselected(boolean flag) {
        cbPartFriend.setChecked(!flag);
        if (!flag) cbAllFriend.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_all_friend:
                cbAllFriend.setChecked(true);
                cbPartFriend.setChecked(false);
                labelWall.cancelSelected();
                break;
            case R.id.layout_part_friend:
                cbPartFriend.setChecked(true);
                cbAllFriend.setChecked(false);
                break;

            case R.id.cb_all_friend:
                cbPartFriend.setChecked(false);
                labelWall.cancelSelected();
                break;
            case R.id.cb_part_friend:
                cbAllFriend.setChecked(false);
                if (!((CheckBox) v).isChecked()) labelWall.cancelSelected();
                break;

            case R.id.btn_send:

                if (!cbAllFriend.isChecked() && !cbPartFriend.isChecked()) {
                    showMsg("请选择发送目标");
                    return;
                }

                ArrayList<ContactLabel> selectedLabels = labelWall.getSelectedLabels();
                if (cbPartFriend.isChecked()) {
                    if (selectedLabels.size() == 0) {
                        showMsg("请选择发送目标");
                        return;
                    }
                }
                notifyHooker2SendMsg(cbAllFriend.isChecked(), selectedLabels);
                WechatPluginApplication.getApplication().backToSpecificActivity(MainActivity.class);
                break;
        }
    }


    /**
     * 通知微信发送消息
     *
     * @param all
     * @param selectedLabels
     */
    private void notifyHooker2SendMsg(boolean all, ArrayList<ContactLabel> selectedLabels) {
        String content = getIntent().getStringExtra(GroupSendMessageFragment.SEND_CONTENT);
        PluginClient.tansferMessage(new PluginMessasge(
                PluginMessasge.NOTIFY_MULTIPLE_SEND_MESSAGE_TO_USER,
                new MultipleSendMessage<ContactLabel>(all, content, selectedLabels)));
    }

    private void reMeasureLayout() {
        labelWall.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams lpContainer = (LinearLayout.LayoutParams) container.getLayoutParams();
                LinearLayout.LayoutParams lpLvContainer = (LinearLayout.LayoutParams) scrollView.getLayoutParams();
                containerHeight = Math.max(containerHeight, container.getHeight());
                scrollViewHeight = Math.max(scrollViewHeight, scrollView.getHeight());
                int h = labelWall.getHeight();
                if (h < scrollViewHeight) {
                    lpContainer.weight = 0;
                    lpContainer.height = containerHeight - (scrollViewHeight - h);
                    lpLvContainer.height = scrollViewHeight - (scrollViewHeight - h);
                } else {
                    lpContainer.weight = 1;
                    lpLvContainer.height = LinearLayout.LayoutParams.MATCH_PARENT;
                }
            }
        });
    }
}
