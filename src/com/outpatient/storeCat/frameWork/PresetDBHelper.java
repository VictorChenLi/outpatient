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

public class PresetDBHelper extends SQLiteOpenHelper {

	public static final String TB_Info="tbl_info";
	public static final String Iid="iid";
	public static final String Str_Que="que";
	public static final String Str_Ans="ans";
	
	
	public PresetDBHelper(Context context,String name,CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		
		
		
		

	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
