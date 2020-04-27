package com.xiezhiai.wechatplugin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiezhiai.wechatplugin.R;
import com.xiezhiai.wechatplugin.utils.image.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/11/8.
 *
 * @Desc:
 */
public class SnsPhotoAdapter extends RecyclerView.Adapter<SnsPhotoAdapter.ViewHolder> {


    private List<Photo> photos;
    private SnsPhotoListener snsPhotoListener;
    /* 图片可删除操作 */
    private boolean operable;

    public SnsPhotoAdapter(List<Photo> photos) {
        this(photos, false);
    }

    public SnsPhotoAdapter(List<Photo> photos, boolean operable) {
        this.photos = photos;
        this.operable = operable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sns_photo, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        final Photo photo = photos.get(i);

        ImageLoader.with(holder.ivSnsPhoto.getContext()).load(
                photo.URL,
                holder.ivSnsPhoto,
                R.mipmap.ic_photo_placeholder
        );

        if (!operable) {

            holder.checkedLayer.setVisibility(photo.qaIndex == -1 ? View.GONE : View.VISIBLE);
            holder.ivSnsPhoto.setEnabled(photo.qaIndex == -1);
            holder.tvPhotoqaIndex.setText("商品" + photo.qaIndex);
            holder.ivFlagChecked.setVisibility(photo.isChecked ? View.VISIBLE : View.GONE);

            holder.ivSnsPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check(i, !photo.isChecked);
                    if (snsPhotoListener != null)
                        snsPhotoListener.onPhotoClick(isCheckedPhotos());
                }
            });
        } else {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (snsPhotoListener != null)
                        snsPhotoListener.onDelete(holder);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivSnsPhoto;
        View checkedLayer;
        TextView tvPhotoqaIndex;
        View btnDelete;
        View ivFlagChecked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSnsPhoto = itemView.findViewById(R.id.iv_sns_photo);
            checkedLayer = itemView.findViewById(R.id.checked_layer);
            tvPhotoqaIndex = itemView.findViewById(R.id.tv_photo_qa_index);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            ivFlagChecked = itemView.findViewById(R.id.iv_flag_checked);
        }
    }


    public interface SnsPhotoListener {

        void onPhotoClick(boolean isCheckedPhoto);

        void onDelete(ViewHolder holder);

    }

    public void setSnsPhotoListener(SnsPhotoListener snsPhotoListener) {
        this.snsPhotoListener = snsPhotoListener;
    }


    /**
     * 选择图片
     *
     * @param posistion
     * @param isCheck
     */
    public void check(int posistion, boolean isCheck) {
        photos.get(posistion).isChecked = isCheck;
        notifyItemChanged(posistion);
    }

    /**
     * 删除图片
     */
    public void delete(ViewHolder holder) {
        photos.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
    }

    public ArrayList<String> getCheckedPhotos() {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).isChecked) {
                result.add(photos.get(i).URL);
            }
        }
        return result;
    }

    public boolean isCheckedPhotos() {
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).isChecked) {
                return true;
            }
        }
        return false;
    }

    public static class Photo {
        public String URL;
        public int qaIndex = -1;
        public boolean isChecked;

        public Photo(String URL) {
            this(URL, -1);
        }

        public Photo(String URL, int qaIndex) {
            this.URL = URL;
            this.qaIndex = qaIndex;
        }

    }


}
