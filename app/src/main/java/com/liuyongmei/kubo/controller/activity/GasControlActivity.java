
package com.liuyongmei.kubo.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.liuyongmei.kubo.R;
import com.liuyongmei.kubo.model.AppService;
import com.liuyongmei.kubo.model.SyncMessageListener;
import com.liuyongmei.kubo.model.datamodel.KuboData;
import com.liuyongmei.kubo.model.datamodel.PressureTemperatureKuboData;
import com.liuyongmei.kubo.model.datamodel.SolenoidValvePFCKuboData;
import com.liuyongmei.kubo.model.datamodel.SyncMessage;


public class GasControlActivity extends Activity implements SyncMessageListener {
	//所有阀门view,共32个，没有则为空
	private TextView[] valveViews;
	//PFC value view
	private TextView pfcValueView;
	//sen123456
	private TextView sen1View,sen2View,sen3View,sen4View,sen5View,sen6View;
	//senpo
	private TextView senPoView;
	//senM
	private TextView senMView;
	//temperature
	private TextView temperatureView;


	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gascontrol);

		init();
	}
	private void init(){
		this.senMView= (TextView) findViewById(R.id.gas_senm);
		this.senPoView= (TextView) findViewById(R.id.gas_senPo);
		this.sen1View= (TextView) findViewById(R.id.gas_sen1);
		this.sen2View= (TextView) findViewById(R.id.gas_sen2);
		this.sen3View= (TextView) findViewById(R.id.gas_sen3);
		this.sen4View= (TextView) findViewById(R.id.gas_sen4);
		this.sen5View= (TextView) findViewById(R.id.gas_sen5);
		this.sen6View= (TextView) findViewById(R.id.gas_sen6);
		this.temperatureView= (TextView) findViewById(R.id.gas_temperatureValue);
		this.pfcValueView =(TextView) findViewById(R.id.gas_pfc);

		valveViews=new TextView[32];
		valveViews[0]=(TextView) findViewById(R.id.gas_valve1);
		valveViews[1]=(TextView) findViewById(R.id.gas_valve2);
		valveViews[2]=(TextView) findViewById(R.id.gas_valve3);
		valveViews[14]=(TextView) findViewById(R.id.gas_valve15);
		valveViews[15]=(TextView) findViewById(R.id.gas_valve16);
		valveViews[16]=(TextView) findViewById(R.id.gas_valve17);
		valveViews[17]=(TextView) findViewById(R.id.gas_valve18);
		valveViews[18]=(TextView) findViewById(R.id.gas_valve19);
		valveViews[19]=(TextView) findViewById(R.id.gas_valve20);


	}

	@Override
	public void onReceive(SyncMessage message) {
		if(message instanceof SolenoidValvePFCKuboData){
			final SolenoidValvePFCKuboData valveData= (SolenoidValvePFCKuboData) message;
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updateValvesView(valveData);
				}
			});

		}else if(message instanceof PressureTemperatureKuboData){
			final PressureTemperatureKuboData ptData= (PressureTemperatureKuboData) message;
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updtePressureTemperatrueView(ptData);
				}
			});
		}
	}

	/**
	 * 更新阀门相关值或状态
	 */
	private void updateValvesView(SolenoidValvePFCKuboData data){
		//更新阀门开关状态
		TextView[] vs=this.valveViews;
		TextView v;
		for (int i=0,len=vs.length;i<len;i++){
			if((v=vs[i])!=null){
				if(data.isValveOpen(i)){
					v.setBackgroundResource(R.color.gas_valve_open);
				}else{
					v.setBackgroundResource(R.color.gas_valve_close);
				}
			}
		}
		//更新pfc
		this.pfcValueView.setText(data.getPFCShowValue());
	}

	@Override
	protected void onResume() {
		super.onResume();
		//对温度和压力数据监听
		AppService.getInstance().addReceiveDataListener(KuboData.PRESSURE_TEMPERATURE,this);
		//对阀门状态数据监听
		AppService.getInstance().addReceiveDataListener(KuboData.SOLENOIDVALVE_PFC,this);
		//发送解锁仪器气路请求
		AppService.getInstance().sendUnlockGasPathCommand();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//删除监听
		AppService.getInstance().removeReceiveDataListener(KuboData.PRESSURE_TEMPERATURE,this);
		AppService.getInstance().removeReceiveDataListener(KuboData.SOLENOIDVALVE_PFC,this);

	}

	/**
	 * 更新相关温度和压力值
	 */
	private void updtePressureTemperatrueView(PressureTemperatureKuboData data){
		this.sen1View.setText(data.getSenValue(1));
		this.sen2View.setText(data.getSenValue(2));
		this.sen3View.setText(data.getSenValue(3));
		this.sen4View.setText(data.getSenValue(4));
		this.sen5View.setText(data.getSenValue(5));
		this.sen6View.setText(data.getSenValue(6));
		this.senMView.setText(data.getSenMValue());
		this.senPoView.setText(data.getSenPoValue());
		temperatureView.setText(data.getTempretureShowValue());
	}
}
