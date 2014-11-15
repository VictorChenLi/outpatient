package com.example.outpatient;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskFragment extends ListFragment{
	String[] list_items;
	
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
}
