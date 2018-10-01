package me.youxiong.person.ui.fragment;

import me.youxiong.person.R;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *  好友列表
 * @author mythwind
 *
 */
public class FriendsVisitorFragment extends BaseFragment {

	public String[] groups = { "我访问的人", "访问我的人" };
	public String[][] children = { { "张三", "李四", "王五" },
			{ "龙井饭店", "西湖饭店", "天山饭庄", "杭州客运公司", "西湖旅游客运集团" } };
	
	
	private View mView = null;
	private ExpandableListView mExpandableListView;
	private MyExpandableListAdapter mAdapter;
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new MyExpandableListAdapter();
		
		mExpandableListView.setAdapter(mAdapter);
		mExpandableListView.setOnChildClickListener(new MyOnChildClickListener());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// fragment_1_startsearch
		mView = inflater.inflate(R.layout.simple_expandable_list, null);
		mExpandableListView = (ExpandableListView) mView.findViewById(R.id.simple_expandable_list);
		return mView;
	}
	
	public class MyOnChildClickListener implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
//			Intent intent = new Intent();
//			intent.putExtra("group", groups[groupPosition]);
//			intent.putExtra("child", children[groupPosition][childPosition]);
//			intent.setClass(ReimbursementScreen.this,
//					ReimbursementInputScreen.class);
//			startActivity(intent);
//			finish();
			return false;
		}
	}
	
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
		// Sample data set. children[i] contains the children (String[]) for groups[i].

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}
		@Override
		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}
		
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			final View view = mInflater.inflate(R.layout.item_friends_list, null);
			ImageView icon = (ImageView) view.findViewById(R.id.item_friends_icon);
			TextView name  = (TextView) view.findViewById(R.id.item_friends_name);
			TextView occupation  = (TextView) view.findViewById(R.id.item_friends_occupation);
			
			return view;
		}
		@Override
		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}
		@Override
		public int getGroupCount() {
			return groups.length;
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

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
