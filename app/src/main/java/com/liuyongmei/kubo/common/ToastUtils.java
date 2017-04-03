package com.liuyongmei.kubo.common;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ToastUtils {

	/** 之前显示的内容 */
	private volatile static String oldMessage ;
	/** 第一次时间 */
	private static long lastShowTime = 0 ;

	/**
	 * @param context
	 * @param message
	 */
	public static void longShow(Context context, Object message){
		show(context,message,Toast.LENGTH_LONG);
	}
	public static void shortShow(Context context, Object message){
		show(context,message,Toast.LENGTH_SHORT);
	}

	private static void show(Context context, Object message,int time){
		if(context==null)return;
		if(message==null)return;
		String m=message.toString();
		if((m=m.trim()).length()==0)return;

		if(message.equals(oldMessage)) {
			if(lastShowTime!=0&&System.nanoTime()-lastShowTime<300) {
				return;
			}
		}
		Log.d("xxx",m);
		Toast.makeText(context, m, time).show();
		lastShowTime=System.nanoTime();
		oldMessage=m;
	}
}