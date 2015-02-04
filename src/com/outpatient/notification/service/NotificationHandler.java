package com.outpatient.notification.service;

import com.outpatient.williamosler.R;
import com.outpatient.main.InfoFragment;
import com.outpatient.main.PlanFragment;
import com.outpatient.main.TaskFragment;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.Constant;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;

public class NotificationHandler extends FragmentActivity {

	private int rid;
	private String action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.getIntent().getBundleExtra("action")
		rid = getIntent().getIntExtra("rid", 0);
		action = getIntent().getStringExtra("action");

		if ("Notify".equals(action)) {
			NotificationHelper.setNotification(this, rid);
		}
		if ("Dismiss".equals(action)) {
			NotificationHelper.dismissNotificationReminder(this, rid);
			this.finish();
		}
		if ("Snooze".equals(action)) {
			NotificationHelper.snoozeNotification(this, rid);
			this.finish();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// If we've received a touch notification that the user has touched
		// outside the app, finish the activity.
		if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
			finish();
			return true;
		}
		// Delegate everything else to Activity.
		return super.onTouchEvent(event);
	}
}
