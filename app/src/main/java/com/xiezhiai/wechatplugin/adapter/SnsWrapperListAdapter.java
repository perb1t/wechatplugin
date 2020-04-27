package com.xiezhiai.wechatplugin.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.model.xiezhi.SnsWrapper;
import com.xiezhiai.wechatplugin.model.xiezhi.Snsqa;
import com.xiezhiai.wechatplugin.ui.aty.EditeGoodsActivity;
import com.xiezhiai.wechatplugin.ui.aty.GenerateGoodsActivity;
import com.xiezhiai.wechatplugin.utils.DensityUtils;
import com.xiezhiai.wechatplugin.utils.LogUtil;
import com.xiezhiai.wechatplugin.utils.image.loader.ImageLoader;
import com.xiezhiai.wechatplugin.utils.sns.bean.SnsInfo;
import com.xiezhiai.wechatplugin.widget.TextViewWrapper;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Created by shijiwei on 2018/11/6.
 *
 * @Desc:
 */
public class SnsWrapperListAdapter extends RecyclerView.Adapter<SnsWrapperListAdapter.ViewHolder> {

    private List<SnsWrapper> snsWrappers;
    private SnsWrapperAdapterListner snsWrapperAdapterListner;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SnsWrapperListAdapter(List<SnsWrapper> snsWrappers) {
        this.snsWrappers = snsWrappers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sns_mine_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        final SnsWrapper snsWrapper = snsWrappers.get(i);
        List<Snsqa> snsqas = snsWrapper.getSnsqas();
        final SnsInfo snsInfo = snsWrapper.getSnsInfo();

        holder.tvSnsTime.setText(sdf.format(snsInfo.timestamp * 1000));
        holder.tvSnsContent.setVisibility(TextUtils.isEmpty(snsInfo.getContent()) ? View.GONE : View.VISIBLE);
        holder.tvSnsContent.setText(snsInfo.getContent());

        if (snsInfo.mediaList != null && snsInfo.mediaList.size() != 0) {
            for (int j = 0; j < 3; j++) {
                holder.gallery.getChildAt(j).setVisibility(View.GONE);
            }
            int size = snsInfo.mediaList.size() > 3 ? 3 : snsInfo.mediaList.size();
            for (int j = 0; j < size; j++) {
                ImageLoader.with(holder.gallery.getContext()).load(snsInfo.mediaList.get(j), (ImageView) holder.gallery.getChildAt(j), R.mipmap.ic_photo_placeholder);
                holder.gallery.getChildAt(j).setVisibility(View.VISIBLE);
            }
            holder.gallery.setVisibility(View.VISIBLE);

        } else {
            holder.gallery.setVisibility(View.GONE);
        }

        if (snsqas != null && snsqas.size() != 0) {
            /* SNS */
            holder.tvSnsGoodsDesc.setText(getPrompt(snsqas.size()));
            holder.moreLayer.setVisibility(View.VISIBLE);
            holder.lvGoods.setVisibility(snsWrapper.isExpand() ? View.VISIBLE : View.GONE);
            holder.goods.clear();
            holder.goods.addAll(snsqas);
            holder.adapter.notifyDataSetChanged();
        } else {
            /* GOODS */
            holder.moreLayer.setVisibility(View.GONE);
            holder.tvSnsGoodsDesc.setText("暂未生成任何商品信息");
        }

        updateExpandButton(holder.btnExpand, snsWrapper.isExpand());
        holder.btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expand = holder.btnExpand.getText().toString().equals("收起");
                snsWrapper.setExpand(!expand);
                updateExpandButton(holder.btnExpand, !expand);
                holder.lvGoods.setVisibility(!expand ? View.VISIBLE : View.GONE);
            }
        });

        /* 生成商品 */
        holder.btnGenerateGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity context = (Activity) v.getContext();
                Intent intent = new Intent(context, GenerateGoodsActivity.class);
                intent.putExtra(GenerateGoodsActivity.KEY_DATA, snsWrappers.get(i));
                intent.putExtra(GenerateGoodsActivity.KEY_SNSWRAPEPR_INDEX, i);
                context.startActivityForResult(intent, URLManager.Add_Goods_qa.action);

            }
        });

        holder.adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.btn_delete_goods:
                        /* 删除商品问答 */
                        if (snsWrapperAdapterListner != null)
                            snsWrapperAdapterListner.onDeleteSnsqa(i, position);
                        break;
                    case R.id.btn_edit_goods:
                        /* 编辑商品问答 */
                        if (snsWrapperAdapterListner != null)
                            snsWrapperAdapterListner.onEditSnnqa(i, position);
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return snsWrappers == null ? 0 : snsWrappers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSnsTime;
        TextView tvSnsContent;
        TextView tvSnsGoodsDesc;
        TextView btnGenerateGoods;
        View moreLayer;
        TextViewWrapper btnExpand;
        RecyclerView lvGoods;
        LinearLayout gallery;
        GoodsListAdapter adapter;
        List<Snsqa> goods = new ArrayList<>();


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSnsTime = itemView.findViewById(R.id.tv_sns_time);
            tvSnsContent = itemView.findViewById(R.id.tv_sns_content);
            tvSnsGoodsDesc = itemView.findViewById(R.id.tv_sns_prompt);
            gallery = itemView.findViewById(R.id.gallery);
            moreLayer = itemView.findViewById(R.id.more_layer);
            btnGenerateGoods = itemView.findViewById(R.id.btn_generate_goods);
            btnExpand = itemView.findViewById(R.id.btn_expand);
            lvGoods = itemView.findViewById(R.id.lv_sns_bind_goods);
            lvGoods.setLayoutManager(new LinearLayoutManager(lvGoods.getContext()));
            adapter = new GoodsListAdapter(R.layout.item_generate_goods, goods,Color.WHITE);
            lvGoods.setAdapter(adapter);
            lvGoods.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.set(0,(int)DensityUtils.dp2px(1),0,0);
                }
            });
        }
    }


    private SpannableString getPrompt(int size) {
        SpannableString label = new SpannableString("已生成 " + size + " 条商品信息");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0300"));
        label.setSpan(colorSpan, 3, label.toString().indexOf(" 条商品信息"), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return label;
    }

    private void updateExpandButton(TextViewWrapper button, boolean isExpand) {
        button.setText(isExpand ? "收起" : "展开");
        @SuppressLint("ResourceType") Drawable d = button.getContext().getResources().getDrawable(isExpand ? R.mipmap.collapse : R.mipmap.expand);
        d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());
        button.setDrawables(d, null, null, null);
    }

    public interface SnsWrapperAdapterListner {

        void onDeleteSnsqa(int snsWrapperIndex, int snsqaIndex);

        void onEditSnnqa(int snsWrapperIndex, int snsqaIndex);

    }

    public void setSnsWrapperAdapterListner(SnsWrapperAdapterListner snsWrapperAdapterListner) {
        this.snsWrapperAdapterListner = snsWrapperAdapterListner;
    }
}
