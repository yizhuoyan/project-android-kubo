package com.liuyongmei.kubo.model;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.liuyongmei.kubo.MyApplication;
import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.common.AssertUtils;
import com.liuyongmei.kubo.common.ThisAppException;
import com.liuyongmei.kubo.model.datamodel.Data;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class AppService  {
	private static ExecutorService pool= Executors.newCachedThreadPool();
	private static AppService instance=new AppService();


	private Socket socket;

    public DataReader dataReader=null;
    public DataSender dataSender=null;
    public KeepAliveStrategy keepAliveStrategy=null;

    public AppService() {
        dataReader=new DataReader();
        dataSender=new DataSender();
        keepAliveStrategy=new KeepAliveStrategy();
    }

    public void stop(){
        try {
            dataReader.stop();
            dataSender.stop();
            keepAliveStrategy.stop();
        }catch (Exception e){

        }
    }
    public void sendMsg(byte[] command){
		this.dataSender.sendCommand(command);
	}
	
	public void addReceiveDataListener(short code,DataReader.Callback callback) {
		dataReader.addCallback(code,callback);
	}
	
	public static AppService getInstance() {
		return instance;
	}
		

	
	public void connect(final String ipInput, final  String passwordInput, final Handler handler){
		pool.submit(new Runnable() {
			@Override
			public void run() {
                Message m=Message.obtain();
                m.what=500;
				try {

                    String ip = AssertUtils.notBlank("请输入IP地址", ipInput);
                    String password = AssertUtils.notBlank("请输入密码", passwordInput);
                    int port = MyApplication.PORT;

                    socket = new Socket(ip, port);

                    dataSender.start(socket.getOutputStream());
                    dataSender.sendCommand(CommandBuilder.login(password));

                    dataReader.start(socket.getInputStream());
                    //开启保持连接策略
                   // keepAliveStrategy.start(dataSender);
                    m.what=200;
                }catch (ThisAppException e){
                    m.obj=e.getMessage();
				}catch (IOException e){
                    e.printStackTrace();
                    //TODO:后期修改为字符资源id
                    m.obj="无法连接到指定ip，请确认ip和密码";
				}catch (Throwable e){
                    e.printStackTrace();
                    m.obj="系统异常，请联系管理员";
                }finally {
                    handler.sendMessage(m);
                }

			}
		});


	}
	


}
