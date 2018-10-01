package me.youxiong.person.model;

import java.io.Serializable;

/**
 * 查看 persion 简要信息的请求(AdapterMain)
 * @author mythwind
 *
 */
public class WorkTimeInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String start_week;
	private String stop_week;
	private String start_time;
	private String stop_time;
	
	public String getStart_week() {
		return start_week;
	}
	public void setStart_week(String start_week) {
		this.start_week = start_week;
	}
	public String getStop_week() {
		return stop_week;
	}
	public void setStop_week(String stop_week) {
		this.stop_week = stop_week;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getStop_time() {
		return stop_time;
	}
	public void setStop_time(String stop_time) {
		this.stop_time = stop_time;
	}
}
