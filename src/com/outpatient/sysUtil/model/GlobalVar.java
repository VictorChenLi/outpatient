package com.outpatient.sysUtil.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.outpatient.fragment.adapters.InfoListAdapter;
import com.example.outpatient.fragment.adapters.PlanListAdapter;
import com.example.outpatient.fragment.adapters.TaskListAdapter;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;

public class GlobalVar {
	
	//list of all the plans
	public static ArrayList<Plan> plan_list;
	
	//list to show all tasks associated with plan, key is the pid
	public static HashMap<Integer, ArrayList<Task>> plan_task_list;
	
	//list to show all infos associated with plan, key is the pid
	public static HashMap<Integer, ArrayList<Info>> plan_info_list;
	
	//list to show reminder associated with task, key is the tid
	public static HashMap<Integer, Reminder> task_reminder_list;
	
	public static InfoListAdapter infoAdapter;
	
	public static PlanListAdapter planAdapter;
	
	public static TaskListAdapter taskAdapter;
	
}
