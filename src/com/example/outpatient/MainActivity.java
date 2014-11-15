package com.example.outpatient;

import com.outpatient.notification.service.NotificationHelper;
import com.outpatient.sysUtil.service.OutPatientService;

import android.app.Activity;
import android.app.Fragment;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity{
	
	private ViewPager pager;
	private TabsAdapter mTabsAdapter;
	
	
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
        
		NotificationHelper.setNotification(this, "outpatient", "Let's win this");
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

}
