package com.example.outpatient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.outpatient.fragment.adapters.PlanListAdapter;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.GlobalVar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
    @Override
    
    
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_plan);
        
		addBtn = (Button)findViewById(R.id.add_button);
		plan_listview = (ListView)findViewById(R.id.plan_listview);
//		selectedList = new ArrayList<Integer>();
		dbAccessImpl = DBAccessImpl.getInstance(this);
		
		 // 1. pass context and data to the custom adapter
		planListAdapter = new PlanListAdapter(getApplicationContext(), generateData());
		
        // 2. setListAdapter
		plan_listview.setAdapter(planListAdapter);
		
        
        // On click listener for the Add button, add tasks and then refresh the task list
        addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					
//				SparseBooleanArray selected = planListAdapter.getSelectedIds();
                // Captures all selected ids with a loop
//                for (int i = (selected.size() - 1); i >= 0; i--) {
//                    if (selected.valueAt(i)) {
//                    	
//                        Plan selecteditem = planListAdapter.getItem(selected.keyAt(i));
//                        // Remove selected items following the ids
//                        DBAccessImpl.getInstance(getApplicationContext()).InsertPlan(selecteditem);
//                        
//                    }
//                }
					
			}
			
        });
        
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
               // Calls toggleSelection method from ListViewAdapter Class
//               planListAdapter.toggleSelection(position);
           	
           }

           @Override
           public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
               // Respond to clicks on the actions in the CAB
        	   Log.v("CAB", String.valueOf(item.getItemId()));
               switch (item.getItemId()) {
                   case R.id.action_confirm:
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
                	   List<Plan> planList = dbAccessImpl.queryShowPlanList();
                	   GlobalVar.planAdapter.refreshPlanList((ArrayList<Plan>) planList);
                	   List<Info> InfoList = dbAccessImpl.queryShowInfoList();
                	   GlobalVar.infoAdapter.refreshList((ArrayList<Info>) InfoList);
                	   List<Task> taskList = dbAccessImpl.queryShowTaskList();
                	   GlobalVar.taskAdapter.refreshTaskList((ArrayList<Task>) taskList);
                	   
            		   Intent resultIntent = new Intent(SelectPlanActivity.this, MainActivity.class);
            		   setResult(Activity.RESULT_OK, resultIntent);
        				
        				// close this activity
            		   SelectPlanActivity.this.finish();
//                   	
//                       mode.finish(); // Action picked, so close the CAB
                       return true;
                   default:
                       return false;
               }
           }

           @Override
           public void onDestroyActionMode(ActionMode mode) {
               // Here you can make any necessary updates to the activity when
               // the CAB is removed. By default, selected items are deselected/unchecked.
        	   
        	   
           }

           @Override
           public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
               // Here you can perform updates to the CAB due to
               // an invalidate() request
               return false;
           }


			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {

				 MenuInflater inflater = mode.getMenuInflater();
			      // assumes that you have "contexual.xml" menu resources
			      inflater.inflate(R.menu.action_menu, menu);
			      return true;
			}

       });
    }
	
		@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
			
	}

		private ArrayList<Plan> generateData(){
		    ArrayList<Plan> preSetPlanList = new ArrayList<Plan>(GlobalVar.plan_list);
			List<Plan> savedPlanList = dbAccessImpl.queryShowPlanList();
			//read the plan list from the global variable 
			planList = GlobalVar.plan_list;
		    for(Plan preSetPlan : preSetPlanList)
		    {
		    	for(Plan savedPlan:savedPlanList)
		    	{
		    		if(savedPlan.getName().equals(preSetPlan.getName()))
		    			planList.remove(savedPlan);
		    	}
		    }
		    selectedList = new Boolean[planList.size()];
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
