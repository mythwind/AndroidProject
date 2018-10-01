package me.youxiong.person.ui;

import me.youxiong.person.R;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.fragment.base.FragmentEnum;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 设置界面
 */
public class SettingActivity extends BaseFragmentActivity implements OnClickListener {

	private TextView mUpgradeVersion;
	private TextView mUserLevel;
	private TextView mFriendCategory;
	// private TextView mUpdateSafecode;
	private TextView mUpdatePassword;
	private TextView mUpdateEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_setting);
	}
	
	@Override
	protected void initViewComponent() {
		mUpgradeVersion = (TextView) findViewById(R.id.setting_tv_upgrade_version);
		mUserLevel = (TextView) findViewById(R.id.setting_tv_user_level);
		mFriendCategory = (TextView) findViewById(R.id.setting_tv_friend_category);
		
		// mUpdateSafecode = (TextView) findViewById(R.id.setting_tv_update_safecode);
		mUpdatePassword = (TextView) findViewById(R.id.setting_tv_update_password);
		mUpdateEmail = (TextView) findViewById(R.id.setting_tv_update_email);
		
	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		
	}

	@Override
	protected void registerListener() {
		mUpgradeVersion.setOnClickListener(this);
		mUserLevel.setOnClickListener(this);
		mFriendCategory.setOnClickListener(this);
		
		// mUpdateSafecode.setOnClickListener(this);
		mUpdatePassword.setOnClickListener(this);
		mUpdateEmail.setOnClickListener(this);
		
		findViewById(R.id.logout).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_tv_upgrade_version:
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.setUpdateAutoPopup(false);
	        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
	            @Override
	            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
	                switch (updateStatus) {
	                case UpdateStatus.Yes: // has update
	                    UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
	                    break;
	                case UpdateStatus.No: // has no update
	                	showToast(R.string.update_latest_version);
	                    break;
	                case UpdateStatus.NoneWifi: // none wifi
	                	showToast(R.string.update_wifi_only);
	                    break;
	                case UpdateStatus.Timeout: // time out
	                	showToast(R.string.network_timeout);
	                    break;
	                }
	            }
	        });
	        UmengUpdateAgent.update(this);
			break;
		case R.id.setting_tv_user_level:
			startFragmentDetailActivity(FragmentEnum.USER_LEVEL.getValue(), mUserLevel.getText().toString());
			break;
		case R.id.setting_tv_friend_category:
			startFragmentDetailActivity(FragmentEnum.FRIEND_CATEGORY.getValue(), mFriendCategory.getText().toString());
			break;
//		case R.id.setting_tv_update_safecode:
//			startFragmentDetailActivity(FragmentEnum.UPDATE_SAFECODE.getValue(), mUpdateSafecode.getText().toString());
//			break;
		case R.id.setting_tv_update_password:
			startFragmentDetailActivity(FragmentEnum.UPDATE_PASSWORD.getValue(), mUpdatePassword.getText().toString());
			break;
		case R.id.setting_tv_update_email:
			startFragmentDetailActivity(FragmentEnum.UPDATE_EMAIL.getValue(), mUpdateEmail.getText().toString());
			break;
		case R.id.logout:
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
	 		SharedPreferences.Editor editor = preferences.edit();
	 		// 退出登录是否会记住用户名 ？
	 		editor.remove(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME));
	 		editor.remove(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD));
	 		editor.commit();
	 		// 回到主界面
	 		startMainActivity();
			break;
		default:
			break;
		}
	}
	private void startFragmentDetailActivity(int fragmentKey, String title) {
		Intent intent = new Intent(this, SettingDetailActivity.class);
		intent.putExtra(SettingDetailActivity.EXTRA_FRAGMENT_PAGE, fragmentKey);
		intent.putExtra(SettingDetailActivity.EXTRA_FRAGMENT_TITLE, title);
		startActivity(intent);
	}
	private void startMainActivity() {
		Intent intent = new Intent(SettingActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // 设置不要刷新将要跳到的界面  
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 它可以关掉所要到的界面中间的activity
		startActivity(intent);
		exitActivity();
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
