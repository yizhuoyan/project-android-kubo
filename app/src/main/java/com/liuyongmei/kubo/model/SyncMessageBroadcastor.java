package com.liuyongmei.kubo.model;

import android.util.Log;

import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class SyncMessageBroadcastor implements Runnable {
    //广播线程
    private volatile Thread broadcastThread;
    //保存所有接收器
    private final Map<Integer, List<SyncMessageListener>> listenerMap;
    //数据接收队列
    private final BlockingQueue<SyncMessage> messageQueue;
    //需要延迟广播的消息代号
    private final Set<Integer> delayMessageCodes;
    //延迟消息集合
    private final Map<Integer,Queue<SyncMessage>> delayMessageMaps;


    //已创建就开始广播线程
    public SyncMessageBroadcastor() {
        this.listenerMap = new HashMap<>();
        messageQueue=new LinkedBlockingQueue<>();
        broadcastThread = new Thread(this);
        delayMessageMaps=new HashMap<>();
        delayMessageCodes=new HashSet<>();
        broadcastThread.start();
        Log.d("xxxlife","开启广播线程");
    }



    //添加延迟消息代号
    public void delayBroadcastMessage(int code){
        delayMessageCodes.add(code);
    }

    public void undelayBroadcastMessage(int code){
        delayMessageCodes.remove(code);
        Queue<SyncMessage> queue=delayMessageMaps.get(code);
        if(queue!=null){
            SyncMessage m=null;
            while((m=queue.poll())!=null){
                sendBroadcastMessage(m);
            }
        }
    }

    /**
     * 停止广播线程，一般在应用关闭时
     */
    public void stop(){
        try {
            //停止线程
            this.broadcastThread.interrupt();
            this.broadcastThread = null;
            //清空接收器
            this.listenerMap.clear();
            //清空消息队列
            this.messageQueue.clear();
            //清空延迟消息列表
            this.delayMessageCodes.clear();
            delayMessageCodes.clear();
            Log.d("xxxlife","停止广播线程");

        }catch (Exception e){

        }
    }

    /**
     * 添加接收器
     * @param code 接收数据类型，参考KuboData数据类型静态常量和SyncMessage常量
     * @param listener 接收器对象
     */
    public void addListener(int code, SyncMessageListener listener) {
        List<SyncMessageListener> cs = this.listenerMap.get(code);
        if (cs == null) {
            this.listenerMap.put(code, cs = new ArrayList<SyncMessageListener>());
        }
        Log.d("xxx","添加接收器"+listener);
        cs.add(listener);
    }

    /**
     * 删除接收器
     * @param code 参考addListener方法
     * @param listener 接收器对象
     */
    public void removeListener(int code, SyncMessageListener listener) {
        List<SyncMessageListener> cs = this.listenerMap.get(code);
        if (cs != null) {
            Log.d("xxx","删除接收器"+listener);
            cs.remove(listener);
        }
    }

    /**
     * 发送广播消息
     * @param m
     */
    public void sendBroadcastMessage(SyncMessage m){
        try {
            int code=m.code;
            if(this.delayMessageCodes.size()!=0&&this.delayMessageCodes.contains(code)){
                Log.d("xxx","是延迟消息，放入队列"+m);
                Queue<SyncMessage> queue=delayMessageMaps.get(code);
                if(queue==null){
                    queue=new LinkedList<>();
                    delayMessageMaps.put(code,queue);
                }
                queue.offer(m);
                return;
            }
            Log.d("xxx","非延迟消息，添加到消息队列"+m);
            this.messageQueue.put(m);
        }catch (Exception e){
            Log.d("xxx","发送广播异常",e);
        }
    }
    public void sendBroadcastMessage(int code,String message) {
        this.sendBroadcastMessage(new SyncMessage(code,message));
    }
    public void sendBroadcastMessage(int code) {
        this.sendBroadcastMessage(new SyncMessage(code));
    }
    /**
     * 底层广播线程任务
     */
    @Override
    public void run() {
        try {
            while (broadcastThread != null) {
                //取得消息，如果无，则阻塞
                SyncMessage data = messageQueue.take();

                //通知广播接收器
                List<SyncMessageListener> cs = this.listenerMap.get(data.code);

                if (cs != null) {
                    Iterator<SyncMessageListener> listenerIterator = cs.iterator();
                    SyncMessageListener li=null;
                    while(listenerIterator.hasNext()){
                        li=listenerIterator.next();
                        try {
                            Log.d("xxx","取得消息，通知jieshouqi"+li);
                            li.onReceive(data);
                        }catch (Throwable e){//如果某个接收器处理消息异常，则从接收器组中删除
                            Log.e("xxx","处理消息异常",e);
                            //删除之
                            listenerIterator.remove();
                        }
                    }
                }else {
                    Log.d("xxx","有消息，无接收器"+data.toString());
                }
            }
        }catch (InterruptedException e){
            //线程被打断，关闭
        }
    }
}
