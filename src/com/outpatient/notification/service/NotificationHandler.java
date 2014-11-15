package com.outpatient.notification.service;

import com.example.outpatient.InfoFragment;
import com.example.outpatient.PlanFragment;
import com.example.outpatient.R;
import com.example.outpatient.TabsAdapter;
import com.example.outpatient.TaskFragment;
import com.outpatient.sysUtil.model.Constant;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class NotificationHandler extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String notificationTitle = this.getIntent().getExtras().getString("NotificationTitle");
        String notificationContent = this.getIntent().getExtras().getString("NotificationContent");
        Log.v("reminder", "success notification");
        NotificationHelper.setNotification(this, notificationTitle, notificationContent);
    }
}
