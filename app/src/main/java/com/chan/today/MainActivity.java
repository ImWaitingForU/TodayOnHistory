package com.chan.today;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.chan.today.adapter.CommonViewPagerAdapter;
import com.chan.today.fragments.CommonFragment;
import com.chan.today.utils.BottomMenu;
import com.chan.today.utils.DataFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private List<CommonFragment> fragmentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTranslateStatus();
		setContentView(R.layout.activity_main);
		initMonthAndDay();
		initViewPager();
		setBottomMenuStuff();
	}

	/**
	 * 设置BottomMennu的事件监听
	 */
	private void setBottomMenuStuff() {

		BottomMenu bottomMenu = (BottomMenu) findViewById(R.id.bottomMenu);
		assert bottomMenu != null;
		bottomMenu
				.setOnBottomMenuItemClickListener(new BottomMenu.OnBottomMenuItemClickListener() {
					@Override
					public void onItemClicked(View view, int position) {
						switch (position) {
							case 0 :
								Toast.makeText (MainActivity.this, "date", Toast.LENGTH_SHORT).show ();
								break;
							case 1 :
								Toast.makeText (MainActivity.this, "share", Toast.LENGTH_SHORT).show ();
								break;
							case 2 :
								Toast.makeText (MainActivity.this, "quit", Toast.LENGTH_SHORT).show ();
								break;
							default :
								break;
						}
					}
				});

	}

	/**
	 * 设置透明状态栏
	 */
	private void setTranslateStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4 全透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0 全透明实现
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);// calculateStatusColor(Color.WHITE,
														// (int) alphaValue)
		}
	}

	/**
	 * 初始化日历,获取月，日
	 */
	private void initMonthAndDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		DataFactory.MONTH = calendar.get(Calendar.MONTH);
		DataFactory.DAY = calendar.get(Calendar.DAY_OF_MONTH);
		Log.d(DataFactory.TAG, DataFactory.MONTH + "month" + DataFactory.DAY
				+ "day");
	}

	/**
	 * 滑动添加页面,到0或2的位置则添加页面
	 * 
	 * @param position
	 *            滑动到的位置
	 */
	private void addPage(int position, CommonViewPagerAdapter adapter,
			ViewPager viewPager) {

		CommonFragment newFragment = new CommonFragment();

		// 当滑动到第一页或者最后一页,让其再加载一页，让viewPager始终保持三页，销毁原来的最远的一页
		if (position == 0) {
			// newFragment.setMonthFlag(fragmentList.get(0).getMonthFlag());
			newFragment.setMonthFlag(fragmentList.get(0).getMonthFlag());
			// newFragment.setDayFlag(fragmentList.get(0).getDayFlag() - 1);
			newFragment.setDayFlag(fragmentList.get(0).getDayFlag() - 1);
			// 先添加newFragment，此时viewPager有4个，再移除position=3的fragment
			fragmentList.add(0, newFragment);
			fragmentList.remove(3);

		} else if (position == 2) {
			newFragment.setMonthFlag(fragmentList.get(2).getMonthFlag());
			newFragment.setDayFlag(fragmentList.get(2).getDayFlag() + 1);
			// 先添加newFragment，此时viewPager有4个，再移除position=3的fragment
			fragmentList.add(3, newFragment);
			fragmentList.remove(0);
		}

		adapter.notifyDataSetChanged();
		viewPager.setCurrentItem(1);
	}

	/**
	 * 初始化viewPager,初始化状态填入3个fragment,以不同的myTag标示,分别代表lastDay,today,nextDay.
	 * 当viewPager滑动到lastDay,动态的加入一个新的fragment,
	 */
	private void initViewPager() {
		fragmentList = new ArrayList<>();
		// 初始化fragmentLsit并给每个fragment添加标记
		CommonFragment todayFragment = new CommonFragment();

		todayFragment.setMonthFlag(DataFactory.MONTH);
		todayFragment.setDayFlag(DataFactory.DAY);

		CommonFragment lastFragment = new CommonFragment();
		// CommonFragment lastFragment = new BlankFragment1 ();
		lastFragment.setMonthFlag(DataFactory.MONTH);
		lastFragment.setDayFlag(DataFactory.DAY - 1);

		CommonFragment nextFragment = new CommonFragment();
		// CommonFragment nextFragment = new BlankFragment2 ();
		nextFragment.setMonthFlag(DataFactory.MONTH);
		nextFragment.setDayFlag(DataFactory.DAY + 1);

		fragmentList.add(lastFragment);
		fragmentList.add(todayFragment);
		fragmentList.add(nextFragment);

		final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		final CommonViewPagerAdapter adapter = new CommonViewPagerAdapter(
				getSupportFragmentManager(), fragmentList);
		if (viewPager != null) {
			viewPager.setOffscreenPageLimit(1);
			viewPager.setAdapter(adapter);
			// 默认停在"今天"的fragment
			viewPager.setCurrentItem(1);

			viewPager
					.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
						@Override
						public void onPageScrolled(int position,
								float positionOffset, int positionOffsetPixels) {

						}

						@Override
						public void onPageSelected(int position) {
							// 添加这个判断防止onPageSelected在notifityDataSetChanged被调用后执行两次导致视图不保存
							if (position == 0 || position == 2) {
								addPage(position, adapter, viewPager);
							}
						}

						@Override
						public void onPageScrollStateChanged(int state) {

						}
					});

		}
	}
}
