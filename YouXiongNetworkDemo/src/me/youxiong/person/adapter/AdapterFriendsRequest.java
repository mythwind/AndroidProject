package me.youxiong.person.adapter;

import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterFriendsRequest extends MyBaseAdapter {

	public AdapterFriendsRequest(Context context) {
		super(context);
	}
	
	public AdapterFriendsRequest(Context context, List<?> list) {
		super(context, list);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		
		if(convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.item_friends_list, null);
			viewHodler = new ViewHodler();
			viewHodler.icon = (ImageView) convertView.findViewById(R.id.item_friends_icon);
			viewHodler.name = (TextView) convertView.findViewById(R.id.item_friends_name);
			viewHodler.occupation = (TextView) convertView.findViewById(R.id.item_friends_occupation);
			viewHodler.layer = convertView.findViewById(R.id.item_friends_layer);
			viewHodler.agree = (TextView) convertView.findViewById(R.id.item_friends_agree);
			viewHodler.ignore = (TextView) convertView.findViewById(R.id.item_friends_ignore);
			convertView.setTag(viewHodler);
			
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		
		viewHodler.name.setText((String) getItem(position));
		viewHodler.layer.setVisibility(View.VISIBLE);
		return convertView;
	}
	private static final class ViewHodler {
		ImageView icon;
		TextView name;
		TextView occupation;
		View layer;
		TextView agree;
		TextView ignore;
	}
}
