package com.liuyongmei.kubo.model.datamodel;

import java.io.DataInput;
import java.io.IOException;

/**
 * 电磁阀和PFC状态
 * Created by Administrator on 2017/3/25 0025.
 */

public class SolenoidValvePFCData extends  Data {
    //阀开关状态
    int openClose;
    //PFC百分比
    int pfc;
    public static SolenoidValvePFCData from(DataReaderInputStream in)throws IOException{
        SolenoidValvePFCData data=new SolenoidValvePFCData();
        data.openClose=in.readInt();
        data.pfc=in.readInt();
        return  data;
    }
}
