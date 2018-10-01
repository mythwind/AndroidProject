package me.youxiong.person.ui.fragment;

import java.io.IOException;
import java.util.HashMap;

import me.youxiong.person.R;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.ui.CardEditActivity;
import me.youxiong.person.ui.SettingUploadIDCardActivity;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import me.youxiong.person.utils.UploadFileTools;
import me.youxiong.person.utils.UploadFileTools.UploadFileListener;
import me.youxiong.person.utils.UploadPhotoUtils;
import me.youxiong.person.utils.UploadPhotoUtils.IUploadPhotoLinstener;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingUserLevelFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = SettingUserLevelFragment.class.getSimpleName();
	
	private static final int MSG_UPLOAD_FILE_START = 0x2010;
	// private static final int MSG_UPLOAD_FILE_UPDATE = 0x2011;
	private static final int MSG_UPLOAD_FILE_FINISH = 0x2012;
	private static final int MSG_UPLOAD_FILE_SUCCESS = 0x2013;
	private static final int MSG_UPLOAD_FILE_ERROR = 0x2014;
	
	private TextView mViewLevel01;
	private TextView mViewLevel02;
	private TextView mViewLevel03;
	private TextView mViewLevel04;
	private TextView mViewLevel05;
	private TextView mViewLevel06;
	
	private PersonDetailInfo mPersonInfo;
	private UploadPhotoUtils mUploadPhoto;
	
	public SettingUserLevelFragment() {}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
//			case MSG_UPLOAD_FILE_START:
//				mpProgressDialog.show();
//				break;
			case MSG_UPLOAD_FILE_SUCCESS:
				showToast("上传图片成功！");
				break;
			case MSG_UPLOAD_FILE_ERROR:
				String errMsg = (String) msg.obj;
				if(!TextUtils.isEmpty(errMsg)) {
					showToast(errMsg);
				}
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindData();
		registeListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting_user_level, null);
		mViewLevel01 = (TextView) view.findViewById(R.id.setting_tv_user_level_01);
		mViewLevel02 = (TextView) view.findViewById(R.id.setting_tv_user_level_02);
		mViewLevel03 = (TextView) view.findViewById(R.id.setting_tv_user_level_03);
		mViewLevel04 = (TextView) view.findViewById(R.id.setting_tv_user_level_04);
		mViewLevel05 = (TextView) view.findViewById(R.id.setting_tv_user_level_05);
		mViewLevel06 = (TextView) view.findViewById(R.id.setting_tv_user_level_06);
		return view;
	}
	private void bindData() {
		mPersonInfo = PreferencesUtility.getUserInfo(mActivity);
		mUploadPhoto = new UploadPhotoUtils(mActivity);
		
	}
	private void registeListener() {
		mViewLevel01.setOnClickListener(this);
		mViewLevel02.setOnClickListener(this);
		mViewLevel03.setOnClickListener(this);
		mViewLevel04.setOnClickListener(this);
		mViewLevel05.setOnClickListener(this);
		mViewLevel06.setOnClickListener(this);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		mUploadPhoto.onActivityResult(requestCode, resultCode, data, SettingUserLevelFragment.this);
		mUploadPhoto.setUploadPhotoLinstener(new IUploadPhotoLinstener() {
			@Override
			public void startUpload(final String photoPath) {
				/*
				Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
				// 调用上传图片的接口，上传图片。。。。
				mIndividualPic.setImageBitmap(bitmap);
				*/
				
				if(TextUtils.isEmpty(photoPath)) {
					showToast("选择图片失败，请重新选择图片!");
					return ;
				}
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
				// 重新获取数据
				String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
				String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
				
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
				params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
				params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
				params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
				
				final UploadFileTools uploadTools = new UploadFileTools(mActivity, params);
				uploadTools.setOnUploadFileListener(new UploadFileListener() {
					@Override
					public void onUploadProcess(int size) {
					}
					@Override
					public void onUploadFinished(String msg) {
						// mpProgressDialog.dismiss();
						if(TextUtils.isEmpty(msg)) {
							Log.e(TAG, TAG + " ******** upload photo finish, but result is null!");
							return;
						}
						ReturnJsonModel json = JsonParseUtil.parse(msg);
						if(null == json) {
							Log.e(TAG, TAG + " ******** json parse error! msg=" + msg);
							return;
						}
						Logger.e("upload user photo,  msg=" + msg);
						if(json.getIsSuccess() && !TextUtils.isEmpty(json.getReturnValue())) {
							JSONObject jsonObject;
							try {
								jsonObject = new JSONObject(json.getReturnValue());
								String user_icon = jsonObject.getString("product_icon");
								// 修改用户信息的 url 地址
								PersonDetailInfo info = PreferencesUtility.getUserInfo(mActivity);
								info.setPhoto(user_icon);
								PreferencesUtility.saveUserInfo(mActivity, info);
								// 拿到当前的图片显示上传成功的图片
								mHandler.sendMessage(mHandler.obtainMessage(MSG_UPLOAD_FILE_SUCCESS, photoPath));
							} catch (JSONException e) {
								e.printStackTrace();
							}
						} else {
							mHandler.sendMessage(mHandler.obtainMessage(MSG_UPLOAD_FILE_ERROR, json.getDescription()));
						}
					}
					@Override
					public void onUploadFailed(String errMsg) {
						// mpProgressDialog.dismiss();
					}
				});
				new Thread() {
					@Override
					public void run() {
						try {
							mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_START);
							uploadTools.uploadFile(MyRequestFactory.USER_UPLOAD_PHOTO, "upImage", photoPath);
							mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_FINISH);
						} catch (IOException e) {
							mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_FINISH);
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
	};
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_tv_user_level_01:
			turnToCardEdit();
			break;
		case R.id.setting_tv_user_level_02:
			mUploadPhoto.choosePhoto(SettingUserLevelFragment.this);
			break;
		case R.id.setting_tv_user_level_03:
			turnToUploadIdcard();
			
		default:
			break;
		}
	}
	private void turnToCardEdit() {
		Intent intent = new Intent(mActivity, CardEditActivity.class);
		intent.putExtra("title", mActivity.getResources().getString(R.string.card_edit));
		startActivity(intent);
	}
	private void turnToUploadIdcard() {
		Intent intent = new Intent(mActivity, SettingUploadIDCardActivity.class);
		intent.putExtra("title", mActivity.getResources().getString(R.string.card_edit));
		startActivity(intent);
	}

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
	
}
