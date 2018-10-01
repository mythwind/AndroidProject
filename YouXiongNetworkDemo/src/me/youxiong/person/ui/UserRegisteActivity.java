package me.youxiong.person.ui;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.base.WebviewActivity;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.config.YXConstants;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 用户注册的 activity
 * @author mythwind
 */
public class UserRegisteActivity extends BaseFragmentActivity implements OnClickListener {

	private EditText mRealName;
	private EditText mUserEmail;
	private EditText mPassword;
	private EditText mConfirmPassword;
	private RadioGroup mUserGender;
	
	private CheckBox mCheckBox;
	private TextView mRegisteAgreement;
	private TextView mRegiste;
	
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_user_registe);
	}
	
	@Override
	protected void initViewComponent() {
		mRealName = (EditText) findViewById(R.id.registe_real_name);
		mUserEmail = (EditText) findViewById(R.id.registe_name);
		mPassword = (EditText) findViewById(R.id.registe_password);
		mConfirmPassword = (EditText) findViewById(R.id.registe_confirm_password);
		mUserGender = (RadioGroup) findViewById(R.id.registe_gender);
		
		mRegisteAgreement = (TextView) findViewById(R.id.registe_tv_agreement);
		mCheckBox = (CheckBox) findViewById(R.id.registe_checkbox);
		mRegiste = (TextView) findViewById(R.id.registe);
	}

	@Override
	protected void bindData() {
		setTileText(getString(R.string.registe_title));
		/*
		mRegisteAgreement.setText(Html.fromHtml("&lt;&lt;<a href=\"" + YXConstants.AGREEMENT_URL + "\">" + getString(R.string.registe_agreement) + "</a>&gt;&gt;"));
		mRegisteAgreement.setMovementMethod(LinkMovementMethod.getInstance());
		*/
		
		mRegisteAgreement.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WebviewActivity.startActivity(UserRegisteActivity.this, null, "协议");
			}
		});
		
	}

	@Override
	protected void registerListener() {
		mRegiste.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registe:
			String strUsername = mRealName.getText().toString().trim();
			String strPassword = mPassword.getText().toString().trim();
			String strUserEmail = mUserEmail.getText().toString().trim();
			
			if(checkRegiste(strUsername, strPassword, strUserEmail)) {
				doRegiste(strUsername, strPassword, strUserEmail);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 验证注册信息
	 * @param strUsername
	 * @param strPassword
	 * @param strUserEmail
	 * @return
	 */
	private boolean checkRegiste(final String strUsername, final String strPassword, final String strUserEmail) {
		String strConfrmPassword = mConfirmPassword.getText().toString().trim();
		if(TextUtils.isEmpty(strUsername)) {
			showToast(R.string.user_name_empty);
			return false;
		}
		if(TextUtils.isEmpty(strUserEmail)) {
			showToast(R.string.user_email_empty);
			return false;
		}
		if(TextUtils.isEmpty(strPassword) || TextUtils.isEmpty(strConfrmPassword)) {
			showToast(R.string.user_password_empty);
			return false;
		}
		if(!strPassword.equals(strConfrmPassword)) {
			showToast(R.string.user_password_different);
			return false;
		}
		// /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/
		if(!CommonUtils.isEmail(strUserEmail)) {
			showToast(R.string.user_email_error);
			return false;
		}
		if(!mCheckBox.isChecked()) {
			showToast(R.string.registe_agreement_first);
			return false;
		}
		return true;
	}
	
	/**
	 * 请求注册，返回是否注册成功。
	 * @param strUsername
	 * @param strPassword
	 * @param strUserEmail
	 */
	private void doRegiste(final String strUsername, final String strPassword, final String strUserEmail) {
		mProgressDialog = ProgressDialog.show(this, "Please Wait...", "Please Wait...");
		
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_REGISTE, 
				getRequestParams(strUsername, strPassword, strUserEmail));
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams(final String strUsername, final String strPassword, final String strUserEmail) {
		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		final double mLatitude = mPreferences.getFloat(YXConstants.PREFS_LATITUDE, 0);
		final double mLontitude = mPreferences.getFloat(YXConstants.PREFS_LONTITUDE, 0);
		
		Map<String, String> params = new HashMap<String, String>();
		/*
		params.put(YXConstants.REQUEST_LATITUDE, String.valueOf(mLatitude));
		params.put(YXConstants.REQUEST_LONTITUDE, String.valueOf(mLontitude));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put(YXConstants.REQUEST_MOBILE_INFO, "123456");
		*/
		int selectPosition = 1;
		switch (mUserGender.getCheckedRadioButtonId()) {
		case R.id.registe_gender_male:
			selectPosition = 1;
			break;
		case R.id.registe_gender_female:
			selectPosition = 2;
			break;
		default:
			break;
		}
//		params.put("Email", EncryptUtil.encryptBASE64(strUserEmail));
//		params.put("Password", EncryptUtil.encryptBASE64(EncryptUtil.encryptMD5(strPassword)));
		params.put("Email", strUserEmail);
		params.put("Password", strPassword);
		params.put("RealName", strUsername);
		params.put("Sex", String.valueOf(selectPosition));
		
		Logger.i("-----mLatitude=" + mLatitude + ",  mLontitude=" + mLontitude);
		Logger.i("-----strUsername=" + strUsername);
		Logger.i("-----selectPosition(1 Man 2 Famail):" + selectPosition);
		return params;
	}

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		Logger.e("UserRegister----response=" + response);
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_REGISTE:
			registeSuccess(response);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_REGISTE:
			registeFaild(errMsg);
			break;
		default:
			break;
		}
	}

	private void registeSuccess(String response) {
		ReturnJsonModel json = JsonParseUtil.parse(response);
		if (null != json) {
			if (json.getIsSuccess()) {
				PersonDetailInfo loginResult = JsonParseUtil.getLoginResult(json.getReturnValue());
				Logger.i("UserRegister----loginResult=" + loginResult);
				
				showLongToast(getString(R.string.registe_success));
				exitActivity();
				
			} else {
				showToast(json.getDescription());
			}
		}
		dismisDialog();
	}
	private void registeFaild(String errMsg) {
		dismisDialog();
	}
	
	private void dismisDialog() {
		if (null != mProgressDialog && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

}
