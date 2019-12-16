package com.example.administrator.news.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.news.R;

import java.util.ArrayList;
import java.util.List;
public class IndexFragment extends Fragment {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;
    private View mView;
    private String[] mTitle={"生活","科技","娱乐","体育","社会"};
    public IndexFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_index, container, false);
        initView();
        init();
        return mView;
    }
    private void init() {
        mFragments=new ArrayList<>();
        mFragments.add(new LifeFragment());
        mFragments.add(new TechnologyFragment());
        mFragments.add(new EntertainmenFragment());
        mFragments.add(new SportFragment());
        mFragments.add(new SocietyFragment());
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }
    private void initView() {
        mViewPager=(ViewPager)mView.findViewById(R.id.view_pager);
        mTabLayout=(TabLayout)mView.findViewById(R.id.tab_layout);
    }
    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position];
        }
    }
}
