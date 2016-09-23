package com.zin.opticaltest.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zin.opticaltest.R;
import com.zin.opticaltest.activity.MappingActivity;
import com.zin.opticaltest.activity.TakePhotoActivity;
import com.zin.opticaltest.entity.Question;
import com.zin.opticaltest.utils.ConvertAnswerInfoUtils;
import com.zin.opticaltest.utils.ImageUtils;
import com.zin.opticaltest.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZIN on 2016/4/12.
 */
public class QuestionItemPage {


    @Bind(R.id.et_answerfor_edittext)
    EditText etAnswerforEdittext;
    @Bind(R.id.bt_done_edittext)
    Button btDoneEdittext;
    @Bind(R.id.layout_answer_edittext)
    LinearLayout layoutAnswerEdittext;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.iv_question_img)
    ImageView ivQuestionImg;
    @Bind(R.id.bt_start_mapping)
    Button btStartMapping;
    @Bind(R.id.iv_answer_img)
    ImageView ivAnswerImg;
    @Bind(R.id.iv_your_answer_img)
    ImageView ivYourAnswerImg;
    @Bind(R.id.iv_analysis_img)
    ImageView ivAnalysisImg;
    @Bind(R.id.bt_upload_answer_img)
    Button btUploadAnswerImg;
    @Bind(R.id.layout_answer_edittext_img)
    LinearLayout layoutAnswerEdittextImg;
    private Question mData;
    @Bind(R.id.question)
    TextView tvQuestion;
    @Bind(R.id.answerA)
    RadioButton rbAnswerA;
    @Bind(R.id.answerB)
    RadioButton rbAnswerB;
    @Bind(R.id.answerC)
    RadioButton rbAnswerC;
    @Bind(R.id.answerD)
    RadioButton rbAnswerD;
    @Bind(R.id.radioGroup)
    RadioGroup radioGroup;


    @Bind(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @Bind(R.id.tv_fillin_answer)
    TextView tvFillinAnswer;
    @Bind(R.id.answer_layout)
    LinearLayout layoutAnswer;
    @Bind(R.id.tv_analysis)
    TextView tvAnalysis;
    @Bind(R.id.analysis_layout)
    LinearLayout layoutAnalysis;
    private Context context;
    private View convertView;
    private int question_type;
    private final static int MODE_EXAM = 100;
    private final static int MODE_CHECK = 101;
    private final static int MODE_PRACTISE = 102;
    private final static int TYPE_FILLIN_QUESTION = 1;
    private final static int TYPE_CHOICE_QUESTION = 0;
    private final static int TYPE_CALCULATION_QUESTION = 3;
    private final static int TYPE_MAPPING_QUESTION = 2;

    public LinearLayout getLayoutAnswer() {
        return layoutAnswer;
    }

    public LinearLayout getLayoutAnalysis() {
        return layoutAnalysis;
    }

    RadioButton[] radioButtons;
    private int activity_mode;
    private List<Question> mQuestionList;
    private Question mQuestion;

    public QuestionItemPage(Context context, Question data, int mode) {
        this.context = context;
        this.mData = data;
        this.activity_mode = mode;
        mQuestionList = new ArrayList<>();
        mQuestion = data;
        initView();
        setData(mData);

    }

    private void initView() {
        LogUtils.i("instantiateItem执行");

        convertView = LayoutInflater.from(context).inflate(R.layout.layout_question_item, null);
        // TextView tvTitle= (TextView) convertView.findViewById(R.id.question);
        ButterKnife.bind(this, convertView);
        // mHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView);


        radioButtons = new RadioButton[4];
        radioButtons[0] = rbAnswerA;
        radioButtons[1] = rbAnswerB;
        radioButtons[2] = rbAnswerC;
        radioButtons[3] = rbAnswerD;

    }

    public ImageView getIvYourAnswerImg() {
        return ivYourAnswerImg;
    }

    public void setIvYourAnswerImg(ImageView ivYourAnswerImg) {

        this.ivYourAnswerImg = ivYourAnswerImg;
    }

    public LinearLayout getLayoutAnswerEdittextImg() {
        return layoutAnswerEdittextImg;
    }


    public void setData(final Question q) {
        //题目描述
        tvQuestion.setText(q.getTitle());
        Log.i("tag", q.getTitle());
        //题目图片
        if (q.getQuesimg() != null) {
            ivQuestionImg.setVisibility(View.VISIBLE);
            String imageUrl = q.getQuesimg();
            LogUtils.i("加载题目图片" + imageUrl);

            //显示图片的配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    //  .showImageOnLoading(R.drawable.ic_stub)
                    .showImageOnFail(R.drawable.ic_loadimg_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(imageUrl, ivQuestionImg, options);
        }
        //题目的类型
        question_type = ConvertAnswerInfoUtils.convertQuestType(q.getTestType());
        setViewVisiableByQuesType(q);
        LogUtils.i("type" + q.getTestType());

        if (activity_mode == MODE_EXAM) {
            //   setViewVisiableByQuesType();
            layoutAnswer.setVisibility(View.GONE);
            layoutAnalysis.setVisibility(View.GONE);
            etAnswerforEdittext.setEnabled(true);
        } else if (activity_mode == MODE_CHECK) {
            LogUtils.i("checkMode");
            // setViewVisiableByQuesType();
            layoutAnswer.setVisibility(View.VISIBLE);
            layoutAnalysis.setVisibility(View.VISIBLE);
            etAnswerforEdittext.setEnabled(false);
            if (TextUtils.isEmpty(q.getYourAnswer())) {
                etAnswerforEdittext.setHint("");
            }
            setAnsAndAnalysisData(q);

        } else if (activity_mode == MODE_PRACTISE) {
            //  setViewVisiableByQuesType();
            layoutAnswer.setVisibility(View.VISIBLE);
            layoutAnalysis.setVisibility(View.VISIBLE);
            etAnswerforEdittext.setEnabled(true);
            if (TextUtils.isEmpty(q.getYourAnswer())) {
                etAnswerforEdittext.setHint("");
            }
            setAnsAndAnalysisData(q);
        }

        mQuestionList.add(q);


    }
    private void setAnsAndAnalysisData(Question q){


        String rightAns = q.getAnswer();
        String analysis = q.getAnalysis();


        if (!TextUtils.isEmpty(rightAns)) {
            etAnswerforEdittext.setText(rightAns);
            tvRightAnswer.setText("正确答案：" + q.getAnswer());
        }
        if (!TextUtils.isEmpty(analysis)) {
            tvAnalysis.setText(q.getAnalysis());
        }

        if (q.getAnsimg() != null) {
            String imgPath = q.getAnsimg();
            ImageUtils.setImgForImageView(ivAnswerImg, imgPath);
        }
        if (q.getAnalysisimg() != null) {
            String imgPath = q.getAnalysisimg();
            ImageUtils.setImgForImageView(ivAnalysisImg, imgPath);
        }
    }

    private void setChionseQuesData(Question q) {
        radioGroup.clearCheck();
        int rigans;
        int yourans;
        for (int i = 0; i < 4; i++) {
            Log.i("", "for循环" + i);
            rigans = ConvertAnswerInfoUtils.ConvertChoiceItem(q.getAnswer());
            yourans = ConvertAnswerInfoUtils.ConvertChoiceItem(q.getYourAnswer());
            LogUtils.i("rigans =" + rigans + "yourans =" + yourans);
            if (yourans != -1) {
                radioButtons[yourans].setChecked(true);
                if (activity_mode == MODE_CHECK || activity_mode == MODE_PRACTISE) {
                    if (yourans == rigans) {
                        radioButtons[rigans].setTextColor(Color.GREEN);
                    } else {
                        radioButtons[rigans].setTextColor(Color.GREEN);
                        radioButtons[yourans].setTextColor(Color.RED);

                    }
                }
                break;
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkId) {

                    switch (checkId) {
                        case R.id.answerA:

                            mQuestion.setYourAnswer("A");
                            Log.i("", "你选择了answer=" + mQuestion.getYourAnswer());
                            //Toast.makeText(StartExamActivity2.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.answerB:

                            mQuestion.setYourAnswer("B");
                            Log.i("", "你选择了answer=" + mQuestion.getYourAnswer());
                            // Toast.makeText(StartExamActivity2.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.answerC:

                            mQuestion.setYourAnswer("C");
                            Log.i("", "你选择了answer=" + mQuestion.getYourAnswer());
                            // Toast.makeText(StartExamActivity2.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.answerD:

                            mQuestion.setYourAnswer("D");
                            Log.i("", "你选择了answer=" + mQuestion.getYourAnswer());
                            //Toast.makeText(StartExamActivity2.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                            break;

                    }

                }
            });
            radioButtons[0].setText(q.getItema());
            radioButtons[1].setText(q.getItemb());
            radioButtons[2].setText(q.getItemc());
            radioButtons[3].setText(q.getItemd());
        }
    }
    private  void setFillInQuesData(Question q){
        String preAns = q.getYourAnswer();
        if (!TextUtils.isEmpty(preAns)) {
            tvFillinAnswer.setText("你的答案：" + preAns);
        }
        btDoneEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trim = etAnswerforEdittext.getText().toString().trim();
                mQuestion.setYourAnswer(trim);
                Toast.makeText(context, "完成~", Toast.LENGTH_SHORT).show();
                Log.i("", "你填写了answer=" + mQuestion.getYourAnswer());
            }
        });
    }
    private void setMappingQuesData(final Question q){
        btStartMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MappingActivity.class);
                intent.putExtra("id", q.getQuesitonid());
                intent.putExtra("paperid", q.getPaperId());
                intent.putExtra("url", q.getQuesimg());

                ((Activity) context).startActivityForResult(intent, 2);
            }
        });
        btUploadAnswerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   context.startActivity(new Intent(context, TakePhotoActivity.class));
                Intent intent = new Intent(context, TakePhotoActivity.class);
                intent.putExtra("id", q.getQuesitonid());
                intent.putExtra("paperid", q.getPaperId());
                ((Activity) context).startActivityForResult(intent, 1);

            }
        });

        if (q.getYouransimg() != null) {
            layoutAnswerEdittextImg.setVisibility(View.VISIBLE);
            ivYourAnswerImg.setVisibility(View.VISIBLE);
            String imgPath = q.getYouransimg();
            ImageUtils.setImgForImageView(ivYourAnswerImg, imgPath);

        }
    }
    private void setCalculQuesData(final Question q){
        String preAns = q.getYourAnswer();
        if (!TextUtils.isEmpty(preAns)) {
            etAnswerforEdittext.setText(preAns);
        }
        btUploadAnswerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   context.startActivity(new Intent(context, TakePhotoActivity.class));
                Intent intent = new Intent(context, TakePhotoActivity.class);
                intent.putExtra("id", q.getQuesitonid());
                intent.putExtra("paperid", q.getPaperId());
                ((Activity) context).startActivityForResult(intent, 1);

            }
        });
        btDoneEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String trim = etAnswerforEdittext.getText().toString().trim();
                mQuestion.setYourAnswer(trim);
                Toast.makeText(context, "完成~", Toast.LENGTH_SHORT).show();
                Log.i("", "你填写了answer=" + mQuestion.getYourAnswer());
            }
        });

        if (q.getYouransimg() != null) {
            layoutAnswerEdittextImg.setVisibility(View.VISIBLE);
            ivYourAnswerImg.setVisibility(View.VISIBLE);
            String imgPath = q.getYouransimg();
            ImageUtils.setImgForImageView(ivYourAnswerImg, imgPath);

        }
    }
    private void setViewVisiableByQuesType(final Question q) {
        if (question_type==TYPE_CHOICE_QUESTION){
            LogUtils.i("choice");
            radioGroup.setVisibility(View.VISIBLE);
            layoutAnswerEdittext.setVisibility(View.GONE);
            btDoneEdittext.setVisibility(View.GONE);
            btStartMapping.setVisibility(View.GONE);
            btUploadAnswerImg.setVisibility(View.GONE);
            setChionseQuesData(q);

        }else if (question_type==TYPE_FILLIN_QUESTION){
            LogUtils.i("fillin");
            radioGroup.setVisibility(View.GONE);
            layoutAnswerEdittext.setVisibility(View.VISIBLE);
            btDoneEdittext.setVisibility(View.VISIBLE);
            btStartMapping.setVisibility(View.GONE);
            btUploadAnswerImg.setVisibility(View.GONE);
            setFillInQuesData(q);

        }else if (question_type==TYPE_MAPPING_QUESTION){
            LogUtils.i("mapping");
            radioGroup.setVisibility(View.GONE);
            layoutAnswerEdittext.setVisibility(View.GONE);
            btDoneEdittext.setVisibility(View.GONE);
            btStartMapping.setVisibility(View.VISIBLE);
            btUploadAnswerImg.setVisibility(View.VISIBLE);
            setMappingQuesData(q);

        }else if (question_type==TYPE_CALCULATION_QUESTION){
            LogUtils.i("calcul");
            etAnswerforEdittext.setHint("解：");
            layoutAnswerEdittext.setVisibility(View.VISIBLE);
            etAnswerforEdittext.setBackgroundResource(R.drawable.editview_board);
            etAnswerforEdittext.setLines(10);
            radioGroup.setVisibility(View.GONE);
            btDoneEdittext.setVisibility(View.VISIBLE);
            btStartMapping.setVisibility(View.GONE);
            btUploadAnswerImg.setVisibility(View.VISIBLE);
            setCalculQuesData(q);

        }
    }

    public void setData2(final Question q) {

        question_type = ConvertAnswerInfoUtils.convertQuestType(q.getTestType());
        LogUtils.i("type"+q.getTestType());

        tvQuestion.setText(q.getTitle());
        Log.i("tag", q.getTitle());
        if (q.getQuesimg() != null) {
            ivQuestionImg.setVisibility(View.VISIBLE);
            String imageUrl = q.getQuesimg();
            LogUtils.i("加载题目图片" + imageUrl);

            //显示图片的配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    //  .showImageOnLoading(R.drawable.ic_stub)
                    .showImageOnFail(R.drawable.ic_loadimg_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(imageUrl, ivQuestionImg, options);
        }
        if (question_type == TYPE_CHOICE_QUESTION) {
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.clearCheck();
            int rigans;
            int yourans;
            for (int i = 0; i < 4; i++) {
                Log.i("", "for循环" + i);
                 rigans = ConvertAnswerInfoUtils.ConvertChoiceItem(q.getAnswer());
                 yourans = ConvertAnswerInfoUtils.ConvertChoiceItem(q.getYourAnswer());
                LogUtils.i("rigans ="+rigans+"yourans ="+yourans);
                if (yourans != -1) {
                    radioButtons[yourans].setChecked(true);
                    if (activity_mode == MODE_CHECK||activity_mode==MODE_PRACTISE) {
                        if (yourans == rigans) {
                            radioButtons[rigans].setTextColor(Color.GREEN);
                        } else {
                            radioButtons[rigans].setTextColor(Color.GREEN);
                            radioButtons[yourans].setTextColor(Color.RED);

                        }
                    }
                    break;
                }
            }


            if (activity_mode == MODE_CHECK || activity_mode == MODE_PRACTISE) {
                //  radioGroup.setClickable(false);
                layoutAnalysis.setVisibility(View.VISIBLE);
                layoutAnswer.setVisibility(View.VISIBLE);
                tvAnalysis.setText(q.getAnalysis());
                tvRightAnswer.setText("正确答案：" + q.getAnswer());
                tvFillinAnswer.setText("你的答案：" + q.getYourAnswer());
            } else if (activity_mode == MODE_EXAM||activity_mode==MODE_PRACTISE) {
                layoutAnalysis.setVisibility(View.GONE);
                layoutAnswer.setVisibility(View.GONE);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {

                        switch (checkId) {
                            case R.id.answerA:

                                mQuestion.setYourAnswer("A");
                                Log.i("", "你选择了answer=" + mQuestion.getYourAnswer());
                                //Toast.makeText(StartExamActivity2.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.answerB:

                                mQuestion.setYourAnswer("B");
                                Log.i("", "你选择了answer=" + mQuestion.getYourAnswer());
                                // Toast.makeText(StartExamActivity2.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.answerC:

                                mQuestion.setYourAnswer("C");
                                Log.i("", "你选择了answer=" + mQuestion.getYourAnswer());
                                // Toast.makeText(StartExamActivity2.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.answerD:

                                mQuestion.setYourAnswer("D");
                                Log.i("", "你选择了answer=" + mQuestion.getYourAnswer());
                                //Toast.makeText(StartExamActivity2.this, "你点击了单选按钮" + mQuestionList.get(position).getYourAnswer() + "position=" + position, Toast.LENGTH_SHORT).show();
                                break;

                        }

                    }
                });
            }
            radioButtons[0].setText(q.getItema());
            radioButtons[1].setText(q.getItemb());
            radioButtons[2].setText(q.getItemc());
            radioButtons[3].setText(q.getItemd());


        } else {   //非选择题
            radioGroup.setVisibility(View.GONE);//将选项隐藏
            layoutAnswerEdittext.setVisibility(View.VISIBLE);//天空题和解答题用的是同一个editview
            String preAns = mQuestion.getYourAnswer();
            if (!TextUtils.isEmpty(preAns)) {
                etAnswerforEdittext.setText(preAns);
            }
            if (q.getYouransimg() != null) {
                layoutAnswerEdittextImg.setVisibility(View.VISIBLE);
                //  ivYourAnswerImg.setVisibility(View.VISIBLE);
                String imgPath = q.getYouransimg();
                ImageUtils.setImgForImageView(ivYourAnswerImg, imgPath);

            }


            if (question_type == TYPE_FILLIN_QUESTION) {//如果是填空题，
                if (activity_mode == MODE_EXAM) {
                    layoutAnalysis.setVisibility(View.GONE);
                    layoutAnswer.setVisibility(View.GONE);
                    //  String trim = etAnswerfoetrFillin.getText().toString().trim();
                    btDoneEdittext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String trim = etAnswerforEdittext.getText().toString().trim();
                            mQuestion.setYourAnswer(trim);
                            Toast.makeText(context, "完成~", Toast.LENGTH_SHORT).show();
                            Log.i("", "你填写了answer=" + mQuestion.getYourAnswer());
                        }
                    });
                    btUploadAnswerImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //   context.startActivity(new Intent(context, TakePhotoActivity.class));
                            Intent intent = new Intent(context, TakePhotoActivity.class);
                            intent.putExtra("id", q.getItemId());
                            intent.putExtra("paperid", q.getPaperId());
                            ((Activity) context).startActivityForResult(intent, 1);

                        }
                    });


                } else if (activity_mode == MODE_CHECK) {
                    layoutAnalysis.setVisibility(View.VISIBLE);
                    layoutAnswer.setVisibility(View.VISIBLE);
                    layoutAnswer.setOrientation(LinearLayout.VERTICAL);
                    viewLine.setVisibility(View.GONE);
                    tvFillinAnswer.setVisibility(View.GONE);
                    btDoneEdittext.setVisibility(View.GONE);
                    btUploadAnswerImg.setVisibility(View.GONE);
                    etAnswerforEdittext.setEnabled(false);
                    if (TextUtils.isEmpty(q.getYourAnswer())) {
                        etAnswerforEdittext.setHint("");
                    }

                    tvAnalysis.setText(q.getAnalysis());

                    tvRightAnswer.setText("正确答案：" + q.getAnswer() + "\n\n");
                    if (q.getAnsimg() != null) {
                        ivAnswerImg.setVisibility(View.VISIBLE);
                        String imgPath = q.getAnsimg();
                        ImageUtils.setImgForImageView(ivAnswerImg, imgPath);
                    }
                    if (q.getAnalysisimg() != null) {
                        ivAnalysisImg.setVisibility(View.VISIBLE);
                        String imgPath = q.getAnalysisimg();
                        ImageUtils.setImgForImageView(ivAnalysisImg, imgPath);
                    }


                }

            } else if (question_type == TYPE_MAPPING_QUESTION) {

                etAnswerforEdittext.setVisibility(View.GONE);
                btDoneEdittext.setVisibility(View.GONE);

                if (activity_mode == MODE_EXAM) {
                    btStartMapping.setVisibility(View.VISIBLE);
                    layoutAnswer.setVisibility(View.GONE);
                    btStartMapping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, MappingActivity.class);
                            intent.putExtra("id", q.getItemId());
                            intent.putExtra("paperid", q.getPaperId());
                            intent.putExtra("url", q.getQuesimg());

                            ((Activity) context).startActivityForResult(intent, 2);
                        }
                    });
                } else if (activity_mode == MODE_CHECK) {
                    layoutAnalysis.setVisibility(View.VISIBLE);
                    layoutAnswer.setVisibility(View.VISIBLE);
                    ivAnswerImg.setVisibility(View.VISIBLE);
                    ivAnalysisImg.setVisibility(View.VISIBLE);
                    btDoneEdittext.setVisibility(View.GONE);
                    btUploadAnswerImg.setVisibility(View.GONE);
                    viewLine.setVisibility(View.GONE);
                    tvFillinAnswer.setVisibility(View.GONE);
                    tvRightAnswer.setText("正确答案：" + q.getAnswer() + "\n\n");
                    if (q.getAnsimg() != null) {
                        ivAnswerImg.setVisibility(View.VISIBLE);
                        String imgPath = q.getAnsimg();
                        ImageUtils.setImgForImageView(ivAnswerImg, imgPath);
                    }
                    if (q.getAnsimg() != null) {
                        ivAnalysisImg.setVisibility(View.VISIBLE);
                        String imgPath = q.getAnalysisimg();
                        ImageUtils.setImgForImageView(ivAnswerImg, imgPath);
                    }

                }

            } else if (question_type == TYPE_CALCULATION_QUESTION) {
                etAnswerforEdittext.setHint("解：");
                etAnswerforEdittext.setBackgroundResource(R.drawable.editview_board);
                etAnswerforEdittext.setLines(10);
                if (activity_mode == MODE_EXAM) {
                    layoutAnalysis.setVisibility(View.GONE);
                    layoutAnswer.setVisibility(View.GONE);
                    btDoneEdittext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String trim = etAnswerforEdittext.getText().toString().trim();
                            mQuestion.setYourAnswer(trim);
                            Toast.makeText(context, "完成~", Toast.LENGTH_SHORT).show();
                            Log.i("", "你填写了answer=" + mQuestion.getYourAnswer());
                        }
                    });
                    btUploadAnswerImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //   context.startActivity(new Intent(context, TakePhotoActivity.class));
                            Intent intent = new Intent(context, TakePhotoActivity.class);
                            intent.putExtra("id", q.getItemId());
                            intent.putExtra("paperid", q.getPaperId());
                            ((Activity) context).startActivityForResult(intent, 1);

                        }
                    });
                } else if (activity_mode == MODE_CHECK) {
                    layoutAnalysis.setVisibility(View.VISIBLE);
                    layoutAnswer.setVisibility(View.VISIBLE);
                    viewLine.setVisibility(View.GONE);
                    tvFillinAnswer.setVisibility(View.GONE);
                    btDoneEdittext.setVisibility(View.GONE);
                    tvAnalysis.setText(q.getAnalysis());
                    btUploadAnswerImg.setVisibility(View.GONE);
                    etAnswerforEdittext.setEnabled(false);
                    if (TextUtils.isEmpty(q.getYourAnswer())) {
                        etAnswerforEdittext.setText("未作答");
                    }
                    tvRightAnswer.setText("正确答案：" + q.getAnswer() + "\n\n");
                    if (q.getAnsimg() != null) {
                        ivAnswerImg.setVisibility(View.VISIBLE);
                        String imgPath = q.getAnsimg();
                        ImageUtils.setImgForImageView(ivAnswerImg, imgPath);
                    }
                    if (q.getAnsimg() != null) {
                        ivAnalysisImg.setVisibility(View.VISIBLE);
                        String imgPath = q.getAnalysisimg();
                        ImageUtils.setImgForImageView(ivAnswerImg, imgPath);
                    }


                }
            }
            mQuestionList.add(q);
        }


        /*else if (question_type == TYPE_FILLIN_QUESTION) {
            radioGroup.setVisibility(View.GONE);
            layoutAnswerEdittext.setVisibility(View.VISIBLE);
            if (q.getYouransimg()!=null){
                ivYourAnswerImg.setVisibility(View.VISIBLE);

            }
            String preAns = mQuestion.getYourAnswer();
            if (!TextUtils.isEmpty(preAns)) {
                etAnswerforEdittext.setText(preAns);
            }
            if (preAns == "-1")
                etAnswerforEdittext.setText(null);

            if (activity_mode == MODE_EXAM) {
                layoutAnalysis.setVisibility(View.GONE);
                layoutAnswer.setVisibility(View.GONE);
                //  String trim = etAnswerfoetrFillin.getText().toString().trim();
                btDoneEdittext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String trim = etAnswerforEdittext.getText().toString().trim();
                        mQuestion.setYourAnswer(trim);
                        Toast.makeText(context, "完成~", Toast.LENGTH_SHORT).show();
                        Log.i("", "你填写了answer=" + mQuestion.getYourAnswer());
                    }
                });


            } else if (activity_mode == MODE_CHECK) {
                layoutAnalysis.setVisibility(View.VISIBLE);
                layoutAnswer.setVisibility(View.VISIBLE);
                layoutAnswer.setOrientation(LinearLayout.VERTICAL);
                viewLine.setVisibility(View.GONE);
                tvFillinAnswer.setVisibility(View.GONE);
                btDoneEdittext.setVisibility(View.GONE);
                tvAnalysis.setText(q.getAnalysis());
                tvRightAnswer.setText("正确答案：" + q.getAnswer() + "\n\n");


            }


        } else if (question_type == TYPE_CALCULATION_QUESTION) {
            radioGroup.setVisibility(View.GONE);
            layoutAnswerEdittext.setVisibility(View.VISIBLE);
            etAnswerforEdittext.setHint("解：");
            etAnswerforEdittext.setBackgroundResource(R.drawable.editview_board);
            etAnswerforEdittext.setLines(10);
            String preAns = mQuestion.getYourAnswer();
            if (!TextUtils.isEmpty(preAns)) {
                etAnswerforEdittext.setText(preAns);

            }
            if (preAns == "-1")
                etAnswerforEdittext.setText(null);
            if (activity_mode == MODE_EXAM) {
                layoutAnalysis.setVisibility(View.GONE);
                layoutAnswer.setVisibility(View.GONE);
                btDoneEdittext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String trim = etAnswerforEdittext.getText().toString().trim();
                        mQuestion.setYourAnswer(trim);
                        Toast.makeText(context, "完成~", Toast.LENGTH_SHORT).show();
                        Log.i("", "你填写了answer=" + mQuestion.getYourAnswer());
                    }
                });
            } else if (activity_mode == MODE_CHECK) {
                layoutAnalysis.setVisibility(View.VISIBLE);
                layoutAnswer.setVisibility(View.VISIBLE);
                viewLine.setVisibility(View.GONE);
                tvFillinAnswer.setVisibility(View.GONE);
                btDoneEdittext.setVisibility(View.GONE);
                tvAnalysis.setText(q.getAnalysis());
                tvRightAnswer.setText("正确答案：" + q.getAnswer() + "\n\n");


            }


        } else if (question_type == TYPE_MAPPING_QUESTION) {
            radioGroup.setVisibility(View.GONE);
            layoutAnswer.setVisibility(View.GONE);
            layoutAnswerEdittext.setVisibility(View.GONE);
            if (activity_mode == MODE_EXAM) {
                btStartMapping.setVisibility(View.VISIBLE);
                btStartMapping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "开始画图", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (activity_mode == MODE_CHECK) {
                layoutAnalysis.setVisibility(View.VISIBLE);
                layoutAnswer.setVisibility(View.VISIBLE);
                ivAnswerImg.setVisibility(View.VISIBLE);
                ivAnalysisImg.setVisibility(View.VISIBLE);
                Resources res = context.getResources();
                Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.img_test);

                ivAnswerImg.setImageBitmap(bmp);
                ivAnalysisImg.setImageBitmap(bmp);

            }

        }*/

    }

    private void refreshView() {

    }

    public Question getQuesAndAns(){

        return mQuestion;
    }

    public View getRootView() {
        return convertView;
    }


    public Question getFillinQuestion() {
        return mQuestion;
    }

    private void doneEditText(final EditText et) {

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String string = et.getText().toString().toString();
            }
        });
    }

    private void doneEditTextString(Button button, final EditText editText) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = editText.getText().toString().toString();
                mQuestion.setYourAnswer(string);

            }
        });
    }

    /*if (question_type == TYPE_CHOICE_QUESTION) {
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.clearCheck();
            int rigans = ConvertAnswerInfoUtils.ConvertChoiceItem(q.getAnswer());
            int yourans = ConvertAnswerInfoUtils.ConvertChoiceItem(q.getYourAnswer());
            for (int i = 0; i < 4; i++) {
                Log.i("", "for循环" + i);
                if (yourans != -1) {
                    radioButtons[yourans].setChecked(true);
                    if (activity_mode == MODE_CHECK) {
                        if (yourans == rigans) {
                            radioButtons[rigans].setTextColor(Color.GREEN);
                        } else {
                            radioButtons[rigans].setTextColor(Color.GREEN);
                            radioButtons[yourans].setTextColor(Color.RED);

                        }
                    }
                    break;
                }
            }*/

}
