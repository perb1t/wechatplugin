package com.xiezhiai.wechatplugin.model.xiezhi;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijiwei on 2018/10/29.
 *
 * @Desc:
 */
public class Commonqa {


    @JSONField(name = "question")
    private String title;
    @JSONField(name = "answer")
    private String content;
    private int status;
    @JSONField(name = "question_id")
    private String questionId;


    private boolean isChecked;
    private boolean isOpen;
    private List<String> imgPaths = new ArrayList<>();

    public Commonqa(boolean isOpen, List<String> imgPaths) {
        this.isOpen = isOpen;
        this.imgPaths = imgPaths;
    }
    public int getStatus() {
        return status;
    }
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<String> getImgPaths() {
        return imgPaths;
    }

    public void setImgPaths(List<String> imgPaths) {
        if (imgPaths != null || imgPaths.size() != 0) {
            this.imgPaths.addAll(imgPaths);
        }
    }
}
