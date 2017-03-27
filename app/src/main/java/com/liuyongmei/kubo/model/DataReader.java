
package com.liuyongmei.kubo.model;


import android.util.Log;

import com.liuyongmei.kubo.model.datamodel.Data;
import com.liuyongmei.kubo.model.datamodel.DataReaderInputStream;

import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class DataReader implements Runnable {

    private static final String TAG = DataReader.class.getName();
    //数据接收队列
    private BlockingQueue<Data> dataQueue = new LinkedBlockingQueue<>();

    static public interface Callback {
        void onReceive(Data data);
    }

    //接收线程
    private volatile Thread readThread;
    //通知线程
    private volatile Thread notifyThread;

    private DataReaderInputStream in;
    private Map<Short, List<Callback>> callbacks;

    public void addCallback(short code, Callback callback) {
        Log.d(TAG,"注册数据接收器"+code+"==>"+callback.getClass());
        List<Callback> cs = this.callbacks.get(code);
        if (cs == null) {
            this.callbacks.put(code, cs = new ArrayList<Callback>());
        }
        cs.add(callback);
    }

    public DataReader() {
        this.callbacks = new HashMap<>();
    }

    public void start(InputStream dis) {
        this.in = new DataReaderInputStream(dis);
        readThread = new Thread(this);
        notifyThread = new Thread(this);
        notifyThread.start();
        this.readThread.start();
        Log.d(TAG, "开始数据读取线程");
    }

    public void run() {
        if (Thread.currentThread() == notifyThread) {
            try {
                while (notifyThread != null) {
                    Data data = dataQueue.take();
                    //通知
                    List<Callback> cs = this.callbacks.get(data.code);

                    if (cs != null) {
                        for (Callback c : cs) {
                            Log.d(TAG,"读到数据，通知"+c.getClass());
                            c.onReceive(data);
                        }
                    }
                }
            }catch (InterruptedException e){
                //线程被打断，关闭

            }
        } else if (Thread.currentThread() == readThread) {
            Data data = null;
            while (this.readThread != null) {
                try {
                    data = Data.read(in);
                    Log.d(TAG, "读到数据" + data);
                    dataQueue.put(data);
                } catch (SocketTimeoutException e) {
                    Log.d(TAG, "读取数据超时，关闭连接");
                    this.stop();
                } catch (Exception e) {
                    Log.d(TAG, "读取数据异常", e);
                }
            }
        }
    }

    public void stop() {
        this.readThread.interrupt();
        this.readThread = null;
        this.notifyThread.interrupt();
        this.notifyThread=null;
    }
}
