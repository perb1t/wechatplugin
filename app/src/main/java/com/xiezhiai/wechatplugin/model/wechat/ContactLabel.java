package com.xiezhiai.wechatplugin.model.wechat;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by shijiwei on 2018/10/19.
 *
 * @Desc:
 */
public class ContactLabel extends DataSupport implements Parcelable {

    private String labelID;
    private String labelName;
    private String labelPYFull;
    private String labelPYShort;
    private String createTime;
    private String isTemporary;
    private boolean isChecked;

    public ContactLabel() {
    }

    public ContactLabel(String labelName) {
        this.labelName = labelName;
    }

    public ContactLabel(String labelID, String labelName, String labelPYFull, String labelPYShort, String createTime, String isTemporary) {
        this.labelID = labelID;
        this.labelName = labelName;
        this.labelPYFull = labelPYFull;
        this.labelPYShort = labelPYShort;
        this.createTime = createTime;
        this.isTemporary = isTemporary;
    }

    public String getLabelID() {
        return labelID;
    }

    public void setLabelID(String labelID) {
        this.labelID = labelID;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelPYFull() {
        return labelPYFull;
    }

    public void setLabelPYFull(String labelPYFull) {
        this.labelPYFull = labelPYFull;
    }

    public String getLabelPYShort() {
        return labelPYShort;
    }

    public void setLabelPYShort(String labelPYShort) {
        this.labelPYShort = labelPYShort;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsTemporary() {
        return isTemporary;
    }

    public void setIsTemporary(String isTemporary) {
        this.isTemporary = isTemporary;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return " labelID:" + labelID +
                " labelName:" + labelName +
                " labelPYFull:" + labelPYFull +
                " labelPYShort:" + labelPYShort +
                " createTime:" + createTime +
                " isTemporary:" + isTemporary
                ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.labelID);
        dest.writeString(this.labelName);
        dest.writeString(this.labelPYFull);
        dest.writeString(this.labelPYShort);
        dest.writeString(this.createTime);
        dest.writeString(this.isTemporary);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    protected ContactLabel(Parcel in) {
        this.labelID = in.readString();
        this.labelName = in.readString();
        this.labelPYFull = in.readString();
        this.labelPYShort = in.readString();
        this.createTime = in.readString();
        this.isTemporary = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public static final Creator<ContactLabel> CREATOR = new Creator<ContactLabel>() {
        @Override
        public ContactLabel createFromParcel(Parcel source) {
            return new ContactLabel(source);
        }

        @Override
        public ContactLabel[] newArray(int size) {
            return new ContactLabel[size];
        }
    };
}
