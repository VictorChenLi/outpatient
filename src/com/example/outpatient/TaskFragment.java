package com.example.outpatient;

import java.util.ArrayList;

import com.example.outpatient.fragment.adapters.InfoListAdapter;
import com.example.outpatient.fragment.adapters.PlanListAdapter;
import com.example.outpatient.fragment.adapters.TaskListAdapter;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.GlobalVar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TaskFragment extends Fragment implements Callback{
	
	private DBAccessImpl dbhandler;
	private Button addBtn;
	private TaskListAdapter taskListAdapter; 
	private ArrayList<Task> task_list;
	private ListView task_listview;
	
	// this is to identify receiving data update from the EditTaskActivity
	public static final int EDIT_TASK_RESULT = 1001;
	
	// this is to identify receiving data update from the EditTaskActivity
	public static final int ADD_TASK_RESULT = 1005;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.task_fragment, container, false);
		
		addBtn = (Button) rootView.findViewById(R.id.add_button);
		task_listview = (ListView)rootView.findViewById(R.id.task_listview);
		
		 // 1. pass context and data to the custom adapter
		if(GlobalVar.taskAdapter==null)
			GlobalVar.taskAdapter = new TaskListAdapter(getActivity(), generateData());
		taskListAdapter = GlobalVar.taskAdapter;
		
		taskListAdapter = new TaskListAdapter(getActivity(), generateData());
		
        // 2. setListAdapter
		task_listview.setAdapter(taskListAdapter);
        
		task_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
		
        // On click listener for the Add button, add tasks and then refresh the task list
        addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Intent mIntent = new Intent(getActivity(), EditTaskActivity.class);
				startActivityForResult(mIntent, ADD_TASK_RESULT);
				
			}
		});
        
        // set on click listener for the list items
        task_listview.setOnItemClickListener(new OnItemClickListener()
        {
            
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position ,long arg3) {
				// TODO Auto-generated method stub
				//do the stuff
				
				//initiate a new bundle
				Bundle mBundle = new Bundle();
				int TaskID = task_list.get(position).getTid();
				mBundle.putInt("tid", TaskID);
				
				Log.v("debugtag","clicked="+TaskID);
				
	            Intent mIntent = new Intent(getActivity(), EditTaskActivity.class);
	            mIntent.putExtras(mBundle);
	            
	    		startActivityForResult(mIntent, EDIT_TASK_RESULT);
	    		
			}
        });
        
        
        task_listview.setOnLongClickListener(new View.OnLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onLongClick(View view) {
               
            	task_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                
                return true;
            }
        });
        
        task_listview.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
            	
            	// Capture total checked items
                final int checkedCount = task_listview.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                taskListAdapter.toggleSelection(position);
            	
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        
                    	 SparseBooleanArray selected = taskListAdapter.getSelectedIds();
		                 // Captures all selected ids with a loop
		                 for (int i = (selected.size() - 1); i >= 0; i--) {
		                     if (selected.valueAt(i)) {
		                         Task selecteditem = taskListAdapter.getItem(selected.keyAt(i));
		                         // Remove selected items following the ids
		                         taskListAdapter.remove(selecteditem);
		                     }
		                 }
                    	
                        mode.finish(); // Action picked, so close the CAB
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
			public boolean onCreateActionMode(ActionMode arg0, Menu menu) {
				getActivity().getMenuInflater().inflate(R.menu.delete_menu, menu);
                return true;
			}

        });
        
        
        
         
		return rootView;
	}
	
	
    private ArrayList<Task> generateData(){
        
        //read the tasks from database and add them to the list
        
        dbhandler = DBAccessImpl.getInstance(getActivity());
        
        //query all the tasks to be shown in the task list 
        task_list = (ArrayList<Task>) dbhandler.queryShowTaskList();
        
        return task_list;
    }
    
    
    
    
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		GlobalVar.taskAdapter.refreshTaskList(this.generateData());
		
		
		
	}


	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
       
    	if(requestCode == EDIT_TASK_RESULT){
    	   
    	   // this handles the event when successfully edited a task
    	   if(resultCode == Activity.RESULT_OK){
    		   
    		   Log.v("debugtag","successfully edited task!");
     		   
     		   //reload the database
     		   task_list = (ArrayList<Task>) DBAccessImpl.getInstance(getActivity()).queryShowTaskList();
     		   
     		   taskListAdapter.refreshTaskList(task_list);
    		   
    	   }
    	   
       }
    	
    	if(requestCode == ADD_TASK_RESULT){
     	   
     	   // this handles the event when successfully edited a task
     	   if(resultCode == Activity.RESULT_OK){
     		   
     		   Log.v("debugtag","successfully added task!");
     		   
     		   //reload the database
     		   task_list = (ArrayList<Task>) DBAccessImpl.getInstance(getActivity()).queryShowTaskList();
     		   
     		   taskListAdapter.refreshTaskList(task_list);
     		   
     	   }
     	   
        }
    }


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
