package com.zin.opticaltest.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zin.opticaltest.R;
import com.zin.opticaltest.dao.PaperDao;
import com.zin.opticaltest.entity.AnswerItem;
import com.zin.opticaltest.entity.ExamResultBean;
import com.zin.opticaltest.entity.OpticalTestURL;
import com.zin.opticaltest.entity.Question;
import com.zin.opticaltest.entity.TestBank;
import com.zin.opticaltest.entity.UploadBean;
import com.zin.opticaltest.entity.UploadFileResponse;
import com.zin.opticaltest.ui.QuestionItemPage;
import com.zin.opticaltest.utils.ImageUtils;
import com.zin.opticaltest.utils.LogUtils;
import com.zin.opticaltest.utils.TimeUtils;
import com.zin.opticaltest.utils.UIUtils;
import com.zin.opticaltest.utils.UserSharePreHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.Call;
import okhttp3.MediaType;


public class StartExamActivity3 extends AppCompatActivity {

    private ViewPager mViewPager;

    ImageButton ibSelectIndex;
    TextView tvIndex;
    int listSize;
    List<Question> mQuestionList;
    List<QuestionItemPage> mQuesItemViewList;
     TestBank paper;
    long startMillis;
    MaterialDialog mMaterialDialog;
    private final static int MODE_EXAM=100;
    private final static int MODE_CHECK=101;
    private final static int TYPE_FILLIN_QUESTION=1;
    private final static int TYPE_CHOICE_QUESTION=0;
    private final static int TYPE_CALCULATION_QUESTION=2;
    private  int question_type;
    private TextView tvTimer;
    private HashMap<Integer,String> urlmap;
    private  List<String> paths=new ArrayList<>();
    private QuestionItemPage mQuesItemPage;
    private ProgressBar progressBar;
    private String userId;
    private List<Question> newQuesList;
    private   int ACTIVITY_MODE;
    private  int paperid;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                case 2:
                    mQuestionList = (ArrayList<Question>) msg.obj;
                    listSize = mQuestionList.size();
                    setIndex();
                    /*MyPagerAdapter mAda  pter = new MyPagerAdapter(mQuestionList);
                    MyViewPagerListener pagerChangeListener = new MyViewPagerListener();
                    mViewPager.setOnPageChangeListener(pagerChangeListener);
                    mViewPager.setAdapter(mAdapter);
                    mViewPager.setCurrentItem(0);*/
                    setQuesItemView();
                    /**/
                    break;
            }
        }
    };
    private int curP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);

        Intent intent=getIntent();
         ACTIVITY_MODE = intent.getExtras().getInt("MODE");
        paperid = intent.getExtras().getInt("PAPERID");
        UserSharePreHelper helper=new UserSharePreHelper(this);
        userId = helper.getUserId();

        urlmap=new HashMap<>();
        initView();
        /*if (ACTIVITY_MODE==MODE_EXAM) {
            getDataFromBmod();
        }else {
            loadPaper();
        }*/
        if (ACTIVITY_MODE==MODE_CHECK){
           // loadPaper();
            loadDataFromLocal();

        }else {
            paper =(TestBank) intent.getParcelableExtra("PAPERINFO");
          //  getDataFromBmod();
            getDataFromNet();
        }



    }
    /**
     * 生成了数据list和 questionItemViewList
     */
    private  void setQuesItemView(){
        mQuesItemViewList=new ArrayList<>();
        for(int i=0;i<mQuestionList.size();i++){
            mQuesItemPage = new QuestionItemPage(this,mQuestionList.get(i),ACTIVITY_MODE);
            mQuesItemViewList.add(mQuesItemPage);

        }

        QustionPageAdapter mAdapter = new QustionPageAdapter(mQuestionList,mQuesItemViewList);
        MyViewPagerListener pagerChangeListener = new MyViewPagerListener();
        mViewPager.setOnPageChangeListener(pagerChangeListener);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        progressBar.setVisibility(View.GONE);
    }
    private class QustionPageAdapter extends PagerAdapter{
        List<Question> questionDataList;
        List<QuestionItemPage> itemViewList;

        public QustionPageAdapter(List<Question> questionDataList, List<QuestionItemPage> itemViewList) {
            this.questionDataList = questionDataList;
            this.itemViewList = itemViewList;
        }

        @Override
        public int getCount() {
            return mQuestionList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View viewItem= itemViewList.get(position).getRootView();
            container.addView(viewItem);


            return viewItem;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView( itemViewList.get(position).getRootView());
        }


    }

    private void loadDataFromLocal(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PaperDao paperDao = new PaperDao(StartExamActivity3.this);
                List<Question> questionList = paperDao.showALLQuestionOfPaper(paperid);

                LogUtils.i("dao");
                LogUtils.i("id="+paperid);
                LogUtils.i("size="+questionList.size());
                for (int i=0;i<questionList.size();i++){
                    LogUtils.i(questionList.get(i).getTitle());
                }
                Message message=handler.obtainMessage();
                message.what=1;
                message.obj=questionList;
                handler.sendMessage(message);
            }
        }).start();
    }


    public void initView() {
     //   mToolbar= (Toolbar) findViewById(R.id.tbExamActivity);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mViewPager = (ViewPager) findViewById(R.id.vp_exam_activity);
        ImageButton ibBack=(ImageButton)findViewById(R.id.ib_back);
        ImageButton ibSelectIndex=(ImageButton)findViewById(R.id.ib_select_index);
        ImageButton ibHandup=(ImageButton)findViewById(R.id.ib_handup);
        ImageButton ib_more=(ImageButton)findViewById(R.id.ib_more);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ibHandup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHandUpDialog();
            }
        });
        tvTimer= (TextView) findViewById(R.id.tv_timer );
         ibSelectIndex= (ImageButton) findViewById(R.id.ib_select_index);
         tvIndex= (TextView) findViewById(R.id.tv_index);
        if (ACTIVITY_MODE== MODE_EXAM) {
            tvTimer.setVisibility(View.VISIBLE);
            initTimer();
        }else  if (ACTIVITY_MODE==MODE_CHECK){
            tvTimer.setCompoundDrawables(null,null,null,null);
            tvTimer.setText("试卷解析");
            ibHandup.setVisibility(View.GONE);

        }


       // mToolbar.setTitle("ZinFun");// 标题的文字需在setSupportActionBar之前，不然会无效
      //  mToolbar.setSubtitle("副标题");
       // setSupportActionBar(mToolbar);
        /* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
      /* / mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
*/
    }
    private class MyButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ib_back:
                    finish();
                    break;
                case R.id.ib_select_index:

                    break;
                case R.id.ib_handup:
                   showHandUpDialog();
                    break;
                case R.id.ib_more:

                    break;

            }
        }
    }


    private void setIndex(){
        tvIndex.setText(1+"/"+ listSize);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_exam_menu, menu);

        /*// 获取菜单项中的条目
        MenuItem item = menu.findItem(R.id.action_timer);
        // 获取条目中的View视图
        View view = item.getActionView();
        // 获取View视图中的控件
        final TextView tvTimer = (TextView) view.findViewById(R.id.tv_timer);
*/


        return true;
    }
    private void initTimer(){
        CountDownTimer timer = new CountDownTimer(120*60*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String result = TimeUtils.formatDuring(millisUntilFinished);
                tvTimer.setText(result);
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




   private  class  MyViewPagerListener implements   ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            tvIndex.setText((position + 1) + "/" + listSize);
            curP = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public List<Question> getQuesAndAnsList(){
        List<Question> newQuesList=new ArrayList<>();
        for (int i=0;i<mQuesItemViewList.size();i++){
            Question quesAndAns = mQuesItemViewList.get(i).getQuesAndAns();
           /* int quesId=quesAndAns.getItemId();
            if (urlmap.containsKey(quesId)){
                quesAndAns.setYouransimg(urlmap.get(quesId));
            }
*/
            newQuesList.add(quesAndAns);
        }

        return newQuesList;
    }
    //原来的 用户答案是保存在Question的，
    //现在要将用户答案的用户id,试卷Id，和QuestionList里的文字答案和图片上传到服务器
    //文字答案：取出QuestionList中的youans保存到uploadAnsBean的AnsItem
    //图片：开启异步线程，循环提交post请求，先将有答案里的图片上传到服务器，保存好了
    //返回一个服务器的保存地址 （AnsItem里的pic）
    //将uploadAnsBean转换成Json，上传到服务器，保存在用户答案数据表
    //上传结束
    private  void showHandUpDialog(){
        mMaterialDialog =new MaterialDialog(this);
        //带有答案的QuestionList
        newQuesList = getQuesAndAnsList();
        uploadAnswerPictures(newQuesList);//将图片上传，返回图片保存在服务器的地址
       /* String json = convertAnsToJson(userId, paperid + "", newQuesList, urlmap);//转换json
        uploadAnswer(json);//post请求上传答案*/


        final ExamResultBean resultBean=checkAnswer(newQuesList);

        mMaterialDialog.setTitle("交卷确认")
                .setMessage("您还有"+(listSize- resultBean.getWriteSum())+"题未做，确定要交卷吗")
                //mMaterialDialog.setBackgroundResource(R.drawable.background);
                .setPositiveButton("是的", new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        saveQuestionAndAnswer(newQuesList,paper.getPaperId());
                        Intent intent=new Intent(UIUtils.getContext(), ExamResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("EXAMRESULT", resultBean);
                        intent.putExtras(bundle);
                        intent.putExtra("PAPERID",paper.getPaperId());
                        //  intent.putExtra("TITLE",data);
                        startActivity(intent);

                        mMaterialDialog.dismiss();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1001){
            switch (requestCode){
                case 1:
                case 2:
                    /* Resources res = getResources();
                Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.img_test);*/

                    String s=data.getStringExtra("PATH");
                    int id=data.getExtras().getInt("id");
                    int paperid=data.getExtras().getInt("paperid");

                    LogUtils.i("获取拍照的路径"+s);
                    mQuesItemViewList.get(curP).getQuesAndAns().setYouransimg(s);
                     LogUtils.i("保存到集合的路径"+mQuesItemViewList.get(curP).getQuesAndAns().getYouransimg());
                    int index=0;

                    for (int i=0;i<mQuesItemViewList.size();i++){
                        int paperId = mQuesItemViewList.get(i).getQuesAndAns().getPaperId();
                        LogUtils.i("45454paperid="+paperId);
                        if (paperId==id){
                            index=i;
                            LogUtils.i(""+index);
                            break;
                        }
                    }

                    mQuesItemViewList.get(curP).getLayoutAnswerEdittextImg().setVisibility(View.VISIBLE);
                    LogUtils.i("path"+mQuestionList.get(curP).getYouransimg());
                    ImageUtils.setImgForImageView(mQuesItemViewList.get(curP).getIvYourAnswerImg(),s);
                    LogUtils.i("拍完照了"+s);
/*
                    mQuesItemViewList.get(id).getLayoutAnswerEdittextImg().setVisibility(View.VISIBLE);
                    LogUtils.i("path"+mQuestionList.get(id).getYouransimg());
                    ImageUtils.setImgForImageView(mQuesItemViewList.get(id).getIvYourAnswerImg(),s);
                    LogUtils.i("拍完照了"+s);*/

                    break;

            }




        }
    }



    private String convertAnsToJson(String userId, String paperId,
                                    List<Question> questionList,HashMap<Integer,String> pathMap){
        //b保存答案到服务器
        //答案分两部分，字符和图片
        //现在的答案是Lis<Question>里的ans，和ansimg
        Gson gson=new Gson();
        UploadBean uploadBean=new UploadBean();
        LogUtils.i("convertAnsToJson-userId"+userId);
        List<AnswerItem> item= new ArrayList<>();
        for (int i=0;i<questionList.size();i++){
            AnswerItem answerItem=new  AnswerItem();
            Question question = questionList.get(i);
            String yourAnswer = question.getYourAnswer();
            int itemId = question.getQuesitonid();
            answerItem.setQuestionId(itemId+"");
            answerItem.setAnswerString(yourAnswer);
            if (question.getYouransimg()!=null){
               if (pathMap.containsKey(itemId)){
                   answerItem.setAnsPicUrl(pathMap.get(itemId));
               }
            }
           item.add(answerItem);
        }
        uploadBean.setPaperId(paperId);
        uploadBean.setUserId(userId);
        uploadBean.setAnswerItem(item);
        String json = gson.toJson(uploadBean);
        LogUtils.i("json:"+json);
        return  json;

    }


    private class  UploadFileTask extends AsyncTask<List<Question>,Void,HashMap<Integer,String>>{

        @Override
        protected HashMap<Integer,String> doInBackground(List<Question>... lists) {
            List<Question> list = lists[0];
            HashMap<Integer,String> pathInServiceMap=new HashMap<>();
            for (int i=0;i<list.size();i++){
                Question question = list.get(i);
                String path=question.getYouransimg();
                int quesid=question.getQuesitonid();
                String filename="ansimg_id="+quesid+"_"+TimeUtils.getCurrentMillis()+".jpg";
                if (!TextUtils.isEmpty(path)){
                    File file = new File(path);
                    if (file.exists()) {
                        System.out.println("文件存在");

                        try {
                            okhttp3.Response fileresponse = OkHttpUtils
                                    .post()
                                    .url(OpticalTestURL.URL_UPLOADFILE)


                                    .addFile("file", filename, file)
                                    .build()
                                    .execute();
                            if (fileresponse.isSuccessful()){
                                String string = fileresponse.body().string();
                                LogUtils.i(""+string);
                                Gson gson=new Gson();
                                UploadFileResponse bean = gson.fromJson(string, UploadFileResponse.class);

                                pathInServiceMap.put(quesid,bean.filePath);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }

            }
            return pathInServiceMap;
        }

        @Override
        protected void onPostExecute(HashMap<Integer,String> map) {
            super.onPostExecute(map);
            String json = convertAnsToJson(1+"", paperid + "", newQuesList, map);//转换json

            uploadAnswer(json);//post请求上传答案



        }
    }

    private  void  uploadAnswerPictures(List<Question> list){
        UploadFileTask task=new UploadFileTask();
        task.execute(list);

    }
    private void uploadAnswer(String json){


     /*   OkHttpUtils
                .post()
                .url(OpticalTestURL.URL_UPLOADANSWER)
                .addParams("ans",json)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.i(e.toString());

                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i(response);
                    }
                });*/
           MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpUtils
                .postString()
                .url(OpticalTestURL.URL_UPLOADANSWER)
                .mediaType(JSON)
                .content(json)
                .build()
               .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        LogUtils.i(e.toString());

                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i(response);
                    }
                });


    }



    private void saveQuestionAndAnswer(final List<Question> list, final int id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PaperDao paperDao=new PaperDao(UIUtils.getContext());
                    paperDao.delAllQuestionOfPaper(paper);
                    paperDao.addOnePaper(list,paper.getPaperId());
                    LogUtils.i("保存试卷试题到数据库成功！");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    LogUtils.i("保存试卷试题到数据库时出错！");
                }

            }
        }).start();
    }

    private  List<Question> getFillinAnsList(){
        List<Question> newQuestionList=new ArrayList<>();
        for (int i=0;i<mQuestionList.size();i++){
           newQuestionList.add( mQuesItemViewList.get(i).getFillinQuestion());
        }
        return  newQuestionList;
    }
    private  ExamResultBean checkAnswer(List<Question> list){
        int totalCount=list.size();
        int writeCount=0;
        int rightCount=0;
        int selectQuesCount=0;
        ExamResultBean resultBean=new ExamResultBean();
        for (int i = 0; i< list.size(); i++){
            Question question=list.get(i);
            LogUtils.i("第"+i+"题答案="+ question.getYourAnswer());
            String yourans=question.getYourAnswer();
            String youransimg=question.getYouransimg();
            String rightAns=question.getAnswer();
            if (!TextUtils.isEmpty(yourans)||!TextUtils.isEmpty(youransimg)){

                writeCount++;
            }

            if (question.getTestType().equals("0")){
                selectQuesCount++;
                if (rightAns.equals(yourans))
                    rightCount++;
            }

        }
        int itemScore=3;
        int allSelectQuesScore=selectQuesCount*3;
        int youscore=itemScore*rightCount;
        String judge="";
        if (youscore>=allSelectQuesScore*0.6){
            if (youscore>=allSelectQuesScore*0.7){
                if (youscore>=allSelectQuesScore*0.8){
                    if (youscore>allSelectQuesScore*0.9){
                        judge="优秀";
                        if (youscore==allSelectQuesScore) {
                            judge ="全对";
                        }
                    }
                }else {//70~80
                    judge="良好";
                }
            }else {//60~70
                judge="一般";
            }
        }else {//<60分
            judge="差";
        }
        long spend=TimeUtils.getCurrentMillis()-startMillis ;
        int l = (int)TimeUtils.converMillisToMinute(spend);
        if (l<1) l=1;
        resultBean.setPaperName(paper.getTitle());//试卷名称
        resultBean.setQuestionSum(totalCount);//总题数
        resultBean.setWriteSum(writeCount);//作答题目数量
        resultBean.setRightSum(rightCount);//正确题数
        resultBean.setTotalTime(paper.getRespondencetime());//考试规定时间
        resultBean.setTotalScore(paper.getAllscore());//总分
        resultBean.setHandupTime(TimeUtils.getCurrentTime());//交卷时间
        resultBean.setYouScore(youscore);
        resultBean.setJudge(judge);

        resultBean.setSpendTime(l);
        return resultBean;
    }

    private void getDataFromNet( ) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(OpticalTestURL.URL_ONEPAPERQUESTIONS).newBuilder()
                .addQueryParameter("paperId", paper.getPaperId()+"")
                .build();
        LogUtils.i("1.网络地址"+url.toString());
        final Request request = new Request.Builder()
                .url(url)
                .build();
        com.squareup.okhttp.Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Message message=handler.obtainMessage();
                message.what=-1;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(final Response response) throws IOException {
                String resultJson = response.body().string();
                LogUtils.i("4.请求结果" + resultJson);
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray Jarray = parser.parse(resultJson).getAsJsonArray();
                ArrayList<Question> lcs = new ArrayList<>();
                for (JsonElement obj : Jarray) {
                    Question cse = gson.fromJson(obj, Question.class);
                    lcs.add(cse);
                }
                Message message = handler.obtainMessage();
                message.what = 2;
                message.obj = lcs;
                handler.sendMessage(message);
            }
        });
    }

    public void getDataFromBmod() {
        LogUtils.i("通过Bmod获取整张试题");
        //查询所有的游戏得分记录
        String bql ="select * from test_question order by itemId asc";
        final BmobQuery<Question> query=new BmobQuery<Question>();
        //设置查询的SQL语句
        query.setSQL(bql);
        query.doSQLQuery(UIUtils.getContext(),new SQLQueryListener<Question>(){

            @Override
            public void done(BmobQueryResult<Question> result, BmobException e) {
                if(e ==null){
                    List<Question> list = (ArrayList<Question>) result.getResults();
                    if(list!=null && list.size()>0){
                       /* for (Question question :list){
                            if (question.getTestType().equals("0")){
                                question.setYourAnswer("-1");
                            }
                        }*/
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
    }



}
