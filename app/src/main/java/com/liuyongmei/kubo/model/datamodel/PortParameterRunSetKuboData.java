package com.liuyongmei.kubo.model.datamodel;

import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
//分析口参数
public class PortParameterRunSetKuboData extends KuboData {
	public  static final long serialVersionUID = 2141085568024215041L;
    //分析端口号
    public int port;

	public  int	anaType;	//分析类型

	public  char[] spName=new char[50];			//样品名称 TCHAR [50]
	public  float  spWeight;			//样品重量 float
	public  int	spDryTemp;			//样品烘干温度 int
	public  int[]	spDryTime=new int[2];		//样品烘干时长   [0]-时，[1]-分
	public  char[]	spPerson=new char[20];		//样品检测员 TCHAR [20]
	public  char[]	spSource=new char[50];		//样品来源 TCHAR [50]
	
	
	public  char[]	spRemark=new char[200];		//样品备注 TCHAR [200]
	
	public  char[]	adsorbate=new char[30];		//吸附质 TCHAR [30]
	public  float	adsorbateArea;		//吸附质面积/毫升 float

	public  long	timeVacuum;				//真空脱气时间[分] int
	public  int		timeAdsorptionBalance;	//吸附平衡时间[秒] int
	public  int		timeDesorptionBalance;	//解吸平衡时间[秒] int
	public  int		timeIsDynamicBalance;	//是否为动态调节平衡时间模式 int
	public  float	timeSlope;				//动态平衡斜率 float
	public  int		timeFirstAdsorption;	//首次吸附时间[秒] int
	public  int		timeThermalExpansion;	//热膨胀平衡时间[秒] int

	public  float[]	measurRange=new float[2];			//分析范围(P/Po) float [1] 0-吸附，1-脱附
	public  int		measurIsDesorption;		//是否脱附分析 int
	public  int		measurIsExColdWells;	//是否使用外部冷井 int
	public  int	measurIsHotWater;		//加热脱气处理
	public  int		measurIsAutoLocate;		//是否线性校准 int
	public  float[]	lineCalibRange=new float[2];		//BET线性校准范围 float [2]
	public  int		isTestDeadV;			//是否测量死体积 int

	public  int		pgvIsDynamic;				//是否等间距调节递进压力 int
	public  float	pgvPPo;						//等间距P/Po float
	public  int		pgvQuantity;				//递进压力数量 int
	public  float[]	pgvPressureRange=new float[30];		//递进压力范围(P/Po) float [30]
	public  float[]	pgvPressureValueAds=new float[30];	//吸附递进压力值(Pa) float [30]
	public  float[]	pgvPressureValueDes=new float[30];	//解吸递进压力值(Pa) float [30]

	public  char[]	fileName=new char[200];		//用于保存的文件名前缀 TCHAR [200]

	public  char[]	isoPntCHAR=new char[2];		//等温线字符 TCHAR [2]

	public String getSpDryTimeString(){
		int[] t=this.spDryTime;
		if(t==null||t.length==0)return "";
		StringBuilder result=new StringBuilder();
		if(t[0]!=0){
			result.append(t[0]).append("小时");
		}
		result.append(t[1]).append("分钟");
		return result.toString();
	}
    public static PortParameterRunSetKuboData from(DataReaderInputStream in)throws IOException{

        PortParameterRunSetKuboData data = new PortParameterRunSetKuboData();
		//分析端口号
        data.port=in.readInt();
        //跳过4个字节
        in.skipBytes(4);
        //分析类型
        data.anaType=in.readIntReverse();
        //样品名称
        in.readCharsReverse(data.spName);
        //样品重量 float
        data.spWeight=in.readFloatReverse();

        //样品烘干温度 int
        data.spDryTemp=in.readIntReverse();

        //样品烘干时长 int [2]  [0]-时，[1]-分
        data.spDryTime[0]=in.readIntReverse();
        data.spDryTime[1]=in.readIntReverse();
        //样品检测员 TCHAR [20]
        in.readCharsReverse(data.spPerson);
        //样品来源 TCHAR [50]
        in.readCharsReverse(data.spSource);
		//样品备注 TCHAR [200]
		in.readCharsReverse(data.spRemark);
        //吸附质 TCHAR [30]
        in.readCharsReverse(data.adsorbate);
        //吸附质面积/毫升 float
        data.adsorbateArea=in.readFloatReverse();
        //真空脱气时间[分] int
        data.timeVacuum=in.readIntReverse();
        //吸附平衡时间[秒] int
        data.timeAdsorptionBalance=in.readIntReverse();
        //解吸平衡时间[秒] int
        data.timeDesorptionBalance=in.readIntReverse();
        //是否为动态调节平衡时间模式 int
        data.timeIsDynamicBalance=in.readIntReverse();
        //动态平衡斜率 float
        data.timeSlope=in.readFloatReverse();
        //首次吸附时间[秒] int
        data.timeFirstAdsorption=in.readIntReverse();
        //热膨胀平衡时间[秒] int
        data.timeThermalExpansion=in.readIntReverse();
        //分析范围(P/Po) float [1] 0-吸附，1-脱附
        data.measurRange[0]=in.readFloatReverse();
        data.measurRange[1]=in.readFloatReverse();
        //是否脱附分析 int
        data.measurIsDesorption=in.readIntReverse();
        //是否使用外部冷井 int
        data.measurIsExColdWells=in.readIntReverse();
        //加热脱气处理
        data.measurIsHotWater=in.readIntReverse();
        //是否线性校准 int
        data.measurIsAutoLocate=in.readIntReverse();
        //BET线性校准范围 float [2]
        data.lineCalibRange[0]=in.readFloatReverse();
        data.lineCalibRange[1]=in.readFloatReverse();
        //是否测量死体积 int
        data.isTestDeadV=in.readIntReverse();
        //是否等间距调节递进压力 int
        data.pgvIsDynamic=in.readIntReverse();
        //等间距P/Po float
        data.pgvPPo=in.readFloatReverse();
        //递进压力数量 int
        data.pgvQuantity=in.readIntReverse();
        //递进压力范围(P/Po) float [30]
        in.readFloatsReverse(data.pgvPressureRange);
        //吸附递进压力值(Pa) float [30]
        in.readFloatsReverse(data.pgvPressureValueAds);
        //解吸递进压力值(Pa) float [30]
        in.readFloatsReverse(data.pgvPressureValueDes);
        //用于保存的文件名前缀 TCHAR [200]
        in.readCharsReverse(data.fileName);
        //等温线字符 TCHAR [2]
        in.readCharsReverse(data.isoPntCHAR);
        return data;
    }

	@Override
	public String toString() {
		return "PortParameterRunSetKuboData [anaType=" + anaType + ", spName="
				+ new String(spName) + ", spWeight=" + spWeight
				+ ", spDryTemp=" + spDryTemp + ", spDryTime="
				+ Arrays.toString(spDryTime) + ", spPerson="
				+ new String(spPerson) + ", spSource="
				+ new String(spSource) + ", spRemark="
				+ new String(spRemark) + ", adsorbate="
				+ new String(adsorbate) + ", adsorbateArea="
				+ adsorbateArea + ", timeVacuum=" + timeVacuum
				+ ", timeAdsorptionBalance=" + timeAdsorptionBalance
				+ ", timeDesorptionBalance=" + timeDesorptionBalance
				+ ", timeIsDynamicBalance=" + timeIsDynamicBalance
				+ ", timeSlope=" + timeSlope + ", timeFirstAdsorption="
				+ timeFirstAdsorption + ", timeThermalExpansion="
				+ timeThermalExpansion + ", measurRange="
				+ Arrays.toString(measurRange) + ", measurIsDesorption="
				+ measurIsDesorption + ", measurIsExColdWells="
				+ measurIsExColdWells + ", measurIsHotWater="
				+ measurIsHotWater + ", measurIsAutoLocate="
				+ measurIsAutoLocate + ", lineCalibRange="
				+ Arrays.toString(lineCalibRange) + ", isTestDeadV="
				+ isTestDeadV + ", pgvIsDynamic=" + pgvIsDynamic + ", pgvPPo="
				+ pgvPPo + ", pgvQuantity=" + pgvQuantity
				+ ", pgvPressureRange=" + Arrays.toString(pgvPressureRange)
				+ ", pgvPressureValueAds="
				+ Arrays.toString(pgvPressureValueAds)
				+ ", pgvPressureValueDes="
				+ Arrays.toString(pgvPressureValueDes) + ", fileName="
				+ new String(fileName) + ", isoPntCHAR="
				+ new String(isoPntCHAR) + "]";
	}
	
}
