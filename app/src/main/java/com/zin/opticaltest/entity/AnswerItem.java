package com.zin.opticaltest.entity;

/**
 * Created by ZIN on 2016/4/21.
 */
public class AnswerItem {

    private String questionId;
    private String answerString;
    private String ansPicUrl;
    public String getAnsPicUrl() {
        return ansPicUrl;
    }
    public void setAnsPicUrl(String ansPicUrl) {
        this.ansPicUrl = ansPicUrl;
    }


    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswerString() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }
}
