package com.example.administrator.news.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.news.R;
import com.example.administrator.news.adapter.NewsAdapter;
import com.example.administrator.news.bean.News;
import com.example.administrator.news.net.HttpUtil;
import com.example.administrator.news.net.entity.ResponseNews;
import com.example.administrator.news.net.exception.ExceptionEngine;
import com.example.administrator.news.ui.activity.NewsDetailActivity;
import com.example.administrator.news.util.Const;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntertainmenFragment extends Fragment {
    private static final String TAG="EntertainmenFragment";
    private RecyclerView mRecyclerView;
    private List<News> mNews;
    private NewsAdapter mAdapter;
    private View mView;
    private ProgressBar mProgressBar;
    public EntertainmenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_entertainmen, container, false);
        initView();
        init();
        return  mView;
    }
    private void initView() {
        mRecyclerView=(RecyclerView)mView.findViewById(R.id.recycler_view);
        mProgressBar=(ProgressBar)mView.findViewById(R.id.progress_bar);
    }
    private void init(){
        mNews=new ArrayList<>();
        mAdapter=new NewsAdapter(R.layout.item_view_news,mNews);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(getContext(), NewsDetailActivity.class);
            }
        });

        Map<String,String> params=new HashMap<>();
        params.put("column",Const.columns[2]);
        HttpUtil.getInstance().getNews(new Subscriber<ResponseNews>() {
            @Override
            public void onSubscribe(Subscription s) {
                mProgressBar.setVisibility(View.VISIBLE);
                s.request(Long.MAX_VALUE);
            }
            @Override
            public void onNext(ResponseNews responseNews) {
                mAdapter.addData(responseNews.getNewsList());
            }
            @Override
            public void onError(Throwable t) {
                ExceptionEngine.handleException(getContext(),t);
            }
            @Override
            public void onComplete() {
                mProgressBar.setVisibility(View.GONE);
            }
        }, params);
    }
}
