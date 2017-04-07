package com.liuyongmei.kubo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.liuyongmei.kubo.MyApplication;
import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.common.ToastUtils;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener,SyncMessageListener {

    private EditText ipET;
    private EditText passwordET;
    private Button loginBtn,cancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ipET = (EditText) findViewById(R.id.login_ip);
        passwordET = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.login_cancel_btn);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //关注登录相关消息
        AppService.getInstance().addReceiveDataListener(SyncMessage.LOGIN_SUCCEED,this);
        AppService.getInstance().addReceiveDataListener(SyncMessage.LOGIN_FAILED,this);
        AppService.getInstance().addReceiveDataListener(SyncMessage.LOGINING,this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //删除监听登录相关消息
        AppService.getInstance().removeReceiveDataListener(SyncMessage.LOGIN_SUCCEED,this);
        AppService.getInstance().removeReceiveDataListener(SyncMessage.LOGIN_FAILED,this);
        AppService.getInstance().removeReceiveDataListener(SyncMessage.LOGINING,this);
    }


    private void handlerLogin() {

        // Reset errors.
        ipET.setError(null);
        passwordET.setError(null);

        String ip = ipET.getText().toString();
        String password = passwordET.getText().toString();

        View focusView = null;
        do {
            if (TextUtils.isEmpty(password)) {
                passwordET.setError(getString(R.string.error_field_required));
                focusView = passwordET;
                break;
            }

            if (TextUtils.isEmpty(ip)) {
                ipET.setError(getString(R.string.error_field_required));
                focusView = ipET;
            }
            if (!isIpValid(ip)) {
                ipET.setError(getString(R.string.error_incorrect_ip));
                focusView = ipET;
            }
        }while(false);

        if (focusView!=null) {
            focusView.requestFocus();
        } else {
            loginBtn.setEnabled(false);
            loginBtn.setTag(loginBtn.getText());
            loginBtn.setText("登录中，请稍候");
            AppService.getInstance().loginInConnect(ip, password);
        }
    }
    public boolean isIpValid(String ip){
        try {
            InetAddress.getByName(ip);
            return true;
        }catch (Exception e){
            return false;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                handlerLogin();
                break;
            case R.id.login_cancel_btn:
                Intent intent=this.getIntent();
                boolean switchLogin=intent.getBooleanExtra(MainActivity.SWITCH_LOGIN_TAG,false);
                //如果是切换登录
                if(switchLogin){
                    //如果之前登录成功过，则返回之前界面
                    if(AppService.getInstance().isLogin()){
                        //关掉当前，回到之前
                        this.finish();
                    }else{
                        //其他界面跳转，且已登录失败，则关闭整个应用
                        Intent exitIntent=new Intent(this,MainActivity.class);
                        exitIntent.putExtra(MyApplication.EXIT,true);
                        startActivity(exitIntent);
                        this.finish();

                    }
                }else {
                    //第一次登录，关掉应用
                    this.finish();
                    //进入程序统一出口
                    this.getApplication().onTerminate();
                }
                break;

        }
    }

    @Override
    public void onReceive(final SyncMessage message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                switch (message.code) {
                    case SyncMessage.LOGINING://登录进度
                        //暂时忽略进度
                       // ToastUtils.shortShow(LoginActivity.this, message.message);
                        break;
                    case SyncMessage.LOGIN_SUCCEED:
                        ToastUtils.longShow(LoginActivity.this, "登录成功");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        //防止返回，销毁当前activity
                        finish();
                        break;
                    case SyncMessage.LOGIN_FAILED:
                        ToastUtils.longShow(LoginActivity.this, message.message);
                        loginBtn.setEnabled(true);
                        loginBtn.setText(loginBtn.getTag()+"");
                        break;
                }


            }
        });

    }
}

