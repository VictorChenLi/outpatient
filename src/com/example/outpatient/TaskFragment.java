package com.example.outpatient;

import java.util.ArrayList;

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
	private static final int EDIT_TASK_RESULT = 1001;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.task_fragment, container, false);
		
		addBtn = (Button) rootView.findViewById(R.id.add_button);
		task_listview = (ListView) rootView.findViewById(R.id.task_listview);
		
		 // 1. pass context and data to the custom adapter
		taskListAdapter = new TaskListAdapter(getActivity(), generateData());
 
        // 2. setListAdapter
		task_listview.setAdapter(taskListAdapter);
        
        if(addBtn==null)Log.v("debugtag","addButton is null");
        
        // On click listener for the Add button, add tasks and then refresh the task list
        addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Task newtask = new Task(1,1,"Eat Healthy","Haha",0,"Just eat every healthy food!",0,1416050690);
				
				task_list.add(newtask);
				
				taskListAdapter.notifyDataSetChanged();
				
			}
		});
        
        // set on click listener for the list items
        task_listview.setOnItemClickListener(new OnItemClickListener()
        {
            
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				//do the stuff
	            Intent i = new Intent(getActivity(), EditTaskActivity.class);
	    		startActivityForResult(i, EDIT_TASK_RESULT);
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
    		   
    	   }
    	   
       }
    }
    
}
