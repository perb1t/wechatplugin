package com.xiezhiai.wechatplugin.ui.aty;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.model.xiezhi.Snsqa;
import com.xiezhiai.wechatplugin.ui.base.BaseActivity;
import com.xiezhiai.wechatplugin.ui.frg.GroupSendMessageFragment;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.widget.CommonTopBar;

/**
 * Created by shijiwei on 2018/10/21.
 *
 * @Desc:
 */
public class SendMsgEditActivity extends BaseActivity implements CommonTopBar.CommonTopBarListener {

    private CommonTopBar commonTopBar;
    private EditText etContent;
    private TextView tvTextSizeTips;
    private int target;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_send_msg_eddit;
    }

    @Override
    public void initialData(Bundle savedInstanceState) {
        target = getIntent().getIntExtra(GroupSendMessageFragment.SEND_TARGET, GroupSendMessageFragment.SEND_MSG_TO_USER);
    }

    @Override
    public void initialView() {
        commonTopBar = findViewById(R.id.common_topbar);
        etContent = findViewById(R.id.et_content);
        tvTextSizeTips = findViewById(R.id.tv_text_size_tips);
    }

    @Override
    public void initialEvn() {
        commonTopBar.setCommonTopBarListener(this);
        etContent.addTextChangedListener(watcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        if (etContent.getText() == null || TextUtils.isEmpty(etContent.getText().toString())) {
            Toast.makeText(this, "请输入您想要发送内容", Toast.LENGTH_LONG).show();
            return;
        }

        if (target == GroupSendMessageFragment.SEND_MSG_TO_USER) {
            startActivity(SendMsgSelectUserTargetActivity.class);
        } else {
            startActivity(SendMsgSelectGroupTargetActivity.class);
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            tvTextSizeTips.setText("剩余可输入：" + (500 - length) + "字");
        }
    };


    private void startActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(GroupSendMessageFragment.SEND_TARGET, target);
        intent.putExtra(GroupSendMessageFragment.SEND_CONTENT, etContent.getText().toString());
        startActivity(intent);
    }
}
