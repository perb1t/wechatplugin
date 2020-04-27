package com.xiezhiai.wechatplugin.ui.aty;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.utils.AppUtil;
import com.xiezhiai.wechatplugin.utils.XposedManager;
import com.xiezhiai.wechatplugin.utils.file.FileTransfer;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;


/**
 * Created by shijiwei on 2018/11/19.
 *
 * @Desc:
 */
public class OperateGuideActicity extends BaseActivity implements View.OnClickListener, CommonTopBar.CommonTopBarListener {

    private CommonTopBar commonTopBar;
    private TextView btnDownloadWechat;
    private TextView btnDownloadXposed;

    private TextView tvEtcSetp1;
    private TextView tvEtcSetp2;
    private TextView tvEtcSetp3;
    private TextView tvEtcSetp4;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_operate_guide;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {

    }

    @Override
    public void initialView() {
        commonTopBar = findViewById(R.id.common_topbar);
        btnDownloadWechat = findViewById(R.id.btn_download_wechat);
        btnDownloadXposed = findViewById(R.id.btn_download_xposed);
        tvEtcSetp1 = findViewById(R.id.tv_etc_step_1);
        tvEtcSetp2 = findViewById(R.id.tv_etc_step_2);
        tvEtcSetp3 = findViewById(R.id.tv_etc_step_3);
        tvEtcSetp4 = findViewById(R.id.tv_etc_step_4);

        tvEtcSetp1.setText(generateSpanna(tvEtcSetp1.getText().toString(), 10, 18));
        tvEtcSetp2.setText(generateSpanna(tvEtcSetp2.getText().toString(), 13, 19));
        tvEtcSetp4.setText(generateSpanna(tvEtcSetp4.getText().toString(), 9, 13));
    }

    @Override
    public void initialEvn() {
        btnDownloadWechat.setOnClickListener(this);
        btnDownloadXposed.setOnClickListener(this);
        commonTopBar.setCommonTopBarListener(this);

    }

    @Override
    public void onBackKeyPressed() {
        finish();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download_wechat:

                break;
            case R.id.btn_download_xposed:
                boolean install = XposedManager.checkHasInstallXposed(this, XposedManager.Xposed[0]);
                if (install) {
                    showMsg("Xposed框架已安装");
                } else {
                    XposedManager.installXposed(this, XposedManager.Xposed[1], new FileTransfer.FileTransferListener() {
                        @Override
                        public void copy(int progress) {

                        }

                        @Override
                        public void complete(boolean success, Object bundle) {
                            AppUtil.installApk(OperateGuideActicity.this, XposedManager.Xposed[1]);
                        }
                    });
                }
                break;
        }
    }

    private SpannableString generateSpanna(String str, int start, int end) {
        SpannableString label = new SpannableString(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#F76260"));
        label.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return label;
    }


}
