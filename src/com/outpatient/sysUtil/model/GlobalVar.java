package com.outpatient.sysUtil.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.outpatient.fragment.adapters.InfoListAdapter;
import com.outpatient.fragment.adapters.PlanListAdapter;
import com.outpatient.fragment.adapters.TaskListAdapter;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;

public class GlobalVar {
	
	//list of all the plans
	public static ArrayList<Plan> plan_list;
	
	//list to show all tasks associated with plan, key is the pid
	public static HashMap<Integer, ArrayList<Task>> plan_task_list;
	
	//list to show all infos associated with plan, key is the pid
	public static HashMap<Integer, ArrayList<Info>> plan_info_list;
	
	//list to show reminder associated with task, key is the tid
	public static HashMap<Integer, Reminder> task_reminder_list;
	
	private static InfoListAdapter infoAdapter;
	
	private static PlanListAdapter planAdapter;
	
	private static TaskListAdapter taskAdapter;
	
	public static InfoListAdapter getInfoListAdapter(Context context)
	{
		if(GlobalVar.infoAdapter==null)
			GlobalVar.infoAdapter = new InfoListAdapter(context);
		return GlobalVar.infoAdapter;
		
	}
	
	public static PlanListAdapter getPlanListAdapter(Context context)
	{
		if(GlobalVar.planAdapter==null)
			GlobalVar.planAdapter = new PlanListAdapter(context);
		return GlobalVar.planAdapter;
		
	}
	
	public static TaskListAdapter getTaskListAdapter(Context context)
	{
		if(GlobalVar.taskAdapter==null)
			GlobalVar.taskAdapter = new TaskListAdapter(context);
		return GlobalVar.taskAdapter;
		
	}
	
}
