package me.youxiong.person.ui.fragment;

import java.util.ArrayList;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterTransmit;
import me.youxiong.person.ui.GotoTransmitActivity;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TransmitFromOthersFragment extends BaseFragment {

	private View mView = null;
	private ListView mTransmitList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// fragment_1_startsearch
		mView = inflater.inflate(R.layout.fragment_transmit, null);
		mTransmitList = (ListView) mView.findViewById(R.id.simple_listview);
		return mView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		AdapterTransmit adapter = new AdapterTransmit(mActivity);
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			arrayList.add("测试" + (i + 1));
		}
		adapter.setList(arrayList);
		mTransmitList.setAdapter(adapter);
		
		mTransmitList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mActivity, GotoTransmitActivity.class);
				
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
