
package com.liuyongmei.kubo.model;


import android.util.Log;

import com.liuyongmei.kubo.model.datamodel.Data;
import com.liuyongmei.kubo.model.datamodel.DataReaderInputStream;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataReader implements Runnable {

    private static final String TAG=DataReader.class.getName();

    static public interface Callback {
        void onReceive(Data data);
    }
    private volatile Thread thread;
    private DataReaderInputStream in;
    private Map<Short,List<Callback>> callbacks;

    public void addCallback(short code,Callback callback){
        List<Callback> cs=this.callbacks.get(code);
        if(cs==null){
            this.callbacks.put(code,cs=new ArrayList<Callback>());
        }
        cs.add(callback);
    }

    public DataReader() {
        this.callbacks=new HashMap<>();
    }
    public void start(InputStream dis){
        this.in = new DataReaderInputStream(dis);
        thread=new Thread(this);
        this.thread.start();
        Log.d(TAG,"开始数据读取线程");
    }
    public void run() {
        try {
            Data data=null;
            while (this.thread!=null) {
                try {

                    data = Data.read(in);
                    Log.d(TAG,"读到数据"+data);
                    //通知
                    List<Callback> cs = this.callbacks.get(data.code);
                    if (cs != null) {
                        for (Callback c : cs) {
                            c.onReceive(data);
                        }
                    }
                }catch (SocketTimeoutException e){
                    Log.d(TAG,"读取数据超时，关闭连接");
                    this.stop();
                }catch (Exception e){
                    Log.d(TAG,"读取数据异常",e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stop(){
        this.thread.interrupt();
        this.thread=null;
    }
}
