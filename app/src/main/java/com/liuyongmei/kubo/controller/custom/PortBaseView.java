package com.liuyongmei.kubo.controller.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.DataReader;
import com.liuyongmei.kubo.model.datamodel.AnalyzeProgressData;
import com.liuyongmei.kubo.model.datamodel.Data;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PortBaseView extends LinearLayout implements DataReader.Callback {

    private static final String TAG = PortBaseView.class.getName();

    private TextView anaType, anaTypeValue;
    private TextView spName, spNameValue;
    private TextView spWeight, spWeightValue;
    private TextView spDryTemp, spDryTempValue;
    private TextView spDryTime, spDryTimeValue;
    private TextView spPerson, spPersonValue;
    private TextView spSource, spSourceValue;


    public PortBaseView(Context context) {
        super(context);
        init(this);
    }

    public PortBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(this);
    }

    public PortBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(this);
    }

    //    private void init(){
//        AppService.getInstance().addReceiveDataListener(CommandBuilder.RECEIVE_CODE$PORT_PARAMETER,this);
//    }
    private void init(View v) {
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

    }

}
