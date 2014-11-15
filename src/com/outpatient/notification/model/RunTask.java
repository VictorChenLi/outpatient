package com.outpatient.notification.model;

public class RunTask extends Thread {
	public void Terminate()
	{
		this.interrupt();
	}
	
}
