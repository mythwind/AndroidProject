package me.youxiong.person.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.dialog.EditTextViewDialog;
import me.youxiong.person.dialog.EditTextViewDialog.IDialogDismiss;
import me.youxiong.person.model.KeywordsInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyStringRequest;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.Utility;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

/**
 * 关键字设置
 * @author mythwind
 *
 */
public class ProductAddKeySettingActivity extends BaseFragmentActivity implements OnClickListener {
	private static final String TAG = ProductAddKeySettingActivity.class.getSimpleName();
	
	private EditTextViewDialog mEditTextViewDialog;
	private LinearLayout mContainerView;
	private TextView mTvDeductTotal;
	private TextView mTvAddContinue;
	
	private RequestQueue mRequestQueue;
	private ArrayList<KeywordsInfo> mKeywordsInfos = new ArrayList<KeywordsInfo>();
	private String mDeductTotal = "";
	private int mCurrDeductIntegral = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		mDeductTotal = intent.getStringExtra("deduct_integral_total");
		mKeywordsInfos.clear();
		mKeywordsInfos = intent.getParcelableArrayListExtra("keywords_info");
		
		appendMainBody(R.layout.activity_product_add_key_setting);
		
	}
	
	@Override
	protected void initViewComponent() {
		mContainerView = (LinearLayout) findViewById(R.id.view_product_add_container);
		mTvDeductTotal = (TextView) findViewById(R.id.tv_keywords_deduct_total);
		mTvAddContinue = (TextView) findViewById(R.id.product_add_tv_continue);
	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		mTextOption.setVisibility(View.VISIBLE);
		mTextOption.setText(R.string.complete);
		
		if(!TextUtils.isEmpty(mDeductTotal)) {
			mTvDeductTotal.setText(mDeductTotal);
		} else {
			mTvDeductTotal.setText("0");
		}
		if(null != mKeywordsInfos && mKeywordsInfos.size() > 0) {
			for (KeywordsInfo info : mKeywordsInfos) {
				mCurrDeductIntegral += info.deductCurrency;
				addItem(info);
			}
		} else {
			addItem(new KeywordsInfo());
		}
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
	}

	@Override
	protected void registerListener() {
		findViewById(R.id.view_keywords_deduct_total).setOnClickListener(this);
		
		mTextOption.setOnClickListener(this);
		mTvAddContinue.setOnClickListener(this);
	}
	
	private void launchRequestRank(final KeywordsInfo info, final TextView textview) {
		MyStringRequest request = new MyStringRequest(
				MyRequestFactory.PRODUCT_KEYWORD_RAND, 
				MyRequestFactory.REQUEST_TYPE_KEYWORD_RAND, 
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						handleRank(response, info, textview);
					}
				}, null) {
			
			@Override
			protected Map<String, String> getParams() {
				return getRequestParams(info.keywords, String.valueOf(info.deductCurrency));
			}
		};
		mRequestQueue.add(request);
	}
	
	private Map<String, String> getRequestParams(String keyword, String score) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 重新获取数据
		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		
		// Keyword
		params.put("Keyword", keyword);
		params.put("Score", score);
		
		return params;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_keywords_deduct_total:
			mEditTextViewDialog = new EditTextViewDialog(this, getString(R.string.keywords_deduct_total), mTvDeductTotal);
			mEditTextViewDialog.setInputType(EditorInfo.TYPE_CLASS_PHONE);
			if("0".equals(mEditTextViewDialog.getEditText())) {
				mEditTextViewDialog.setEditText("");
			}
			mEditTextViewDialog.show();
			mEditTextViewDialog.setDialogDismiss(new IDialogDismiss() {
				@Override
				public void onPositive(String text) {
					if(TextUtils.isEmpty(text)) {
						mTvDeductTotal.setText("0");
					}
				}
				@Override
				public void onNegative() {
				}
			});
			break;
		case R.id.product_add_tv_continue:
			addItem(new KeywordsInfo());
			break;
		case R.id.text_option: // 完成
			for (int i = mKeywordsInfos.size() - 1; i >= 0; i--) {
				KeywordsInfo info = mKeywordsInfos.get(i);
				if(TextUtils.isEmpty(info.keywords)) {
					mKeywordsInfos.remove(info);
				}
				if(0 == info.deductCurrency) {
					mKeywordsInfos.remove(info);
					showToast(R.string.hint_keyword_setting_deduct);
					return ;
				}
			}
			Intent data = new Intent();
			data.putExtra("deduct_integral_total", mTvDeductTotal.getText().toString());
			data.putParcelableArrayListExtra("keywords_info", mKeywordsInfos);
			setResult(RESULT_OK, data);
			finish();
			break;
		default:
			break;
		}
	}

	private void addItem(final KeywordsInfo info) {
		if(TextUtils.isEmpty(info.keywordId)) {
			mKeywordsInfos.add(info);
		}
		
		final View v = getLayoutInflater().inflate(R.layout.item_keywords_setting, null);
		final TextView key = (TextView) v.findViewById(R.id.item_keywords_textview);
		final TextView deduct = (TextView) v.findViewById(R.id.item_keywords_tv_deduct);
		final TextView ranking = (TextView) v.findViewById(R.id.item_keywords_tv_ranking);
		TextView clickCount = (TextView) v.findViewById(R.id.item_keywords_tv_click_count);
		ImageView clear = (ImageView) v.findViewById(R.id.item_keywords_iv_clear);
		
		v.findViewById(R.id.view_item_keywords).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEditTextViewDialog = new EditTextViewDialog(ProductAddKeySettingActivity.this, getString(R.string.keywords_setting), key);
				mEditTextViewDialog.show();
				mEditTextViewDialog.setDialogDismiss(new IDialogDismiss() {
					@Override
					public void onPositive(String text) {
						info.keywords = text;
					}
					@Override
					public void onNegative() {
					}
				});
			}
		});
		v.findViewById(R.id.view_item_keywords_deduct).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(info.keywords)) {
					showToast(R.string.hint_keywords);
					return ;
				}
				final int integralOld = CommonUtils.parseInt(deduct.getText().toString());
				
				mEditTextViewDialog = new EditTextViewDialog(ProductAddKeySettingActivity.this, getString(R.string.click_deduct_currency), deduct);
				mEditTextViewDialog.setInputType(EditorInfo.TYPE_CLASS_PHONE);
				if("0".equals(mEditTextViewDialog.getEditText())) {
					mEditTextViewDialog.setEditText("");
				}
				mEditTextViewDialog.show();
				mEditTextViewDialog.setDialogDismiss(new IDialogDismiss() {
					@Override
					public void onPositive(String text) {
						// TODO 处理总龙币数的方法
						int integralNew = 0;
						try {
							integralNew = Integer.parseInt(text);
							info.deductCurrency = integralNew;
						} catch (Exception e) {
							info.deductCurrency = 0;
							showToast(R.string.hint_number);
							deduct.setText(String.valueOf(integralOld));
							return;
						}
						mCurrDeductIntegral += integralNew - integralOld;
						int total = CommonUtils.parseInt(mTvDeductTotal.getText().toString());
						if(mCurrDeductIntegral > total) {
							showToast(R.string.hint_integral_exceed);
							info.deductCurrency = integralOld;
							deduct.setText(String.valueOf(integralOld));
							return;
						}
						launchRequestRank(info, ranking);
					}
					@Override
					public void onNegative() {
					}
				});
			}
		});
		
		key.setText(info.keywords);
		deduct.setText(String.valueOf(info.deductCurrency));
		ranking.setText(String.valueOf(info.ranking));
		clickCount.setText(String.valueOf(info.clickCount));
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		float dimen = getResources().getDimension(R.dimen.keywords_setting_margin);
		lp.topMargin = Utility.dpToPx(getResources(), dimen);
		mContainerView.addView(v, lp);
		
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				mContainerView.removeView(v);
				mKeywordsInfos.remove(info);
			}
		});
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_KEYWORD_RAND:
			// handleRank(response);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

	private void handleRank(String response, KeywordsInfo info, TextView textview) {
		// Logger.e("keyword-------------response=" + response);
		if(TextUtils.isEmpty(response)) {
			Log.e(TAG, " ******** get product keywords rand is empty!!!");
			return;
		}
		ReturnJsonModel json = JsonParseUtil.parse(response);
		if(json == null) {
			Log.e(TAG, " ******** get product keywords rand is empty!!! response=" + response);
			return;
		}
		// Logger.e("keyword-------------json.getReturnValue()=" + json.getReturnValue());
		if(json.getIsSuccess() && !TextUtils.isEmpty(json.getReturnValue())) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(json.getReturnValue());
				String rank = jsonObject.getString("rank");
				textview.setText(rank);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
