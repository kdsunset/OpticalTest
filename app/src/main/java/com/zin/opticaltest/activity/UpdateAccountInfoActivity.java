package com.zin.opticaltest.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zin.opticaltest.R;
import com.zin.opticaltest.entity.OpticalTestURL;
import com.zin.opticaltest.utils.FileUtils;
import com.zin.opticaltest.utils.GetPathFromUri4kitkat;
import com.zin.opticaltest.utils.ImageUtils;
import com.zin.opticaltest.utils.LogUtils;
import com.zin.opticaltest.utils.ToastUtils;
import com.zin.opticaltest.utils.UserSharePreHelper;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.Call;


public class UpdateAccountInfoActivity extends AppCompatActivity {

    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.ll_mytoolbar_UAActi)
    LinearLayout llMytoolbar;
    @Bind(R.id.iv_item_headimg)
    CircleImageView ivItemHeadimg;
    @Bind(R.id.rl_item_change_headimg)
    RelativeLayout rlItemChangeHeadimg;
    @Bind(R.id.rl_item_change_bgimg)
    RelativeLayout rlItemChangeBgimg;
    @Bind(R.id.rl_item_change_username)
    RelativeLayout rlItemChangeUsername;
    private Context mContext;

    /**
     * 从相册选取
     */
    private static final int REQUESTCODE_FROM_PHOTOALBUM = 1;
    /**
     * 拍照选取
     */
    private static final int REQUESTCODE_FROM_PHOTOGRAPH = 3;
    /**
     * 剪裁
     */
    private static final int REQUESTCODE_FROM_CLIPPING= 2;
    /**
     * 旋转界面
     */
    private static final int REQUESTCODE_FROM_ROTATE= 8;
    /**
     * 拍照的照片存储的目录。
     */
    private File PHOTO_DIR;


    //头像图片路径
    private String headImgPath = "";
    // 上传真正的头像
    private String uploadheadPath = "";

    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_ISTIEMOUT = "isTimeOut";
    private static final String STATUS_ISNULL = "isNull";
    private static final String STATUS_UNREGISTER = "unregister";
    private static final String STATUS_BY_REGISTERED = "registered";
    private static final String STATUS_ERROR = "error";
    private String mHeadImageUrl;
    private String cropheadImgPath;
    private String userName;
    private String headImg;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account_info);
        ButterKnife.bind(this);
        mContext=UpdateAccountInfoActivity.this;
        tvBarTitle.setText("修改信息");
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rlItemChangeHeadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectHeadImageDialog();
            }
        });
        UserSharePreHelper helper=new UserSharePreHelper(this);
        userName = helper.getUserName();
        headImg = helper.getheadImg();
         userId =    helper.getUserId();


    }

    public void showSelectHeadImageDialog() {
        final MaterialDialog dialog = new MaterialDialog(this);
        View view = LayoutInflater.from(UpdateAccountInfoActivity.this)
                .inflate(R.layout.dialog_select_headimg, null);
        TextView tvSelectfromTakephoto = (TextView) view.findViewById(R.id.tv_selectfrom_takephoto);
        TextView tvSelectfromalbum = (TextView) view.findViewById(R.id.tv_selectfrom_album);
        /*点击从相册获取按钮*/
        tvSelectfromalbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUESTCODE_FROM_PHOTOALBUM);
                    dialog.dismiss();
                } catch (ActivityNotFoundException e) {
                    ToastUtils.showToast(mContext,"没有找到照片");
                    dialog.dismiss();
                }
            }
        });
        tvSelectfromTakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {
                    //拍照获取图片
                    takePhotoNow();
                    dialog.dismiss();
                } else {
                    ToastUtils.showToast(mContext,"没有可用的存储卡");
                    dialog.dismiss();
                }
            }
        });
        dialog.setTitle("提示信息")
                .setView(view)
                .setCanceledOnTouchOutside(true)
                // You can change the message anytime.
                // mMaterialDialog.setTitle("提示");
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        })
                .show();


    }
    /**
     * 拍照获取图片
     */
    protected void takePhotoNow() {

      File  file = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + "headimg" +".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


          headImgPath=file.getAbsolutePath();
        try {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(headImgPath));
            startActivityForResult(intent,REQUESTCODE_FROM_PHOTOGRAPH);
        } catch (Exception e) {
            ToastUtils.showToast(mContext,"未找到系统相机程序");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUESTCODE_FROM_PHOTOALBUM) {// 相册选择图片回调
            /*
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            LogUtils.i("相册选择图片URI" + uri.toString());
            // 获取到的URI： content://media/external/images/media/598
            cropImage(uri, 680, 680, REQUESTCODE_FROM_CLIPPING);
            //获取到图片后，再调用裁剪页面，完成后进入requestCode == REQUESTCODE_FROM_CLIPPING

        } else if (requestCode == REQUESTCODE_FROM_CLIPPING) {// 裁剪后
            if (resultCode == Activity.RESULT_OK) {
                // 裁剪成功
                if (data != null) {

                   ImageUtils.setImgForImageView(ivItemHeadimg,cropheadImgPath);
                   /* Bitmap loacalBitmap = ImageUtils.getLoacalBitmap(cropheadImgPath);
                    ivItemHeadimg.setImageBitmap(loacalBitmap);*/
                    uploadHeadImg(cropheadImgPath);
                }
            }


        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUESTCODE_FROM_PHOTOGRAPH) {// 拍照后

        } else if (requestCode == REQUESTCODE_FROM_ROTATE && resultCode == 100) {// 从旋转那里跳进来


        }
    }


    /**
     * 调用系统自带的一个图片剪裁页面，点击确定之后就会得到一张图片。
     */
    private void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        /*如果高于Android 4.4*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //String url = getPath(context, uri);
            String url = GetPathFromUri4kitkat.getPath(mContext, uri);

            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        // intent.putExtra("outputFormat",
        // Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        // 让系统帮忙拉伸填充 不然会出现图片不够像素填充黑边的情况
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // if (headPath.length() <= 0) {

      //  cropheadImgPath=createPath();
        String fileName="/"+"headimg"+System.currentTimeMillis() +".jpg";
        String filePath="/opticalTest/images";
        cropheadImgPath= FileUtils.createPath(this,filePath,fileName);
        LogUtils.i("裁剪后的图片的保存路径："+cropheadImgPath);
        Uri parse = Uri.parse("file://"+cropheadImgPath);
        LogUtils.i("裁剪后的图片的保存路径URI："+parse.toString());

        /* 裁剪后的图片的保存路径：/data/data/com.zin.opticaltest/files1461160272743newheadimg.jpg
           裁剪后的图片的保存路径URI：file:///data/data/com.zin.opticaltest/files1461160272743newheadimg.jpg*/

        intent.putExtra(MediaStore.EXTRA_OUTPUT, parse);

        startActivityForResult(intent, requestCode);
    }

    private void   uploadHeadImg(String path){
        // bitmap = data.getParcelableExtra("data");
        // civHead.setImageBitmap(bitmap);
        uploadheadPath = path;
        // new faceTask().execute(new
        // File(uploadheadPath.replace("file://",
        // "")));
        System.out.println(path);


        if(!TextUtils.isEmpty(uploadheadPath)){
            uploadheadPath = uploadheadPath.replace("file://","");
            LogUtils.i("上传时图片的路径："+cropheadImgPath);
            File file = new File(uploadheadPath);
            if(file.exists()){
                System.out.println("文件存在");

                OkHttpUtils
                        .post()
                        .url(OpticalTestURL.URL_UPDATEINFO)
                        .addParams("username",userName)

                        .addFile("headImageFile","headImage.jpg",file)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
//                                            System.out.println(e.getMessage());
                                ToastUtils.showToast(mContext,"上传头像失败，请检查网络");
                            }

                            @Override
                            public void onResponse(String response) {
                                LogUtils.i("修改返回结果："+response);
                            }
                        });
            }else{
                System.out.println("文件不存在");
            }
        }else{

        }
    }



}

