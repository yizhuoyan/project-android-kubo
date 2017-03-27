package com.liuyongmei.kubo.model.datamodel;

import android.util.Log;

import java.io.DataInput;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import static com.liuyongmei.kubo.model.CommandBuilder.CODE$CHECK_CONNECTION;
import static com.liuyongmei.kubo.model.CommandBuilder.COMMAND_HEADER;
import static com.liuyongmei.kubo.model.CommandBuilder.RECEIVE_CODE$ANALYZE_PROGRESS;
import static com.liuyongmei.kubo.model.CommandBuilder.RECEIVE_CODE$PORT_COUNT;
import static com.liuyongmei.kubo.model.CommandBuilder.RECEIVE_CODE$PORT_PARAMETER;
import static com.liuyongmei.kubo.model.CommandBuilder.RECEIVE_CODE$PRESSURE_TEMPERATURE;
import static com.liuyongmei.kubo.model.CommandBuilder.RECEIVE_CODE$SOLENOIDVALVE_PFC;
import static com.liuyongmei.kubo.model.CommandBuilder.RECEIVE_CODE$SPECTRUM;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public abstract class Data implements Serializable {
    private  static final String TAG=Data.class.getName();
    //命令头
    public final byte[] header;
    //识别码
    public short code;

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
        Log.d(TAG, "开始是否是命名头"+ Arrays.toString(header));
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
                Log.d(TAG, "不等，又从头开始");
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

        //根据不同识别码，读取数据
        switch (code) {
            //接收谱图数据
            case RECEIVE_CODE$SPECTRUM:
                data = SpectrumData.from(in);
                break;
            //接收压力和温度值
            case RECEIVE_CODE$PRESSURE_TEMPERATURE:
                //数据从40位开始，跳过4位
                in.skipBytes(4);
                data = NetPaData.from(in);
                break;
            //端口数量
            case RECEIVE_CODE$PORT_COUNT:
                data = PortCountData.from(in);
                break;
            //分析进度
            case RECEIVE_CODE$ANALYZE_PROGRESS:
                data = AnalyzeProgressData.from(in);
                break;
            //电磁阀状态
            case RECEIVE_CODE$SOLENOIDVALVE_PFC:
                data = SolenoidValvePFCData.from(in);
                break;
            //分析口参数
            case RECEIVE_CODE$PORT_PARAMETER:
                data = NetRunSetData.from(in);
                break;
            //测试连接
            case CODE$CHECK_CONNECTION:
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
