package com.example.administrator.news.net.entity;

/**
 * Created by Administrator on 2019/12/8 0008.
 */

public class ResponseLogin {
    private String phone;
    private String nickName;
    private String avater;
    private String token;

    public ResponseLogin(String phone, String nickName, String avater, String token) {
        this.phone = phone;
        this.nickName = nickName;
        this.avater = avater;
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
