package me.youxiong.person.ui.fragment;

import java.util.ArrayList;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterFriendsRequest;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 *  好友申请。。。。
 * @author mythwind
 *
 */
public class FriendsRequestsFragment extends BaseFragment {

	private View mView = null;
	private ListView mListView;
	private View mEmptyView;
	private ImageView mEmptyImageView;
	private TextView mEmptyTextView;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mListView.setEmptyView(mEmptyView);
		
		AdapterFriendsRequest adapter = new AdapterFriendsRequest(mActivity);
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			arrayList.add("测试" + (i + 1));
		}
		adapter.setList(arrayList);
		mListView.setAdapter(adapter);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// fragment_1_startsearch
		mView = inflater.inflate(R.layout.simple_listview, null);
		mListView = (ListView) mView.findViewById(R.id.simple_listview);
		return mView;
	}
	

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
