package com.liuyongmei.kubo.model.datamodel;

import java.io.IOException;

/**
 * 分析进度
 * @author valentina
 *
 */
public class AnalyzeProgressKuboData extends KuboData {

	private static final long serialVersionUID = 8024574013208791153L;
    //0-未开始分析，1-真空校准，2-测量死体积，3-测定样品，4-结束分析
	public byte progressNumber; //进度号
    //TODO:  进度号常量
	public float progress; //进度(百分比)

	public  static AnalyzeProgressKuboData from(DataReaderInputStream in)throws IOException{
        AnalyzeProgressKuboData data=new AnalyzeProgressKuboData();
		data.progressNumber =in.readByte();
		data.progress = in.readFloatReverse();
        return data;
	}
	@Override
	public String toString() {
		return "AnalyzeProgressKuboData [progressNumber=" + progressNumber + ", progress=" + progress + "]";
	}
	
	
	
}
