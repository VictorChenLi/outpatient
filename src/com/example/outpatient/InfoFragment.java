package com.example.outpatient;

import java.util.ArrayList;
import java.util.List;

import com.example.outpatient.fragment.adapters.InfoListAdapter;
import com.example.outpatient.fragment.adapters.PlanListAdapter;
import com.outpatient.storeCat.model.Info;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.model.GlobalVar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class InfoFragment extends Fragment{
	
	// shared preference used to retrieve app status
	private SharedPreferences appinfo = null;
	
	private InfoListAdapter infoListAdapter; 
	private ArrayList<Info> infoList;
	private ListView info_listview;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.info_fragment, container, false);

//		list_items = getResources().getStringArray(R.array.routine_list);
//		setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list_items));

		
		info_listview = (ListView) rootView.findViewById(R.id.info_listview);
		
		 // 1. pass context and data to the custom adapter
		infoListAdapter = new InfoListAdapter(getActivity(), generateData());
 
        // 2. setListAdapter
		info_listview.setAdapter(infoListAdapter);
      
        // set on click listener for the list items
		info_listview.setOnItemClickListener(new OnItemClickListener()
        {
            
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position ,long arg3) {
				// TODO Auto-generated method stub
				//do the stuff
				
			}
        });
         
		return rootView;
	}
	
	
    private ArrayList<Info> generateData(){
    	
    	//the following decided if the app runs for the first time and show consent form
//    	appinfo = getActivity().getSharedPreferences("appinfo", Context.MODE_PRIVATE);
//    	String[] planString = appinfo.getString("plans", "0").split(",");
//    	
//    	infoList = GlobalVar.plan_info_list.get("0");
//        
//        //read the plan list from the global variable 
//    	
//        return infoList;
    	ArrayList<Info> infoList = new ArrayList<Info>();
    	DBAccessImpl dbAccessImpl = DBAccessImpl.getInstance(getActivity());
    	for(Plan plan :dbAccessImpl.queryShowPlanList())
    	{
    		if(null!=GlobalVar.plan_info_list.get(plan.getPid()))
    			infoList.addAll(GlobalVar.plan_info_list.get(plan.getPid()));
    	}
    	return infoList;
    }
    
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
       
    	
    }

}
