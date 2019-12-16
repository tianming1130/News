package com.example.administrator.news.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.news.R;
import com.example.administrator.news.adapter.NewsAdapter;
import com.example.administrator.news.bean.News;
import com.example.administrator.news.net.HttpUtil;
import com.example.administrator.news.net.entity.ResponseNews;
import com.example.administrator.news.net.exception.ExceptionEngine;
import com.example.administrator.news.ui.activity.NewsDetailActivity;
import com.example.administrator.news.util.Const;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LifeFragment extends Fragment{
    private static final String TAG="LifeFragment";
    private RecyclerView mRecyclerView;
    private List<News> mNews;
    private NewsAdapter mAdapter;
    private View mView;
    private ProgressBar mProgressBar;
    private int mPage=1;
    private int mMaxPage;
    private SmartRefreshLayout mSmartRefreshLayout;
    private Subscription mSubscription;
    public LifeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_life, container, false);
        initView();
        init();
        return mView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mSmartRefreshLayout =(SmartRefreshLayout) mView.findViewById(R.id.smart_refresh_layout);
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
                intent.putExtra("news",mAdapter.getData().get(position));
                startActivity(intent);
            }
        });
        /**
         * 初始化网络请求
         */
        Map<String,String> params=new HashMap<>();
        params.put("column",Const.columns[0]);
        params.put("page_size",Const.PAGE_SIZE+"");
        params.put("page",1+"");
        HttpUtil.getInstance().getNews(new Subscriber<ResponseNews>() {
            @Override
            public void onSubscribe(Subscription s) {
                mSubscription=s;
                mProgressBar.setVisibility(View.VISIBLE);
                s.request(Long.MAX_VALUE);
            }
            @Override
            public void onNext(ResponseNews responseNews) {
                mAdapter.addData(responseNews.getNewsList());
                mMaxPage=responseNews.getMaxPage();
                mPage=1;
            }
            @Override
            public void onError(Throwable t) {
                ExceptionEngine.handleException(getContext(),t);
            }
            @Override
            public void onComplete() {
                mProgressBar.setVisibility(View.GONE);
            }
        },params);
        /**
         * 初始化下拉刷新事件
         */
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            Log.i(TAG,"onRefresh");
            Map<String,String> params=new HashMap<>();
            params.put("column",Const.columns[0]);
            params.put("page_size",Const.PAGE_SIZE+"");
            params.put("page",1+"");
            HttpUtil.getInstance().getNews(new Subscriber<ResponseNews>() {
                @Override
                public void onSubscribe(Subscription s) {
                    mSubscription=s;
                    s.request(Long.MAX_VALUE);
                }
                @Override
                public void onNext(ResponseNews responseNews) {
                    mNews.clear();
                    mNews.addAll(responseNews.getNewsList());
                    mAdapter.notifyDataSetChanged();
                    mMaxPage=responseNews.getMaxPage();
                    mPage=1;
                    refreshLayout.finishRefresh();
                }
                @Override
                public void onError(Throwable t) {
                    ExceptionEngine.handleException(getContext(),t);
                }
                @Override
                public void onComplete() {

                }
            },params);
            }
        });
        /**
         * 初始化上拉加载事件
         */
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
            Log.i(TAG,"onLoadMore");
//                refreshLayout.finishLoadMore(2000);
            mPage++;
            if (mPage<=mMaxPage){
                Map<String,String> params=new HashMap<>();
                params.put("column",Const.columns[0]);
                params.put("page_size",Const.PAGE_SIZE+"");
                params.put("page",mPage+"");
                HttpUtil.getInstance().getNews(new Subscriber<ResponseNews>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        mSubscription=s;
                        s.request(Long.MAX_VALUE);
                    }
                    @Override
                    public void onNext(ResponseNews responseNews) {
                        mAdapter.addData(responseNews.getNewsList());
                        mMaxPage=responseNews.getMaxPage();
                        refreshLayout.finishLoadMore();
                    }
                    @Override
                    public void onError(Throwable t) {
                        ExceptionEngine.handleException(getContext(),t);
                    }
                    @Override
                    public void onComplete() {

                    }
                },params);
            }else {
                Toast.makeText(getActivity(),"没有更多数据了！",Toast.LENGTH_SHORT).show();
                refreshLayout.finishLoadMore(false);
            }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"onDestroyView");
//        mSubscription.cancel();
    }

    private void getDataFromServer(final int action){
        Map<String,String> params=new HashMap<>();
        switch (action){
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
        }

        HttpUtil.getInstance().getNews(new Subscriber<ResponseNews>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(ResponseNews responseNews) {
                switch (action){

                }
            }

            @Override
            public void onError(Throwable t) {
                ExceptionEngine.handleException(getActivity(),t);
            }

            @Override
            public void onComplete() {

            }
        },params);

    }
}
