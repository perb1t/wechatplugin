package com.xiezhiai.wechatplugin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by shijiwei on 2018/10/31.
 *
 * @Desc:
 */
public class WrapcontentListView extends ListView {

    public WrapcontentListView(Context context) {
        super(context);
    }

    public WrapcontentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapcontentListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
