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

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.common.ToastUtils;
import com.liuyongmei.kubo.model.AppService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                case 200:
                    ToastUtils.longShow(LoginActivity.this, "登录成功");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    //获取谱图数据
                    break;
                case 500:
                    ToastUtils.longShow(LoginActivity.this, msg.obj);
            }
            showProgress(false);
        }
    };
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
        signInBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
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
            AppService.getInstance().connect(ip, password, loginHandler);
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


}

