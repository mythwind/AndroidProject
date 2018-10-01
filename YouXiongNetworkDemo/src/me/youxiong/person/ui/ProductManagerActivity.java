package me.youxiong.person.ui;

import java.util.ArrayList;
import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterBaseFragment;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.fragment.ProductDeductionFragment;
import me.youxiong.person.ui.fragment.ProductListFragment;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.view.PagerSlidingTabStrip;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.jfeinstein.jazzyviewpager.JazzyViewPager.TransitionEffect;

/**
 * 
 * 产品管理
 * @author mythwind
 *
 */
public class ProductManagerActivity extends BaseFragmentActivity implements OnClickListener {
	private JazzyViewPager mViewPager;
	private PagerSlidingTabStrip mPagerStrip;
	private AdapterBaseFragment mAdapter;
	private List<BaseFragment> mFragments = new ArrayList<BaseFragment>();
	private ProductListFragment mProductListFragment;
	
	//PreferencesUtility.saveUserInfo(UserLoginActivity.this, loginResult);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		mTextOption.setText(R.string.product_add_title);
		mTextOption.setVisibility(View.VISIBLE);
		
		mProductListFragment = new ProductListFragment();
		mFragments.add(mProductListFragment);
//		mFragments.add(new ProductSettingFragment());
		mFragments.add(new ProductDeductionFragment());
		
		String[] titleList = getResources().getStringArray(R.array.tab_product_manager);
		
		mAdapter = new AdapterBaseFragment(getSupportFragmentManager(), 
				mFragments, mViewPager, titleList);
		
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setPageMargin(0);
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setFadeEnabled(true);
		
		mPagerStrip.setShouldExpand(true);
		mPagerStrip.setViewPager(mViewPager);
	}

	@Override
	protected void registerListener() {
		mTextOption.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if(v == mTextOption) {
			Intent intent = new Intent();
			intent.setClass(this, ProductAddActivity.class);
			startActivityForResult(intent, ProductListFragment.REQUEST_CODE_ADD_PRODUCT);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Logger.e("ProductMAnager---onActivityResult  ");
		if(requestCode == ProductListFragment.REQUEST_CODE_ADD_PRODUCT) {
			mProductListFragment.onActivityResult(requestCode, resultCode, data);
		}
		
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
