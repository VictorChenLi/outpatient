package com.outpatient.sysUtil.service;

import java.util.Calendar;

import com.outpatient.notification.service.NotificationHandler;
import com.outpatient.notification.service.NotificationMgr;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class OutPatientService extends Service {
	
	private OutPatientService demo;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void taskJobs()
	{
		Intent intent = new Intent(this, NotificationHandler.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		getApplication().startActivity(intent);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate(){
		demo=this;
		Toast.makeText(this, "OutPatient Service is Created", Toast.LENGTH_LONG).show();
	}

}
