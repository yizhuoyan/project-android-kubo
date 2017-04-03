package com.liuyongmei.kubo.model;

import android.util.Log;

import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 发送命令器
 * @author Administrator
 *
 */
public class DataSender implements Runnable{
	private static final String TAG=DataSender.class.getName();
    //发送线程
	private volatile  Thread sendThread ;
	//发送命令队列
	private BlockingQueue<byte[]> queue; 
	//底层输出流，任务开始时传入
	private OutputStream out;
	
	public DataSender() {

	}
    public void start(OutputStream out){
        queue=new LinkedBlockingQueue<>();
        this.out = out;
        sendThread = new Thread(this);
        //开启线程
        this.sendThread.start();
    }
	@Override
	public void run() {
		try {
			//存为局部变量，提高效率
			OutputStream out = this.out;
			//发送命令
			byte[] command = null;
			while (this.sendThread != null) {
				out.write(command = queue.take());
				out.flush();
				Log.d(TAG, "发送命令成功:" + CommandBuilder.getCommandName(command));
			}
		}catch (InterruptedException e){
			//线程被手动打断，忽略异常
		}catch(Exception e){
			//其他
			if(this.sendThread==null){//线程被手动关闭，忽略异常，通知
				return;
			}
            //发生未知异常，发送广播
			AppService.getInstance().broadMessage(new SyncMessage(SyncMessage.UNKNOW_EXCEPTION));
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加要发送的命令
	 */
	public void sendCommand(byte[] command){
		try {
			this.queue.put(command);
		} catch (InterruptedException e) {
		}
	}

    /**
     * 停止发送线程
     */
	public void stop(){
		if(this.sendThread.isAlive()) {
			this.sendThread.interrupt();
			this.sendThread = null;
		}
		this.queue.clear();
        this.queue=null;
		//不关闭流，让socket统一关闭
		//out.close();
		this.out=null;
		Log.d("xxxlife","关闭发送线程");
	}

	
}
