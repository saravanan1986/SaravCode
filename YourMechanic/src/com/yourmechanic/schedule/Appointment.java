package com.yourmechanic.schedule;

public class Appointment {

	private int startTime;
	private int endTime;
	public Appointment(int startTime, int endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public int getEndTime() {
		return endTime;
	}
	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
	@Override
	public String toString() {
		return new StringBuffer().append("[").append(startTime).append(",").append(endTime).append("]").toString();
	}
}
