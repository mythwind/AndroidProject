package me.youxiong.person.adapter.base;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import me.youxiong.person.cache.CacheItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

public class BaseListViewAdapter extends BaseAdapter {
	private List<CacheItem> mItemList = null;  // a
	  private WeakHashMap<Integer, CacheItem> mWeakHashMap = new WeakHashMap<Integer, CacheItem>(); // b
	  private int mListSize = 0;  // c
	  private AdapterView<?> mAdapterView;  // d
	  private CacheItem bufItem;
	  private CacheItem f;
	  private int mLastPosition = 0;
	  private int mCacheVisible = 2;
	  
	@SuppressWarnings("unchecked")
	public BaseListViewAdapter(List<?> list) {
		mItemList = (List<CacheItem>) list;
		mListSize = list.size();
	}

	public BaseListViewAdapter(ArrayList<CacheItem> list, AdapterView<?> adapterView) {
		mAdapterView = adapterView;
		mItemList = (List<CacheItem>) list;
		mListSize = list.size();
		for (int i = 0; i < mListSize; i++) {
			mWeakHashMap.put(Integer.valueOf(i), list.get(i));
		}
	}

	@Override
	public int getCount() {
		return mItemList == null ? 0 : mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList == null ? null : mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		bufItem = ((CacheItem) mItemList.get(position));
		if (mAdapterView != null) {
			if (position <= mLastPosition) {
				// cond_2
				if (position < mLastPosition) {
					this.f = ((CacheItem) mWeakHashMap.get(Integer.valueOf(mAdapterView.getLastVisiblePosition()
							+ mCacheVisible)));
					if (this.f != null)
						this.f.setVisible(false);
				}
			} else {

				this.f = ((CacheItem) mWeakHashMap.get(Integer.valueOf(mAdapterView.getFirstVisiblePosition() - mCacheVisible)));
				if (this.f != null)
					this.f.setVisible(false);
			}

		}
	    // cond_0
	    mLastPosition = position;
	    bufItem.setVisible(true);
	    return bufItem.getView(convertView, position);
	    
	}
	@Override
	public void notifyDataSetChanged() {
		mListSize = mItemList.size();
		if (mAdapterView != null) {
			mWeakHashMap.clear();
			for (CacheItem item : mItemList) {
				item.setVisible(false);
			}
			for (int i = 0; i < mListSize; i++) {
				mWeakHashMap.put(i, mItemList.get(i));
			}
		}
		super.notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetInvalidated() {
		mListSize = mItemList.size();
		if (mAdapterView != null) {
			mWeakHashMap.clear();
			for (CacheItem item : mItemList) {
				item.setVisible(false);
			}
			for (int i = 0; i < mListSize; i++) {
				mWeakHashMap.put(i, mItemList.get(i));
			}
		}
		super.notifyDataSetInvalidated();
	}
	
	// 关闭所有Item的可见性
	public void colseItemVisible() {
		for (CacheItem item : mItemList) {
			item.setVisible(false);
		}
	}

	/***
	 * 设置显示缓存数量 会上下扩展的
	 * 
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void setVisibleCacheItem(int number) {
		if(number < 2)
			number = 2;
		mCacheVisible = number;
	}
	public void onStop() {
		if (mAdapterView != null) {
			for (CacheItem item : mItemList) {
				item.setVisible(false);
			}
		}
	}
	public void onResume() {
		notifyDataSetChanged();
	}
	public void updataAll() {
		notifyDataSetInvalidated();
	}

	public void updata() {
		notifyDataSetChanged();
	}

	public void onDestroy() {
		if (mItemList != null)
			mItemList.clear();
		if (mWeakHashMap != null)
			mWeakHashMap.clear();
		mListSize = 0;
	}
}
