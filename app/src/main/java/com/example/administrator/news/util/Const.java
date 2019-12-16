package com.example.administrator.news.util;

/**
 * Created by Administrator on 2018\5\9 0009.
 */

public class Const {
    public static final String SHARED_PREFERENCE_NAME = "APP";

    public static final String USER_LOGIN_FILE_NAME="LOGIN";

    public static final int PAGE_SIZE=5;

    public static final String BASE_URL="http://10.0.2.2/news/api/";

    public static  final String TOKEN="token";
    public static final String PHONE="phone";
    public static final String NICK_NAME="nick_name";
    public static final String AVATER="avater";

    public static final int isPraise=0;
    public static  final String[] columns={
            "生活","科技","娱乐","体育","社会"
    };
    public static final String[] BANNER_IMAGE_URL={
      BASE_URL+"image/banner/news_banner1.jpg",
      BASE_URL+"image/banner/news_banner2.jpg",
      BASE_URL+"image/banner/news_banner3.jpg",
    };
    public static final String[] USER_ACTION={
            "login",
            "register",
            "upload_avater",
    };

}
