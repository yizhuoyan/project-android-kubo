package com.liuyongmei.kubo.model.datamodel;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class PortCountKuboData extends KuboData {
    //端口数量
    public int count;


    public static PortCountKuboData from(DataReaderInputStream in)throws IOException{
        PortCountKuboData data=new PortCountKuboData();
        data.count=in.readInt();
        return data;

    }
}
