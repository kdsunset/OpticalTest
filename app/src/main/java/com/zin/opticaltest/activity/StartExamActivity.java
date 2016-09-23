package com.zin.opticaltest.activity;

import android.support.v7.app.AppCompatActivity;

public class StartExamActivity extends AppCompatActivity {

 /*   private ViewPager mViewPager;
    Toolbar mToolbar;
    ImageButton ibSelectIndex;
    TextView tvIndex;
    int listSize;
    List<Question> mQuestionList;
     TestBank paper;
    long startMillis;
    MaterialDialog mMaterialDialog;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    mQuestionList = (ArrayList<Question>) msg.obj;
                    listSize = mQuestionList.size();
                    setIndex();
                    MyPagerAdapter mAdapter = new MyPagerAdapter(mQuestionList);
                    MyViewPagerListener pagerChangeListener = new MyViewPagerListener();
                    mViewPager.setOnPageChangeListener(pagerChangeListener);
                    mViewPager.setAdapter(mAdapter);
                    mViewPager.setCurrentItem(0);
                    break;
            }
        }
    };
    private TextView tvTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        initView();
        getDataFromBmod();

         Intent intent=getIntent();
         paper =(TestBank) intent.getParcelableExtra("PAPERINFO");



    }

    public void initView() {
        mToolbar= (Toolbar) findViewById(R.id.tbExamActivity);
        tvTimer= (TextView) findViewById(R.id.tv_timer );
         ibSelectIndex= (ImageButton) findViewById(R.id.ib_select_index);
         tvIndex= (TextView) findViewById(R.id.tv_index);
        initTimer();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mToolbar.setTitle("ZinFun");// 标题的文字需在setSupportActionBar之前，不然会无效
      //  mToolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        *//* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 *//*
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        break;
                    case R.id.ab_hansup:
                        showHandUpDialog();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void setIndex(){
        tvIndex.setText(1+"/"+ listSize);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_exam_menu, menu);

   /*     *//**//*//**//*//* 获取菜单项中的条目
        MenuItem item = menu.findItem(R.id.action_timer);
        // 获取条目中的View视图
        View view = item.getActionView();
        // 获取View视图中的控件
        final TextView tvTimer = (TextView) view.findViewById(R.id.tv_timer);
*//**//*


        return true;
    }
    private void initTimer(){
        CountDownTimer timer = new CountDownTimer(2*60*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String result = TimeUtils.formatDuring(millisUntilFinished);
                tvTimer.setText("剩余时间："+result);
            }
            @Override
            public void onFinish() {
                tvTimer.setEnabled(true);
                tvTimer.setText("交卷时间到！");
            }
        };
        timer.start();
        startMillis = TimeUtils.getCurrentMillis();
    }

    private  void showHandUpDialog(){
        mMaterialDialog =new MaterialDialog(this);
        final ExamResultBean resultBean=checkAnswer(mQuestionList);
        saveQuestionAndAnswer(mQuestionList,paper.getPaperId());
        mMaterialDialog.setTitle("交卷确认")
                .setMessage("您还有"+(listSize- resultBean.getWriteSum())+"题未做，确定要交卷吗")
                //mMaterialDialog.setBackgroundResource(R.drawable.background);
                .setPositiveButton("是的", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intent=new Intent(UIUtils.getContext(), ExamResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("EXAMRESULT", resultBean);
                        intent.putExtras(bundle);
                        intent.putExtra("PAPERID",paper.getPaperId());
                        //  intent.putExtra("TITLE",data);
                        startActivity(intent);

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

   private  class  MyViewPagerListener implements   ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            tvIndex.setText((position + 1) + "/" + mQuestionList.size());

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    private class MyPagerAdapter extends PagerAdapter {
        List<Question> list;

        public MyPagerAdapter(List<Question> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LogUtils.i("instantiateItem执行");
            View convertView = LayoutInflater.from(StartExamActivity.this).inflate(R.layout.layout_question_item, null);
            // mHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            TextView tv_question = (TextView) convertView.findViewById(R.id.question);
            RadioGroup radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGroup);
            RadioButton[] radioButtons = new RadioButton[4];
            radioButtons[0] = (RadioButton) convertView.findViewById(R.id.answerA);
            radioButtons[1] = (RadioButton) convertView.findViewById(R.id.answerB);
            radioButtons[2] = (RadioButton) convertView.findViewById(R.id.answerC);
            radioButtons[3] = (RadioButton) convertView.findViewById(R.id.answerD);
          //  TextView tv_explaination = (TextView) convertView.findViewById(R.id.explaination);
            //mHolder.imageView.setImageResource(mImages.get());


            Question q = list.get(position);

            radioGroup.clearCheck();

            int ans = list.get(position).getYourAnswer();
            Log.i("", "answer" + ans);
            for (int i = 0; i < 4; i++) {
                Log.i("", "for循环" + i);
                if (ans != -1) {
                    radioButtons[ans].setChecked(true);
                    break;
                }
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkId) {

                    switch (checkId) {
                        case R.id.answerA:

                            mQuestionList.get(position).setYourAnswer(0);
                            Log.i("", "你保存了answer=" + mQuestionList.get(position).getYourAnswer());
                            Toast.makeText(StartExamActivity.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.answerB:

                            mQuestionList.get(position).setYourAnswer(1);
                            Log.i("", "你保存了answer=" + mQuestionList.get(position).getYourAnswer());
                            Toast.makeText(StartExamActivity.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.answerC:

                            mQuestionList.get(position).setYourAnswer(2);
                            Log.i("", "你保存了answer=" + mQuestionList.get(position).getYourAnswer());
                            Toast.makeText(StartExamActivity.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.answerD:

                            mQuestionList.get(position).setYourAnswer(3);
                            Log.i("", "你保存了answer=" + mQuestionList.get(position).getYourAnswer());
                            Toast.makeText(StartExamActivity.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                            break;

                    }

                }
            });

            Log.i("tag", q.getTitle());
            tv_question.setText(q.getTitle());
           // tv_explaination.setText(q.getTitle());
            radioButtons[0].setText(q.getItema());
            radioButtons[1].setText(q.getItemb());
            radioButtons[2].setText(q.getItemc());
            radioButtons[3].setText(q.getItemd());


            container.addView(convertView);
            return convertView;

        }

        *//**//**
         * 一道单选题的ViewHolder
         *//**//*
         class ViewHolder {
            TextView tv_question;
            RadioButton[] radioButtons;

            TextView tv_explaination;
            RadioGroup radioGroup;

        }

    }

    private void saveQuestionAndAnswer(final List<Question> list, int id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PaperDao paperDao=new PaperDao(UIUtils.getContext());
                    paperDao.addOnePaper(list,paper.getPaperId());
                    LogUtils.i("保存试卷试题到数据库成功！");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    LogUtils.i("保存试卷试题到数据库时出错！");
                }

            }
        }).start();
    }

    private  ExamResultBean checkAnswer(List<Question> list){
        int totalCount=list.size();
        int writeCount=0;
        int rightCount=0;
        ExamResultBean resultBean=new ExamResultBean();
        for (int i = 0; i< mQuestionList.size(); i++){
            Question question=list.get(i);
            LogUtils.i("第"+i+"题答案="+ question.getYourAnswer());
            if (question.getYourAnswer()!=-1){

                writeCount++;
            }
            if (question.getYourAnswer()==Integer.valueOf(question.getAnswer())){
                rightCount++;
            }
        }
        int itemScore=3;
        int allScore=paper.getAllscore();
        int youscore=itemScore*writeCount;
        String judge="";
        if (youscore>=allScore*0.6){
            if (youscore>=allScore*0.7){
                if (youscore>=allScore*0.8){
                    if (youscore>allScore*0.9){
                        judge="优秀";
                        if (youscore==allScore) {
                            judge ="满分";
                        }
                    }
                }else {//70~80
                    judge="良好";
                }
            }else {//60~70
                judge="一般";
            }
        }else {//<60分
            judge="不及格";
        }
        long spend=TimeUtils.getCurrentMillis()-startMillis ;
        int l = (int)TimeUtils.converMillisToMinute(spend);
        resultBean.setPaperName(paper.getTitle());//试卷名称
        resultBean.setQuestionSum(totalCount);//总题数
        resultBean.setWriteSum(writeCount);//作答题目数量
        resultBean.setRightSum(rightCount);//正确题数
        resultBean.setTotalTime(paper.getRespondencetime());//考试规定时间
        resultBean.setTotalScore(paper.getAllscore());//总分
        resultBean.setHandupTime(TimeUtils.getCurrentTime());//交卷时间
        resultBean.setYouScore(allScore);
        resultBean.setJudge(judge);

        resultBean.setSpendTime(l);
        return resultBean;
    }



    public void getDataFromBmod() {
        LogUtils.i("通过Bmod获取数据");
        //查询所有的游戏得分记录
        String bql ="select * from test_question";
        BmobQuery<Question> query=new BmobQuery<Question>();
        //设置查询的SQL语句
        query.setSQL(bql);
        query.doSQLQuery(UIUtils.getContext(),new SQLQueryListener<Question>(){

            @Override
            public void done(BmobQueryResult<Question> result, BmobException e) {
                if(e ==null){
                    List<Question> list = (ArrayList<Question>) result.getResults();
                    if(list!=null && list.size()>0){



                        for (int i=0;i<list.size();i++){
                           list.get(i).setYourAnswer(-1);
                        }
                        Message message=handler.obtainMessage();
                        message.what=2;
                        message.obj=list;

                        handler.sendMessage(message);


                    }else{
                        Log.i("smile", "查询成功，无数据返回");
                    }
                }else{
                    Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });
    }*/
}
