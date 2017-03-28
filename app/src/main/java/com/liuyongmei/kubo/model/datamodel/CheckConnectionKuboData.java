package com.liuyongmei.kubo.model.datamodel;

import java.io.DataInput;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class CheckConnectionKuboData extends KuboData {
    public static CheckConnectionKuboData from(DataInput in)throws IOException{
        CheckConnectionKuboData data=new CheckConnectionKuboData();
        //跳过8个0,筹够40
        in.skipBytes(8);
        return data;
    }
}
