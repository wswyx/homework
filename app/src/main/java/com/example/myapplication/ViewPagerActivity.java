package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ViewPagerActivity extends AppCompatActivity {


    ViewPager viewPager;
   TabLayout tabLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        //找控件
        viewPager = findViewById(R.id.vp_viewpager);
        tabLayout = findViewById(R.id.tablayout);
        //初始化适配器
        MyPageAdapter myPageAdapter = new MyPageAdapter(getSupportFragmentManager());

        ArrayList<Fragment> datas = new ArrayList<Fragment>();
        datas.add(new ImageFragment());
        datas.add(new ImageTwoFragment());
        datas.add(new ImageThreeFragment());
        myPageAdapter.setData(datas);

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("学生的本分");
        titles.add("每周那个点");
        titles.add("临时有点事");
        myPageAdapter.setTitles(titles);
        viewPager.setAdapter(myPageAdapter);

        tabLayout.setupWithViewPager(viewPager);


    }


/*
* ViewPager + Fragment + TabLayout
* */
    public class MyPageAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> datas;
        ArrayList<String> titles;

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setData(ArrayList<Fragment> datas) {
            this.datas = datas;
        }

        public void setTitles(ArrayList<String> titles) {
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return datas == null ? null : datas.get(position);
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles == null ? null : titles.get(position);
        }
    }

}
