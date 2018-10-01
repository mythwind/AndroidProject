package me.youxiong.person.ui.fragment;

import me.youxiong.person.R;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  好友列表
 * @author mythwind
 *
 */
public class FriendsListFragment extends BaseFragment {
	public String[] groups = { "家人亲戚", "同学好友", "公司同事" };
	public String[][] children = { { "西湖大酒店", "杭州湾大酒店", "古墩大酒店" },
			{ "龙井饭店", "西湖饭店", "天山饭庄" }, { "杭州客运公司", "西湖旅游客运集团" }, { "杭州交通运输公司", "西湖国际旅游公司" } };
	
	private View mView = null;
	private EditText mFriendName;
	private Button mSearchButton;
	private ExpandableListView mExpandableListView;
	private MyExpandableListAdapter mAdapter;
	
//	private View mEmptyView;
//	private ImageView mEmptyImageView;
//	private TextView mEmptyTextView;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mAdapter = new MyExpandableListAdapter();
		
		mExpandableListView.setAdapter(mAdapter);
		mExpandableListView.setOnChildClickListener(new MyOnChildClickListener());
		
		registerForContextMenu(mExpandableListView);
		
		((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mFriendName.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// fragment_1_startsearch
		mView = inflater.inflate(R.layout.fragment_friends_list, null);
		mExpandableListView = (ExpandableListView) mView.findViewById(R.id.simple_expandable_list);
		mFriendName = (EditText) mView.findViewById(R.id.friend_name);
		mSearchButton = (Button) mView.findViewById(R.id.search);
		
//		mEmptyView = mView.findViewById(R.id.empty_listview);
//		mEmptyImageView = (ImageView) mView.findViewById(R.id.empty_imageview);
//		mEmptyTextView = (TextView) mView.findViewById(R.id.empty_content);
		return mView;
	}
	
	public class MyOnChildClickListener implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			Intent intent = new Intent();
//			intent.putExtra("group", groups[groupPosition]);
//			intent.putExtra("child", children[groupPosition][childPosition]);
//			intent.setClass(ReimbursementScreen.this,
//					ReimbursementInputScreen.class);
//			startActivity(intent);
//			finish();
			return false;
		}
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
	      ExpandableListContextMenuInfo adapterContextMenuInfo = (ExpandableListContextMenuInfo) menuInfo;
	      View targetView = adapterContextMenuInfo.targetView;
	      Object obj = targetView.getTag();
	      // group的长按事件时
	      int type = ExpandableListView.getPackedPositionType(adapterContextMenuInfo.packedPosition);
	      
	      
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			menu.setHeaderTitle("好友");
			// add context menu item
			menu.add(0, 1, Menu.NONE, "好友分组");
			menu.add(0, 2, Menu.NONE, "删除好友");

			Toast.makeText(mActivity, "child", Toast.LENGTH_SHORT).show();
		} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			Toast.makeText(mActivity, "group", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// 得到当前被选中的item信息
		ExpandableListContextMenuInfo adapterContextMenuInfo = (ExpandableListContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
		case 1:
			// do something
			Toast.makeText(mActivity, "点击好友分组", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(mActivity, "点击删除好友", Toast.LENGTH_SHORT).show();
			break;
		default:
			return super.onContextItemSelected(item);
		}
		return true;
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
