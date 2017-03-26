package com.liuyongmei.kubo.controller.custom;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.common.AssertUtils;
import com.liuyongmei.kubo.common.ThisAppException;
import com.liuyongmei.kubo.common.ToastUtils;


/**
 * the login popup window
 */
public class LoginAlertDialog extends AlertDialog implements OnClickListener{
	private static final String TAG=LoginAlertDialog.class.getName();

	
	private EditText ipET;
	private EditText passwordET;
	private Button loginBtn,cancelBtn;


	public LoginAlertDialog(Context context){
		super(context);
		initView(View.inflate(context,R.layout.popup_login,null));
	}
	
	
	
	private void initView(View view){
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		this.setTitle("登录");
		this.setView(view);


		ipET = (EditText) view.findViewById(R.id.ip_address);
		passwordET = (EditText) view.findViewById(R.id.password);
		loginBtn= (Button) view.findViewById(R.id.login);
		cancelBtn= (Button) view.findViewById(R.id.cancle);
        loginBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

	}
	


	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.login:
				handleLoginAction();
				break;
			case R.id.cancle:
				this.dismiss();
				break;

		}
	}
	private void handleLoginAction(){

		try {
			String ip = AssertUtils.notBlank("请输入IP地址", ipET.getText().toString());
			String password = AssertUtils.notBlank("请输入密码", passwordET.getText().toString());
			//进行处理
			//ConnectionServices.getInstance().login(ip,password);
			return;
		}catch (ThisAppException e){
			ToastUtils.longShow(this.getContext(), e.getMessage());
		}catch (Exception e) {
			Log.e(TAG,"登录异常",e);
			ToastUtils.longShow(this.getContext(), "网络繁忙，请稍候再试");
		}

	}
}
