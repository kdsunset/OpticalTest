package com.zin.opticaltest.utils;

import android.text.TextUtils;

/**
 * Created by ZIN on 2016/4/12.
 */
public class ConvertAnswerInfoUtils {

    public  static String convertPaperType(int type){
        switch (type){
            case 0:
                return "真题";

            case 1:
                return "模拟题";

            default:
                return "未知试题类型";

        }
    }

    public static int ConvertChoiceItem(String ans) {

        int ansint = -1;
        if (TextUtils.isEmpty(ans)) {
            return -1;
        } else {
            switch (ans) {
                case "A":
                case "a":
                    ansint = 0;
                    break;
                case "B":
                case "b":
                    ansint = 1;
                    break;
                case "C":
                case "c":
                    ansint = 2;
                    break;
                case "D":
                case "d":
                    ansint = 3;
                    break;


            }
        }
        return ansint;
    }
    public static int convertQuestType(String TypeStr){

        int type=-1;
        if ("选择题".equals(TypeStr)) {
            LogUtils.i("选择题");
           type=  0;
        }else if ("填空题".equals(TypeStr)){
            LogUtils.i("填空题");
            type= 1;
        }else if ("作图题".equals(TypeStr)){
            LogUtils.i("作图题");

            type=2;
        }else if ("计算题".equals(TypeStr)){
            LogUtils.i("计算题");
            type=3;
        }
        return type;
    }
}
