package me.youxiong.person.adapter;

import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import me.youxiong.person.model.PersionBriefInfo;
import me.youxiong.person.utils.LoadImageUtils;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 扣分统计 Item 的适配器
 * 
 * @author mythwind
 * 
 */
public class AdapterProductDeduction extends MyBaseAdapter {

	public AdapterProductDeduction(Context context) {
		super(context);
	}
	
	public AdapterProductDeduction(Context context, List<?> list) {
		super(context, list);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		
		if(convertView == null) {
			viewHodler = new ViewHodler();
			convertView = getLayoutInflater().inflate(R.layout.item_product_deduction, null);
			viewHodler.icon = (ImageView) convertView.findViewById(R.id.iv_item_deduction_icon);
			viewHodler.name = (TextView) convertView.findViewById(R.id.tv_item_deduction_name);
			viewHodler.title = (TextView) convertView.findViewById(R.id.tv_item_deduction_title);
			viewHodler.number = (TextView) convertView.findViewById(R.id.tv_item_deduction_num);
			viewHodler.time = (TextView) convertView.findViewById(R.id.tv_item_deduction_time);
			convertView.setTag(viewHodler);
			
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		PersionBriefInfo info = (PersionBriefInfo) getItem(position);
		viewHodler.name.setText(info.getReal_name());
		viewHodler.title.setText(info.getCard_title());
		viewHodler.number.setText(info.getKey_word());
		viewHodler.time.setText(info.getCreate_time());
		if(!TextUtils.isEmpty(info.getPhoto())) {
			LoadImageUtils.displayImage(info.getPhoto(), viewHodler.icon);
		}
		
		return convertView;
	}
	private static final class ViewHodler {
		ImageView icon;
		TextView name;
		TextView title;
		TextView number;
		TextView time;
	}
}
