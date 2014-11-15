package com.outpatient.notification.model;

import android.content.Context;
import android.content.Intent;

import com.outpatient.notification.service.NotificationHandler;

public class RunNotificationReminder extends RunTask {
	private String title =null;
	private String content =null;
	private Context target;
	
	
	public RunNotificationReminder(Context context, String notificationTitle, String notificationContent)
	{
		super();
		title = notificationTitle;
		content = notificationContent;
		target = context;
	}
	
	public void run()
	{
		Intent intent = new Intent(target, NotificationHandler.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("NotificationTitle", title);
		intent.putExtra("NotificationContent", content);
		target.startActivity(intent);
	}
}
