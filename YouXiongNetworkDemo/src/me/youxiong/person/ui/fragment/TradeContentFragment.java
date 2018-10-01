package me.youxiong.person.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.adapter.SelectTradeAdapter;
import me.youxiong.person.model.BaseDaoModel;
import me.youxiong.person.ui.TradeSelectActivity;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.config.YXConstants;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnChildClickListener;

public class TradeContentFragment extends BaseFragment {
	private static TradeContentFragment mFragment;
	private SelectTradeAdapter mAdapter;
	private ExpandableListView mExpandableListView;

	private TradeSelectActivity conditionActivity;

	private List<BaseDaoModel> mTradeList;

	private TradeContentFragment() {
		
	}
	public synchronized static TradeContentFragment getInstance(List<BaseDaoModel> tradeList) {
		if(mFragment == null) {
			mFragment = new TradeContentFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("tradeList", (ArrayList<? extends Parcelable>)tradeList);
		mFragment.setArguments(bundle);
		return mFragment;
	}

	public void noticeListChanged(BaseDaoModel object) {
		if (mAdapter != null && null != conditionActivity) {
			int parentId = object.getParentId();
			for (BaseDaoModel item : mTradeList) {
				if(item.getId() == parentId) {
					List<BaseDaoModel> childList = item.getSubList();
					BaseDaoModel child = childList.get(childList.indexOf(object));
					child.setChecked(!child.isChecked());
					
				}
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		FragmentActivity activity = getActivity();
		if ((activity instanceof TradeSelectActivity))
			conditionActivity = ((TradeSelectActivity) activity);

	}

	@Override
	public void onActivityCreated(Bundle paramBundle) {
		super.onActivityCreated(paramBundle);
		if (conditionActivity == null)
			return;

		mTradeList = getArguments().getParcelableArrayList("tradeList");
		
		mAdapter = new SelectTradeAdapter(conditionActivity);
		mAdapter.updateTradeList(mTradeList);
		mExpandableListView.setAdapter(mAdapter);
		mExpandableListView.setGroupIndicator(null);
		mExpandableListView.setOnChildClickListener(childClickListener);

	}

	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
		// <include layout="@layout/simple_expandable_list" />
		View localView = paramLayoutInflater.inflate(R.layout.simple_expandable_list, null);
		mExpandableListView = ((ExpandableListView) localView.findViewById(R.id.simple_expandable_list));
		return localView;
	}
	
	private  OnChildClickListener childClickListener = new OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			// SelectTradeAdapter adapter = (SelectTradeAdapter) parent.getExpandableListAdapter();
			BaseDaoModel object = mAdapter.getChild(groupPosition, childPosition);
			ImageView imageView = (ImageView) v.findViewById(R.id.condition_checkbox);
			
			if(imageView.isSelected()) {
				conditionActivity.removeSelectedItem(object);
				imageView.setSelected(false);
				object.setChecked(false);
				mAdapter.notifyDataSetChanged();
			} else {
				if(conditionActivity.getSelectedList().size() < YXConstants.LIMIT_SELECT_TRADE_NUMBER) {
					conditionActivity.addSelectedItem(object);
					imageView.setSelected(true);
					object.setChecked(true);
					mAdapter.notifyDataSetChanged();
				} else {
					showToast(getString(R.string.trade_selected_upper_limit, YXConstants.LIMIT_SELECT_TRADE_NUMBER));
				}
			}
			return false;
		}
	};
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

}
