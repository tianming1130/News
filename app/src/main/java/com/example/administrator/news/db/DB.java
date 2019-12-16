package com.example.administrator.news.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.administrator.news.util.Const;

/**
 * Created by Administrator on 2019/11/4 0004.
 */

public class DB {
    private static DB db;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private DB(Context context){
        sp=context.getSharedPreferences(Const.USER_LOGIN_FILE_NAME,Context.MODE_PRIVATE);
        editor=sp.edit();
    }
    public static DB getInstance(Context context){
        synchronized (DB.class){
            if(db==null){
                db=new DB(context);
            }
            return db;
        }
    }
    public void  setData(String key,String data){
        editor.putString(key,data);
        editor.commit();
    }
    public String getData(String key){
        return sp.getString(key,"");
    }
}
