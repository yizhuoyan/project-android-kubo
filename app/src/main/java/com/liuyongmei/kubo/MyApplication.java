
package com.liuyongmei.kubo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.liuyongmei.kubo.controller.activity.ErrorHandlerActivity;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.DataReader;
import com.liuyongmei.kubo.model.DataSender;
import com.liuyongmei.kubo.model.KeepAliveStrategy;
import com.liuyongmei.kubo.model.SyncMessageBroadcastor;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

public class MyApplication extends Application implements SyncMessageListener {
	//默认连接端口
	public static final int PORT = 1682;
	//默认超时时间s
	public static final int timeout=10;
	//退出标识
	public static final String EXIT="exit";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("test","application.oncreate");
	}


	/**
	 * 用于整个应用被关闭到后台
	 */
	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d("test","application.onTerminate");
		//清空所有资源
		AppService.getInstance().destroy();
	}

	/**
     * 未知异常，统一处理
     * @param m
     */
    @Override
    public void onReceive(SyncMessage m) {
        //清空网络资源
        AppService.getInstance().destroy();
        Intent intent=new Intent();
        intent.setClass(this, ErrorHandlerActivity.class);
        intent.putExtra(ErrorHandlerActivity.ERROR_MESSAGE,m.message);
        startActivity(intent);
    }
}
