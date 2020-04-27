package com.xiezhiai.wechatplugin.model.wechat;

/**
 * Created by caicai on 2018/10/30.
 */
public class GoodsInfo {
    String GoodsName;//名称
    String GoodsPrice;//价格
    String Postage;//邮费
    String AfterSales;//售后
    String Details;//详情

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public String getGoodsPrice() {
        return GoodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        GoodsPrice = goodsPrice;
    }

    public String getPostage() {
        return Postage;
    }

    public void setPostage(String postage) {
        Postage = postage;
    }

    public String getAfterSales() {
        return AfterSales;
    }

    public void setAfterSales(String afterSales) {
        AfterSales = afterSales;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "GoodsName='" + GoodsName + '\'' +
                ", GoodsPrice='" + GoodsPrice + '\'' +
                ", Postage='" + Postage + '\'' +
                ", AfterSales='" + AfterSales + '\'' +
                ", Details='" + Details + '\'' +
                '}';
    }
}
