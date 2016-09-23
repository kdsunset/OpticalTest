package com.zin.opticaltest.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ZIN on 2016/4/20.
 */
public class ToastUtils {
    public  static void  showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
