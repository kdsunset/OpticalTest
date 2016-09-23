package com.zin.opticaltest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zin.opticaltest.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeiboLoginActivity extends Activity {

    @Bind(R.id.tvUserId)
    TextView tvUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_login);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        tvUserId.setText(userId);
    }
}
