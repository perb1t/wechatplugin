package com.xiezhiai.wechatplugin.ui.frg;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiezhiai.wechatplugin.R;

import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleRequest;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;
import com.xiezhiai.wechatplugin.func.transfer.PluginClient;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.func.transfer.PluginMessasge;
import com.xiezhiai.wechatplugin.model.xiezhi.Cookie;
import com.xiezhiai.wechatplugin.model.xiezhi.WXUser;

import com.xiezhiai.wechatplugin.ui.aty.MainActivity;
import com.xiezhiai.wechatplugin.ui.aty.UserAccountManageActivity;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.ui.base.BaseFragment;

import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.xiezhiai.wechatplugin.utils.LogUtil;

import com.xiezhiai.wechatplugin.utils.image.loader.ImageLoader;
import com.xiezhiai.wechatplugin.widget.CircleImageView;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;
import com.xiezhiai.wechatplugin.widget.UserAccountDialog;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.HashMap;
import java.util.List;


/**
 * Created by shijiwei on 2018/10/19.
 *
 * @Desc:
 */
public class MyFragment extends BaseFragment implements CommonTopBar.CommonTopBarListener, View.OnClickListener, UserAccountDialog.UserAccountDialogListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MyFragment";

    private CommonTopBar commonTopBar;
    private CircleImageView ivWechatAvatar;
    private TextView tvBindWechatTips;
    private TextView tvWechatName;
    private TextView tvWechatId;
    private TextView tvWechatSnsCount;
    private TextView tvWechatLuckMoneyCount;
    private TextView tvWechatTransferCount;
    private View layoutChattingReply;

    private CheckBox cbAutoReply;
    private CheckBox cbChattingAutoReply;
    private CheckBox cbAutoReceiveLuckMoney;
    private CheckBox cbAutoReceiveTransfer;

    private UserAccountDialog userAccountDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_my;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {
        registerUIRefreshReceiver();
    }

    @Override
    public void initialView(View view) {

        userAccountDialog = ((BaseActivity) getActivity()).userAccountDialog;
        commonTopBar = view.findViewById(R.id.common_topbar);
        ivWechatAvatar = view.findViewById(R.id.iv_wechat_avatar);
        tvBindWechatTips = view.findViewById(R.id.tv_bind_wechat_tips);
        tvWechatName = view.findViewById(R.id.tv_wechat_name);
        tvWechatId = view.findViewById(R.id.tv_wechat_id);
        tvWechatSnsCount = view.findViewById(R.id.tv_wechat_sns_count);
        tvWechatLuckMoneyCount = view.findViewById(R.id.tv_wechat_luckmoeny_count);
        tvWechatTransferCount = view.findViewById(R.id.tv_wechat_transfer_count);
        layoutChattingReply = view.findViewById(R.id.layout_chatting_reply);
        cbAutoReply = view.findViewById(R.id.cb_auto_reply);
        cbChattingAutoReply = view.findViewById(R.id.cb_chatting_auto_reply);
        cbAutoReceiveLuckMoney = view.findViewById(R.id.cb_auto_receive_luckmoney);
        cbAutoReceiveTransfer = view.findViewById(R.id.cb_auto_receive_transfer);

    }

    @Override
    public void initialEvn() {

        commonTopBar.setCommonTopBarListener(this);
        ivWechatAvatar.setOnClickListener(this);
        userAccountDialog.setUserAccountDialogListener(this);

        cbAutoReply.setOnCheckedChangeListener(this);
        cbChattingAutoReply.setOnCheckedChangeListener(this);
        cbAutoReceiveLuckMoney.setOnCheckedChangeListener(this);
        cbAutoReceiveTransfer.setOnCheckedChangeListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
        /* 登录状态 */
        if (PluginHandler.cookie.isPluginLogin() && !PluginHandler.cookie.isInitialize()) {
            PluginHandler.cookie.setInitialize(true);
            getLooadingDialog().show();
            getWechatList();
        }
        initTopProfile();
    }


    @Override
    public void onDestroy() {
        unregisterUIRefreshReceiver();
        super.onDestroy();
    }

    @Override
    public void onTopLeftButtonClick(View v) {

    }

    @Override
    public void onTopRightButtonClick(View v) {
        if (PluginHandler.checkOperationVaild(getActivity())) {
            getActivity().startActivity(new Intent(getActivity(), UserAccountManageActivity.class));
        }
    }


    @Override
    public void onExit(Dialog dialog, View view, UserAccountDialog.FunctionType type) {
        dialog.dismiss();
    }

    @Override
    public void onCancel(Dialog dialog, View view, UserAccountDialog.FunctionType type) {
        dialog.dismiss();
    }

    @Override
    public void onConfirm(Dialog dialog, View view, UserAccountDialog.FunctionType type) {
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_wechat_avatar:
                if (PluginHandler.checkOperationVaild(getActivity())) {
                    getActivity().startActivity(new Intent(getActivity(), UserAccountManageActivity.class));
                }
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_auto_reply:
                layoutChattingReply.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                if (!isChecked) cbChattingAutoReply.setChecked(false);
                PluginHandler.cookie.getPermission().setAutoReplyUser(isChecked);
                break;
            case R.id.cb_chatting_auto_reply:
                PluginHandler.cookie.getPermission().setAutoReplyUserChatting(isChecked);
                break;
            case R.id.cb_auto_receive_luckmoney:
                PluginHandler.cookie.getPermission().setAutoReceiveLuckMoney(isChecked);
                break;
            case R.id.cb_auto_receive_transfer:
                PluginHandler.cookie.getPermission().setAutoReceiveTransfer(isChecked);
                break;
        }
        ((MainActivity) getActivity()).updateSettingConfig(PluginHandler.cookie.getInServiceWXUserId());
    }

    @Override
    public void onHttpSucceed(int what, SimpleResult ret, Response response) {
        super.onHttpSucceed(what, ret, response);
        if (ret == null) return;
        if (what == URLManager.Get_Wechat_User_LIST.action) {
            if (ret.getCode() == SimpleResult.SUCCESS) {
                try {
                    JSONObject data = (JSONObject) JSON.parse(ret.getData());
                    JSONArray wxUserArray = data.getJSONArray("wx_user");
                    List<WXUser> wxUsers = JSONArray.parseArray(wxUserArray.toJSONString(), WXUser.class);
                    PluginHandler.updateBindWXUsers(getActivity(),wxUsers, PluginHandler.cookie.getCacheInServiceWXUserId(getActivity()));
                    initTopProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showMsg(ret.getMessage());
                tvBindWechatTips.setVisibility(View.VISIBLE);
                tvWechatName.setVisibility(View.GONE);
                tvWechatId.setVisibility(View.GONE);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivWechatAvatar.getLayoutParams();
                ivWechatAvatar.setImageResource(R.mipmap.icon_add_wechat_account);
                lp.topMargin = (int) DensityUtils.dp2px(32);
            }
        } else if (what == URLManager.Get_Setting_Config.action) {
            if (ret.getCode() == SimpleResult.SUCCESS) {
                try {
                    Cookie.Permission permission = JSON.parseObject(ret.getData(), Cookie.Permission.class);
                    PluginHandler.cookie.getPermission().copy(permission);
                    initUIPermission(permission);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                showMsg(ret.getMessage());
            }
        } else if (what == URLManager.Get_Incoming_Record.action) {
            if (ret.getCode() == SimpleResult.SUCCESS) {
                JSONObject data = (JSONObject) JSON.parse(ret.getData());
                String totalLuckMoneyCount = data.getString("total_red");
                String totalTransferMoeny = data.getString("total_trans");
                tvWechatLuckMoneyCount.setText(totalLuckMoneyCount);
                tvWechatTransferCount.setText(totalTransferMoeny);
            } else {
                showMsg(ret.getMessage());
            }
        }
    }

    @Override
    public void onHttpFailed(int what, SimpleResult ret, Response response) {
        super.onHttpFailed(what, ret, response);
        if (ret == null) return;
        showMsg(ret.getMessage());
    }

    @Override
    public void onHttpFinish(int what) {
        super.onHttpFinish(what);
        getLooadingDialog().dismiss();
    }

    /**
     * 获取绑定微信列表
     */
    private void getWechatList() {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Get_Wechat_User_LIST.getURL(), URLManager.Get_Wechat_User_LIST.method, SimpleResult.class);
        addTask2Queue(
                URLManager.Get_Wechat_User_LIST.action,
                request
        );
    }

    /**
     * 获取配置
     *
     * @param wxID
     */
    private void getSettingConfig(String wxID) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Get_Setting_Config.getURL(), URLManager.Get_Setting_Config.method, SimpleResult.class);
        HashMap<String, Object> p = new HashMap<>();
        p.put("wx_id", wxID);
        request.setRequestBodyAsJson(p);
        addTask2Queue(
                URLManager.Get_Setting_Config.action,
                request
        );
    }

    /**
     * 获取红包收益记录
     */
    private void getIncomingRecords() {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Get_Incoming_Record.getURL(), URLManager.Get_Incoming_Record.method, SimpleResult.class);
        HashMap<String, Object> p = new HashMap<>();
        p.put("incoming_type", "all");
        request.setRequestBodyAsJson(p);
        addTask2Queue(URLManager.Get_Incoming_Record.action, request);
    }

    /**
     * 获取朋友圈发布条数
     */
    private void getWechatSnsSize() {
        tvWechatSnsCount.setText(PluginHandler.getWXSnses(getActivity()).size() + "");
    }


    /**
     * 初始化顶部信息布局
     */
    private void initTopProfile() {
        WXUser inServiceWXUser = PluginHandler.getInServiceWXUser();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ivWechatAvatar.getLayoutParams();
        if (inServiceWXUser != null) {
            getSettingConfig(inServiceWXUser.getId());
            getIncomingRecords();
            getWechatSnsSize();
            tvBindWechatTips.setVisibility(View.GONE);
            tvWechatName.setVisibility(View.VISIBLE);
            tvWechatId.setVisibility(View.VISIBLE);
            lp.topMargin = (int) DensityUtils.dp2px(21);
            tvWechatName.setText(inServiceWXUser.getName());
            tvWechatId.setText("ID: " + inServiceWXUser.getId());
            ImageLoader.with(getActivity()).load(inServiceWXUser.getAvatar(), ivWechatAvatar, R.mipmap.ic_avatar_placeholder);
        } else {
            tvWechatLuckMoneyCount.setText("0");
            tvWechatTransferCount.setText("0.0");
            tvWechatSnsCount.setText("0");
            tvBindWechatTips.setVisibility(View.VISIBLE);
            tvWechatName.setVisibility(View.GONE);
            tvWechatId.setVisibility(View.GONE);
            ivWechatAvatar.setImageResource(R.mipmap.icon_add_wechat_account);
            lp.topMargin = (int) DensityUtils.dp2px(32);
            PluginHandler.cookie.getPermission().clear();
        }
        initUIPermission(PluginHandler.cookie.getPermission());
    }

    /**
     * 初始化界面权限
     *
     * @param permission
     */
    private void initUIPermission(Cookie.Permission permission) {
        cbAutoReply.setChecked(permission.isAutoReplyUser());
        cbChattingAutoReply.setChecked(permission.isAutoReplyUserChatting());
        cbAutoReceiveTransfer.setChecked(permission.isAutoReceiveTransfer());
        cbAutoReceiveLuckMoney.setChecked(permission.isAutoReceiveLuckMoney());
    }


    public class UIRefreshReceiver extends BroadcastReceiver {

        public static final String ACTION_UI_REFRESH = "com.xiezhiai.wechatplugin.UI_Refresh_Receiver";
        public static final String TYPE = "type";
        public static final String ADD_INCOMING = "add_incoming";
        public static final String ADD_SNS = "add_sns";


        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                LogUtil.e(TAG + " 红包、转账、朋友圈 数据更新了 ");
                if (intent.getAction().equals(ACTION_UI_REFRESH)) {
                    if (PluginHandler.cookie.isPluginLogin()) {
                        switch (intent.getStringExtra(TYPE)) {
                            case ADD_INCOMING:
                                getIncomingRecords();
                                break;
                            case ADD_SNS:
                                getWechatSnsSize();
                                break;
                        }
                    }
                }
            }
        }
    }

    private UIRefreshReceiver uiRefreshReceiver = new UIRefreshReceiver();

    private void registerUIRefreshReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UIRefreshReceiver.ACTION_UI_REFRESH);
        getActivity().registerReceiver(uiRefreshReceiver, filter);
    }

    private void unregisterUIRefreshReceiver() {
        getActivity().unregisterReceiver(uiRefreshReceiver);
    }
}
