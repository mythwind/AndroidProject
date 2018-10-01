package me.youxiong.person.request;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

public class MyStringRequest extends StringRequest {

	public int type;
	
	public MyStringRequest(String url, int requestType, Listener<String> listener, ErrorListener errorListener) {
		super(Request.Method.POST, url, listener, errorListener);
		this.type = requestType;
	}
	
}
