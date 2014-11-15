package com.example.outpatient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.outpatient.notification.service.NotificationHelper;
import com.outpatient.storeCat.model.Info;
import com.outpatient.sysUtil.service.OutPatientService;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.app.ActionBar;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends FragmentActivity{
	
	private ViewPager pager;
	private TabsAdapter mTabsAdapter;
	

	// this is to identify receiving data update from the EditTaskActivity
	private static final int EDIT_TASK_RESULT = 1001;
	
	//Time out for the "choose from address book" button
	private static final int LOADING_SCREEN_RESULT = 2002;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		pager = new ViewPager(this);
		pager.setId(R.id.pager);
		setContentView(pager);
		
		final ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		mTabsAdapter = new TabsAdapter(this, pager);
		mTabsAdapter.addTab(bar.newTab().setText("Tasks").setIcon(R.drawable.ic_launcher), TaskFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("Info").setIcon(R.drawable.ic_launcher), InfoFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("Plan").setIcon(R.drawable.ic_launcher), PlanFragment.class, null);
        
		startService();
		
		Intent i = new Intent(MainActivity.this, LoadingScreen.class);
		startActivityForResult(i, LOADING_SCREEN_RESULT);
		
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
	        if (resultCode == RESULT_OK && data!=null) {
	        	
	        		Toast.makeText(MainActivity.this, "Load Data Succeess!", Toast.LENGTH_LONG).show();
	        	 
	             }else{Log.w("debugtag", "Warning: activity result not ok");}
		} else {
	        Log.w("debugtag", "Warning: no activity result found");
	    }
	}
    
    
    

}
