package com.outpatient.sysUtil.frameWork;

import com.outpatient.sysUtil.service.OutPatientService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "BootBroadcastReceiver is onReceive", Toast.LENGTH_LONG).show();
		Intent serviceIntent = new Intent(context,OutPatientService.class);
		context.startService(serviceIntent);
	}

}
