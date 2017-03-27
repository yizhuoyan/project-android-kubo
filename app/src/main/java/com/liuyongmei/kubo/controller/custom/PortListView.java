package com.liuyongmei.kubo.controller.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.controller.activity.MainActivity;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.DataReader;
import com.liuyongmei.kubo.model.datamodel.Data;
import com.liuyongmei.kubo.model.datamodel.PortCountData;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class PortListView extends LinearLayout implements DataReader.Callback, View.OnClickListener {
    private static final String TAG=PortListView.class.getName();
    private MainActivity context;

    public PortListView(Context context) {
        super(context);

        initView();
    }

    public PortListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PortListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        this.context = (MainActivity) this.getContext();
        //发送分析端口数量请求
        AppService.getInstance().sendPortCountCommand(this);
        //默认8个端口
        PortCountData data=new PortCountData();
        data.count=8;
        onReceive(data);
    }

    @Override
    public void onReceive(final Data data) {
        Log.d(TAG,"得到数据通知:"+data);
        this.post(new Runnable() {
            @Override
            public void run() {
                //don't need requestLayout
                removeAllViewsInLayout();
                if (data instanceof PortCountData) {
                    PortCountData portCountData = (PortCountData) data;
                    int count = portCountData.count;
                    for (int i = 0; i < count; i++) {
                        addView(createPortView(i + 1));
                    }
                }
            }
        });
    }

    private View createPortView(int port) {
        Button portView = new Button(this.getContext());
        portView.setText(String.valueOf(port));
        portView.setBackgroundResource(R.drawable.port);
        portView.setGravity(Gravity.CENTER);
        portView.setLayoutParams(new LayoutParams(-1, -2));
        portView.setTag(port);
        portView.setOnClickListener(this);
        return portView;
    }

    @Override
    public void onClick(View v) {
        int port=(Integer) v.getTag();
        this.context.switchView(port);
    }
}
