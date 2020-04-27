package com.xiezhiai.wechatplugin.ui.aty;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.adapter.WechatUserAccountAdapter;
import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleRequest;
import com.xiezhiai.wechatplugin.func.nohttp.base.SimpleResult;
import com.xiezhiai.wechatplugin.func.transfer.PluginHandler;
import com.xiezhiai.wechatplugin.model.wechat.UserInfo;
import com.xiezhiai.wechatplugin.model.xiezhi.Cookie;
import com.xiezhiai.wechatplugin.model.xiezhi.WXUser;
import com.xiezhiai.wechatplugin.ui.app.WechatPluginApplication;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.ui.interf.PluginUIHooker;
import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.PluginLoginManager;
import com.xiezhiai.wechatplugin.utils.VerifyUtil;
import com.xiezhiai.wechatplugin.utils.image.cache.disk.DiskLruCacheHelper;
import com.xiezhiai.wechatplugin.utils.image.loader.ImageLoader;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;
import com.xiezhiai.wechatplugin.widget.UserAccountDialog;
import com.yanzhenjie.nohttp.BitmapBinary;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.rest.Response;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/23.
 *
 * @Desc:
 */
public class UserAccountManageActivity extends BaseActivity implements CommonTopBar.CommonTopBarListener, View.OnClickListener, UserAccountDialog.UserAccountDialogListener, WechatUserAccountAdapter.BindedWXUserListener {


    private CommonTopBar commonTopBar;
    private FrameLayout container;
    private ListView lvWechatUserAccount;
    private View btnAboutUs;
    private View btnVersion;
    private View btntLogout;
    private View btntOperateGuide;
    private View lvFooter;
    private TextView tvLoginPhone;

    private WechatUserAccountAdapter adapter;
    private int containerHeight;
    private boolean doBindWechat = false;
    private String lastInServiceWXUserId;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_account_manage;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {
        lastInServiceWXUserId = PluginHandler.cookie.getInServiceWXUserId();
        adapter = new WechatUserAccountAdapter(PluginHandler.bindWXUserList, this);
    }

    @Override
    public void initialView() {

        commonTopBar = findViewById(R.id.common_topbar);
        container = findViewById(R.id.container);
        btnAboutUs = findViewById(R.id.btn_about_us);
        btnVersion = findViewById(R.id.btn_version);
        btntLogout = findViewById(R.id.btn_logout);
        btntOperateGuide = findViewById(R.id.btn_operate_guide);
        tvLoginPhone = findViewById(R.id.tv_login_phone_number);
        lvWechatUserAccount = findViewById(R.id.lv_wechat_user_account);
        lvFooter = LayoutInflater.from(this).inflate(R.layout.item_user_account_footer, null);
        lvWechatUserAccount.addFooterView(lvFooter);
        lvWechatUserAccount.setAdapter(adapter);


        lvWechatUserAccount.post(new Runnable() {
            @Override
            public void run() {
                containerHeight = container.getHeight();
                reMeasureLayout();
            }
        });
    }

    @Override
    public void initialEvn() {

        commonTopBar.setCommonTopBarListener(this);
        btntLogout.setOnClickListener(this);
        btnAboutUs.setOnClickListener(this);
        btnVersion.setOnClickListener(this);
        btntOperateGuide.setOnClickListener(this);
        lvFooter.setOnClickListener(this);
        userAccountDialog.setUserAccountDialogListener(this);
        adapter.setBindedWXUserListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvLoginPhone.setText(getLoginPhone((String) PluginLoginManager.getLastLoginAccount(this)[0]));
        getWechatList();
    }

    @Override
    public void onBackKeyPressed() {
        exit();
    }

    @Override
    public void onNetworkStateChanged(int type, boolean isAvailable) {

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
        switch (type) {
            case BIND_WECHAT:
                bindWechat(PluginHandler.wxLastLoginUser);
                break;
            case UNBIND_WECHAT:
                if (preDelWXUser != null) {
                    deleteWechat(preDelWXUser.getId());
                } else {
                    showMsg("解绑失败");
                }
                break;
            case BIND_WECHAT_SUCCESS:
                break;
        }

        dialog.dismiss();

    }

    private WXUser preDelWXUser;

    @Override
    public void onUnbind(WXUser wxUser, int position) {
        preDelWXUser = wxUser;
        userAccountDialog.show(UserAccountDialog.FunctionType.UNBIND_WECHAT, wxUser);
    }

    @Override
    public void onSwitchServiceWXUser(WXUser wxUser, int position) {
        if (!wxUser.getId().equals(PluginHandler.cookie.getCacheInServiceWXUserId(this))){
            PluginHandler.switchInServiceWXUser(this, wxUser.getId());
            adapter.notifyDataSetChanged();
            swichInServiceWecahtAccount();
        }
    }

    @Override
    public void onTopLeftButtonClick(View v) {
        exit();
    }

    @Override
    public void onTopRightButtonClick(View v) {
        if (adapter.getCount() == 0) return;
        TextView tv = (TextView) v;
        String label = tv.getText().toString();
        adapter.edit(label.equals("编辑"));
        tv.setText(label.equals("编辑") ? "完成" : "编辑");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                logout(Integer.parseInt(PluginHandler.cookie.getUserID()));
                logout(false);
                finish();
                break;
            case R.id.btn_bind_wechat:
                if (isLoginWXUserIsBinded(PluginHandler.wxLastLoginUser)) {
                    showMsg("该设备上登陆的微信号已绑定");
                } else if (TextUtils.isEmpty(PluginHandler.wxLastLoginUser.getUserName()) || !PluginHandler.wxLastLoginUser.isLogin()) {
                    showMsg("请登录微信");
                } else {
                    ImageLoader.with(this).remove(PluginHandler.wxLastLoginUser.getAvatarURL());
                    userAccountDialog.show(UserAccountDialog.FunctionType.BIND_WECHAT, PluginHandler.wxLastLoginUser);
                }
                break;
            case R.id.btn_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.btn_version:
                break;
            case R.id.btn_operate_guide:
                startActivity(new Intent(this, OperateGuideActicity.class));
                break;
        }
    }


    @Override
    public void onHttpSucceed(int what, SimpleResult ret, Response response) {
        super.onHttpSucceed(what, ret, response);
        if (ret != null) {
            if (what == URLManager.Get_Wechat_User_LIST.action) {
                /* 获取微信列表 */
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    try {
                        JSONObject data = (JSONObject) JSON.parse(ret.getData());
                        JSONArray wxUserArray = data.getJSONArray("wx_user");
                        List<WXUser> wxUsers = JSONArray.parseArray(wxUserArray.toJSONString(), WXUser.class);
                        String lastInServiceWXUserId = PluginHandler.cookie.getInServiceWXUserId();
                        if (doBindWechat) {
                            doBindWechat = false;
                            PluginHandler.updateBindWXUsers(this, wxUsers, PluginHandler.wxLastLoginUser.getUserName());
                        } else {
                            PluginHandler.updateBindWXUsers(this, wxUsers, PluginHandler.cookie.getCacheInServiceWXUserId(this));
                        }
                        String inServiceWXUserId = PluginHandler.cookie.getInServiceWXUserId();
                        getSettingConfig(inServiceWXUserId);
                        adapter.notifyDataSetChanged();
                        reMeasureLayout();
                    } catch (Exception e) {

                    }

                } else {
                    showMsg(ret.getMessage());
                }
            } else if (what == URLManager.Bind_Wechat_User.action) {
                /*  绑定微信  */
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    userAccountDialog.show(UserAccountDialog.FunctionType.BIND_WECHAT_SUCCESS, null);
                    doBindWechat = true;
                    getWechatList();
                } else {
                    showMsg(ret.getMessage());
                }
            } else if (what == URLManager.Delete_Wechat_User.action) {
                /*  解绑微信  */
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    for (int i = 0; i < PluginHandler.bindWXUserList.size(); i++) {
                        if (PluginHandler.bindWXUserList.get(i).getId().equals(preDelWXUser.getId())) {
                            PluginHandler.bindWXUserList.remove(i);
                            PluginHandler.switchInServiceWXUser(this, PluginHandler.cookie.getInServiceWXUserId());
                            adapter.notifyDataSetChanged();
                            reMeasureLayout();
                        }
                    }
                } else {
                    showMsg(ret.getMessage());
                }
            } else if (what == URLManager.Get_Setting_Config.action) {
                if (ret.getCode() == SimpleResult.SUCCESS) {
                    try {
                        Cookie.Permission permission = JSON.parseObject(ret.getData(), Cookie.Permission.class);
                        PluginHandler.cookie.getPermission().copy(permission);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    showMsg(ret.getMessage());
                }
            }
        }
    }

    @Override
    public void onHttpFailed(int what, SimpleResult ret, Response response) {
        super.onHttpFailed(what, ret, response);
        if (ret != null) {
            showMsg(ret.getMessage());
        }
    }

    @Override
    public void onHttpFinish(int what) {
        super.onHttpFinish(what);
    }

    /**
     * 退出登录
     *
     * @param userId
     */
    private void logout(int userId) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Logout.getURL(), URLManager.Logout.method, SimpleResult.class);
        HashMap<String, Object> p = new HashMap<>();
        p.put("user_id", userId);
        request.setRequestBodyAsJson(p);
        addTask2Queue(
                URLManager.Logout.action,
                request
        );
    }

    /**
     * 绑定微信
     *
     * @param wxUser
     */
    private void bindWechat(UserInfo wxUser) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Bind_Wechat_User.getURL(), URLManager.Bind_Wechat_User.method, SimpleResult.class);
        request.setMultipartFormEnable();
        request.add("wx_id", wxUser.getUserName());
        request.add("wx_name", wxUser.getUserNickName());
        File avatar = new File(wxUser.getAvatarURL());
        if (!avatar.exists()) {
            request.add("img", new BitmapBinary(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_avatar_placeholder), "ic_avatar_placeholder", "image/png"));
        } else {
            request.add("img", new FileBinary(avatar, wxUser.getAvatarURL(), "image/png"));
        }
        addTask2Queue(
                URLManager.Bind_Wechat_User.action,
                request
        );
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
     * 删除绑定的微信号
     *
     * @param wxID
     */
    private void deleteWechat(String... wxID) {
        SimpleRequest<SimpleResult> request = new SimpleRequest<SimpleResult>(URLManager.Delete_Wechat_User.getURL(), URLManager.Delete_Wechat_User.method, SimpleResult.class);
        HashMap<String, Object> p = new HashMap<>();
        p.put("wx_ids", wxID);
        request.setRequestBodyAsJson(p);
        addTask2Queue(
                URLManager.Delete_Wechat_User.action,
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


    private String getLoginPhone(String phone) {
        if (TextUtils.isEmpty(phone) || phone.length() != 11) return phone;
        StringBuilder builder = new StringBuilder(phone);
        builder.insert(3, " ");
        builder.insert(8, " ");
        return builder.toString();
    }


    private void reMeasureLayout() {
        int actualHeight = (int) (PluginHandler.bindWXUserList.size() * DensityUtils.dp2px(71) + DensityUtils.dp2px(70));
        LinearLayout.LayoutParams containerLp = (LinearLayout.LayoutParams) container.getLayoutParams();
        if (actualHeight < containerHeight) {
            containerLp.weight = 0;
            containerLp.height = actualHeight;
        } else {
            containerLp.weight = 1;
        }
    }

    private boolean isLoginWXUserIsBinded(UserInfo user) {
        for (WXUser entity : PluginHandler.bindWXUserList) {
            try {
                if (entity.getId().equals(user.getUserName())) return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    private void swichInServiceWecahtAccount() {
        String inServiceWXUserId = PluginHandler.cookie.getInServiceWXUserId();
        if (!lastInServiceWXUserId.equals(inServiceWXUserId)) {
            for (PluginUIHooker hooker : PluginHandler.pluginUIHookers) {
                if (hooker != null) hooker.onPluginSwichInServiceWecahtAccount();
            }
        }
    }

    /**
     * 退出页面
     */
    private void exit() {
        swichInServiceWecahtAccount();
        finish();
    }
}
