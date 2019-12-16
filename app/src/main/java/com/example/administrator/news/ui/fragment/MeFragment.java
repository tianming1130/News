package com.example.administrator.news.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.news.R;
import com.example.administrator.news.app.App;
import com.example.administrator.news.db.DB;
import com.example.administrator.news.loader.ImagePickerLoader;
import com.example.administrator.news.net.HttpUtil;
import com.example.administrator.news.net.exception.ExceptionEngine;
import com.example.administrator.news.ui.activity.LoginActivity;
import com.example.administrator.news.ui.activity.PraiseNewsActivity;
import com.example.administrator.news.util.Const;
import com.example.administrator.news.util.FileTypeUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    public static final String TAG="MeFragment";
    private static final int IMAGE_PICKER =110 ;
    private View mView;
    private TextView mUserName;
    private ImageView mIvImg;
    private BroadcastReceiver mBroadcastReceiver;
    private LinearLayout mLLPraise;
    private String mPhone="";
    private String mAvater;
    public MeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_me, container, false);
        initView();
        init();
        return mView;
    }
    private void initView() {
        mLLPraise=(LinearLayout)mView.findViewById(R.id.ll_praise);
        mUserName = (TextView) mView.findViewById(R.id.name);
        mIvImg=(ImageView)mView.findViewById(R.id.iv_img);
    }
    private void init() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImagePickerLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setMultiMode(false);
//        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setSelectLimit(1);

        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

        mUserName.setOnClickListener(this);
        mLLPraise.setOnClickListener(this);
        mIvImg.setOnClickListener(this);

        mPhone=DB.getInstance(getActivity()).getData(Const.PHONE);
        mAvater= DB.getInstance(getActivity()).getData(Const.AVATER);
        if (mAvater==""){
            mIvImg.setImageResource(R.mipmap.default_avater);
        }else {
            Glide.with(getActivity()).load(mAvater).into(mIvImg);
        }
        if (mPhone==""){
            mUserName.setText("未登陆");
        }else {
            mUserName.setText(mPhone);
        }
        IntentFilter intentFilter=new IntentFilter("login-success");
        mBroadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String phone=DB.getInstance(getActivity()).getData(Const.PHONE);
                String avaterUrl= DB.getInstance(getActivity()).getData(Const.AVATER);
                mUserName.setText(phone);
                if (avaterUrl.isEmpty()){
                    mIvImg.setImageResource(R.mipmap.default_avater);
                }else{
                    Glide.with(getActivity()).load(avaterUrl).into(mIvImg);
                }
            }
        };
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.ll_praise:
                startActivity(new Intent(getActivity(), PraiseNewsActivity.class));
                break;
            case R.id.iv_img:
                if(mPhone==""){
                    Toast.makeText(getActivity(),"请先登录，然后再上传图像!",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
             ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
             Glide.with(getActivity())
                    .load(Uri.fromFile(new File(images.get(0).path)))
                    .into(mIvImg);
                uploadAvater(images.get(0).path);
            } else {
                Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadAvater(String path) {
        File file=new File(path);
        String name=file.getName();
        String type=name.substring(name.lastIndexOf(".")+1,name.length());
        Log.i(TAG,type);

        //1.创建MultipartBody.Builder对象
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        //2.获取图片，创建请求体
//        RequestBody body=RequestBody.create(MediaType.parse("multipart/form-data"),file_name);//表单类型
        RequestBody body=RequestBody.create(MediaType.parse("image/"+type),file);//表单类型

        //3.调用MultipartBody.Builder的addFormDataPart()方法添加表单数据
        builder.addFormDataPart("avater",file.getName(),body); //添加图片数据，body创建的请求体
        builder.addFormDataPart("phone", mPhone);//传入服务器需要的key，和相应value值
        builder.addFormDataPart("action",Const.USER_ACTION[2]);
        //4.创建List<MultipartBody.Part> 集合，
        //  调用MultipartBody.Builder的build()方法会返回一个新创建的MultipartBody
        //  再调用MultipartBody的parts()方法返回MultipartBody.Part集合
        List<MultipartBody.Part> partList = builder.build().parts();

        HttpUtil.getInstance().uploadAvater(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG,s);
            }

            @Override
            public void onError(Throwable t) {
                ExceptionEngine.handleException(getActivity(),t);
            }

            @Override
            public void onComplete() {

            }
        },partList);

    }
}
