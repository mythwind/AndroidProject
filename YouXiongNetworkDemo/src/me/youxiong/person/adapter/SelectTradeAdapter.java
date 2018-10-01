package me.youxiong.person.adapter;

import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.model.BaseDaoModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 选择好友的适配器
 * @author mythwind
 *
 */
public class SelectTradeAdapter extends BaseExpandableListAdapter {
	private List<BaseDaoModel> mTradeList;
    private LayoutInflater mInflater;
    
	public SelectTradeAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}
	
	public void updateTradeList(List<BaseDaoModel> tradeList) {
		mTradeList = tradeList; 
	}
	
	@Override
	public BaseDaoModel getChild(int groupPosition, int childPosition) {
		return mTradeList.get(groupPosition).getSubList().get(childPosition);
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		return mTradeList.get(groupPosition).getSubList() == null ? 0 : mTradeList.get(groupPosition).getSubList().size();
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder;
		if (convertView == null) {
			holder = new ChildHolder();
			convertView = mInflater.inflate(R.layout.item_condition_subitem_jobname, null);
			holder.mCheckBoxImage = ((ImageView) convertView.findViewById(R.id.condition_checkbox));
			holder.mChildName = ((TextView) convertView.findViewById(R.id.condition_name));
			convertView.setTag(holder);
		} else {
			holder = ((ChildHolder) convertView.getTag());
		}
		BaseDaoModel model = getChild(groupPosition, childPosition);
		holder.mChildName.setText(model.getName());
		holder.mCheckBoxImage.setSelected(model.isChecked());
		return convertView;
	}
	@Override
	public BaseDaoModel getGroup(int groupPosition) {
		return mTradeList == null ? null : mTradeList.get(groupPosition);
	}
	@Override
	public int getGroupCount() {
		return mTradeList == null ? 0 : mTradeList.size();
	}
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder holder;
		if(null == convertView) {
			holder = new GroupHolder();
			convertView = mInflater.inflate(R.layout.item_condition_subitem, null);
			holder.mArrowImage = (ImageView) convertView.findViewById(R.id.ico_arrow);
			holder.mGroupName = (TextView) convertView.findViewById(R.id.condition_name);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}
		holder.mGroupName.setText(getGroup(groupPosition).toString());
		if (!isExpanded) {
			holder.mArrowImage.setImageResource(R.drawable.arrow_filter_down);
		} else {
			holder.mArrowImage.setImageResource(R.drawable.arrow_filter_up);
		}
		// convertView.setOnClickListener(myOnClickListener);
		return convertView;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	@Override
	public boolean hasStableIds() {
		return true;
	}
	private static class ChildHolder {
		ImageView mCheckBoxImage;
		TextView mChildName;
	}
	private static class GroupHolder {
		ImageView mArrowImage;
		TextView mGroupName;
	}
}
