package com.liuyongmei.kubo.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.charset.Charset;

public class CommandBuilder {
    //命名头
	public static final byte[] COMMAND_HEADER=new byte[30];
    static {
        byte[]  bs="Kubo X1000".getBytes(Charset.forName("ISO-8859-1"));
        System.arraycopy(bs,0,COMMAND_HEADER,0,bs.length);
    }
	/**
     * 登陆仪器
     * */
	public static final short SEND_CODE$LOGIN=0x1010;
	/**接收谱图数据*/
	public static final short RECEIVE_CODE$SPECTRUM=0x5050;

	/**获取仪器的分析端口数量*/
	public static final short SEND_CODE$PORT_COUNT=0x7171;
	/**接收仪器端口数量*/
	public static final short RECEIVE_CODE$PORT_COUNT=0x7272;

	/**发送获取仪器分析进度请求*/
	public static final short SEND_CODE$ANALYZE_PROGRESS =0x3c3c;
	/**接收仪器当前分析进度*/
	public static final short RECEIVE_CODE$ANALYZE_PROGRESS =0x3d3d;

	/**发送解锁仪器气路请求*/
	public static final short SEND_CODE$UNLOCK_GAS_PATH=0x3737;
	/**接收仪器压力和温度值*/
	public static final short RECEIVE_CODE$PRESSURE_TEMPERATURE=0x3939;
	/**接收电磁阀和PFC状态*/
	public static final short RECEIVE_CODE$SOLENOIDVALVE_PFC=0x3a3a;


	/**连接检测*/
	public static final short CODE$CHECK_CONNECTION=0x4e4e;

	/**发送获取分析端口参数请求*/
	public static final short SEND_CODE$PORT_PARAMETER=0x7373;
	/**接收分析端口参数*/
	public static final short RECEIVE_CODE$PORT_PARAMETER=0x7474;

	/**终止分析命令*/
	public static final short SEND_CODE$TERMINATION=0x3f3f;


	/**
	 * 登陆
	 * @param password
	 * @return
	 */
	public static byte[] login(String password) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(50);
			DataOutputStream os = new DataOutputStream(baos);
			os.write(COMMAND_HEADER);
			os.writeShort(SEND_CODE$LOGIN);/** 命令码*/
			os.writeInt(password.length() + 1); /** 密码长度 +1*/
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeBytes(password); /** 附件信息 密码*/
			os.writeByte(0);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 连接检测
	 * @return
	 */
	public static byte[] keepAlive(){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(50);
			DataOutputStream os = new DataOutputStream(baos);
			os.write(COMMAND_HEADER);
			os.writeShort(CODE$CHECK_CONNECTION);/** 命令码*/
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0); 
			os.writeByte(0);
			os.writeByte(0); 
			os.writeByte(0);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * 分析端口数量命令
	 * @return
     */
	public static byte[] portNumber(){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(50);
			DataOutputStream os = new DataOutputStream(baos);
			os.write(COMMAND_HEADER);
			os.writeShort(SEND_CODE$PORT_COUNT);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 分析进度命令
	 * @return
	 */
	public static byte[] progress(){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(50);
			DataOutputStream os = new DataOutputStream(baos);
			os.write(COMMAND_HEADER);
			os.writeShort(SEND_CODE$ANALYZE_PROGRESS);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 发送仪器参数请求
	 * @param port 仪器分析端口号
	 * @return
	 */
	public static byte[] netRunSetData(int port) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(50);
			DataOutputStream os = new DataOutputStream(baos);
			os.write(COMMAND_HEADER);
			os.writeShort(SEND_CODE$PORT_PARAMETER);
			os.writeInt(port);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 发送解锁仪器气路请求
	 * @param password 解锁密码
	 * @return
	 */
	public static byte[] unlockAirWay(String password){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(50);
			DataOutputStream os = new DataOutputStream(baos);
			os.write(COMMAND_HEADER);
			os.writeShort(SEND_CODE$UNLOCK_GAS_PATH);
			os.writeInt(password.length()+1); /**密码长度 +1*/
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeBytes(password); /**附件信息 密码*/
			os.writeByte(0);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 终止分析
	 * @return
	 */
	public static byte[] analysisStop(){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(50);
			DataOutputStream os = new DataOutputStream(baos);
			os.write(COMMAND_HEADER);
			os.writeShort(SEND_CODE$TERMINATION); /**命令码*/
			os.writeInt(0x02020000); /**类型码*/
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			os.writeByte(0);
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
