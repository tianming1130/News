package com.example.administrator.news.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.news.R;
import com.example.administrator.news.adapter.VideoAdapter;
import com.example.administrator.news.bean.Video;
import com.example.administrator.news.loader.BannerImageLoader;
import com.example.administrator.news.net.HttpUtil;
import com.example.administrator.news.net.exception.ExceptionEngine;
import com.example.administrator.news.ui.activity.NewsDetailActivity;
import com.example.administrator.news.ui.activity.VideoActivity;
import com.example.administrator.news.util.Const;
import com.youth.banner.Banner;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {
    private View mView;
    private RecyclerView mRecyclerView;
    private List<Video> mData;
    private VideoAdapter mAdapter;
    private ProgressBar mProgressBar;
    private Banner mBanner;
    private List<String> mBannerImages;
    public VideoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_video, container, false);
        initView();
        init();
        return mView;
    }

    private void initView() {
//        mProgressBar=(ProgressBar)mView.findViewById(R.id.progress_bar);
        mRecyclerView=(RecyclerView)mView.findViewById(R.id.recycler_view);
        mBanner=(Banner)mView.findViewById(R.id.banner);
    }

    private void init() {
        mBannerImages=new ArrayList<>();
        mBannerImages.add("http://10.0.2.2/news/images/banner/banner1.jpg");
        mBannerImages.add("http://10.0.2.2/news/images/banner/banner2.jpg");
        mBannerImages.add("http://10.0.2.2/news/images/banner/banner3.jpg");
        mBanner.setImageLoader(new BannerImageLoader());
        mBanner.setImages(mBannerImages);
        mBanner.start();



        mData=new ArrayList<>();
        mAdapter=new VideoAdapter(R.layout.item_view_video,mData);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(getContext(), VideoActivity.class);
                intent.putExtra("video",mAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        Map<String,String> params=new HashMap<>();

        HttpUtil.getInstance().getVideo(new Subscriber<List<Video>>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(List<Video> videos) {
                mAdapter.addData(videos);
            }

            @Override
            public void onError(Throwable t) {
                ExceptionEngine.handleException(getContext(),t);
            }

            @Override
            public void onComplete() {
//                mProgressBar.setVisibility(View.GONE);
            }
        },params);
    }

}
