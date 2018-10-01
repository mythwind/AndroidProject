package me.youxiong.person.ui;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.model.FaceValueInfo;
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class FaceValueActivity extends BaseFragmentActivity {

	private View mLoadingView;
	private ImageView mLoadingIcon;
	private TextView mLoadingText;
	
	private ImageView mIvUserPhoto;
	private TextView mTvUserName;
	private TextView mTvCardTitle;
	private TextView mTvFaceValuePotential;
	private TextView mTvFaceValueReal;
	private TextView mTvFaceValueAdd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_face_value);
		
	}
	
	@Override
	protected void initViewComponent() {
		mLoadingView = findViewById(R.id.hint_loading_layout);
		mLoadingIcon = (ImageView) findViewById(R.id.hint_loading_icon);
		mLoadingText = (TextView) findViewById(R.id.hint_loading_text);
		
		mIvUserPhoto = (ImageView) findViewById(R.id.user_photo);
		mTvUserName = (TextView) findViewById(R.id.user_real_name);
		mTvCardTitle = (TextView) findViewById(R.id.user_card_title);
		mTvFaceValuePotential = (TextView) findViewById(R.id.face_value_potential);
		mTvFaceValueReal = (TextView) findViewById(R.id.face_value_real);
		mTvFaceValueAdd = (TextView) findViewById(R.id.face_value_add);
		
	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		
		launchRequestFaceValue();
		
		mLoadingView.setVisibility(View.VISIBLE);
		mLoadingIcon.startAnimation(getRotateLoadingAnimation());
		mLoadingText.setText(R.string.loading_message);
		
	}

	@Override
	protected void registerListener() {
		
	}

	private void launchRequestFaceValue() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_FACEVALUE, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 重新获取数据
		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
				 
		PersonDetailInfo info = PreferencesUtility.getUserInfo(this);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		params.put("UserId", String.valueOf(info.getUser_id()));
		
		Logger.d("FaceValueActivity----DeviceImsi=" + PackageUtil.getDeviceImsi(this));
		Logger.d("FaceValueActivity----mUserName=" + username.trim());
		Logger.d("FaceValueActivity----mPassword=" + password.trim());
		return params;
	}

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_FACEVALUE:
			Logger.i("FaceValueActivity----onRequestSuccessed=" + response);
			try {
				handleFaceValue(response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_FACEVALUE:
			mLoadingView.setVisibility(View.VISIBLE);
			mLoadingIcon.clearAnimation();
			mLoadingIcon.setImageResource(R.drawable.ic_error);
			mLoadingText.setText(R.string.error_message);
			break;
		default:
			break;
		}
	}
	private void handleFaceValue(String response) throws Exception {
		if(TextUtils.isEmpty(response)) {
			mLoadingView.setVisibility(View.VISIBLE);
			mLoadingIcon.clearAnimation();
			mLoadingIcon.setImageResource(R.drawable.ic_error);
			mLoadingText.setText(R.string.error_message);
			
			Log.e("FaceValue", " ***** response is empty!");
			return;
		}
		ReturnJsonModel json = JsonParseUtil.parse(response);
		if(null != json && json.getIsSuccess()) {
			if(!TextUtils.isEmpty(json.getReturnValue())) {
				Gson gson = new Gson();
				FaceValueInfo info = gson.fromJson(json.getReturnValue(), FaceValueInfo.class);
				if(null != info) {
					mIvUserPhoto = (ImageView) findViewById(R.id.user_photo);
					mTvUserName.setText(info.getReal_name());
					mTvCardTitle.setText(info.getCard_title());
					mTvFaceValuePotential.setText(info.getFace_value_potential());
					mTvFaceValueReal.setText(info.getFace_value_real());
					mTvFaceValueAdd.setText(info.getFace_value_add());
					if(!TextUtils.isEmpty(info.getPhoto())) {
						LoadImageUtils.displayImage(info.getPhoto(), mIvUserPhoto);
					}
					mLoadingIcon.clearAnimation();
					mLoadingView.setVisibility(View.GONE);
					return;
				}
			}
		}
		mLoadingView.setVisibility(View.VISIBLE);
		mLoadingIcon.clearAnimation();
		mLoadingIcon.setImageResource(R.drawable.ic_error);
		mLoadingText.setText(R.string.error_message);
	}

}
