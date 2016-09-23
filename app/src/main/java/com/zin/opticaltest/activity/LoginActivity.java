package com.zin.opticaltest.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zin.opticaltest.R;
import com.zin.opticaltest.entity.LoginResponse;
import com.zin.opticaltest.entity.MyContract;
import com.zin.opticaltest.entity.OpticalTestURL;
import com.zin.opticaltest.entity.User;
import com.zin.opticaltest.utils.LogUtils;
import com.zin.opticaltest.utils.UIUtils;
import com.zin.opticaltest.utils.UserSharePreHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    @Bind(R.id.tv_register)
    TextView tvRegister;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button otherlogin;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1: {
                    HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
                    JSONObject json = new JSONObject(map);
                    LogUtils.i("weibo:"+json.toString());
                    Intent intent = new Intent(LoginActivity.this, WeiboLoginActivity.class);

                    intent.putExtra("userId", json.toString());
                    LogUtils.i(json.toString());
                    startActivity(intent);

                    break;
                }
            }
        }
    };
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initToolbar();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        //登录按钮，点击尝试登录
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        ImageView weibologin = (ImageView) findViewById(R.id.weibologin);
        weibologin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goWeiboLogin();
            }
        });
        tvRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                startActivityForResult(new Intent(LoginActivity.this,RegisterActivity.class), MyContract.INTENT_REQUESTCODE_REG);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK&&requestCode==MyContract.INTENT_REQUESTCODE_REG){  //注册成功

            setResult(MyContract.INTENT_RESULTCODE_REG_OK);
            finish();  //从登录页面进入注册页面，如果注册成功则返回，并结束登录页面
        }
    }

    //我的方法

    private void goWeiboLogin() {
        ShareSDK.initSDK(this);
        Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        authorize(platform);
    }

    //执行授权,获取用户信息
    //文档：http://wiki.mob.com/Android_%E8%8E%B7%E5%8F%96%E7%94%A8%E6%88%B7%E8%B5%84%E6%96%99
    private void authorize(Platform platform) {
        if (platform == null) {
            return;
        }
        if (platform.isValid()) {
            String userId = platform.getDb().getUserId();
            //Toast.makeText(this, userId, 1).show();
            platform.removeAccount();
        }
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                // TODO Auto-generated method stub

            }


            //这个是登录完成后进行的操作
            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Message msg = Message.obtain();
                msg.obj = arg2;
                msg.what = 1;
                handler.sendMessage(msg);

            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                // TODO Auto-generated method stub

            }
        });

        platform.SSOSetting(true);
        platform.showUser(null);
    }


    //我的方法


    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.tb_login);


        mToolbar.setTitle("登 陆");// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));

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


    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        //密码非空但是不合法
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.

        if (TextUtils.isEmpty(email)) {            //如果账号为空
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {        //账号非空但是不合法
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return  email.length()>6;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, LoginResponse> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
           // mPassword = MD5Utils.MD5(password);
            mPassword=password;

        }

        @Override
        protected LoginResponse doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            /*try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }*/
            LoginResponse response=new LoginResponse();
            try {

                response = loginNow(OpticalTestURL.URL_LOGIN, mEmail, mPassword);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO: register the new account here.

            return response;
        }

        @Override
        protected void onPostExecute(final LoginResponse response) {
           mAuthTask = null;
            showProgress(false);

//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
            String status=response.status;
            if (status==null){
                Toast.makeText(LoginActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                mEmailView.requestFocus();
            }
            if (status.equals(LoginResponse.STATUS_SUCCESS)){

                setResult(MyContract.INTENT_RESULTCODE_LOG_OK);
                User user = response.user;
                UserSharePreHelper helper=new UserSharePreHelper(UIUtils.getContext());
                helper.saveUserName(user.getUserName());

                helper.saveUserID(user.getUserId()+"");
                helper.updateLastLoginTime();
                finish();
            }else if (status.equals(LoginResponse.STATUS_ERROR_PASSWORDERROR)){
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }else if (status.equals(LoginResponse.STATUS_ERROR_UNREGISTER)){
                mEmailView.setError("该帐号未注册");
                mEmailView.requestFocus();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private LoginResponse loginNow(String url,String account,String pwd) throws IOException{
        OkHttpClient client=new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()

                .add("username", account)

                .add("password", pwd)


                .build();


        Request request = new Request.Builder()

                .url(url)

                .post(formBody)

                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {

            String json= response.body().string();
            LogUtils.i("登录访问结果"+json);
            Gson gson=new Gson();
            LoginResponse loginResponse = gson.fromJson(json, LoginResponse.class);



            return  loginResponse;

        } else {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setStatus(LoginResponse.STATUS_ISTIEMOUT);
            throw new IOException("Unexpected code " + response);

        }
/*
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtils.i("onFailure"+e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr =  response.body().string();
                Gson gson=new Gson();
                gson.fromJson(htmlStr, LoginResponse.class);

                LogUtils.i("登录访问结果"+htmlStr);
            }
        });*/


    }
}

