package com.xiezhiai.wechatplugin.utils.sns.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shijiwei on 2018/10/31.
 *
 * @Desc:
 */
public class Comment implements Parcelable {

    public String authorName;
    public String content;
    public String toUser;
    public String authorId;
    public String toUserId;

    public Comment() {
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.authorName);
        dest.writeString(this.content);
        dest.writeString(this.toUser);
        dest.writeString(this.authorId);
        dest.writeString(this.toUserId);
    }

    protected Comment(Parcel in) {
        this.authorName = in.readString();
        this.content = in.readString();
        this.toUser = in.readString();
        this.authorId = in.readString();
        this.toUserId = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
