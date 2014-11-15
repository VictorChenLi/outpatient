package com.outpatient.storeCat.frameWork;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SQLiteHelper extends SQLiteOpenHelper {

	public static final String TB_Info="tbl_info";
	public static final String Iid="iid";
	public static final String Str_Que="que";
	public static final String Str_Ans="ans";
	
	public static final String TB_Plan="tbl_plan";
	public static final String Pid="pid";
	public static final String Str_Name="name";
	public static final String Int_PlanType="planType";
	public static final String Long_Date="date";
	public static final String Int_IsArch="isArch";
	
	public static final String TB_Task="tbl_task";
	public static final String Tid="tid";
	public static final String Str_Notes="notes";
	public static final String Int_TaskType="taskType";
	public static final String Str_Des="des";
	
	public static final String TB_Reminder="tbl_reminder";
	public static final String Rid="rid";
	public static final String Long_StartTime="startTime";
	public static final String Long_EndTime="endTime";
	public static final String Int_IsRoutine="isRoutine";
	public static final String Int_RepeatingDays="repeatingDays";
	public static final String Int_RepeatingTimes="repeatingTimes";

	
	
	public SQLiteHelper(Context context,String name,CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		String strCreatParam="";
		
		//Create tbl_info
		strCreatParam="CREATE TABLE IF NOT EXISTS "+TB_Info+" ( "+Iid+" INTEGER PRIMARY KEY, "+Str_Que+" VARCHAR, "+Str_Ans+" VARCHAR, ["+Pid+"] INTEGER)";
		arg0.execSQL(strCreatParam);
		
		//Create tbl_plan
		strCreatParam="CREATE TABLE IF NOT EXISTS "+TB_Plan+" ("+Pid+" INTEGER PRIMARY KEY, "+Str_Name+" VARCHAR,"+Int_PlanType+" INTEGER, "+Long_Date+" INTEGER, "+Int_IsArch+" INTEGER)";
		arg0.execSQL(strCreatParam);
		
		//Create tbl_task
		strCreatParam="CREATE TABLE IF NOT EXISTS "+TB_Task+" ("+Tid+" INTEGER PRIMARY KEY, "+Pid+" INTEGER NOT NULL, "+Str_Name+" VARCHAR, "+Str_Notes+" VARCHAR, "+Int_TaskType+" INTEGER, "+Str_Des+" VARCHAR, "+Int_IsArch+" INTEGER, "+Long_Date+" INTEGER)";
		arg0.execSQL(strCreatParam);
		
		//Create tbl_reminder
		strCreatParam="CREATE TABLE IF NOT EXISTS "+TB_Reminder+" ("+Rid+" INTEGER PRIMARY KEY, "+Tid+" INTEGER NOT NULL, "+Long_StartTime+" INTEGER, "+Int_IsRoutine+" INTEGER, "+Long_EndTime+" INTEGER, "+Int_RepeatingDays+" INTEGER, "+Int_RepeatingTimes+" INTEGER)";
		arg0.execSQL(strCreatParam);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		arg0.execSQL("DROP TABLE IF EXISTS "+TB_Task);
		arg0.execSQL("DROP TABLE IF EXISTS "+TB_Info);
		arg0.execSQL("DROP TABLE IF EXISTS "+TB_Task);
		arg0.execSQL("DROP TABLE IF EXISTS "+TB_Reminder);
		onCreate(arg0);
	}

}
