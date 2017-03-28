package com.liuyongmei.kubo.model;

import android.os.Message;

import com.liuyongmei.kubo.MyApplication;
import com.liuyongmei.kubo.common.AssertUtils;
import com.liuyongmei.kubo.common.ThisAppException;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppService implements SyncMessageListener {
    private static ExecutorService pool = Executors.newCachedThreadPool();
    private static AppService instance = new AppService();


    private Socket socket;

    private String savedPassword;
    public DataReader dataReader = null;
    public DataSender dataSender = null;
    public KeepAliveStrategy keepAliveStrategy = null;
    private SyncMessageBroadcastor broadcastor=SyncMessageBroadcastor.getInstance();

    public AppService() {
       this.restart();
        //关注连接失败
        broadcastor.addListener(SyncMessage.READ_EXCEPTION,this);
    }

    private void restart(){
        dataReader = new DataReader();
        dataSender = new DataSender();
        keepAliveStrategy = new KeepAliveStrategy();
    }

    public void stop() {
        try {
            dataReader.stop();
            dataSender.stop();
            keepAliveStrategy.stop();
            this.socket.close();
            dataReader=null;
            dataSender=null;
            keepAliveStrategy=null;
            this.socket=null;
        } catch (Exception e) {

        }
    }

    public void sendMsg(byte[] command) {
        this.dataSender.sendCommand(command);
    }

    public void addReceiveDataListener(int code, SyncMessageListener callback) {
        broadcastor.addListener(code, callback);
    }

    public static AppService getInstance() {
        return instance;
    }

    public boolean isLogin(){
        return this.socket!=null&&this.socket.isConnected();
    }

    public void connect(final String ipInput,final String passwordInput) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //1 验证ip和密码
                    String ip = AssertUtils.notBlank("请输入IP地址", ipInput);
                    String password = AssertUtils.notBlank("请输入密码", passwordInput);
                    //2判断是否之前已经登录，若是，释放之前相关资源，重新构建
                    if(socket!=null){
                        stop();
                        restart();
                    }
                    //3 重新连接登录
                    int port = MyApplication.PORT;
                    int timeout=MyApplication.timeout*1000;
                    socket = new Socket();
                    SocketAddress address=new InetSocketAddress(ip,port);
                    socket.connect(address,timeout);

                    dataSender.start(socket.getOutputStream());
                    dataSender.sendCommand(CommandBuilder.login(password));
                    //保存密码，用于获取谱图数据
                    savedPassword=password;
                    dataReader.start(socket.getInputStream());
                    //开启保持连接策略
                    // keepAliveStrategy.start(dataSender);
                    //连接成功
                } catch (ThisAppException e) {
                    broadcastor.sendMessage(new SyncMessage(SyncMessage.LOGIN_SUCCEED));
                } catch (IOException e) {
                    String message= "无法连接到指定ip，请确认ip";
                    broadcastor.sendMessage(new SyncMessage(SyncMessage.LOGIN_FAILED,message));
                    e.printStackTrace();
                }

            }
        });


    }

    /**
     * 请求谱图数据
     */
    public void sendSpectrumData(){
        dataSender.sendCommand(CommandBuilder.login(savedPassword));
    }


    /**
     * 请求端口数量
     *
     */
    public void sendPortCountCommand() {

        this.dataSender.sendCommand(CommandBuilder.portCount());
    }

    /**
     * 请求端口参数
     *
     * @param port
     */
    public void sendPortParameterSetCommand(int port) {

        this.dataSender.sendCommand(CommandBuilder.portParameterSetData(port));
    }

    /**
     * 请求当前端口分析进度
     *
     */
    public void sendProgressCommand() {

        this.dataSender.sendCommand(CommandBuilder.progress());
    }

    @Override
    public void onReceive(SyncMessage message) {
        switch (message.code){
            case SyncMessage.READ_EXCEPTION: {
                //连接发生异常，清空相关资源
                this.stop();
                break;
            }
        }
    }

}
