package me.youxiong.person.dialog;

import me.youxiong.person.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * 等待框
 * 
 * @author Mythwind
 * 
 */
public class WaitDialog extends Dialog  {

	/** 对话框占屏幕宽度 比例 */
	protected double perWidth = 0.5;
	protected boolean canceledOnTouchOutside = true;
	protected View root;
	protected Context context;
	private ImageView wait_img;
	private TextView wait_text;
	
	public WaitDialog(Context context) {
		super(context);
		this.context = context;
		root = LayoutInflater.from(context).inflate(R.layout.dialog_wait, null);
		initViews(root);
	}
	
	public WaitDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		root = LayoutInflater.from(context).inflate(R.layout.dialog_wait, null);
		initViews(root);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setGravity(Gravity.CENTER);
		setContentView(root);
		setCanceledOnTouchOutside(canceledOnTouchOutside);
	}

	protected void initViews(View root) {
		wait_img = (ImageView) root.findViewById(R.id.wait_img);
		wait_text = (TextView) root.findViewById(R.id.wait_text);
	}

	/**
	 * 屏幕宽度适配
	 */
	protected void screenAdapter(Context context) {
		WindowManager.LayoutParams params = getWindow().getAttributes();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		params.width = (int) Math.ceil(dm.widthPixels * perWidth);
		getWindow().setAttributes(params);
	}
	
	public void setWaitText(int resId){
		wait_text.setText(resId);
	}
	public void setWaitText(String msg){
		wait_text.setText(msg);
	}
	public void setWaitImage(int resId){
		wait_img.setBackgroundResource(resId);
		wait_img.setVisibility(View.VISIBLE);
		wait_img.postDelayed(new Runnable() {
			@Override
			public void run() {
				wait_img.setVisibility(View.GONE);
			}
		}, 2000);
	}
	
}
