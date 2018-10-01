package me.youxiong.person.ui;

import java.util.ArrayList;
import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterBaseFragment;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.fragment.TransmitFromOthersFragment;
import me.youxiong.person.ui.fragment.TransmitToMeFragment;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.view.PagerSlidingTabStrip;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;

public class CardTransmitActivity extends BaseFragmentActivity {

	private JazzyViewPager mViewPager;
	private PagerSlidingTabStrip mPagerStrip;
	private AdapterBaseFragment mAdapter;
	private List<BaseFragment> mFragments = new ArrayList<BaseFragment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mFragments.add(new TransmitToMeFragment());
		mFragments.add(new TransmitFromOthersFragment());
		
		appendMainBody(R.layout.base_jazzviewpager_layout);
		
	}
	@Override
	protected void initViewComponent() {
		mPagerStrip = ((PagerSlidingTabStrip) findViewById(R.id.tabs));
		mViewPager = (JazzyViewPager) ((ViewPager) findViewById(R.id.jazzy_pager));
	}
	
	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		
		String[] titleList = getResources().getStringArray(R.array.tab_transmit_info);
		
		mAdapter = new AdapterBaseFragment(getSupportFragmentManager(), 
				mFragments, mViewPager, titleList);
		
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setPageMargin(0);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setFadeEnabled(true);
		
		mPagerStrip.setShouldExpand(true);
		mPagerStrip.setViewPager(mViewPager);
	}

	@Override
	protected void registerListener() {
		
	}
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

}
