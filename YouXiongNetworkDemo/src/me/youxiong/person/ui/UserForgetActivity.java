package me.youxiong.person.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.youxiong.person.R;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.config.YXConstants;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class UserForgetActivity extends BaseFragmentActivity implements OnClickListener {

	private EditText mEmailEditText;
	private TextView mResetPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_user_forget);
		
	}
	
	@Override
	protected void initViewComponent() {
		mEmailEditText = (EditText) findViewById(R.id.reset_registe_email);
		mResetPassword = (TextView) findViewById(R.id.reset_password);
		
	}

	@Override
	protected void bindData() {
		setTileText(getString(R.string.reset_password));
		
	}

	@Override
	protected void registerListener() {
		mResetPassword.setOnClickListener(this);
	}

	private boolean checkEmail() {
		String strEmail = mEmailEditText.getText().toString();
		if(TextUtils.isEmpty(strEmail)) {
			showToast(R.string.user_email_empty);
			return false;
		}
		if(!CommonUtils.isEmail(strEmail)) {
			showToast(R.string.user_email_error);
			return false;
		}
		return true;
	}
	private Map<String, String> getRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put(YXConstants.REQUEST_MOBILE_INFO, "123456");
		/*
		params.put(YXConstants.REQUEST_TOKEN_KEY, EncryptUtil.encryptBASE64(strUserEmail));
		params.put(YXConstants.REQUEST_TOKEN_SECRET, EncryptUtil.encryptBASE64(EncryptUtil.encryptMD5(strPassword)));
		*/
		return params;
	}
	/*
	private void doRequestForget() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_FORGET_PASSWORD, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	*/
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reset_password:
			// 发送重置密码的请求。。。。
			Random random = new Random(System.currentTimeMillis());
			random.nextInt(2);
			
			if(checkEmail()) {
				// 找回密码成功或者失败，都回到主界面 mainactivity
				MyRequestOperation operation = new MyRequestOperation(
						MyRequestFactory.REQUEST_TYPE_FORGET_PASSWORD, getRequestParams());
				launchRequest(MyRequestFactory.execute(operation, this));
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_FORGET_PASSWORD:
			
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_FORGET_PASSWORD:
			
			break;
		default:
			break;
		}
	}

}
