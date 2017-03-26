package com.liuyongmei.kubo.model.datamodel;

import java.io.DataInput;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class PortCountData extends  Data{
    //端口数量
    public int count;


    public static PortCountData from(DataReaderInputStream in)throws IOException{
        PortCountData data=new PortCountData();
        data.count=in.readInt();
        return data;

    }
}
