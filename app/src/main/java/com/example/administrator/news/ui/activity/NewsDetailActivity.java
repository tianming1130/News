package com.example.administrator.news.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.news.R;
import com.example.administrator.news.app.App;
import com.example.administrator.news.bean.News;
import com.example.administrator.news.db.DB;
import com.example.administrator.news.net.HttpUtil;
import com.example.administrator.news.net.exception.ExceptionEngine;
import com.example.administrator.news.util.Const;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG ="NewsDetailActivity" ;
    private ImageView mIvImage,mIvPraise;
    private TextView tvTitle,tvContent;
    private boolean mIsPraise;
    private String mPhone;
    private int mNewsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initView();
        init();
    }

    private void init() {
        Intent intent=getIntent();
        News news=(News)intent.getSerializableExtra("news");
        Glide.with(this).load(news.getImageUrl()).into(mIvImage);
        tvTitle.setText(news.getTitle());
        tvContent.setText(news.getContent());

        mIvPraise.setOnClickListener(this);

        mPhone=((App)getApplication()).getPhone();
        mNewsId=news.getId();

        Map<String,Object> params=new HashMap<>();
        params.put("phone",mPhone);
        if (mPhone=="未登陆"){
            return;
        }
        params.put("news_id",mNewsId);
        params.put("action","get");

        HttpUtil.getInstance().handlePraise(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG,integer+"");
                if(integer==Const.isPraise){
                    mIsPraise=true;
                }else{
                    mIsPraise=false;
                }
                mIvPraise.setSelected(mIsPraise);
            }
            @Override
            public void onError(Throwable t) {
            }
            @Override
            public void onComplete() {
            }
        },params);
    }

    private void initView() {
        mIvImage=(ImageView)findViewById(R.id.iv_news_image);
        mIvPraise=(ImageView)findViewById(R.id.iv_praise);
        tvTitle=(TextView)findViewById(R.id.tv_title);
        tvContent=(TextView)findViewById(R.id.tv_content);
    }

    @Override
    public void onClick(View v) {
        if(mPhone=="未登陆"){
            Toast.makeText(this,"请先登陆!",Toast.LENGTH_LONG).show();
            return;
        }
        mIsPraise=!mIsPraise;
        Map<String,Object> params=new HashMap<>();
        params.put("phone",mPhone);
        params.put("news_id",mNewsId);
        params.put("action","handle");
        if(mIsPraise){
            params.put("is_praise",0);
        }else{
            params.put("is_praise",1);
        }
        HttpUtil.getInstance().handlePraise(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                if (integer==0){
                    Toast.makeText(NewsDetailActivity.this,"点赞成功!",Toast.LENGTH_LONG).show();
                }else if(integer==1){
                    Toast.makeText(NewsDetailActivity.this,"取消点赞成功",Toast.LENGTH_LONG).show();
                }
                Log.i(TAG,mIsPraise+"");
                mIvPraise.setSelected(mIsPraise);
            }
            @Override
            public void onError(Throwable t) {
                ExceptionEngine.handleException(NewsDetailActivity.this,t);
            }

            @Override
            public void onComplete() {
            }
        },params);
    }
}
