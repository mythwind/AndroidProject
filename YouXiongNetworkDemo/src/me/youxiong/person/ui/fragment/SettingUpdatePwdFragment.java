package me.youxiong.person.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.UserLoginActivity;
import me.youxiong.person.ui.fragment.base.BaseFragment;
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

/**
 * 
 *******************************************************************************
 * <p>ClassName:	SettingUpdatePwdFragment.java   
 * <p>Created:		2015   2015-5-31    上午11:43:00    
 * <p>Description:	TODO  修改用户密码
 * <p>Department:	软件开发事业部
 * <p>Author:		shusheng.wang@3dbizhi.com
 * 
 * 修改记录:
 *
 ********************************************************************************
 */
public class SettingUpdatePwdFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = SettingUpdatePwdFragment.class.getSimpleName();
	private static SettingUpdatePwdFragment mInstance;
	private EditText mOriginalEditText;
	private EditText mPasswordEditText;
	private EditText mPasswordConfirmEditText;
	private TextView mCommitTextView;
	
	private SettingUpdatePwdFragment() {}
	public synchronized static SettingUpdatePwdFragment getInstannce() {
		if(mInstance == null) {
			mInstance = new SettingUpdatePwdFragment();
		}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting_update_pwd, null);
		mOriginalEditText = (EditText) view.findViewById(R.id.setting_et_update_pwd_original);
		mPasswordEditText = (EditText) view.findViewById(R.id.setting_et_update_pwd_new);
		mPasswordConfirmEditText = (EditText) view.findViewById(R.id.setting_et_update_pwd_confirm);
		mCommitTextView = (TextView) view.findViewById(R.id.setting_tv_update_pwd_commit);
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
		mCommitTextView.setOnClickListener(this);
		
	}
	
	private Map<String, String> getRequestParams() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		// 重新获取数据
		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		
		String strOriginalPwd = mOriginalEditText.getText().toString();
		String strNewPwd = mPasswordEditText.getText().toString();
		String strConfirmPwd = mPasswordConfirmEditText.getText().toString();
		
		params.put("OldPasswd", strOriginalPwd.trim());
		params.put("NewPasswd", strNewPwd.trim());
		params.put("ReNewPasswd", strConfirmPwd.trim());
        
		Logger.i(TAG + "   persionfragment----username.trim()=" + username.trim());
		Logger.i(TAG + "   persionfragment----password.trim()=" + password.trim());
        
		return params;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_tv_update_pwd_commit:
			if(validate()) {
				MyRequestOperation operation = new MyRequestOperation(
						MyRequestFactory.REQUEST_TYPE_SETTING_EDIT_PASSWORD, getRequestParams());
				
				Logger.i(TAG + "  MyRequestFactory----operation=" + operation);
				launchRequest(MyRequestFactory.execute(operation, this));
			}
			break;
		default:
			break;
		}
	}
	
	private boolean validate() {
		String strOriginalPwd = mOriginalEditText.getText().toString();
		String strNewPwd = mPasswordEditText.getText().toString();
		String strConfirmPwd = mPasswordConfirmEditText.getText().toString();
		if(TextUtils.isEmpty(strOriginalPwd)) {
			showToast(R.string.hint_user_password_old);
			return false;
		}
		if (TextUtils.isEmpty(strNewPwd)) {
			showToast(R.string.hint_user_password_new);
			return false;
		}
		if (TextUtils.isEmpty(strConfirmPwd)) {
			showToast(R.string.hint_user_password_confirm);
			return false;
		}
		if (!strNewPwd.equals(strConfirmPwd)) {
			showToast(R.string.hint_user_password_differ);
			return false;
		}
		return true;
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_SETTING_EDIT_PASSWORD:
			handleRequestSuccess(response);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
	
	private void handleRequestSuccess(String response) {
		Logger.d(TAG + "---------->response=" + response);
		ReturnJsonModel json = JsonParseUtil.parse(response);
		if(null != json) {
			if(json.getIsSuccess()) {
				showToast(R.string.hint_password_edit_success);

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
		}
	}
	
}
