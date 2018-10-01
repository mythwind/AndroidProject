package me.youxiong.person.ui.fragment.base;


public abstract class LazyLoadFragment extends BaseFragment {
	private boolean mFirstVisiable = true;
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser && mFirstVisiable) {
			firstVisiable();
			mFirstVisiable = false;
		}
	}
	protected abstract void firstVisiable();
	
}
