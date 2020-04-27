package com.xiezhiai.wechatplugin.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.model.xiezhi.Commonqa;
import com.xiezhiai.wechatplugin.model.xiezhi.Snsqa;
import com.xiezhiai.wechatplugin.utils.image.loader.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/29.
 *
 * @Desc:
 */
public class SnsqaListAdapter extends RecyclerView.Adapter<SnsqaListAdapter.ViewHolder> {

    private List<Snsqa> snsqaList;
    private boolean isEdit = false;

    public SnsqaListAdapter(List<Snsqa> snsqaList) {
        this.snsqaList = snsqaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_snsqa_list, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        Snsqa entity = snsqaList.get(i);
        holder.cb.setVisibility(isEdit ? View.VISIBLE : View.GONE);

        holder.tvLabel.setBackgroundResource(entity.isOpen() ? R.mipmap.bg_on : R.mipmap.bg_off);
        holder.tvLabel.setText(entity.isOpen() ? "ON" : "OFF");
        holder.tvPrice.setText(getSpanablePrice(entity.getPrice()));
        holder.tvTitle.setText(entity.getName());
        holder.tvContent.setText(entity.getDescribe());
        holder.tvDesc.setText(entity.getPostage() + " | " + entity.getAfterSale());

        if (entity.getPhotos().size() != 0) {
            ImageLoader.with(holder.ivPhoto.getContext()).load(
                    entity.getPhotos().get(0).getWxURL(),
                    holder.ivPhoto,
                    R.mipmap.ic_photo_placeholder
            );
        } else {
            holder.ivPhoto.setImageResource(R.mipmap.ic_photo_placeholder);
        }

        holder.layoutGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snsqaListAdapterListner.onItemClick(i);
            }
        });
        holder.cb.setChecked(entity.isChecked());
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                snsqaList.get(i).setChecked(cb.isChecked());
                int size = 0;
                List<Snsqa> select = new ArrayList<>();
                for (int j = 0; j < snsqaList.size(); j++) {
                    if (snsqaList.get(j).isChecked()) {
                        select.add(snsqaList.get(j));
                        size++;
                    }
                }
                if (snsqaListAdapterListner != null)
                    snsqaListAdapterListner.onSelect(size, select);
            }
        });
    }

    @Override
    public int getItemCount() {
        return snsqaList == null ? 0 : snsqaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox cb;
        TextView tvLabel;
        TextView tvTitle;
        TextView tvContent;
        TextView tvPrice;
        TextView tvDesc;
        ImageView ivPhoto;
        LinearLayout layoutGoods;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb = itemView.findViewById(R.id.cb_snsqa);
            tvLabel = itemView.findViewById(R.id.tv_sns_qa_label);
            tvTitle = itemView.findViewById(R.id.tv_sns_qa_title);

            tvContent = itemView.findViewById(R.id.tv_sns_qa_content);
            tvPrice = itemView.findViewById(R.id.tv_sns_qa_price);
            tvDesc = itemView.findViewById(R.id.tv_sns_qa_desc);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            layoutGoods = itemView.findViewById(R.id.layout_goods);
        }
    }


    public interface SnsqaListAdapterListner {

        void onSelect(int size, List<Snsqa> select);

        void onItemClick(int position);
    }

    private SnsqaListAdapterListner snsqaListAdapterListner;

    public void setCommonqaListAdapterListner(SnsqaListAdapterListner snsqaListAdapterListner) {
        this.snsqaListAdapterListner = snsqaListAdapterListner;
    }

    private SpannableString getPrice(float price) {
        DecimalFormat df = new DecimalFormat(".00");
        SpannableString label = new SpannableString("¥" + df.format(price));
        int pointIndex = label.toString().indexOf(".");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0300"));
        label.setSpan(colorSpan, 0, label.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan smallSize = new RelativeSizeSpan(1.0f);
        RelativeSizeSpan lagerSize = new RelativeSizeSpan(1.4f);
        label.setSpan(smallSize, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        label.setSpan(lagerSize, 1, pointIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        label.setSpan(smallSize, pointIndex, label.toString().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return label;
    }

    private SpannableString getSpanablePrice(String price) {
        SpannableString label = new SpannableString("¥" + price);
        RelativeSizeSpan smallSize = new RelativeSizeSpan(1.0f);
        RelativeSizeSpan lagerSize = new RelativeSizeSpan(1.4f);
        label.setSpan(smallSize, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        label.setSpan(lagerSize, 1, label.toString().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return label;
    }

    public void isEdit(boolean edit) {
        this.isEdit = edit;
        notifyDataSetChanged();
    }

    public void checkAll(boolean all) {
        for (int i = 0; i < snsqaList.size(); i++) {
            snsqaList.get(i).setChecked(all);
        }
        notifyDataSetChanged();
        if (snsqaListAdapterListner != null)
            snsqaListAdapterListner.
                    onSelect(
                            all ? snsqaList.size() : 0,
                            all ? snsqaList : null
                    );
    }
}
