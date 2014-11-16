package com.outpatient.sysUtil.service;

import java.util.Calendar;

import com.outpatient.notification.service.NotificationHandler;
import com.outpatient.notification.service.NotificationMgr;
import com.outpatient.storeCat.model.Plan;
import com.outpatient.storeCat.model.Reminder;
import com.outpatient.storeCat.model.Task;
import com.outpatient.storeCat.service.DBAccessImpl;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class OutPatientService extends Service {
	
	private OutPatientService demo;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void resumeAllReminder()
	{
		Log.v("reminder", "resumeAllReminder");
		NotificationMgr notifyMgr = new NotificationMgr(demo);
		notifyMgr.resumeAllReminder();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate(){
		demo=this;
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(1416061258000L);
		
		Log.v("reminder", cal.getTime().toString());
		resumeAllReminder();
//		Toast.makeText(this, "OutPatient Service is Created", Toast.LENGTH_LONG).show();
	}

}
