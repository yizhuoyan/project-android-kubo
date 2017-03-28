package com.liuyongmei.kubo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.AnalyzeProgressKuboData;
import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PortDetailView extends ScrollView implements SyncMessageListener {
    private static final String TAG = PortDetailView.class.getName();

    //折线图
    private LineChartView chartView;
    //端口参数
    private PortBaseView baseView;

    /********************各种进度条************************/
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
    //停止按钮
    private Button teminateBtn;

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

    private void init(View v) {
        //对分析进度数据感兴趣
        AppService.getInstance().addReceiveDataListener(KuboData.PORT_ANALYZE_PROGRESS,this);
        chartView = (LineChartView) v.findViewById(R.id.chart);
        baseView= (PortBaseView) v.findViewById(R.id.port_base);

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
        teminateBtn= (Button) v.findViewById(R.id.teminate);

    }

    @Override
    public void onReceive(final SyncMessage message) {
        this.post(new Runnable() {
            @Override
            public void run() {
                Log.d("xxx", message.toString());
                if (message instanceof AnalyzeProgressKuboData) {
                    AnalyzeProgressKuboData analyzeProgressData = (AnalyzeProgressKuboData) message;
                    upateView(analyzeProgressData);
                }
            }
        });

    }

    /**
     * 更新进度值
     *
     * @param data
     */
    public void upateView(AnalyzeProgressKuboData data) {
        byte step = data.progressNumber;
        float progress = data.progress;
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
