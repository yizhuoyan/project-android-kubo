package com.liuyongmei.kubo.controller.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.CommandBuilder;
import com.liuyongmei.kubo.model.DataReader;
import com.liuyongmei.kubo.model.datamodel.AnalyzeProgressData;
import com.liuyongmei.kubo.model.datamodel.Data;
import com.liuyongmei.kubo.model.datamodel.PortCountData;
import com.liuyongmei.kubo.model.datamodel.SpectrumData;

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

public class PortDetailView extends ScrollView implements DataReader.Callback {
    private static final String TAG = PortDetailView.class.getName();
    //第一个进度条
    private ProgressBar progress1;
    // 第一个进度条中的文字控件，用于修改文字内容，与字体颜色
    private TextView progress1Name, progress1Arrow, progress1Result;
    //第二个进度条
    private ProgressBar progress2;
    //第二个进度条中的文字控件，用于修改文字内容，与字体颜色
    private TextView progress2Name, progress2Arrow, progress2Result;
    //第三个进度条
    private ProgressBar progress3;
    //第三个进度条中的文字控件，用于修改文字内容，与字体颜色
    private TextView progress3Name, progress3Arrow, progress3Result;
    // 第四个进度条
    private ProgressBar progress4;
    // 第四个进度条中的文字控件，用于修改文字内容，与字体颜色
    private TextView progress4Name;

    private LineChartView chartView;

    public PortDetailView(Context context) {
        super(context);
        init(this);
    }

    public PortDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(this);
    }

    public PortDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(this);
    }

    //    private void init(){
//        AppService.getInstance().addReceiveDataListener(CommandBuilder.RECEIVE_CODE$PORT_PARAMETER,this);
//    }
    private void init(View v) {
        chartView = (LineChartView) v.findViewById(R.id.chart);
        progress1 = (ProgressBar) v.findViewById(R.id.progess_1);
        progress1Name = (TextView) v.findViewById(R.id.progress_1_name);
        progress1Arrow = (TextView) v.findViewById(R.id.progress_1_arrow);
        progress1Result = (TextView) v.findViewById(R.id.progress_1_result);

        progress2 = (ProgressBar) v.findViewById(R.id.progess_2);
        progress2Name = (TextView) v.findViewById(R.id.progress_2_name);
        progress2Arrow = (TextView) v.findViewById(R.id.progress_2_arrow);
        progress2Result = (TextView) v.findViewById(R.id.progress_2_result);

        progress3 = (ProgressBar) v.findViewById(R.id.progess_3);
        progress3Name = (TextView) v.findViewById(R.id.progress_3_name);
        progress3Arrow = (TextView) v.findViewById(R.id.progress_3_arrow);
        progress3Result = (TextView) v.findViewById(R.id.progress_3_result);

        progress4 = (ProgressBar) v.findViewById(R.id.progess_4);
    }

    @Override
    public void onReceive(final Data data) {
        this.post(new Runnable() {
            @Override
            public void run() {
                Log.d("xxx", data.toString());
                if (data instanceof AnalyzeProgressData) {
                    AnalyzeProgressData analyzeProgressData = (AnalyzeProgressData) data;
                    upateView(analyzeProgressData);
                }
            }
        });

    }

    /**
     * 更新进度值
     *
     * @param analyzeProgressData
     */
    public void upateView(AnalyzeProgressData analyzeProgressData) {
        byte step = analyzeProgressData.progressNumber;
        float progress = analyzeProgressData.progress;
        int progressInt = (int) progress * 100;
        switch (step) {
            case 3:
                this.progress1.setProgress(100);
                this.progress1Result.setText("@+id/finished");
                this.progress2.setProgress(100);
                this.progress2Result.setText("@+id/finished");
                this.progress3.setProgress(100);
                this.progress3Result.setText("@+id/finished");
                this.progress4.setProgress(progressInt);
                break;
            case 2:
                this.progress1.setProgress(100);
                this.progress1Result.setText("@+id/finished");
                this.progress2.setProgress(100);
                this.progress2Result.setText("@+id/finished");
                this.progress3.setProgress(progressInt);
                this.progress3Result.setText("解析");
                this.progress4.setProgress(100);
                break;
            case 1:
                this.progress1.setProgress(100);
                this.progress1Result.setText("@+id/finished");
                this.progress2.setProgress(progressInt);
                this.progress2Result.setText("解析");
                this.progress3.setProgress(0);
                this.progress3Result.setText("待解析");
                this.progress4.setProgress(0);
                break;
            case 0:
                this.progress1.setProgress(progressInt);
                this.progress1Result.setText("解析");
                this.progress2.setProgress(0);
                this.progress2Result.setText("待解析");
                this.progress3.setProgress(0);
                this.progress3Result.setText("待解析");
                this.progress4.setProgress(0);
                break;
            default:
                break;
        }
    }

}
