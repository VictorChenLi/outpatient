package com.outpatient.notification.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.outpatient.notification.service.NotificationHandler;

public class RunNotificationReminder extends RunTask {
	private Context target;
	private int mrid;
	
	
	public RunNotificationReminder(Context context, int rid)
	{
		super();
		target = context;
		mrid=rid;
	}
	
	public void run()
	{
		Intent intent = new Intent(target, NotificationHandler.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("rid", mrid);
		intent.putExtra("action", "Notify");
//		Bundle mBundle = new Bundle();
//		mBundle.putInt("rid", mrid);
//		mBundle.putString("action", "Notify");
//		intent.putExtras(mBundle);
		try{
			target.startActivity(intent);
		}
		catch(Exception ee)
		{
			Log.v("reminder", ee.getStackTrace().toString());
		}
	}
}
