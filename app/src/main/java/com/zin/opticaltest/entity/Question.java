package com.zin.opticaltest.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by ZIN on 2016/3/28.
 */
public class Question extends BmobObject {
    public Question() {
    }

    private int itemId;

    public int getQuesitonid() {
        return quesitonid;
    }

    public void setQuesitonid(int quesitonid) {
        this.quesitonid = quesitonid;
    }

    private int quesitonid;
    private String title;

    private String questiontype;

    private String itema;

    private String itemb;

    private String itemc;

    private String itemd;

    private String answer;
    private String analysis;

    private String image;

    private String yourAnswer ;
    private String quesimg;
    private String ansimg;
    private String analysisimg;
    private String youransimg;

    public String getQuesimg() {
        return quesimg;
    }

    public void setQuesimg(String quesimg) {
        this.quesimg = quesimg;
    }

    public String getAnsimg() {
        return ansimg;
    }

    public void setAnsimg(String ansimg) {
        this.ansimg = ansimg;
    }

    public String getAnalysisimg() {
        return analysisimg;
    }

    public void setAnalysisimg(String analysisimg) {
        this.analysisimg = analysisimg;
    }

    public String getYouransimg() {
        return youransimg;
    }

    public void setYouransimg(String youransimg) {
        this.youransimg = youransimg;
    }

    private int paperId;

    public String getYourAnswer() {
        return yourAnswer;
    }

    public void setYourAnswer(String yourAnswer) {
        this.yourAnswer = yourAnswer;
    }



    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return this.itemId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTestType(String testType) {
        this.questiontype = testType;
    }

    public String getTestType() {
        return this.questiontype;
    }

    public void setItema(String itema) {
        this.itema = itema;
    }

    public String getItema() {
        return this.itema;
    }

    public void setItemb(String itemb) {
        this.itemb = itemb;
    }

    public String getItemb() {
        return this.itemb;
    }

    public void setItemc(String itemc) {
        this.itemc = itemc;
    }

    public String getItemc() {
        return this.itemc;
    }

    public void setItemd(String itemd) {
        this.itemd = itemd;
    }

    public String getItemd() {
        return this.itemd;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getAnalysis() {
        return this.analysis;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }
//questionId, title, questionType,itema,itemb, itemc ,itemd,answer,fillAnswer,analysis,image
    public Question(int itemId, String title, String testType, String itema,
                    String itemb, String itemc, String itemd, String answer,
                    String yourAnswer, String analysis, String image, int paperId, String
                            quesimg, String ansimg, String youransimg, String anaimg) {
        this.itemId = itemId;
        this.title = title;
        this.questiontype = testType;
        this.itema = itema;
        this.itemb = itemb;
        this.itemc = itemc;
        this.itemd = itemd;
        this.answer = answer;
        this.analysis = analysis;
        this.image = image;
        this.yourAnswer = yourAnswer;
        this.paperId = paperId;
        this.quesimg = quesimg;
        this.ansimg = ansimg;
        this.youransimg=youransimg;
        this.analysisimg =anaimg;
    }
}
