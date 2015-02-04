package com.outpatient.storeCat.model;

public class Plan {
	private int pid;
	private String name;
	private int planType;
	private long date;
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
	public int getPlanType() {
		return planType;
	}
	public void setPlanType(int planType) {
		this.planType = planType;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getIsArch() {
		return isArch;
	}
	public void setIsArch(int isArch) {
		this.isArch = isArch;
	}
	
	public Plan(int pid, String name, int planType, long date, int isArch) {
		super();
		this.pid = pid;
		this.name = name;
		this.planType = planType;
		this.date = date;
		this.isArch = isArch;
	}
	
	//overloaded constructor used by preset database
	public Plan(int pid, int planType, String name) {
		super();
		this.pid = pid;
		this.name = name;
		this.planType = planType;
	}
	
	public Plan() {
		super();
	}
	@Override
	public boolean equals(Object o) {
		if(o == null)                return false;
		if (!(o instanceof Plan)) 	 return false;
		   
	    Plan other = (Plan) o;
	    if(this.pid != other.pid || !this.name.equals(other.name))      return false;
		return true;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.pid;
	}
	
	
	
	

}
