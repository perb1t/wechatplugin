package com.xiezhiai.wechatplugin.utils.sns.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.xiezhiai.wechatplugin.utils.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;


public class SnsInfo implements Parcelable {

    public static final String IMAGE = "1";

    /* Base info*/
    public String snsId;
    public String head;
    public String type;
    public String sourceType;
    public byte[] contentBuf;
    public byte[] attrBuf;


    /* Detail */
    public String id = "";
    public String authorName = "";
    public String content = "";
    public String authorId = "";
    public ArrayList<Like> likes = new ArrayList<Like>();
    public ArrayList<Comment> comments = new ArrayList<Comment>();
    public ArrayList<String> mediaList = new ArrayList<String>();
    public String rawXML = "";
    public long timestamp = 0;

    public SnsInfo() {
    }

    public SnsInfo(String snsId, String head, String type, String sourceType, byte[] contentBuf, byte[] attrBuf) {
        this.snsId = snsId;
        this.head = head;
        this.type = type;
        this.sourceType = sourceType;
        this.contentBuf = contentBuf;
        this.attrBuf = attrBuf;
    }

    public byte[] getContentBuf() {
        return contentBuf;
    }

    public void setContentBuf(byte[] contentBuf) {
        this.contentBuf = contentBuf;
    }

    public byte[] getAttrBuf() {
        return attrBuf;
    }

    public void setAttrBuf(byte[] attrBuf) {
        this.attrBuf = attrBuf;
    }

    public String getSnsId() {
        return snsId;
    }

    public void setSnsId(String snsId) {
        this.snsId = snsId;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public ArrayList<Like> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Like> likes) {
        this.likes = likes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ArrayList<String> mediaList) {
        if (mediaList != null && mediaList.size() != 0)
            this.mediaList.addAll(mediaList);
    }

    public String getRawXML() {
        return rawXML;
    }

    public void setRawXML(String rawXML) {
        this.rawXML = rawXML;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isMediaEmpty() {
        return mediaList == null || mediaList.size() == 0;
    }

    public void print() {
        LogUtil.e("微信朋友圈详情", "================================");
        LogUtil.e("微信朋友圈详情", "id: " + this.id);
        LogUtil.e("微信朋友圈详情", "authorId: " + this.authorId);
        LogUtil.e("微信朋友圈详情", "Author: " + this.authorName);
        LogUtil.e("微信朋友圈详情", "timestamp: " + this.timestamp);
        LogUtil.e("微信朋友圈详情", "type: " + this.type);
        LogUtil.e("微信朋友圈详情", "sourceType: " + this.sourceType);
        LogUtil.e("微信朋友圈详情", "Content: " + this.content);
        LogUtil.e("微信朋友圈详情", "Likes:");
        for (int i = 0; i < likes.size(); i++) {
            LogUtil.e("微信朋友圈详情", likes.get(i).userName);
        }
        LogUtil.e("微信朋友圈详情", "Comments:");
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            LogUtil.e("微信朋友圈详情", "CommentAuthor: " + comment.authorName + "; CommentContent: " + comment.content + "; ToUser: " + comment.toUser);
        }
        LogUtil.e("微信朋友圈详情", "Media List:");
        for (int i = 0; i < mediaList.size(); i++) {
            LogUtil.e("微信朋友圈详情", mediaList.get(i));
        }
    }

    public SnsInfo clone() {
        SnsInfo newSns = new SnsInfo();
        newSns.id = this.id;
        newSns.authorName = this.authorName;
        newSns.content = this.content;
        newSns.authorId = this.authorId;
        newSns.likes = new ArrayList<Like>(this.likes);
        newSns.comments = new ArrayList<Comment>(this.comments);
        newSns.mediaList = new ArrayList<String>(this.mediaList);
        newSns.rawXML = this.rawXML;
        newSns.timestamp = this.timestamp;
        return newSns;
    }


    public void copy(SnsInfo source) {
        if (source == null) return;
        this.snsId = source.snsId;
        this.head = source.head;
        this.type = source.type;
        this.sourceType = source.sourceType;
        this.contentBuf = source.contentBuf;
        this.attrBuf = source.attrBuf;
    }


    public void clear() {
        id = "";
        authorName = "";
        content = "";
        authorId = "";
        likes.clear();
        comments.clear();
        mediaList.clear();
        rawXML = "";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.snsId);
        dest.writeString(this.head);
        dest.writeString(this.type);
        dest.writeString(this.sourceType);
        dest.writeString(this.id);
        dest.writeString(this.authorName);
        dest.writeString(this.content);
        dest.writeString(this.authorId);
        dest.writeTypedList(this.likes);
        dest.writeTypedList(this.comments);
        dest.writeStringList(this.mediaList);
        dest.writeString(this.rawXML);
        dest.writeLong(this.timestamp);
        dest.writeByteArray(this.contentBuf);
        dest.writeByteArray(this.attrBuf);
    }

    protected SnsInfo(Parcel in) {
        this.snsId = in.readString();
        this.head = in.readString();
        this.type = in.readString();
        this.sourceType = in.readString();
        this.id = in.readString();
        this.authorName = in.readString();
        this.content = in.readString();
        this.authorId = in.readString();
        this.likes = in.createTypedArrayList(Like.CREATOR);
        this.comments = in.createTypedArrayList(Comment.CREATOR);
        this.mediaList = in.createStringArrayList();
        this.rawXML = in.readString();
        this.timestamp = in.readLong();
        this.contentBuf = in.createByteArray();
        this.attrBuf = in.createByteArray();
    }

    public static final Creator<SnsInfo> CREATOR = new Creator<SnsInfo>() {
        @Override
        public SnsInfo createFromParcel(Parcel source) {
            return new SnsInfo(source);
        }

        @Override
        public SnsInfo[] newArray(int size) {
            return new SnsInfo[size];
        }
    };
}
