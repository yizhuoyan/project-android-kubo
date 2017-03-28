package com.liuyongmei.kubo.controller.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.liuyongmei.kubo.MyApplication;
import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.common.ToastUtils;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener,SyncMessageListener {

    // UI references.
    private AutoCompleteTextView ipView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        ipView = (AutoCompleteTextView) findViewById(R.id.ip_tv);
        mPasswordView = (EditText) findViewById(R.id.password_et);
        signInBtn = (Button) findViewById(R.id.sign_in_button);
        signInBtn.setOnClickListener(this);
        findViewById(R.id.sign_cancel_button).setOnClickListener(this);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        //关注登录相关消息
        AppService.getInstance().addReceiveDataListener(SyncMessage.LOGIN_SUCCEED,this);
        AppService.getInstance().addReceiveDataListener(SyncMessage.LOGIN_FAILED,this);
    }


    private void attemptLogin() {
        // Reset errors.
        ipView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String ip = ipView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid ip address.
        if (TextUtils.isEmpty(ip)) {
            ipView.setError(getString(R.string.error_field_required));
            focusView = ipView;
            cancel = true;
        } else if (!isIpValid(ip)) {
            ipView.setError(getString(R.string.error_incorrect_ip));
            focusView = ipView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            AppService.getInstance().connect(ip, password);
        }
    }

    private boolean isIpValid(String ip) {
        //TODO: Replace this with your own logic
        if (ip.length() > 15 || ip.length() < 7) {
            return false;
        }

        return ip.contains(".");
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button:
                attemptLogin();
                break;
            case R.id.sign_cancel_button:
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
                    case SyncMessage.LOGIN_SUCCEED:
                        ToastUtils.longShow(LoginActivity.this, "登录成功");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        //防止返回，销毁当前activity
                        finish();
                        break;
                    case SyncMessage.LOGIN_FAILED:
                        ToastUtils.longShow(LoginActivity.this, message.message);
                }

                showProgress(false);
            }
        });

    }
}

