package com.muzhi.camerasdk;



import com.muzhi.camerasdk.library.utils.MResource;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


public abstract class BaseActivity extends FragmentActivity{
	
	protected Context mContext;
	protected TextView btn_back,mActionBarTitle;
	protected ImageView mIvBtnBack;
	protected ImageView mIvBtnComplite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//不可横屏幕
		
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.camerasdk_action_bar);*/
	}
	
	/*@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}*/
	
	
	
	public void setActionBarTitle(String s) {
		if(mActionBarTitle==null){
			mActionBarTitle=(TextView)findViewById(MResource.getIdRes(mContext, "camerasdk_actionbar_title"));
		}
		mActionBarTitle.setText(s);
	}
	
	//显示返回按钮
	public void showLeftIcon(){
		if (btn_back == null) {
			btn_back = (TextView) findViewById(MResource.getIdRes(mContext, "camerasdk_btn_back"));
		}
		// btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if(mIvBtnBack == null) {
			mIvBtnBack = (ImageView) findViewById(MResource.getIdRes(mContext, "camerasdk_iv_back"));
		}
		mIvBtnBack.setVisibility(View.VISIBLE);
		mIvBtnBack.setImageResource(R.drawable.camerasdk_selector_edit_back);
		mIvBtnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	protected void showRightIcon(int resId, OnClickListener listener) {
		if(mIvBtnComplite == null) {
			mIvBtnComplite = (ImageView) findViewById(MResource.getIdRes(mContext, "camerasdk_title_imgv_right_icon"));
		}
		mIvBtnComplite.setVisibility(View.VISIBLE);
		if(resId <= 0) {
			// 默认图标
			mIvBtnComplite.setImageResource(R.drawable.camerasdk_selector_edit_done);
		} else {
			mIvBtnComplite.setImageResource(resId);
		}
		mIvBtnComplite.setOnClickListener(listener);
	}
	protected ImageView getRightImageView() {
		if(mIvBtnComplite == null) {
			mIvBtnComplite = (ImageView) findViewById(MResource.getIdRes(mContext, "camerasdk_title_imgv_right_icon"));
		}
		return mIvBtnComplite;
	}
	
}
