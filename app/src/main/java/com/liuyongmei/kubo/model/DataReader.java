
package com.liuyongmei.kubo.model;


import android.util.Log;

import com.liuyongmei.kubo.common.ThisAppException;
import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.DataReaderInputStream;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class DataReader implements Runnable {

    private static final String TAG = DataReader.class.getName();

    //接收线程
    private volatile Thread readThread;
    //底层输入流
    private DataReaderInputStream in;
    //广播器
    private final SyncMessageBroadcastor broadcastor;

    public DataReader(SyncMessageBroadcastor messageBroadcastor) {
        broadcastor = messageBroadcastor;
    }

    public void start(InputStream dis) {
        this.in = new DataReaderInputStream(dis);
        readThread = new Thread(this);
        this.readThread.start();
    }

    /**
     * 处理登录返回数据
     * @return 第一个谱图数据
     */
    private KuboData  handleLogin(DataReaderInputStream in)throws ThisAppException{
        KuboData data = null;
        try {
            //如果一开始就读到数据且收到的是谱图数据，表示登录成功,缓存谱图数据
            data = KuboData.read(in);
            if (data.code == KuboData.PORTS_SPECTRUM) {
                return data;
            }else {
                //不是谱图数据,则登录失败，抛出异常，下面统一处理
                throw  new ThisAppException("登录失败");
            }
        } catch (IOException e) {
            //此过程出现任何IOException异常，则表示密码不正确,登录失败
            String message = "密码不正确，请确认";
            e.printStackTrace();
            throw  new ThisAppException(message);
        }
    }
    public void run() {
        //存放局部，提高效率
        final DataReaderInputStream in = this.in;
        //广播器
        final SyncMessageBroadcastor broadcastor = this.broadcastor;
        //数据变量
        KuboData data = null;
        //登录处理
        try{
            data=handleLogin(in);
            //则通知登录成功
            broadcastor.sendBroadcastMessage(new SyncMessage(SyncMessage.LOGIN_SUCCEED));
        }catch (ThisAppException e){
            //登录失败
            broadcastor.sendBroadcastMessage(new SyncMessage(SyncMessage.LOGIN_FAILED, e.getMessage()));
            //关闭线程
            return;
        }
        try {
            //登录成功，循环读取后面的数据
            while (this.readThread != null) {
                broadcastor.sendBroadcastMessage(data);
                data = KuboData.read(in);
            }
        } catch (Exception e) {
            if(this.readThread==null){
                Log.d(TAG, "手动关闭，不记录异常", e);
                //手动关闭，不记录异常
                return;
            }
            //未知异常，通知
            broadcastor.sendBroadcastMessage(new SyncMessage(SyncMessage.UNKNOW_EXCEPTION));
            Log.e(TAG, "读取数据异常", e);
        }
    }

    /**
     * 停止线程
     */
    public void stop() {
        if(this.readThread.isAlive()) {
            this.readThread.interrupt();
            this.readThread = null;
        }
            //不关闭流，让socket统一关闭
            this.in=null;
        Log.d("xxxlife","关闭读线程");
    }
}
