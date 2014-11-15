package com.outpatient.storeCat.model;

public class Plan {
	private int pid;
	private String name;
	private int planName;
	private int date;
	private int isArch;
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
	public int getPlanName() {
		return planName;
	}
	public void setPlanName(int planName) {
		this.planName = planName;
	}
	public int getDate() {
		return date;
	}
	public void setDate(int date) {
		this.date = date;
	}
	public int getIsArch() {
		return isArch;
	}
	public void setIsArch(int isArch) {
		this.isArch = isArch;
	}
	
	public Plan(int pid, String name, int planName, int date, int isArch) {
		super();
		this.pid = pid;
		this.name = name;
		this.planName = planName;
		this.date = date;
		this.isArch = isArch;
	}
	
	public Plan() {
		super();
	}
	
	

}
