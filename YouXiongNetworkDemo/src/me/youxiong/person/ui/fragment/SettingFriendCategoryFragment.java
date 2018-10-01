package me.youxiong.person.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import me.youxiong.person.dialog.CustomSelectDialog;
import me.youxiong.person.model.FriendGroupInfo;
import me.youxiong.person.model.FriendGroupList;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.SettingDetailActivity;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kanak.emptylayout.EmptyLayout;

public class SettingFriendCategoryFragment extends BaseFragment {
	private static final String TAG = SettingDetailActivity.class.getSimpleName();
	
	private static SettingFriendCategoryFragment mInstance;
	
	private AdapterFriendCategory mAdapter;
	private ListView mListView;
	private EmptyLayout mEmptyLayout;
	
	private String mEditName;
	private int mCurrItemPos = -1;
	
	private SettingFriendCategoryFragment() {}
	public synchronized static SettingFriendCategoryFragment getInstannce() {
		if(mInstance == null) {
			mInstance = new SettingFriendCategoryFragment();
		}
		return mInstance;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindData();
		registeListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting_friend_categry, null);
		mListView = (ListView) view.findViewById(R.id.friend_category_listview);
		return view;
	}
	private void bindData() {
		
		mEmptyLayout = new EmptyLayout(mActivity, mListView);
		mEmptyLayout.showLoading();
		
		mAdapter = new AdapterFriendCategory(mActivity);
		mListView.setAdapter(mAdapter);
		// 加 ListView 底部下划线
		TextView v = new TextView(mActivity);
		v.setTextAppearance(mActivity, R.style.DivideLineStyle);
		mListView.addFooterView(v);
		
		launchRequestFriGroup();
	}
	private void registeListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				mCurrItemPos = position;
				FriendGroupInfo info = (FriendGroupInfo) parent.getAdapter().getItem(position);
				
				showEditDialog(mActivity, getString(R.string.setting_friend_category_title), info);
			}
		});
	}
	
	private void launchRequestFriGroup() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_SETTING_FRIEND_GROUP, getRequestParams(null));
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	private void launchRequestEditFriGroup(final FriendGroupInfo info) {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_SETTING_FRIEND_GROUP_EDIT, getRequestParams(info));
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams(FriendGroupInfo info) {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		String strUserName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String strPassword = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		params.put(YXConstants.REQUEST_TOKEN_KEY, strUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, strPassword.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		if(null != info) {
			params.put("GroupId", String.valueOf(info.group_id));
			params.put("GroupName", mEditName);
		}
		return params;
	}

	// REQUEST_TYPE_SETTING_FRIEND_GROUP
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		Logger.d("#### onRequestSuccessed  response=" + response);
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_SETTING_FRIEND_GROUP:
			handleFriGroupList(response);
			break;
		case MyRequestFactory.REQUEST_TYPE_SETTING_FRIEND_GROUP_EDIT:
			handleFriGroupEdit(response);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_SETTING_FRIEND_GROUP:
			mEmptyLayout.setErrorMessage(errMsg);
    		mEmptyLayout.showError();
			break;
		default:
			break;
		}
	}
	
	private void handleFriGroupList(String response) {
		ReturnJsonModel json = JsonParseUtil.parse(response);
        if(null != json) {
        	if(json.getIsSuccess() && !TextUtils.isEmpty(json.getReturnValue())) {
        		FriendGroupList groupList = JsonParseUtil.getFriendGroup(json.getReturnValue());
            	Logger.d(TAG + "  ProductFragment----productList=" +  groupList);
            	if(null == groupList || null == groupList.my_friend_group_list) {
            		mEmptyLayout.setEmptyMessage("该用户暂时没有好友分组信息");
            		mEmptyLayout.showEmpty();
            		
            	} else {
		            mAdapter.setList(groupList.my_friend_group_list);
		            mAdapter.notifyDataSetChanged();
            	}
        	} 
        } else {
        	Logger.e(TAG + "  ProductFragment----json==null");
        }
	}
	
	private void handleFriGroupEdit(String response) {
		ReturnJsonModel json = JsonParseUtil.parse(response);
        if(null != json) {
        	if(json.getIsSuccess()) {
        		if(mCurrItemPos != -1) {
	        		FriendGroupInfo info = (FriendGroupInfo) mAdapter.getList().get(mCurrItemPos);
	        		info.group_name = mEditName;
	        		mAdapter.notifyDataSetChanged();
	        		showToast("编辑成功");
        		}
        	} else {
        		showToast("编辑失败");
        	}
        } else {
        }
	}
	
	private void showEditDialog(final Context context, String title, final FriendGroupInfo info) {
		final CustomSelectDialog mDialog = new CustomSelectDialog(context);
		final EditText mEditText = new EditText(context);
		mDialog.setTitleText(title);
		mEditText.setText(info.group_name);
		mEditText.requestFocus();
		mDialog.setView(mEditText);
		mDialog.setRightButtonListener(R.string.positive_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mEditName = mEditText.getText().toString();
				launchRequestEditFriGroup(info);
				
				// mFriendCategory[position] = mEditText.getText().toString();
			}
		});
		mDialog.setLeftButtonListener(R.string.negative_button, null);
		
		mEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s.toString())) {
					mDialog.setRightButtonEnable(false);
					Toast.makeText(context, R.string.hint_not_empty, Toast.LENGTH_SHORT).show();
				} else {
					mDialog.setRightButtonEnable(true);
				}
			}
		});
		mDialog.show();
	}
	private class AdapterFriendCategory extends MyBaseAdapter {
		
		public AdapterFriendCategory(Context context) {
			super(context);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if(convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.item_friend_categry, null);
				holder.name = (TextView) convertView.findViewById(R.id.tv_item_friend_category_name);
				// holder.edit = (TextView) convertView.findViewById(R.id.tv_item_friend_category_edit);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final FriendGroupInfo info = (FriendGroupInfo) getItem(position);
			holder.name.setText(info.group_name);
			/*
			holder.edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCurrItemView = v;
					mCurrItemPos = position;
					showEditDialog(mActivity, 
							getString(R.string.setting_friend_category_title), holder.name, info);
				}
			});
			*/
			return convertView;
		}
	}
	private static class ViewHolder {
		TextView name;
		// TextView edit;
	}

}
