package com.liuyongmei.kubo.controller.custom;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.liuyongmei.kubo.MyApplication;
import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.common.ToastUtils;
import com.liuyongmei.kubo.model.AppService;


/**
 * 这个类做的好笨，，，浪费时间，点切换登录，直接跳到登录界面不就ok。。。。
 *
 */
public class LoginAlertDialog extends AlertDialog implements OnClickListener,DialogInterface.OnDismissListener{
	private static final String TAG=LoginAlertDialog.class.getName();

	
	private EditText ipET;
	private EditText passwordET;
	private Button loginBtn,cancelBtn;
	private AsyncTask updateTimeoutViewTask;

	private Handler loginHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 200:
					ToastUtils.longShow(getContext(), "登录成功");
					//关闭登录窗口,等待界面更新
					//销毁当前activity，重启之

					//dismiss();

					break;
				case 500:
					ToastUtils.longShow(getContext(), msg.obj);
					break;
			}
			if(updateTimeoutViewTask!=null){
				if(!updateTimeoutViewTask.isCancelled()){
					updateTimeoutViewTask.cancel(true);
				}
				updateTimeoutViewTask=null;
				//恢复按钮
				loginBtn.setText((String)loginBtn.getTag());
				loginBtn.setEnabled(true);
				cancelBtn.setEnabled(true);
			}

		}
	};


	public LoginAlertDialog(Context context){
		super(context);
		initView(View.inflate(context,R.layout.popup_login,null));
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
		this.setOnDismissListener(this);
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

			loginBtn.setEnabled(false);
			//不允许取消
			cancelBtn.setEnabled(false);

			String ip = ipET.getText().toString();
			String password =passwordET.getText().toString();
			//进行处理
			AppService.getInstance().loginInConnect(ip,password);
			//开始一个线程，显示超时
			showTimeout();
	}
	private void showTimeout(){
		this.updateTimeoutViewTask=new AsyncTask<Integer,Integer,Void>(){

			@Override
			protected void onPreExecute() {
				loginBtn.setTag(loginBtn.getText());
			}

			@Override
			protected Void doInBackground(Integer... params) {
				//获取超时总时间
				int timeout=params[0];
				try {
					while ((!this.isCancelled())&&timeout-->= 0) {
						this.publishProgress(timeout);
						Thread.sleep(1000);
					}
				}catch (Exception e){
					//取消
				}
				return null;
			}



			@Override
			protected void onProgressUpdate(Integer... values) {
				int progress=values[0];
				loginBtn.setText("请稍候("+progress+")");
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				loginBtn.setText("请稍候(0)");
			}
		}.execute(MyApplication.timeout);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		this.loginBtn.setEnabled(true);
		this.cancelBtn.setEnabled(true);
	}
}
