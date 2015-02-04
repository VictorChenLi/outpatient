package com.outpatient.storeCat.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.outpatient.storeCat.frameWork.UserDBHelper;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAccessImpl implements DBAccess {
	
	private static DBAccessImpl dbAccessImpl = null;
	
	public static final String PRESET_DB="outpatient_preset.db";
	public static final String USER_DB="outpatient_user.db";
	public static final int VERSION = 4;
	public Context udcontext;
	
	//SQLiteHelper for the user created database
	UserDBHelper userDB_Helper;
	SQLiteDatabase user_wdb;
	SQLiteDatabase user_rdb;
	
	//SQLiteHelper for the preset stored database
	UserDBHelper presetDB_Helper;
	SQLiteDatabase preset_wdb;
	SQLiteDatabase preset_rdb;
	
	
	private DBAccessImpl(Context context) {
    	udcontext = context;
    	userDB_Helper = new UserDBHelper(context,USER_DB,null,VERSION);
        user_wdb=userDB_Helper.getWritableDatabase();
        user_rdb=userDB_Helper.getReadableDatabase();
        
    }
	
	public static synchronized DBAccessImpl getInstance(Context context)
	{
		if(dbAccessImpl==null)
			dbAccessImpl= new DBAccessImpl(context);
		return dbAccessImpl;
	}
	
	/********************************** Task Operation********************************/
    public int InsertTask(Task task)
    {
    	String strSql = "select tid from tbl_task ORDER BY tid desc LIMIT 0,1";
    	int nextId=0;
    	Cursor cursor = user_rdb.rawQuery(strSql, null);
    	if(cursor.moveToNext())
    		nextId = cursor.getInt(0);
    	strSql="Insert into [tbl_task](pid,name,notes,taskType,des,isArch,date) VALUES (?,?,?,?,?,?,?)";
    	Object[] bindArgs = { task.getPid(),task.getName(),task.getNotes(),task.getTaskType(),task.getDes(),task.getIsArch(),task.getDate()};
    	user_wdb.execSQL(strSql,bindArgs);
    	return ++nextId;
    }
    
    public void UpdateTask(Task task)
    {
    	String strSql="Update [tbl_task] set pid=?, name=?, notes=?, taskType=?, des=?, isArch=?, date=? where tid=?";
    	Object[] bindArgs = { task.getPid(),task.getName(),task.getNotes(),task.getTaskType(),task.getDes(),task.getIsArch(),task.getDate(),task.getTid()};
    	user_wdb.execSQL(strSql,bindArgs);
    }
    
    public void deletTask(int tid)
    {
    	String strSql="Delete from [tbl_task] where tid=?";
    	Object[] bindArgs ={tid};
    	user_rdb.execSQL(strSql,bindArgs);
    }
    
    public void ArchTask(int tid)
    {
    	String strSql="Update [tbl_task] set isArch=1 where tid=?";
    	Object[] bindArgs ={tid};
    	user_rdb.execSQL(strSql,bindArgs);
    }
    
    private List<Task> fillTaskList(Cursor cursor)
    {
    	List<Task> list=new ArrayList<Task>();
        while (cursor.moveToNext())
        	 list.add(new Task(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getString(5),cursor.getInt(6),cursor.getLong(7)));
        return list;
    }
    
    public List<Task> queryTaskList()
    {
    	Log.v("reminder", "queryTaskList");
    	String strSql="Select * from [tbl_task]";
    	Cursor cursor = user_rdb.rawQuery(strSql,null);
        return fillTaskList(cursor);
    }
    
    public List<Task> queryArchTaskList()
    {
    	String strSql="Select * from [tbl_task] where isArch=1";
    	Cursor cursor = user_rdb.rawQuery(strSql,null);
    	return fillTaskList(cursor);
    }
    
    public List<Task> queryShowTaskList()
    {
    	String strSql="Select * from [tbl_task] where isArch=0";
    	Cursor cursor = user_rdb.rawQuery(strSql,null);
    	return fillTaskList(cursor);
    }
    
    public List<Task> queryTaskListByType(int taskType)
    {
    	String strSql="Select * from [tbl_task] where taskType=?";
    	String[] bindArgs ={String.valueOf(taskType)};
    	Cursor cursor = user_rdb.rawQuery(strSql,bindArgs);
    	return fillTaskList(cursor);
    }
    
    public List<Task> queryTaskListByPid(int pid)
    {
    	String strSql="Select * from [tbl_task] where pid=?";
    	String[] bindArgs ={String.valueOf(pid)};
    	Cursor cursor = user_rdb.rawQuery(strSql,bindArgs);
    	return fillTaskList(cursor);
    }
    
    public Task describeTask(int taskId)
    {
    	String strSql="Select * from [tbl_task] where tid=?";
    	Task task =null;
    	String[] bindArgs ={String.valueOf(taskId)};
    	Cursor cursor = user_rdb.rawQuery(strSql,bindArgs);
    	List<Task> list = fillTaskList(cursor);
        return list.size()>0?list.get(0):null;
    }
    /********************************** Task Operation********************************/
    
    /********************************** Reminder Operation********************************/
    public int InsertReminder(Reminder reminder)
    {
    	String strSql = "select rid from tbl_reminder ORDER BY tid desc LIMIT 0,1";
    	int nextId=0;
    	Cursor cursor = user_rdb.rawQuery(strSql, null);
    	if(cursor.moveToNext())
    		nextId = cursor.getInt(0);
    	
    	strSql="Insert into [tbl_reminder] (tid,startTime,isRoutine,endTime,repeatingDays,repeatingTimes) VALUES (?,?,?,?,?,?)";
    	Object[] bindArgs = { reminder.getTid(),reminder.getStartTime(),reminder.getIsRoutine(),reminder.getEndTime(),reminder.getRepeatingDays(),reminder.getRepeatingTimes()};
    	user_wdb.execSQL(strSql,bindArgs);
    	return ++nextId;
    }
    
    public void UpdateReminder(Reminder reminder)
    {
    	String strSql="Update [tbl_reminder] set tid=?, startTime=?, isRoutine=?, endTime=?, repeatingDays=?, repeatingTimes=? where rid=?";
    	Object[] bindArgs = { reminder.getTid(),reminder.getStartTime(),reminder.getIsRoutine(),reminder.getEndTime(),reminder.getRepeatingDays(),reminder.getRepeatingTimes(),reminder.getRid()};
    	user_wdb.execSQL(strSql,bindArgs);
    }
    
    public void deletReminder(int rid)
    {
    	String strSql="Delete from [tbl_reminder] where rid=?";
    	Object[] bindArgs ={rid};
    	user_rdb.execSQL(strSql,bindArgs);
    }
    
    private List<Reminder> fillReminderList(Cursor cursor)
    {
    	List<Reminder> list=new ArrayList<Reminder>();
        while (cursor.moveToNext())
        	 list.add(new Reminder(cursor.getInt(0),cursor.getInt(1),cursor.getLong(2),cursor.getInt(3),cursor.getLong(4),cursor.getInt(5),cursor.getInt(6)));
        return list;
    }
    
    public Reminder describeReminder(int rid)
    {
    	String strSql="Select * from [tbl_reminder] where rid=?";
    	Reminder reminder =null;
    	String[] bindArgs ={String.valueOf(rid)};
    	Cursor cursor = user_rdb.rawQuery(strSql,bindArgs);
    	List<Reminder> list = fillReminderList(cursor);
        return list.size()>0?list.get(0):null;
    }
    
    public Reminder getReminderByTid(int tid)
    {
    	String strSql="Select * from [tbl_reminder] where tid=? ";
    	String[] bindArgs ={String.valueOf(tid)};
    	Cursor cursor = user_rdb.rawQuery(strSql,bindArgs);
    	List<Reminder> list = fillReminderList(cursor);
        return list.size()>0?list.get(0):null;
    }
    /********************************** Reminder Operation********************************/
    
    /********************************** Plan Operation********************************/
    public int InsertPlan(Plan plan)
    {
    	String strSql = "select pid from tbl_plan ORDER BY pid desc LIMIT 0,1";
    	int nextId=0;
    	Cursor cursor = user_rdb.rawQuery(strSql, null);
    	if(cursor.moveToNext())
    		nextId = cursor.getInt(0);
    	
    	strSql="Insert into [tbl_plan](name,planType,date,isArch) VALUES (?,?,?,?)";
    	Object[] bindArgs = { plan.getName(),plan.getPlanType(),plan.getDate(),plan.getIsArch()};
    	user_wdb.execSQL(strSql,bindArgs);
    	return ++nextId;
    }
    
    public void UpdatePlan(Plan plan)
    {
    	String strSql="Update [tbl_plan] set name=?, planType=?, date=?, isArch=? where pid=?";
    	Object[] bindArgs = { plan.getName(),plan.getPlanType(),plan.getDate(),plan.getIsArch(),plan.getPid()};
    	user_wdb.execSQL(strSql,bindArgs);
    }
    
    public void deletPlan(int pid)
    {
    	String strSql="Delete from tbl_plan where pid=?";
    	Object[] bindArgs ={pid};
    	user_rdb.execSQL(strSql,bindArgs);
    }
    public void ArchPlan(int pid)
    {
    	String strSql="Update [tbl_plan] set isArch=1 where pid=?";
    	Object[] bindArgs ={pid};
    	user_rdb.execSQL(strSql,bindArgs);
    }
    
    private List<Plan> fillPlanList(Cursor cursor)
    {
    	List<Plan> list = new ArrayList<Plan>();
        while (cursor.moveToNext())
        	 list.add(new Plan(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getLong(3),cursor.getInt(4)));
        return list;
    }
    
    public Plan describePlan(int pid)
    {
    	String strSql="Select * from [tbl_plan] where pid=?";
    	Plan plan =null;
    	String[] bindArgs ={String.valueOf(pid)};
    	Cursor cursor = user_rdb.rawQuery(strSql,bindArgs);
    	List<Plan> list = fillPlanList(cursor);
        return list.size()>0?list.get(0):null;
    }
    
    public List<Plan> queryPlanList()
    {
    	String strSql="Select * from [tbl_plan]";
    	Cursor cursor = user_rdb.rawQuery(strSql,null);
        return fillPlanList(cursor);
    }
    
    public Plan GetPlanByName(String name)
    {
    	String strSql="Select * from [tbl_plan] where name=?";
    	String[] bindArgs ={name};
    	Cursor cursor = user_rdb.rawQuery(strSql,bindArgs);
        List<Plan> list=fillPlanList(cursor);
        return list.size()>0?list.get(0):null;
    }
    
    public List<Plan> queryShowPlanList()
    {
    	String strSql="Select * from [tbl_plan] where isArch=0";
    	Cursor cursor = user_rdb.rawQuery(strSql,null);
        List<Plan> list=new ArrayList<Plan>();
        return fillPlanList(cursor);
    }
    
    public List<Plan> queryArchPlanList()
    {
    	String strSql="Select * from [tbl_plan] where isArch=1";
    	Cursor cursor = user_rdb.rawQuery(strSql,null);
        List<Plan> list=new ArrayList<Plan>();
        return fillPlanList(cursor);
    }
    
    
    
    /********************************** Plan Operation********************************/
    
    /********************************** Info Operation********************************/
    public void InsertInfo(Info info)
    {
    	String strSql="Insert into [tbl_info](que,ans,pid) VALUES (?,?,?)";
    	Object[] bindArgs = { info.getQue(), info.getAns(),info.getPid()};
    	user_wdb.execSQL(strSql,bindArgs);
    }
    public void UpdateInfo(Info info)
    {
    	String strSql="Update [tbl_info] set que=?, ans=?, pid=? where iid=?";
    	Object[] bindArgs = { info.getQue(), info.getAns(),info.getPid(),info.getIid()};
    	user_wdb.execSQL(strSql,bindArgs);
    }
    public void deletInfo(int iid)
    {
    	String strSql="Delete from [tbl_info] where iid=?";
    	Object[] bindArgs ={iid};
    	user_rdb.execSQL(strSql,bindArgs);
    }
    private List<Info> fillInfoList(Cursor cursor)
    {
    	List<Info> list=new ArrayList<Info>();
        while (cursor.moveToNext())
        	 list.add(new Info(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3)));
        return list;
    }
    public Info describeInfo(int iid)
    {
    	String strSql="Select * from [tbl_info] where iid=?";
    	Info info =null;
    	String[] bindArgs ={String.valueOf(iid)};
    	Cursor cursor = user_rdb.rawQuery(strSql,bindArgs);
    	List<Info> list = fillInfoList(cursor);
        return list.size()>0?list.get(0):null;
    }
//    public List<Info> queryShowInfoList()
//    {
//    	String strSql="Select * from [tbl_info] where isArch=0";
//    	Cursor cursor = rdb.rawQuery(strSql,null);
//        return fillInfoList(cursor);
//    }
    public List<Info> queryInfoListByPid(int pid)
    {
    	String strSql="Select * from [tbl_info] where pid=?";
    	String[] bindArgs ={String.valueOf(pid)};
    	Cursor cursor = user_rdb.rawQuery(strSql,bindArgs);
        return fillInfoList(cursor);
    }
    
}
