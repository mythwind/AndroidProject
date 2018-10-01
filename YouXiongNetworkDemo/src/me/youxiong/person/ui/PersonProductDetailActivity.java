package me.youxiong.person.ui;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.dialog.WaitDialog;
import me.youxiong.person.model.ProductDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.LoadImageUtils;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonProductDetailActivity extends BaseFragmentActivity implements OnClickListener {
	private static final String TAG = "ProductDetailActivity";
	public static final String EXTRA_PRODUCT_BRIEF = "ProductBriefInfo";
	public static final String EXTRA_FROM_USER = "isFromUser";
	
	private static final int REQUEST_CODE_MODTFY = 10;
	
	private static final int STATUS_DEFAULT = 0;
	private static final int STATUS_PUBLISH = 1;
	private static final int STATUS_OFFLINE = 2;
	private static final int STATUS_DELETE = 3;
	
	private WaitDialog mWaitDialog = null;
	private View mLoadingView;
	private TextView mLoadingTextView;
	private ImageView mLoadingImageView;
	
	private ImageView mIvProductIcon;
	private TextView mTvProductCategory;
	private TextView mTvProductTrade;
	private TextView mTvProductProfession;
	private TextView mTvProductDetailPrice;
	private TextView mTvProductOriginalPrice;
	private TextView mProductDesc;
	
	private TextView mTvModefy;
	private TextView mTvOffline;
	private TextView mTvPublish;
	private TextView mTvDelete;
	
	private String mProductName;
	private String mProductId;
	private String mProductIconUrl;
	private boolean mFromUser = false;
	private boolean mProductModified = false;
	
	private int mCurrentStatus = STATUS_PUBLISH;
	
	// tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //删除线   　//Paint.UNDERLINE_TEXT_FLAG 下划线
//	public static void openActivity(Activity activity, ProductDetailInfo briefInfo, boolean isFromUser) {
//		Intent intent = new Intent(activity, PersonProductDetailActivity.class);
//		intent.putExtra(EXTRA_PRODUCT_BRIEF, briefInfo);
//		intent.putExtra(EXTRA_FROM_USER, isFromUser);
//		activity.startActivityForResult(intent, ProductListFragment.REQUEST_CODE_EDIT);
//	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ProductDetailInfo briefInfo = (ProductDetailInfo) getIntent().getSerializableExtra(EXTRA_PRODUCT_BRIEF);
		mFromUser = getIntent().getBooleanExtra(EXTRA_FROM_USER, false);
		mProductName = briefInfo.getProduct_name();
		mProductId = briefInfo.getProduct_id();
		
		appendMainBody(R.layout.activity_person_product_detail);
		
	}
	
	@Override
	protected void initViewComponent() {
		mLoadingView = findViewById(R.id.hint_loading_layout);
		mLoadingTextView = (TextView) findViewById(R.id.hint_loading_text);
		mLoadingImageView = (ImageView) findViewById(R.id.hint_loading_icon);
		
		mIvProductIcon = (ImageView) findViewById(R.id.p_product_detail_icon);
		mTvProductTrade = (TextView) findViewById(R.id.p_product_detail_trade);
		mTvProductProfession = (TextView) findViewById(R.id.p_product_detail_profession);
		mTvProductCategory = (TextView) findViewById(R.id.p_product_detail_catetory);
		mTvProductDetailPrice = (TextView) findViewById(R.id.p_product_detail_price);
		mTvProductOriginalPrice = (TextView) findViewById(R.id.p_product_original_price);
		mProductDesc = (TextView) findViewById(R.id.p_product_detail_desc);
		
		mTvModefy = (TextView) findViewById(R.id.product_tv_detail_modify);
		mTvOffline = (TextView) findViewById(R.id.product_tv_detail_offline);
		mTvPublish = (TextView) findViewById(R.id.product_tv_detail_publish);
		mTvDelete = (TextView) findViewById(R.id.product_tv_detail_delete);
	}

	@Override
	protected void bindData() {
		setTileText(mProductName);
		if(mFromUser) {
			findViewById(R.id.product_detail_option).setVisibility(View.VISIBLE);
		}
		mLoadingView.setVisibility(View.VISIBLE);
		launchRequestInfo();
	}

	@Override
	protected void registerListener() {
		setBackViewListener(mBackViewListener);
		
		mIvProductIcon.setOnClickListener(this);
		mTvModefy.setOnClickListener(this);
		mTvOffline.setOnClickListener(this);
		mTvPublish.setOnClickListener(this);
		mTvDelete.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_tv_detail_modify:
			Intent intent = new Intent(this, ProductAddActivity.class);
			intent.putExtra(ProductAddActivity.EXTRA_PRODUCT_ID, mProductId);
			startActivityForResult(intent, REQUEST_CODE_MODTFY);
			break;
		case R.id.product_tv_detail_offline:
			mCurrentStatus = STATUS_OFFLINE;
			launchRequestStatus();
			waiting(false);
			
			break;
		case R.id.product_tv_detail_publish:
			mCurrentStatus = STATUS_PUBLISH;
			launchRequestStatus();
			waiting(false);
			
			break;
		case R.id.product_tv_detail_delete:
			deleteProduct();
			
			break;
		case R.id.p_product_detail_icon:
			if(!TextUtils.isEmpty(mProductIconUrl)) {
				if(mProductIconUrl.endsWith(".jpg") 
						|| mProductIconUrl.endsWith(".png")
						|| mProductIconUrl.endsWith(".gif")
						|| mProductIconUrl.endsWith(".bmp")) {
					PersonProductPhotoActivity.startPhotoActivity(this, mProductIconUrl);
				}
			}
			break;
		default:
			break;
		}
	}
	
	private void deleteProduct() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.dialog_delete_title, mProductName));
		builder.setMessage(getString(R.string.dialog_delete_message, mProductName));
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				mCurrentStatus = STATUS_DELETE;
				launchRequestStatus();
			}
		});
		builder.setNegativeButton(R.string.cancel, null);
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == REQUEST_CODE_MODTFY && resultCode == RESULT_OK) {
			mProductModified = true;
			// 如果是修改产品之后，则需要重新请求数据刷新
			launchRequestInfo();
		}
	}
	@Override
	public void onBackPressed() {
		Intent data = new Intent();
		data.putExtra("ProductModified", mProductModified);
		setResult(RESULT_OK, data);
		super.onBackPressed();
		
	}
	private OnClickListener mBackViewListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent data = new Intent();
			data.putExtra("ProductModified", mProductModified);
			setResult(RESULT_OK, data);
			exitActivity();
		}
	};
	
	protected void waiting(boolean cancelable) {
		mWaitDialog = new WaitDialog(this,R.style.TransparentDialog);
		mWaitDialog.setCancelable(cancelable);
		// Disable the back button
		mWaitDialog.setOnKeyListener(new Dialog.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return keyCode == KeyEvent.KEYCODE_BACK;
			}
		});
		mWaitDialog.show();
	}
	
	private void launchRequestInfo() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_PRODUCT_DETAIL, getStatusRequestParams(false));
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private void launchRequestStatus() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_PRODUCT_STATUS, getStatusRequestParams(true));
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getStatusRequestParams(boolean status) {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String strUserName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String strPassword = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		params.put(YXConstants.REQUEST_TOKEN_KEY, strUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, strPassword.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		if(status) {
			params.put("Status", String.valueOf(mCurrentStatus));
		}
		params.put("ProductId", mProductId);
		return params;
	}
	
	private void fillParams(ProductDetailInfo detailInfo) {
		if(null == detailInfo) {
			Log.e(TAG, " ********　ProductDetailInfo　is null !!!");
			showErrorPage();
			return ;
		}
		
		mLoadingView.setVisibility(View.GONE);
		
		mProductName = detailInfo.getProduct_name();
		setTileText(mProductName);
		
		mProductIconUrl = detailInfo.getProduct_icon();
		if (!TextUtils.isEmpty(mProductIconUrl)) {
			LoadImageUtils.displayImage(mProductIconUrl, mIvProductIcon, R.drawable.ico_product_default);
		}
		String category = getString(R.string.product_detail_category_value, detailInfo.getProduct_catetory());
		mTvProductCategory.setText(category);
		String trade = getString(R.string.product_detail_trade_value, detailInfo.getProduct_trades());
		mTvProductTrade.setText(trade);
		String profession = getString(R.string.product_detail_profession_value, detailInfo.getProfession_value());
		mTvProductProfession.setText(profession);
		mProductDesc.setText(detailInfo.getProduct_content());
		mTvProductDetailPrice.setText(getString(R.string.item_rmb_price, detailInfo.getProduct_price()));
		mTvProductOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		mTvProductOriginalPrice.setText(getString(R.string.item_rmb_price, detailInfo.getOriginal_product_price()));
		
		mCurrentStatus = detailInfo.getProduct_status();
		switch (mCurrentStatus) {
		case STATUS_DEFAULT:
		case STATUS_PUBLISH:
			mTvPublish.setEnabled(false);
			mTvPublish.setClickable(false);
			mTvPublish.setAlpha(0.7f);
			mTvOffline.setEnabled(true);
			mTvOffline.setClickable(true);
			mTvOffline.setAlpha(1.0f);
			break;
		case STATUS_OFFLINE:
			mTvPublish.setEnabled(true);
			mTvPublish.setClickable(true);
			mTvPublish.setAlpha(1.0f);
			mTvOffline.setEnabled(false);
			mTvOffline.setClickable(false);
			mTvOffline.setAlpha(0.7f);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_PRODUCT_DETAIL:
			handleProductDetail(response);
			break;
		case MyRequestFactory.REQUEST_TYPE_PRODUCT_STATUS:
			handleProductStatus(response);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_PRODUCT_DETAIL:
			showErrorPage();
			break;

		default:
			break;
		}
	}
	
	private void handleProductDetail(String response) {
		if(TextUtils.isEmpty(response)) {
			showErrorPage();
			return ;
		}
		Logger.d("DetailActivity----handleProductDetail.response=" +  response);
        ReturnJsonModel json = JsonParseUtil.parse(response);
        if (null != json) {
			if (json.getIsSuccess()) {
				ProductDetailInfo detailInfo = JsonParseUtil.getPersionProductDetail(json.getReturnValue());
				fillParams(detailInfo);
			} else {
				// showToast(json.getDescription());
				showErrorPage();
			}
		} else {
			showErrorPage();
		}
	}

	private void showErrorPage() {
		mLoadingTextView.setText(R.string.error_message);
		mLoadingImageView.setVisibility(View.VISIBLE);
		mLoadingImageView.setImageResource(R.drawable.ic_error);
		findViewById(R.id.hint_loading_progress).setVisibility(View.GONE);
	}
	private void handleProductStatus(String response) {
		if(null != mWaitDialog && mWaitDialog.isShowing()) {
			mWaitDialog.dismiss();
		}
		Logger.d("PersionProductDetailActivity----handleProductStatus.response=" +  response);
        ReturnJsonModel json = JsonParseUtil.parse(response);
        
        if (null != json) {
        	 if(json.getIsSuccess()) {
    		 switch (mCurrentStatus) {
    			case STATUS_DEFAULT:
    			case STATUS_PUBLISH:
    				mTvPublish.setEnabled(false);
    				mTvPublish.setClickable(false);
    				mTvPublish.setAlpha(0.7f);
    				mTvOffline.setEnabled(true);
    				mTvOffline.setClickable(true);
    				mTvOffline.setAlpha(1.0f);
    				break;
    			case STATUS_OFFLINE:
    				mTvPublish.setEnabled(true);
    				mTvPublish.setClickable(true);
    				mTvPublish.setAlpha(1.0f);
    				mTvOffline.setEnabled(false);
    				mTvOffline.setClickable(false);
    				mTvOffline.setAlpha(0.7f);
    				break;
    			case STATUS_DELETE:
    				// 刷新列表数据
    				mProductModified = true;
    				Intent data = new Intent();
    				data.putExtra("ProductModified", mProductModified);
    				setResult(RESULT_OK, data);
    				finish();
    				break;
    			default:
    				break;
    			}
        		showToast(R.string.operate_success);
        	 } else {
        		 if(TextUtils.isEmpty(json.getDescription())) {
        			 showToast(R.string.operate_failed);
        		 } else {
        			 showToast(json.getDescription());
        		 }
        	 }
		} else {
			showToast(R.string.operate_failed);
		}
	}
}
