package com.chan.today.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.chan.today.fragments.CommonFragment;

import java.util.List;

/**
 * Created by Chan on 2016/5/24.
 *
 * 通用的viewPager适配器，继承fragmentstatepageradapter
 *
 * 重写getItem(),
 */
public class CommonViewPagerAdapter extends FragmentStatePagerAdapter {

	private List<CommonFragment> fragmentList;

	public CommonViewPagerAdapter(FragmentManager fm,
			List<CommonFragment> fragmentList) {
		super(fm);
		this.fragmentList = fragmentList;
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

}