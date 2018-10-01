package me.youxiong.person.cache;

import android.content.Context;
import android.view.LayoutInflater;

public abstract class BufCacheItem implements CacheItem {

	protected Context mContext;
	protected LayoutInflater mInflater;
	protected boolean mVisible;

	public BufCacheItem(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public void setVisible(boolean visible) {
		mVisible = visible;
	}

	public boolean getVisible() {
		return mVisible;
	}
}
