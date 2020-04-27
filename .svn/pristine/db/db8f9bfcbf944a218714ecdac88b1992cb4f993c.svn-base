package com.xiezhiai.wechatplugin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.utils.DensityUtils;

/**
 * Created by shijiwei on 2018/10/20.
 *
 * @Desc:
 */
public class CommonTopBar extends RelativeLayout implements View.OnClickListener {

    private TextView btnTopLeft;
    private TextView tvTopTitle;
    private TextView btnTopRight;

    private String mTitle;
    private String mLeftButtonText;
    private String mRightButtonText;
    private boolean mLeftButtonVisible;
    private boolean mRightButtonVisible;

    private CommonTopBarListener commonTopBarListener;

    public CommonTopBar(Context context) {
        this(context, null);
    }

    public CommonTopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initDefault(attrs);
        initView();
        initEvn();

        setBackgroundColor(Color.WHITE);
        setPadding(0,
                (int)  DensityUtils.getStatusBarHeight(context)
                , 0, 0);

    }

    private void initDefault(AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CommonTopBar);
        mTitle = ta.getString(R.styleable.CommonTopBar_title);
        mLeftButtonText = ta.getString(R.styleable.CommonTopBar_left_button_text);
        mRightButtonText = ta.getString(R.styleable.CommonTopBar_right_button_text);
        mLeftButtonVisible = ta.getBoolean(R.styleable.CommonTopBar_left_button_visible, false);
        mRightButtonVisible = ta.getBoolean(R.styleable.CommonTopBar_right_button_visible, false);
        ta.recycle();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_topbar, this);
        btnTopLeft = findViewById(R.id.btn_top_left);
        tvTopTitle = findViewById(R.id.tv_top_title);
        btnTopRight = findViewById(R.id.btn_top_right);

        btnTopLeft.setVisibility(mLeftButtonVisible ? VISIBLE : GONE);
        btnTopRight.setVisibility(mRightButtonVisible ? VISIBLE : GONE);
        btnTopLeft.setText(mLeftButtonText);
        btnTopRight.setText(mRightButtonText);
        tvTopTitle.setText(mTitle);
    }

    private void initEvn() {
        btnTopLeft.setOnClickListener(this);
        btnTopRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_left:
                if (commonTopBarListener != null) commonTopBarListener.onTopLeftButtonClick(v);
                break;
            case R.id.btn_top_right:
                if (commonTopBarListener != null) commonTopBarListener.onTopRightButtonClick(v);
                break;
        }
    }

    public void setCommonTopBarListener(CommonTopBarListener commonTopBarListener) {
        this.commonTopBarListener = commonTopBarListener;
    }

    public interface CommonTopBarListener {

        void onTopLeftButtonClick(View v);

        void onTopRightButtonClick(View v);
    }


    public View getTopLeftButton() {
        return btnTopLeft;
    }

    public TextView getTopTitle() {
        return tvTopTitle;
    }

    public TextView getTopRightButton() {
        return btnTopRight;
    }

    public void setTitle(String title) {
        tvTopTitle.setText(title);
    }

    public void setLeftButtonText(String title) {
        btnTopLeft.setText(title);
    }

    public void setRightButtonText(String title) {
        btnTopRight.setText(title);
    }

}
