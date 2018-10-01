package me.youxiong.person.ui;

import me.youxiong.person.R;
import me.youxiong.person.model.PersionBriefInfo;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.fragment.base.FragmentFactory;
import me.youxiong.person.view.PagerSlidingTabStrip;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PersonalInfoActivity extends BaseFragmentActivity implements OnClickListener {
	
	private MyPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private PagerSlidingTabStrip mPagerStrip;
	
	private PopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.base_jazzviewpager_layout);
	}

	@Override
	protected void initViewComponent() {
		mPagerStrip = ((PagerSlidingTabStrip) findViewById(R.id.tabs));
		mViewPager = ((ViewPager) findViewById(R.id.jazzy_pager));
	}

	@Override
	protected void bindData() {
		setTileText(getString(R.string.person_title));
		// mIconOption.setVisibility(View.VISIBLE);
		mIconOption.setImageResource(R.drawable.card_option);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("info", (PersionBriefInfo) getIntent().getSerializableExtra("info"));
		bundle.putBoolean("islogin", getIntent().getBooleanExtra("islogin", false));
		
		mAdapter = new MyPagerAdapter(getSupportFragmentManager(), bundle);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setAdapter(mAdapter);
		
		mPagerStrip.setShouldExpand(true);
		mPagerStrip.setViewPager(mViewPager);
		
	}

	@Override
	protected void registerListener() {
		mIconOption.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initPopupWindow();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_add_friend:
			showAddFriendDialog();
			break;
		case R.id.popup_add_attention:
			showAddAttentionDialog();
			break;
		case R.id.popup_add_black:
			showAddBlackDialog();
			break;
		case R.id.popup_transmit:
			Intent intent = new Intent();
			intent.setClass(this, GotoTransmitActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		if(null != mPopupWindow && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void initPopupWindow() {
		View container = View.inflate(PersonalInfoActivity.this, R.layout.layout_popupwindow, null);
		TextView popupAddFriend = (TextView) container.findViewById(R.id.popup_add_friend);
		TextView popupAddAttention = (TextView) container.findViewById(R.id.popup_add_attention);
		TextView popupAddBlack = (TextView) container.findViewById(R.id.popup_add_black);
		TextView popupTransmit = (TextView) container.findViewById(R.id.popup_transmit);
		popupAddFriend.setOnClickListener(this);
		popupAddAttention.setOnClickListener(this);
		popupAddBlack.setOnClickListener(this);
		popupTransmit.setOnClickListener(this);
		
		DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        
        mPopupWindow = new PopupWindow(container, 
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        
        int[] location = new int[2];  
        mTitleLayout.getLocationOnScreen(location);
          
        mPopupWindow.showAtLocation(mTitleLayout, Gravity.TOP|Gravity.RIGHT, 0, location[1] + mTitleLayout.getHeight());
        // mPopupWindow.showAsDropDown(mTitleLayout);
        
	}
	private void showAddFriendDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.person_add_friend);
		builder.setMessage("是否加好友");
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/// 发请求，是否退出
				
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.cancel, diaologCancelListener);
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	private void showAddAttentionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.person_add_attention);
		builder.setMessage(R.string.person_add_attention_msg);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/// 发请求，是否退出
				
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.cancel, diaologCancelListener);
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	private void showAddBlackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.person_add_black);
		builder.setMessage("是否加黑名单");
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				/// 发请求，是否退出
				
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.cancel, diaologCancelListener);
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private DialogInterface.OnClickListener diaologCancelListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter {
		private final String[] TITLES = PersonalInfoActivity.this.getResources().getStringArray(R.array.tab_person_info);
		private Bundle mBundle;
		
		public MyPagerAdapter(FragmentManager fragmentManager, Bundle bundle) {
			super(fragmentManager);
			mBundle = bundle;
		}

		@Override
		public int getCount() {
			return this.TITLES.length;
		}

		@Override
		public Fragment getItem(int paramInt) {
			FragmentFactory.FragmentKey fragmentKey;
			switch (paramInt) {
			default:
				fragmentKey = FragmentFactory.FragmentKey.PERSON_INFO;
				break;
			case 1:
				fragmentKey = FragmentFactory.FragmentKey.PERSON_PRODUCT;
				break;
			}
			return FragmentFactory.newInstance(fragmentKey, mBundle);
		}

		@Override
		public CharSequence getPageTitle(int paramInt) {
			return this.TITLES[paramInt];
		}
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

}
