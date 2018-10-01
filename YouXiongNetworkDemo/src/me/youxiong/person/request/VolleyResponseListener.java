package me.youxiong.person.request;

import com.android.volley.Response;

public abstract interface VolleyResponseListener {

	public abstract Response.Listener<String> getResponseListener();
	public abstract Response.ErrorListener getResponseErrorListener();
	
}
