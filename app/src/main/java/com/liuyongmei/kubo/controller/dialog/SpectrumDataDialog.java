package com.liuyongmei.kubo.controller.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.datamodel.SpectrumKuboData;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/6 0006.
 */

public class SpectrumDataDialog extends AlertDialog implements AlertDialog.OnClickListener {


    private ListView listView;

    private SpectrumListViewAdapter spectrumListViewAdapter;

    public SpectrumDataDialog(Context context) {
        super(context);
        init();
    }

    private void init(){
        this.setTitle("T 列表-吸附和解析");
        this.setCanceledOnTouchOutside(true);
        View view=View.inflate(this.getContext(),R.layout.dialog_t_list,null);
        this.setView(view);
        this.setButton(BUTTON_POSITIVE,"关闭",this);
        listView= (ListView)view.findViewById(R.id.spectrum_data_listview);
        spectrumListViewAdapter=new SpectrumListViewAdapter();
        listView.setAdapter(spectrumListViewAdapter);
    }
    public void show(SpectrumKuboData data){
        spectrumListViewAdapter.setDatas(data);
        this.show();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
}

class   SpectrumListViewAdapter extends BaseAdapter{
    private List<Float[]> datas=new ArrayList<>();
    private SpectrumKuboData useData;
    private static final NumberFormat NUMBER_FORMAT=NumberFormat.getNumberInstance();
    private static String formatNumber(float v,int digits){
        NumberFormat f=NUMBER_FORMAT;
        //保留digits位小数
        f.setMaximumFractionDigits(digits);
        f.setMinimumFractionDigits(digits);
        return f.format(v);
    }
    public SpectrumListViewAdapter() {

    }
    public void setDatas(SpectrumKuboData data){
        //the data no change,just show
        if(useData==data){
            return;
        }

        float[] adsXAxis = data.adsPPo;
        float[] adsYAxis = data.adsVomume;


        List<Float[]> listDatas =datas=new ArrayList<>(2000);

        for (int i = 0, len = data.adsMax; i < len; i++) {
            listDatas.add(new Float[]{new Float(i),adsXAxis[i],adsYAxis[i]});
        }
        //des
        float[] desXAxis = data.desPPo;
        float[] desYAxis = data.desVomume;
        listDatas.add(new Float[]{-1f,-1f,-1f});
        for (int i = 0, len = data.desMax; i < len; i++) {
            listDatas.add(new Float[]{new Float(i),desXAxis[i],desYAxis[i]});
        }
        useData=data;
        //notify invalidate the view
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(parent.getContext(),R.layout.item_t_list,null);
        }
        TextView noView= (TextView) convertView.findViewById(R.id.item_no);
        TextView pressureView = (TextView) convertView.findViewById(R.id.item_pressure);
        TextView volumeView = (TextView) convertView.findViewById(R.id.item_volume);
        Float[] values=this.datas.get(position);
        if(values[0]==-1){
            //插入空行
            noView.setText("");
            pressureView.setText("");
            volumeView.setText("");
        }else {
            noView.setText(String.valueOf(values[0].intValue()+1));
            pressureView.setText(formatNumber(values[1],8));
            volumeView.setText(formatNumber(values[2],8));
        }
        Log.d("xxx",convertView.toString());
        return convertView;

    }
}