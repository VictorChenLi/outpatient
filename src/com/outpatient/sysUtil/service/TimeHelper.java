package com.outpatient.sysUtil.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.outpatient.sysUtil.model.Constant;
import com.outpatient.sysUtil.model.Constant.viewModel;

import android.R;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class TimeHelper {
	
	public static String MAXDATASTR="23:59";
	public static long DAYInMillis=86400000;
	public static final String STDDATAFORMAT="yyyy/MM/dd";
	public static final String STDTIMEFORMAT="HH:mm";
	
	public static long getMaxTimeInMillis()
	{
		Calendar data = Calendar.getInstance();
		data.set(Calendar.YEAR, data.getActualMaximum(Calendar.YEAR));
		return data.getTimeInMillis();
	}
	
	//the form of str_data is yyyy/MM/dd
	public static long getTimeInMillis(String str_data)
	{
		Calendar data = Calendar.getInstance();
		if(str_data.contains("/"))
		{
			String[] finishTime = str_data.split("/");
			data.clear();
			data.set(Integer.valueOf(finishTime[0]), Integer.valueOf(finishTime[1])-1, Integer.valueOf(finishTime[2]));
		}
		else
		{
			String[] finishTime = str_data.split(":");
			data.set(Calendar.HOUR_OF_DAY, Integer.valueOf(finishTime[0]));
			data.set(Calendar.MINUTE,Integer.valueOf(finishTime[1]));
			data.set(Calendar.SECOND, 0);
			data.set(Calendar.MILLISECOND, 0);
		}
		return data.getTimeInMillis();
	}
	
	// 5m, 5h, 5d
	public static long getPlainTimeInMillis(String str_time)
	{
		long investTime = 0;
		if(str_time.equals(""))
			return 0;
		
		int time_value = Integer.valueOf(str_time.substring(0, str_time.length()-1));
		if(str_time.contains("d"))
		{
			investTime = time_value*1000*24*60*60L;
		}
		else if(str_time.contains("h"))
		{
			investTime = time_value*1000*60*60L;
		}
		else if(str_time.contains("m"))
		{
			investTime = time_value*1000*60L;
		}
		return investTime;
	}
	
	public static String getPlainStrFromTimeMillis(long data)
	{
		long investTime=data/(1000*60);
		return String.valueOf(investTime)+"m";
		
	}
	
	public static String getPaintStrFromTimeMillis(long date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		return cal.getTime().toString();
	}
	
	public static int compareTimeFromMillis(long dateA, long dateB, int field)
	{
		Calendar targetA =Calendar.getInstance();
		targetA.setTimeInMillis(dateA);
		Calendar targetB = Calendar.getInstance();
		targetB.setTimeInMillis(dateB);
		return targetA.get(field)-targetB.get(field);
	}
	
	public static double getIdealPercent(long startTime, long deadline)
	{
		Calendar cur = Calendar.getInstance();
		if(cur.getTimeInMillis()<=startTime)
			return 0;
		if(cur.getTimeInMillis()>deadline)
			return 1;
		return (double)(cur.getTimeInMillis()-startTime)/(deadline-startTime);
	}
	
	public static Long getDayTime(Long targetTime)
	{
		Calendar targetCal = Calendar.getInstance();
		targetCal.setTimeInMillis(targetTime);
		targetCal.set(0, 0, 0);
		return targetCal.getTimeInMillis();
	}
	
	public static long[] generatePeriodTime(long startTime, long endTime, viewModel model)
	{
		Calendar cur = Calendar.getInstance();
		Calendar c_start=Calendar.getInstance();
		c_start.setTimeInMillis(startTime);
		Calendar c_end=Calendar.getInstance();
		c_end.setTimeInMillis(endTime);
		
		long start=0;
		long end=0;
		// if the current time is not within the period return double zero
		if(cur.getTimeInMillis()<startTime||cur.getTimeInMillis()>endTime)
			return new long[]{start,end}; 
			
		start=startTime;
		end=endTime;
		switch(model)
		{
			case WHOLE:
			{
				break;
			}
			case MONTH:
			{
				if((cur.get(Calendar.YEAR)!=c_start.get(Calendar.YEAR)
						||cur.get(Calendar.MONTH)!=c_start.get(Calendar.MONTH))
						&&cur.getTimeInMillis()>=startTime)
				{
					Calendar monthBegin=Calendar.getInstance(Locale.CHINA);
					monthBegin.set(Calendar.DATE, 1); 
					monthBegin.set(Calendar.AM_PM, 0);
					monthBegin.clear(Calendar.HOUR_OF_DAY);
					monthBegin.clear(Calendar.MINUTE);
					monthBegin.clear(Calendar.SECOND);
					monthBegin.clear(Calendar.MILLISECOND);
					start=monthBegin.getTimeInMillis();
				}
				if((cur.get(Calendar.YEAR)!=c_end.get(Calendar.YEAR)
						||cur.get(Calendar.MONTH)!=c_end.get(Calendar.MONTH))
						&&cur.getTimeInMillis()<=endTime)
				{
					Calendar monthEnd=Calendar.getInstance(Locale.CHINA);
					monthEnd.add(Calendar.MONTH, 1);
					monthEnd.set(Calendar.DATE, 1); 
					monthEnd.set(Calendar.AM_PM, 0);
					monthEnd.clear(Calendar.HOUR_OF_DAY);
					monthEnd.clear(Calendar.MINUTE);
					monthEnd.clear(Calendar.SECOND);
					monthEnd.clear(Calendar.MILLISECOND);
					end=monthEnd.getTimeInMillis();
				}
				break;
			}
			case WEEK:
			{
				cur.add(Calendar.DATE, -1);
				c_start.add(Calendar.DATE, -1);
				c_end.add(Calendar.DATE, -1);
				if((cur.get(Calendar.YEAR)!=c_start.get(Calendar.YEAR)
						||cur.get(Calendar.MONTH)!=c_start.get(Calendar.MONTH)
						||cur.get(Calendar.WEEK_OF_MONTH)!=c_start.get(Calendar.WEEK_OF_MONTH))
						&&cur.getTimeInMillis()>=startTime)
				{
					Calendar c_begin=Calendar.getInstance(Locale.CHINA);
					c_begin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
					c_begin.set(Calendar.AM_PM, 0);
					c_begin.clear(Calendar.HOUR_OF_DAY);
					c_begin.clear(Calendar.MINUTE);
					c_begin.clear(Calendar.SECOND);
					c_begin.clear(Calendar.MILLISECOND);
					start=c_begin.getTimeInMillis();
				}
				if((cur.get(Calendar.YEAR)!=c_end.get(Calendar.YEAR)
						||cur.get(Calendar.MONTH)!=c_end.get(Calendar.MONTH)
						||cur.get(Calendar.WEEK_OF_MONTH)!=c_end.get(Calendar.WEEK_OF_MONTH))
						&&cur.getTimeInMillis()<=endTime)
				{
					Calendar c_last=Calendar.getInstance(Locale.CHINA);
					c_last.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					c_last.add(Calendar.WEEK_OF_MONTH, 1);
					c_last.set(Calendar.AM_PM, 0);
					c_last.clear(Calendar.HOUR_OF_DAY);
					c_last.clear(Calendar.MINUTE);
					c_last.clear(Calendar.SECOND);
					c_last.clear(Calendar.MILLISECOND);
					end=c_last.getTimeInMillis();
				}
				break;
			}
		}
		return new long[]{start,end};
	}
	
	private static double generateIdealPercent(long startTime, long endTime, viewModel model)
	{
		long[] period = generatePeriodTime(startTime,endTime,model);
		long start = period[0];
		long end = period[1];
		if(0==start&&0==end)
			return 0;
		double idealPercent = TimeHelper.getIdealPercent(start, end);
		return idealPercent*100;
	}
	
	public static String getStrTimeFromMillis(long date,String format)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		SimpleDateFormat myFormat = new SimpleDateFormat(format,Locale.CHINA); 
		return myFormat.format(c.getTime());
	}
	
	public static String getWordTimeFromMillis(long date)
	{
		Calendar cur = Calendar.getInstance();
		Calendar target = Calendar.getInstance();
		target.setTimeInMillis(date);
		if(target.get(Calendar.YEAR)==cur.get(Calendar.YEAR))
		{
			if(target.get(Calendar.DAY_OF_YEAR)==cur.get(Calendar.DAY_OF_YEAR))
				return Constant.TODAY;
			if(target.get(Calendar.DAY_OF_YEAR)==cur.get(Calendar.DAY_OF_YEAR)+1)
				return Constant.TOMORROW;
		}
		
		return TimeHelper.getStrTimeFromMillis(date,"MM-dd");
		
	}
	
	public static OnDateSetListener getDataSetListener(final EditText[] editList)
	{
		return new OnDateSetListener()
		{
			public void onDateSet(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				monthOfYear++;
				for(EditText edit: editList)
					edit.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
			}
		};
	}
	
	public static OnTimeSetListener getTimeSetListener(final EditText[] editList)
	{
		return new OnTimeSetListener()
		{
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				for(EditText edit: editList)
					edit.setText(hourOfDay+":"+minute);
				
			}
		};
	}
	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		Goal goal = new Goal();
//		goal.setStartTime(Calendar.getInstance().getTimeInMillis()-26*60*60*1000);
//		goal.setEndTime(Calendar.getInstance().getTimeInMillis()+1*35*24*60*60*1000);
//		TimeHelper.generateIdealPercent(goal, viewModel.MONTH);
//	}

}
