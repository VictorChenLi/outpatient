package com.outpatient.notification.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.outpatient.notification.model.RunTask;
import com.outpatient.storeCat.service.DBAccessImpl;


import android.content.Context;

public class NotificationMgr {
	
	private Context context;
	
	public NotificationMgr(Context context)
	{
		this.context=context;
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
	
}
