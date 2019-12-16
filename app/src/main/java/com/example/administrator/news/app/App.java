package com.example.administrator.news.app;

import android.app.Application;

import com.example.administrator.news.db.DB;
import com.example.administrator.news.loader.ImagePickerLoader;
import com.example.administrator.news.util.Const;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;

/**
 * Created by Administrator on 2019/10/29 0029.
 */

public class App extends Application {
    private DB mDb;
    @Override
    public void onCreate() {
        super.onCreate();
//        TypefaceProvider.registerDefaultIconSets();
        mDb=DB.getInstance(getApplicationContext());
    }
    public String getPhone(){
        String phone=mDb.getData(Const.PHONE);
        if (phone==""){
            return "未登陆";
        }else{
            return phone;
        }
    }
    public String getAvater(){
        return mDb.getData(Const.AVATER);
    }
}
