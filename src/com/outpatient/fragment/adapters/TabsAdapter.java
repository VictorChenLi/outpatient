package com.outpatient.fragment.adapters;

import java.util.ArrayList;

import com.outpatient.sysUtil.model.GlobalVar;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class TabsAdapter extends FragmentPagerAdapter implements TabListener, OnPageChangeListener{
	private final Context mContext;
	private final ActionBar mActionBar;
	private final ViewPager mViewPager;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
	private final String TAG = "debugtag";
	
	static final class TabInfo{
		private final Class<?> clss;
		private final Bundle args;
		
		TabInfo(Class<?> _class, Bundle _args){
			clss = _class;
			args = _args;
		}
	}
	

	public TabsAdapter(FragmentActivity activity, ViewPager pager) {
		super(activity.getSupportFragmentManager());
		mContext = activity;
		mActionBar = activity.getActionBar();
		mViewPager = pager;
		mViewPager.setAdapter(this);
		mViewPager.setOnPageChangeListener(this);
	}
	
	public void addTab(Tab tab, Class<?> clss, Bundle args){
		TabInfo info = new TabInfo(clss, args);
		tab.setTag(info);
		tab.setTabListener(this);
		mTabs.add(info);
		mActionBar.addTab(tab);
		notifyDataSetChanged();
	}


	@Override
	public void onPageScrollStateChanged(int state) {
		return ;
	}


	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		return;
	}


	@Override
	public void onPageSelected(int position) {
		mActionBar.setSelectedNavigationItem(position);
		switch(position) {
			case 0:
          	   GlobalVar.getTaskListAdapter(mContext).refreshTaskList();
          	   break;
			case 1:
				GlobalVar.getInfoListAdapter(mContext).refreshInfoList();
	          	break;
			case 2:
				GlobalVar.getPlanListAdapter(mContext).refreshPlanList();
				break;
		}
		Log.v(TAG, "scrolled to page="+position);
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		
		mViewPager.setCurrentItem(tab.getPosition());
		Log.v(TAG, "clicked page"+tab.getPosition());
	}


	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}


	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}


	@Override
	public int getCount() {
		return mTabs.size();
	}

}
