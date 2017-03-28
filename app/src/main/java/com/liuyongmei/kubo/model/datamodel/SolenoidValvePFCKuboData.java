package com.liuyongmei.kubo.model.datamodel;

import java.io.IOException;

/**
 * 电磁阀和PFC状态
 * Created by Administrator on 2017/3/25 0025.
 */

public class SolenoidValvePFCKuboData extends KuboData {
    //阀开关状态
    int openClose;
    //PFC百分比
    int pfc;
    public static SolenoidValvePFCKuboData from(DataReaderInputStream in)throws IOException{
        SolenoidValvePFCKuboData data=new SolenoidValvePFCKuboData();
        data.openClose=in.readInt();
        data.pfc=in.readInt();
        return  data;
    }
}
