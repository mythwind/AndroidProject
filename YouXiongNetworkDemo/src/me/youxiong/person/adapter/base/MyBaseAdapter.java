package me.youxiong.person.adapter.base;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter extends BaseAdapter {

	private Context mContext;
	private List<?> mList;

	private LayoutInflater mInflater;
	
	public MyBaseAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}
	
	public MyBaseAdapter(Context context, List<?> list) {
		mContext = context;
		mList = list;
		mInflater = LayoutInflater.from(context);
	}
	
	public LayoutInflater getLayoutInflater() {
		return mInflater;
	}
	public Context getContext() {
		return mContext;
	}

	public List<?> getList() {
		return mList;
	}

	public void setList(List<?> list) {
		mList = list;
	}
	
	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position % mList.size());
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
}
