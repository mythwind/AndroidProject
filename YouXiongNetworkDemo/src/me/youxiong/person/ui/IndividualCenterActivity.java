package me.youxiong.person.ui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterIndividual;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.BitmapUtils;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.LoadImageUtils;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import me.youxiong.person.utils.UploadFileTools;
import me.youxiong.person.utils.UploadFileTools.UploadFileListener;
import me.youxiong.person.utils.UploadPhotoUtils;
import me.youxiong.person.utils.UploadPhotoUtils.IUploadPhotoLinstener;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import me.youxiong.person.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 登陆之后的个人中心
 * @author mythwind
 *
 */
public class IndividualCenterActivity extends BaseFragmentActivity implements OnClickListener, OnItemClickListener {
	private static final String TAG = IndividualCenterActivity.class.getSimpleName();
	
	public static final String EXTRA_PERSON_INFO = "PersionDetail";
	public static final String EXTRA_TITLE = "title";
	
	private static final int REQUEST_CODE_CARD_EDIT = 0x0030;
	
	private static final int MSG_UPLOAD_FILE_START = 0x2010;
	// private static final int MSG_UPLOAD_FILE_UPDATE = 0x2011;
	private static final int MSG_UPLOAD_FILE_FINISH = 0x2012;
	private static final int MSG_UPLOAD_FILE_SUCCESS = 0x2013;
	private static final int MSG_UPLOAD_FILE_ERROR = 0x2014;
	
	private final Class<?>[] mTurnClz = {
			FaceValueActivity.class, 
			IntegralActivity.class, 
			CardEditActivity.class, 
			ProductManagerActivity.class, 
			/*
			FriendsRecommendActivity.class, 
			FriendsRelatedActivity.class, 
			CardTransmitActivity.class, 
			*/
			ShareActivity.class,
			SettingActivity.class, 
			HelpActivity.class
	};
	
	private PersonDetailInfo mPersonDetail;
	private UploadPhotoUtils mUploadPhoto;

//	private ListView mIndividualListView;
	private GridView mIndividualGridView;
	private CircleImageView mIndividualPic;
	private TextView mIndividualName;
	private TextView mIndividualSign;
	private TextView mIndividualLevel;
	
	private String mProductIconUrlSuffix;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
//			case MSG_UPLOAD_FILE_START:
//				mpProgressDialog.show();
//				break;
			case MSG_UPLOAD_FILE_SUCCESS:
				// mpProgressDialog.dismiss();
				String photoPath = (String) msg.obj;
				if(!TextUtils.isEmpty(photoPath)) {
					Bitmap bitmap = BitmapUtils.getBitmapByPath(IndividualCenterActivity.this, photoPath);
					// 调用上传图片的接口，上传图片。。。。
					mIndividualPic.setImageBitmap(bitmap);
					showToast("上传图片成功！");
				}
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		appendMainBody(R.layout.activity_individual_center);
	}

	@Override
	protected void initViewComponent() {
		mIndividualGridView = (GridView) findViewById(R.id.individual_message_list);
		mIndividualPic = (CircleImageView) findViewById(R.id.individual_pic);
		mIndividualName = (TextView) findViewById(R.id.individual_name);
		mIndividualSign = (TextView) findViewById(R.id.individual_sign);
		mIndividualLevel = (TextView) findViewById(R.id.individual_level);

	}

	@Override
	protected void bindData() {
		setTileText(R.string.individual_title);
		mIconOption.setVisibility(View.VISIBLE);
		mIconOption.setImageResource(R.drawable.head_search);
		
		mUploadPhoto = new UploadPhotoUtils(this);
		
		AdapterIndividual adapter = new AdapterIndividual(this);
		mIndividualGridView.setAdapter(adapter);
		mIndividualGridView.requestFocusFromTouch();
		
		mPersonDetail = (PersonDetailInfo) getIntent().getSerializableExtra(EXTRA_PERSON_INFO);
		if(null == mPersonDetail) {
			mPersonDetail = PreferencesUtility.getUserInfo(IndividualCenterActivity.this);
		}
		if(null == mPersonDetail) {
			startActivity(new Intent(this, UserLoginActivity.class));
			finish();
			return ;
		}
		fillIndividualInfo();
	}

	@Override
	protected void registerListener() {
		mIconOption.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IndividualCenterActivity.this, SearchableActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		mIndividualGridView.setOnItemClickListener(this);
		mIndividualSign.setOnClickListener(this);
		mIndividualPic.setOnClickListener(this);
		
	}
	
	private void fillIndividualInfo() {
		if(null == mPersonDetail) {
			Log.e(TAG, " ******** person info is null, some error!");
			return ;
		}
		if (!TextUtils.isEmpty(mPersonDetail.getReal_name())) {
			// setTileText(getString(R.string.individual_title, mPersionDetail.getReal_name()));
			mIndividualName.setText(mPersonDetail.getReal_name());
		}
		mIndividualLevel.setText(getString(R.string.individual_level, mPersonDetail.getUser_level()));
		
		String picUrl = mPersonDetail.getPhoto();
		Logger.i("---picUrl=" +  picUrl);
		if (!TextUtils.isEmpty(picUrl)) {
			LoadImageUtils.displayImage(picUrl, mIndividualPic, R.drawable.head_bg_selector);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.individual_sign:
			// 签到的接口
			launchRequestSign();
			break;
		case R.id.individual_pic:
			// choosePicture();
			mUploadPhoto.choosePhoto(this);
			break;
		default:
			break;
		}
	}
	private void launchRequestSign() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_SIGN, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	private Map<String, String> getRequestParams() {
		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String username = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		return params;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AdapterIndividual adapter = (AdapterIndividual) parent.getAdapter();
		Intent intent = null;
		if(0 == position) {
			String picUrl = mPersonDetail.getPhoto();
			if (TextUtils.isEmpty(picUrl)) {
				showToast("上传图像之后才能查看脸值！");
				mUploadPhoto.choosePhoto(this);
			} else {
				intent = new Intent(IndividualCenterActivity.this, mTurnClz[position]);
				intent.putExtra(EXTRA_TITLE, adapter.getTitleByPosition(position));
				startActivity(intent);
			}
		} else if (2 == position) {
			intent = new Intent(IndividualCenterActivity.this, mTurnClz[position]);
			intent.putExtra(EXTRA_TITLE, adapter.getTitleByPosition(position));
			startActivityForResult(intent, REQUEST_CODE_CARD_EDIT);
		} else {
			intent = new Intent(IndividualCenterActivity.this, mTurnClz[position]);
			intent.putExtra(EXTRA_TITLE, adapter.getTitleByPosition(position));
			startActivity(intent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CODE_CARD_EDIT) {
			PersonDetailInfo detailInfo = PreferencesUtility.getUserInfo(IndividualCenterActivity.this);
			if (mPersonDetail != detailInfo) {
				mPersonDetail = detailInfo;
				fillIndividualInfo();
			}
		} else {
			mUploadPhoto.onActivityResult(requestCode, resultCode, data, this);
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
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(IndividualCenterActivity.this);
					// 重新获取数据
					String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
					String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
					
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
					params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
					params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(IndividualCenterActivity.this));
					params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
					
					final UploadFileTools uploadTools = new UploadFileTools(IndividualCenterActivity.this, params);
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
									mProductIconUrlSuffix = jsonObject.getString("product_icon");
									// 修改用户信息的 url 地址
									PersonDetailInfo info = PreferencesUtility.getUserInfo(IndividualCenterActivity.this);
									info.setPhoto(mProductIconUrlSuffix);
									PreferencesUtility.saveUserInfo(IndividualCenterActivity.this, info);
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
		}
		Logger.d("***********onActivityForResult");
	}

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		if(TextUtils.isEmpty(response)) {
			return ;
		}
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_SIGN:
			handleSignResponse(response);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

	private void handleSignResponse(String response) {
		ReturnJsonModel json = JsonParseUtil.parse(response);
		if(null == json) {
			return ;
		}
		if(json.getIsSuccess()) {
			JSONObject jsonObject;
			int totalIntegral = 0;
			try {
				jsonObject = new JSONObject(json.getReturnValue());
				totalIntegral = jsonObject.getInt("integral");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			PreferencesUtility.setTotalIntegral(this, totalIntegral);
			showToast(getString(R.string.sign_successed, totalIntegral));
		} else {
			showToast(json.getDescription());
		}
	}
	
}
