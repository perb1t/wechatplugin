package com.xiezhiai.wechatplugin.utils.sns.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shijiwei on 2018/10/31.
 *
 * @Desc:
 */
public class Like implements Parcelable {

    public String userName;
    public String userId;

    public Like() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.userId);
    }

    protected Like(Parcel in) {
        this.userName = in.readString();
        this.userId = in.readString();
    }

    public static final Parcelable.Creator<Like> CREATOR = new Parcelable.Creator<Like>() {
        @Override
        public Like createFromParcel(Parcel source) {
            return new Like(source);
        }

        @Override
        public Like[] newArray(int size) {
            return new Like[size];
        }
    };
}
