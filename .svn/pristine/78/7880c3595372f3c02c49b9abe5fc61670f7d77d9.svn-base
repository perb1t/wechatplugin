package com.xiezhiai.wechatplugin.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.xiezhiai.wechatplugin.R;

/**
 * Created by shijiwei on 2018/11/5.
 *
 * @Desc:
 */
public class LoadingDialog extends Dialog {

    private ImageView ivLoading;
    private LinearInterpolator interpolator;
    private Animation anim;

    public LoadingDialog(Context context) {
        this(context, R.style.simple_dialog);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.layout_dialog_loading);
        ivLoading = findViewById(R.id.iv_loading);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_loading);
        interpolator = new LinearInterpolator();
        anim.setInterpolator(interpolator);
    }


    @Override
    public void show() {
        ivLoading.clearAnimation();
        ivLoading.startAnimation(anim);
        super.show();
    }
}
