package com.example.administrator.news.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.news.R;
import com.example.administrator.news.bean.Video;

import java.util.List;

/**
 * Created by Administrator on 2018\4\21 0021.
 */

public class VideoAdapter extends BaseQuickAdapter<Video,BaseViewHolder> {
    private static final String TAG="VideoAdapter";
    public VideoAdapter(@LayoutRes int layoutResId, @Nullable List<Video> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Video item) {
        //helper.setImageBitmap(R.id.video_thumb,getVideoThumb(item.getUrl()));
        helper.setText(R.id.tv_video_title,item.getTitle());
        Glide.with(mContext).load(item.getVideoThumb()).into((ImageView) helper.getView(R.id.video_thumb));
    }
}
