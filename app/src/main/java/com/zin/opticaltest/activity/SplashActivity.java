package com.zin.opticaltest.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;


public class SplashActivity extends Activity {
	private UpdateInfo info;	
	private static final int GET_INFO_SUCCESS = 10;
	private static final int SERVER_ERROR = 11;
	private static final int SERVER_URL_ERROR = 12;
	private static final int IO_ERROR = 13;
	private static final int XML_PARSER_ERROR = 14;
	protected static final String TAG = "SplashActivity";
	private Context mContext;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//AppManager.getAppManager().addActivity(this);
		/*setContentView(R.layout.activity_splash);
		mContext = this;
		LinearLayout ll_root = (LinearLayout) findViewById(R.id.ll_root);
		// 添加界面的渐变动画 效果
				Animation animation = new AlphaAnimation(0.3f, 1f);
				animation.setDuration(2000);
				ll_root.startAnimation(animation); // 启动动画
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
//				mHandler.sendEmptyMessage(1);
				checkVesion();
			}
		});*/
	}
	
	/*private void checkVesion() {
		new AsyncTask<Void, Void, Void>(){

			protected Void doInBackground(Void... params) {
				new CheckVersionTask().run();
				return null;
			}
		}.execute();
	}*/
/*
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case XML_PARSER_ERROR:
				*//*Toast.makeText(getApplicationContext(), "XML解析异常",
						Toast.LENGTH_LONG).show();*//*
				redirectTo();// 相同进入
				break;
			case IO_ERROR:
				*//*Toast.makeText(getApplicationContext(), "IO异常",
						Toast.LENGTH_LONG).show();*//*
				redirectTo();// 相同进入
				break;
			case SERVER_URL_ERROR:
				*//*Toast.makeText(getApplicationContext(), "服务器URL出错",
						Toast.LENGTH_LONG).show();*//*
				redirectTo();// 相同进入
				break;
			case SERVER_ERROR:
				*//*Toast.makeText(getApplicationContext(), "服务器异常",
						Toast.LENGTH_LONG).show();*//*
				redirectTo();// 相同进入
				break;
			case GET_INFO_SUCCESS:
				String serverVersion = info.getVersion(); // 取得服务器上的版本号
				String currentVersion = getVersion(); // 取得当前应用的版本号
				if (currentVersion.equals(serverVersion)) { // 判断两个版本号是否相同
					redirectTo();// 相同进入
				} else {
					showUpdateDialog(); // 如果版本号不相同，则加载更新对话框
				}
				break;

			default:
				redirectTo();// 相同进入
				break;
			}
		}

	};

	// 显示升级对话框
	protected void showUpdateDialog() {
		final Builder builder = new Builder(this); // 实例化对话框
		builder.setTitle("升级提示"); // 添加标题
		builder.setMessage(Html.fromHtml(info.getDescription())); // 添加内容
		builder.setPositiveButton("升级", new OnClickListener() { // 点击升级时的操作方法
					@Override
					public void onClick(DialogInterface dialog, int which) {
						apkUrl = info.getApkurl();
						showDownloadDialog();
					}
				});
		builder.setNegativeButton("取消", new OnClickListener() { // 点击取消时的操作方法
					@Override
					public void onClick(DialogInterface dialog, int which) {
						redirectTo();
					}
				});
		builder.create().show(); // 显示对话框

	}

	private class CheckVersionTask implements Runnable {
		@Override
		public void run() {
			Message msg = Message.obtain();
			// 1、取得服务器地址
			String serverUrl = getResources().getString(R.string.serverurl); // 取得服务器地址
			// 2、连接服务器
			try {
				URL url = new URL(serverUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				int code = conn.getResponseCode();
				if (code == 200) {
					InputStream is = conn.getInputStream(); // 取得服务器返回的内容
					info = UpdateInfoParser.getUpdateInfo(is); // 调用自己编写的方法，将输入流转换为UpdateInfo对象
					msg.what = GET_INFO_SUCCESS;
					handler.sendMessage(msg);
				} else {
					msg.what = SERVER_ERROR;
					handler.sendMessage(msg);
				}
			} catch (MalformedURLException e) {
				msg.what = SERVER_URL_ERROR;
				handler.sendMessage(msg);
				handler.sendMessage(msg);
			} catch (IOException e) {
				msg.what = IO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				msg.what = XML_PARSER_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}

	*//**
	 * 取得应用的版本号
	 * 
	 * @return
	 *//*
	public String getVersion() {
		PackageManager pm = getPackageManager(); // 取得包管理器的对象，这样就可以拿到应用程序的管理对象
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0); // 得到应用程序的包信息对象
			return info.versionName; // 取得应用程序的版本号
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// 此异常不会发生
			return "";
		}
	}
	private String apkUrl = "";
	private Dialog downloadDialog;
	*//* 下载包安装路径 *//*
	private static final String savePath = "/sdcard/kzyyApk/";

	private static final String saveFileName = savePath + "kzyy.apk";

	*//* 进度条与通知ui刷新的handler和msg常量 *//*
	private ProgressBar mProgress;

	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	// 取消下载
	public boolean intercept() {
		return this.interceptFlag;
	}

	public void showDownloadDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				redirectTo();
				interceptFlag = true;
			}
		});
		builder.setPositiveButton("后台下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				redirectTo();
			}
		});
		downloadDialog = builder.create();
		downloadDialog.show();
		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	*//**
	 * 下载apk
	 * 
	 * @param url
	 *//*

	public void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	*//**
	 * 安装apk
	 * 
	 * @param url
	 *//*
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		startActivityForResult(i, 0);// 通过此方式启动Activity，会回调onActivitResult()方法

	}

	// 进入
	private void redirectTo() {
		startActivity(new Intent(getApplicationContext(),
				MainActivity.class));
		AppManager.getAppManager().finishActivity();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0){
			if(AppManager.getAppManager().currentActivity() == this){
				redirectTo();
			}
		}
	}*/

}
