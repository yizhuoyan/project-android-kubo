package com.liuyongmei.kubo.model.datamodel;

import java.io.IOException;
import java.util.Arrays;

/**
 * 分析进度
 * @author valentina
 *
 */
public class AnalyzeProgressKuboData extends KuboData {

	private static final long serialVersionUID = 8024574013208791153L;
    //0-未开始分析，1-真空校准，2-测量死体积，3-测定样品，4-结束分析
	public int step; //进度号
    //4个端口进度进度(百分比)
	public byte[] progress=new byte[4];

	public  static AnalyzeProgressKuboData from(DataReaderInputStream in)throws IOException{
        AnalyzeProgressKuboData data=new AnalyzeProgressKuboData();
		data.step =in.readByte();
		in.read(data.progress);
		in.skipBytes(3);
        return data;
	}
	public int getProgress(int no){
		return this.progress[no];
	}
	public String getShowProgress(int no){
		byte p=this.progress[no];
		return p+"%";
	}
	@Override
	public String toString() {
		return "AnalyzeProgressKuboData [step=" + step + ", progress=" + Arrays.toString(progress) + "]";
	}
	
	
	
}
