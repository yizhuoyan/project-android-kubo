package com.liuyongmei.kubo.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.AnalyzeProgressKuboData;
import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PortDetailProgressView extends LinearLayout implements SyncMessageListener,View.OnClickListener,DialogInterface.OnClickListener {
    private static final String TAG = PortDetailProgressView.class.getName();
    //保存所有端口进度
   // private Map<Integer,PortProgressData> progressKuboDataMap=new HashMap<>(8,1);
    //当前端口号
    private volatile int currentPort=-1;
    /********************各种进度条************************/
    //4个进度条
    private ProgressBar progress1,progress2,progress3,progress4;
    // 4个进度条的文字进度
    private TextView progress1tv,progress2tv,progress3tv,progress4tv;
    //停止按钮
    private Button teminateBtn;
    private AnalyzeProgressKuboData lastProgressData;
    private AlertDialog teminateConfirmDialog;

    public PortDetailProgressView(Context context) {
        super(context);
    }

    public PortDetailProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortDetailProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progress1= (ProgressBar) findViewById(R.id.analyse_progress_step1);
        progress2= (ProgressBar) findViewById(R.id.analyse_progress_step2);
        progress3= (ProgressBar) findViewById(R.id.analyse_progress_step3);
        progress4= (ProgressBar) findViewById(R.id.analyse_progress_step4);

        progress1tv= (TextView) findViewById(R.id.analyse_progress_step1_value);
        progress2tv= (TextView) findViewById(R.id.analyse_progress_step2_value);
        progress3tv= (TextView) findViewById(R.id.analyse_progress_step3_value);
        progress4tv= (TextView) findViewById(R.id.analyse_progress_step4_value);
        teminateBtn= (Button) findViewById(R.id.teminateBtn);
        teminateBtn.setOnClickListener(this);
    }



    public void switchView(int port){
        currentPort=port;
        if(lastProgressData!=null){
            upateView(lastProgressData);
        }
    }
    @Override
    public void onReceive(final SyncMessage message) {
        this.post(new Runnable() {
            @Override
            public void run() {
                if (message instanceof AnalyzeProgressKuboData) {
                    AnalyzeProgressKuboData data = (AnalyzeProgressKuboData) message;
                    upateView(data);
                }
            }
        });
    }

    private void reset(){
        this.progress1.setProgress(0);
        this.progress2.setProgress(0);
        this.progress3.setProgress(0);
        this.progress4.setProgress(0);
        this.progress1tv.setText("未开始");
        this.progress2tv.setText("未开始");
        this.progress3tv.setText("未开始");
        this.progress4tv.setText("未开始");
        teminateBtn.setText(R.string.btn_teminated_analyse);
        teminateBtn.setEnabled(false);
    }

    /**
     * 更新进度值
     *
     * @param data
     */
    public void upateView(AnalyzeProgressKuboData data) {
        //还未选，则展示第一个
        if(currentPort==-1){
            currentPort=0;
        }
        int step = data.step;

        int progressInt = data.getProgress(currentPort);

        String progressText=progressInt+"%";
        if(step>1){
            this.progress1.setProgress(100);
            this.progress1tv.setText("已完成");
        }
        if(step>2){
            this.progress2.setProgress(100);
            this.progress2tv.setText("已完成");
        }
        if(step>3){
            this.progress3.setProgress(100);
            this.progress4tv.setText("已完成");
        }
        switch (step) {
            case 4://结束分析
                this.progress4.setProgress(progressInt);
                this.progress4tv.setText(progressText);
                if(progressInt>=100){
                    //分析完毕
                    this.analyseFinished();
                }
                break;
            case 3://测定样品
                this.progress3.setProgress(progressInt);
                this.progress3tv.setText(progressText);
                break;
            case 2://测量死体积
                this.progress2.setProgress(progressInt);
                this.progress2tv.setText(progressText);
                break;
            case 1://真空校准
                this.progress1.setProgress(progressInt);
                this.progress1tv.setText(progressText);
                teminateBtn.setText(R.string.btn_teminate_analyse);
                teminateBtn.setEnabled(true);
                break;
            case 0://未开始分析
                teminateBtn.setText(R.string.btn_teminated_analyse);
                teminateBtn.setEnabled(false);
                break;
        }
        lastProgressData=data;

    }
    private void analyseFinished(){
        this.reset();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teminateBtn:{
                showComfirmDialog();
                break;
            }
        }
    }
    private void showComfirmDialog(){
        if(teminateConfirmDialog==null){
            teminateConfirmDialog=new AlertDialog.Builder(this.getContext())
                    .setTitle("确认")
                    .setMessage(R.string.teminate_confirm_ask)
                    .setPositiveButton("确定",this)
                    .setNegativeButton("取消",this)
                    .create();
        }
        teminateConfirmDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case AlertDialog.BUTTON_POSITIVE:
                //发送终止指令
                AppService.getInstance().sendSpectrumData();
                this.reset();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                dialog.dismiss();
                break;
        }
    }
}
