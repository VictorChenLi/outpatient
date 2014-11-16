package com.outpatient.notification.service;

import com.example.outpatient.InfoFragment;
import com.example.outpatient.PlanFragment;
import com.example.outpatient.R;
import com.example.outpatient.TaskFragment;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.Constant;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class NotificationHandler extends FragmentActivity {
	
	private int rid;
	private String action;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getIntent().getBundleExtra("action")
        rid =  getIntent().getIntExtra("rid", 0);
        action = getIntent().getStringExtra("action");
        
        if("Notify".equals(action))
        {
	        NotificationHelper.setNotification(this, rid);
        }
        if("Dismiss".equals(action))
        {
        	NotificationHelper.dismissNotificationReminder(this, rid);
        	this.finish();
        }
        if("Snooze".equals(action))
        {
        	NotificationHelper.snoozeNotification(this, rid);
        	this.finish();
        }
    }
}
