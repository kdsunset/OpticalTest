package com.zin.opticaltest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zin.opticaltest.R;
import com.zin.opticaltest.application.MyApplication;
import com.zin.opticaltest.utils.UIUtils;
import com.zin.opticaltest.utils.UserSharePreHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountDetailActivity extends Activity {

    @Bind(R.id.iv_user_headimg)
    CircleImageView ivUserHeadimg;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.iv_edit_account_info)
    ImageView ivEditAccountInfo;
    @Bind(R.id.tv_cvitem_wrote_count)
    TextView tvCvitemWroteCount;
    @Bind(R.id.cv_item_wrote)
    CardView cvItemWrote;
    @Bind(R.id.tv_cvitem_favorites_count)
    TextView tvCvitemFavoritesCount;
    @Bind(R.id.cv_item_favorites)
    CardView cvItemFavorites;
    @Bind(R.id.tv_cvitem_notes_count)
    TextView tvCvitemNotesCount;
    @Bind(R.id.cv_item_notes)
    CardView cvItemNotes;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);
        mContext=AccountDetailActivity.this;
        initHead();

        ivUserHeadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AccountDetailActivity.this,ShowImageActivity.class),0);
            }
        });
        ivEditAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,UpdateAccountInfoActivity.class));
            }
        });
    }

    private void initHead() {
       if ( MyApplication.getLoginStatus()){
           Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.headimg__login_default);
           ivUserHeadimg.setImageBitmap(bitmap);
           UserSharePreHelper helper=new UserSharePreHelper(UIUtils.getContext());
           String username=helper.getUserName();
           tvUserName.setText(username);
       }
        ivEditAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
