
package com.liuyongmei.kubo.model;


import android.util.Log;

import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.DataReaderInputStream;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import java.io.InputStream;


public class DataReader implements Runnable {

    private static final String TAG = DataReader.class.getName();

    //接收线程
    private volatile Thread readThread;

    private DataReaderInputStream in;

    public DataReader() {

    }

    public void start(InputStream dis) {
        this.in = new DataReaderInputStream(dis);
        readThread = new Thread(this);
        this.readThread.start();
    }

    public void run() {
        if (Thread.currentThread() == readThread) {

            SyncMessageBroadcastor broadcastor=SyncMessageBroadcastor.getInstance();
            KuboData data = null;
                try {
                    //如果一开始就读到数据，表示登录成功,扔掉谱图数据
                    int i=1;
                    do {
                        data = KuboData.read(in);
                        i++;
                    }while(data.code== KuboData.PORTS_SPECTRUM);
                    //发送成功
                    broadcastor.sendMessage(new SyncMessage(SyncMessage.LOGIN_SUCCEED));
                } catch (Exception e) {
                    e.printStackTrace();
                    String message= "密码不正确，请确认";
                    //否则失败
                    broadcastor.sendMessage(new SyncMessage(SyncMessage.LOGIN_FAILED,message));
                    //直接结束读取线程
                    return;
                }

                try {
                    //登录成功，则继续读取数据
                    broadcastor.sendMessage(data);
                    while (this.readThread != null) {
                            data = KuboData.read(in);
                        broadcastor.sendMessage(data);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "读取数据异常", e);
                    //发出错误异常消息数据
                    broadcastor.sendMessage(new SyncMessage(SyncMessage.READ_EXCEPTION));
                }
        }
    }

    public void stop() {
        try {
            this.readThread.interrupt();
            this.readThread = null;
        }catch (Exception e){

        }
    }
}
