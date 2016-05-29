package com.chan.today.acitivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chan.today.R;
import com.chan.today.adapter.CommonRecyclerViewAdapter;
import com.chan.today.bean.TodayBean;
import com.chan.today.utils.DataFactory;
import com.chan.today.utils.JuheDemo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 选定日期后就跳转到这个Activity
 */

public class SpecifyActivity extends AppCompatActivity {

	private RecyclerView recyclerView;
	private ProgressDialog dialog;
	private List<TodayBean> beanList;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == DataFactory.GETDATA) {
				CommonRecyclerViewAdapter commonRecyclerViewAdapter = new CommonRecyclerViewAdapter(
						SpecifyActivity.this, beanList);
				recyclerView.setAdapter(commonRecyclerViewAdapter);
                dialog.dismiss ();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_specify);
		initDialog();
		initRecyclerView();
        getData();
	}

	private void initDialog() {
		dialog = ProgressDialog.show(this, "正在加载", "您的网络状况不佳，加载可能比较缓慢");
		dialog.show();
	}

	private void initRecyclerView() {
		recyclerView = (RecyclerView) findViewById(R.id.rv_specify);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view,
					RecyclerView parent, RecyclerView.State state) {
				if (parent.getChildLayoutPosition(view) != 0) {
					outRect.top = DataFactory.RECYCLERVIEW_SIZE;
				}
			}
		});
	}

	private void getData() {
		beanList = new ArrayList<>();
		Intent intent = getIntent();
		final int month = intent.getIntExtra("month", 0);
		final int day = intent.getIntExtra("day", 0);
		new Thread() {
			@Override
			public void run() {
				JSONObject jsonObject = JuheDemo.getRequest1(month, day);
				JuheDemo.parseJsonData(jsonObject, beanList, handler);
			}
		}.start();
	}
}
