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

public abstract class Data extends SyncMessage implements Serializable {
    private  static final String TAG=Data.class.getName();
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
    //命令头
    public final byte[] header;

    public Data() {
        this.header = COMMAND_HEADER;
    }
    /**
     * 是否是头数据
     * @param
     * @return
     */
    private static boolean readCommandHeader(DataInput in)throws IOException{
        byte[] header=COMMAND_HEADER;
        Log.d(TAG, "开始识别命名头");
        for(int i=0,len=header.length;i<len;){
            Log.d(TAG, "开始读取第"+i+"个字节");
            //跳过不是
            if(header[i]==in.readByte()){
                //判断下一位
                i++;
                Log.d(TAG, "读到一个字节和命名头相等");
            }else{
                //又从头开始
                i=0;
                Log.d(TAG, "不等，从头开始");
            }
        }
        return true;
    }
    public static Data read(DataReaderInputStream in)throws IOException {
        Data data = null;
        //读取命令头和识别码
        Log.d(TAG, "开始读取数据");
        if (readCommandHeader(in)) {
            Log.d(TAG, "读到命名头");
        }

        //读取识别码
        Log.d(TAG, "开始读取识别码");
        short code = in.readShort();
        Log.d(TAG, "读到识别码"+code+"="+ CommandBuilder.getCommandName(code));
        //根据不同识别码，读取数据
        switch (code) {
            //接收谱图数据
            case PORTS_SPECTRUM:
                data = SpectrumData.from(in);
                break;
            //接收压力和温度值
            case PRESSURE_TEMPERATURE:
                //数据从40位开始，跳过4位
                in.skipBytes(4);
                data = NetPaData.from(in);
                break;
            //端口数量
            case PORT_COUNT:
                data = PortCountData.from(in);
                break;
            //分析进度
            case PORT_ANALYZE_PROGRESS:
                data = AnalyzeProgressData.from(in);
                break;
            //电磁阀状态
            case SOLENOIDVALVE_PFC:
                data = SolenoidValvePFCData.from(in);
                break;
            //分析口参数
            case PORT_PARAMETER:
                data = PortParameterRunSetData.from(in);
                break;
            //测试连接
            case CHECK_CONNECTION:
                data = CheckConnectionData.from(in);
                break;
            default:
                Log.e(TAG,"未找到对应识别码"+code);
        }
        if(data!=null){
            data.code=code;
        }
        return data;
    }


}
