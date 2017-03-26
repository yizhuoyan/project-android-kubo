package com.liuyongmei.kubo.model;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 发送命令
 * @author Administrator
 *
 */
public class DataSender implements Runnable{
	private static final String TAG=DataSender.class.getName();
	private Thread thread = null;
	
	//发送命令队列
	private BlockingQueue<byte[]> queue; 
	
	private OutputStream out;
	
	public DataSender() {

	}

	@Override
	public void run() {
		try{
			//发送命令
			while(this.thread!=null){
				Log.d(TAG,"获取待发送的命令");
				byte[] command = queue.take();
				Log.d(TAG,"发送命令"+ Arrays.toString(command));
				out.write(command);
				out.flush();
			}
		}catch(Exception e){
			try {
				out.close();
			} catch (IOException e1) {}
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 发送命令
	 * @param command
	 */
	public void sendCommand(byte[] command){
		try {
			Log.d(TAG,"放入命令"+ Arrays.toString(command));
			this.queue.put(command);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void stop(){
		this.thread.interrupt();
		this.thread=null;
	}
	public void start(OutputStream out){
		queue = new LinkedBlockingDeque<byte[]>();
		this.out = out;
		thread = new Thread(this);
		this.thread.start(); //开启线程
		Log.d(TAG,"开始发送读取线程");
	}
	
}
