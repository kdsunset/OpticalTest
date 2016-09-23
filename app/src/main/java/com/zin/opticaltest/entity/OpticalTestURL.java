package com.zin.opticaltest.entity;

/**
 * Created by ZIN on 2016/4/17.
 */
public class OpticalTestURL {
    private static final String IP_FOR_GENYMOTION ="http://10.0.3.2:8080";
    private static final String IP_FOR_ZINPC ="http://192.168.1.122:8080";
    private static final String IP=IP_FOR_GENYMOTION;

    /** :用户登录的URL*/
    public static  final  String URL_LOGIN = IP +"/otweb/user/login.action";
    /** 用户注册的URL*/
    public static  final  String URL_REGISTER= IP +"/otweb/user/register.action";

    /** 用户修改信息的URL*/
    public static  final  String URL_UPDATEINFO= IP +"/otweb/user/updateInfo.action";
    /** 用户上传答案的URL*/
    public static  final  String URL_UPLOADANSWER= IP +"/otweb/userAnswer/uploadAnswer.action";
    public static  final  String URL_UPLOADFILE= IP +"/otweb/uplaod/uploadFile.action";
    public static  final  String URL_ONEPAPERQUESTIONS= IP +"/otweb/question/onePaperQuestions.action";
    public static  final  String URL_QUESLISTBYTYPE= IP +"/otweb/question/questionListByType.action";
    public static  final  String URL_RealPaper= IP +"/otweb/testBank/tlist.action";

    //"http://192.168.1.122:8080/otweb/testBank/tlist.action"


}
