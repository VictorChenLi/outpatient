package com.outpatient.storeCat.frameWork;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Task;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PresetDBHelper extends SQLiteOpenHelper {
	
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "outpatient_preset.sqlite";
    public final static String DATABASE_PATH = "/data/data/com.outpatient/databases/";
    public static final int DATABASE_VERSION = 1;
    //public static final int DATABASE_VERSION_old = 1;

    //Constructor
    public PresetDBHelper(Context context)
    {
          super(context, DATABASE_NAME, null, DATABASE_VERSION);
          this.myContext=context;
          boolean dbexist = checkDataBase();
          
          if (dbexist) {
              //System.out.println("Database exists");
        	  openDatabase(); 
          } else {
              System.out.println("Database doesn't exist");
            try {
				createDatabase();
				
				//create and then open the database
				openDatabase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.v("debugtag","createDatabase Failed!");
			}
          }
    }

    //Create a empty database on the system
    public void createDatabase() throws IOException
    {
          boolean dbExist = checkDataBase();

          if(dbExist)
          {
                Log.v("db_operation", "db exists");
                // By calling this method here onUpgrade will be called on a
                // writable database, but only if the version number has been
                // bumped
                //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
          }
          
          boolean dbExist1 = checkDataBase();
          if(!dbExist1)
          {
                this.getReadableDatabase();
                try
                {
                      this.close();    
                      copyDataBase();
                }
                catch (IOException e)
                {
                      throw new Error("Error copying database");
             }
          }
    }

    //Check database already exist or not
    private boolean checkDataBase()
    {
          boolean checkDB = false;
          try
          {
                String myPath = DATABASE_PATH + DATABASE_NAME;
                File dbfile = new File(myPath);
                checkDB = dbfile.exists();
          }
          catch(SQLiteException e)
          {
          }
          return checkDB;
    }

    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException
    {
          String outFileName = DATABASE_PATH + DATABASE_NAME;
          OutputStream myOutput = new FileOutputStream(outFileName);
          InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

          byte[] buffer = new byte[1024];
          int length;
          while ((length = myInput.read(buffer)) > 0)
          {
                myOutput.write(buffer, 0, length);
          }
          myInput.close();
          myOutput.flush();
          myOutput.close();
    }

    //delete database
    public void db_delete()
    {
          File file = new File(DATABASE_PATH + DATABASE_NAME);
          if(file.exists())
          {
                file.delete();
                System.out.println("delete database file.");
          }
    }

    //Open database
    public void openDatabase() throws SQLException
    {
          String myPath = DATABASE_PATH + DATABASE_NAME;
          myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
          if(myDataBase != null)
                myDataBase.close();
          super.close();
    }

    public void onCreate(SQLiteDatabase db)
    {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {    
          if (newVersion > oldVersion)
          {
                Log.v("db_operation", "Database version higher than old.");
                db_delete();
          }
    }
    
    // Here are the public methods that read from the preset database
    
    
    // read all the preset plans
    public List<Plan> getPresetPlanList(){
    	
    	String strSql="Select * from [tbl_plan] where isArch=0";
    	Cursor cursor = myDataBase.rawQuery(strSql,null);
    	return fillPlanList(cursor);
    	
    }
    
    // read all the preset information by giving a plan ID
    public List<Info> getPresetInfoByPid(int pid){
    	
    	String strSql="Select * from [tbl_info] where pid = ?";
    	String[] bindArgs ={String.valueOf(pid)};
    	Cursor cursor = myDataBase.rawQuery(strSql,bindArgs);
        return fillInfoList(cursor);
    }
    
    // read all the preset plans
    public List<Task> getPresetTaskByPid(int pid){
    	
    	String strSql="Select * from [tbl_task] where pid=?";
    	String[] bindArgs ={String.valueOf(pid)};
    	Cursor cursor = myDataBase.rawQuery(strSql,bindArgs);
    	return fillTaskList(cursor);
    }
    
    private List<Task> fillTaskList(Cursor cursor)
    {
    	List<Task> list=new ArrayList<Task>();
        while (cursor.moveToNext())
        	 list.add(new Task(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getString(5),cursor.getInt(6),cursor.getLong(7)));
        return list;
    }
    
    private List<Plan> fillPlanList(Cursor cursor)
    {
    	List<Plan> list=new ArrayList<Plan>();
        while (cursor.moveToNext())
        	 list.add(new Plan(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getLong(3),cursor.getInt(4)));
        return list;
    }
    
    private List<Info> fillInfoList(Cursor cursor)
    {
    	List<Info> list=new ArrayList<Info>();
        while (cursor.moveToNext())
        	 list.add(new Info(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3)));
        return list;
    }

}