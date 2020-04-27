package com.xiezhiai.wechatplugin.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiezhiai.wechatplugin.R;

/**
 * Created by shijiwei on 2018/11/9.
 *
 * @Desc:
 */
public class CommonDilalog extends Dialog implements View.OnClickListener {

    private ImageView btnTopCancel;
    private TextView btnCancel;
    private TextView btnConfirm;
    private TextView tvTips;

    private CommonDilalogListener commonDilalogListener;

    public CommonDilalog(Context context) {
        this(context, R.style.simple_dialog);
    }

    public CommonDilalog(Context context, CommonDilalogListener listener) {
        this(context);
        setCommonDilalogListener(listener);
    }

    public CommonDilalog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.layout_dialog_delete);

        btnConfirm = findViewById(R.id.btn_confirm);
        btnCancel = findViewById(R.id.btn_cancel);
        btnTopCancel = findViewById(R.id.btn_top_cancel);
        tvTips = findViewById(R.id.tv_tips);

        btnTopCancel.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

    }

    public void show(String tips, String comfirmText) {
        tvTips.setGravity(Gravity.CENTER);
        tvTips.setTextColor(Color.parseColor("#F76260"));
        tvTips.setText(tips);
        btnConfirm.setText(comfirmText);
        super.show();
    }

    public void show(String tips, int tipsTextColor, int gravity, String comfirmText) {
        tvTips.setGravity(gravity);
        tvTips.setTextColor(tipsTextColor);
        tvTips.setText(tips);
        btnConfirm.setText(comfirmText);
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_cancel:
                if (commonDilalogListener != null) commonDilalogListener.onTopCancel(this);
                break;
            case R.id.btn_cancel:
                if (commonDilalogListener != null) commonDilalogListener.onCancel(this);
                break;
            case R.id.btn_confirm:
                if (commonDilalogListener != null) commonDilalogListener.onConfim(this);
                break;
        }
    }


    public interface CommonDilalogListener {

        void onTopCancel(CommonDilalog dilalog);

        void onCancel(CommonDilalog dilalog);

        void onConfim(CommonDilalog dilalog);
    }

    public void setCommonDilalogListener(CommonDilalogListener Listener) {
        this.commonDilalogListener = Listener;
    }
}
