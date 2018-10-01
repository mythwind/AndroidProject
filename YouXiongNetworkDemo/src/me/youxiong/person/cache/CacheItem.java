package me.youxiong.person.cache;

import android.view.View;

public abstract interface CacheItem {

	public abstract View getView(View convertView, int position);

	public abstract void setVisible(boolean visible);

	public abstract boolean getVisible();
}
