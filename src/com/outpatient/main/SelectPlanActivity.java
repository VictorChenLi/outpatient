package com.outpatient.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.outpatient.fragment.adapters.PlanListAdapter;
import com.outpatient.storeCat.frameWork.PresetDBHelper;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.GlobalVar;
import com.outpatient.williamosler.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.OnItemClickListener;

public class SelectPlanActivity extends Activity{
	
	private Button addBtn;
	private PlanListAdapter planListAdapter; 
	private ArrayList<Plan> planList;
	private ListView plan_listview;
	private Boolean[] selectedList;	
	private DBAccessImpl dbAccessImpl;
	private Boolean flag=true;
	private PresetDBHelper presetDBAccess;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_plan);
        
		addBtn = (Button)findViewById(R.id.add_button);
		addBtn.setVisibility(View.GONE);
		
		plan_listview = (ListView)findViewById(R.id.plan_listview);
//		selectedList = new ArrayList<Integer>();
		
		//initiate data access to the predefined database
		presetDBAccess = new PresetDBHelper(getApplicationContext());
		
		//initiate the database access to the user database
		dbAccessImpl = DBAccessImpl.getInstance(this);
		
		 // 1. pass context and data to the custom adapter
		planListAdapter = new PlanListAdapter(getApplicationContext(), generateData());
		
        // 2. setListAdapter
		plan_listview.setAdapter(planListAdapter);
		
		flag=true;
		
        
       plan_listview.setOnItemClickListener(new OnItemClickListener()
       {
           
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position ,long arg3) {
				// TODO Auto-generated method stub
				//do the stuff

				plan_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//				SelectPlanActivity.this.startActionMode(new ActionBarCallBack());
				plan_listview.setItemChecked(position,true);
				
			}
       });
        
     
        // set on click listener for the list items
 	   plan_listview.setMultiChoiceModeListener(new MultiChoiceModeListener() {

           @Override
           public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                 long id, boolean checked) {
               // Here you can do something when items are selected/de-selected,
               // such as update the title in the CAB
//           	Log.v("mchoice", );
	           	// Capture total checked items
	               final int checkedCount = plan_listview.getCheckedItemCount();
	               // Set the CAB title according to total checked items
	               mode.setTitle(checkedCount + " Selected");
	               selectedList[position]=checked;
	               
//
//                   plan_listview.getChildAt(position).setBackgroundColor(0x30ACEC);
//	               plan_listview.invalidate();
//	               Log.v("interaction","interaction");
//               
//            	   plan_listview.getChildAt(position).setBackgroundColor(0xFFFFFF);
//	               plan_listview.invalidate();
	
	               
	               //HARD CODE: suggest wound care when selected Post-Appendicitis Care
//     			   if(planList.get(position).getPid()==6)Toast.makeText(SelectPlanActivity.this, R.string.recommend_wound, Toast.LENGTH_LONG).show();
     			 TextView tv_plan_name = (TextView)plan_listview.getChildAt(position).findViewById(R.id.plan_name);
     			 if(tv_plan_name.getText().equals("Post-Appendicitis Care"))
     				 Toast.makeText(SelectPlanActivity.this, R.string.recommend_wound, Toast.LENGTH_LONG).show();
	               
	               
               // Calls toggleSelection method from ListViewAdapter Class
//               planListAdapter.toggleSelection(position);
           		if(flag)
           		{
           			flag=false;
           			int doneButtonId = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");
                    View doneButton = SelectPlanActivity.this.findViewById(doneButtonId);
                    doneButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                     	// To save the plan, info, task and reminder
                     	   for(int i=0;i<selectedList.length;i++)
                     	   {
                     		   if(selectedList[i])
                     		   {
                     			   int pid = dbAccessImpl.InsertPlan(planList.get(i));
                     			   
                     			   //save the pre-setting task list
                         		   ArrayList<Task> taskList = GlobalVar.plan_task_list.get(planList.get(i).getPid());
                         		   for(Task task:taskList)
                         		   {
                         			   task.setPid(pid);
                         			   int tid = dbAccessImpl.InsertTask(task);
                         			   Reminder reminder = GlobalVar.task_reminder_list.get(task.getTid());
                         			   if(null!=reminder)
                         			   {
                         				   reminder.setTid(tid);
                         				   dbAccessImpl.InsertReminder(reminder);
                         			   }
                         		   }
                         		   //save the pre-setting info list
                         		   ArrayList<Info> infoList = GlobalVar.plan_info_list.get(planList.get(i).getPid());
                         		   for(Info info:infoList)
                         		   {
                         			   info.setPid(pid);
                         			   dbAccessImpl.InsertInfo(info);
                         		   }
                     		   }
                     	   }
                     	   
                     	   // update Plan, info and task list
                     	   GlobalVar.getPlanListAdapter(getApplicationContext()).refreshPlanList();
                     	   GlobalVar.getInfoListAdapter(getApplicationContext()).refreshInfoList();
                     	   GlobalVar.getTaskListAdapter(getApplicationContext()).refreshTaskList();
                     	   
                 		   Intent resultIntent = new Intent(SelectPlanActivity.this, MainActivity.class);
                 		   setResult(Activity.RESULT_OK, resultIntent);
             				
             				// close this activity
                 		   SelectPlanActivity.this.finish();
                        }
                    });
           		}
           }

           @Override
           public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
               // Respond to clicks on the actions in the CAB
        	   Log.v("CAB", String.valueOf(item.getItemId()));
               switch (item.getItemId()) {
                   case R.id.action_confirm:
                	  
                       return true;
                   default:
                       return false;
               }
           }

           @Override
           public void onDestroyActionMode(ActionMode mode) {
               // Here you can make any necessary updates to the activity when
               // the CAB is removed. By default, selected items are deselected/unchecked.
        	   
//               mode.finish(); // Action picked, so close the CAB
        	   
           }

           @Override
           public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
               // Here you can perform updates to the CAB due to
               // an invalidate() request
        	   
               return false;
           }


			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//				 MenuInflater inflater = mode.getMenuInflater();
			      // assumes that you have "contexual.xml" menu resources
//			      inflater.inflate(R.menu.action_menu, menu);
			      return true;
			}

       });
    }
    
	
		@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(dbAccessImpl.queryShowPlanList().size()>0)
		{
			Intent resultIntent = new Intent(SelectPlanActivity.this, MainActivity.class);
  		   	setResult(Activity.RESULT_OK, resultIntent);
			SelectPlanActivity.this.finish();
		}
		else
			Toast.makeText(SelectPlanActivity.this, R.string.firsttime_selectplan, Toast.LENGTH_LONG).show();
	}

		private ArrayList<Plan> generateData(){
//			Set<Plan> temp = new HashSet<Plan>(GlobalVar.plan_list);
//			Set<Plan> presettingSet = new HashSet<Plan>(GlobalVar.plan_list);
//			Set<Plan> savedPlanSet = new HashSet<Plan>(dbAccessImpl.queryShowPlanList());
			
			List<Plan> temp = new ArrayList<Plan>(GlobalVar.plan_list);
			List<Plan> preSetPlanList = new ArrayList<Plan>(GlobalVar.plan_list);
			List<Plan> savedPlanList = new ArrayList<Plan>(dbAccessImpl.queryShowPlanList());
			//read the plan list from the global variable 
		    for(Plan savedPlan : savedPlanList)
		    {
		    	for(Plan preSetPlan:preSetPlanList)
		    	{
		    		if(savedPlan.getName().equals(preSetPlan.getName()))
		    		{
		    			temp.remove(preSetPlan);
		    			break;
		    		}
		    	}
		    }
//			temp.retainAll(savedPlanSet);
//			presettingSet.removeAll(savedPlanSet);
//			savedPlanSet.removeAll(temp);
			planList = new ArrayList<Plan>(temp);
		    selectedList = new Boolean[planList.size()];
		    for(int i=0;i<planList.size();i++)
		    	selectedList[i]=false;
		    	
		    return planList;
		}
		
	
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
		}
		
	    class ActionBarCallBack implements ActionMode.Callback {
	    	 
	        @Override
	        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	            // TODO Auto-generated method stub
	            return false;
	        }
	 
	        @Override
	        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	            // TODO Auto-generated method stub
	            return false;
	        }
	 
	        @Override
	        public void onDestroyActionMode(ActionMode mode) {
	            // TODO Auto-generated method stub
	        	
	        }
	 
	        @Override
	        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	            // TODO Auto-generated method stub
	 
	            return false;
	        }
	 
	    }
	    
}
