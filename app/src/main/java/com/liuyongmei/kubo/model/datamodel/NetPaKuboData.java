package com.liuyongmei.kubo.model.datamodel;

import java.io.IOException;
import java.util.Arrays;

/**
 * 压力和温度值
 *
 * @author lym
 */
public class NetPaKuboData extends KuboData {
    public static final long serialVersionUID = -5798825474478819416L;
    //对应气路图中Sen1,Sen3,Sen5的显示内容
    public float[] pa_H = new float[4];
    //对应气路图中Sen2,Sen4,Shen6的显示内容
    public float[] pa_L = new float[4];
    //对应气路图中SenPo的显示内容
    public float po;
    //对应气路图中SenM的显示内容
    public float pa_M;
    //对应气路图中控温温度的显示内容
    public float tmp;

    public static NetPaKuboData from(DataReaderInputStream in) throws IOException {
        NetPaKuboData data = new NetPaKuboData();
        //将值赋值到data.setPa_H()
        in.readFloatsReverse(data.pa_H);

        //将值赋值到data.setPa_L()
        in.readFloatsReverse(data.pa_L);
        data.pa_M = in.readFloatReverse();
        data.po = in.readFloatReverse();
        data.tmp = in.readFloatReverse();

        return data;
    }

    @Override
    public String toString() {
        return "NetPaDataVO [pa_H=" + Arrays.toString(pa_H) + ", pa_L="
                + Arrays.toString(pa_L) + ", po=" + po + ", pa_M=" + pa_M
                + ", tmp=" + tmp + "]";
    }

}
