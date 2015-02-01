package com.outpatient.main;

import java.util.ArrayList;

import com.example.outpatient.R;
import com.outpatient.fragment.adapters.TabsAdapter;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.service.DBAccessImpl;
import com.outpatient.sysUtil.service.OutPatientService;

import android.os.Bundle;
import android.util.Log;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends FragmentActivity{
	
	private ViewPager pager;
	private TabsAdapter mTabsAdapter;
	
	// shared preference used to retrieve app status
	private SharedPreferences appinfo = null;
	
	// this is to identify receiving data update from the EditTaskActivity
	private static final int EDIT_TASK_RESULT = 1001;
	
	//Time out for the "choose from address book" button
	private static final int LOADING_SCREEN_RESULT = 2002;
	
	//Time out for the "choose from address book" button
	private static final int SELECT_PLAN_RESULT = 3003;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		pager = new ViewPager(this);
		pager.setId(R.id.pager);
		setContentView(pager);
	
		
		//the following decided if the app runs for the first time
		appinfo = getSharedPreferences("appinfo", Context.MODE_PRIVATE);
		
		Intent i = new Intent(MainActivity.this, LoadingScreen.class);
		startActivityForResult(i, LOADING_SCREEN_RESULT);
		
		startService();
		
    }
    
    
    public void startService()
	{
		Intent serviceIntent = new Intent(this,OutPatientService.class);
		this.startService(serviceIntent);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == LOADING_SCREEN_RESULT) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	
	        		// retrieve all the plan from the database
	        		ArrayList<Plan> currentPlans = (ArrayList<Plan>) DBAccessImpl.getInstance(getApplicationContext()).queryShowPlanList();
	    				    			
		        	// if use hasn't picked a plan, will prompt them to choose one
		    		if(currentPlans.size()==0){
		    			
		    			Intent planInt = new Intent(MainActivity.this, SelectPlanActivity.class);
		    			startActivityForResult(planInt, SELECT_PLAN_RESULT);
		    			
		    		}else{
		    			
		    			final ActionBar bar = getActionBar();
		        		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		        		
		        		mTabsAdapter = new TabsAdapter(this, pager);
		        		mTabsAdapter.addTab(bar.newTab().setIcon(R.drawable.task_icon), TaskFragment.class, null);
		        		mTabsAdapter.addTab(bar.newTab().setIcon(R.drawable.info_icon), InfoFragment.class, null);
		        		mTabsAdapter.addTab(bar.newTab().setIcon(R.drawable.plan_icon), PlanFragment.class, null);
		                
		    		}
		    		
		    		
	        		
	        		Toast.makeText(MainActivity.this, "Load Data Succeess!", Toast.LENGTH_LONG).show();
	        	 
	             }else{Log.w("debugtag", "Warning: activity result not ok");}
		} 
		else {
	        Log.w("debugtag", "Warning: no activity result found");
	    }
		
		
		if (requestCode == SELECT_PLAN_RESULT) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	
	        		Toast.makeText(MainActivity.this, "Selected Plan!", Toast.LENGTH_LONG).show();
	        	 
	        		final ActionBar bar = getActionBar();
	        		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        		
	        		mTabsAdapter = new TabsAdapter(this, pager);
	        		
	        		mTabsAdapter.addTab(bar.newTab().setIcon(R.drawable.task_icon), TaskFragment.class, null);
	        		mTabsAdapter.addTab(bar.newTab().setIcon(R.drawable.info_icon), InfoFragment.class, null);
	        		mTabsAdapter.addTab(bar.newTab().setIcon(R.drawable.plan_icon), PlanFragment.class, null);
	                
	        			
	             }else{Log.w("debugtag", "Warning: activity result not ok");}
		} else {
	        Log.w("debugtag", "Warning: no activity result found");
	    }
		
	}

}
