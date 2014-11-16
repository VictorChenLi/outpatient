package com.example.outpatient;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.outpatient.fragment.adapters.PlanListAdapter;
import com.outpatient.storeCat.model.Plan;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SelectPlanActivity extends Activity{
	
	// shared preference used to retrieve app status
	private SharedPreferences appinfo = null;
	
	private Button addBtn;
	private PlanListAdapter planListAdapter; 
	private ArrayList<Plan> planList;
	private ArrayList<Integer> selectedPlan;
	private ListView plan_listview;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_plan);
        
        //the following decided if the app runs for the first time and show consent form
      	appinfo = getSharedPreferences("plans", Context.MODE_PRIVATE);
        
		addBtn = (Button)findViewById(R.id.add_button);
		plan_listview = (ListView)findViewById(R.id.plan_listview);
		
		 // 1. pass context and data to the custom adapter
		planListAdapter = new PlanListAdapter(getApplicationContext(), generateData());
		
        // 2. setListAdapter
		plan_listview.setAdapter(planListAdapter);
		
		plan_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		
        
        // On click listener for the Add button, add tasks and then refresh the task list
        addBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					
				//changing activation status to activated
				Editor editor = appinfo.edit();
				
				String planString = "";
				
				for (int i = 0; i < selectedPlan.size(); i++){
					
					if(i != selectedPlan.size()-1)planString = planString + selectedPlan.get(i) +",";
					else {planString = planString + selectedPlan.get(i);}
					
				}
				
				// use has selected, go on
				if(planString!=""){
				
					//saving the pid and did to app settings for future use
					editor.putString("plans",planString);
					editor.commit();
					
					for(int i = 0; i < GlobalVar.plan_list.size(); i++){
						
						for (int j = 0; j < selectedPlan.size(); i ++){
								
								//if found a selected plan, add to the database
								if(GlobalVar.plan_list.get(i).getPid() == selectedPlan.get(j))
								DBAccessImpl.getInstance(getApplicationContext()).InsertPlan(GlobalVar.plan_list.get(i));
								
							}
					
					}
					
					Intent resultIntent = new Intent(SelectPlanActivity.this, MainActivity.class);
					setResult(Activity.RESULT_OK, resultIntent);
					
					// close this activity
					finish();
				
				}else {
					
					Toast.makeText(SelectPlanActivity.this, "Please select at least one plan.", Toast.LENGTH_SHORT).show();
					
				}
					
			}
			
        });
        
        
        
        
        
        // set on click listener for the list items
 	   plan_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               try{
            	   		
            	   		//if selected then cancel
            	   		if(selectedPlan.contains(planList.get(i).getPid())){
            	   			
            	   			plan_listview.getChildAt(i).setBackgroundColor(Color.WHITE);
            	   			selectedPlan.remove(planList.get(i).getPid());
            	   			planListAdapter.notifyDataSetChanged();
            	   		
            	   		}
            	   		else {
            	   			
            	   			plan_listview.getChildAt(i).setBackgroundColor(Color.BLACK);
                        	selectedPlan.add(planList.get(i).getPid());
                        	planListAdapter.notifyDataSetChanged();
            	   		
            	   		}
            	   
            	   Log.v("debugtag","selected="+selectedPlan.toString());	
            	   
                       
                }
               
               catch (Exception e){
                   e.printStackTrace();
               }
           }
       });

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
