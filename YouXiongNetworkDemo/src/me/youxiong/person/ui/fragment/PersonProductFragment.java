package me.youxiong.person.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterProductList;
import me.youxiong.person.model.PersionBriefInfo;
import me.youxiong.person.model.ProductDetailInfo;
import me.youxiong.person.model.ProductListInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.PersonProductDetailActivity;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kanak.emptylayout.EmptyLayout;

public class PersonProductFragment extends BaseFragment implements OnItemClickListener {
	private static final String TAG = "PersonInfoActivity";
	
	private ListView mProductListView;
	private EmptyLayout mEmptyLayout;
	private int mUserId;
	@SuppressWarnings("unused")
	private boolean mIsLogined;
	private boolean mFirstVisiable = true;
	
	// private AdapterUserProductList mAdapter;
	private AdapterProductList mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_personproduct, null);
		initViewComponent(root);
		return root;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle = getArguments();
		mUserId = ((PersionBriefInfo) bundle.getSerializable("info")).getUser_id();
		mIsLogined = bundle.getBoolean("islogin");
		
		bindData();
		registeLinstener();
		
	}
	
	private void initViewComponent(View root) {
		mProductListView = (ListView) root.findViewById(R.id.base_listview);
		/*
		mEmptyView = root.findViewById(R.id.empty_listview);
		mEmptyImageView = (ImageView) root.findViewById(R.id.empty_imageview);
		mEmptyTextView = (TextView) root.findViewById(R.id.empty_content);
		*/
	}
	private void bindData() {
		mEmptyLayout = new EmptyLayout(mActivity, mProductListView);
		mEmptyLayout.showLoading();
		// mAdapter = new AdapterUserProductList(mActivity);
		mAdapter = new AdapterProductList(mActivity);
		mProductListView.setAdapter(mAdapter);
	}
	
	private Map<String, String> getRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		String strUserName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String strPassword = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Logger.d(TAG + "  ProductFragment----Request.mUserId=" + mUserId);
		params.put("UserId", String.valueOf(mUserId));
		params.put("Page", "1");
		params.put("PerPage", "15");
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put(YXConstants.REQUEST_TOKEN_KEY, strUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, strPassword.trim());
		return params;
	}

	private void registeLinstener() {
		mProductListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == ProductListFragment.REQUEST_CODE_EDIT && resultCode == Activity.RESULT_OK) {
			if(null != data && data.getBooleanExtra("ProductModified", false)) {
				// 如果是修改产品之后，则需要重新请求数据刷新
				requestProductList();
			}
		}
	};
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser && mFirstVisiable) {
			requestProductList();
			mFirstVisiable = false;
		}
	}
	@Override
	public void onDetach() {
		super.onDetach();
		mFirstVisiable = true;
	}
	
	private void requestProductList() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_PRODUCT_LIST, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ProductDetailInfo detailInfo =  (ProductDetailInfo) mAdapter.getItem(position);
		// PersonProductDetailActivity.openActivity(mActivity, detailInfo, false);
		
		Intent intent = new Intent(mActivity, PersonProductDetailActivity.class);
		intent.putExtra(PersonProductDetailActivity.EXTRA_PRODUCT_BRIEF, detailInfo);
		intent.putExtra(PersonProductDetailActivity.EXTRA_FROM_USER, false);
		startActivityForResult(intent, ProductListFragment.REQUEST_CODE_EDIT);
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		Logger.d(TAG + "  PersonProductFragment.response:" + response);
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_PRODUCT_LIST:
			handleProductList(response);
			break;
		default:
			break;
		}
	}
	private void handleProductList(String response) {
        ReturnJsonModel json = JsonParseUtil.parse(response);
        if(null != json) {
        	if(json.getIsSuccess() && !TextUtils.isEmpty(json.getReturnValue())) {
	            ProductListInfo productList = JsonParseUtil.getPersionProductList(json.getReturnValue());
            	if(null == productList 
            			|| null == productList.my_product_list 
            			|| productList.my_product_list.size() == 0) {
            		/*
	            	mEmptyTextView.setText("该用户暂时没有产品哦，敬请期待。。。。");
	            	mProductListView.setEmptyView(mEmptyView);
	            	*/
            		mEmptyLayout.setEmptyMessage("该用户暂时没有产品哦，敬请期待。。。。");
            		mEmptyLayout.showEmpty();
            		
            	} else {
		            mAdapter.setList(productList.my_product_list);
		            mAdapter.notifyDataSetChanged();
            	}
        	} 
        } else {
        	Logger.e(TAG + "  ProductFragment----json==null");
        }
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
