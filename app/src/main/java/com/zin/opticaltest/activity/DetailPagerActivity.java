package com.zin.opticaltest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zin.opticaltest.R;
import com.zin.opticaltest.application.MyApplication;
import com.zin.opticaltest.entity.TestBank;
import com.zin.opticaltest.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailPagerActivity extends AppCompatActivity {
    @Bind(R.id.tb_detail_paper)
    Toolbar mToolbar;
    @Bind(R.id.tv_title_dialog)
    TextView mTvTitle;
    @Bind(R.id.tv_quetionSum)
    TextView tvQuetionSum;
    @Bind(R.id.tv_totalscore_dialog)
    TextView tvTotalscore;
    @Bind(R.id.tv_totaltime_dialog)
    TextView tvTotaltime;
    @Bind(R.id.tv_papertype_dialog)
    TextView tvPapertype;
    @Bind(R.id.bt_startExam_dialog)
    Button btStartExam;
    @Bind(R.id.bt_check_paper)
    Button btCheckPaper;


    /*Button btMapping;
    private TextView mTvTitle;
    private TextView mTvDetail;
    private TextView mTvDescription;
    private Button mBtStart;
    private Toolbar mToolbar;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_paper_info);
        ButterKnife.bind(this);

        initToolbar();
        Intent intent = getIntent();
        final TestBank paper = (TestBank) intent.getParcelableExtra("PAPERINFO");
        LogUtils.i("试题详情页面：" + "title=" + paper.getTitle());
        //intent.getSerializableExtra("PERSON_INFO");




        mTvTitle.setText(paper.getTitle());
        tvQuetionSum.setText("题  数  :\t\t" + "100题");
        tvTotalscore.setText("总  分  :\t\t" + paper.getAllscore());
        tvPapertype.setText("类  型  :\t\t" + paper.getPapertype());
        tvTotaltime.setText("时  间  :\t\t" + paper.getRespondencetime() + "分钟");

        btStartExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.getLoginStatus()){
                Intent intent = new Intent(DetailPagerActivity.this, StartExamActivity3.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("PAPERINFO", paper);
                intent.putExtra("PAPERID",paper.getPaperId());
                intent.putExtras(bundle);
                intent.putExtra("MODE", 100);
                startActivity(intent);
                finish();
                }else {
                    startActivity(new Intent(DetailPagerActivity.this,LoginActivity.class));

                }

            }
        });
        btCheckPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailPagerActivity.this,StartExamActivity3.class);
                intent.putExtra("PAPERID",paper.getPaperId());
                intent.putExtra("MODE",101);
                startActivity(intent);
                finish();
            }
        });

    }


    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tb_detail_paper);


        mToolbar.setTitle("试题信息");// 标题的文字需在setSupportActionBar之前，不然会无效
        //  mToolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        /* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
