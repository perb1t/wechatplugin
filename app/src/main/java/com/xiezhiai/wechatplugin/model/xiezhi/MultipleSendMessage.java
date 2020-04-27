package com.xiezhiai.wechatplugin.model.xiezhi;

import java.util.ArrayList;

/**
 * Created by shijiwei on 2018/12/3.
 *
 * @Desc: 通知微信群发消息
 */
public class MultipleSendMessage<T> {

    private boolean all;
    private String content;
    private ArrayList<T> targets;

    public MultipleSendMessage() {
    }

    public MultipleSendMessage(boolean all, String content, ArrayList<T> targets) {
        this.all = all;
        this.content = content;
        this.targets = targets;
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public ArrayList<T> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<T> targets) {
        this.targets = targets;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
