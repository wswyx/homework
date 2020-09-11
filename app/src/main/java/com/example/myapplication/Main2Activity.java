package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.DrawerMenu.SlipperyFragment;
import com.example.myapplication.DrawerMenu.TabFragment.TabFragment1;
import com.example.myapplication.DrawerMenu.TabFragment.TabFragment2;
import com.example.myapplication.DrawerMenu.TabFragment.TabFragment3;
import com.example.myapplication.DrawerMenu.TabFragment.TabFragment4;
import com.example.myapplication.DrawerMenu.TabFragment.TabFragment5;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Main2Activity extends AppCompatActivity {

    private static final String TAG = "尴尬咯";
    private static final String APP_ID = "101900088";

    private Handler mHandler;

    private Toolbar mtoolbar;

    private Button btn_Viewpager;

    protected DrawerLayout mDrawerLayout;
    private FrameLayout contentFrameLayout;
    private Fragment currentFragment;
    private List<Fragment> tabFragments = new ArrayList<>();





    private SwipeRefreshLayout swipeRefresh;
    private TextView textView = null;
    private int currenttext = 0;
    String[] numbers = new String[]{"1号", "2号", "3号", "4号", "5号", "6号"};


    //腾讯
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private Button login;
    private TextView name;
    private ImageView img;



    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        initToolBar();


        //进入viewpager的按钮
        btn_Viewpager = findViewById(R.id.btn_viewpager);
        btn_Viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(Main2Activity.this, ViewPagerActivity.class);
                startActivity(intent);
            }
        });



        //刷新refresh
        textView = findViewById(R.id.text_name);
        textView.setText(numbers[0]);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh () {
                refreshName();
            }
        });


        //drawer
        mDrawerLayout = findViewById(R.id.main2_layout);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawer_layout, Fragment.instantiate(this, SlipperyFragment.class.getName()), SlipperyFragment.class.getSimpleName()).commitAllowingStateLoss();



        tabFragments.add(new TabFragment1());
        tabFragments.add(new TabFragment2());
        tabFragments.add(new TabFragment3());
        tabFragments.add(new TabFragment4());
        tabFragments.add(new TabFragment5());

        currentFragment = tabFragments.get(0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.drawer_layout, currentFragment).commit();












        //传入参数APPID和全局Context上下文
        mTencent = Tencent.createInstance(APP_ID,Main2Activity.this.getApplicationContext());

        login = findViewById(R.id.button_login);
        name = findViewById(R.id.info_name);
        img = findViewById(R.id.info_icon);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIUiListener = new BaseUiListener();
//                //all表示获取所有权限
//                mTencent.login(Main2Activity.this,"all",mIUiListener);
//                mUserInfo = new UserInfo(Main2Activity.this,mTencent.getQQToken()); //获取用户信息
//                mUserInfo.getUserInfo(mIUiListener);
//            }
//        });

        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.button_login:
                        mIUiListener = new BaseUiListener();
                        //all表示获取所有权限
                        mTencent.login(Main2Activity.this, "all", mIUiListener);
                        mUserInfo = new UserInfo(Main2Activity.this, mTencent.getQQToken()); //获取用户信息
                        mUserInfo.getUserInfo(mIUiListener);
                        break;
                }
                    return true;
            }

        });


    }



    //toolbar(topmenu)
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    private void initToolBar () {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用
    }

    //设置Toolbar(返回)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    //刷新当天值日名字
    private void refreshName () {
        new Thread(new Runnable() {
            @Override
            public void run () {
                try {
                    Thread.sleep(500);
                    textView.setText(numbers[++currenttext % numbers.length]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run () {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }



    //QQ登录
    private IUiListener listener;

    /**
     * 自定义监听器实现IUiListener接口后，需要实现的3个方法
     * onComplete完成 onError错误 onCancel取消
     */
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(Main2Activity.this,"授权成功",Toast.LENGTH_SHORT).show();
            Log.e(TAG,"response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken,expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(),qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG,"登录成功"+response.toString());
                        if(response == null){
                            return;
                        }
                        try {
                            JSONObject jo = (JSONObject) response;
                            Toast.makeText(Main2Activity.this,"登录成功",Toast.LENGTH_LONG).show();
                            String nickName = jo.getString("nickname");
                            String figureurl_1= jo.getString("figureurl_1");
                            name.setText(nickName);
                            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(Main2Activity.this).build();
                            ImageLoader.getInstance().init(configuration);
                            ImageLoader.getInstance().displayImage(figureurl_1,img);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG,"登录失败"+uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG,"登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(Main2Activity.this,"授权失败",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(Main2Activity.this,"授权取消",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //腾讯QQ回调
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_LOGIN) {
                Tencent.handleResultData(data, listener);
            }
        }
    }










//    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment current = fragmentManager.findFragmentById(R.id.drawer_layout);
//        if (current != null) {
//            if (current.getClass().equals(fragment.getClass())) {
//                // 如果是同个fragment，则维持原样
//                return;
//            }
//        }
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.drawer_layout, fragment);
//        transaction.addToBackStack(null); // 模拟返回栈
//        transaction.commit();
//    }

    /**
     * 切换主视图的fragment，避免重复实例化加载
     */
    public void switchFragment(int position) {
        Fragment fragment = tabFragments.get(position);
        if (currentFragment != fragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (fragment.isAdded()) {
                transaction.hide(currentFragment)
                        .show(fragment)
                        .commit();
            } else {
                transaction.hide(currentFragment)
                        .add(R.id.drawer_layout, fragment)
                        .commit();
            }
            currentFragment = fragment;
        }
    }



}

