package com.xiezhiai.wechatplugin.model.xiezhi;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by shijiwei on 2018/11/7.
 *
 * @Desc:
 */
public class XqaAnswer {

    private String status;
    @JSONField(name = "answer_type")
    private String type;
    private String name;
    private String price;
    private String postage;
    @JSONField(name = "after_sale")
    private String afterSale;
    private String description;
    @JSONField(name = "served_wx_id")
    private String talker;

    public XqaAnswer() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public boolean isCommonqa() {
        try {
            // "common | goods"
            return getType().equals("common");
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public String toString() {
        return " talker: " + talker
                + "  description：" + description;
    }
}
