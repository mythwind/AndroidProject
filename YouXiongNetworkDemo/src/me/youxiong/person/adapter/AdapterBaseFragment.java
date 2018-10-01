package me.youxiong.person.adapter;

import java.util.List;

import me.youxiong.person.ui.fragment.base.BaseFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

/**
 * 带 viewpager fragment 的适配器
 * @author mythwind
 *
 */
public class AdapterBaseFragment extends FragmentStatePagerAdapter {

	private final String[] mTitleList;
	private List<BaseFragment> mFragmentLists;
	private JazzyViewPager mJazzyViewPager;

	public AdapterBaseFragment(FragmentManager fm, List<BaseFragment> fragments, JazzyViewPager pager, String[] titleList) {
		super(fm);
		mFragmentLists = fragments;
		mJazzyViewPager = pager;
		mTitleList = titleList;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentLists.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTitleList[position % mTitleList.length];
	}

	@Override
	public int getCount() {
		return mTitleList.length;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		Object obj = super.instantiateItem(container, position);
		mJazzyViewPager.setObjectForPosition(obj, position);
		return obj;
	}
}
