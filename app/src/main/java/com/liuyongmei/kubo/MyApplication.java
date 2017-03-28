
package com.liuyongmei.kubo;

import android.app.Application;

import com.liuyongmei.kubo.model.DataReader;
import com.liuyongmei.kubo.model.DataSender;
import com.liuyongmei.kubo.model.KeepAliveStrategy;

public class MyApplication extends Application {
	//默认连接端口
	public static final int PORT = 1682;
	//默认超时时间s
	public static final int timeout=10;
	//退出标识
	public static final String EXIT="exit";

	@Override
	public void onCreate() {
		super.onCreate();

	}

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
