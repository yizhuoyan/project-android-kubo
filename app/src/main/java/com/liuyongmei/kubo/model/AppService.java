package com.liuyongmei.kubo.model;

import android.util.Log;

import com.liuyongmei.kubo.MyApplication;
import com.liuyongmei.kubo.common.AssertUtils;
import com.liuyongmei.kubo.common.ThisAppException;
import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppService  {
    //线程池
    private static ExecutorService pool = Executors.newSingleThreadExecutor();
    //单例模式
    private static AppService instance = new AppService();
    //底层的socket对象
    private Socket socket;
    public String currentIp;
    //保存登录成功的密码，用于获取谱图数据
    private String currentPassword;
    //数据读取器
    private DataReader dataReader;
    //数据发送器
    private DataSender dataSender;
    //保存连接策略，后期可以不要
    private KeepAliveStrategy keepAliveStrategy ;
    //消息广播器
    private  SyncMessageBroadcastor broadcastor;
    //是否已发送了解锁请求，用于避免多次发送
    private boolean sendUnlockGasPathAlready=false;
    //是否准备好
    private static boolean unready=true;

    /**
     * 返回唯一实例
     */
    public static AppService getInstance() {

        return instance;
    }

    public AppService() {

    }
    private void init(){
        //判断是否初始化
        if(unready){
            //获取唯一广播实例
            broadcastor=new SyncMessageBroadcastor();
            //谱图数据需要延迟推送
            broadcastor.delayBroadcastMessage(KuboData.PORTS_SPECTRUM);
            Log.d("xxxlife","初始化资源");
            unready=false;
        }
    }
    /**
     * 销毁各种资源
     */
    public void destroy(){
        try{
            Log.d("xxxlife","销毁各种资源开始");
            if(dataReader!=null) {
                dataReader.stop();
            }
            if(dataReader!=null){
                dataSender.stop();
            }
            if(keepAliveStrategy!=null) {
                keepAliveStrategy.stop();
            }
            if(socket!=null) {
                try {
                    if (this.socket.isConnected()) {
                        this.socket.close();
                        socket = null;
                        Log.d("xxxlife", "关闭socket");
                    }
                }catch (IOException e){}
            }
            if(broadcastor!=null) {
                broadcastor.stop();
            }
            unready=true;
            Log.d("xxxlife","销毁各种资源完毕");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 广播数据
     */
    public void broadMessage(SyncMessage m){
        init();
        this.broadcastor.sendBroadcastMessage(m);
    }

    /**
     * 重新封装，给界面提供唯一操作对象，不直接操作broadcastor
     */
    public void addReceiveDataListener(int code, SyncMessageListener callback) {
        init();
        broadcastor.addListener(code, callback);
    }

    /**
     * * 重新封装，给界面提供唯一操作对象，不直接操作broadcastor
     */
    public void removeReceiveDataListener(int code, SyncMessageListener callback) {
        if(broadcastor!=null) {

            broadcastor.removeListener(code, callback);
        }
    }

    /**
     * 提供给视图当前是否已登录标志
     */
    public boolean isLogin(){
        return this.socket!=null&&this.socket.isConnected();
    }

    /**
     * 登录连接操作
     * @param ipInput 输入ip
     * @param passwordInput 输入密码
     */
    public void loginInConnect(final String ipInput, final String passwordInput) {
        init();
        pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //1 验证ip和密码
                    String ip = AssertUtils.notBlank("请输入IP地址", ipInput);
                    String password = AssertUtils.notBlank("请输入密码", passwordInput);
                    //关闭之前的socket，如果有的话
                    closeOldSocket();
                    //构建新的socket
                    int port = MyApplication.PORT;
                    int timeout=MyApplication.timeout*1000;
                    socket=buildSocket(ip,port,timeout);
                    broadcastor.sendBroadcastMessage(SyncMessage.LOGINING,"开始用密码进行登录");
                    //发送登录命令，如果登录失败，会直接回调界面
                    dataSender.sendCommand(CommandBuilder.login(password));
                    //保存密码，用于再次获取谱图数据
                    currentPassword =password;
                    currentIp=ip;
                } catch (ThisAppException e) {
                    //广播登录失败消息
                    broadcastor.sendBroadcastMessage(SyncMessage.LOGIN_FAILED,e.getMessage());
                }catch (Exception e){
                    e.printStackTrace();
                    //其他未知异常
                    broadcastor.sendBroadcastMessage(SyncMessage.UNKNOW_EXCEPTION);
                }

            }
        });
    }


    private void closeOldSocket(){
        if(socket!=null){
            if(socket.isConnected()){
                Log.d("xxxlife","之前socket未关闭，关闭之");
                try {
                    //关闭读写线程()
                    dataSender.stop();
                    dataReader.stop();
                    //关闭socket
                    socket.close();
                    socket=null;
                    Log.d("xxxlife","关闭socket");
                }catch (Exception e){
                    //关闭的异常忽略
                }
            }
        }
    }
    private Socket buildSocket(String ip,int port,int timeout){
        //构建socket连接
        Socket socket = new Socket();
        SocketAddress address=new InetSocketAddress(ip,port);
        try {

            //通知进度
            broadcastor.sendBroadcastMessage(new SyncMessage(SyncMessage.LOGINING,"正在连接到"+ip));
            socket.connect(address, timeout);
            if(socket.isConnected()){
                //通知进度
                broadcastor.sendBroadcastMessage(new SyncMessage(SyncMessage.LOGINING,"已成功连接到设备"));
            }
            //启动各种服务
            dataReader = new DataReader(this.broadcastor);
            dataSender = new DataSender();
            //keepAliveStrategy = new KeepAliveStrategy();
            dataSender.start(socket.getOutputStream());
            dataReader.start(socket.getInputStream());
            return socket;
        }catch (SocketTimeoutException e){
            throw new  ThisAppException("连接超时，请确认IP");
        } catch (IOException e) {
            throw new  ThisAppException("在连接期间发生错误，请确认ip",e);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
    /**
     * 请求谱图数据
     */
    public void sendSpectrumData(){
        broadcastor.undelayBroadcastMessage(KuboData.PORTS_SPECTRUM);
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

    /**
     * 发送终止命令
     */
    public void sendTeminateCommand(){
        this.dataSender.sendCommand(CommandBuilder.teminateAnalyse());
    }

    /**
     * 发送解锁仪器气路请求
     */
    public void sendUnlockGasPathCommand(){
        //如果已发送过，这不再次发送
        if(!sendUnlockGasPathAlready) {
            this.dataSender.sendCommand(CommandBuilder.unlockGasPath("system"));
        }
    }
}
