package com.example.administrator.news.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.news.R;
import com.example.administrator.news.adapter.NewsAdapter;
import com.example.administrator.news.bean.News;
import com.example.administrator.news.net.HttpUtil;
import com.example.administrator.news.net.exception.ExceptionEngine;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PraiseNewsActivity extends AppCompatActivity {
    private static final String TAG="PraiseNewsActivity";
    private RecyclerView mRecyclerView;
    private List<News> mNews;
    private NewsAdapter mNewsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise_news);
        initView();
        init();
    }

    private void initView() {
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler_view);
    }
    private void init(){
        mNews=new ArrayList<>();
        mNewsAdapter=new NewsAdapter(R.layout.item_view_news,mNews);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mNewsAdapter);

        Map<String,Object> params=new HashMap<>();

        HttpUtil.getInstance().getPraiseByUser(new Subscriber<List<News>>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(List<News> newses) {
                mNewsAdapter.addData(newses);
            }

            @Override
            public void onError(Throwable t) {
                ExceptionEngine.handleException(PraiseNewsActivity.this,t);
            }

            @Override
            public void onComplete() {

            }
        },params);
    }
}
