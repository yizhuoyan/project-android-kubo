package com.liuyongmei.kubo.model;

import com.liuyongmei.kubo.model.datamodel.KuboData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.charset.Charset;

public class CommandBuilder {
    //命名头
	public static final byte[] COMMAND_HEADER=new byte[30];
	//有效命令头字符个数
	public static int VALID_COMMAND_HEADER_BYTES;
    static {
		//初始化命令头相关
        byte[]  bs="Kubo X1000".getBytes(Charset.forName("ISO-8859-1"));
		VALID_COMMAND_HEADER_BYTES=bs.length;
        System.arraycopy(bs,0,COMMAND_HEADER,0,VALID_COMMAND_HEADER_BYTES);
    }
	/**
     * 登陆仪器
     * */
	public static final short SEND_CODE$LOGIN=0x1010;
	/**获取仪器的分析端口数量*/
	public static final short SEND_CODE$PORT_COUNT=0x7171;
	/**发送获取仪器分析进度请求*/
	public static final short SEND_CODE$ANALYZE_PROGRESS =0x3C3C;
	/**发送解锁仪器气路请求*/
	public static final short SEND_CODE$UNLOCK_GAS_PATH=0x3737;
	/**连接检测*/
	public static final short CODE$CHECK_CONNECTION=0x4e4e;
	/**发送获取分析端口参数请求*/
	public static final short SEND_CODE$PORT_PARAMETER=0x7373;
	/**终止分析命令*/
	public static final short SEND_CODE$TERMINATION=0x3f3f;


	public static String getCommandName(byte[] command){
		//读取识别码
		int a=command[30];
		int b=command[31];
		short code=(short)((a<<8)+b);
		switch (code){
			case SEND_CODE$ANALYZE_PROGRESS:
				return "发送分析进程";

			case KuboData.PORT_ANALYZE_PROGRESS:
				return "接收进度数据";
			case CODE$CHECK_CONNECTION:
				return "连接检测";
			case KuboData.PORT_COUNT:
				return "接收端口数量";
			case KuboData.PORT_PARAMETER:
				return "接收端口参数";
			case KuboData.PRESSURE_TEMPERATURE:
				return "接收压力和温度";
			case KuboData.SOLENOIDVALVE_PFC:
				return "接收电磁阀和PFC状态";
			case KuboData.PORTS_SPECTRUM:
				return "接收谱图数据";
			case SEND_CODE$LOGIN:
				return "登录";
			case SEND_CODE$PORT_COUNT:
				return "发送端口数量";
			case SEND_CODE$PORT_PARAMETER:
				return "发送端口参数请求";
			case SEND_CODE$TERMINATION:
				return "终止";
			case SEND_CODE$UNLOCK_GAS_PATH:
				return "发送解锁气门";
		}
		return "未识别";
	}
	public static String getCommandName(int code){
		switch (code){
			case SEND_CODE$ANALYZE_PROGRESS:
				return "发送分析进程";
			case KuboData.PORT_ANALYZE_PROGRESS:
				return "接收进度数据";
			case CODE$CHECK_CONNECTION:
				return "连接检测";
			case KuboData.PORT_COUNT:
				return "接收端口数量";
			case KuboData.PORT_PARAMETER:
				return "接收端口参数";
			case KuboData.PRESSURE_TEMPERATURE:
				return "接收压力和温度";
			case KuboData.SOLENOIDVALVE_PFC:
				return "接收电磁阀和PFC状态";
			case KuboData.PORTS_SPECTRUM:
				return "接收谱图数据";
			case SEND_CODE$LOGIN:
				return "登录";
			case SEND_CODE$PORT_COUNT:
				return "发送端口数量";
			case SEND_CODE$PORT_PARAMETER:
				return "发送端口参数请求";
			case SEND_CODE$TERMINATION:
				return "终止";
			case SEND_CODE$UNLOCK_GAS_PATH:
				return "发送解锁气门";
		}
		return "未识别";
	}
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
			os.writeShort(SEND_CODE$LOGIN);/** 识别码*/
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
			os.writeShort(CODE$CHECK_CONNECTION);/** 识别码*/
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
	public static byte[] portCount(){
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
	public static byte[] portParameterSetData(int port) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(40);
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
	public static byte[] unlockGasPath(String password){
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
	public static byte[] teminateAnalyse(){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(40);
			DataOutputStream os = new DataOutputStream(baos);
			os.write(COMMAND_HEADER);
			os.writeShort(SEND_CODE$TERMINATION); /**识别码*/
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
