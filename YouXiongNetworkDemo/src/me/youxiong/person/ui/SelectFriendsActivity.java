package me.youxiong.person.ui;

import java.util.ArrayList;
import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.adapter.SelectFriendsAdapter;
import me.youxiong.person.model.FriendBasicInfo;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class SelectFriendsActivity extends BaseFragmentActivity implements OnClickListener {
	
	private List<FriendBasicInfo> groupData = new ArrayList<FriendBasicInfo>();    
	private List<List<FriendBasicInfo>> childData = new ArrayList<List<FriendBasicInfo>>();
    
	private ExpandableListView mExpandableListView;
	private SelectFriendsAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.fragment_friends_list);
		
		List<FriendBasicInfo> child;
		for (int i = 0; i < 3; i++) {
			FriendBasicInfo basicInfo = new FriendBasicInfo();
			basicInfo.setName("NAME_" + i);
			groupData.add(basicInfo);

			child = new ArrayList<FriendBasicInfo>();
			for (int j = 0; j < 3; j++) {
				basicInfo = new FriendBasicInfo();
				basicInfo.setName("CHILD_NAME_" + j);
				basicInfo.setChecked(false);
				child.add(basicInfo);
			}
			childData.add(child);
		}

	}
	@Override
	protected void initViewComponent() {
		mExpandableListView = (ExpandableListView) findViewById(R.id.simple_expandable_list);
	}
	
	@Override
	protected void bindData() {
		setTileText(getString(R.string.select_friend));
		mTextOption.setVisibility(View.VISIBLE);
		mTextOption.setText(R.string.person_transmit);
		// mTextOption.setOnClickListener(this);
		
		mAdapter = new SelectFriendsAdapter(this, groupData, childData);
		
		mExpandableListView.setAdapter(mAdapter);
		
		int groupCount = mAdapter.getGroupCount();
		// 展开分组显示的view
		for (int i = 0; i < groupCount; i++) {
			mExpandableListView.expandGroup(i);
		}
		
		// mExpandableListView.expandGroup(0); // 展开分组显示的view
		mExpandableListView.setOnChildClickListener(new MyOnChildClickListener());
	}

	@Override
	protected void registerListener() {
		mTextOption.setOnClickListener(this);
	}
	
	public class MyOnChildClickListener implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {

			CheckBox checkBox = (CheckBox) v.findViewById(R.id.item_friends_checkbox);      
			checkBox.toggle();

			FriendBasicInfo info = childData.get(groupPosition).get(childPosition);
			if (info.isChecked()) {
				info.setChecked(false);					
			} else {
				info.setChecked(true);
			}
			return false;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 转发
		case R.id.text_option:
			
			break;

		default:
			break;
		}
	}
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
