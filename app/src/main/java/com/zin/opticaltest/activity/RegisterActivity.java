package com.zin.opticaltest.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zin.opticaltest.R;
import com.zin.opticaltest.application.MyApplication;
import com.zin.opticaltest.dao.UserDao;
import com.zin.opticaltest.entity.MyResponse;
import com.zin.opticaltest.entity.OpticalTestURL;
import com.zin.opticaltest.entity.User;
import com.zin.opticaltest.utils.LogUtils;
import com.zin.opticaltest.utils.MD5Utils;
import com.zin.opticaltest.utils.UIUtils;
import com.zin.opticaltest.utils.UserSharePreHelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.tl_regisger_act)
    Toolbar mToolbar;
    @Bind(R.id.et_reg_username)
    EditText etRegUsername;
    @Bind(R.id.et_reg_pwd)
    EditText etRegPwd;
    @Bind(R.id.et_reg_pwd_confirm)
    EditText etRegPwdConfirm;
    @Bind(R.id.et_reg_realname)
    EditText etRegRealname;
    @Bind(R.id.et_reg_class)
    EditText etRegClass;
    @Bind(R.id.et_reg_studentId)
    EditText etRegStudentId;
    @Bind(R.id.bt_register)
    Button btRegister;
    private String strRegUsername;
    private String strRegPwd;
    private String strRegPwdConfirm;
    private String strRegRealname;
    private String strRegClass;
    private String strRegStudentId;
    private Context mContext;
    private MediaType JSON;
    private OkHttpClient client;
    private String strPwsMD5;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    MyResponse obj = (MyResponse) msg.obj;
                    String status=obj.status;
                    String info=obj.info;
                    User user=obj.user;
                    if(status.equals(MyResponse.STATUS_SUCCESS)){
                        //注册成功，
                        Toast.makeText(RegisterActivity.this,info,Toast.LENGTH_SHORT).show();
                        saveUserInfoToLoacal();
                        UserSharePreHelper helper=new UserSharePreHelper(UIUtils.getContext());
                        helper.saveUserName(user.getUserName());
                        helper.saveUserName(user.getUserId()+"");

                        helper.updateLastLoginTime();
                        MyApplication.setLoginStatus(true);
                       /* Intent intent=new Intent();
                        intent.putExtra("userName",strRegUsername);
                        intent.putExtra("pwd",strRegPwd);
                        setResult(100,intent);*/
                        setResult(RESULT_OK);
                        finish();
                    }else  {
                        Toast.makeText(RegisterActivity.this,info,Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 2:
                    Toast.makeText(RegisterActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext=this;
        initToolbar();

        //获取用户输入

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();

            }
        });
    }

    private void attemptRegister() {
       /* if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);*/

        // Store values at the time of the login attempt.
       /* String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();*/

        //获取用户输入
        getRegUserInfo();

        boolean cancel = false;

        View focusView = null;

        // Check for a valid password, if the user entered one.
        //检验密码
        //密码非空但是不合法

        if (TextUtils.isEmpty(strRegPwd)) {            //如果账号为空
            etRegPwd.setError("密码不能为空");
            focusView = etRegPwd;
            cancel = true;
        } else if (!isUserNameValid(strRegPwd)) {        //账号非空但是不合法
            etRegPwd.setError("密码不能少于6位有效字符");
            focusView = etRegPwd;
            cancel = true;
        }
      /*  if (!TextUtils.isEmpty(strRegPwd) && !isPasswordValid(strRegPwd)) {
            etRegPwd.setError(getString(R.string.error_invalid_password));//密码不能少于6位
            focusView = etRegPwd;
            cancel = true;
        }*/

        if (TextUtils.isEmpty(strRegPwdConfirm) ) {
            etRegPwdConfirm.setError("确认密码不能为空");//密码不能少于6位
            focusView = etRegPwdConfirm;
            cancel = true;
        }


        if (!strRegPwd.equals(strRegPwdConfirm)) {
            etRegPwdConfirm.setError("两次输入的密码不一致");//密码不能少于6位
            focusView = etRegPwdConfirm;
            cancel = true;
        }

        if (!strRegPwd.matches("^[a-zA-Z]\\w{5,17}$")){
            etRegPwd.setError("以字母开头，长度在6~18之间，只能包含字符、数字和下划线");//密码不能少于6位
            focusView = etRegPwd;
            cancel = true;
        }
        // Check for a valid email address.
        //检验账号
        if (TextUtils.isEmpty(strRegUsername)) {            //如果账号为空
           etRegUsername.setError(getString(R.string.error_field_required));
            focusView = etRegUsername;
            cancel = true;
        } else if (!isUserNameValid(strRegUsername)) {        //账号非空但是不合法
            etRegUsername.setError("帐号不能少于4位有效字符");
            focusView = etRegUsername;
            cancel = true;
        }
        if (!strRegUsername.matches("[A-Za-z0-9_\\-\\u4e00-\\u9fa5]+")){
            etRegUsername.setError("帐号不能包含特殊字符");
            focusView = etRegUsername;
            cancel = true;
        }

        if (TextUtils.isEmpty(strRegClass)) {
            etRegClass.setError("班级不能为空！");//密码不能少于6位
            focusView = etRegClass;
            cancel = true;
        }

        if (TextUtils.isEmpty(strRegStudentId)) {
            etRegStudentId.setError("学号不能为空！");//密码不能少于6位
            focusView = etRegStudentId;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
           /* showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);*/
            try {
                strPwsMD5 = MD5Utils.MD5(strRegPwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
            postUserRegInfo();
        }
    }


    private boolean isUserNameValid(String email)  {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        LogUtils.i("length"+email.length());
        //email.length()>4;//这样得到的是字符的长度，中文一个汉字和一个字母都是1
       // email.getBytes("utf-8").length;//字节长度
        int length = 0;
        try {
            length = email.getBytes("utf-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  length>=4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic

        //^[a-zA-Z]\w{5,17}$
        //以字母开头，长度在6~18之间，只能包含字符、数字和下划线
        return password.length() > 6;
    }
    private void postUserRegInfo()   {
      //  String url="http://192.168.56.1:8080/otweb/student/register.action";
        String url= OpticalTestURL.URL_REGISTER;
        try {
            post(url);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.i("访问出错");
        }


    }

    private void post(String url) throws IOException {
        client = new OkHttpClient();

        RequestBody formBody = new FormEncodingBuilder()
                .add("username", strRegUsername)
                .add("password", strPwsMD5)
                .add("realname", strRegRealname)
                .add("grade", strRegClass)
                .add("stdid", strRegStudentId)
                .build();
        final Request request = new Request.Builder()

                .url(url)

                .post(formBody)

                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                    LogUtils.i("onFailure"+e.toString());
                Message message=handler.obtainMessage();
                message.what=2;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr =  response.body().string();

                Gson gson=new Gson();
                MyResponse result = gson.fromJson(htmlStr, MyResponse.class);
                LogUtils.i("注册返回结果"+htmlStr);
                Message message=handler.obtainMessage();
                message.what=1;
                message.obj=result;
                handler.sendMessage(message);
            }
        });


    }




    public void initToolbar() {



        mToolbar.setTitle("注 册");// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        //  mToolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
        /* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    private void getRegUserInfo(){

        strRegUsername = getEditTextString(etRegUsername);
        strRegPwd = getEditTextString(etRegPwd);
        strRegPwdConfirm = getEditTextString(etRegPwdConfirm);
        strRegRealname = getEditTextString(etRegRealname);
        strRegClass = getEditTextString(etRegClass);
        strRegStudentId = getEditTextString(etRegStudentId);




    }
    private void   saveUserInfoToLoacal(){
        UserDao userDao =new UserDao(this);
        User user=new User();
            user.setUserName(strRegUsername);
        user.setPassWord(strRegPwd);
        user.setRealName(strRegRealname==null?"":strRegRealname);
        user.setGrade(strRegClass);
        user.setStudentId(strRegStudentId);
        try {
            userDao.add(user);
            LogUtils.i("用户信息保存到数据成功");
        }catch (Exception e){
            LogUtils.i("用户信息保存到数据失败");
        }

    }

    private String getEditTextString(EditText editText){
       return editText.getText().toString().trim();
    }
}
