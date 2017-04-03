package com.liuyongmei.kubo.model.datamodel;

import android.util.Log;

import com.liuyongmei.kubo.model.CommandBuilder;

import java.io.DataInput;
import java.io.IOException;
import java.io.Serializable;

import static com.liuyongmei.kubo.model.CommandBuilder.COMMAND_HEADER;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public abstract class KuboData extends SyncMessage implements Serializable {
    private  static final String TAG=KuboData.class.getName();
    /**接收仪器端口数量*/
    public static final short PORT_COUNT =0x7272;
    /**接收谱图数据*/
    public static final short PORTS_SPECTRUM =0x5050;
    /**接收仪器当前分析进度*/
    public static final short PORT_ANALYZE_PROGRESS =0x3d3d;
    /**接收仪器压力和温度值*/
    public static final short PRESSURE_TEMPERATURE =0x3939;
    /**接收电磁阀和PFC状态*/
    public static final short SOLENOIDVALVE_PFC =0x3a3a;
    /**连接检测*/
    public static final short CHECK_CONNECTION =0x4e4e;
    /**接收分析端口参数*/
    public static final short PORT_PARAMETER =0x7474;
    //所有数据的命令头一样，定义为静态变量
    public final  static byte[] COMMAND_HEADER=CommandBuilder.COMMAND_HEADER;

    public KuboData(){

    }
    /**
     * 是否是头数据
     * @param
     * @return
     */
    private static boolean readCommandHeader(DataInput in)throws IOException{
        final byte[] header=KuboData.COMMAND_HEADER;
        final int valideHeaderLength=CommandBuilder.VALID_COMMAND_HEADER_BYTES;
        //读到的每个字节
        byte b=0;
        //仅用于debug
        int j=0;
        //先读取有效命令头个字节
        for(int i=0,len=valideHeaderLength;i<len;){
            if(header[i]==(b=in.readByte())){
                //判断下一位
                i++;
                j++;
                Log.d(TAG, "读到第"+i+"个字节和命名头相等,值为:"+b);
            }else{
                //又从头开始
                i=0;
                j++;
                Log.d(TAG, "第"+j+"个不等，从头开始，值为:"+b);
            }
        }
        //命令头匹配，跳过其余的
        in.skipBytes(header.length-valideHeaderLength);
        return true;
    }
    /**
     *读取各种设备数据
     */
    public static KuboData read(DataReaderInputStream in)throws IOException {

        Log.d(TAG, "开始识别读取命令头");
        if (readCommandHeader(in)) {
            Log.d(TAG, "读到命名头");
        }
        Log.d(TAG, "开始读取识别码");
        short code = in.readShort();
        Log.d(TAG, "读到识别码"+code+"="+ CommandBuilder.getCommandName(code));
        //根据不同识别码，读取数据(已根据接收几率接收，如压力和温度值应是最多的)
        KuboData data = null;
        switch (code) {
            //接收压力和温度值
            case PRESSURE_TEMPERATURE:
                data = PressureTemperatureKuboData.from(in);
                break;
            //分析进度
            case PORT_ANALYZE_PROGRESS:
                data = AnalyzeProgressKuboData.from(in);
                break;
            //分析口参数
            case PORT_PARAMETER:
                data = PortParameterRunSetKuboData.from(in);
                break;
            //测试连接
            case CHECK_CONNECTION:
                data = CheckConnectionKuboData.from(in);
                break;
            //接收谱图数据
            case PORTS_SPECTRUM:
                data = SpectrumKuboData.from(in);
                break;
            //电磁阀状态
            case SOLENOIDVALVE_PFC:
                data = SolenoidValvePFCKuboData.from(in);
                break;
            //端口数量
            case PORT_COUNT:
                data = PortCountKuboData.from(in);
                break;
            default:
                Log.e(TAG,"未找到对应识别码"+code);
        }

        if(data!=null){
            data.code=code;
            Log.d(TAG,"读取完毕数据并开始下次读取<="+data.toString());
        }
        return data;
    }


}
