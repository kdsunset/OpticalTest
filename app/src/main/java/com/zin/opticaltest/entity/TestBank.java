package com.zin.opticaltest.entity;


import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;

public class TestBank extends BmobObject implements Parcelable {
    private int paperid;

    private String title;


    private String titletwo;

    private String issuetime;

    private String papertype;

    private int qualified;

    private int allscore;

    private int respondencetime;
    private String remark;
    private String provider;

    private int subject;

    private int hasDone;

    public int getHasDone() {
        return hasDone;
    }

    public void setHasDone(int hasDone) {
        this.hasDone = hasDone;
    }

    public TestBank(int paperId, String title, String titletwo, String issuetime,
                    String papertype, int qualified, int allscore, int respondencetime,
                    String provider, String remark, int subject,int hasDone) {
        this.paperid = paperId;
        this.title = title;
        this.titletwo = titletwo;
        this.issuetime = issuetime;
        this.papertype = papertype;
        this.qualified = qualified;
        this.allscore = allscore;
        this.respondencetime = respondencetime;
        this.provider = provider;
        this.subject = subject;
        this.remark = remark;
        this.hasDone=hasDone;
    }



    public int getPaperId() {
        return paperid;
    }

    public void setPaperId(int paperId) {
        this.paperid = paperId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitletwo() {
        return titletwo;
    }

    public void setTitletwo(String titletwo) {
        this.titletwo = titletwo;
    }

    public String getIssuetime() {
        return issuetime;
    }

    public void setIssuetime(String issuetime) {
        this.issuetime = issuetime;
    }

    public String getPapertype() {
        return papertype;
    }

    public void setPapertype(String papertype) {
        this.papertype = papertype;
    }

    public int getQualified() {
        return qualified;
    }

    public void setQualified(int qualified) {
        this.qualified = qualified;
    }

    public int getAllscore() {
        return allscore;
    }

    public void setAllscore(int allscore) {
        this.allscore = allscore;
    }

    public int getRespondencetime() {
        return respondencetime;
    }

    public void setRespondencetime(int respondencetime) {
        this.respondencetime = respondencetime;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public TestBank() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.paperid);
        dest.writeString(this.title);
        dest.writeString(this.titletwo);
        dest.writeString(this.issuetime);
        dest.writeString(this.papertype);
        dest.writeInt(this.qualified);
        dest.writeInt(this.allscore);
        dest.writeInt(this.respondencetime);
        dest.writeString(this.remark);
        dest.writeString(this.provider);
        dest.writeInt(this.subject);
        dest.writeInt(this.hasDone);
    }

    protected TestBank(Parcel in) {
        this.paperid = in.readInt();
        this.title = in.readString();
        this.titletwo = in.readString();
        this.issuetime = in.readString();
        this.papertype = in.readString();
        this.qualified = in.readInt();
        this.allscore = in.readInt();
        this.respondencetime = in.readInt();
        this.remark = in.readString();
        this.provider = in.readString();
        this.subject = in.readInt();
        this.hasDone = in.readInt();
    }

    public static final Parcelable.Creator<TestBank> CREATOR = new Parcelable.Creator<TestBank>() {
        @Override
        public TestBank createFromParcel(Parcel source) {
            return new TestBank(source);
        }

        @Override
        public TestBank[] newArray(int size) {
            return new TestBank[size];
        }
    };
}