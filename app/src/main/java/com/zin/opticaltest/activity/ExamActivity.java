package com.zin.opticaltest.activity;

import android.support.v7.app.AppCompatActivity;

public class ExamActivity extends AppCompatActivity {

    /*private ViewPager mViewPager;
    Toolbar mToolbar;
    ImageButton ibSelectIndex;
    TextView tvIndex;
    int curPosition=0;
    LinkedList<View> mCaches = new LinkedList<View>();
    List<Question> mQuestionList;
    List<Integer> yourAnswerList;
    MyViewPagerListener pagerChangeListener=new MyViewPagerListener();
    MaterialDialog mMaterialDialog;
    MyPagerAdapter mAdapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==2){

                mQuestionList = (ArrayList<Question>) msg.obj;
                setIndex();
                mAdapter = new MyPagerAdapter(mQuestionList);
                mViewPager.setAdapter(mAdapter);

                

                mViewPager.setOnPageChangeListener(pagerChangeListener);
                mViewPager.setCurrentItem(0);

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
        tvIndex.setText(1+"/"+ mQuestionList.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_exam_menu, menu);

      /*  *//**//*//**//*//* 获取菜单项中的条目
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
    }

    private  void showHandUpDialog(){
        mMaterialDialog =new MaterialDialog(this);
        mMaterialDialog.setTitle("交卷确认")
                .setMessage("您还有"+(mQuestionList.size()-checkAnswer())+"题未做，确定要交卷吗")
                //mMaterialDialog.setBackgroundResource(R.drawable.background);
                .setPositiveButton("是的", new View.OnClickListener() {
                    @Override public void onClick(View v) {
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
            //  viewpager.setCurrentItem(arg0);
            curPosition=position;

           // setRadioButton(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    *//**//*private  void setRadioButton(int p){
        RadioGroup radioGroup = (RadioGroup) mViewPager.findViewWithTag("buttonKey"+p );
        final RadioButton[] buttons =  new RadioButton[4];
        buttons[0]= (RadioButton) mViewPager.findViewWithTag("A"+p);
        buttons[1]= (RadioButton) mViewPager.findViewWithTag("B"+p);
        buttons[2]= (RadioButton) mViewPager.findViewWithTag("C"+p);
        buttons[3]= (RadioButton) mViewPager.findViewWithTag("D"+p);
        final int pos=p;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                LogUtils.i("你点击");

                yourAnswerList=new ArrayList<>();

                for(int i = 0; i < 4; i++)
                {
                    if(buttons[i].isChecked() == true)
                    {
                        mQuestionList.get(pos).setYourAnswer(i);
                        LogUtils.i("你点击的是"+i);
                        yourAnswerList.add(i);
                        break;
                    }else {
                        mQuestionList.get(pos).setYourAnswer(-1);
                        yourAnswerList.add(-1);
                    }
                }
            }
        });
        mAdapter.notifyDataSetChanged();
    }*//**//*


    private void setViewPagerAdapter( ) {

    }

      class  MyPagerAdapter extends  PagerAdapter{

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
            return  view==object;

        }

          @Override
          public void setPrimaryItem(ViewGroup container, int position, Object object) {
              super.setPrimaryItem(container, position, object);
          }


          @Override
          public void destroyItem(ViewGroup container, int position, Object object) {
              container.removeView((View) object);
              mCaches.add((View) object);
          }
          @Override
          public Object instantiateItem(ViewGroup container, final int position) {
              LogUtils.i("instantiateItem执行");
              View convertView = null;
              ViewHolder mHolder = null;
              if (mCaches.size() == 0) {
                  convertView = LayoutInflater.from(ExamActivity.this).inflate(R.layout.layout_question_item, null);
                  mHolder = new ViewHolder();
                  // mHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
                  mHolder.tv_question = (TextView) convertView.findViewById(R.id.question);
                  mHolder.radioButtons = new RadioButton[4];
                  mHolder.radioButtons[0] = (RadioButton) convertView.findViewById(R.id.answerA);
                  mHolder.radioButtons[1] = (RadioButton) convertView.findViewById(R.id.answerB);
                  mHolder.radioButtons[2] = (RadioButton) convertView.findViewById(R.id.answerC);
                  mHolder.radioButtons[3] = (RadioButton) convertView.findViewById(R.id.answerD);
                  mHolder.radioButtons[0].setTag("A"+position);
                  mHolder.radioButtons[1].setTag("B"+position);
                  mHolder.radioButtons[2].setTag("C"+position);
                  mHolder.radioButtons[3].setTag("D"+position);
                 // mHolder.tv_explaination = (TextView) convertView.findViewById(R.id.explaination);
                  mHolder.radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGroup);
                  convertView.setTag(mHolder);
              } else {
                  convertView = mCaches.removeFirst();
                  mHolder = (ViewHolder) convertView.getTag();
              }
              //mHolder.imageView.setImageResource(mImages.get());

              Question q = list.get(position);
              LogUtils.i("075answer"+list.get(position).getYourAnswer());
            //  mHolder.radioGroup.clearCheck();

              if (yourAnswerList.get(position) != -1) {
                  mHolder.radioButtons[yourAnswerList.get(position)].setChecked(true);
              }

              Log.i("tag", q.getTitle());
              mHolder.tv_question.setText(q.getTitle());
             // mHolder.tv_explaination.setText(q.getTitle());
              mHolder.radioButtons[0].setText(q.getItema());
              mHolder.radioButtons[1].setText(q.getItemb());
              mHolder.radioButtons[2].setText(q.getItemc());
              mHolder.radioButtons[3].setText(q.getItemd());
              String buttonKey = "buttonKey"+position ;
              mHolder.radioGroup.setTag(buttonKey);

              mHolder.tv_question.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      LogUtils.i("你点击了标题"+position);
                      Toast.makeText(ExamActivity.this,"你点击了标题"+position,Toast.LENGTH_SHORT).show();
                  }
              });

              final RadioButton[] buttons =  mHolder.radioButtons;
              final int pos=position;
              mHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                  @Override
                  public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                      LogUtils.i("你点击");

                      yourAnswerList=new ArrayList<>();

                      for(int i = 0; i < 4; i++)
                      {
                          if(buttons[i].isChecked() == true)
                          {
                              list.get(pos).setYourAnswer(i);
                              LogUtils.i("你点击的是"+i);
                              yourAnswerList.add(i);
                              break;
                          }else {
                              list.get(pos).setYourAnswer(-1);
                              yourAnswerList.add(-1);
                          }
                      }
                  }
              });


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



    private  int checkAnswer(){
        int total=0;
        for (int i = 0; i< mQuestionList.size(); i++){
            LogUtils.i("第"+i+"题答案="+ mQuestionList.get(i).getYourAnswer());
            if (mQuestionList.get(i).getYourAnswer()!=-1){

                total++;
            }
        }
        return total;
    }
    private  int getyourAnswerByPosition(int pos){

        return  mQuestionList.get(pos).getYourAnswer();
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
