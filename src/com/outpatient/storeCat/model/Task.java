package com.outpatient.storeCat.model;

public class Task {
	private int tid;
	private int pid;
	private String name;
	private String notes;
	private int taskType;
	private String des;
	private int isArch;
	private long date;
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public int getIsArch() {
		return isArch;
	}
	public void setIsArch(int isArch) {
		this.isArch = isArch;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public Task(int tid, int pid, String name, String notes, int taskType,
			String des, int isArch, long date) {
		super();
		this.tid = tid;
		this.pid = pid;
		this.name = name;
		this.notes = notes;
		this.taskType = taskType;
		this.des = des;
		this.isArch = isArch;
		this.date = date;
	}
	public Task() {
		super();
	}
	@Override
	public String toString() {
		return "Task [tid=" + tid + ", pid=" + pid + ", name=" + name
				+ ", notes=" + notes + ", taskType=" + taskType + ", des="
				+ des + ", isArch=" + isArch + ", date=" + date + "]";
	}
	
	
}
