package com.outpatient.sysUtil.model;

public class Constant {
	
	public enum taskStatus{WAIT,RUN,DELAY,START,EXTEND,COMPLETE,DISMISS,OVERDUE};
	
	public enum taskType
	{
		ALLDAY(1),ROUTINE(1<<1),AUTO(1<<2);
		
		private int value=0;
		private taskType(int value)
		{
			this.value=value;
		}
		
		public boolean isContain(int value)
		{
			return (value&this.value)==0?false:true;
		}
		
		public int add(int value)
		{
			return this.value|value;
		}
		
		public int del(int value)
		{
			return value^this.value;
		}
		
		public int value()
		{
			return this.value;
		}
	};
	
	public enum stageStatus{RUN,COMPLETE};
	
	public enum goalStatus{RUN,COMPLETE};
	
	public enum goalType{QUANIFIABLE,UNQUANIFIABLE};
	
	public enum listItemType{GOAL,STAGE,TASK,PRIZE};
	
	public enum alertType{STARTALERT,COMPLETEALERT,OVERDUEALERT};
	
	public enum viewModel{WHOLE,YEAR,MONTH,WEEK,DAY};
	
	public enum tabIndex{GOAL,TODO,GAME,FRIENDS,REVIEW};
	
	public enum alertMusic{MUSIC,VIBRATOR,BOTH,SILENCE};
	
	public enum prizeType{User,Public};
	
	public static final String TODAY="Today";
	public static final String TOMORROW="Tomorrow";
	public static final String UNDO="UnDo";
	public static final String DOING="Doing";
	public static final String OVERDUE="Overdue";
}
