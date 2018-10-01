package me.youxiong.person.ui.fragment.base;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

import me.youxiong.person.request.MyStringRequest;
import me.youxiong.person.request.VolleyResponseListener;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.config.YXConstants;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment implements VolleyResponseListener {
	protected FragmentActivity mActivity;
	protected LayoutInflater mInflater;
	
	private RequestQueue mRequestQueue;
	private int mRequestType = 0;
	
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		mActivity = getActivity();
		mInflater = LayoutInflater.from(mActivity);
		mRequestQueue = Volley.newRequestQueue(mActivity.getApplicationContext());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(YXConstants.UMENG_ENABLE) {
			MobclickAgent.onPageStart(getClass().getSimpleName());
		}
	}
	@Override
	public void onPause() {
		super.onPause();
		if(YXConstants.UMENG_ENABLE) {
			MobclickAgent.onPageEnd(getClass().getSimpleName());
		}
	}

	protected void launchRequest(MyStringRequest request) {
		mRequestType = request.type;
		mRequestQueue.add(request);
	}
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}
	
	protected void showToast(String text) {
		Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
	}
	protected void showToast(int resId) {
		Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
	}
	protected void showLongToast(String text) {
		Toast.makeText(mActivity, text, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public Response.Listener<String> getResponseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				onRequestSuccessed(mRequestType, response);
			}
		};
	}
	
	public Response.ErrorListener getResponseErrorListener() {
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				StringBuilder errMsg = new StringBuilder("Response Error:");
				if(null != error) {
					if(null != error.networkResponse) {
						byte[] htmlBodyBytes = error.networkResponse.data;
						errMsg.append(new String(htmlBodyBytes));
						errMsg.append(",  ");
					}
					errMsg.append(error.getMessage());
					Logger.e("onRequestFailed====" + errMsg.toString());
				}
				onRequestFailed(mRequestType, errMsg.toString());
			}
		};
	}
	
	protected abstract void onRequestSuccessed(int requestType, String response);
	
	protected abstract void onRequestFailed(int requestType, String errMsg);
	
}
