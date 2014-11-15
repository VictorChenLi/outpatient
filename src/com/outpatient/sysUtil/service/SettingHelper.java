package com.outpatient.sysUtil.service;

import com.example.outpatient.R;
import com.outpatient.sysUtil.model.Constant.alertMusic;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingHelper {
	
	public static alertMusic getAlertMusic(Context context)
	{
		alertMusic alertmusic;
		try
		{
			String str_alertMusic = context.getResources().getString(R.string.setting_alertMusic);
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
			alertmusic = alertMusic.valueOf(settings.getString(str_alertMusic, alertMusic.BOTH.toString()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			alertmusic=alertMusic.BOTH;
		}
		return alertmusic;
	}
}
