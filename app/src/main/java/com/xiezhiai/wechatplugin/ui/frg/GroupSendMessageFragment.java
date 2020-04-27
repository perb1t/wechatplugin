package com.xiezhiai.wechatplugin.ui.frg;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.ui.aty.SendMsgEditActivity;
import com.xiezhiai.wechatplugin.ui.base.BaseFragment;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by shijiwei on 2018/10/20.
 *
 * @Desc:
 */
public class GroupSendMessageFragment extends BaseFragment implements View.OnClickListener {

    private View btnSendMsgToUser;
    private View btnSendMsgToGroup;

    public static final int SEND_MSG_TO_USER = 0;
    public static final int SEND_MSG_TO_CHATROOM = 1;
    public static final String SEND_TARGET = "send_target";
    public static final String SEND_CONTENT = "send_content";

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_group_send_msg;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {

    }

    @Override
    public void initialView(View view) {

        btnSendMsgToUser = view.findViewById(R.id.btn_send_msg_2_user);
        btnSendMsgToGroup = view.findViewById(R.id.btn_send_msg_2_group);
    }

    @Override
    public void initialEvn() {
        btnSendMsgToUser.setOnClickListener(this);
        btnSendMsgToGroup.setOnClickListener(this);
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_send_msg_2_user:
                if (TextUtils.isEmpty(PluginHandler.wxLastLoginUser.getUserName()) || !PluginHandler.wxLastLoginUser.isLogin()) {
                    showMsg("请确认您当前选中的微信已登录");
                    return;
                }
                if (PluginHandler.checkOperationVaild(getActivity()))
                    startActivity(SendMsgEditActivity.class, SEND_MSG_TO_USER);
                break;
            case R.id.btn_send_msg_2_group:
                if (TextUtils.isEmpty(PluginHandler.wxLastLoginUser.getUserName()) || !PluginHandler.wxLastLoginUser.isLogin()) {
                    showMsg("请确认您当前选中的微信已登录");
                    return;
                }
                if (PluginHandler.checkOperationVaild(getActivity()))
                    startActivity(SendMsgEditActivity.class, SEND_MSG_TO_CHATROOM);
                break;
        }
    }


    private void startActivity(Class cls, int target) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra(SEND_TARGET, target);
        getActivity().startActivity(intent);
    }
}
