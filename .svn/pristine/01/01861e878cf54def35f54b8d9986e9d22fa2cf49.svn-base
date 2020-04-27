package com.xiezhiai.wechatplugin.model.xiezhi;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by shijiwei on 2018/11/1.
 *
 * @Desc:
 */
public class WXUser {

    @JSONField(name = "wx_id")
    private String id;
    @JSONField(name = "username")
    private String name;
    @JSONField(name = "img_url")
    private String avatar;
    @JSONField(name = "tenant_id")
    private String tenantID;

    private boolean inServic;

    public WXUser() {
    }

    public WXUser(String id, String name, String avatar, String tenantID) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.tenantID = tenantID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return "https://trialchat.xiezhiai.com:6160/data/" + avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

    public boolean isInServic() {
        return inServic;
    }

    public void setInServic(boolean inServic) {
        this.inServic = inServic;
    }
}
