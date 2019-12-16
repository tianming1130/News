package com.example.administrator.news.net.exception;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import org.json.JSONException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by Administrator on 2018\5\21 0021.
 */

public class ExceptionEngine {
    public static final String TAG="ExceptionEngine";

    public static void handleException(Context context, Throwable t) {
        if (t instanceof ServerException) {    //服务器返回的错误
            switch (((ServerException) t).getCode()) {
                case 101:
                    showExceptionMsg(context, "用户名或密码错误！");
                    break;
                case 102:
                    showExceptionMsg(context,"用户已经存在，注册失败");
                    break;
                case 103:
                    showExceptionMsg(context,"旧密码错误，修改密码错误");
                    break;
                case 104:
                    showExceptionMsg(context,"上传照片为空，请重新上传");
                    break;
                case 105:
                    showExceptionMsg(context,"用户不存在，请确认正确登陆后重新上传");
                    break;
                case 120:
                    showExceptionMsg(context,"点赞失败!");
                    break;
                case 121:
                    showExceptionMsg(context,"取消点赞失败!");
                case 210:
                    showExceptionMsg(context,"未知错误！");
                    break;
            }
        } else if (t instanceof JsonParseException //均视为解析错误
                || t instanceof JSONException
                || t instanceof ParseException) {
            showExceptionMsg(context, "数据解析失败");
        } else if (t instanceof ConnectException ||t instanceof SocketTimeoutException) { //均视为网络错误
            showExceptionMsg(context, "连接失败");
        } else {  //未知错误
            showExceptionMsg(context, "未知错误:"+t.getMessage());
        }
        Log.i(TAG,t.getMessage());
    }

    private static void showExceptionMsg(Context context, String msg) {
        if(context!=null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
