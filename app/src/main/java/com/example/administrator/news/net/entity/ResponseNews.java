package com.example.administrator.news.net.entity;

import com.example.administrator.news.bean.News;

import java.util.List;

/**
 * Created by Administrator on 2019/11/8 0008.
 */

public class ResponseNews {
    private int maxPage;
    private List<News> newsList;

    public ResponseNews(int maxPage, List<News> newsList) {
        this.maxPage = maxPage;
        this.newsList = newsList;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}
