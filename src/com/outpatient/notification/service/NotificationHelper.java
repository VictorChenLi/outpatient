package com.outpatient.notification.service;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.example.outpatient.MainActivity;
import com.example.outpatient.R;
import com.outpatient.notification.model.RunNotificationReminder;
import com.outpatient.notification.model.RunTask;
import com.outpatient.storeCat.model.Task;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationHelper {
	
	public static void runTask(RunTask runTask,String taskTag,long delay)
	{
		if(null!=TaskMapMgr.getTaskByTaskId(taskTag))
		{
			TaskMapMgr.removeTask(taskTag);
		}
		TaskMapMgr.registTask(taskTag, runTask);
		ExecutorServiceHelper.schedule(runTask, delay,TimeUnit.MILLISECONDS);
	}
	
	public static void setNotificationReminder(Context context, Task task, Long targetTime)
	{
		Log.v("reminder", "setNotificationReminder:"+String.valueOf(targetTime));
		Long delay = targetTime - Calendar.getInstance().getTimeInMillis();
		RunNotificationReminder runNotifi = new RunNotificationReminder(context, task.getName(),task.getDes());
		NotificationHelper.runTask(runNotifi, String.valueOf(task.getTid()),delay );
	}
	
	
	public static void setNotification(Activity activity,String notificationTitle,String notificationContent)
	{
		
		Intent intent = new Intent(activity, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(activity, 0, intent, 0);

		// build notification
		// the addAction re-use the same intent to keep the example short
		NotificationCompat.Builder NotificationBuilder  = new NotificationCompat.Builder(activity)
		        .setContentTitle(notificationTitle)
		        .setContentText(notificationContent)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pIntent)
		        .setAutoCancel(true)
		        .addAction(R.drawable.ic_launcher, "Dismiss", pIntent)
		        .addAction(R.drawable.ic_launcher, "Snooze", pIntent);
		    
		  
		NotificationManager notificationManager = 
		  (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);

		notificationManager.notify(0, NotificationBuilder.build());
	}
}
