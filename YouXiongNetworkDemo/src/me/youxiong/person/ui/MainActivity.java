package me.youxiong.person.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterMain;
import me.youxiong.person.model.PersionBriefInfo;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.LoadImageUtils;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import me.youxiong.person.view.CircleImageView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.update.UmengUpdateAgent;

/**
 * 主 Activity
 */
public class MainActivity extends BaseFragmentActivity implements OnClickListener, OnItemClickListener {
	private static final String TAG = "MainActivity";
	
	// 百度定位 SDK
	private static final String TEMPCOOR = "gcj02";
	private static final int DEFAULT_SPAN = 5 * 60 * 1000; // 定位时间间隔，默认1000 毫秒
	
	private LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;
	
	private RelativeLayout mLoadingLayout;
	private TextView mLoadingText;
	private ImageView mIndividualCenter;
	private CircleImageView mCircleImageView;
	private TextView mSearchTitle;
	private PullToRefreshListView mRefreshListView;
	private ListView mListView;
	private AdapterMain mAdapter;
	
	private RadioButton mTabCreditTextView;
	private RadioButton mTabProfessionTextView;
	private RadioButton mTabPriceTextView;
	private RadioButton mTabDistanceTextView;
	
	private String mUserName = "";
	private String mPassword = "";
	
	private int mCurPage = 1;
	private int mPageSize = 15;
	
	private double mLatitude = 0;
	private double mLontitude = 0;
	
	private long mExitTime;
	private boolean hasMore = true;
	private boolean mHasRequested = false;
	
	private String mKeyWords = "";
	
	private SharedPreferences mPreferences;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(YXConstants.UMENG_ENABLE) {
	        UmengUpdateAgent.setUpdateOnlyWifi(false);
	        UmengUpdateAgent.update(this);
	        UmengUpdateAgent.setUpdateListener(null);
        }
        
        appendMainBody(R.layout.activity_main);
        initLocation();
        
    }
    
	@Override
	protected void initViewComponent() {
		mIndividualCenter = (ImageView) findViewById(R.id.user);
		mCircleImageView = (CircleImageView) findViewById(R.id.user_pic);
		
		mSearchTitle = (TextView) findViewById(R.id.main_search_title);
		
		mRefreshListView = (PullToRefreshListView) findViewById(R.id.main_listview);
		mLoadingLayout = (RelativeLayout) findViewById(R.id.hint_loading_layout);
		mLoadingText = (TextView) findViewById(R.id.hint_loading_text);
		
		mTabCreditTextView = (RadioButton) findViewById(R.id.main_tv_tab_credit);
		mTabProfessionTextView = (RadioButton) findViewById(R.id.main_tv_tab_profession);
		mTabPriceTextView = (RadioButton) findViewById(R.id.main_tv_tab_price);
		mTabDistanceTextView = (RadioButton) findViewById(R.id.main_tv_tab_distance);
		/*
		Typeface tf = FontsOverride.getFontTypeface();
		mTabCreditTextView.setTypeface(tf);
		*/
	}

	@Override
	protected void bindData() {
		hideTitleLayout(true);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 重新获取数据
		mUserName = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		mPassword = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		mTabDistanceTextView.setChecked(true);
		
		mAdapter = new AdapterMain(MainActivity.this);
		mListView = mRefreshListView.getRefreshableView();
        mListView.setAdapter(mAdapter);
        
//        if(mLontitude != 0 && mLontitude != 0) {
        	// 定位之后，发送请求消息请求附近的人
            launchRequestSearch();
            mHasRequested = true;
//        }
	}
	
	private void launchRequestSearch() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_SEARCH_LIST, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private void launchRequestSearchMore() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_SEARCH_LIST, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_TOKEN_KEY, mUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, mPassword.trim());
		/**
		 params.put(YXConstants.REQUEST_TOKEN_KEY, "");
		 params.put(YXConstants.REQUEST_TOKEN_SECRET, "");
		 */
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(MainActivity.this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("Terms", mKeyWords);
		params.put("Page", String.valueOf(mCurPage));
		params.put("PerPage", String.valueOf(mPageSize));
		/*
		//  BasicNetwork.performRequest: Unexpected response code 500 for http://alloong.com.cn/appSearch/actionIndex/
		params.put(YXConstants.REQUEST_LATITUDE, String.valueOf(mLatitude));
		params.put(YXConstants.REQUEST_LONTITUDE, String.valueOf(mLontitude));
		*/
		
		Logger.d("MainActivity----DeviceImsi=" + PackageUtil.getDeviceImsi(MainActivity.this));
		Logger.d("MainActivity----mLatitude=" + String.valueOf(mLatitude) + ", mLontitude=" + String.valueOf(mLontitude));
		Logger.d("MainActivity----mCurPage=" + String.valueOf(mCurPage) + ", mPageSize=" + String.valueOf(mPageSize));
		Logger.d("MainActivity----mKeyWords=" + mKeyWords);
		Logger.d("MainActivity----mUserName=" + mUserName.trim());
		Logger.d("MainActivity----mPassword=" + mPassword.trim());
		return params;
	}
	@Override
	protected void registerListener() {
		mRefreshListView.setOnRefreshListener(getOnRefresh2());
		mListView.setOnItemClickListener(this);
		///  mIndividualCenter.setOnClickListener(this);
		findViewById(R.id.fl_main_user).setOnClickListener(this);
		mSearchTitle.setOnClickListener(this);
		
		RadioGroup rg = (RadioGroup) findViewById(R.id.main_title_layout);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.main_tv_tab_credit:
					mTabCreditTextView.setChecked(true);
//					mAdapter.notifyDataChanged(SortType.CREDIT_TOTAL);
					break;
				case R.id.main_tv_tab_profession:
					mTabProfessionTextView.setChecked(true);
//					mAdapter.notifyDataChanged(SortType.PROFESSION_TOTAL);
					break;
				case R.id.main_tv_tab_price:
					mTabPriceTextView.setChecked(true);
//					mAdapter.notifyDataChanged(SortType.PRICE);
					break;
				case R.id.main_tv_tab_distance:
					mTabDistanceTextView.setChecked(true);
//					mAdapter.notifyDataChanged(SortType.DISTANCE);
					break;
				default:
					break;
				}
			}
		});
		
	}
	private OnRefreshListener2<ListView> getOnRefresh2() {
		return new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//				launchRequest(StoreRequestFactory.getUserPoints(1,pageSize));
				mCurPage = 1;
				launchRequestSearchMore();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if(hasMore) {
//					launchRequest(StoreRequestFactory.getUserPoints(page + 1,pageSize));
					launchRequestSearchMore();
				}
			}
		};
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 这里 position 从 1 开始？？？？
		PersionBriefInfo info = (PersionBriefInfo) mAdapter.getItem(position - 1);
		Logger.e("--------" + position + ",  userId=" + info.getUser_id());
		Intent intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
		intent.putExtra("info", info);
		intent.putExtra("islogin", isLogined());
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		// case R.id.user:
		case R.id.fl_main_user:
			// 先判断是否已经登陆
			Class<?> clazz = isLogined() ? IndividualCenterActivity.class : UserLoginActivity.class;
			if(clazz != null) {
				intent.setClass(MainActivity.this, clazz);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			break;
		case R.id.main_search_title:
			intent.setClass(MainActivity.this, SearchableActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// 重新获取数据
		mUserName = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		mPassword = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		mLatitude = mPreferences.getFloat(YXConstants.PREFS_LATITUDE, 0);
		mLontitude = mPreferences.getFloat(YXConstants.PREFS_LONTITUDE, 0);
		if(isLogined()) {
			mIndividualCenter.setVisibility(View.GONE);
			mCircleImageView.setVisibility(View.VISIBLE);
			
			PersonDetailInfo info = PreferencesUtility.getUserInfo(this);
			if(null != info && !TextUtils.isEmpty(info.getPhoto())) {
				LoadImageUtils.displayImage(info.getPhoto(), mCircleImageView, R.drawable.head_bg_selector);
			} else {
				mCircleImageView.setImageResource(R.drawable.head_bg_selector);
			}
		} else {
			mIndividualCenter.setVisibility(View.VISIBLE);
			mCircleImageView.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putFloat(YXConstants.PREFS_LATITUDE, (float) mLatitude);
		editor.putFloat(YXConstants.PREFS_LONTITUDE, (float) mLontitude);
		editor.commit();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null != mLocationClient) {
			mLocationClient.stop();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				showToast(R.string.exit_application);
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 初始化地理位置信息
	 */
	private void initLocation() {
		mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);//设置定位模式
		option.setCoorType(TEMPCOOR);// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(DEFAULT_SPAN);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("    error code : ");
			sb.append(location.getLocType());
			sb.append("   latitude : ");
			sb.append(location.getLatitude());
			sb.append("   lontitude : ");
			sb.append(location.getLongitude());
			sb.append(" 	radius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("	speed : ");
				sb.append(location.getSpeed());
				sb.append("	satellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("	direction : ");
				sb.append("	addr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("	addr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			mLatitude = location.getLatitude();
			mLontitude = location.getLongitude();
			if(!mHasRequested) {
				launchRequestSearch();
				mHasRequested = true;
			}
		}

	}
	
	/**
	 * 判断是否已经登录
	 * @return
	 */
	private boolean isLogined() {
		if(TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassword)) {
			return false;
		}
		return true;
	}

	/**
	 * 处理请求附近的人的返回信息列表
	 * @param response
	 */
	private void handleResponseResult(String response) {
		if(TextUtils.isEmpty(response)) {
			Log.e(TAG, TAG + " ***** handleResponseResult response is null!");
        	return;
		}
		ReturnJsonModel json = JsonParseUtil.parse(response);
        if (null == json) {
			Log.e(TAG, TAG + " ***** handleResponseResult json is null, parse error!");
        	return;
		}
        if (json.getIsSuccess() && !TextUtils.isEmpty(json.getReturnValue())) {
			ArrayList<PersionBriefInfo> briefInfos = JsonParseUtil.getSearch(json.getReturnValue());
			if (null != briefInfos) {
				mAdapter.setList(briefInfos);
				mAdapter.notifyDataSetChanged();
				if (!hasMore) {
					mRefreshListView.setMode(Mode.PULL_FROM_START);
				} else {
					mRefreshListView.setMode(Mode.BOTH);
				}
			}
		} else {
			showToast(json.getDescription());
		}
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_SEARCH_LIST:
			// response
            handleResponseResult(response);
            if(mLoadingLayout.getVisibility() == View.VISIBLE) {
            	mLoadingLayout.setVisibility(View.GONE);
            } else {
                mRefreshListView.onRefreshComplete();
            }
			break;

		default:
			break;
		}
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_SEARCH_LIST:
			mLoadingText.setText(R.string.network_timeout);
			((ImageView) findViewById(R.id.hint_loading_icon)).setImageResource(R.drawable.ic_error);
			if(mRefreshListView.isRefreshing()) {
				mRefreshListView.onRefreshComplete();
			}
			break;
		default:
			break;
		}
	}
}
