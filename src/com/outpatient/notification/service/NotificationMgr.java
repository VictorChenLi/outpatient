package com.outpatient.notification.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.outpatient.notification.model.RunTask;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.service.TimeHelper;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class NotificationMgr {
	
	private Context context;
	private DBAccessImpl dbAccessImpl;
	
	public NotificationMgr(Context context)
	{
		this.context=context;
		dbAccessImpl=DBAccessImpl.getInstance(context);
	}
	
	public void resumeAllReminder()
	{
		Log.v("reminder", "in resumeAllReminder");
		List<Task> taskList = dbAccessImpl.queryShowTaskList();
		Log.v("reminder", "tasklist:"+String.valueOf(taskList.size()));
		for(Task task : taskList)
		{
			Reminder reminder = dbAccessImpl.getReminderByTid(task.getTid());
			if(null!=reminder)
			{
				this.resumeReminder(reminder.getRid());
			}
		}
	}
	
	public void resumeReminder(int rid)
	{
		Log.v("reminder", "in resumeReminder:"+rid);
		Reminder reminder = dbAccessImpl.describeReminder(rid);
		Task task = dbAccessImpl.describeTask(reminder.getTid());
		
		Long currentTime = Calendar.getInstance().getTimeInMillis();
		if(currentTime<reminder.getStartTime())
		{
			Log.v("reminder", "currentTime<reminder.getStartTime");
			NotificationHelper.setNotificationReminder(context, rid, reminder.getStartTime());
		}
		else if(currentTime<reminder.getEndTime())
		{
			Log.v("reminder", "currentTime>reminder.getStartTime");
			if(1==reminder.getIsRoutine())
			{
				String timeInterval=null;
				// means will repeat every "RepeatingTimes" hours
				if(1==reminder.getRepeatingDays())
				{
					Log.v("reminder", "1==reminder.getRepeatingDays");
					//add repeating days to the start time
					timeInterval = String.valueOf(reminder.getRepeatingTimes())+"h";
					
				}
				// means will repeat every "RepeatingTimes" days
				if(2==reminder.getRepeatingDays())
				{
					Log.v("reminder", "1==reminder.getRepeatingDays");
					//add repeating days to the start time
					timeInterval = String.valueOf(reminder.getRepeatingTimes())+"d";
				}
				// means will repeat every "RepeatingTimes" weeks
				if(3==reminder.getRepeatingDays())
				{
					Log.v("reminder", "1==reminder.getRepeatingDays");
					//add repeating days to the start time
					timeInterval = String.valueOf(reminder.getRepeatingTimes()*7)+"d";
				}
//				else //means everyday have certain times reminder
//				{
//					// we divide the 12 day hour with daily repeat times
//					// we reminder every certain hours
//					int repeatTimes = 1;
//					String timeInterval = String.valueOf(12/reminder.getRepeatingTimes())+"h";
//					Long nextIndexTime = TimeHelper.getDayTime(reminder.getStartTime());
//					Long nextNotifyTime = reminder.getStartTime();
//					while(nextIndexTime<TimeHelper.getDayTime(currentTime))
//					{
//						// if the next notify time is earlier than current time keep moving to the next
//						nextNotifyTime = nextNotifyTime+TimeHelper.getPlainTimeInMillis(timeInterval);
//						nextIndexTime = nextIndexTime +TimeHelper.getPlainTimeInMillis(timeInterval);
//						repeatTimes++;
//					}// will exit when the next notify time later than current time
//					
//					// if the repeat times not larger than reminder daily repeating time
//					// we could set it as our next notification
//					if(repeatTimes<=reminder.getRepeatingTimes())
//					{
//						NotificationHelper.setNotificationReminder(context, rid,nextNotifyTime);
//					}
//				}
				
				Long nextNotifyTime = reminder.getStartTime();
				while(nextNotifyTime<currentTime)
				{
					// if the next notify time is earlier than current time keep moving to the next
					nextNotifyTime = nextNotifyTime+TimeHelper.getPlainTimeInMillis(timeInterval);
				}// will exit when the next notify time later than current time
				
				// if the next notify time is earlier than the end time
				// we could set it as our next notification
				if(nextNotifyTime<reminder.getEndTime())
				{
					NotificationHelper.setNotificationReminder(context, rid,nextNotifyTime);
				}
			}
		}
	}
	
}
