package com.example.outpatient;

import java.util.ArrayList;

import com.example.outpatient.fragment.adapters.InfoListAdapter;
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
	private int ADD_PLAN_RESULT = 2001;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.plan_fragment, container, false);

		
		addBtn = (Button) rootView.findViewById(R.id.add_button);
		plan_listview = (ListView) rootView.findViewById(R.id.plan_listview);
		
		 // 1. pass context and data to the custom adapter
		if(GlobalVar.planAdapter==null)
			GlobalVar.planAdapter = new PlanListAdapter(getActivity(), generateData());
		planListAdapter = GlobalVar.planAdapter;
		
		planListAdapter = new PlanListAdapter(getActivity(), generateData());
 
        // 2. setListAdapter
		plan_listview.setAdapter(planListAdapter);
        
        // On click listener for the Add button, add tasks and then refresh the task list
        addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), SelectPlanActivity.class);
				startActivityForResult(mIntent, ADD_PLAN_RESULT);
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
//    	planList = GlobalVar.plan_list;
    	DBAccessImpl dbAccessImpl = DBAccessImpl.getInstance(getActivity());
    	planList = (ArrayList<Plan>) dbAccessImpl.queryShowPlanList();
        
        return planList;
    }
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == ADD_PLAN_RESULT){
      	   
      	   // this handles the event when successfully edited a task
      	   if(resultCode == Activity.RESULT_OK){
      		   
      		   Log.v("debugtag","successfully added plan!");
      		   
      		   //reload the database
      		   generateData();
      		   
      		   planListAdapter.refreshPlanList(planList);
      		   
      	   }
      	   
         }
    	
    }
}
