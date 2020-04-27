package com.xiezhiai.wechatplugin.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.model.xiezhi.Snsqa;

import java.security.PublicKey;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/29.
 *
 * @Desc:
 */
public class GoodsListAdapter extends BaseQuickAdapter<Snsqa, BaseViewHolder> {

    private int bgRid = 0;

    public GoodsListAdapter(int layoutResId, @Nullable List<Snsqa> data) {
        super(layoutResId, data);
    }

    public GoodsListAdapter(int layoutResId, @Nullable List<Snsqa> data, int bgRid) {
        super(layoutResId, data);
        this.bgRid = bgRid;
    }

    @Override
    protected void convert(BaseViewHolder helper, Snsqa item) {

        if (bgRid != 0) helper.getView(R.id.sns_qa_layer).setBackgroundColor(bgRid);
        TextView label = helper.getView(R.id.tv_goods_label);
        TextView name = helper.getView(R.id.tv_goods_name);
        TextView price = helper.getView(R.id.tv_goods_price);
        TextView postage = helper.getView(R.id.tv_goods_postage);
        TextView afterSale = helper.getView(R.id.tv_goods_aftersales);
        TextView detail = helper.getView(R.id.tv_goods_details);

        helper.addOnClickListener(R.id.btn_delete_goods);
        helper.addOnClickListener(R.id.btn_edit_goods);
//        label.setText("商品" + number2Character((helper.getAdapterPosition() + 1) + ""));
        label.setText("商品" + (helper.getAdapterPosition() + 1));
        name.setText(item.getName());
        price.setText(item.getPrice());
        postage.setText(item.getPostage());
        afterSale.setText(item.getAfterSale());
        detail.setText(item.getDescribe());

    }

    /**
     * 数字转汉字
     *
     * @param number
     * @return
     */
    private String number2Character(String number) {
        String[] s1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] s2 = {"十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String result = "";
        int n = number.length();
        for (int i = 0; i < n; i++) {
            int num = number.charAt(i) - '0';
            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
        }
        return result;
    }
}
