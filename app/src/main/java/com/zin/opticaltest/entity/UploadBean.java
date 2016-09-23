package com.zin.opticaltest.entity;

import java.util.List;

/**
 * Created by ZIN on 2016/4/21.
 */
public class UploadBean {
    private String userId;
    private String paperId;
    private List<AnswerItem> answerItem;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public List<AnswerItem> getAnswerItem() {
        return answerItem;
    }

    public void setAnswerItem(List<AnswerItem> answerItem) {
        this.answerItem = answerItem;
    }
}
