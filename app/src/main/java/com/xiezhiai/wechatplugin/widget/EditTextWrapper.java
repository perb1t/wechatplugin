package com.xiezhiai.wechatplugin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.xiezhiai.wechatplugin.R;


/**
 * Created by shijiwei on 2018/4/17.
 */

public class EditTextWrapper extends android.support.v7.widget.AppCompatEditText {

    int drawableWidth;
    int drawableHeight;

    private Drawable[] compoundDrawables;

    public EditTextWrapper(Context context) {
        this(context, null);
    }

    public EditTextWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewWrapper);
        drawableWidth = a.getDimensionPixelOffset(R.styleable.TextViewWrapper_drawable_width, 40);
        drawableHeight = a.getDimensionPixelOffset(R.styleable.TextViewWrapper_drawable_height, 40);
        compoundDrawables = getCompoundDrawables();
        a.recycle();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < compoundDrawables.length; i++) {
            Drawable drawable = compoundDrawables[i];
            if (drawable != null) {
                drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            }
        }
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
    }

    public void setDrawables(Drawable l, Drawable t, Drawable r, Drawable b) {
        compoundDrawables[0] = l;
        compoundDrawables[1] = t;
        compoundDrawables[2] = r;
        compoundDrawables[3] = b;
        invalidate();
    }

}
