package com.liuyongmei.kubo.model;

import android.util.Log;

import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class SyncMessageBroadcastor implements Runnable {
    //通知线程
    private volatile Thread notifyThread;
    private Map<Integer, List<SyncMessageListener>> listenerMap;
    //数据接收队列
    private BlockingQueue<SyncMessage> messageQueue = new LinkedBlockingQueue<>();
    private static  SyncMessageBroadcastor instance=new SyncMessageBroadcastor();

    public SyncMessageBroadcastor() {
        this.listenerMap = new HashMap<>();
        notifyThread = new Thread(this);
        notifyThread.start();
    }
    public static SyncMessageBroadcastor getInstance(){
        return instance;
    }
    public void addListener(int code, SyncMessageListener listener) {
        List<SyncMessageListener> cs = this.listenerMap.get(code);
        if (cs == null) {
            this.listenerMap.put(code, cs = new ArrayList<SyncMessageListener>());
        }
        cs.add(listener);
    }
    public void sendMessage(SyncMessage m){
        try {
            this.messageQueue.put(m);
        }catch (Exception e){}
    }
    @Override
    public void run() {
        try {
            while (notifyThread != null) {
                SyncMessage data = messageQueue.take();
                //通知
                List<SyncMessageListener> cs = this.listenerMap.get(data.code);
                if (cs != null) {
                    for (SyncMessageListener c : cs) {
                        Log.d("xxx","读到数据，通知"+c.getClass());
                        c.onReceive(data);
                    }
                }
            }
        }catch (InterruptedException e){
            //线程被打断，关闭
        }
    }
}
