package com.liuyongmei.kubo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.PortParameterRunSetKuboData;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PortBaseView extends LinearLayout implements SyncMessageListener {

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

    private void init(View v) {
        //對端口参数数据感兴趣
        AppService.getInstance().addReceiveDataListener(KuboData.PORT_PARAMETER,this);
        //TODO:找到所有需要更新的组件(定义成员变量)
    }

    @Override
    public void onReceive(final SyncMessage message) {
                if (message instanceof PortParameterRunSetKuboData) {
                    PortParameterRunSetKuboData runSetData = (PortParameterRunSetKuboData) message;
                    upateView(runSetData);
                }

    }

    /**
     * 更新进度值
     *
     */
    public void upateView(final PortParameterRunSetKuboData data) {
        this.post(new Runnable() {
            @Override
            public void run() {
                //TODO:更新视图
            }
        });
    }

}
