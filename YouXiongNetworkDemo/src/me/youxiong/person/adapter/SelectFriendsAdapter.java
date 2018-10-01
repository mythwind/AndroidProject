package me.youxiong.person.adapter;

import java.util.ArrayList;
import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.model.FriendBasicInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 选择好友的适配器
 * @author mythwind
 *
 */
public class SelectFriendsAdapter extends BaseExpandableListAdapter {
	List<FriendBasicInfo> mGroupList = new ArrayList<FriendBasicInfo>();    
    List<List<FriendBasicInfo>> mChildList = new ArrayList<List<FriendBasicInfo>>();
    private LayoutInflater mInflater;
    
	public SelectFriendsAdapter(Context context, List<FriendBasicInfo> groupList, List<List<FriendBasicInfo>> childList) {
		mInflater = LayoutInflater.from(context);
		mGroupList = groupList;
		mChildList = childList;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mChildList.get(groupPosition).get(childPosition);
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		return mChildList.get(groupPosition) == null ? 0 : mChildList.get(groupPosition).size();
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final View view = mInflater.inflate(R.layout.item_friends_list, null);
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.item_friends_checkbox);
		ImageView icon = (ImageView) view.findViewById(R.id.item_friends_icon);
		TextView name  = (TextView) view.findViewById(R.id.item_friends_name);
		TextView occupation  = (TextView) view.findViewById(R.id.item_friends_occupation);
		
		checkBox.setVisibility(View.VISIBLE);
		checkBox.setChecked(mChildList.get(groupPosition).get(childPosition).isChecked());
		return view;
	}
	@Override
	public Object getGroup(int groupPosition) {
		return mGroupList.get(groupPosition) == null ? null : mGroupList.get(groupPosition).getName();
	}
	@Override
	public int getGroupCount() {
		return mGroupList == null ? null : mGroupList.size();
	}
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final TextView view = (TextView) mInflater.inflate(R.layout.simple_textview, null);
		view.setText(getGroup(groupPosition).toString());
		// view.setOnClickListener(myOnClickListener);
		return view;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	@Override
	public boolean hasStableIds() {
		return true;
	}
}
