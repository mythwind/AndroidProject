package me.youxiong.person.ui.base;

import me.youxiong.person.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author mythwind
 */
public abstract class BaseFragmentActivity extends BaseActivity {
	protected View mTitleLayout;
	protected ImageView mBack;
	protected ImageView mIconOption;
	protected ImageView mWaitOption;
	
	protected TextView mTitle;
	protected TextView mTextOption;
	
	protected ProgressBar mProgressBar;
	protected View mButtomLine;
	
	protected boolean bRedirectHome = false;  // 是否直接回到主界面
	
	private Animation mHideToShowAnim;
	private Animation mShowToHideAnim;
	private OnClickListener mBackViewListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_base_layout);
		initBaseTitle();
		initAnimation();
	}
	
	/**
	 * 继承 BaseTitleActivity ，必须在 onCreate() 方法调用。
	 * @param resId
	 */
	protected void appendMainBody(int resId) {
		LinearLayout mainBody = (LinearLayout) findViewById(R.id.layout_container);
		View view = LayoutInflater.from(this).inflate(resId, null);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		mainBody.addView(view, params);
		
		initViewComponent();
		bindData();
		registerListener();
		
		if(null != mBackViewListener) {
			mBack.setOnClickListener(mBackViewListener);
		} else {
			mBack.setOnClickListener(clickListener);
		}
		mBack.setOnTouchListener(getTouchAnimationListener());
		mIconOption.setOnTouchListener(getTouchAnimationListener());
		mTextOption.setOnTouchListener(getTouchAnimationListener());
	}
	
	protected void appendMainBody(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.layout_container, fragment);
		transaction.commitAllowingStateLoss();
		
		initViewComponent();
		bindData();
		registerListener();
		
		if(null != mBackViewListener) {
			mBack.setOnClickListener(mBackViewListener);
		} else {
			mBack.setOnClickListener(clickListener);
		}
		mBack.setOnTouchListener(getTouchAnimationListener());
		mIconOption.setOnTouchListener(getTouchAnimationListener());
		mTextOption.setOnTouchListener(getTouchAnimationListener());
	}
	
	protected void setBackViewListener(OnClickListener clickListener) {
		mBackViewListener = clickListener;
	}
	
	protected void setTileText(String text) {
		mTitle.setText(text);
	}
	protected void setTileText(int textResId) {
		mTitle.setText(textResId);
	}
	
	protected void hideTitleLayout(boolean isHideTitleLayout) {
		if(isHideTitleLayout) {
			mTitleLayout.setVisibility(View.GONE);
		} else {
			mTitleLayout.setVisibility(View.VISIBLE);
		}
	}
	protected void initAnimation() {
		mHideToShowAnim = AnimationUtils.loadAnimation(this, R.anim.hide_to_show);
		mShowToHideAnim = AnimationUtils.loadAnimation(this, R.anim.show_to_hide);
	}
	
	private void initBaseTitle() {
		mTitleLayout = findViewById(R.id.title_layout);
		mBack = (ImageView) findViewById(R.id.back);
		mIconOption = (ImageView) findViewById(R.id.icon_option);
		mWaitOption = (ImageView) findViewById(R.id.wait_option);
		mTitle = (TextView) findViewById(R.id.title);
		mTextOption = (TextView) findViewById(R.id.text_option);
		mProgressBar = (ProgressBar) findViewById(R.id.headProgressBar);
		mButtomLine = findViewById(R.id.line);
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				exitActivity();
				break;
			default:
				break;
			}
		}
	};
	
	protected OnTouchListener getTouchAnimationListener() {
		return new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					view.startAnimation(mShowToHideAnim);
					break;
				case MotionEvent.ACTION_UP:
					view.startAnimation(mHideToShowAnim);
					break;
				default:
					break;
				}
				return false;
			}
		};
	}
}
