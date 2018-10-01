package me.youxiong.person.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.database.TradeDAO;
import me.youxiong.person.database.impl.TradeDAOImpl;
import me.youxiong.person.dialog.EditTextViewDialog;
import me.youxiong.person.model.BaseDaoModel;
import me.youxiong.person.model.KeywordsInfo;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ProductDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.BitmapUtils;
import me.youxiong.person.utils.FileUtils;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.LoadImageUtils;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import me.youxiong.person.utils.UploadFileTools;
import me.youxiong.person.utils.UploadFileTools.UploadFileListener;
import me.youxiong.person.utils.UploadPhotoUtils;
import me.youxiong.person.utils.UploadPhotoUtils.IUploadPhotoLinstener;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * 添加产品
 * @author mythwind
 */
public class ProductAddActivity extends BaseFragmentActivity implements OnClickListener {
	private static final String TAG = ProductAddActivity.class.getSimpleName();
	
	public static final String EXTRA_PRODUCT_ID = "productId";
	// private static final String EXTRA_PRODUCT_INFO = "ProductInfo";
	// private static final int REQUEST_CODE_PHOTO = 1 << 1;
	private static final int REQUEST_CODE_CONTENT = 1 << 2;
	private static final int REQUEST_CODE_KEYWORDS = 1 << 3;
	
	private static final int MSG_UPLOAD_FILE_START = 0x0021;
	// private static final int MSG_UPLOAD_FILE_UPDATE = 0x0022;
	private static final int MSG_UPLOAD_FILE_FINISH = 0x0023;
	private static final int MSG_UPLOAD_FILE_SUCCESS = 0x0024;
	private static final int MSG_UPLOAD_FILE_ERROR = 0x0025;

	private EditTextViewDialog mEditTextViewDialog;
	private TextView mTvProductTitle;
	private TextView mTvMemberPrice;
	private TextView mTvProductPrice;
	private TextView mTvTradeView;
	private TextView mTvCategoryView;
	private TextView mTvDescriptionView;
	
	private TextView mTvKeywords;
	private TextView mTvDeductIntegralTotal;
	private TextView mTvIntegralTotal;
	
	private ImageView mIvProductIcon;
	private TextView mCommitView;
	
	// private RequestQueue mRequestQueue;
	private Bitmap mBmpProduct = null;
	// private String mUpFilePath = "";
	private String mProductIconUrlSuffix = "";
	
	private int mCategoryChoosedPos = 0;
	private String mKeywordJson = "";
	private String mProductId;
	private String mDeductIntegralOld = "";
	
	private UploadPhotoUtils mUploadPhoto;
	
	private TradeDAO mTradeDAO;
	private BaseDaoModel mChoosedItem; // 选择的职业
	private AlertDialog mAlertDialog;
	private ArrayList<KeywordsInfo> mKeywordsInfos = new ArrayList<KeywordsInfo>();
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
//			case MSG_UPLOAD_FILE_START:
//				mpProgressDialog.show();
//				break;
			case MSG_UPLOAD_FILE_SUCCESS:
				// mpProgressDialog.dismiss();
				String photoPath = (String) msg.obj;
				if(!TextUtils.isEmpty(photoPath)) {
					mBmpProduct = BitmapUtils.getBitmapByPath(ProductAddActivity.this, photoPath);
					// 调用上传图片的接口，上传图片。。。。
					mIvProductIcon.setImageBitmap(mBmpProduct);
					showToast("上传图片成功！");
				}
				break;
			case MSG_UPLOAD_FILE_ERROR:
				String errMsg = (String) msg.obj;
				if(!TextUtils.isEmpty(errMsg)) {
					showToast(errMsg);
				}
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mProductId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
		
		appendMainBody(R.layout.activity_product_add);
	}
	
	@Override
	protected void initViewComponent() {
		mIvProductIcon = (ImageView) findViewById(R.id.product_add_iv_icon);
		mTvProductTitle = (TextView) findViewById(R.id.product_add_tv_title);
		mTvMemberPrice = (TextView) findViewById(R.id.product_add_tv_member_price);
		mTvProductPrice = (TextView) findViewById(R.id.product_add_tv_price);
		mTvTradeView = (TextView) findViewById(R.id.product_add_tv_trade);
		mTvCategoryView = (TextView) findViewById(R.id.product_add_tv_category);
		mTvDescriptionView = (TextView) findViewById(R.id.product_add_tv_description);
		mTvKeywords = (TextView) findViewById(R.id.product_add_tv_keywords);
		
		mTvDeductIntegralTotal = (TextView) findViewById(R.id.product_add_tv_deduct_integral_total);
		mTvIntegralTotal = (TextView) findViewById(R.id.product_add_tv_integral_total);
		
		mCommitView = (TextView) findViewById(R.id.commit);
		
	}

	@Override
	protected void bindData() {
		// 能接收到 ID， 表示是修改，否则是添加
		if(!TextUtils.isEmpty(mProductId)) {
			launchRequestEdit();
			setTileText(getString(R.string.product_edit_title));
		} else {
			setTileText(getString(R.string.product_add_title));
		}
		
		mTradeDAO = new TradeDAOImpl();
		mUploadPhoto = new UploadPhotoUtils(this);
	}

	@Override
	protected void registerListener() {
		mCommitView.setOnClickListener(this);
		
		findViewById(R.id.view_add_upload_pic).setOnClickListener(this);
		findViewById(R.id.view_add_product_title).setOnClickListener(this);
		findViewById(R.id.view_add_menber_price).setOnClickListener(this);
		findViewById(R.id.view_add_price).setOnClickListener(this);
		findViewById(R.id.view_add_trade).setOnClickListener(this);
		findViewById(R.id.view_add_category).setOnClickListener(this);
		findViewById(R.id.view_add_description).setOnClickListener(this);
		findViewById(R.id.view_add_keywords_setting).setOnClickListener(this);
		findViewById(R.id.view_add_deduct_integral_total).setOnClickListener(this);
		
	}
	
	private void launchRequestEdit() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_PRODUCT_EDIT, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String strUserName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String strPassword = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		params.put(YXConstants.REQUEST_TOKEN_KEY, strUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, strPassword.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("ProductId", mProductId);
		return params;
	}

//	private void launchRequestCommit() {
//		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//		// 重新获取数据
//		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
//		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
//		PersonDetailInfo info = PreferencesUtility.getUserInfo(this);
//		
//		MultipartRequest multipartRequest = new MultipartRequest(MyRequestFactory.PRODUCT_ADD_URL, 
//				new Response.Listener<String>() {
//					@Override
//					public void onResponse(String response) {
//						Logger.i(TAG + "  commit: " + response);
//						ReturnJsonModel json = JsonParseUtil.parse(response);
//						if (null != json) {
//							if (json.getIsSuccess()) {
//								showToast("添加产品成功！");
//								finish();
//							} else {
//								showToast(json.getDescription());
//							}
//						} else {
//							showToast("添加产品失败！");
//						}
//					}
//		});
//		// 添加header
//		multipartRequest.addHeader("header-name", "value");
//		// 通过MultipartEntity来设置参数
//		MultipartEntity multi = multipartRequest.getMultiPartEntity();
//		// 文本参数 
//		multi.addStringPart(YXConstants.REQUEST_TOKEN_KEY, username.trim());
//		multi.addStringPart(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
//		multi.addStringPart(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
//		multi.addStringPart(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
//		multi.addStringPart("UserId", String.valueOf(info.getUser_id()));
//		multi.addStringPart("ProductName", mTvProductTitle.getText().toString());
//		multi.addStringPart("ProductPrice", mTvMemberPrice.getText().toString());
//		multi.addStringPart("OriginalProductPrice", mTvProductPrice.getText().toString());
//		multi.addStringPart("ProductTrade", String.valueOf(mChoosedItem.getId()));
//		multi.addStringPart("ProductCatetory", String.valueOf(mCategoryChoosedPos + 1));
//		multi.addStringPart("ProductContent", mTvDescriptionView.getText().toString());
//		multi.addStringPart("DeductIntegral", mTvDeductIntegralTotal.getText().toString());
//		multi.addStringPart("KeywordsJson", mKeywordJson);
//		
//		Logger.d("username:" + username.trim());
//		Logger.d("password:" + password.trim());
//		Logger.d("imsi:" + PackageUtil.getDeviceImsi(this));
//		Logger.d("EquipmentType:" + YXConstants.EQUIPMENT_TYPE_ANDROID);
//		Logger.d("mProductId:" + mProductId);
//		Logger.d("ProductName:" + mTvProductTitle.getText().toString());
//		Logger.d("ProductPrice" + mTvMemberPrice.getText().toString());
//		Logger.d("OriginalProductPrice" + mTvProductPrice.getText().toString());
//		Logger.d("ProductTrade" + String.valueOf(mChoosedItem.getId()));
//		Logger.d("ProductCatetory" + String.valueOf(mCategoryChoosedPos + 1));
//		Logger.d("ProductContent" + mTvDescriptionView.getText().toString());
//		Logger.d("DeductIntegral" + mTvDeductIntegralTotal.getText().toString());
//		Logger.d("DeductIntegralOld" + mDeductIntegralOld);
//		Logger.d("KeywordsJson" + mKeywordJson);
//		
//		//if(null != mBmpProduct) {
//		//	// 直接从上传 Bitmap 
//		//	multi.addBinaryPart("ProductIcon", BitmapUtils.bmpToByteArr(mBmpProduct));
//		//}
//		// 上传文件
//		// multi.addFilePart("imgfile", new File("storage/emulated/0/test.jpg"));
//		// 将请求添加到队列中 
//		mRequestQueue.add(multipartRequest);
//		
//	}
	private void launchRequestCommit() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 重新获取数据
		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		PersonDetailInfo info = PreferencesUtility.getUserInfo(this);
		
		Map<String, String> params = new HashMap<String, String>();
		// 文本参数 
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("UserId", String.valueOf(info.getUser_id()));
		params.put("ProductName", mTvProductTitle.getText().toString());
		if(!TextUtils.isEmpty(mProductIconUrlSuffix)) {
			params.put("ProductIcon", mProductIconUrlSuffix);
		}
		params.put("ProductPrice", mTvMemberPrice.getText().toString());
		params.put("OriginalProductPrice", mTvProductPrice.getText().toString());
		params.put("ProductTrade", String.valueOf(mChoosedItem.getId()));
		params.put("ProductCatetory", String.valueOf(mCategoryChoosedPos + 1));
		params.put("ProductContent", mTvDescriptionView.getText().toString());
		params.put("DeductIntegral", mTvDeductIntegralTotal.getText().toString());
		params.put("KeywordsJson", mKeywordJson);
		
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_ADD_PRODUCT, params);
		launchRequest(MyRequestFactory.execute(operation, this));
		
	}
	
//	private void launchRequestEditCommit() {
//		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//		// 重新获取数据
//		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
//		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
//		
//		MultipartRequest multipartRequest = new MultipartRequest(MyRequestFactory.PRODUCT_EDIT_COMMIT,
//				new Response.Listener<String>() {
//					@Override
//					public void onResponse(String response) {
//						Logger.i(TAG + "  edit commit: " + response);
//						ReturnJsonModel json = JsonParseUtil.parse(response);
//						if (null != json) {
//							if (json.getIsSuccess()) {
//								showToast("修改产品成功！");
//								finish();
//							} else {
//								showToast(json.getDescription());
//							}
//						} else {
//							showToast("修改产品失败！");
//						}
//					}
//				});
//		// 添加header
//		multipartRequest.addHeader("header-name", "value");
//		// 通过MultipartEntity来设置参数
//		MultipartEntity multi = multipartRequest.getMultiPartEntity();
//		// 文本参数 
//		multi.addStringPart(YXConstants.REQUEST_TOKEN_KEY, username.trim());
//		multi.addStringPart(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
//		multi.addStringPart(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
//		multi.addStringPart(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
//		multi.addStringPart("ProductId", mProductId);
//		multi.addStringPart("ProductName", mTvProductTitle.getText().toString());
//		multi.addStringPart("ProductPrice", mTvMemberPrice.getText().toString());
//		multi.addStringPart("OriginalProductPrice", mTvProductPrice.getText().toString());
//		multi.addStringPart("ProductTrade", String.valueOf(mChoosedItem.getId()));
//		multi.addStringPart("ProductCatetory", String.valueOf(mCategoryChoosedPos + 1));
//		multi.addStringPart("ProductContent", mTvDescriptionView.getText().toString());
//		multi.addStringPart("DeductIntegral", mTvDeductIntegralTotal.getText().toString());
//		multi.addStringPart("DeductIntegralOld", mDeductIntegralOld);
//		multi.addStringPart("KeywordsJson", mKeywordJson);
//		/*
//		multi.addStringPart(YXConstants.REQUEST_TOKEN_KEY, "NDYxMDcxMjVAcXEuY29t");
//		multi.addStringPart(YXConstants.REQUEST_TOKEN_SECRET, "ZTk5YTE4YzQyOGNiMzhkNWYyNjA4NTM2Nzg5MjJlMDM=");
//		multi.addStringPart(YXConstants.REQUEST_IMSI, "8686e7ce3dc7ac6105dd128e7d1350e013d44a92");
//		multi.addStringPart(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
//		multi.addStringPart("ProductId", "14");
//		multi.addStringPart("ProductName", "标题");
//		multi.addStringPart("ProductPrice", "120");
//		multi.addStringPart("OriginalProductPrice", "100");
//		multi.addStringPart("ProductTrade", "1");
//		multi.addStringPart("ProductCatetory", "1");
//		multi.addStringPart("ProductContent", "产品介绍");
//		multi.addStringPart("DeductIntegral", "330");
//		multi.addStringPart("DeductIntegralOld", "10");
//		String j= "[{\"keywordId\":\"62\",\"keywords\":\"专家\",\"deductCurrency\":12,\"deductCurrencyOld\":1,\"clickCount\":0,\"ranking\":0},{\"keywordId\":\"26\",\"keywords\":\"内科\",\"deductCurrency\":5,\"deductCurrencyOld\":1,\"clickCount\":0,\"ranking\":0}]";
//		multi.addStringPart("KeywordsJson", j);
//		*/
//		Logger.d("username:" + username.trim());
//		Logger.d("password:" + password.trim());
//		Logger.d("imsi:" + PackageUtil.getDeviceImsi(this));
//		Logger.d("EquipmentType:" + YXConstants.EQUIPMENT_TYPE_ANDROID);
//		Logger.d("mProductId:" + mProductId);
//		Logger.d("ProductName:" + mTvProductTitle.getText().toString());
//		Logger.d("ProductPrice" + mTvMemberPrice.getText().toString());
//		Logger.d("OriginalProductPrice" + mTvProductPrice.getText().toString());
//		Logger.d("ProductTrade" + String.valueOf(mChoosedItem.getId()));
//		Logger.d("ProductCatetory" + String.valueOf(mCategoryChoosedPos + 1));
//		Logger.d("ProductContent" + mTvDescriptionView.getText().toString());
//		Logger.d("DeductIntegral" + mTvDeductIntegralTotal.getText().toString());
//		Logger.d("DeductIntegralOld" + mDeductIntegralOld);
//		Logger.d("KeywordsJson" + mKeywordJson);
//		
//		if(null != mBmpProduct) {
//			// 直接从上传 Bitmap 
//			multi.addBinaryPart("ProductIcon", BitmapUtils.bmpToByteArr(mBmpProduct));
//		}
//		// 上传文件
//		// multi.addFilePart("imgfile", new File("storage/emulated/0/test.jpg"));
//		// 将请求添加到队列中 
//		mRequestQueue.add(multipartRequest);
//	}
	
	
	private void launchRequestEditCommit() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 重新获取数据
		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		// 文本参数 
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("ProductId", mProductId);
		params.put("ProductName", mTvProductTitle.getText().toString());
		if(!TextUtils.isEmpty(mProductIconUrlSuffix)) {
			params.put("ProductIcon", mProductIconUrlSuffix);
		}
		params.put("ProductPrice", mTvMemberPrice.getText().toString());
		params.put("OriginalProductPrice", mTvProductPrice.getText().toString());
		params.put("ProductTrade", String.valueOf(mChoosedItem.getId()));
		params.put("ProductCatetory", String.valueOf(mCategoryChoosedPos + 1));
		params.put("ProductContent", mTvDescriptionView.getText().toString());
		params.put("DeductIntegral", mTvDeductIntegralTotal.getText().toString());
		params.put("DeductIntegralOld", mDeductIntegralOld);
		params.put("KeywordsJson", mKeywordJson);
		
		
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_PRODUCT_EDIT_COMMIT, params);
		launchRequest(MyRequestFactory.execute(operation, this));
		
//		final UploadFileTools uploadTools = new UploadFileTools(ProductAddActivity.this, params);
//		uploadTools.setOnUploadFileListener(new UploadFileListener() {
//			@Override
//			public void onUploadProcess(int size) {
//				System.out.println("upload======onUploadProcess======size:" + size);
//			}
//			@Override
//			public void onUploadFinished(String msg) {
//				System.out.println("upload======onUploadFinished======msg:" + msg);
//			}
//			@Override
//			public void onUploadFailed(String errMsg) {
//				System.out.println("upload======onUploadFailed======errMsg:" + errMsg);
//			}
//		});
//		new Thread() {
//			@Override
//			public void run() {
//				try {
//					mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_START);
//					uploadTools.uploadFile(MyRequestFactory.PRODUCT_EDIT_COMMIT, "ProductIcon", mUpFilePath);
//					mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_FINISH);
//				} catch (IOException e) {
//					mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_FINISH);
//					e.printStackTrace();
//				}
//			}
//		}.start();
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_add_upload_pic:
			mUploadPhoto.choosePhoto(this);
			break;
		case R.id.view_add_product_title:
			mEditTextViewDialog = new EditTextViewDialog(this, getString(R.string.product_detail_title), mTvProductTitle);
			mEditTextViewDialog.show();
			break;
		case R.id.view_add_menber_price:
			mEditTextViewDialog = new EditTextViewDialog(this, getString(R.string.product_detail_vip_price), mTvMemberPrice);
			mEditTextViewDialog.show();
			break;
		case R.id.view_add_price:
			mEditTextViewDialog = new EditTextViewDialog(this, getString(R.string.product_detail_price), mTvProductPrice);
			mEditTextViewDialog.show();
			break;
		case R.id.view_add_trade: // 职业
			chooseTrade();
			break;
		case R.id.view_add_category:
			chooseCategory();
			break;
		case R.id.view_add_description:
			turnToProductDescription();
			break;
		case R.id.view_add_keywords_setting:
			Intent intent = new Intent(this, ProductAddKeySettingActivity.class);
			intent.putExtra("title", getString(R.string.keywords_setting));
			intent.putExtra("deduct_integral_total", mTvDeductIntegralTotal.getText().toString());
			intent.putParcelableArrayListExtra("keywords_info", mKeywordsInfos);
			startActivityForResult(intent, REQUEST_CODE_KEYWORDS);
			break;
		case R.id.view_add_deduct_integral_total:
			mEditTextViewDialog = new EditTextViewDialog(this, getString(R.string.keywords_deduct_total), mTvDeductIntegralTotal);
			mEditTextViewDialog.show();
			break;
		case R.id.commit:
			// 提交添加产品信息
			if(checkCommitValidate()) {
				if(TextUtils.isEmpty(mProductId)) {
					launchRequestCommit();
				} else {
					launchRequestEditCommit();
				}
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null != mBmpProduct && !mBmpProduct.isRecycled()) {
			mBmpProduct.recycle();
			mBmpProduct = null;
		}
	}
	

	private void turnToProductDescription() {
		Intent intent = new Intent(this, ProductDescriptionActivity.class);
		intent.putExtra("title", getString(R.string.product_detail_desc));
		intent.putExtra("content", mTvDescriptionView.getText().toString());
		startActivityForResult(intent, REQUEST_CODE_CONTENT);
	}

	private void chooseTrade() {
		final List<BaseDaoModel> tmpTradeList = mTradeDAO.getTradeGroupList(FileUtils.getDatabaseTradePath(this));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.product_add_select_trade);
		ListView view = (ListView) View.inflate(this, R.layout.simple_listview, null);
		
		ArrayAdapter<BaseDaoModel> adapter = new ArrayAdapter<BaseDaoModel>(this, R.layout.simple_textview_2, tmpTradeList);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(null != mAlertDialog){
					mAlertDialog.dismiss();
				}
				chooseChildTrade(tmpTradeList.get(position).getId());
			}
		});
		builder.setView(view);
		mAlertDialog = builder.create();
		mAlertDialog.show();
	}

	private void chooseChildTrade(int parentId) {
		final List<BaseDaoModel> tmpChildList = mTradeDAO.getTradeDetailListByParentId(String.valueOf(parentId), 
				FileUtils.getDatabaseTradePath(this));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.product_add_select_trade);
		ListView view = (ListView) View.inflate(this, R.layout.simple_listview, null);
		ArrayAdapter<BaseDaoModel> adapter = new ArrayAdapter<BaseDaoModel>(this, R.layout.simple_textview_2, tmpChildList);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mChoosedItem = tmpChildList.get(position);
				mTvTradeView.setText(mChoosedItem.getName());
				if(null != mAlertDialog){
					mAlertDialog.dismiss();
				}
			}
		});
		builder.setView(view);
		mAlertDialog = builder.create();
		mAlertDialog.show();
	}
	private void chooseCategory() {
		final String[] categories = getResources().getStringArray(R.array.tab_product_classify_info);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.product_detail_category);
		ListView view = (ListView) View.inflate(this, R.layout.simple_listview, null);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_textview_2, categories);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCategoryChoosedPos = position;
				mTvCategoryView.setText(categories[position]);
				if(null != mAlertDialog){
					mAlertDialog.dismiss();
				}
			}
		});
		builder.setView(view);
		mAlertDialog = builder.create();
		mAlertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case REQUEST_CODE_CONTENT:
			if(resultCode == RESULT_OK) {
				String content = data.getStringExtra("content_result");
				if(!TextUtils.isEmpty(content)) {
					mTvDescriptionView.setText(content);
				}
			}
			break;
		case REQUEST_CODE_KEYWORDS: // 关键字设置
			if(resultCode == RESULT_OK) {
				String deductTotal = data.getStringExtra("deduct_integral_total");
				mKeywordsInfos = data.getParcelableArrayListExtra("keywords_info");
				if(!TextUtils.isEmpty(deductTotal)) {
					mTvDeductIntegralTotal.setText(deductTotal);
				}
				if(null != mKeywordsInfos && mKeywordsInfos.size() > 0) {
					StringBuilder sb = new StringBuilder();
					for (KeywordsInfo info : mKeywordsInfos) {
						sb.append(info.keywords).append(",");
					}
					mTvKeywords.setText(sb.substring(0, sb.length() - 1));
					
					Gson gson = new Gson();
					mKeywordJson = gson.toJson(mKeywordsInfos);
					Logger.e("json ****** mKeywordJson=" + mKeywordJson);
				}
			}
			break;
		default:
			mUploadPhoto.onActivityResult(requestCode, resultCode, data, this);
			mUploadPhoto.setUploadPhotoLinstener(new IUploadPhotoLinstener() {
				@Override
				public void startUpload(final String photoPath) {
					// mUpFilePath = photoPath;
					
					if(TextUtils.isEmpty(photoPath)) {
						showToast("选择图片失败，请重新选择图片!");
						return ;
					}
					/*
					mBmpProduct = BitmapUtils.getBitmapByPath(ProductAddActivity.this, photoPath);
					// 调用上传图片的接口，上传图片。。。。
					mIvProductIcon.setImageBitmap(mBmpProduct);
					*/
//					mpProgressDialog = new ProgressDialog(ProductAddActivity.this);
//					//设置最大值为100  
//					mpProgressDialog.setMax(100);  
//					//设置进度条风格STYLE_HORIZONTAL  
//					mpProgressDialog.setProgressStyle(  
//					        ProgressDialog.STYLE_HORIZONTAL);  
//					mpProgressDialog.setTitle("进度条对话框");
//					
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProductAddActivity.this);
					// 重新获取数据
					String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
					String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
					
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
					params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
					params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(ProductAddActivity.this));
					params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
					
					final UploadFileTools uploadTools = new UploadFileTools(ProductAddActivity.this, params);
					uploadTools.setOnUploadFileListener(new UploadFileListener() {
						@Override
						public void onUploadProcess(int size) {
						}
						@Override
						public void onUploadFinished(String msg) {
							// mpProgressDialog.dismiss();
							if(TextUtils.isEmpty(msg)) {
								Log.e(TAG, TAG + " ******** upload photo finish, but result is null!");
								return;
							}
							ReturnJsonModel json = JsonParseUtil.parse(msg);
							if(null == json) {
								Log.e(TAG, TAG + " ******** json parse error! msg=" + msg);
								return;
							}
							if(json.getIsSuccess()) {
								if(!TextUtils.isEmpty(json.getReturnValue())) {
									JSONObject jsonObject;
									try {
										jsonObject = new JSONObject(json.getReturnValue());
										mProductIconUrlSuffix = jsonObject.getString("product_icon");
										mHandler.sendMessage(mHandler.obtainMessage(MSG_UPLOAD_FILE_SUCCESS, photoPath));
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							} else {
								// MSG_UPLOAD_FILE_ERROR
								mHandler.sendMessage(mHandler.obtainMessage(MSG_UPLOAD_FILE_ERROR, json.getDescription()));
							}
						}
						@Override
						public void onUploadFailed(String errMsg) {
							// mpProgressDialog.dismiss();
						}
					});
					new Thread() {
						@Override
						public void run() {
							try {
								mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_START);
								uploadTools.uploadFile(MyRequestFactory.PRODUCT_UPLOAD_IMAGE, "upImage", photoPath);
								mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_FINISH);
							} catch (IOException e) {
								mHandler.sendEmptyMessage(MSG_UPLOAD_FILE_FINISH);
								e.printStackTrace();
							}
						}
					}.start();
				}
			});
			break;
		}
	}
	
//	private ProgressDialog mpProgressDialog;
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		Logger.d("ProductAddActivity----onRequestSuccessed.response=" +  response);
		if(TextUtils.isEmpty(response)) {
			Log.e(TAG, TAG + " **** request success, but response is null !");
			return;
		}
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_PRODUCT_EDIT:
			handleProductDetail(response);
			break;
		case MyRequestFactory.REQUEST_TYPE_ADD_PRODUCT: {
			ReturnJsonModel json = JsonParseUtil.parse(response);
			if (null != json) {
				if (json.getIsSuccess()) {
					showToast("添加产品成功！");
					setResult(RESULT_OK);
					finish();
				} else {
					showToast(json.getDescription());
				}
			} else {
				showToast("添加产品失败！");
			}
			break;
		}
		case MyRequestFactory.REQUEST_TYPE_PRODUCT_EDIT_COMMIT: {
			ReturnJsonModel json = JsonParseUtil.parse(response);
			if (null != json) {
				if (json.getIsSuccess()) {
					showToast("修改产品成功！");
					setResult(RESULT_OK);
					finish();
				} else {
					showToast(json.getDescription());
				}
			} else {
				showToast("修改产品失败！");
			}
			break;
		}
		default:
			break;
		}
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
	
	private void handleProductDetail(String response) {
        ReturnJsonModel json = JsonParseUtil.parse(response);
       
        if (null != json) {
        	 ProductDetailInfo detailInfo = JsonParseUtil.getPersionProductDetail(json.getReturnValue());
			if (null != detailInfo && json.getIsSuccess()) {
				fillParams(detailInfo);
			} else {
				// showToast(json.getDescription());
			}
		}
	}
	private void fillParams(ProductDetailInfo info) {
		String iconUrl = info.getProduct_icon();
		if (!TextUtils.isEmpty(iconUrl)) {
			LoadImageUtils.displayImage(iconUrl, mIvProductIcon);
		}
		mTvProductTitle.setText(info.getProduct_name());
		mTvMemberPrice.setText(info.getProduct_price());
		mTvProductPrice.setText(info.getOriginal_product_price());
		BaseDaoModel base = mTradeDAO.getTradeDetailById(info.getSub_trade_id(), FileUtils.getDatabaseTradePath(this));
		if(null != base) {
			mTvTradeView.setText(base.getName());
		}
		final String[] categories = getResources().getStringArray(R.array.tab_product_classify_info);
		try {
			int category = Integer.parseInt(info.getProduct_catetory());
			mTvCategoryView.setText(categories[category % categories.length]);
		} catch (Exception e) {
			mTvCategoryView.setText(info.getProduct_catetory());
		}
		
		mTvDescriptionView.setText(info.getProduct_content());
		StringBuilder sb = new StringBuilder();
		mKeywordsInfos = info.getKeywords();
		if(null != mKeywordsInfos && mKeywordsInfos.size() > 0) {
			for (KeywordsInfo kwordsinfo : info.getKeywords()) {
				sb.append(kwordsinfo.keywords).append(",");
			}
			mTvKeywords.setText(sb.substring(0, sb.length() - 1));
			Gson gson = new Gson();
			mKeywordJson = gson.toJson(mKeywordsInfos);
			Logger.e("json ****** mKeywordJson=" + mKeywordJson);
			
		}
		mDeductIntegralOld = info.getDeduct_integral();
		mTvDeductIntegralTotal.setText(mDeductIntegralOld);
		mTvIntegralTotal.setText(info.getIntegral());
	}
	
	private boolean checkCommitValidate() {
		if(TextUtils.isEmpty(mTvProductTitle.getText().toString())) {
			showToast("产品标题不能为空");
			return false;
		}
		if(TextUtils.isEmpty(mTvMemberPrice.getText().toString())) {
			showToast("会员价格不能为空");
			return false;
		}
		if(TextUtils.isEmpty(mTvTradeView.getText().toString())) {
			showToast("职业不能为空");
			return false;
		}
		if(TextUtils.isEmpty(mTvCategoryView.getText().toString())) {
			showToast("产品类别不能为空");
			return false;
		}
		if(TextUtils.isEmpty(mTvDescriptionView.getText().toString())) {
			showToast("产品介绍不能为空");
			return false;
		}
		if(TextUtils.isEmpty(mTvKeywords.getText().toString())) {
			showToast("关键字不能为空");
			return false;
		}
		return true;
	}
	
}
