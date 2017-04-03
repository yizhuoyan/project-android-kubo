package com.liuyongmei.kubo.model.datamodel;

import java.io.IOException;

/**
 * 电磁阀和PFC状态
 * Created by Administrator on 2017/3/25 0025.
 */

public class SolenoidValvePFCKuboData extends KuboData {
    //阀开关状态
    int allValveStatus;
    //PFC百分比
    int pfc;
    public static SolenoidValvePFCKuboData from(DataReaderInputStream in)throws IOException{
        SolenoidValvePFCKuboData data=new SolenoidValvePFCKuboData();
        data.allValveStatus=in.readInt();
        data.pfc=in.readIntReverse();
        return  data;
    }

    /**
     * 获取阀门开关状态
     * @param valve 阀门号，从1开始
     * @return
     */
    public boolean isValveOpen(int valve){
        return (this.allValveStatus&(1<<(valve-1)))==1;
    }
    public String getPFCShowValue(){
        return String.valueOf(pfc)+"%";
    }

    @Override
    public String toString() {
        return "SolenoidValvePFCKuboData{" +
                "allValveStatus=" + allValveStatus +
                ", pfc=" + pfc +
                '}';
    }
}
