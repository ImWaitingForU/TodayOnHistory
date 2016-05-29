package com.chan.today.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chan.today.R;
import com.chan.today.adapter.CommonRecyclerViewAdapter;
import com.chan.today.bean.TodayBean;
import com.chan.today.utils.BottomMenu;
import com.chan.today.utils.DataFactory;
import com.chan.today.utils.JuheDemo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommonFragment extends Fragment {

	// fragment对应的月/日
	protected int monthFlag = 0;
	protected int dayFlag = 0;
	protected View view;

	protected List<TodayBean> todayList;
	protected CommonRecyclerViewAdapter commonRecyclerViewAdapter;

	protected RecyclerView recyclerView;

	// 自定义的 BottomMenu
	private BottomMenu bottomMenu;

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == DataFactory.GETDATA) {
				commonRecyclerViewAdapter = new CommonRecyclerViewAdapter(
						getContext(), todayList);
				recyclerView.setAdapter(commonRecyclerViewAdapter);
			}
		}
	};

	public CommonFragment() {
	}

	public int getDayFlag() {
		return dayFlag;
	}

	public void setDayFlag(int dayFlag) {
		this.dayFlag = dayFlag;
	}

	public int getMonthFlag() {
		return monthFlag;
	}

	public void setMonthFlag(int monthFlag) {
		this.monthFlag = monthFlag;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取数据并解析
		todayList = new ArrayList<>();
		getTodayHistory(monthFlag, dayFlag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_today, container, false);
		initRecyclerView();
		return view;
	}

	/*
	 * 初始化recyclerView
	 */
	protected void initRecyclerView() {
		bottomMenu = (BottomMenu) getActivity().findViewById(R.id.bottomMenu);
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void getItemOffsets(Rect outRect, View view,
					RecyclerView parent, RecyclerView.State state) {
				if (parent.getChildLayoutPosition(view) != 0) {
					outRect.top = DataFactory.RECYCLERVIEW_SIZE;
				}
			}
		});

		// 监听recyclerView的滑动状态，滑动时隐藏bottomMenu
		// 滑动停止时显示bottomMenu
		// 静止时再隐藏bottomMenu
		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				// super.onScrollStateChanged (recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
					bottomMenu.startSlipInAnimation(0);
				} else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

				} else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					bottomMenu.startSlipOutAnimation();
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
			}
		});
	}

	/**
	 * 获取历时的某一天的事件列表,根据标记判断要获取哪种数据
	 */
	public void getTodayHistory(final int month, final int day) {
		new Thread() {
			@Override
			public void run() {
				JSONObject todayJsonObject = JuheDemo.getRequest1(month, day);
				JuheDemo.parseJsonData(todayJsonObject, todayList, handler);
			}
		}.start();
	}

}
