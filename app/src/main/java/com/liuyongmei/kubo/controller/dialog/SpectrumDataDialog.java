package com.liuyongmei.kubo.controller.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.datamodel.SpectrumKuboData;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;

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
        this.setTitle("T 列表");
        this.setCanceledOnTouchOutside(true);
        View view=View.inflate(this.getContext(),R.layout.dialog_spectrum_table,null);
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
    public SpectrumListViewAdapter() {
        datas.add(new Float[]{1f,1f,1f});
    }
    public void setDatas(SpectrumKuboData data){

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
            convertView=View.inflate(parent.getContext(),R.layout.spectrum_list_item,null);
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
            noView.setText(String.valueOf(values[0].intValue()));
            pressureView.setText(String.valueOf(values[1]));
            volumeView.setText(String.valueOf(values[2]));
        }
        Log.d("xxx",convertView.toString());
        return convertView;

    }
}