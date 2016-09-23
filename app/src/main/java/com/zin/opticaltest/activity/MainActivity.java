package com.zin.opticaltest.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zin.opticaltest.R;
import com.zin.opticaltest.application.MyApplication;
import com.zin.opticaltest.entity.MyContract;
import com.zin.opticaltest.factory.FragmentFactory;
import com.zin.opticaltest.ui.widget.PagerTab;
import com.zin.opticaltest.utils.LogUtils;
import com.zin.opticaltest.utils.UIUtils;
import com.zin.opticaltest.utils.UserSharePreHelper;

import cn.bmob.v3.Bmob;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends BaseActivity {
    //声明相关变量
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private ArrayAdapter arrayAdapter;
    private PagerTab pagerTab;
    private  ViewPager viewPager;
    private NavigationView mNavigationView;
    private CircleImageView ivProfile;
    private TextView tvProfile;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         /*Bmod*/
        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "16086a1faa32f1af48ce3bd7fe02f2de");
        mContext=this;
        findViews(); //获取控件

        setToolBar();

        setDrawerLayout();
        setupDrawerContent(mNavigationView);
        initViewPager();
    }

    private void setDrawerLayout() {
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        /*//设置菜单列表
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);*/
        //设置NavigationView点击事件

    }

    private void setToolBar() {
        toolbar.setTitle("光学考试");// 标题的文字需在setSupportActionBar之前，不然会无效
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
       /* // toolbar.setSubtitle("副标题");
                setSupportActionBar(mToolbar);
        *//* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 *//*
        // getSupportActionBar().setTitle("标题");
        // getSupportActionBar().setSubtitle("副标题");
        // getSupportActionBar().setLogo(R.drawable.ic_launcher);

        *//* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过Activity的onOptionsItemSelected回调方法来处理 *//*
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "action_settings", 0).show();
                        break;
                    case R.id.action_share:
                        Toast.makeText(MainActivity.this, "action_share", 0).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });*/

    }

    private void initViewPager() {
        //1,获取指针自定义控件和viewPager

        //2,给viewPager准备数据适配器
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(myAdapter);
        //3,viewpager和指针绑定
        pagerTab.setViewPager(viewPager);
        //4,给每一个根据索引生的fragment去维护onCreateView方法中返回的界面效果

        //5,给指针做一个选中界面的事件监听,选中某个界面,就请求某个界面的网络数据
        pagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Fragment baseFragment = FragmentFactory.createFragment(position);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void findViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        pagerTab = (PagerTab) findViewById(R.id.pagerTab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
       // lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);
    }
    //结合Fragment在viewPager中去做展示
    class MyAdapter extends FragmentPagerAdapter {
        private String[] stringArrays;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            stringArrays = UIUtils.getStringArray(R.array.tab_names);
        }
        //根据索引去创建fragment对象,并且返回此fragment
        //返回的fragment中对应的onCreateView方法中返回的view,就等同于PagerAdatper中instantiateItem方法中需要去添加view对象
        //FragmentPagerAdapter会自动将根据索引position生成的fragment中onCreateView中的view添加到viewPager内部,不需要手动添加
        @Override
        public Fragment getItem(int position) {
            //Fragment的工厂类中去统一提供,根据传递进来的索引,生成此索引对应fragment
            return FragmentFactory.createFragment(position);
        }
        //返回viewPager中展示界面总数的方法
        @Override
        public int getCount() {
            //获取指针标题的数组的长度,即为viewPager页面的个数
            return stringArrays.length;
        }

        //给指针自定义控件设置标题方法
        @Override
        public CharSequence getPageTitle(int position) {
            return stringArrays[position];
        }
    }

    //设置NavigationView点击事件
    private void setupDrawerContent(NavigationView navigationView) {
      // View navheader = navigationView.inflateHeaderView(R.layout.navigation_header);
       View navheader = LayoutInflater.from(this).inflate(R.layout.navigation_header,navigationView );

        ivProfile = (CircleImageView) navheader.findViewById(R.id.profile_image);
        tvProfile = (TextView) navheader.findViewById(R.id.profile_text);
        ImageView ivLoginOut= (ImageView) navheader.findViewById(R.id.iv_login_out);
        if (MyApplication.getLoginStatus()) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.headimg__login_default);
            ivProfile.setImageBitmap(bitmap);
            UserSharePreHelper helper=new UserSharePreHelper(UIUtils.getContext());
            String username=helper.getUserName();
            tvProfile.setText(username);
        }
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  startActivity(new Intent(MainActivity.this,LoginActivity.class));
                jumpToPage();

            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(MainActivity.this,LoginActivity.class));
                jumpToPage();


            }
        });
        ivLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showLoginOutDialog();
            }
        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_item_home:
                               // switchToExample();
                                break;
                            case R.id.navigation_item_mine:
                               // switchToBlog();
                                startActivity(new Intent(MainActivity.this,AccountDetailActivity.class));
                                break;
                            case R.id.navigation_item_setting:
                                // switchToBlog();
                                break;
                            case R.id.navigation_item_share:
                               // switchToAbout();
                                showShare();

                                break;


                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (resultCode== MyContract.INTENT_RESULTCODE_LOG_OK&&requestCode== MyContract.INTENT_REQUESTCODE_LOG){ //登录成功
            //设置用户名和头像


        }else if (resultCode== MyContract.INTENT_RESULTCODE_REG_OK&&requestCode==MyContract.INTENT_REQUESTCODE_LOG){

        }*/
        if (resultCode== MyContract.INTENT_RESULTCODE_LOG_OK||resultCode==MyContract.INTENT_RESULTCODE_REG_OK){
            LogUtils.i("ffff");
          //  ImageUtils.setImgForImageView(ivProfile);
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.headimg__login_default);
            ivProfile.setImageBitmap(bitmap);
            UserSharePreHelper helper=new UserSharePreHelper(UIUtils.getContext());
            String username=helper.getUserName();
            tvProfile.setText(username);
            MyApplication.setLoginStatus(true);

        }


    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("一键分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    private void  jumpToPage(){

      if (MyApplication.getLoginStatus()){
          startActivity(new Intent(MainActivity.this,AccountDetailActivity.class));
      }else {
          startActivityForResult(new Intent(MainActivity.this,LoginActivity.class),119);
      }
    }

    private void showLoginOutDialog(){
        final MaterialDialog mMaterialDialog=new MaterialDialog(this);
        mMaterialDialog.setTitle("提示信息")
                .setMessage("你确定退出吗")
                //mMaterialDialog.setBackgroundResource(R.drawable.background);
                .setPositiveButton("是的", new View.OnClickListener() {
                    @Override public void onClick(View v) {

                        UserSharePreHelper helper=new UserSharePreHelper(UIUtils.getContext());
                        helper.cleanLoginStatus();
                        MyApplication.setLoginStatus(false);
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
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

}
