package com.outpatient.storeCat.model;

public class Reminder {
	private int rid;
	private int tid;
	private long startTime;
	private int isRoutine;
	private long endTime;
	private int repeatingDays;
	private int repeatingTimes;
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public int getIsRoutine() {
		return isRoutine;
	}
	public void setIsRoutine(int isRoutine) {
		this.isRoutine = isRoutine;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getRepeatingDays() {
		return repeatingDays;
	}
	public void setRepeatingDays(int repeatingDays) {
		this.repeatingDays = repeatingDays;
	}
	public int getRepeatingTimes() {
		return repeatingTimes;
	}
	public void setRepeatingTimes(int repeatingTimes) {
		this.repeatingTimes = repeatingTimes;
	}
	public Reminder(int rid, int tid, long startTime, int isRoutine,
			long endTime, int repeatingDays, int repeatingTimes) {
		super();
		this.rid = rid;
		this.tid = tid;
		this.startTime = startTime;
		this.isRoutine = isRoutine;
		this.endTime = endTime;
		this.repeatingDays = repeatingDays;
		this.repeatingTimes = repeatingTimes;
	}
	
	public Reminder() {
		super();
	}
	
	public String toString(){
		
		if(repeatingDays == 1)return repeatingTimes+" times daily";
		if(repeatingTimes == 1)return "Once every "+repeatingDays + " days";
		else {return "NOREMINDER";}
	}
	
	
}
