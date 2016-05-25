package com.chan.today.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chan.today.R;
import com.chan.today.bean.TodayBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Chan on 2016/5/23.
 *
 * todayFragment的recyclerView适配器
 */
public class CommonRecyclerViewAdapter
		extends
			RecyclerView.Adapter<CommonRecyclerViewAdapter.MyViewHolder> {

	private List<TodayBean> beanList;
	private Context context;

	public CommonRecyclerViewAdapter(Context context, List<TodayBean> beanList) {
		this.beanList = beanList;
		this.context = context;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.today_item_layout, parent, false);
		return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {

		TodayBean bean = beanList.get(position);
		// picasso加载图片
		Picasso.with(context).load(Uri.parse(bean.getPic())).fit ()
				.config(Bitmap.Config.ARGB_8888).placeholder(R.mipmap.loading)
				.into(holder.iv_pic);

		// 加载文字
		holder.tv_title.setText(bean.getTitle());
		String date = bean.getYear() + "年" + bean.getMonth() + "月"
				+ bean.getDay() + "日";
		holder.tv_date.setText(date);
		holder.tv_des.setText("     " + bean.getDes());
		holder.tv_id.setText(bean.getId());
		holder.tv_lunar.setText(bean.getLunar());
	}
	@Override
	public int getItemCount() {
		return beanList.size();
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {

		public ImageView iv_pic;
		public TextView tv_title;
		public TextView tv_date;
		public TextView tv_des;
		public TextView tv_id;
		public TextView tv_lunar;

		public MyViewHolder(View itemView) {
			super(itemView);

			iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
			tv_id = (TextView) itemView.findViewById(R.id.tv_id);
			tv_title = (TextView) itemView.findViewById(R.id.tv_title);
			tv_date = (TextView) itemView.findViewById(R.id.tv_date);
			tv_des = (TextView) itemView.findViewById(R.id.tv_des);
			tv_lunar = (TextView) itemView.findViewById(R.id.tv_lunar);
		}
	}
}
