package com.xiezhiai.wechatplugin.model.wechat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shijiwei on 2018/10/22.
 *
 * @Desc:
 */
public class ChatRoom implements Parcelable {

    private String name;
    private String id;
    private boolean isChecked;

    public ChatRoom() {
    }

    public ChatRoom(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    protected ChatRoom(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ChatRoom> CREATOR = new Parcelable.Creator<ChatRoom>() {
        @Override
        public ChatRoom createFromParcel(Parcel source) {
            return new ChatRoom(source);
        }

        @Override
        public ChatRoom[] newArray(int size) {
            return new ChatRoom[size];
        }
    };
}
