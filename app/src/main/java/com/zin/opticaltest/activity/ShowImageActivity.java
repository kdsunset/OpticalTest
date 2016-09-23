package com.zin.opticaltest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.zin.opticaltest.R;
import com.zin.opticaltest.utils.ImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowImageActivity extends Activity {

    @Bind(R.id.iv_show_image)
    ImageView ivShowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);

        Intent intent=getIntent();
        if (intent!=null){
            String path = intent.getStringExtra("path");
            if (!TextUtils.isEmpty(path)){
                ImageUtils.setImgForImageView(ivShowImage,path);
            }
        }

        ivShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
