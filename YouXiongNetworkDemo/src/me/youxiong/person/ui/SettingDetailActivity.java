package me.youxiong.person.ui;

import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.fragment.SettingFriendCategoryFragment;
import me.youxiong.person.ui.fragment.SettingSafeCodeFragment;
import me.youxiong.person.ui.fragment.SettingUpdateEmailFragment;
import me.youxiong.person.ui.fragment.SettingUpdatePwdFragment;
import me.youxiong.person.ui.fragment.SettingUserLevelFragment;
import me.youxiong.person.ui.fragment.base.FragmentEnum;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SettingDetailActivity extends BaseFragmentActivity {
	public static final String EXTRA_FRAGMENT_PAGE = "FragmentPage";
	public static final String EXTRA_FRAGMENT_TITLE = "FragmentTitle";
	private Fragment mFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		int value = getIntent().getIntExtra(EXTRA_FRAGMENT_PAGE, FragmentEnum.ERROR_VALUE.getValue());
		
		// 添加 fragment，处理业务逻辑
		mFragment = getDisplayFragment(value);
		if(null != mFragment) {
			appendMainBody(mFragment);
		} else {
			exitActivity();
		}
	}
	
	private Fragment getDisplayFragment(int value) {
		Fragment fragment = null;
		if(value == FragmentEnum.USER_LEVEL.getValue()) {
			fragment = new SettingUserLevelFragment();
		}
		
		else if(value == FragmentEnum.FRIEND_CATEGORY.getValue()) {
			fragment = SettingFriendCategoryFragment.getInstannce();
		}
		else if(value == FragmentEnum.UPDATE_SAFECODE.getValue()) {
			fragment = SettingSafeCodeFragment.getInstannce();
		}
		else if(value == FragmentEnum.UPDATE_PASSWORD.getValue()) {
			fragment = SettingUpdatePwdFragment.getInstannce();
		}
		else if(value == FragmentEnum.UPDATE_EMAIL.getValue()) {
			fragment = SettingUpdateEmailFragment.getInstannce();
		}
		else if (value == FragmentEnum.ERROR_VALUE.getValue()) {
			throw new IllegalArgumentException("call fragment failed, params error");
		}
		
		return fragment;
	}

	@Override
	protected void initViewComponent() {
	}
	@Override
	protected void bindData() {
		String title = getIntent().getStringExtra(EXTRA_FRAGMENT_TITLE);
		setTileText(title);
	}

	@Override
	protected void registerListener() {
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	};
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
	
}
