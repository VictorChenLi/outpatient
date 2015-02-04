package com.outpatient.sysUtil.service;

import com.outpatient.sysUtil.model.Constant;
import com.outpatient.sysUtil.model.Constant.alertMusic;
import com.outpatient.williamosler.R;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore.Audio.Media;

public class PlayAlarmMusic extends Service {

	private MediaPlayer myMediaPlayer;
	private Vibrator vibrator;
	private Handler handler = new Handler();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		myMediaPlayer = MediaPlayer.create(this, R.raw.alarm);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		myMediaPlayer.stop();
		vibrator.cancel();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		KeyguardManager  km= (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardLock kl = km.newKeyguardLock("unLock"); 
		kl.disableKeyguard();
		PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright"); 
		wl.acquire();
		wl.release();
		alertMusic alertmusic = SettingHelper.getAlertMusic(this);
		if(alertmusic.equals(Constant.alertMusic.MUSIC)||alertmusic.equals(Constant.alertMusic.BOTH))
		{
			myMediaPlayer.start();
			myMediaPlayer.setLooping(true);
			handler.postDelayed(stopMedia, 1000*60);
		}
		if(alertmusic.equals(Constant.alertMusic.VIBRATOR)||alertmusic.equals(Constant.alertMusic.BOTH))
		{
			long[] pattern = {1000, 2000, 1000, 2000}; // OFF/ON/OFF/ON...
	        vibrator.vibrate(pattern, 0);

	    	handler.postDelayed(stopVibrator, 1000*60);
		}
		super.onStart(intent, startId);
	}
	
	private Runnable stopMedia = new Runnable() {
	   @Override
	   public void run() {
	      /* do what you need to do */
		   myMediaPlayer.stop();
	      /* and here comes the "trick" */
	      handler.postDelayed(this, 100);
	   }
	};
	
	private Runnable stopVibrator = new Runnable() {
		   @Override
		   public void run() {
		      /* do what you need to do */
			   vibrator.cancel();
		      /* and here comes the "trick" */
		      handler.postDelayed(this, 100);
		   }
		};
	
}
