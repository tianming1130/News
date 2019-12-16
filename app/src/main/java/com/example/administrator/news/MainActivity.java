package com.example.administrator.news;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.administrator.news.ui.fragment.IndexFragment;
import com.example.administrator.news.ui.fragment.MeFragment;
import com.example.administrator.news.ui.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar
        .OnTabSelectedListener {

    private BottomNavigationBar mBottomNavigationBar;
    private List<Fragment> mFragments;
    private String[] mActionBarTitle={"首页","视频","我"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }
    private void initView() {
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.btm_nav_bar);
    }
    private void init() {
        mFragments = new ArrayList<>();
        mFragments.add(new IndexFragment());
        mFragments.add(new VideoFragment());
        mFragments.add(new MeFragment());
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.nav_index_selector, "主页"))
                .addItem(new BottomNavigationItem(R.drawable.nav_video_selector, "视频"))
                .addItem(new BottomNavigationItem(R.drawable.nav_me_selector, "我"))
                .setMode(BottomNavigationBar.MODE_FIXED)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }
    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        setFragment(0);
        getSupportActionBar().setTitle(mActionBarTitle[0]);
    }
    public void setFragment(int position){
        FragmentManager fm = this.getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_container, mFragments.get(position));
        // 事务提交
        transaction.commit();
    }
    @Override
    public void onTabSelected(int position) {
        setFragment(position);
        getSupportActionBar().setTitle(mActionBarTitle[position]);
    }
    @Override
    public void onTabUnselected(int position) {
    }
    @Override
    public void onTabReselected(int position) {
    }
}
