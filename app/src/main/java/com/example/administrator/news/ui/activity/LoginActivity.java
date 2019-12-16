package com.example.administrator.news.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.administrator.news.MainActivity;
import com.example.administrator.news.R;
import com.example.administrator.news.db.DB;
import com.example.administrator.news.net.HttpUtil;
import com.example.administrator.news.net.entity.ResponseLogin;
import com.example.administrator.news.net.exception.ExceptionEngine;
import com.example.administrator.news.util.Const;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="LoginActivity" ;
    private BootstrapEditText mPhoneEditText,mPasswordEditText;
    private BootstrapButton mLoginButton;
    private TextView mRegisterTextView,mForgetPasswordTextView;
    private LinearLayout mLLBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        initView();
        init();
    }

    private void init() {
        mLLBack.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mRegisterTextView.setOnClickListener(this);
        mForgetPasswordTextView.setOnClickListener(this);
    }

    private void initView() {
        mLLBack=(LinearLayout)findViewById(R.id.ll_back);
        mPhoneEditText=(BootstrapEditText)findViewById(R.id.phone_edit_text);
        mPasswordEditText=(BootstrapEditText)findViewById(R.id.password_edit_text);
        mLoginButton=(BootstrapButton)findViewById(R.id.login_button);
        mRegisterTextView=(TextView)findViewById(R.id.register_text_view);
        mForgetPasswordTextView=(TextView)findViewById(R.id.forget_password_text_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                toBack();
                break;
            case R.id.login_button:
                login();
                break;
            case R.id.register_text_view:
                toRegister();
                break;
        }
    }

    private void toBack() {
        this.finish();
    }

    private void toRegister() {
        startActivity(new Intent(this,RegisterActivity.class));
    }

    private void login() {
        final String strPhone=mPhoneEditText.getText().toString().trim();
        String strPassword=mPasswordEditText.getText().toString().trim();
        if(strPhone=="" || strPassword==""){
            Toast.makeText(this,"手机号和密码均不能为空！",Toast.LENGTH_LONG).show();
            return;
        }
        Map<String,String> loginParams=new HashMap<>();
        loginParams.put("phone",strPhone);
        loginParams.put("password",strPassword);
        loginParams.put("action","login");
        HttpUtil.getInstance().login(new Subscriber<ResponseLogin>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }
            @Override
            public void onNext(ResponseLogin responseLogin) {
                DB.getInstance(LoginActivity.this).setData(Const.TOKEN,responseLogin.getToken());
                DB.getInstance(LoginActivity.this).setData(Const.PHONE,responseLogin.getPhone());
                DB.getInstance(LoginActivity.this).setData(Const.NICK_NAME,responseLogin.getNickName());
                DB.getInstance(LoginActivity.this).setData(Const.AVATER,responseLogin.getAvater());

                Intent intent=new Intent();
                intent.setAction("login-success");
                sendBroadcast(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void onError(Throwable t) {
                ExceptionEngine.handleException(LoginActivity.this,t);
            }

            @Override
            public void onComplete() {
            }
        }, loginParams);
    }
}
