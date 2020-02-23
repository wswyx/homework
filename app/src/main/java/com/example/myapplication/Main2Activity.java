package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;



public class Main2Activity extends AppCompatActivity {
    private Handler mHandler;

    private Toolbar toolbar;

    private Button btn_Viewpager;

    private SwipeRefreshLayout swipeRefresh;
    private TextView textView=null;
    private int currenttext=0;
    String[] name=new String[]{"1号","2号","3号","4号","5号","6号"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initToolBar();


        //进入viewpager的按钮
        btn_Viewpager = findViewById(R.id.btn_viewpager);
        btn_Viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, ViewPagerActivity.class);
                startActivity(intent);
            }
        });


        //天气按钮
        ImageButton whether = findViewById(R.id.whether);
        whether.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://m.tianqi.com/jiancaopingqu/"));
                startActivity(intent);
            }
        });

        //refresh
        textView=findViewById(R.id.text_name);
        textView.setText(name[0]);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshName();
            }
        });
    }





    //设置Toolbar(返回)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "backup", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolBar() {
        //找资源文件
        Drawable back_img = getResources().getDrawable(R.drawable.back);
        //设置导航图标
        toolbar.setNavigationIcon(back_img);
        //设置导航图标的点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //4、关闭本页面
                finish();
            }
        });
    }


    //刷新当天值日名字
    private void refreshName(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(500);
                    textView.setText(name[++currenttext%name.length]);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }


}





