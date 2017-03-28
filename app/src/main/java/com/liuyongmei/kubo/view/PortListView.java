package com.liuyongmei.kubo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.controller.activity.MainActivity;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.SpectrumKuboData;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PortListView extends LinearLayout implements SyncMessageListener, View.OnClickListener {
    private static final String TAG=PortListView.class.getName();
    private MainActivity context;
    private View currentSelectedView;

    public PortListView(Context context) {
        super(context);

        initView();
    }

    public PortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PortListView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        this.context = (MainActivity) this.getContext();
        //也对谱图数据感兴趣
        AppService.getInstance().addReceiveDataListener(KuboData.PORTS_SPECTRUM, this);
        //发送分析端口数量请求,没意义啊，直接从谱图数据中找出端口
        //AppService.getInstance().sendPortCountCommand(this);
    }

    @Override
    public void onReceive(final SyncMessage m) {
        Log.d(TAG,"PortlistView得到数据通知:"+m);
        this.post(new Runnable() {
            @Override
            public void run() {
                if (m instanceof SpectrumKuboData) {
                    SpectrumKuboData spectrumData = (SpectrumKuboData) m;
                    View portView=createPortView(spectrumData.port);
                    addView(portView);
                    //默认选择第一个
                    if(currentSelectedView==null){
                        portView.performClick();
                    }
                }
            }
        });
    }

    private View createPortView(int port) {
        Button portView = new Button(this.getContext());
        portView.setText(String.valueOf(port));
        portView.setBackgroundResource(R.drawable.btn_port_view);
        portView.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(-1, -2);
        //set the margin
        params.topMargin=10;
        params.bottomMargin=10;
        params.leftMargin=5;
        params.rightMargin=5;

        portView.setTextColor(0xffffffff);
        portView.setTextSize(20);

        portView.setLayoutParams(params);
        portView.setTag(port);
        portView.setOnClickListener(this);
        return portView;
    }

    @Override
    public void onClick(View v) {
        try {
            //防止重复点击
            if(v==currentSelectedView)return;
            if(currentSelectedView!=null){
                currentSelectedView.setEnabled(true);
                currentSelectedView.setSelected(false);
            }
            currentSelectedView=v;
            v.setEnabled(false);
            v.setSelected(true);
            int port = (Integer) v.getTag();
            //发出请求端口参数命令
            AppService.getInstance().sendPortParameterSetCommand(port);
            //发出开启端口分析命令
            //通知mainActivity更新视图
            this.context.switchView(port);
        }finally {
            v.setEnabled(true);
        }
    }
}
