package com.liuyongmei.kubo.controller.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.controller.custom.LoginAlertDialog;
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
 * Created by Administrator on 2017/3/27 0027.
 */

public class PortDetailFragment extends ScrollView implements DataReader.Callback {
    private static  final String TAG=PortDetailFragment.class.getName();
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

    public PortDetailFragment(Context context) {
        super(context);
    }

    public PortDetailFragment(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortDetailFragment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(View v){
        chartView=(LineChartView) v.findViewById(R.id.chart);
        progress1 = (ProgressBar)  v.findViewById(R.id.progess_1);
        progress1Name = (TextView) v. findViewById(R.id.progress_1_name);
        progress1Arrow = (TextView) v. findViewById(R.id.progress_1_arrow);
        progress1Result = (TextView)  v.findViewById(R.id.progress_1_result);

        progress2 = (ProgressBar)  v.findViewById(R.id.progess_2);
        progress2Name = (TextView)  v.findViewById(R.id.progress_2_name);
        progress2Arrow = (TextView) v. findViewById(R.id.progress_2_arrow);
        progress2Result = (TextView)  v.findViewById(R.id.progress_2_result);

        progress3 = (ProgressBar) v. findViewById(R.id.progess_3);
        progress3Name = (TextView) v. findViewById(R.id.progress_3_name);
        progress3Arrow = (TextView) v. findViewById(R.id.progress_3_arrow);
        progress3Result = (TextView)  v.findViewById(R.id.progress_3_result);

        progress4 = (ProgressBar) v. findViewById(R.id.progess_4);
        progress4Name = (TextView)  v.findViewById(R.id.finish);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.port_detail,container,false);

        return view;
    }
    @Override
    public void onReceive(final Data data) {
        this.getView().post(new Runnable() {
            @Override
            public void run() {
                //谱图数据
                if(data instanceof NetIsoData){
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
