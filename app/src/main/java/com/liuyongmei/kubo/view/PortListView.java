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
import com.liuyongmei.kubo.model.datamodel.PortCountKuboData;
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
    }

    public PortListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortListView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.context = (MainActivity) this.getContext();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d("xxx","onAttachedToWindow-portlist");
    }

    @Override
    public void onReceive(final SyncMessage m) {
        Log.d("xxx","portlist收到消息"+m);
        this.post(new Runnable() {
            @Override
            public void run() {
                if (m instanceof SpectrumKuboData) {
                    SpectrumKuboData spectrumData = (SpectrumKuboData) m;
                    View portView = createPortView(spectrumData.port);

                    //默认选择第一个
                    if (currentSelectedView == null) {
                        portView.performClick();
                    }
                } else if (m instanceof PortCountKuboData) {
                    PortCountKuboData countKuboData = (PortCountKuboData) m;
                    Log.d("count", "run: "+countKuboData.count);
                }
            }
        });
    }

    private View createPortView(int port) {
        View old=this.findViewWithTag(port);
        if(old!=null){//已存在，不重新创建
            return old;
        }
        Button portView = new Button(this.getContext());
        //portView.setId(port);
        portView.setTag(port);
        portView.setText(String.valueOf(port));
        portView.setBackgroundResource(R.drawable.btn_port_view);
        portView.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(-1, -2);
        //set the margin
        params.topMargin=10;
        params.bottomMargin=10;
        params.leftMargin=5;
        params.rightMargin=5;

        portView.setLayoutParams(params);
        portView.setTextColor(0xffffffff);
        portView.setTextSize(20);
        portView.setOnClickListener(this);
        addView(portView);
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
            //通知mainActivity更新视图
            this.context.switchView(port);
        }finally {
            v.setEnabled(true);
        }
    }
}
