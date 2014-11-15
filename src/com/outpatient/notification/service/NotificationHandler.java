package com.outpatient.notification.service;

import com.example.outpatient.InfoFragment;
import com.example.outpatient.PlanFragment;
import com.example.outpatient.R;
import com.example.outpatient.TabsAdapter;
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
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = this.getIntent().getExtras().getString("action");
        int rid = this.getIntent().getExtras().getInt("rid");
        
        if("Notify"==action)
        {
	        NotificationHelper.setNotification(this, rid);
	        NotificationHelper.setAlert(this, rid);
        }
        if("Dismiss"==action)
        {
        	NotificationHelper.dismissNotificationReminder(this, rid);
        }
        if("Snooze"==action)
        {
        	NotificationHelper.snoozeNotification(this, rid);
        }
    }
}
