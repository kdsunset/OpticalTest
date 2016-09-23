package com.zin.opticaltest.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by ZIN on 2016/5/1.
 */
public class BitmapUtils {
    public static Bitmap adjustPhotoRotation(Bitmap obitmap, final int orientationDegree) {
      /*  Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            return bm1;
        } catch (OutOfMemoryError ex) {

        }
        return null;*/
        Matrix matrix = new Matrix();
        // 缩放原图
        matrix.postScale(1f, 1f);
        // 向左旋转45度，参数为正则向右旋转
        matrix.postRotate(orientationDegree);
        //bmp.getWidth(), 500分别表示重绘后的位图宽高
        Bitmap dstbmp = Bitmap.createBitmap(obitmap,0, 0, obitmap.getWidth(), obitmap.getHeight(),
                matrix, true);
        return  dstbmp;
    }

    public  static Bitmap setBitmapRotation(Bitmap obitmap,int rotation){
        Matrix matrix = new Matrix();
        // 缩放原图
        matrix.postScale(1f, 1f);
        // 向左旋转45度，参数为正则向右旋转
        matrix.postRotate(rotation);
        //bmp.getWidth(), 500分别表示重绘后的位图宽高
        Bitmap dstbmp = Bitmap.createBitmap(obitmap,0, 0, obitmap.getWidth(), obitmap.getHeight(),
                matrix, true);
        return  dstbmp;
    }

}
