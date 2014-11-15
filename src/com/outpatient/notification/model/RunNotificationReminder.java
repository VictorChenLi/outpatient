package com.outpatient.notification.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.outpatient.notification.service.NotificationHandler;

public class RunNotificationReminder extends RunTask {
	private Context target;
	private int rid;
	
	
	public RunNotificationReminder(Context context, int rid)
	{
		super();
		target = context;
	}
	
	public void run()
	{
		Intent intent = new Intent(target.getApplicationContext(), NotificationHandler.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("rid", rid);
		intent.putExtra("action", "Notify");
		try{
			target.startActivity(intent);
		}
		catch(Exception ee)
		{
			Log.v("reminder", ee.getStackTrace().toString());
		}
	}
}
