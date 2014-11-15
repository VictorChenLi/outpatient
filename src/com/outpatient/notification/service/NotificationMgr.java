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


import android.app.Activity;
import android.content.Context;

public class NotificationMgr {
	
	private Activity activity;
	private DBAccessImpl dbAccessImpl;
	
	public NotificationMgr(Activity activity)
	{
		this.activity=activity;
		dbAccessImpl=DBAccessImpl.getInstance(activity);
	}
	
	public static void runTask(RunTask runTask,String taskTag,long delay)
	{
		if(null!=TaskMapMgr.getTaskByTaskId(taskTag))
		{
			TaskMapMgr.removeTask(taskTag);
		}
		TaskMapMgr.registTask(taskTag, runTask);
		ExecutorServiceHelper.schedule(runTask, delay,TimeUnit.MILLISECONDS);
	}
	
	public void resumeAllReminder()
	{
		
	}
	
	public void resumeReminder(int rid)
	{
		Reminder reminder = dbAccessImpl.describeReminder(rid);
		Task task = dbAccessImpl.describeTask(reminder.getTid());
		
		Long currentTime = Calendar.getInstance().getTimeInMillis();
		if(currentTime<reminder.getStartTime())
		{
			NotificationHelper.setNotification(activity, task.getName(), task.getDes());
		}
		else if(currentTime<reminder.getEndTime())
		{
			if(1==reminder.getIsRoutine())
			{
				// means everyday will notify 1 time at same start time
				if(1==reminder.getRepeatingTimes())
				{
//					Long nextNotifyTime = currentTime+
//					while()
				}
			}
		}
	}
	
}
