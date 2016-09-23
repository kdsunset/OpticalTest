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
import com.zin.opticaltest.entity.ExamResultBean;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExamResultActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_totalscore)
    TextView tvTotalscore;
    @Bind(R.id.tv_totaltime)
    TextView tvTotaltime;
    @Bind(R.id.tv_youscore)
    TextView tvYouscore;
    @Bind(R.id.tv_costtime)
    TextView tvCosttime;
    @Bind(R.id.tv_handuptime)
    TextView tvHandupTime;
    @Bind(R.id.tv_judge)
    TextView tvJudge;
    @Bind(R.id.bt_check_answer)
    Button btCheckAnswer;
    @Bind(R.id.tv_CountInfo)
    TextView tvCountInfo;
    ExamResultBean result;
    int paperid;
    @Bind(R.id.tb_result_activity)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);
        ButterKnife.bind(this);
        initToolbar();
        Intent intent = getIntent();
        result = (ExamResultBean) intent.getParcelableExtra("EXAMRESULT");
        paperid = intent.getExtras().getInt("PAPERID");

        initView();
    }

    private void initView() {
        tvTitle.setText(result.getPaperName());
        tvTotalscore.setText("总  分 ："+result.getTotalScore() + " 分");
        tvTotaltime.setText(" 时 间 ："+result.getTotalTime() + " 分钟");
        tvYouscore.setText("成 绩："+result.getYouScore() + " （选择题）");
        tvCosttime.setText("用 时 ："+result.getSpendTime() + " 分钟");
        tvHandupTime.setText("日 期 ："+result.getHandupTime());
        tvJudge.setText("评 价"+result.getJudge());
        tvCountInfo.setText("总题数" + result.getQuestionSum() + "题，" + "作答"
                + result.getWriteSum() + "题," + "其中选择题正确" + result.getRightSum() + "题。");
        btCheckAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExamResultActivity.this, StartExamActivity3.class);
                intent.putExtra("PAPERID", paperid);
                intent.putExtra("MODE", 101);
                startActivity(intent);
                finish();
            }
        });
    }

    public void initToolbar() {



        mToolbar.setTitle("考试结果");// 标题的文字需在setSupportActionBar之前，不然会无效
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
