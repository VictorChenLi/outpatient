package com.outpatient.sysUtil.service;

import com.example.outpatient.R;
import com.outpatient.sysUtil.model.Constant;
import com.outpatient.sysUtil.model.Constant.alertMusic;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore.Audio.Media;

public class PlayAlarmMusic extends Service {

	private MediaPlayer myMediaPlayer;
	private Vibrator vibrator;
	
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
		}
		if(alertmusic.equals(Constant.alertMusic.VIBRATOR)||alertmusic.equals(Constant.alertMusic.BOTH))
		{
			long[] pattern = {1000, 2000, 1000, 2000}; // OFF/ON/OFF/ON...
	        vibrator.vibrate(pattern, 0);
		}
		super.onStart(intent, startId);
	}

}
