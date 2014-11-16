package com.example.outpatient;

import java.util.ArrayList;

import com.example.outpatient.fragment.adapters.TaskListAdapter;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class TaskFragment extends Fragment{
	
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
		task_listview = (ListView) rootView.findViewById(R.id.task_listview);
		
		 // 1. pass context and data to the custom adapter
		taskListAdapter = new TaskListAdapter(getActivity(), generateData());
 
        // 2. setListAdapter
		task_listview.setAdapter(taskListAdapter);
        
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
				
	            Intent mIntent = new Intent(getActivity(), EditTaskActivity.class);
	            mIntent.putExtras(mBundle);
	            
	    		startActivityForResult(mIntent, EDIT_TASK_RESULT);
	    		
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
    
}
