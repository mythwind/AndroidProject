package me.youxiong.person.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import me.youxiong.person.R;
import me.youxiong.person.dialog.EditTextViewDialog;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.BitmapUtils;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.UploadFileTools;
import me.youxiong.person.utils.Utility;
import me.youxiong.person.utils.UploadFileTools.UploadFileListener;
import me.youxiong.person.utils.UploadPhotoUtils;
import me.youxiong.person.utils.UploadPhotoUtils.IUploadPhotoLinstener;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingUploadIDCardActivity extends BaseFragmentActivity implements OnClickListener {
	private static final String TAG = SettingUploadIDCardActivity.class.getSimpleName();
	private static final int REQUEST_CODE_ALBUM = 1;
	
	private static final int MSG_UPLOAD_FILE_START = 0x2010;
	// private static final int MSG_UPLOAD_FILE_UPDATE = 0x2011;
	private static final int MSG_UPLOAD_FILE_FINISH = 0x2012;
	private static final int MSG_UPLOAD_FILE_SUCCESS = 0x2013;
	private static final int MSG_UPLOAD_FILE_ERROR = 0x2014;
	
	private EditTextViewDialog mEditTextViewDialog;
	
	private ImageView mIvUploadIDCard;
	private TextView mTvIdcardName;
	private TextView mTvIdcardNumber;
	private TextView mTvCommit;
	
	private String mPhotoPath;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_UPLOAD_FILE_SUCCESS:
				String photoPath = (String) msg.obj;
				if(!TextUtils.isEmpty(photoPath)) {
					Bitmap bitmap = BitmapUtils.getBitmapByPath(SettingUploadIDCardActivity.this, photoPath);
					// 调用上传图片的接口，上传图片。。。。
					mIvUploadIDCard.setImageBitmap(bitmap);
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
		
		appendMainBody(R.layout.activity_setting_upload_idcard);
		
	}
	
	@Override
	protected void initViewComponent() {
		mIvUploadIDCard = (ImageView) findViewById(R.id.iv_upload_idcard);
		mTvIdcardName = (TextView) findViewById(R.id.tv_idcard_name);
		mTvIdcardNumber = (TextView) findViewById(R.id.tv_idcard_number);
		mTvCommit = (TextView) findViewById(R.id.commit);
		
	}
	@Override
	protected void bindData() {
		setTileText(R.string.setting_level_3_title);
		
		mTvCommit.setEnabled(false);
	}

	@Override
	protected void registerListener() {
		findViewById(R.id.view_upload_idcard).setOnClickListener(this);
		findViewById(R.id.view_idcard_name).setOnClickListener(this);
		findViewById(R.id.view_idcard_number).setOnClickListener(this);
		mTvCommit.setOnClickListener(this);
	}

	private HashMap<String, String> getUploadRequestParams() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingUploadIDCardActivity.this);
		// 重新获取数据
		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(SettingUploadIDCardActivity.this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("PapersNo", mTvIdcardNumber.getText().toString());
		params.put("PapersName", getString(R.string.idcard));
		
		return params;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CODE_ALBUM && resultCode == RESULT_OK) {
			if(data == null) {
				showToast("选择图片失败，请重新选择图片!");
				mTvCommit.setEnabled(false);
				return ;
			}
            Uri uri = data.getData();  
            ContentResolver cr = this.getContentResolver();  
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));  
                /* 将Bitmap设定到ImageView */  
                mIvUploadIDCard.setImageBitmap(bitmap);  
            } catch (FileNotFoundException e) {  
                Log.e("Exception", e.getMessage(),e);  
            }
            // 这里开始的第二部分，获取图片的路径： 
            String[] proj = {MediaStore.Images.Media.DATA}; 
            Cursor cursor = managedQuery(uri, proj, null, null, null); 
            //按我个人理解 这个是获得用户选择的图片的索引值 
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
            cursor.moveToFirst(); 
            //最后根据索引值获取图片路径 
            mPhotoPath = cursor.getString(column_index);
            mTvCommit.setEnabled(true);
            mHandler.sendMessage(mHandler.obtainMessage(MSG_UPLOAD_FILE_SUCCESS, mPhotoPath));
            cursor.close();
		}
		Logger.d("***********onActivityForResult");
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_upload_idcard:
			choosePhoto();
			break;
		case R.id.view_idcard_name:
			mEditTextViewDialog = new EditTextViewDialog(this, getString(R.string.idcard_name), mTvIdcardName);
			mEditTextViewDialog.show();
			break;
		case R.id.view_idcard_number:
			mEditTextViewDialog = new EditTextViewDialog(this, getString(R.string.idcard_number), mTvIdcardNumber);
			mEditTextViewDialog.setInputType(EditorInfo.TYPE_CLASS_PHONE);
			mEditTextViewDialog.show();
			break;
		case R.id.commit:
			if(checkValidate()) {
				launchRequestUpIDCard();
			}
			break;
		default:
			break;
		}
	}
	private void choosePhoto() {
		if (!Utility.isExternalStorageMounted()) {
			Toast.makeText(this, "no sdcard", Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		try {
			startActivityForResult(intent, REQUEST_CODE_ALBUM);
		} catch (Exception e) {
		}
	}
	
	private boolean checkValidate() {
		if(TextUtils.isEmpty(mTvIdcardName.getText().toString())) {
			showToast("同户名称不能为空！");
			return false;
		}
		if(TextUtils.isEmpty(mTvIdcardNumber.getText().toString())) {
			showToast("身份证号码不能为空！");
			return false;
		}
		return true;
	}
	private void launchRequestUpIDCard() {
		final UploadFileTools uploadTools = new UploadFileTools(SettingUploadIDCardActivity.this, getUploadRequestParams());
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
				if(json.getIsSuccess()) {
					// 拿到当前的图片显示上传成功的图片
					mHandler.sendMessage(mHandler.obtainMessage(MSG_UPLOAD_FILE_SUCCESS, mPhotoPath));
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
					uploadTools.uploadFile(MyRequestFactory.USER_UPLOAD_PHOTO, "upImage", mPhotoPath);
					mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_FINISH);
				} catch (IOException e) {
					mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_FINISH);
					e.printStackTrace();
				}
			}
		}.start();
	}
	
}
