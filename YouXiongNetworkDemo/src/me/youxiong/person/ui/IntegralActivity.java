package me.youxiong.person.ui;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterIntagral;
import me.youxiong.person.model.IntegralInfoResult;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 我的龙币
 * @author mythwind
 *
 */
public class IntegralActivity extends BaseFragmentActivity {
	
	private View mLoadingView;
	private TextView mTvPuchase;
	private TextView mTvTotal;
	
	private PullToRefreshListView mPullToRefreshListView;
	private int mCurPage = 1;
	private int mPageSize = 15;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_integral);
	}
	
	@Override
	protected void initViewComponent() {
		mLoadingView = findViewById(R.id.hint_loading_layout);
		mTvPuchase = (TextView) findViewById(R.id.tv_integral_purchase);
		mTvTotal = (TextView) findViewById(R.id.tv_integral_total);
		
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.lv_integral);
		mLoadingView.setVisibility(View.VISIBLE);
		
	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		
		launchRequestIntegeral();
		
	}

	@Override
	protected void registerListener() {
		mTvPuchase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IntegralActivity.this, RechargeActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void launchRequestIntegeral() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_INTEGRAL, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 重新获取数据
		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("Page", String.valueOf(mCurPage));
		params.put("PerPage", String.valueOf(mPageSize));
		return params;
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_INTEGRAL:
			handleIntegral(response);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_INTEGRAL:
			
			break;
		default:
			break;
		}
	}
	
	private void handleIntegral(String response) {
		ReturnJsonModel model = JsonParseUtil.parse(response);
		if(null == model) {
			return;
		}
		if(model.getIsSuccess() && !TextUtils.isEmpty(model.getReturnValue())) {
			IntegralInfoResult result = JsonParseUtil.getIntegralInfoResult(model.getReturnValue());
			if(null != result) {
				if(null != result.my_integral_list) {
					mLoadingView.setVisibility(View.GONE);
					
					AdapterIntagral adapter = new AdapterIntagral(this);
					adapter.setList(result.my_integral_list);
					mPullToRefreshListView.setAdapter(adapter);
				}
				String total = getString(R.string.integral_total, result.total_integral);
				if(!TextUtils.isEmpty(total)) {
					mTvTotal.setText(Html.fromHtml(total));
				}
			}
		} else {
			showToast(model.getDescription());
		}
	}

}
