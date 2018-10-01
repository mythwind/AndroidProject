package me.youxiong.person.ui;

import me.youxiong.person.R;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.fragment.base.FragmentFactory;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.view.PagerSlidingTabStrip;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class CardEditActivity extends BaseFragmentActivity {
	private MyPagerAdapter mAdapter;
	private ViewPager mViewPager;
	private PagerSlidingTabStrip mPagerStrip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.base_jazzviewpager_layout);
		
	}
	
	@Override
	protected void initViewComponent() {
		mPagerStrip = ((PagerSlidingTabStrip) findViewById(R.id.tabs));
		mViewPager = ((ViewPager) findViewById(R.id.jazzy_pager));
	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		
		// String[] titleList = getResources().getStringArray(R.array.tab_product_manager);
		
		mAdapter = new MyPagerAdapter(getSupportFragmentManager());
		Bundle bundle = new Bundle();
		bundle.putSerializable("info", (PersonDetailInfo) getIntent().getSerializableExtra("info"));
		bundle.putBoolean("islogin", getIntent().getBooleanExtra("islogin", false));
		mAdapter.getItem(0).setArguments(bundle);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setAdapter(mAdapter);

		mPagerStrip.setShouldExpand(true);

		mPagerStrip.setViewPager(mViewPager);
	}

	@Override
	protected void registerListener() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	public class MyPagerAdapter extends FragmentPagerAdapter {
		private final String[] TITLES = CardEditActivity.this.getResources().getStringArray(R.array.tab_card_setting_info);

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return this.TITLES.length;
		}

		@Override
		public Fragment getItem(int paramInt) {
			FragmentFactory.FragmentKey localFragmentKey;
			switch (paramInt) {
			default:
				localFragmentKey = FragmentFactory.FragmentKey.CARD_EDIT;
				break;
			case 1:
				localFragmentKey = FragmentFactory.FragmentKey.CARD_SETTING;
				break;
			}
			Logger.e("paramInt=" + paramInt + ",,  localFragmentKey=" + localFragmentKey);
			return FragmentFactory.newInstance(localFragmentKey, null);
		}

		@Override
		public CharSequence getPageTitle(int paramInt) {
			return this.TITLES[paramInt];
		}
	}

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		
	}

}
