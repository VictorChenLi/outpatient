package com.example.outpatient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.outpatient.fragment.adapters.InfoListAdapter;
import com.example.outpatient.fragment.adapters.PlanListAdapter;
import com.example.outpatient.fragment.adapters.TaskListAdapter;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.sysUtil.model.GlobalVar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoadingScreen extends Activity {
	
	private ProgressBar loadingBar;
	private TextView loadingText;
	
	private PrefetchData dataLoader;
	
	//ArrayLists to store all the parsed data
	private ArrayList<Task> task_list;
	private ArrayList<Info> info_list;
	private ArrayList<Plan> plan_list;
	private ArrayList<Reminder> reminder_list;
	
	@Override
	public void onBackPressed() {
		
		//Simply disable back button
	}
	
	public void initGlobaAdapterl()
	{
		if(null==GlobalVar.infoAdapter)
		{
			GlobalVar.infoAdapter = new InfoListAdapter(this);
		}
		if(null==GlobalVar.planAdapter)
		{
			GlobalVar.planAdapter = new PlanListAdapter(this);
		}
		if(null==GlobalVar.taskAdapter)
		{
			GlobalVar.taskAdapter = new TaskListAdapter(this);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		loadingBar = (ProgressBar)findViewById(R.id.loadingProgress);
		loadingText = (TextView)findViewById(R.id.loadingtext);
		
		/*
		 * Showing splash screen while making network calls to download necessary
		 * data before launching the app Will use AsyncTask to make http call
		 */
		dataLoader = new PrefetchData();
		dataLoader.execute();
	
	}

	/*
	 * Async Task to make http call
	 */
	private class PrefetchData extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			loadingBar.setProgress(1);
			
		}

		@Override
		protected String doInBackground(String... arg0) {
				    
					try {
						
						// parse all the data from text file
						getTaskList();
						getPlanList();
						getInfoList();
						getReminderList();
						
						loadingBar.setProgress(3);
						
						//store all data in Global Variables
						GlobalVar.plan_list = plan_list;
						
						
						//store data in the plan_task_list
						GlobalVar.plan_task_list = new HashMap<Integer, ArrayList<Task>>();
						
						for (int i = 0 ; i < plan_list.size() ; i++){
							
							int pid = plan_list.get(i).getPid();
							
							ArrayList<Task> plantask_list = new ArrayList<Task>();
							
							for (int j = 0; j < task_list.size() ; j++ )
								
								{
									// if found the tasks for that plan, add to the list
									if(task_list.get(j).getPid() == pid)plantask_list.add(task_list.get(j));
									
								}
							
							//when done, add the mapping to global variable
							GlobalVar.plan_task_list.put(pid, plantask_list);
							
						}
						
						loadingBar.setProgress(6);
						
						//store data in the plan_info_list
						GlobalVar.plan_info_list = new HashMap<Integer, ArrayList<Info>>();
						
						for (int i = 0 ; i < plan_list.size() ; i++){
							
							int pid = plan_list.get(i).getPid();
							
							ArrayList<Info> planinfo_list = new ArrayList<Info>();
							
							for (int j = 0; j < info_list.size() ; j++ )
								
								{
									// if found the tasks for that plan, add to the list
									if(info_list.get(j).getPid() == pid)planinfo_list.add(info_list.get(j));
									
								}
							
							//when done, add the mapping to global variable
							GlobalVar.plan_info_list.put(pid, planinfo_list);
							
						}
						
						
						try
		                {
		                    Thread.sleep(1500);
		                    
		                } catch (InterruptedException e)
		                {
		                    e.printStackTrace();
		                }
						
						
						loadingBar.setProgress(8);
						
						//Store data in the task_reminder_list
						//store data in the plan_info_list
						GlobalVar.task_reminder_list = new HashMap<Integer, Reminder>();
						
						for (int i = 0 ; i < reminder_list.size() ; i++){
							
							int tid = reminder_list.get(i).getTid();
							
							Reminder taskReminder = reminder_list.get(i);
							
							//when done, add the mapping to global variable
							if(taskReminder!=null)GlobalVar.task_reminder_list.put(tid, taskReminder);
							
						}
					    
						loadingBar.setProgress(10);
						
					    return "ok"; 
					    
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return "no";
					}					
		}

		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if(result.equals("ok")){
			
				// when date is ready we loading it to the global adapter
				initGlobaAdapterl();
				
				// After completing http call
				// will close this activity and lauch main activity
				Intent resultIntent = new Intent(LoadingScreen.this, MainActivity.class);
				setResult(Activity.RESULT_OK, resultIntent);
				
				// close this activity
				finish();
				
			}else if(result.equals("no")){

				//error message 
				Toast.makeText(getApplicationContext(),"Error loading data, please try again", Toast.LENGTH_SHORT).show();
				
				// make visible the retry button
				loadingBar.setVisibility(View.GONE);
				loadingText.setVisibility(View.GONE);
				
			}
			
		}

	}
	
	
	private void getInfoList() throws IOException{
		
		info_list = new ArrayList<Info>();
		 
		int cursor = 0; //set cursor to 0
		
		InputStream input = getAssets().open("info.csv");
	    String str = convertStreamToString(input);
	    String[] strArray = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	    
	    while (cursor < strArray.length){
			int iid = 0;
			int pid = 0;
			String que = "";
			String ans = "";
			
			iid = (int)Integer.parseInt(strArray[cursor].replace("\"", "").replace("\n", "").replace("\r", "")); //reading iid
			cursor++; // move to the next
			
			pid = (int)Integer.parseInt(strArray[cursor].replace("\"", "")); //reading pid
			cursor++; // move to the next
			
			//Error Control
			if(!strArray[cursor].replace("\"", "").equals(""))
			que = strArray[cursor].replace("\"", ""); //read que
			cursor++; // move to the next
			
			//Error Control
			if(!strArray[cursor].replace("\"", "").equals(""))
			ans = strArray[cursor].replace("\"", "");//read que
			cursor++; // move to the next
			
			info_list.add(new Info(iid, que, ans, pid));
			
		}
		
	}
	
	private void getPlanList() throws IOException{
		
		plan_list = new ArrayList<Plan>();
		 
		int cursor = 0; //set cursor to 0
		
		InputStream input = getAssets().open("plan.csv");
	    String str = convertStreamToString(input);
	    String[] strArray = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	    
	    while (cursor < strArray.length){
			int pid = 0;
			int pType = 0;
			String name = "";
			
			pid = (int)Integer.parseInt(strArray[cursor].replace("\"", "").replace("\n", "").replace("\r", "")); 
			cursor++; // move to the next
			
			//Error Control
			if(!strArray[cursor].replace("\"", "").equals(""))
			pType = (int)Integer.parseInt(strArray[cursor].replace("\"", ""));
			cursor++; // move to the next
			
			//Error Control
			if(!strArray[cursor].replace("\"", "").equals(""))
			name = strArray[cursor].replace("\"", ""); 
			cursor++; // move to the next
			
			cursor++; // passed date field
			
			cursor++; // passed date isArch field
			
			plan_list.add(new Plan(pid,name,pType,0,0));
			
		}
		
	}
	
	private void getTaskList() throws IOException{
		
		task_list = new ArrayList<Task>();
		 
		int cursor = 0; //set cursor to 0
		
		InputStream input = getAssets().open("task.csv");
	    String str = convertStreamToString(input);
	    String[] strArray = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	    
	    while (cursor < strArray.length){
			int tid = 0;
			int pid = 0;
			String name = "";
			int taskType = 0;
			String description = "";
			
			tid = (int)Integer.parseInt(strArray[cursor].replace("\"", "").replace("\n", "").replace("\r", "")); 
			cursor++; // move to the next
			
			pid = (int)Integer.parseInt(strArray[cursor].replace("\"", ""));
			cursor++; // move to the next
			
			//Error Control
			if(!strArray[cursor].replace("\"", "").equals(""))
			name = strArray[cursor].replace("\"", ""); 
			cursor++; // move to the next
			
			cursor++; // pass notes
			
			//Error Control
			if(!strArray[cursor].replace("\"", "").equals(""))
			taskType = (int)Integer.parseInt(strArray[cursor].replace("\"", "")); 
			cursor++; // move to the next
			
			//Error Control
			if(!strArray[cursor].replace("\"", "").equals(""))
			description = strArray[cursor].replace("\"", ""); 
			cursor++; // move to the next
			
			cursor++; // passed isArch field
			
			cursor++; // passed date field
			
			task_list.add(new Task(tid,pid,name,"",taskType,description,0,0));
			
		}
		
	}


	private void getReminderList() throws IOException{
		
		reminder_list = new ArrayList<Reminder>();
		 
		int cursor = 0; //set cursor to 0
		
		InputStream input = getAssets().open("reminder.csv");
	    String str = convertStreamToString(input);
	    String[] strArray = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
	    
	    while (cursor < strArray.length){
			int rid = 0;
			int tid = 0;
			int isRoutine;
			int repeatingDays = 0;
			int repeatingTimes = 0;
			
			rid = (int)Integer.parseInt(strArray[cursor].replace("\"", "").replace("\n", "").replace("\r", "")); 
			cursor++; // move to the next
			
			tid = (int)Integer.parseInt(strArray[cursor].replace("\"", "").replace("\n", "").replace("\r", ""));
			cursor++; // move to the next
			
			cursor++; // move to the next
			
			isRoutine = (int)Integer.parseInt(strArray[cursor].replace("\"", "").replace("\n", "").replace("\r", ""));
			cursor++; // move to the next
			
			cursor++; // move to the next
			
			//Error Control
			if(!strArray[cursor].replace("\"", "").equals(""))
			repeatingDays = (int)Integer.parseInt(strArray[cursor].replace("\"", "").replace("\n", "").replace("\r", ""));
			cursor++; // move to the next
			
			//Error Control
			if(!strArray[cursor].replace("\"", "").equals(""))
			repeatingTimes = (int)Integer.parseInt(strArray[cursor].replace("\"", "").replace("\n", "").replace("\r", ""));
			cursor++; // move to the next
			
			reminder_list.add(new Reminder(rid,tid,0,isRoutine,0,repeatingDays,repeatingTimes));
			
		}
		
	}
	
	
	 private String convertStreamToString(InputStream is) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();

	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
	
}
