package com.liuyongmei.kubo.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.controller.custom.LoginAlertDialog;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.CommandBuilder;
import com.liuyongmei.kubo.model.DataReader;
import com.liuyongmei.kubo.model.datamodel.Data;
import com.liuyongmei.kubo.model.datamodel.NetIsoData;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 */
public class MainActivity extends Activity implements OnClickListener,DataReader.Callback {
	private final static  String TAG=MainActivity.class.getName();
	//第一个进度条
	private ProgressBar progress1;
	// 第一个进度条中的文字控件，用于修改文字内容，与字体颜色
	private TextView progress1Name,progress1Arrow,progress1Result;
	//第二个进度条
	private ProgressBar progress2;
	//第二个进度条中的文字控件，用于修改文字内容，与字体颜色
	private TextView progress2Name,progress2Arrow,progress2Result;
	//第三个进度条
	private ProgressBar progress3;
	//第三个进度条中的文字控件，用于修改文字内容，与字体颜色
	private TextView progress3Name,progress3Arrow,progress3Result;
	// 第四个进度条
	private ProgressBar progress4;
	// 第四个进度条中的文字控件，用于修改文字内容，与字体颜色
	private TextView progress4Name;
	private LoginAlertDialog loginAlertDialog;

    private LineChartView chartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        AppService.getInstance().addReceiveDataListener(CommandBuilder.RECEIVE_CODE$SPECTRUM,this);
    }
    
    
    
    private void init(){
    	findViewById(R.id.login).setOnClickListener(this);
    	findViewById(R.id.last_page).setOnClickListener(this);
    	findViewById(R.id.next_page).setOnClickListener(this);
    	findViewById(R.id.stop).setOnClickListener(this);
    	findViewById(R.id.control).setOnClickListener(this);
        chartView=(LineChartView) findViewById(R.id.chart);

    	progress1 = (ProgressBar) findViewById(R.id.progess_1);
    	progress1Name = (TextView) findViewById(R.id.progress_1_name);
    	progress1Arrow = (TextView) findViewById(R.id.progress_1_arrow);
    	progress1Result = (TextView) findViewById(R.id.progress_1_result);
    	
    	progress2 = (ProgressBar) findViewById(R.id.progess_2);
    	progress2Name = (TextView) findViewById(R.id.progress_2_name);
    	progress2Arrow = (TextView) findViewById(R.id.progress_2_arrow);
    	progress2Result = (TextView) findViewById(R.id.progress_2_result);
    	
    	progress3 = (ProgressBar) findViewById(R.id.progess_3);
    	progress3Name = (TextView) findViewById(R.id.progress_3_name);
    	progress3Arrow = (TextView) findViewById(R.id.progress_3_arrow);
    	progress3Result = (TextView) findViewById(R.id.progress_3_result);
    	
    	progress4 = (ProgressBar) findViewById(R.id.progess_4);
    	progress4Name = (TextView) findViewById(R.id.finish);
    	
    }


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.login:

            this.showLoginPopup();
			break;
		case R.id.stop:
			// TODO 停止分析
			
			break;
		case R.id.control:
			Intent intent = new Intent(this, DetailActivity.class);
			startActivity(intent);
			break;
		case R.id.last_page:
			// TODO 上一页
			
			break;
		case R.id.next_page:
			// TODO 下一页
			
			break;
			
			
			
		}
	}
	protected void showLoginPopup(){
		if(this.loginAlertDialog ==null) {
			loginAlertDialog = new LoginAlertDialog(this);
		}

		loginAlertDialog.show();
	}
    @Override
    public void onReceive(final Data data) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //谱图数据
                if(data instanceof  NetIsoData){
                    updateChartView((NetIsoData)data);
                }
            }
        });
    }
    private void updateChartView(NetIsoData data){
        //ads
        float[] adsXAxis=data.adsPPo;
        float[] adsYAxis=data.adsVomume;

        List<PointValue> adsPoints = new ArrayList<PointValue>(adsXAxis.length);
        for (int i=0,len=data.adsMax;i<len;i++){
            adsPoints.add(new PointValue(adsXAxis[i], adsXAxis[i]));
        }
        Line adsLine = new Line(adsPoints).setColor(Color.BLUE).setStrokeWidth(2);
        //des
        float[] desXAxis=data.adsPPo;
        float[] desYAxis=data.adsVomume;
        List<PointValue> desPoints = new ArrayList<PointValue>(adsXAxis.length);
        for (int i=0,len=data.desMax;i<len;i++){
            desPoints.add(new PointValue(desXAxis[i], desYAxis[i]));
        }
        Line desLine = new Line(desPoints).setColor(Color.GREEN).setStrokeWidth(1);
        List<Line> lines = new ArrayList<Line>(2);
        lines.add(adsLine);
        lines.add(desLine);

        //构建图数据
        LineChartData chartData = new LineChartData();
        //放入折线数据
        chartData.setLines(lines);
        //设置x轴
        Axis xAxis=new Axis();
        xAxis.setName("Relative Pressure[P/PO]");
        List<AxisValue> xAxisValues=new ArrayList<>(10);
        float xValue=0;
        for(int i=0;i<=10;i++){
            xValue=i*0.1f;
            xAxisValues.add(new AxisValue(xValue).setLabel(xValue+""));
        }
        xAxis.setValues(xAxisValues);
        chartData.setAxisXBottom(xAxis);
        //设置y轴
        Axis yAxis=new Axis();
        yAxis.setName("Volumn[ml/g]");
        yAxis.setAutoGenerated(true);
        chartData.setAxisYLeft(yAxis);

        Log.d(TAG,"hahaha");
        //设置相关显示特性
        chartView.setLineChartData(chartData);
        chartView.setInteractive(true);
        chartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        chartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chartView.setVisibility(View.VISIBLE);
    }




    
   
	
    
    
    

}
