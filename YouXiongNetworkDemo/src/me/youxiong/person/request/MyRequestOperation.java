package me.youxiong.person.request;

import java.util.Map;

import com.android.volley.Request;

public class MyRequestOperation {
	
	public int method;
	public int type;
	public Map<String, String> params;
	
	public MyRequestOperation(int type, Map<String, String> params) {
		method = Request.Method.POST;
		this.type = type;
		this.params = params;
	}
}
