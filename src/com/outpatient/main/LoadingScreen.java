package com.outpatient.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.outpatient.storeCat.frameWork.PresetDBHelper;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.sysUtil.model.GlobalVar;
import com.outpatient.williamosler.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoadingScreen extends Activity {
	
	private ProgressBar loadingBar;
	private TextView loadingText;
	
	private PrefetchData dataLoader;
	
	
	@Override
	public void onBackPressed() {
		
		//Simply disable back button
	}
	
	public void initGlobaAdapterl()
	{
//		if(null==GlobalVar.infoAdapter)
//		{
//			GlobalVar.infoAdapter = new InfoListAdapter(this);
//		}
//		if(null==GlobalVar.planAdapter)
//		{
//			GlobalVar.planAdapter = new PlanListAdapter(this);
//		}
//		if(null==GlobalVar.taskAdapter)
//		{
//			GlobalVar.taskAdapter = new TaskListAdapter(this);
//		}
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
						
						// initiate just to make sure the preset database is copied if haven't
						PresetDBHelper presetDB = PresetDBHelper.getInstance(getApplicationContext());
						
						loadingBar.setProgress(4);
						
						//Locally load preset tasks and infos to local varible for used below
						ArrayList<Plan> plan_list = new ArrayList<Plan>(presetDB.getPresetPlanList());

						ArrayList<Task> task_list = new ArrayList<Task>(presetDB.getPresetTaskList());
						
						ArrayList<Info> info_list = new ArrayList<Info>(presetDB.getPresetInfoList());
						
						
						
						//Directly assign the plan list from the database to the global variable
						GlobalVar.plan_list = plan_list;
						
						
						//Add all associated plans to the plan_task_list
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
						
						
						//Add all associated plans to the plan_info_list
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
		                    Thread.sleep(500);
		                    
		                } catch (InterruptedException e)
		                {
		                    e.printStackTrace();
		                }
						
						
						loadingBar.setProgress(8);
						
						//Should there be no reminders by default?
					
//						GlobalVar.task_reminder_list = new HashMap<Integer, Reminder>();
//						
//						for (int i = 0 ; i < reminder_list.size() ; i++){
//							
//							int tid = reminder_list.get(i).getTid();
//							
//							Reminder taskReminder = reminder_list.get(i);
//							
//							//when done, add the mapping to global variable
//							if(taskReminder!=null)GlobalVar.task_reminder_list.put(tid, taskReminder);
//							
//						}
					    
						loadingBar.setProgress(10);
						
						presetDB.closeDatabase();
						
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
	
}
