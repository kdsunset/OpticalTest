package com.zin.opticaltest.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.zin.opticaltest.R;
import com.zin.opticaltest.entity.OpticalTestURL;
import com.zin.opticaltest.entity.Question;
import com.zin.opticaltest.ui.QuestionItemPage;
import com.zin.opticaltest.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChoiseQuesActivity extends BaseActivity {


    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.ib_more)
    ImageButton ibMore;
    @Bind(R.id.ib_check_answer)
    TextView tvCheckAnswer;
    @Bind(R.id.tv_index)
    TextView tvIndex;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.vp_practice_activity)
    ViewPager vpPracticeActivity;
    private String mQuesType;
    public List<Question> mQuestionList;
    private int listSize;
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
                    setQuesItemView();
                    break;
            }
        }
    };
    private List<QuestionItemPage> mQuesItemViewList;
    private QuestionItemPage mQuesItemPage;

    /**
     * 生成了数据list和 questionItemViewList
     */
    private  void setQuesItemView(){
        mQuesItemViewList=new ArrayList<>();
        for(int i=0;i<mQuestionList.size();i++){
            mQuesItemPage = new QuestionItemPage(this,mQuestionList.get(i),102);
            mQuesItemViewList.add(mQuesItemPage);

        }

        QustionPageAdapter mAdapter = new QustionPageAdapter(mQuestionList,mQuesItemViewList);
        MyViewPagerListener pagerChangeListener = new MyViewPagerListener();
        vpPracticeActivity.setOnPageChangeListener(pagerChangeListener);
        vpPracticeActivity.setAdapter(mAdapter);
        vpPracticeActivity.setCurrentItem(0);
        viewAnswer(0);
        progressBar.setVisibility(View.GONE);
    }
    private void setIndex(){
        tvIndex.setText(1+"/"+ listSize);

    }

    private class QustionPageAdapter extends PagerAdapter {
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
    private  void viewAnswer(final int curPs){

        mQuesItemViewList.get(curPs).getLayoutAnswer().setVisibility(View.GONE);
        mQuesItemViewList.get(curPs).getLayoutAnalysis().setVisibility(View.GONE);
        tvCheckAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuesItemViewList.get(curPs).getLayoutAnswer().setVisibility(View.VISIBLE);
                mQuesItemViewList.get(curPs).getLayoutAnalysis().setVisibility(View.VISIBLE);
            }
        });
    }
    private  class  MyViewPagerListener implements   ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected( int position) {

            tvIndex.setText((position + 1) + "/" + listSize);

          viewAnswer(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choise_ques);
        ButterKnife.bind(this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mQuesType = getIntent().getStringExtra("quesType");
        tvBarTitle.setText(mQuesType);
        LogUtils.i(TextUtils.isEmpty(mQuesType)==true?"空":mQuesType);
        getDataFromNet(mQuesType);


    }

    private void getDataFromNet(String type) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(OpticalTestURL.URL_QUESLISTBYTYPE).newBuilder()
                .addQueryParameter("quesType",type)
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
}
