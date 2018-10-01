package me.youxiong.person.adapter;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterFriendsRecommend extends MyBaseAdapter {

	public AdapterFriendsRecommend(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		
		if(convertView == null) {
			viewHodler = new ViewHodler();
			convertView = getLayoutInflater().inflate(R.layout.item_friends_list, null);
			viewHodler.icon = (ImageView) convertView.findViewById(R.id.item_friends_icon);
			viewHodler.name = (TextView) convertView.findViewById(R.id.item_friends_name);
			viewHodler.occupation = (TextView) convertView.findViewById(R.id.item_friends_occupation);
			viewHodler.tips = (TextView) convertView.findViewById(R.id.item_friends_tips);
			viewHodler.distance = (TextView) convertView.findViewById(R.id.item_friends_distance);
			viewHodler.ignore = (TextView) convertView.findViewById(R.id.item_friends_ignore);
			viewHodler.agree = (TextView) convertView.findViewById(R.id.item_friends_agree);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		viewHodler.icon.setImageResource(R.drawable.ic_launcher);
		viewHodler.name.setText((String) getItem(position));
		viewHodler.distance.setVisibility(View.VISIBLE);
		viewHodler.distance.setText("0.5km");
		
		return convertView;
	}
	private static final class ViewHodler {
		ImageView icon;
		TextView name;
		TextView occupation;
		TextView tips;
		TextView distance;
		TextView ignore;
		TextView agree;
	}
	
}
