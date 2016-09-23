package com.zin.opticaltest.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.zin.opticaltest.R;
import com.zin.opticaltest.utils.LogUtils;
import com.zin.opticaltest.utils.ZipImage;

import java.io.File;
import java.io.IOException;


/*日志：使用Intent调用系统相机的方式两种，
 * 1.Intent.addAction(MediaStore.ACTION_IMAGE_CAPTURE)
 * startActivityForResult(intent, 1);
 * 这种没有传参的，ononActivityResult里data返回的是一个缩略图
 * 2.Intent.addAction(MediaStore.ACTION_IMAGE_CAPTURE)
 * intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); 
 * startActivityForResult(intent, 1);
 * 这种传了一个文件路径的URI，表名图片的将存到这里，data返回控制
 * 那么问题来了
 * 本例调试时使用1.能正常返回并在showResult中显示拍好的照片
 * 然而用第二种方法，却卡系统照相界面，当按下拍照，点击确定和返回都没反应，且没有进入onActivityResult
 * uri是好的，文件路径能正确创建，网搜称与机型有关
 * 红米手机无法执行onActivityResult回调，部分华为手机无法获取到照片数据
 * ...................
 * 2015-10-09*/
public class TakePhotoActivity extends Activity {
	
	ImageView showresult;
	String title,date,content;
	private static final int PICTURE_FROM_CAMERA = 0X32;
	private static final int PICTURE_FROM_GALLERY = 0X34;

	String path;
	private File file;//存储拍摄图片的文件
	private int itemid;
	private int paperid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_takephoto);

		Intent lastIntent = getIntent();
		itemid = lastIntent.getExtras().getInt("id");
		paperid = lastIntent.getExtras().getInt("paperid");
		file = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis() + "paperid="+ paperid +"quesid="+ itemid +".jpg");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LogUtils.i("PhotoActivity");

		path=file.toString();

		try {

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intent,PICTURE_FROM_CAMERA);
		} catch (Exception e) {

		}
		/*Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//启动相机的Action
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));//设置图片拍摄后保存的位置
		startActivityForResult(intent, PICTURE_FROM_CAMERA);//启动相机，这里使用有返回结果的启动*/
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.i("onActivityResult");
		if (resultCode == Activity.RESULT_OK && requestCode == PICTURE_FROM_CAMERA){
			LogUtils.i("onActivityResult--RESULT_OK--PICTURE_FROM_CAMERA");




				//这里对图片进行了压缩，因为有些手机拍摄的照片过大，无法显示到ImageView中，所以
				// 我们将图片近行了压缩然后在进行显示
				String path=Uri.fromFile(file).getPath();
				if(path.startsWith("file:")){
					path = path.substring(5, path.length());
				}
			try {
				ZipImage.zipImage(path);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(TakePhotoActivity.this,"保存图片时遇到问题",Toast.LENGTH_SHORT).show();
			}
			//将图片设置到ImageView中，这里使用setImageURI（）方法进行设置。
				Intent in=new Intent();
				in.putExtra("PATH",path);
				in.putExtra("id",itemid);
				in.putExtra("paperid",paperid);

				setResult(1001,in);



		}

		finish();






	}

/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.i("onActivityResult");
		if (resultCode == RESULT_OK) {
			LogUtils.i("onActivityResult--RESULT_OK");
			switch (requestCode) {
				case PICTURE_FROM_CAMERA:
					LogUtils.i("onActivityResult--RESULT_OK--PICTURE_FROM_CAMERA");
					//这里对图片进行了压缩，因为有些手机拍摄的照片过大，无法显示到ImageView中，所以我们将图片近行了压缩然后在进行显示
					Toast.makeText(this,"保存图片成功",Toast.LENGTH_SHORT).show();
					ZipImage.zipImage(Uri.fromFile(file).getPath());
					//将图片设置到ImageView中，这里使用setImageURI（）方法进行设置。
					Intent in=new Intent();
					in.putExtra("PATH",path);
					in.putExtra("id",itemid);
					in.putExtra("paperid",paperid);
					setResult(1001,in);
					finish();
					break;

			}


		}
		finish();




	}*/

	/*
	*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			switch (requestCode) {
				case PICTURE_FROM_CAMERA:
					//这里对图片进行了压缩，因为有些手机拍摄的照片过大，无法显示到ImageView中，所以我们将图片近行了压缩然后在进行显示
					Toast.makeText(this,"保存图片成功",Toast.LENGTH_SHORT).show();
					ZipImage.zipImage(Uri.fromFile(file).getPath());
					//将图片设置到ImageView中，这里使用setImageURI（）方法进行设置。

					break;
			}
		}
		setResult(1001);
		finish();
	}*/

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("tioashi", "onActivResult 执行了");
		Bitmap bitmap = null;

		if (resultCode == RESULT_OK) {

			OutputStream os = null;
			try {
				Log.i("", "dead code ?");
				Bundle bundle = data.getExtras();
				bitmap = (Bitmap) bundle.get("data");//获取照相机返回的图片，并抓换成图片对象


				String savetime = MainUtils.getWriteTime(2);

				String date1 = MainUtils.getWriteTime(1);
				String filepath = getApplicationContext().getFilesDir().getAbsolutePath() + "/photonote" + savetime + ".jpeg";
				date = date1;
				content = filepath;
				File file = new File(filepath);
				os = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);// 把数据写入文件
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					os.flush();
					os.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		}

		//showresult.setImageBitmap(bitmap);
		finish();
			

	}*/
	




}
