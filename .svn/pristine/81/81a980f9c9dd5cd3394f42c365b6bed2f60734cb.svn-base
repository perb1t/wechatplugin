package com.xiezhiai.wechatplugin.model.wechat;

import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import com.xiezhiai.wechatplugin.core.Config;
import com.yanzhenjie.permission.Setting;

/**
 * Created by shijiwei on 2018/10/12.
 *
 * @Desc:
 */
public class UserInfo implements Parcelable {

    /* 微信号id */
    private String userName;
    /* 微信昵称 */
    private String userNickName;
    /* 获取当前微信登录用户的数据库文件父级文件夹名（MD5("mm"+uin) ） */
    private String uinEnc;
    /* 自己修改的微信id */
    private String alias;
    /* 备注 */
    private String conRemark;
    /* 好友分类标签 */
    private String labels;

    private boolean isLogin;
    private String avatarPath;
    private String avatarName;
    private String avatarURL;

    public UserInfo() {
    }

    public UserInfo(String userName, String userNickName, String alias, String conRemark) {
        this.userName = userName;
        this.userNickName = userNickName;
        this.alias = alias;
        this.conRemark = conRemark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUinEnc() {
        return uinEnc;
    }

    public void setUinEnc(String uinEnc) {
        this.uinEnc = uinEnc;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getConRemark() {
        return conRemark;
    }

    public void setConRemark(String conRemark) {
        this.conRemark = conRemark;
    }

    public String getLabels() {
        return labels == null ? "" : labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getAvatarPath() {
        return avatarPath == null ? "" : avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getAvatarURL() {
        return Environment.getExternalStorageDirectory() + Config.EXTERNAL_DIR + "/" + getUserName() + ".jpg";
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public void copy(UserInfo user) {
        setUserName(user.getUserName());
        setUserNickName(user.getUserNickName());
        setAlias(user.getAlias());
        setLogin(user.isLogin());
        setAvatarPath(user.getAvatarPath());
        setLabels(user.getLabels());
        setUinEnc(user.getUinEnc());
        setConRemark(user.getConRemark());
        setAvatarName(user.getAvatarName());
    }

    @Override
    public String toString() {
        return "userNickName: " + userNickName
                + "  userName: " + userName
                + "  alias: " + alias
                + "  conRemark: " + conRemark
                + "  labels: " + labels
                + "  isLogin: " + isLogin
                + "  avatarPath: " + avatarPath

                ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.userNickName);
        dest.writeString(this.uinEnc);
        dest.writeString(this.alias);
        dest.writeString(this.conRemark);
        dest.writeString(this.labels);
        dest.writeByte(this.isLogin ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatarPath);
        dest.writeString(this.avatarName);
        dest.writeString(this.avatarURL);
    }

    protected UserInfo(Parcel in) {
        this.userName = in.readString();
        this.userNickName = in.readString();
        this.uinEnc = in.readString();
        this.alias = in.readString();
        this.conRemark = in.readString();
        this.labels = in.readString();
        this.isLogin = in.readByte() != 0;
        this.avatarPath = in.readString();
        this.avatarName = in.readString();
        this.avatarURL = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
