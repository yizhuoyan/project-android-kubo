package com.liuyongmei.kubo.model.datamodel;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

/**
 * 压力和温度值
 *
 * @author lym
 */
public class PressureTemperatureKuboData extends KuboData {
    public static final long serialVersionUID = -5798825474478819416L;
    //对应端口号
    public int port;
    //对应气路图中Sen1,Sen3,Sen5的显示内容
    public float[] sen135 = new float[4];
    //对应气路图中Sen2,Sen4,Shen6的显示内容
    public float[] sen246 = new float[4];
    //对应气路图中SenPo的显示内容
    public float senPo;
    //对应气路图中SenM的显示内容
    public float senM;
    //对应气路图中控温温度的显示内容
    public float temperature;
    //工具数字格式话对象
    private static final NumberFormat NUMBER_FORMAT=NumberFormat.getNumberInstance();

    private static String formatNumber(float v,int digits){
        NumberFormat f=NUMBER_FORMAT;
        //保留3位小数
        f.setMaximumFractionDigits(digits);
        f.setMinimumFractionDigits(digits);

        return f.format(v);
    }

    public static PressureTemperatureKuboData from(DataReaderInputStream in) throws IOException {
        PressureTemperatureKuboData data = new PressureTemperatureKuboData();
        //port
        data.port=in.readInt();
        //跳过4个，到40位
        in.skipBytes(4);
        //将值赋值到data.setPa_H()
        in.readFloatsReverse(data.sen135);
        //将值赋值到data.setPa_L()
        in.readFloatsReverse(data.sen246);
        data.senPo = in.readFloatReverse();
        data.senM = in.readFloatReverse();
        data.temperature = in.readFloatReverse();

        return data;
    }
    public String getSenValue(int no){
        float f=0;
        switch (no){
            case 1:
            case 3:
            case 5:
                f=sen135[no/2];
                return formatSenKpaValue(f);
            case 2:
            case 4:
            case 6:
                f=sen246[(no/2)-1];
                return formatSenPaValue(f);
        }
        return "null";
    }

    public String getSenPoValue(){
        return formatSenKpaValue(this.senPo);
    }
    public String getSenMValue(){
        return formatSenKpaValue(this.senM);
    }

    private String formatSenKpaValue(float v){
        return formatNumber(v/1000,3)+"KPa";
    }
    private String formatSenPaValue(float v){
        return formatNumber(v,3)+"Pa";
    }
    public String getTempretureShowValue(){
        return formatNumber(this.temperature,2)+"℃";
    }

    @Override
    public String toString() {
        return "PressureTemperatureKuboData{" +
                "port=" + port +
                ", sen135=" + Arrays.toString(sen135) +
                ", sen246=" + Arrays.toString(sen246) +
                ", senPo=" + senPo +
                ", senM=" + senM +
                ", temperature=" + temperature +
                '}';
    }
}
