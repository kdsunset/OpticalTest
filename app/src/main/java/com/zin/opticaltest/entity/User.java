package com.zin.opticaltest.entity;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("userid")
    private int userId;
    @SerializedName("studentid")
    private String studentId;
    @SerializedName("mobilephone")
    private String mobilePhone;

    private String grade;
    @SerializedName("password")
    private String passWord;
    @SerializedName("realname")
    private String realName;
    @SerializedName("username")
    private String userName;
    @SerializedName("usertype")
    private String userType;
    @SerializedName("emailaddress")
    private String emailAddress;
    @SerializedName("headimage")
    private String headImage;
    @SerializedName("lastlogintime")
    private long lastLoginTime;

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }
    /*new User(userId, userType, userName,passWord,realName, grade ,studentId,headImage,lastLoginTime)*/

    public User(int userId, String userType, String userName, String passWord, String realName,
                String grade, String studentId, String mobilePhone, String emailAddress, String headImage,
                long lastLoginTime) {
        this.userId = userId;
        this.studentId = studentId;
        this.mobilePhone = mobilePhone;
        this.grade = grade;
        this.passWord = passWord;
        this.realName = realName;
        this.userName = userName;
        this.userType = userType;
        this.emailAddress = emailAddress;
        this.headImage = headImage;
        this.lastLoginTime = lastLoginTime;
    }

    public User() {
    }
}
