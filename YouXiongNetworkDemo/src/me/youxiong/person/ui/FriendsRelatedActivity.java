package me.youxiong.person.ui;

import me.youxiong.person.R;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.fragment.base.FragmentFactory;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.view.PagerSlidingTabStrip;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class FriendsRelatedActivity extends BaseFragmentActivity {
	private MyPagerAdapter adapter;
	private ViewPager pager;
	private PagerSlidingTabStrip tabs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.base_jazzviewpager_layout);
		
	}
	@Override
	protected void initViewComponent() {
		tabs = ((PagerSlidingTabStrip)findViewById(R.id.tabs));
	    pager = ((ViewPager)findViewById(R.id.jazzy_pager));
	    pager.setOffscreenPageLimit(2);
	    
	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		
		
		adapter = new MyPagerAdapter(getSupportFragmentManager());
	    pager.setAdapter(adapter);
	    
	    tabs.setShouldExpand(true);
	    
	    tabs.setViewPager(pager);
	}

	@Override
	protected void registerListener() {
		
	}

	
	public class MyPagerAdapter extends FragmentPagerAdapter {
		private final String[] TITLES = FriendsRelatedActivity.this.getResources().getStringArray(R.array.tab_friends_info);

		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}
		@Override
		public Fragment getItem(int paramInt) {
			FragmentFactory.FragmentKey localFragmentKey;
			switch (paramInt) {
			default:
				localFragmentKey = FragmentFactory.FragmentKey.FRIENDS_LIST;
				break;
			case 1:
				localFragmentKey = FragmentFactory.FragmentKey.FRIENDS_VISITOR;
				break;
			case 2:
				localFragmentKey = FragmentFactory.FragmentKey.FRIENDS_REQUESTS;
				break;
			case 3:
				localFragmentKey = FragmentFactory.FragmentKey.FRIENDS_CARE;
				break;
			case 4:
				localFragmentKey = FragmentFactory.FragmentKey.FRIENDS_BLACK;
				break;
			}
			Logger.e("paramInt=" + paramInt + ",,  localFragmentKey=" + localFragmentKey);
			return FragmentFactory.newInstance(localFragmentKey, null);
		}
		@Override
		public CharSequence getPageTitle(int paramInt) {
			return TITLES[paramInt];
		}
	}
	

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
