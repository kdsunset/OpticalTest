package com.zin.opticaltest.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zin.opticaltest.R;
import com.zin.opticaltest.activity.ChoiseQuesActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZIN on 2016/4/17.
 */
public class SpecialPracticeFrag extends Fragment  {

    @Bind(R.id.tv_choice_ques_count)
    TextView tvChoiceQuesCount;
    @Bind(R.id.cv_choice_ques)
    CardView cvChoiceQues;
    @Bind(R.id.tv_fillin_ques_count)
    TextView tvFillinQuesCount;
    @Bind(R.id.cv_fillin_ques)
    CardView cvFillinQues;
    @Bind(R.id.tv_mapping_ques_count)
    TextView tvMappingQuesCount;
    @Bind(R.id.cv_mapping_ques)
    CardView cvMappingQues;
    @Bind(R.id.tv_calculation_ques_count)
    TextView tvCalculationQuesCount;
    @Bind(R.id.cv_calculation_ques)
    CardView cvCalculationQues;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_special_practice, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        cvChoiceQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChoiseQuesActivity.class);
                intent.putExtra("quesType","选择题");
                startActivity(intent);
            }
        });
        cvFillinQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChoiseQuesActivity.class);
                intent.putExtra("quesType","填空题");
                startActivity(intent);
            }
        });
        cvMappingQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChoiseQuesActivity.class);
                intent.putExtra("quesType","作图题");
                startActivity(intent);
            }
        });
        cvCalculationQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ChoiseQuesActivity.class);
                intent.putExtra("quesType","计算题");
                startActivity(intent);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
