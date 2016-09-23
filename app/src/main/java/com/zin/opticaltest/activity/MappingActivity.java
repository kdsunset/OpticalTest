package com.zin.opticaltest.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zin.opticaltest.R;
import com.zin.opticaltest.ui.DrawView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;

public class MappingActivity extends AppCompatActivity {
    @Bind(R.id.tb_mapping_activity)
    Toolbar mToolbar;
    @Bind(R.id.dvMappingQues)
    DrawView dvMappingQues;
    MaterialDialog  mMaterialDialog;
    private int itemid;
    private int paperid;
    private File file;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);
        ButterKnife.bind(this);
        initToolbar();
        Intent lastIntent=getIntent();
        itemid = lastIntent.getExtras().getInt("id");
        paperid = lastIntent.getExtras().getInt("paperid");
        String imageUrl=lastIntent.getStringExtra("url");

        /*Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.test_image).copy(Bitmap.Config.ARGB_8888, true); ;


        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);



      //  dvMappingQues.setInitBitmap(bmp);
        dvMappingQues.initBitmap(bmp);*/

        ImageLoader.getInstance().loadImage(imageUrl, new SimpleImageLoadingListener(){

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                dvMappingQues.initBitmap(loadedImage);
            }

        });

        file = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + "paperid="+ paperid +"quesid="+ itemid +".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        path=file.toString();


    }

    private void initToolbar() {

        // toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        //  这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了
        // getSupportActionBar().setTitle("标题");
        // getSupportActionBar().setSubtitle("副标题");
        // getSupportActionBar().setLogo(R.drawable.ic_launcher);

        // mToolbar.inflateMenu(R.menu.activity_mapping_menu);
        // 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过Activity的onOptionsItemSelected回调方法来处理
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mapping_pen:
                        Toast.makeText(MappingActivity.this,"画笔",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mapping_pen_line:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_LINE);
                        break;
                    case R.id.mapping_pen_dot:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_DOT);
                        break;
                    case R.id.mapping_pen_circul:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CIRCUL);
                        break;
                    case R.id.mapping_pen_curve:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CURVE);
                        break;
                    case R.id.mapping_text:
                        settextDialog();
                        break;
                    case R.id.mapping_oritation:
                        dvMappingQues.changeOritation();
                        break;
                    case R.id.mapping_cleanall:
                        dvMappingQues.removeAllPaint();
                        break;
                    case R.id.mapping_undo:
                        dvMappingQues.undo();
                        break;
                    case R.id.mapping_redo:
                        dvMappingQues.redo();
                        break;
                    case R.id.mapping_color:

                        break;
                    case R.id.mapping_color_red:
                        dvMappingQues.setDrawColor(Color.RED);
                        break;
                    case R.id.mapping_color_yellow:
                        dvMappingQues.setDrawColor(Color.YELLOW);
                        break;
                    case R.id.mapping_color_blue:
                        dvMappingQues.setDrawColor(Color.BLUE);
                        break;
                    case R.id.mapping_color_green:
                        dvMappingQues.setDrawColor(Color.GREEN);
                        break;
                    case R.id.mapping_color_black:
                        dvMappingQues.setDrawColor(Color.BLACK);
                        break;
                    case R.id.mapping_color_white:
                        dvMappingQues.setDrawColor(Color.WHITE);
                        break;
                    case R.id.mapping_color_gray:
                        dvMappingQues.setDrawColor(Color.GRAY);
                        break;
                    case R.id.mapping_size:
                        break;
                    case R.id.mapping_size_width_1:
                        dvMappingQues.setDrawSize(1);
                        break;
                    case R.id.mapping_size_width_2:
                        dvMappingQues.setDrawSize(3);
                        break;
                    case R.id.mapping_size_width_5:
                        dvMappingQues.setDrawSize(5);
                        break;
                    case R.id.mapping_size_width_10:
                        dvMappingQues.setDrawSize(10);
                        break;
                    case R.id.mapping_size_width_20:
                        dvMappingQues.setDrawSize(20);
                        break;
                    case R.id.mapping_eraser:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_ERASER);
                        break;
                    case R.id.mapping_ok:
                        showSureDialog();

                        break;
                    case R.id.mapping_convexlens_hor:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONVEXLENS_HOR);
                        break;
                    case R.id.mapping_convexlens_ver:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONVEXLENS_ver);
                        break;
                    case R.id.mapping_concavelens_ver:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONCAVELENS_VER);
                        break;
                    case R.id.mapping_concavelens_hor:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONCAVELENS_HOR);
                        break;
                    case R.id.mapping_planemirror_cw0:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_PLANEMIRROR_CW0);
                        break;
                    case R.id.mapping_planemirror_cw90:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_PLANEMIRROR_CW90);
                        break;
                    case R.id.mapping_planemirror_cw180:
                         dvMappingQues.setDrawType(DrawView.Pen.DRAW_PLANEMIRROR_CW180);
                        break;
                    case R.id.mapping_planemirror_cw270:
                         dvMappingQues.setDrawType(DrawView.Pen.DRAW_PLANEMIRROR_CW270);
                        break;
                    case R.id.mapping_convex_mirror_cw0_128:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONVEX_MIRROR_CW0_128);
                        break;
                    case R.id.mapping_convex_mirror_cw90_128:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONVEX_MIRROR_CW90_128);
                        break;
                    case R.id.mapping_convex_mirror_cw180_128:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONVEX_MIRROR_CW180_128);
                        break;
                    case R.id.mapping_convex_mirror_cw270_128:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONVEX_MIRROR_CW270_128);
                        break;
                    case R.id.mapping_concave_mirror_cw0_128:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONCAVE_MIRROR_CW0_128);
                        break;
                    case R.id.mapping_concave_mirror_cw90_128:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONCAVE_MIRROR_CW90_128);
                        break;
                    case R.id.mapping_concave_mirror_cw180_128:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONCAVE_MIRROR_CW180_128);
                        break;
                    case R.id.mapping_concave_mirror_cw270_128:
                        dvMappingQues.setDrawType(DrawView.Pen.DRAW_CONCAVE_MIRROR_CW270_128);
                        break;








                    default:
                        break;
                }
                return true;
            }
        });
    }
    private void showSureDialog(){
        mMaterialDialog =new MaterialDialog(this);
        mMaterialDialog
                .setMessage("确定提交吗？")
                .setPositiveButton("是的", new View.OnClickListener() {
                    @Override public void onClick(View v) {

                        saveBitmap();
                        Toast.makeText(MappingActivity.this,"提交成功！请返回并继续下一题",Toast.LENGTH_SHORT).show();
                        mMaterialDialog.dismiss();
                        Intent in=new Intent();
                        in.putExtra("PATH",path);
                        in.putExtra("id",itemid);
                        in.putExtra("paperid",paperid);
                        setResult(1001,in);
                        finish();
                    }
                })
                .setNegativeButton("取消",
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                mMaterialDialog.dismiss();

                            }
                        })
                .setCanceledOnTouchOutside(true)
                .setOnDismissListener(
                        new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        })
                .show();
    }

    public  void   settextDialog(){

        final EditText title_et=new EditText(this);
        title_et.setHint("输入文字");
          mMaterialDialog =new MaterialDialog(this);
        mMaterialDialog.setTitle("输入文字或字母")
                .setMessage("输入文字或字母")
                .setView(title_et)
                .setPositiveButton("确定", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                      String  text=title_et.getText().toString().trim();

                        if (TextUtils.isEmpty(text)) {
                            text="";
                        }
                        dvMappingQues.setText(text);

                        mMaterialDialog.dismiss();


                    }
                })
                .setNegativeButton("取消",
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                mMaterialDialog.dismiss();

                            }
                        })
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


    public void saveBitmap(){
/*
        String name= MainUtils.getWriteTime(2);
        String filepath=getApplicationContext().getFilesDir().getAbsolutePath()+"/drawnote"+name+".jpeg";
        File file=new File(filepath);*/
        Bitmap drawBitmap = dvMappingQues.getBitmap();

        OutputStream os=null;

        if (drawBitmap==null) {
            Log.i("","drawbitmap为空");
            return;
        }
        try {
            os=new FileOutputStream(file);
            //MainUtils.convertViewToBitmap(d)
            drawBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);// 把数据写入文件



        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                os.flush();
                os.close();
                drawBitmap.recycle();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_drawnote_page, menu);
        return true;
    }

}
