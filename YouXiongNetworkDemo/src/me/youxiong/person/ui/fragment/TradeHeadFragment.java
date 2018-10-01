package me.youxiong.person.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.model.BaseDaoModel;
import me.youxiong.person.ui.TradeSelectActivity;
import me.youxiong.person.ui.fragment.base.BaseListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TradeHeadFragment extends BaseListFragment {
	private static TradeHeadFragment mFragment;
	
	private List<BaseDaoModel> mSelectedList;
	private MyHeadAdapter mAdapter;
	
	private TradeSelectActivity conditionActivity;
	
	// private TextView mNumberChoicedView;
	
	private TradeHeadFragment() {
		
	}
	public synchronized static TradeHeadFragment getInstance(List<BaseDaoModel> selectedList) {
		if(mFragment == null) {
			mFragment = new TradeHeadFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("selectedList", (ArrayList<? extends Parcelable>) selectedList);
		mFragment.setArguments(bundle);
		return mFragment;
	}
	
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		
		FragmentActivity activity = getActivity();
		if ((activity instanceof TradeSelectActivity))
			conditionActivity = ((TradeSelectActivity) activity);
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (conditionActivity == null)
			return;
		
		mSelectedList = getArguments().getParcelableArrayList("selectedList");
		mAdapter = new MyHeadAdapter(mActivity, R.layout.fragment_trade_head_item, R.id.head_item_text, mSelectedList);
		setListAdapter(mAdapter);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_trade_head, null);
		// mNumberChoicedView = ((TextView) mActivity.findViewById(R.id.trade_num_choiced));
		return root;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		BaseDaoModel object = mAdapter.getItem(position);
		noticeHeadChanged(object, false);
		conditionActivity.noticeListChanged(object);
		
	}
	
	public void noticeHeadChanged(BaseDaoModel object, boolean addOrRemove) {
		if (mAdapter != null) {
			if(addOrRemove) {
				mSelectedList.remove(object);
				mSelectedList.add(object);
			} else {
				mSelectedList.remove(object);
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	private class MyHeadAdapter extends ArrayAdapter<BaseDaoModel> {
		private LayoutInflater mInflater;
		private int mResource;
		private int mFieldId;

		public MyHeadAdapter(Context context, int layoutId, int resId, List<BaseDaoModel> list) {
			super(context, layoutId, resId, list);
			mInflater = LayoutInflater.from(context);
			mResource = layoutId;
			mFieldId = resId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(mResource, parent, false);
				holder.selectedName = (TextView) convertView.findViewById(mFieldId);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			BaseDaoModel baseItem = (BaseDaoModel) getItem(position);
			if (baseItem != null) {
				holder.selectedName.setText(baseItem.getName());
			}
			return convertView;
		}
		
	}
	private static class ViewHolder {
		TextView selectedName;
	}
}
