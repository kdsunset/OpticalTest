package com.zin.opticaltest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZIN on 2016/4/10.
 */
public class ExamResultBean implements Parcelable {
    private String paperName;
    private int questionSum;
    private int writeSum;
    private int rightSum;
    private int totalTime;
    private int spendTime;
    private String handupTime;
    private String judge;
    private int totalScore;
    private int youScore;

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public int getQuestionSum() {
        return questionSum;
    }

    public void setQuestionSum(int questionSum) {
        this.questionSum = questionSum;
    }

    public int getWriteSum() {
        return writeSum;
    }

    public void setWriteSum(int writeSum) {
        this.writeSum = writeSum;
    }

    public int getRightSum() {
        return rightSum;
    }

    public void setRightSum(int rightSum) {
        this.rightSum = rightSum;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(int spendTime) {
        this.spendTime = spendTime;
    }

    public String getHandupTime() {
        return handupTime;
    }

    public void setHandupTime(String handupTime) {
        this.handupTime = handupTime;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getYouScore() {
        return youScore;
    }

    public void setYouScore(int youScore) {
        this.youScore = youScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paperName);
        dest.writeInt(this.questionSum);
        dest.writeInt(this.writeSum);
        dest.writeInt(this.rightSum);
        dest.writeInt(this.totalTime);
        dest.writeInt(this.spendTime);
        dest.writeString(this.handupTime);
        dest.writeString(this.judge);
        dest.writeInt(this.totalScore);
        dest.writeInt(this.youScore);
    }

    public ExamResultBean() {
    }

    protected ExamResultBean(Parcel in) {
        this.paperName = in.readString();
        this.questionSum = in.readInt();
        this.writeSum = in.readInt();
        this.rightSum = in.readInt();
        this.totalTime = in.readInt();
        this.spendTime = in.readInt();
        this.handupTime = in.readString();
        this.judge = in.readString();
        this.totalScore = in.readInt();
        this.youScore = in.readInt();
    }

    public static final Parcelable.Creator<ExamResultBean> CREATOR = new Parcelable.Creator<ExamResultBean>() {
        @Override
        public ExamResultBean createFromParcel(Parcel source) {
            return new ExamResultBean(source);
        }

        @Override
        public ExamResultBean[] newArray(int size) {
            return new ExamResultBean[size];
        }
    };
}
