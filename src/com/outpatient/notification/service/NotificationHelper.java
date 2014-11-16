package com.outpatient.notification.service;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.example.outpatient.MainActivity;
import com.example.outpatient.NotificationDialogFragment;
import com.example.outpatient.R;
import com.outpatient.notification.model.RunNotificationReminder;
import com.outpatient.notification.model.RunTask;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.service.PlayAlarmMusic;
import com.outpatient.sysUtil.service.TimeHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class NotificationHelper {
	
	public static void runTask(RunTask runTask,String taskTag,long delay)
	{
		if(null!=TaskMapMgr.getTaskByTaskId(taskTag))
		{
			TaskMapMgr.removeTask(taskTag);
		}
		TaskMapMgr.registTask(taskTag, runTask);
		Log.v("reminder", "thread will start after"+TimeHelper.getPlainStrFromTimeMillis(delay));
		ExecutorServiceHelper.schedule(runTask, delay,TimeUnit.MILLISECONDS);
	}
	
	public static void setNotificationReminder(Context context, int rid, Long targetTime)
	{
		Log.v("reminder", "setNotificationReminder rid:"+rid+" at:"+TimeHelper.getPaintStrFromTimeMillis(targetTime));
		Long delay = targetTime - Calendar.getInstance().getTimeInMillis();
		RunNotificationReminder runNotifi = new RunNotificationReminder(context, rid);
		NotificationHelper.runTask(runNotifi, String.valueOf(rid),delay );
	}
	
	public static void dismissNotificationReminder(Context context, int rid)
	{
		TaskMapMgr.removeTask(String.valueOf(rid));
		NotificationMgr notifyMgr = new NotificationMgr(context);
		notifyMgr.resumeReminder(rid);
		NotificationHelper.dismissAlert(context, rid);
		
		NotificationManager notificationManager = 
				  (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		
		notificationManager.cancel(rid);
	}
	
	public static void snoozeNotification(Context context, int rid)
	{
		// will replace by setting attributes
		Long delayTime = 1000*60*5L;
		TaskMapMgr.removeTask(String.valueOf(rid));
		DBAccessImpl dbAccessImpl = DBAccessImpl.getInstance(context);
		Reminder reminder = dbAccessImpl.describeReminder(rid);
		reminder.setStartTime(delayTime+reminder.getStartTime());
		if(1==reminder.getIsRoutine())
			reminder.setEndTime(delayTime+reminder.getEndTime());
		dbAccessImpl.UpdateReminder(reminder);
		Toast.makeText(context, context.getResources().getString(R.string.snoozeReminder), Toast.LENGTH_LONG).show();
		NotificationMgr notifyMgr = new NotificationMgr(context);
		notifyMgr.resumeReminder(rid);
		NotificationHelper.dismissAlert(context, rid);
		
		NotificationManager notificationManager = 
				  (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		
		notificationManager.cancel(rid);
	}
	
	public static void dismissAlert(Context context,int rid)
	{
		Intent alertMusicIntent= new Intent(context,PlayAlarmMusic.class);
		context.stopService(alertMusicIntent);
	}
	
	public static void setAlert(FragmentActivity activity,int rid)
	{	
		NotificationDialogFragment notifyDialog = new  NotificationDialogFragment();
		Bundle args = new Bundle();
		args.putInt("rid", rid);
		notifyDialog.setArguments(args);
		notifyDialog.show(activity.getSupportFragmentManager(), "Notification");
		Intent alertMusicIntent= new Intent(activity,PlayAlarmMusic.class);
		activity.startService(alertMusicIntent);
	}
	
	public static void setNotification(FragmentActivity activity,int rid)
	{
        DBAccessImpl dbAccessImpl = DBAccessImpl.getInstance(activity);
        int tid = dbAccessImpl.describeReminder(rid).getTid();
        Task task = dbAccessImpl.describeTask(tid);
        String notificationTitle = task.getName();
        String notificationContent = task.getDes();

		Intent dismissIntent = new Intent(activity, NotificationHandler.class);
//		Bundle extras_dismiss = new Bundle();
//		extras_dismiss.putInt("rid", rid);
//		extras_dismiss.putString("action", "Dismiss");
		dismissIntent.putExtra("rid", rid);
		dismissIntent.putExtra("action", "Dismiss");
		PendingIntent pIntent_dismiss = PendingIntent.getActivity(activity, 0, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		Intent snoozeIntent = new Intent(activity, NotificationHandler.class);
//		Bundle extras_snooze = new Bundle();
//		extras_snooze.putInt("rid", rid);
//		extras_snooze.putString("action", "Snooze");
		snoozeIntent.putExtra("rid", rid);
		snoozeIntent.putExtra("action", "Snooze");
//		snoozeIntent.putExtras(extras_dismiss);
		PendingIntent pIntent_snooze = PendingIntent.getActivity(activity, 0, snoozeIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		// build notification
		// the addAction re-use the same intent to keep the example short
		NotificationCompat.Builder NotificationBuilder  = new NotificationCompat.Builder(activity)
		        .setContentTitle(notificationTitle)
		        .setContentText(notificationContent)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pIntent_dismiss)
		        .setAutoCancel(true)
		        .addAction(R.drawable.ic_launcher, "Dismiss", pIntent_dismiss)
		        .addAction(R.drawable.ic_launcher, "Snooze", pIntent_snooze);
		    
		  
		NotificationManager notificationManager = 
		  (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);

		notificationManager.notify(rid, NotificationBuilder.build());
		NotificationHelper.setAlert(activity, rid);
	}
}
