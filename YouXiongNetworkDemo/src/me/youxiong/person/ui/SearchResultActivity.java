package me.youxiong.person.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterMain;
import me.youxiong.person.model.PersionBriefInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class SearchResultActivity extends BaseFragmentActivity implements OnItemClickListener {
	private static final String TAG = SearchResultActivity.class.getSimpleName();
	public static final String EXTRA_NAME = "extra_name";
	
	private PullToRefreshListView mRefreshListView;
	private ListView mListView;
	private AdapterMain mAdapter;
	
	private String mUserName = "";
	private String mPassword = "";
	private String mKeywords = "";
	private int mCurPage = 1;
	private int mPageSize = 15;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mKeywords = getIntent().getStringExtra(EXTRA_NAME);
		appendMainBody(R.layout.simple_refresh_listview);
		
	}
	
	@Override
	protected void initViewComponent() {
		mRefreshListView = (PullToRefreshListView) findViewById(R.id.simple_refresh_listview);
	}

	@Override
	protected void bindData() {
		setTileText(mKeywords);
		
		mAdapter = new AdapterMain(this);
		mListView = mRefreshListView.getRefreshableView();
        mListView.setAdapter(mAdapter);
        
        launchRequestSearch();
	}

	@Override
	protected void registerListener() {
		// TODO Auto-generated method stub
		mRefreshListView.setOnRefreshListener(getOnRefresh2());
		mListView.setOnItemClickListener(this);
	}
	
	private void launchRequestSearch() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_SEARCH_LIST, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 重新获取数据
		mUserName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		mPassword = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_TOKEN_KEY, mUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, mPassword.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("Terms", mKeywords);
		params.put("Page", String.valueOf(mCurPage));
		params.put("PerPage", String.valueOf(mPageSize));
		
		Logger.d("MainActivity----DeviceImsi=" + PackageUtil.getDeviceImsi(this));
		Logger.d("MainActivity----mKeyWords=" + mKeywords);
		Logger.d("MainActivity----mUserName=" + mUserName.trim());
		Logger.d("MainActivity----mPassword=" + mPassword.trim());
		return params;
	}

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		if(TextUtils.isEmpty(response)) {
			Log.e(TAG, TAG + " ******** request success, but response is null!");
			return ;
		}
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_SEARCH_LIST:
			handleResponseResult(response);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 这里 position 从 1 开始？？？？
		PersionBriefInfo info = (PersionBriefInfo) mAdapter.getItem(position - 1);
		Logger.e("--------" + position + ",  userId=" + info.getUser_id());
		Intent intent = new Intent(SearchResultActivity.this, PersonalInfoActivity.class);
		intent.putExtra("info", info);
		intent.putExtra("islogin", isLogined());
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
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
		mRefreshListView.onRefreshComplete();
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
//				if (!hasMore) {
//					mRefreshListView.setMode(Mode.PULL_FROM_START);
//				} else {
//					mRefreshListView.setMode(Mode.BOTH);
//				}
			}
		} else {
			showToast(json.getDescription());
		}
	}
	
	private OnRefreshListener2<ListView> getOnRefresh2() {
		return new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				mCurPage = 1;
				launchRequestSearch();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				mCurPage ++;
				launchRequestSearch();
			}
		};
	}

}
