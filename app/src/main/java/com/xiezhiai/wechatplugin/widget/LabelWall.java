package com.xiezhiai.wechatplugin.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.model.wechat.ContactLabel;
import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.xiezhiai.wechatplugin.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/22.
 *
 * @Desc:
 */
public class LabelWall extends ViewGroup {

    private List<List<View>> children = new ArrayList<>();
    private List<Integer> perLineHeight = new ArrayList<>();
    private List<ContactLabel> contactLabelList = new ArrayList<>();


    public LabelWall(Context context) {
        this(context, null);
    }

    public LabelWall(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelWall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        perLineHeight.clear();
        children.clear();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int actualWidth = 0;
        int actualHeight = getPaddingTop() + getPaddingBottom();

        int currentLineWidth = 0;
        int currentLineHeight = 0;

        List<View> perLineChidren = new ArrayList<>();

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHight = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;

            if (actualHeight == 0 || currentLineWidth + childWidth > width - getPaddingLeft() - getPaddingRight()) {

                currentLineHeight = childHight;

                actualWidth = Math.max(currentLineWidth, width);
                actualHeight += currentLineHeight;

                perLineChidren = new ArrayList<>();

                children.add(perLineChidren);
                perLineHeight.add(childHight);

                perLineChidren.add(child);
                currentLineWidth = childWidth;
            } else {
                perLineChidren.add(child);
                currentLineWidth += childWidth;
            }
        }

//        LogUtil.e("==== " + width + "," + height + " | " + actualWidth + "," + actualHeight);
        setMeasuredDimension(actualWidth, actualHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        /* 标签墙的padding */
        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (int i = 0; i < perLineHeight.size(); i++) {
            List<View> perLineChildren = children.get(i);
            for (int j = 0; j < perLineChildren.size(); j++) {
                View child = perLineChildren.get(j);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cl = left + lp.leftMargin;
                int ct = top + lp.topMargin;
                int cr = cl + child.getMeasuredWidth();
                int cb = ct + child.getMeasuredHeight();
                child.layout(cl, ct, cr, cb);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            left = getPaddingLeft();
            top += perLineHeight.get(i);
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    public void setLabels(List<ContactLabel> labels) {
        if (labels == null || labels.size() == 0) return;
        removeAllViews();
        contactLabelList.clear();
        contactLabelList.addAll(labels);
        for (int i = 0; i < labels.size(); i++) {
            addLabelView(labels.get(i));
        }

    }

    @SuppressLint("NewApi")
    private void addLabelView(ContactLabel label) {
        CheckBox child = new CheckBox(getContext());
        MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.rightMargin = (int) DensityUtils.dp2px(11);
        lp.bottomMargin = (int) DensityUtils.dp2px(11);
        child.setPadding((int) DensityUtils.dp2px(12), (int) DensityUtils.dp2px(6), (int) DensityUtils.dp2px(12), (int) DensityUtils.dp2px(6));
        child.setLayoutParams(lp);
        child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        child.setText(label.getLabelName());
        child.setButtonDrawable(null);
        child.setChecked(label.isChecked());
        child.setTextColor(getResources().getColor(child.isChecked() ? R.color.white : R.color.txt_999999));
        child.setBackground(getResources().getDrawable(R.drawable.selector_send_target_check_background));
        child.setTag(getChildCount());
        addView(child);

        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                cb.setTextColor(getResources().getColor(cb.isChecked() ? R.color.white : R.color.txt_999999));
                contactLabelList.get((Integer) cb.getTag()).setChecked(cb.isChecked());

                boolean allUnselected = true;
                for (int i = 0; i < contactLabelList.size(); i++) {
                    if (contactLabelList.get(i).isChecked()) {
                        allUnselected = false;
                        break;
                    }
                }
                if (labelWallListener != null) labelWallListener.onAllUnselected(allUnselected);
            }
        });
    }


    /**
     * 取消选择
     */
    public void cancelSelected() {
        for (int i = 0; i < getChildCount(); i++) {
            CheckBox cb = (CheckBox) getChildAt(i);
            cb.setChecked(false);
            cb.setTextColor(getResources().getColor(cb.isChecked() ? R.color.white : R.color.txt_999999));
        }
    }

    /**
     * 返回获取的标签
     *
     * @return
     */
    public ArrayList<ContactLabel> getSelectedLabels() {
        ArrayList<ContactLabel> result = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            CheckBox child = (CheckBox) getChildAt(i);
            if (child.isChecked()) {
                String name = child.getText().toString();
                for (int j = 0; j < contactLabelList.size(); j++) {
                    if (contactLabelList.get(j).getLabelName().equals(name)) {
                        result.add(contactLabelList.get(j));
                    }
                }
            }
        }
        return result;
    }

    public interface LabelWallListener {

        void onAllUnselected(boolean flag);
    }

    private LabelWallListener labelWallListener;

    public void setLabelWallListener(LabelWallListener labelWallListener) {
        this.labelWallListener = labelWallListener;
    }
}
