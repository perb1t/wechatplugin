package com.xiezhiai.wechatplugin.model.xiezhi;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/30.
 *
 * @Desc:
 */
public class Snsqa implements Parcelable {

    private boolean isChecked;

    @JSONField(name = "goods_id")
    private String id;
    @JSONField(name = "monent_id")
    private String snsId;
    private String name;
    private String price;
    @JSONField(name = "description")
    private String describe;
    private String postage;
    @JSONField(name = "after_sale")
    private String afterSale;
    private int status; // 商品状态 0：关闭，1：开启
    @JSONField(name = "img_list")
    private List<Photo> photos = new ArrayList<>();
    @JSONField(name = "img_url_list")
    private List<String> wxPhotoURL = new ArrayList<>();


    public Snsqa() {
    }

    public Snsqa(String name, String describe, String price, String postage, String afterSale) {
        this.name = name;
        this.describe = describe;
        this.price = price;
        this.postage = postage;
        this.afterSale = afterSale;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSnsId() {
        return snsId;
    }

    public void setSnsId(String snsId) {
        this.snsId = snsId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        if (photos != null && photos.size() != 0)
            this.photos.addAll(photos);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isOpen() {
        return status == 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getAfterSale() {
        return afterSale;
    }

    public void setAfterSale(String afterSale) {
        this.afterSale = afterSale;
    }

    public List<String> getWxPhotoURL() {
        return wxPhotoURL;
    }

    public void setWxPhotoURL(List<String> wxPhotoURL) {
        if (wxPhotoURL != null && wxPhotoURL.size() != 0)
            this.wxPhotoURL.addAll(wxPhotoURL);
    }

    public void copy(Snsqa qa) {

    }



    public static class Photo implements Parcelable {
        @JSONField(name = "wx_url")
        private String wxURL;
        @JSONField(name = "url")
        private String URL;

        public Photo() {
        }

        public Photo(String wxURL, String URL) {
            this.wxURL = wxURL;
            this.URL = URL;
        }

        public String getWxURL() {
            return wxURL;
        }

        public void setWxURL(String wxURL) {
            this.wxURL = wxURL;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.wxURL);
            dest.writeString(this.URL);
        }

        protected Photo(Parcel in) {
            this.wxURL = in.readString();
            this.URL = in.readString();
        }

        public static final Creator<Photo> CREATOR = new Creator<Photo>() {
            @Override
            public Photo createFromParcel(Parcel source) {
                return new Photo(source);
            }

            @Override
            public Photo[] newArray(int size) {
                return new Photo[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.snsId);
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.describe);
        dest.writeString(this.postage);
        dest.writeString(this.afterSale);
        dest.writeInt(this.status);
        dest.writeList(this.photos);
        dest.writeStringList(this.wxPhotoURL);
    }

    protected Snsqa(Parcel in) {
        this.isChecked = in.readByte() != 0;
        this.id = in.readString();
        this.snsId = in.readString();
        this.name = in.readString();
        this.price = in.readString();
        this.describe = in.readString();
        this.postage = in.readString();
        this.afterSale = in.readString();
        this.status = in.readInt();
        this.photos = new ArrayList<Photo>();
        in.readList(this.photos, Photo.class.getClassLoader());
        this.wxPhotoURL = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Snsqa> CREATOR = new Parcelable.Creator<Snsqa>() {
        @Override
        public Snsqa createFromParcel(Parcel source) {
            return new Snsqa(source);
        }

        @Override
        public Snsqa[] newArray(int size) {
            return new Snsqa[size];
        }
    };
}
