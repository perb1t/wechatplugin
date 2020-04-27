package com.xiezhiai.wechatplugin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiezhiai.wechatplugin.func.nohttp.URLManager;
import com.xiezhiai.wechatplugin.model.xiezhi.SnsWrapper;
import com.xiezhiai.wechatplugin.model.xiezhi.Snsqa;
import com.xiezhiai.wechatplugin.ui.aty.EditeGoodsActivity;
import com.xiezhiai.wechatplugin.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import com.xiezhiai.wechatplugin.R;

/**
 * Created by shijiwei on 2018/11/8.
 *
 * @Desc:
 */
public class GenerateGoodsAdapter extends RecyclerView.Adapter<GenerateGoodsAdapter.ViewHolder> {

    private SnsWrapper snsWrapper;

    public GenerateGoodsAdapter(SnsWrapper snsWrapper) {
        this.snsWrapper = snsWrapper;
    }

    @NonNull
    @Override
    public GenerateGoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_generate_goods, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final GenerateGoodsAdapter.ViewHolder holder, final int i) {
        if (i == 0) {
            holder.snsPhotoLayer.setVisibility(View.VISIBLE);
            holder.snsqaLayer.setVisibility(View.GONE);
            holder.photos.clear();
            holder.photos.addAll(generatePhotos(snsWrapper));
            holder.adapter.notifyDataSetChanged();
            holder.btnGenerateGoods.setEnabled(holder.adapter.isCheckedPhotos());

            holder.adapter.setSnsPhotoListener(new SnsPhotoAdapter.SnsPhotoListener() {

                @Override
                public void onPhotoClick(boolean isCheckedPhoto) {
                    holder.btnGenerateGoods.setEnabled(isCheckedPhoto);
                }

                @Override
                public void onDelete(SnsPhotoAdapter.ViewHolder holder) {

                }
            });

            /* 生成商品 */
            holder.btnGenerateGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity context = (Activity) v.getContext();
                    Intent intent = new Intent(context, EditeGoodsActivity.class);
                    intent.putStringArrayListExtra(EditeGoodsActivity.KEY_PHOTO, holder.adapter.getCheckedPhotos());
                    intent.putExtra(EditeGoodsActivity.KEY_SNS_ID, snsWrapper.getSnsInfo().snsId);
                    context.startActivityForResult(intent, URLManager.Add_Goods_qa.action);
                }
            });

        } else {
            holder.snsPhotoLayer.setVisibility(View.GONE);
            holder.snsqaLayer.setVisibility(View.VISIBLE);
            holder.tvGoodsLabel.setText("商品" + Integer.toString(i));
            Snsqa qa = snsWrapper.getSnsqas().get(i - 1);
            holder.tvGoodsName.setText(qa.getName());
            holder.tvGoodsPrice.setText(qa.getPrice());
            holder.tvGoodsPostage.setText(qa.getPostage());
            holder.tvGoodsAferSale.setText(qa.getAfterSale());
            holder.tvGoodsDetail.setText(qa.getDescribe());

            holder.btnDeleteSnsqa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (generateGoodsAdapterListener != null)
                        generateGoodsAdapterListener.onDeleteSnsqa(i);
                }
            });

            holder.btnEditSnsqa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (generateGoodsAdapterListener != null)
                        generateGoodsAdapterListener.onEditSnsqa(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return snsWrapper == null ? 0 : (1 + snsWrapper.getSnsqas().size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        View snsPhotoLayer;
        View snsqaLayer;
        Button btnGenerateGoods;
        TextView tvGoodsLabel;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        TextView tvGoodsPostage;
        TextView tvGoodsAferSale;
        TextView tvGoodsDetail;
        ImageView btnDeleteSnsqa;
        ImageView btnEditSnsqa;

        RecyclerView lvSnsPhoto;
        SnsPhotoAdapter adapter;
        List<SnsPhotoAdapter.Photo> photos = new ArrayList<>();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            snsPhotoLayer = itemView.findViewById(R.id.sns_photo_layer);
            snsqaLayer = itemView.findViewById(R.id.sns_qa_layer);
            btnGenerateGoods = itemView.findViewById(R.id.btn_generate_goods);

            tvGoodsLabel = itemView.findViewById(R.id.tv_goods_label);
            tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvGoodsPrice = itemView.findViewById(R.id.tv_goods_price);
            tvGoodsPostage = itemView.findViewById(R.id.tv_goods_postage);
            tvGoodsAferSale = itemView.findViewById(R.id.tv_goods_aftersales);
            tvGoodsDetail = itemView.findViewById(R.id.tv_goods_details);
            btnDeleteSnsqa = itemView.findViewById(R.id.btn_delete_goods);
            btnEditSnsqa = itemView.findViewById(R.id.btn_edit_goods);

            lvSnsPhoto = itemView.findViewById(R.id.lv_snn_photo);
            lvSnsPhoto.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
            adapter = new SnsPhotoAdapter(photos);
            lvSnsPhoto.setAdapter(adapter);
            lvSnsPhoto.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    int i = parent.indexOfChild(view);
                    outRect.set(0, (int) DensityUtils.dp2px(5), 0, 0);
                }
            });

        }
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


    /**
     * @param snsWrapper
     * @return
     */
    private List<SnsPhotoAdapter.Photo> generatePhotos(SnsWrapper snsWrapper) {
        List<SnsPhotoAdapter.Photo> result = new ArrayList<>();
        if (snsWrapper != null) {
            List<Snsqa> snsqas = snsWrapper.getSnsqas();
            ArrayList<String> mediaList = snsWrapper.getSnsInfo().mediaList;
            for (int i = 0; i < mediaList.size(); i++) {
                boolean shot = false;
                for (int j = 0; j < snsqas.size(); j++) {
                    List<Snsqa.Photo> photos = snsqas.get(j).getPhotos();
                    for (int k = 0; k < photos.size(); k++) {
                        if (mediaList.get(i).equals(photos.get(k).getWxURL())) {
                            result.add(new SnsPhotoAdapter.Photo(mediaList.get(i), (j + 1)));
                            shot = true;
                            continue;
                        }
                    }
                }
                if (!shot) result.add(new SnsPhotoAdapter.Photo(mediaList.get(i), (-1)));
            }

        }
        return result;
    }

    public interface GenerateGoodsAdapterListener {

        void onDeleteSnsqa(int position);

        void onEditSnsqa(int position);
    }

    private GenerateGoodsAdapterListener generateGoodsAdapterListener;

    public void setGenerateGoodsAdapterListener(GenerateGoodsAdapterListener generateGoodsAdapterListener) {
        this.generateGoodsAdapterListener = generateGoodsAdapterListener;
    }
}
