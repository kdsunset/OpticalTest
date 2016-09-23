package com.zin.opticaltest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageCacheUtil {
	private static final String tag = "ImageCacheUtil";
	
	
	public static final int SUCCESS = 100;
	public static final int FAIL = 101;

	private LruCache<String, Bitmap> lruCache;
	private File cacheDir;
	private ExecutorService newFixedThreadPool;

	private Handler handler;
	
	public ImageCacheUtil(Context context,Handler handler) {
		int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);
		lruCache = new LruCache<String,Bitmap>(maxSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				//getRowBytes()一行上面对应像素点占用的大小*value.getHeight()行号
				return value.getRowBytes()*value.getHeight();
			}
		};
		cacheDir = context.getCacheDir();
		//2*cup核数+1
		newFixedThreadPool = Executors.newFixedThreadPool(5);
		this.handler = handler;
	}
	public Bitmap getBitmap(String imgUrl,int position){
		//position为确认给那个imageview控件使用的tag
		Bitmap bitmap = null;
		//1,内存中去取
		bitmap = lruCache.get(imgUrl);
		if(bitmap!=null){
			Log.i(tag, "从内存中获取到的图片");
			return bitmap;
		}
		//2,文件中去取
		bitmap = getBitmapFromLocal(imgUrl);
		if(bitmap!=null){
			Log.i(tag, "从文件中获取到的图片");
			return bitmap;
		}
		//3,网络去下载
		getBitmapFromNet(imgUrl,position);
		Log.i(tag, "网络获取到的图片.......");
		return null;
	}
	
	class RunnableTask implements Runnable{
		private String imageUrl;
		private int position;
		
		public RunnableTask(String imageUrl,int position) {
			this.imageUrl = imageUrl;
			this.position = position;
		}
		@Override
		public void run() {
			//访问网络,下载图片
			try {
				URL url = new URL(imageUrl);
				HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
				//读取超时时间()
				connection.setReadTimeout(5000);
				//链接超时时间()
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				InputStream inputStream = connection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				
				//消息机制()
				Message msg = new Message();
				msg.what = SUCCESS;
				msg.obj = bitmap;
				msg.arg1 = position;
				handler.sendMessage(msg);
				
				//内存缓存
				lruCache.put(imageUrl, bitmap);
				//写入文件
				writeToLocal(imageUrl, bitmap);
				
				return ;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Message msg = new Message();
			msg.what = FAIL;
			handler.sendMessage(msg);
		}
	}
	
	private void writeToLocal(String imageUrl, Bitmap bitmap) {
		String fileName;
		try {
			fileName = MD5Utils.MD5(imageUrl).substring(10);
			File file = new File(cacheDir,fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
			//1,定义一个大小如果超过当前大小的时候,就需要进行等比例压缩
			bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getBitmapFromNet(String url,int position) {
		newFixedThreadPool.execute(new RunnableTask(url,position));
	}
	
	private Bitmap getBitmapFromLocal(String imgUrl) {
		String fileName;
		try {
			fileName = MD5Utils.MD5(imgUrl).substring(10);
			File file = new File(cacheDir,fileName);
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			//加入到内存中去(内存的效率要高)
			lruCache.put(imgUrl, bitmap);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
