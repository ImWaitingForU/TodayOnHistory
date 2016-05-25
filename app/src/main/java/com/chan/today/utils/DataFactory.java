package com.chan.today.utils;

import android.content.Context;

/**
 * Created by Chan on 2016/5/23.
 *
 * 存放一些共用的简单常量
 */
public class DataFactory {

	// Log TAG
	public static final String TAG = "todayHistory";

	// 获取到数据后发送消息的what是这个
	public static final int GETDATA = 0x111;

	// RecyclerView的间距
	public static final int RECYCLERVIEW_SIZE = 50;

	public static int MONTH;
	public static int DAY;

	//dip转化为px
	public static int dip2px(Context context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}

	//px转化为dip
	public static int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}

}
