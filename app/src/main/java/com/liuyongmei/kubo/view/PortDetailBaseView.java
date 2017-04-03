package com.liuyongmei.kubo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.PortParameterRunSetKuboData;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PortDetailBaseView extends LinearLayout implements SyncMessageListener {

    private static final String TAG = PortDetailBaseView.class.getName();
    private static final String LOADING_TEXT="加载中...";
    private TextView  anaTypeValue;
    private TextView  spNameValue;
    private TextView  spWeightValue;
    private TextView  spDryTempValue;
    private TextView  spDryTimeValue;
    private TextView  spPersonValue;
    private TextView  spSourceValue;


    public PortDetailBaseView(Context context) {
        super(context);
    }

    public PortDetailBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortDetailBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.anaTypeValue= (TextView) findViewById(R.id.anaTypeValue);
        this.spNameValue= (TextView) findViewById(R.id.spNameValue);
        this.spDryTempValue= (TextView) findViewById(R.id.spDryTempValue);
        this.spDryTimeValue=(TextView) findViewById(R.id.spDryTimeValue);
        this.spPersonValue=(TextView) findViewById(R.id.spPersonValue);
        this.spSourceValue= (TextView) findViewById(R.id.spSourceValue);
        this.spWeightValue= (TextView) findViewById(R.id.spWeightValue);
    }

    private void reset(){
        anaTypeValue.setText(LOADING_TEXT);
        spPersonValue.setText(LOADING_TEXT);
        spSourceValue.setText(LOADING_TEXT);
        spNameValue.setText(LOADING_TEXT);
        spDryTempValue.setText(LOADING_TEXT);
        spDryTimeValue.setText(LOADING_TEXT);
        spWeightValue.setText(LOADING_TEXT);
    }
    @Override
    public void onReceive(final SyncMessage message) {
                if (message instanceof PortParameterRunSetKuboData) {
                    PortParameterRunSetKuboData runSetData = (PortParameterRunSetKuboData) message;
                    upateView(runSetData);
                }

    }
    public void switchView(int port){
        this.reset();
        AppService.getInstance().sendPortParameterSetCommand(port);
    }
    /**
     * 更新进度值
     *
     */
    public void upateView(final PortParameterRunSetKuboData data) {
        this.post(new Runnable() {
            @Override
            public void run() {
                //更新视图
                anaTypeValue.setText(String.valueOf(data.anaType));
                spPersonValue.setText(new String(data.spPerson));
                spSourceValue.setText(new String(data.spPerson));
                spNameValue.setText(new String(data.spName));
                spDryTempValue.setText(String.valueOf(data.spDryTemp));
                spDryTimeValue.setText(data.getSpDryTimeString());
                spWeightValue.setText(String.valueOf(data.spWeight));
            }
        });
    }


}
