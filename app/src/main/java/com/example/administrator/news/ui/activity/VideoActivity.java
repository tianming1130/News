package com.example.administrator.news.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.news.R;
import com.example.administrator.news.bean.Video;

import cn.jzvd.JZVideoPlayerStandard;

public class VideoActivity extends AppCompatActivity {
    private JZVideoPlayerStandard mJZVideoPlayerStandard;
    private TextView mTvVideoProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        init();
    }

    private void initView() {
        mJZVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.player_video);
        mTvVideoProfile=(TextView)findViewById(R.id.tv_video_profile);
    }
    private void init(){
        Intent intent =getIntent();
        Video video=(Video) intent.getSerializableExtra("video");
        mJZVideoPlayerStandard.setUp(video.getVideoUrl(),
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, video.getTitle());
        Glide.with(this).load(video.getVideoThumb()).into(mJZVideoPlayerStandard.thumbImageView);
        mTvVideoProfile.setText(video.getProfile());
    }
    @Override
    public void onBackPressed() {
        if (mJZVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mJZVideoPlayerStandard.releaseAllVideos();
    }
}
