package com.xiezhiai.wechatplugin.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.model.wechat.UserInfo;
import com.xiezhiai.wechatplugin.model.xiezhi.WXUser;
import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.xiezhiai.wechatplugin.utils.image.loader.ImageLoader;

/**
 * Created by shijiwei on 2018/10/23.
 *
 * @Desc:
 */
public class UserAccountDialog extends Dialog implements View.OnClickListener {


    public enum FunctionType {
        BIND_WECHAT,
        BIND_WECHAT_SUCCESS,
        UNBIND_WECHAT,
    }

    private TextView tvTitle;
    private CircleImageView ivWechatAvatar;
    private TextView tvWechatName;
    private View layoutBindWechatTips;
    private View layoutUnbindWechatTips;
    private View layoutBindWechatSuccessTips;
    private View btnExit;
    private TextView btnCancel;
    private TextView btnConfirm;

    private FunctionType mFunctionType = FunctionType.BIND_WECHAT;
    private UserAccountDialogListener userAccountDialogListener;


    public UserAccountDialog(Context context) {
        this(context, R.style.simple_dialog);
    }

    public UserAccountDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.layout_dialog_user_account);

        tvTitle = findViewById(R.id.tv_title);
        ivWechatAvatar = findViewById(R.id.iv_wechat_avatar);
        tvWechatName = findViewById(R.id.tv_wechat_name);
        layoutBindWechatTips = findViewById(R.id.layout_bind);
        layoutBindWechatSuccessTips = findViewById(R.id.layout_bind_success);
        layoutUnbindWechatTips = findViewById(R.id.layout_unbind);
        btnCancel = findViewById(R.id.btn_cancel);
        btnConfirm = findViewById(R.id.btn_confirm);
        btnExit = findViewById(R.id.btn_exit);

        btnExit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                if (userAccountDialogListener != null)
                    userAccountDialogListener.onExit(this, v, mFunctionType);
                break;
            case R.id.btn_cancel:
                if (userAccountDialogListener != null)
                    userAccountDialogListener.onCancel(this, v, mFunctionType);
                break;
            case R.id.btn_confirm:
                if (userAccountDialogListener != null)
                    userAccountDialogListener.onConfirm(this, v, mFunctionType);
                break;
        }
    }

    @Override
    public void show() {
//        show(mFunctionType);
    }

    public void show(FunctionType mFunctionType, Object wxUser) {
        this.mFunctionType = mFunctionType;
        switch (mFunctionType) {
            case BIND_WECHAT:
                tvTitle.setText("| 绑定微信号");
                layoutBindWechatTips.setVisibility(View.VISIBLE);
                layoutBindWechatSuccessTips.setVisibility(View.GONE);
                layoutUnbindWechatTips.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setText("取消");
                btnConfirm.setText("确认绑定");
                tvWechatName.setText(((UserInfo) wxUser).getUserNickName());
                ImageLoader.with(getContext()).load(((UserInfo) wxUser).getAvatarURL(), ivWechatAvatar, R.mipmap.ic_avatar_placeholder);

                break;
            case BIND_WECHAT_SUCCESS:
                tvTitle.setText("| 绑定微信号");
                layoutBindWechatTips.setVisibility(View.GONE);
                layoutBindWechatSuccessTips.setVisibility(View.VISIBLE);
                layoutUnbindWechatTips.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                btnConfirm.setText("知道了");
                break;
            case UNBIND_WECHAT:
                tvTitle.setText("| 解绑微信号");
                layoutBindWechatTips.setVisibility(View.GONE);
                layoutBindWechatSuccessTips.setVisibility(View.GONE);
                layoutUnbindWechatTips.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setText("取消");
                btnConfirm.setText("解除绑定");
                tvWechatName.setText(((WXUser) wxUser).getName());
                ImageLoader.with(getContext()).load(((WXUser) wxUser).getAvatar(), ivWechatAvatar, R.mipmap.ic_avatar_placeholder);
                break;
        }
        super.show();
    }


    public interface UserAccountDialogListener {

        void onExit(Dialog dialog, View view, FunctionType type);

        void onCancel(Dialog dialog, View view, FunctionType type);

        void onConfirm(Dialog dialog, View view, FunctionType type);
    }

    public void setUserAccountDialogListener(UserAccountDialogListener userAccountDialogListener) {
        this.userAccountDialogListener = userAccountDialogListener;
    }
}
