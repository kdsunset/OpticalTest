package com.zin.opticaltest.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;

import com.zin.opticaltest.R;

import java.lang.ref.WeakReference;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class MainUtils {

	
	public static Bitmap filePathToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }
	
public static String getWriteTime(int style){
		SimpleDateFormat sdf=null;
		switch (style) {
		case 1:
			 sdf=new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
			break;
		case 2:
			 sdf=new SimpleDateFormat("yyyyMMdd_hhmmss");		
			break;
		default:
			break;
		}
	
		Date curDate=new Date(System.currentTimeMillis());
		String writedate= sdf.format(curDate);
		
		return writedate;
		
		
	}
public static String[] getSP(Context mContext){
	SharedPreferences sp=mContext.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
	String isFirstUse=sp.getString("ISFIRSTUSE", "是");
	String password=sp.getString("PASSWORD",null);
	String [] s=new String[]{isFirstUse,password};
	
	return s;

}
public static void setSP(Context mContext,String flag,String pwd){
	SharedPreferences sp=mContext.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
	SharedPreferences.Editor editor=sp.edit();
	if (flag!=null) {
		editor.putString("ISFIRSTUSE", flag);
	}
	if (pwd!=null) {
		editor.putString("PASSWORD", pwd);
	}
	editor.commit();
}


public static int[] getWidthAndHeight(Context context){
	int [] widthandheight=new int[2];
	DisplayMetrics dm = new DisplayMetrics();

	((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
	int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
	int mScreenHeight = dm.heightPixels;
	widthandheight[0]=mScreenWidth;
	widthandheight[1]=mScreenHeight;
	return widthandheight;
	
	
	}

	public static Bitmap createBitmap(Context context){
		Resources res = context.getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.test_image);
		return  bmp;
	}


	public static Bitmap convertViewToBitmap(View view){
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}
}
