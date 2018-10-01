package me.youxiong.person.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterProductList;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ProductDetailInfo;
import me.youxiong.person.model.ProductListInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.PersonProductDetailActivity;
import me.youxiong.person.ui.ProductManagerActivity;
import me.youxiong.person.ui.fragment.base.LazyLoadFragment;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.kanak.emptylayout.EmptyLayout;

/**
 * 
 * 用户产品管理  --- 产品列表
 * 
 */
public class ProductListFragment extends LazyLoadFragment implements OnItemClickListener {
	private static final String TAG = ProductManagerActivity.class.getSimpleName();
	
	public static final int REQUEST_CODE_ADD_PRODUCT = 1 << 4;
	public static final int REQUEST_CODE_EDIT = 1 << 5;
	
	private AdapterProductList mAdapter;
	private ListView mProductListView;
	private EmptyLayout mEmptyLayout;
	/*
	private View mEmptyView;
	private TextView mEmptyTextView;
	*/
	private View mCurrentItemView;
	private int mCurrentItemPos;
	
	private PopupWindow mPopupWindow;
	private TextView mTvPopDeleteItem;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindData();
		registeListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_personproduct, null);
		mProductListView = (ListView) view.findViewById(R.id.base_listview);
		/*
		mEmptyView = view.findViewById(R.id.empty_listview);
		mEmptyTextView = (TextView) view.findViewById(R.id.empty_content);
		*/
		return view;
	}
	
	private void bindData() {

		mEmptyLayout = new EmptyLayout(mActivity, mProductListView);
		mEmptyLayout.showLoading();
		
		mAdapter = new AdapterProductList(mActivity);
		mProductListView.setAdapter(mAdapter);
		
		initDelPopup();
		
	}
	private void registeListener() {
		mProductListView.setOnItemClickListener(this);
		mProductListView.setOnItemLongClickListener(mItemLongClick);
	}
	
	@Override
	protected void firstVisiable() {
		requestProductList();
	}

	private void requestProductList() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_PRODUCT_LIST, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	private Map<String, String> getRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		String strUserName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String strPassword = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		PersonDetailInfo detailInfo = PreferencesUtility.getUserInfo(mActivity);
		
		Logger.d(TAG + "  ProductFragment----Request.mUserId=" + detailInfo.getUser_id());
		params.put("UserId", String.valueOf(detailInfo.getUser_id()));
		params.put("Page", "1");
		params.put("PerPage", "15");
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put(YXConstants.REQUEST_TOKEN_KEY, strUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, strPassword.trim());

		Logger.i(TAG + "  ProductFragment----username.trim()=" + strUserName.trim());
		Logger.i(TAG + "  ProductFragment----password.trim()=" + strPassword.trim());
        
		return params;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ProductDetailInfo detailInfo = (ProductDetailInfo) parent.getAdapter().getItem(position);
		// PersonProductDetailActivity.openActivity(mActivity, productInfo, true);
		
		Intent intent = new Intent(mActivity, PersonProductDetailActivity.class);
		intent.putExtra(PersonProductDetailActivity.EXTRA_PRODUCT_BRIEF, detailInfo);
		intent.putExtra(PersonProductDetailActivity.EXTRA_FROM_USER, true);
		startActivityForResult(intent, ProductListFragment.REQUEST_CODE_EDIT);
	}
	
	private OnItemLongClickListener mItemLongClick = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			mCurrentItemView = view;
			mCurrentItemPos = position;
			view.setBackgroundColor(mActivity.getResources().getColor(R.color.item_press_color));
            showDelPopup(view);
			return true;
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Logger.e("ProductListFragment---onActivityResult  ");
		
		switch (requestCode) {
		case REQUEST_CODE_EDIT:
			if(resultCode == Activity.RESULT_OK) {
				if(null != data && data.getBooleanExtra("ProductModified", false)) {
					// 如果是修改产品之后，则需要重新请求数据刷新
					requestProductList();
				}
			}
			break;
		case REQUEST_CODE_ADD_PRODUCT:
			if(resultCode == Activity.RESULT_OK) {
				// 如果是修改产品之后，则需要重新请求数据刷新
				requestProductList();
			}
			break;
		default:
			break;
		}
	
	};
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		Logger.d(TAG + "  ProductFragment----response=" +  response);
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_PRODUCT_LIST:
			handleProductList(response);
			break;
		case MyRequestFactory.REQUEST_TYPE_PRODUCT_STATUS:
			ReturnJsonModel json = JsonParseUtil.parse(response);
			if(null != json) {
				if(json.getIsSuccess()) {
					mAdapter.getList().remove(mCurrentItemPos);
					mAdapter.notifyDataSetChanged();
					showToast("删除产品成功!");
				} else {
					showToast(json.getDescription());
				}
			}
			break;
		default:
			break;
		}
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_PRODUCT_LIST:
			mEmptyLayout.showError();
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
            	Logger.d(TAG + "  ProductFragment----productList=" +  productList);
            	if(null == productList || null == productList.my_product_list) {
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
        	} else {
        		mEmptyLayout.showError();
        	}
        } else {
        	mEmptyLayout.showError();
        	Logger.e(TAG + "  ProductFragment----json==null");
        }
	}


	private void launchRequestStatus() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_PRODUCT_STATUS, getStatusRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	private Map<String, String> getStatusRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		String strUserName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String strPassword = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		params.put(YXConstants.REQUEST_TOKEN_KEY, strUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, strPassword.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("Status", "3");
		
		ProductDetailInfo info = (ProductDetailInfo) mAdapter.getItem(mCurrentItemPos);
		params.put("ProductId", info.getProduct_id());
		
		Logger.d("============strUserName:" + strUserName);
		Logger.d("============strPassword:" + strPassword);
		
		return params;
	}
	
	@SuppressWarnings("deprecation")
	private void initDelPopup() {
		View container = View.inflate(mActivity, R.layout.layout_popwindow_del, null);
		mTvPopDeleteItem = (TextView) container.findViewById(R.id.popup_tv_item_delete);
		mTvPopDeleteItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				launchRequestStatus();
			}
		});
		
        mPopupWindow = new PopupWindow(container, 
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				if(null != mCurrentItemView) {
					mCurrentItemView.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
				}
			}
		});
        
	}
	private void showDelPopup(View v) {
		if(null == mPopupWindow) {
			Log.e(TAG, "****** popupwindow does not init!");
			return;
		}
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		mPopupWindow.getContentView().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int popupWidth = mPopupWindow.getContentView().getMeasuredWidth();
		int popupHeight =  mPopupWindow.getContentView().getMeasuredHeight();
		mPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
	}
}
