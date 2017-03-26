
package com.liuyongmei.kubo;

import android.app.Application;

import com.liuyongmei.kubo.model.DataReader;
import com.liuyongmei.kubo.model.DataSender;
import com.liuyongmei.kubo.model.KeepAliveStrategy;

public class MyApplication extends Application {

	public static final int PORT = 1682;


	@Override
	public void onCreate() {
		super.onCreate();

	}

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
