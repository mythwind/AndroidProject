package me.youxiong.person.adapter;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import me.youxiong.person.model.IntegralInfoModel;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 
 * 我的龙币 Adapter
 * @author mythwind
 */
public class AdapterIntagral extends MyBaseAdapter {

	public AdapterIntagral(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		
		if(convertView == null) {
			viewHodler = new ViewHodler();
			convertView = getLayoutInflater().inflate(R.layout.item_integral_list, null);
			viewHodler.remarkTextView = (TextView) convertView.findViewById(R.id.tv_item_intagral_remark);
			viewHodler.valueTextView = (TextView) convertView.findViewById(R.id.tv_item_intagral_value);
			viewHodler.timeTextView = (TextView) convertView.findViewById(R.id.tv_item_intagral_time);
			convertView.setTag(viewHodler);
			
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		
		IntegralInfoModel info = (IntegralInfoModel) getItem(position);
		if(null != info) {
			viewHodler.remarkTextView.setText(info.remark);
			viewHodler.valueTextView.setText(info.change_value);
			viewHodler.timeTextView.setText(info.change_time);
		}
		return convertView;
	}
	private static final class ViewHodler {
		TextView remarkTextView;
		TextView valueTextView;
		TextView timeTextView;
	}
}
