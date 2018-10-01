package me.youxiong.person.ui.base;

import me.youxiong.person.R;
import me.youxiong.person.request.MyStringRequest;
import me.youxiong.person.request.VolleyResponseListener;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.config.YXConstants;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends FragmentActivity implements VolleyResponseListener {

	private RequestQueue mRequestQueue;
	private int mRequestType = 0;
	private boolean mBackAnimEnable = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		if(YXConstants.UMENG_ENABLE) {
			MobclickAgent.setDebugMode(true);
			// 禁止使用页面统计
			// MobclickAgent.openActivityDurationTrack(false);
			MobclickAgent.updateOnlineConfig(this);
		}
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		
	}
	protected void launchRequest(MyStringRequest request) {
		mRequestType = request.type;
		mRequestQueue.add(request);
	}
	
	protected void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	protected void showToast(int resId) {
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
	}
	protected void showLongToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	protected static Animation getRotateLoadingAnimation() {
		final RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
		rotateAnimation.setDuration(1500);		
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setRepeatCount(Animation.INFINITE);		
		return rotateAnimation;
	}
	
	protected void exitActivity() {
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	/**
	 * 初始化 View 组件
	 */
	protected abstract void initViewComponent();
	/**
	 * 一些初始化操作
	 */
	protected abstract void bindData();
	/**
	 * 注册事件监听
	 */
	protected abstract void registerListener();
	
	@Override
	protected void onResume() {
		super.onResume();
		if(YXConstants.UMENG_ENABLE) {
			MobclickAgent.onResume(this);
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		if(YXConstants.UMENG_ENABLE) { 
			MobclickAgent.onPause(this);
		}
	}
	public void setBackAnimEnable(boolean enable) {
		mBackAnimEnable = enable;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(mBackAnimEnable) {
			exitActivity();
		}
	}
	
	@Override
	public Response.Listener<String> getResponseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Logger.e("onRequestSuccessed====" + response);
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
