package com.outpatient.notification.service;

import com.example.outpatient.MainActivity;
import com.example.outpatient.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper {
	
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
