package com.outpatient.notification.service;

import com.example.outpatient.InfoFragment;
import com.example.outpatient.PlanFragment;
import com.example.outpatient.R;
import com.example.outpatient.TabsAdapter;
import com.example.outpatient.TaskFragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class NotificationHandler extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        NotificationMgr notifyMgr = new NotificationMgr(this);
        notifyMgr.resumeAllReminder();
    }
}
