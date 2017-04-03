package com.liuyongmei.kubo.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

/**
 * Created by Administrator on 2017/4/2 0002.
 * 用于处理程序中出现的各种异常错误
 */

public class ErrorHandlerActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String ERROR_MESSAGE="err_msg";

    private TextView messageView;
    private Button handleBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errorhandler);
        messageView=(TextView)findViewById(R.id.error_message);
        handleBtn= (Button) findViewById(R.id.error_handle_btn);
        handleBtn.setOnClickListener(this);

        init();
    }
    private void init(){
        Intent intent=this.getIntent();
        String message=intent.getStringExtra(ERROR_MESSAGE);
        message=message==null?getDefaultMessage():message;
        messageView.setText(message);


    }

    private  String getDefaultMessage(){
        return this.getResources().getString(R.string.default_error_message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.error_handle_btn:
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }
}
