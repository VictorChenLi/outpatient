package com.example.outpatient;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.outpatient.fragment.adapters.PlanListAdapter;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Task;
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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.MultiChoiceModeListener;

public class SelectPlanActivity extends Activity{
	
	private Button addBtn;
	private PlanListAdapter planListAdapter; 
	private ArrayList<Plan> planList;
	private ListView plan_listview;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_plan);
        
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
					
//				SparseBooleanArray selected = planListAdapter.getSelectedIds();
                // Captures all selected ids with a loop
//                for (int i = (selected.size() - 1); i >= 0; i--) {
//                    if (selected.valueAt(i)) {
//                    	
//                        Plan selecteditem = planListAdapter.getItem(selected.keyAt(i));
//                        // Remove selected items following the ids
//                        DBAccessImpl.getInstance(getApplicationContext()).InsertPlan(selecteditem);
//                        
//                    }
//                }
					
			}
			
        });
        
     
        // set on click listener for the list items
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
//               planListAdapter.toggleSelection(position);
           	
           }

           @Override
           public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
               // Respond to clicks on the actions in the CAB
               switch (item.getItemId()) {
                   case R.id.action_delete:
                       
//                   	 SparseBooleanArray selected = planListAdapter.getSelectedIds();
//		                 // Captures all selected ids with a loop
//		                 for (int i = (selected.size() - 1); i >= 0; i--) {
//		                     if (selected.valueAt(i)) {
//		                         Plan selecteditem = planListAdapter.getItem(selected.keyAt(i));
//		                         // Remove selected items following the ids
//		                         planListAdapter.remove(selecteditem);
//		                     }
//		                 }
//                   	
//                       mode.finish(); // Action picked, so close the CAB
                       return true;
                   default:
                       return false;
               }
           }

           @Override
           public void onDestroyActionMode(ActionMode mode) {
               // Here you can make any necessary updates to the activity when
               // the CAB is removed. By default, selected items are deselected/unchecked.
        	   plan_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
           }

           @Override
           public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
               // Here you can perform updates to the CAB due to
               // an invalidate() request
               return false;
           }


			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
			      // assumes that you have "contexual.xml" menu resources
			      inflater.inflate(R.menu.delete_menu, menu);
			      return true;
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
