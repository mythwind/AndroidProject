package me.youxiong.person.adapter;

import java.util.Arrays;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 个人中心适配器接口
 * @author mythwind
 */
public class AdapterIndividual extends MyBaseAdapter {

	private int[] mIconList;
	private String[] mTiltList;
	
	public AdapterIndividual(Context context) {
		super(context);
		
		TypedArray ar = context.getResources().obtainTypedArray(R.array.individual_info_icon);
		int len = ar.length();
		mIconList = new int[len];
		for (int i = 0; i < len; i++) {    
			mIconList[i] = ar.getResourceId(i, 0);
		}
		ar.recycle();
		
		mTiltList = context.getResources().getStringArray(R.array.individual_info);
		setList(Arrays.asList(mTiltList));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = getLayoutInflater().inflate(R.layout.item_individual_center, null);
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
			viewHolder.title = (TextView) convertView.findViewById(R.id.item_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.icon.setImageResource(mIconList[position % mIconList.length]);
		viewHolder.title.setText(mTiltList[position % mTiltList.length]);
		switch (position) {
		case 0:
			break;
		default:
			break;
		}
		return convertView;
	}
	
	public String getTitleByPosition(int position) {
		return mTiltList[position % mTiltList.length];
	}

	private static class ViewHolder {
		ImageView icon;
		TextView title;
	}
}
