package com.zin.opticaltest.entity;

/**
 * Created by ZIN on 2016/4/18.
 */
public class MyResponse {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR_PASSWORDERROR= "error_passwordwrong";
    public static final String STATUS_ISTIEMOUT = "isTimeOut";
    public static final String STATUS_ISNULL = "isNull";
    public static final String STATUS_ERROR_UNREGISTER = "error_unregister";
    public static final String STATUS_ERROR_REGISTERED = "registered";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_USERID_ISNULL = "userId_isNull";
    public static final String STATUS_UPLOAD_ERROR = "upload_error";

    public User user;

    public String status;

    public String info;
}
