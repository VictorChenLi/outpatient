package com.example.outpatient;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TaskFragment extends ListFragment{
	String[] list_items;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.task_fragment, container, false);
		
		//Read all the list items from the database
		list_items = getResources().getStringArray(R.array.list);
		
		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_items));
		
		return rootView;
	}

}
