package me.youxiong.person.ui;

import java.util.ArrayList;

import me.youxiong.person.R;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.fragment.GuideFirstFragment;
import me.youxiong.person.ui.fragment.GuideSecondFragment;
import me.youxiong.person.view.DirectionalViewPager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;

import com.viewpagerindicator.CirclePageIndicator;

public class GuideActivity extends BaseFragmentActivity {

	private DirectionalViewPager mViewPager;
	private CirclePageIndicator mPageIndicator;
	private ArrayList<Fragment> mFragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_guide);
		hideTitleLayout(true);
	}
	
	@Override
	protected void initViewComponent() {
		mViewPager = (DirectionalViewPager) findViewById(R.id.vp_pager);
		mPageIndicator = (CirclePageIndicator) findViewById(R.id.cpi_indicator);
		
	}

	@Override
	protected void bindData() {
		mFragments = new ArrayList<Fragment>();
		GuideFirstFragment firstFragment = GuideFirstFragment.getInstannce(R.drawable.splash_01);
		GuideSecondFragment secondFragment = GuideSecondFragment.getInstannce(R.drawable.splash_02);
		mFragments.add(firstFragment);
		mFragments.add(secondFragment);
		
		MyGuideAdapter adapter = new MyGuideAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(adapter);
		final float density = getResources().getDisplayMetrics().density;
		mPageIndicator.setBackgroundColor(0x00000000);
        mPageIndicator.setRadius(5 * density);
        mPageIndicator.setPageColor(0x55FFFFFF);
        mPageIndicator.setFillColor(0xFFFFFFFF);
        mPageIndicator.setStrokeColor(0x88FFFFFF);
        mPageIndicator.setStrokeWidth(1 * density);
		mPageIndicator.setViewPager(mViewPager);
		adapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(0);
		
	}

	@Override
	protected void registerListener() {
	}

	/**监听按键*/
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
	
	private class MyGuideAdapter extends FragmentPagerAdapter{
		public MyGuideAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int index) {
			return mFragments.get(index);
		}
		@Override
		public int getCount() {
			return mFragments.size();
		}
	}

}
