package com.outpatient.storeCat.model;

public class Info {
	private int iid;
	private String que;
	private String ans;
	private int pid;
	public int getIid() {
		return iid;
	}
	public void setIid(int iid) {
		this.iid = iid;
	}
	public String getQue() {
		return que;
	}
	public void setQue(String que) {
		this.que = que;
	}
	public String getAns() {
		return ans;
	}
	public void setAns(String ans) {
		this.ans = ans;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public Info(int iid, String que, String ans, int pid) {
		super();
		this.iid = iid;
		this.que = que;
		this.ans = ans;
		this.pid = pid;
	}
	
	public Info()
	{
		super();
	}
	
	

}
