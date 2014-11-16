package com.example.outpatient;

import java.util.ArrayList;

import com.example.outpatient.fragment.adapters.PlanListAdapter;
import com.example.outpatient.fragment.adapters.TaskListAdapter;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.GlobalVar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlanFragment extends Fragment{
	
	private Button addBtn;
	private PlanListAdapter planListAdapter; 
	private ArrayList<Plan> planList;
	private ListView plan_listview;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.plan_fragment, container, false);
		
		addBtn = (Button) rootView.findViewById(R.id.add_button);
		plan_listview = (ListView) rootView.findViewById(R.id.plan_listview);
		
		 // 1. pass context and data to the custom adapter
		planListAdapter = new PlanListAdapter(getActivity(), generateData());
 
        // 2. setListAdapter
		plan_listview.setAdapter(planListAdapter);
        
        // On click listener for the Add button, add tasks and then refresh the task list
        addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
			}
		});
        
        // set on click listener for the list items
        plan_listview.setOnItemClickListener(new OnItemClickListener()
        {
            
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position ,long arg3) {
				// TODO Auto-generated method stub
				//do the stuff
				String name = planList.get(position).getName();
				
				Toast.makeText(getActivity(), "clicked:"+name, Toast.LENGTH_LONG).show();
			}
        });
         
		return rootView;
	}
	
	
    private ArrayList<Plan> generateData(){
        
        //read the plan list from the global variable 
    	planList = GlobalVar.plan_list;
        
        return planList;
    }
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
       
    	
    }
}
