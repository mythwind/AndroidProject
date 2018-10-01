package me.youxiong.person.ui;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.NetworkUtil;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 用户登陆的界面
 * 
 * @author mythwind
 * 
 */
public class UserLoginActivity extends BaseFragmentActivity implements OnClickListener {
	private static final String TAG = UserLoginActivity.class.getSimpleName();
	
	private EditText mEtUserName;
	private EditText mEtPassword;
	private TextView mTvLogin;
	private TextView mForgetPassword;

	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		appendMainBody(R.layout.activity_user_login);
		
	}

	@Override
	protected void initViewComponent() {
		mEtUserName = (EditText) findViewById(R.id.login_user);
		mEtPassword = (EditText) findViewById(R.id.login_password);
		mTvLogin = (TextView) findViewById(R.id.login);
		mForgetPassword = (TextView) findViewById(R.id.login_forget_password);
	}

	@Override
	protected void bindData() {
		setTileText(getString(R.string.login));
		mTextOption.setText(R.string.registe_title);
		mTextOption.setVisibility(View.VISIBLE);
	}

	@Override
	protected void registerListener() {
		mTvLogin.setOnClickListener(this);
		mTextOption.setOnClickListener(this);
		
		mForgetPassword.setOnClickListener(this);
		mForgetPassword.setOnTouchListener(getTouchAnimationListener());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			String strUsername = mEtUserName.getText().toString().trim();
			String strPassword = mEtPassword.getText().toString().trim();
			if (checkLogin(strUsername, strPassword)) {
				doLogin(strUsername, strPassword);
			}
			break;
		case R.id.login_forget_password: {
			Intent intent = new Intent(UserLoginActivity.this, UserForgetActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		}
		case R.id.text_option: { // 右上角
			Intent intent = new Intent(UserLoginActivity.this, UserRegisteActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 登陆功能。http://alloong.com.cn/appMember/actionLogin/
	 */
	private void doLogin(final String strUsername, final String strPassword) {
		mProgressDialog = ProgressDialog.show(this, "Please Wait...", "Please Wait...");

//		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//		final double mLatitude = mPreferences.getFloat(YXConstants.PREFS_LATITUDE, 0);
//		final double mLontitude = mPreferences.getFloat(YXConstants.PREFS_LONTITUDE, 0);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("UserName", strUsername);
		params.put("Password", EncryptUtil.encryptMD5(strPassword));
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put(YXConstants.REQUEST_SOFT_VERSION, String.valueOf(PackageUtil.getAppVersionCode(this)));
		params.put(YXConstants.REQUEST_MOBILE_INFO, PackageUtil.getMobileInfo(this));
		/*
		params.put(YXConstants.REQUEST_TOKEN_SECRET, EncryptUtil.encryptBASE64(EncryptUtil.encryptMD5(strPassword)));
		params.put(YXConstants.REQUEST_TOKEN_KEY, EncryptUtil.encryptBASE64(strUsername));
		*/
		Logger.d("UserName=" + strUsername);
		Logger.d("Password=" + EncryptUtil.encryptMD5(strPassword));
		Logger.d(YXConstants.REQUEST_IMSI + "=" + PackageUtil.getDeviceImsi(this));
		Logger.d(YXConstants.REQUEST_EQUIPMENT_TYPE + "=" + YXConstants.EQUIPMENT_TYPE_ANDROID);
		Logger.d(YXConstants.REQUEST_SOFT_VERSION + "=" + String.valueOf(PackageUtil.getAppVersionCode(this)));
		Logger.d(YXConstants.REQUEST_MOBILE_INFO + "=" + PackageUtil.getMobileInfo(this));
		
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_LOGIN, params);
		launchRequest(MyRequestFactory.execute(operation, this));
	}

	/**
	 * 登录成功之后，跳转到个人中心页面
	 */
	private void startIndividualCenterActivity(PersonDetailInfo persionDetail) {
		Intent intent = new Intent(UserLoginActivity.this, IndividualCenterActivity.class);
		intent.putExtra(IndividualCenterActivity.EXTRA_PERSON_INFO, persionDetail);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	/**
	 * 保存用户名密码功能
	 */
	private void saveLoginInfo(String strUsername, String strPassword) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), EncryptUtil.encryptBASE64(strUsername));
		editor.putString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), EncryptUtil.encryptBASE64(EncryptUtil.encryptMD5(strPassword)));
		editor.commit();
	}

	/**
	 * 校验登陆信息
	 * 
	 * @param strUsername
	 * @param strPassword
	 * @return
	 */
	private boolean checkLogin(String strUsername, String strPassword) {
		if (TextUtils.isEmpty(strUsername)) {
			showToast(R.string.user_name_empty);
			return false;
		}
		if (TextUtils.isEmpty(strPassword)) {
			showToast(R.string.user_password_empty);
			return false;
		}
		// /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/
		if (!CommonUtils.isEmail(strUsername)) {
			showToast(R.string.user_email_error);
			return false;
		}
		if (!NetworkUtil.isNetworkAvailable(this)) {
			showToast(R.string.network_unavailable);
			return false;
		}
		return true;
	}

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		Logger.e("UserLogin----response=" + response);
		if(TextUtils.isEmpty(response)) {
			Log.e(TAG, TAG + " **** login successed but response msg is null !");
			return ;
		}
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_LOGIN:
			loginSuccessed(response);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_LOGIN:
			loginFailed(errMsg);
			break;
		default:
			break;
		}
	}

	private void loginSuccessed(String response) {
		ReturnJsonModel json = JsonParseUtil.parse(response);
		if (null != json) {
			if (json.getIsSuccess() && !TextUtils.isEmpty(json.getReturnValue())) {
				Logger.i("UserLogin----json.getReturnValue()=" + json.getReturnValue());
				PersonDetailInfo loginResult = JsonParseUtil.getLoginResult(json.getReturnValue());
				Logger.i("UserLogin----loginResult=" + loginResult);
				// 保存用户信息
				if (loginResult != null) {
					PreferencesUtility.saveUserInfo(UserLoginActivity.this, loginResult);
					String strUsername = mEtUserName.getText().toString().trim();
					String strPassword = mEtPassword.getText().toString().trim();
					saveLoginInfo(strUsername, strPassword);
					startIndividualCenterActivity(loginResult);
				} else {
					showToast(R.string.login_failed);
				}
			} else {
				showToast(json.getDescription());
			}
		}
		dismisDialog();
	}

	private void loginFailed(String errMsg) {
		dismisDialog();
	}

	private void dismisDialog() {
		if (null != mProgressDialog && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

}
