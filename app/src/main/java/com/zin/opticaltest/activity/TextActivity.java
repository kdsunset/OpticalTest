package com.zin.opticaltest.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zin.opticaltest.R;
import com.zin.opticaltest.entity.Question;
import com.zin.opticaltest.ui.QuestionItemPage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZIN on 2016/4/12.
 */
public class TextActivity extends Activity {

    @Bind(R.id.vp)
    ViewPager vp;
    private List<QuestionItemPage> pageList;
    List<Question> mQuestionList;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_text);
        ButterKnife.bind(this);

        /**
         * 获取数据
         */

        mQuestionList = new ArrayList<>();
        pageList = new ArrayList<>();







    }


    private class QustionPageAnapter extends PagerAdapter{

        public QustionPageAnapter() {
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
            container.addView( pageList.get(position).getRootView());


            return super.instantiateItem(container, position);
        }
    }


}
