package com.outpatient.main;

import java.util.ArrayList;

import com.example.outpatient.R;
import com.outpatient.fragment.adapters.InfoListAdapter;
import com.outpatient.fragment.adapters.PlanListAdapter;
import com.outpatient.fragment.adapters.TaskListAdapter;
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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.MultiChoiceModeListener;
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
		planListAdapter = GlobalVar.getPlanListAdapter(getActivity());
//		planListAdapter.notifyDataSetChanged();
//		
//		planListAdapter = new PlanListAdapter(getActivity(), generateData());
 
        // 2. setListAdapter
		plan_listview.setAdapter(planListAdapter);
		
		plan_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        
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
        
        plan_listview.setOnLongClickListener(new View.OnLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onLongClick(View view) {
               
            	plan_listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                
                return true;
            }
        });
        
        
        // add action bar listener
        plan_listview.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
            	
            	// Capture total checked items
                final int checkedCount = plan_listview.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                planListAdapter.toggleSelection(position);
            	
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        
                    	 SparseBooleanArray selected = planListAdapter.getSelectedIds();
		                 // Captures all selected ids with a loop
		                 for (int i = (selected.size() - 1); i >= 0; i--) {
		                     if (selected.valueAt(i)) {
		                         Plan selecteditem = planListAdapter.getItem(selected.keyAt(i));
		                         // Remove selected items following the ids
		                         planListAdapter.remove(selecteditem);
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
//      		   generateData();
//      		   
//      		   planListAdapter.refreshPlanList(planList);
      		
      	   }
      	   
         }
    	
    }
}
