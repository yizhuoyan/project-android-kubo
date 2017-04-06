package com.liuyongmei.kubo.model.datamodel;

import android.util.Log;

import java.io.IOException;
import java.text.NumberFormat;

/**
 * 电磁阀和PFC状态
 * Created by Administrator on 2017/3/25 0025.
 */

public class SolenoidValvePFCKuboData extends KuboData {
    //阀开关状态
    int allValveStatus;
    //PFC百分比
    int pfc;
    //工具数字格式话对象
    private static final NumberFormat NUMBER_FORMAT=NumberFormat.getNumberInstance();
    public static SolenoidValvePFCKuboData from(DataReaderInputStream in)throws IOException{
        SolenoidValvePFCKuboData data=new SolenoidValvePFCKuboData();
        data.allValveStatus=in.readInt();
        data.pfc=in.readInt();
        return  data;
    }
    private static String formatNumber(float v,int digits){
        NumberFormat f=NUMBER_FORMAT;
        //保留digits位小数
        f.setMaximumFractionDigits(digits);
        f.setMinimumFractionDigits(digits);

        return f.format(v);
    }

    /**
     * 获取阀门开关状态
     * @param valve 阀门号，从0开始
     * @return
     */
    public boolean isValveOpen(int valve){
        return (this.allValveStatus&(1<<valve))>0;
    }
    public String getPFCShowValue(){
        return formatNumber(pfc/10,0)+"%";
    }

    @Override
    public String toString() {
        return "SolenoidValvePFCKuboData{" +
                "allValveStatus=" + allValveStatus +
                ", pfc=" + pfc +
                '}';
    }
}
