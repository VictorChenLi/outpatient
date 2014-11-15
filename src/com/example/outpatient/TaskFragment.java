package com.example.outpatient;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TaskFragment extends ListFragment{
	
	// this is to identify receiving data update from the EditTaskActivity
	private static final int EDIT_TASK_RESULT = 1001;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.task_fragment, container, false);
	
		 // 1. pass context and data to the custom adapter
        TaskListAdapter adapter = new TaskListAdapter(getActivity(), generateData());
 
        // 2. setListAdapter
        setListAdapter(adapter);
       
		return rootView;
	}
	
	
    private ArrayList<Item> generateData(){
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(new Item("Item 1","First Item on the list"));
        items.add(new Item("Item 2","Second Item on the list"));
        items.add(new Item("Item 3","Third Item on the list"));
 
        return items;
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        //do the stuff
        Intent i = new Intent(getActivity(), EditTaskActivity.class);
		startActivityForResult(i, EDIT_TASK_RESULT);
		
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
