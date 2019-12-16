package com.example.administrator.news.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\4\21 0021.
 */

public class Video implements Serializable{
    private int id;
    private String title;
    private String profile;
    private String videoUrl;
    private String videoThumb;

    public Video(int id, String title, String profile, String videoUrl, String videoThumb) {
        this.id = id;
        this.title = title;
        this.profile = profile;
        this.videoUrl = videoUrl;
        this.videoThumb = videoThumb;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getProfile() {
        return profile;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoThumb() {
        return videoThumb;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setVideoThumb(String videoThumb) {
        this.videoThumb = videoThumb;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", profile='" + profile + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", videoThumb='" + videoThumb + '\'' +
                '}';
    }
}
