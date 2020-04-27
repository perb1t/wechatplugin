package com.xiezhiai.wechatplugin.model.xiezhi;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by shijiwei on 2018/11/7.
 *
 * @Desc:
 */
public class Incoming {

    public static final String LUCKMONEY = "red";
    public static final String TRANSFER = "trans";

    @JSONField(name = "incoming_type")
    private String type;
    @JSONField(name = "from_user")
    private String fromUser;
    private float money = 0.0f;

    public Incoming() {
    }

    public Incoming(String type, String fromUser, float money) {
        this.type = type;
        this.fromUser = fromUser;
        this.money = money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }


    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public static Incoming newTransfer(float money, String from) {
        return new Incoming(TRANSFER, from, money);
    }

    public static Incoming newLuckMoney(String from) {
        return newLuckMoney(0.0f, from);
    }

    public static Incoming newLuckMoney(float money, String from) {
        return new Incoming(LUCKMONEY, from, money);
    }

    @Override
    public String toString() {
        return "type = " + type
                + " fromUser = " + fromUser
                + " money = " + money;
    }
}
