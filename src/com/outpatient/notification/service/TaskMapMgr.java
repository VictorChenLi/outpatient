package com.outpatient.notification.service;

import java.util.HashMap;
import java.util.Map;

import com.outpatient.notification.model.RunTask;


public class TaskMapMgr {
	private static Map<String,RunTask> taskMap=new HashMap<String,RunTask>();
	
	public static void registTask(String id,RunTask runTask)
	{
		taskMap.put(id, runTask);
	}
	
	public static void registTask(int id,RunTask runTask)
	{
		taskMap.put(String.valueOf(id), runTask);
	}
	
	public static RunTask getTaskByTaskId(String taskId)
	{
		return taskMap.get(taskId);
	}
	
	public static void removeTask(String taskId)
	{
		if(null!=taskMap.get(taskId))
		{
			taskMap.get(taskId).interrupt();
			taskMap.remove(taskId);
		}
	}
	
	public static void clearTask()
    {
        if (taskMap == null)
        {
            return;
        }
        for (Map.Entry<String, RunTask> m : taskMap.entrySet())
        {
           RunTask task = m.getValue();
           task.Terminate();
        }
        taskMap.clear();
    }
}
