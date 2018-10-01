package me.youxiong.person.adapter;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterTransmit extends MyBaseAdapter {

	public AdapterTransmit(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		
		if(convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.item_friends_list, null);
			viewHodler = new ViewHodler();
			viewHodler.icon = (ImageView) convertView.findViewById(R.id.item_friends_icon);
			viewHodler.name = (TextView) convertView.findViewById(R.id.item_friends_name);
			viewHodler.time = (TextView) convertView.findViewById(R.id.item_friends_occupation);
			
			convertView.setTag(viewHodler);
			
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		
		viewHodler.name.setText((String) getItem(position));
		viewHodler.time.setText("转发时间：2015-01-12");
		return convertView;
	}
	private static final class ViewHodler {
		ImageView icon;
		TextView name;
		TextView time;
	}
}
