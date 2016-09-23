package com.zin.opticaltest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZIN on 2016/4/19.
 */
public class UserSharePreHelper {

    long lastLoginTime= TimeUtils.getCurrentMillis();
    Context mContex;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public UserSharePreHelper(Context mContex) {
        this.mContex = mContex;
        //私有数据
        sharedPreferences = mContex. getSharedPreferences("LOGIN_STATUS", Context.MODE_PRIVATE);
        //获取编辑器
        editor = sharedPreferences.edit();
    }


  //  editor.putBoolean("HASLOGIN",hasLogin);



    public void  saveUserName(String userName){
        editor.putString("USERNAME", userName);

        editor.commit();//提交修改
    }
    public void  saveUserID(String userId){
        editor.putString("USERID", userId);

        editor.commit();//提交修改
    }


    public void  saveheadImg(String headImg){
        editor.putString("USERNAME", headImg);

        editor.commit();//提交修改
    }

    public void  updateLastLoginTime(){
        long lastLoginTime=TimeUtils.getCurrentMillis();
        editor.putLong("LASTLOGINTIME", lastLoginTime);
        editor.commit();//提交修改
        LogUtils.i("LASTLOGINTIME保存值"+lastLoginTime);
       // Toast.makeText(mContex,"LASTLOGINTIME取出值"+lastLoginTime,Toast.LENGTH_SHORT).show();
    }
    public long  getLastLoginTime(){
        long lastlogintime = sharedPreferences.getLong("LASTLOGINTIME", 0);
        LogUtils.i("LASTLOGINTIME取出值"+lastlogintime);
       // Toast.makeText(mContex,"LASTLOGINTIME取出值"+lastlogintime,Toast.LENGTH_SHORT).show();
        return sharedPreferences.getLong("LASTLOGINTIME",Integer.MAX_VALUE);
    }
    public String  getUserName(){

        return sharedPreferences.getString("USERNAME","立即登录");
    }
    public String  getheadImg(){

        return sharedPreferences.getString("HEADIMG","");
    }
    public String  getUserId(){

        return sharedPreferences.getString("USERID","");
    }


    public  boolean isLoginStatusValid(){
            boolean loginstatus;
            long currentMillis = TimeUtils.getCurrentMillis();

            long lastlogintime = this.getLastLoginTime();
            long days=(currentMillis-lastlogintime)/1000/60/60/24;
            if (days<15){           //
                loginstatus=  true;
            }else {
                loginstatus=  false;
            }
        boolean s=loginstatus;
        LogUtils.i(""+Boolean.toString(s));
        return loginstatus;
    }

    public  void cleanLoginStatus(){
        editor.clear();
        editor.commit();
    }
}
