package com.liuyongmei.kubo.model.datamodel;

import java.io.DataInput;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class CheckConnectionData extends  Data {
    public static CheckConnectionData from(DataInput in)throws IOException{
        CheckConnectionData data=new CheckConnectionData();
        return data;
    }
}
