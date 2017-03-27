package com.liuyongmei.kubo.model.datamodel;

import java.io.IOException;
import java.util.Arrays;

/**
 * 谱图数据
 */
public class SpectrumData extends Data{

	public  static final long serialVersionUID = 694069136401119665L;

	public  int port; //端口
	
	public  int adsMax;				//吸附点数量，吸附点是图中蓝色的线
	public  float[] adsPPo=new float[1000];		//吸附点x值
	public  float[] adsVomume=new float[1000];	//吸附点y值
	public  int desMax;				//解吸点数量，解吸点是图中红色的线
	public  float[] desPPo=new float[1000];		//解吸点x值
	public  float[] desVomume=new float[1000];	//解吸点y值

	
	
	public static SpectrumData from(DataReaderInputStream in)throws IOException{
        SpectrumData data=new SpectrumData();
        //端口号，高位在前
        data.port=in.readInt();
        in.skipBytes(4);
        //吸附点数量,低位在前
        data.adsMax=in.readIntReverse();
        //吸附点x值
        in.readFloatsReverse(data.adsPPo);
        //吸附点y值
        in.readFloatsReverse(data.adsVomume);
        //解吸点数量,低位在前
        data.desMax=in.readIntReverse();
        //解吸点x值
        in.readFloatsReverse(data.desPPo);
        //解吸点y值
        in.readFloatsReverse(data.desVomume);
		return data;
	}
	
	
	@Override
	public String toString() {
		return "SpectrumData [port=" + port + ", adsMax=" + adsMax + ", adsPPo=" + Arrays.toString(adsPPo)
				+ ", adsVomume=" + Arrays.toString(adsVomume) + ", desMax=" + desMax + ", desPPo="
				+ Arrays.toString(desPPo) + ", desVomume=" + Arrays.toString(desVomume) + "]";
	}	
	
}
