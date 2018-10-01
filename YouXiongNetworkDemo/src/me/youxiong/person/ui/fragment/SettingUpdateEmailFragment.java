package me.youxiong.person.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.UserLoginActivity;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class SettingUpdateEmailFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = SettingUpdateEmailFragment.class.getSimpleName();
	
	private static SettingUpdateEmailFragment mInstance;
	private EditText mEmailEditText;
	private TextView mTvCommit;
	
	private SettingUpdateEmailFragment() {}
	public synchronized static SettingUpdateEmailFragment getInstannce() {
		if(mInstance == null) {
			mInstance = new SettingUpdateEmailFragment();
		}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting_update_email, null);
		mEmailEditText = (EditText) view.findViewById(R.id.setting_et_update_email_edittext);
		mTvCommit = (TextView) view.findViewById(R.id.setting_tv_update_email_commit);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		bindData();
		registeListener();
	}

	private void bindData() {

	}

	private void registeListener() {
		mTvCommit.setOnClickListener(this);

	}
	
	private void launchRequestEmail() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_SETTING_EDIT_EMAIL, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	private Map<String, String> getRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		String strUserName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String strPassword = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		params.put(YXConstants.REQUEST_TOKEN_KEY, strUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, strPassword.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("NewEmail", mEmailEditText.getText().toString());
		return params;
	}
	
	private boolean validate() {
		String strEmail = mEmailEditText.getText().toString();
		if (TextUtils.isEmpty(strEmail)) {
			showToast(R.string.user_email_empty);
			return false;
		}
		if (!CommonUtils.isEmail(strEmail)) {
			showToast(R.string.user_email_error);
			return false;
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_tv_update_email_commit:
			if(validate()) {
				launchRequestEmail();
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		Logger.d("#### edit email onRequestSuccessed  response=" + response);
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_SETTING_EDIT_EMAIL:
			ReturnJsonModel json = JsonParseUtil.parse(response);
	        if(null != json) {
	        	if(json.getIsSuccess()) {
	        		showToast(json.getDescription());
	        		
	        		/*******  退出登陆并且跳转到登陆界面  *******/
	        		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
	    	 		SharedPreferences.Editor editor = preferences.edit();
	    	 		// 退出登录是否会记住用户名 ？
	    	 		editor.remove(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME));
	    	 		editor.remove(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD));
	    	 		editor.commit();
	        		Intent intent = new Intent(mActivity, UserLoginActivity.class);
	        		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // 设置不要刷新将要跳到的界面  
	        		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 它可以关掉所要到的界面中间的activity
	        		startActivity(intent);
	        		mActivity.finish();
	        		mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	        		
	        	} else {
	        		showToast(json.getDescription());
	        	}
	        } else {
	        	Logger.e(TAG + "  ProductFragment----json==null");
	        }
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

}
