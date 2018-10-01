package me.youxiong.person.ui;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.dialog.EditTextViewDialog;
import me.youxiong.person.dialog.EditTextViewDialog.IDialogDismiss;
import me.youxiong.person.model.RechargeInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

public class RechargeActivity extends BaseFragmentActivity implements OnClickListener {
	// 加载界面
	private View mLoadingView;
	private ImageView mLoadingIcon;
	private TextView mLoadingText;
	
	private EditTextViewDialog mEditTextDialog;
	
	private TextView mTvRechargeAmount;
	private TextView mTvIntegralTotal;
	private TextView mTvRechargePurchase;
	private TextView mTvGetIntegral;
	private TextView mTvCommit;
	
	private BigDecimal mAmount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_recharge);
	}
	
	@Override
	protected void initViewComponent() {
		mLoadingView = findViewById(R.id.hint_loading_layout);
		mLoadingIcon = (ImageView) findViewById(R.id.hint_loading_icon);
		mLoadingText = (TextView) findViewById(R.id.hint_loading_text);
		
		mTvRechargeAmount = (TextView) findViewById(R.id.tv_recharge_amount);
		mTvIntegralTotal = (TextView) findViewById(R.id.tv_recharge_integral_total);
		mTvRechargePurchase = (TextView) findViewById(R.id.tv_recharge_purchase);
		mTvGetIntegral = (TextView) findViewById(R.id.tv_recharge_get_integral);
		mTvCommit = (TextView) findViewById(R.id.tv_recharge_commit);
	}

	@Override
	protected void bindData() {
		setTileText(R.string.recharge_title);
		launchRequestRecharge();
		
		mLoadingView.setVisibility(View.VISIBLE);
		mLoadingIcon.startAnimation(getRotateLoadingAnimation());
		mLoadingText.setText(R.string.loading_message);
	}

	@Override
	protected void registerListener() {
		findViewById(R.id.view_recharge_purchase).setOnClickListener(this);
		mTvCommit.setOnClickListener(this);
	}

	private void launchRequestRecharge() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_RECHARGE, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 重新获取数据
		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(this));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		return params;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_recharge_purchase:
			mEditTextDialog = new EditTextViewDialog(this, getString(R.string.recharge_purchase_amount), mTvRechargePurchase);
			mEditTextDialog.setInputType(EditorInfo.TYPE_CLASS_PHONE);
			mEditTextDialog.setCanBeNull(true);
			mEditTextDialog.setDialogDismiss(new IDialogDismiss() {
				@Override
				public void onPositive(String text) {
					if(TextUtils.isEmpty(text)) {
						mTvRechargePurchase.setText("");
						return ;
					}
					// TODO 处理总龙币数的方法
					int purchaseAmount = 0;
					try {
						purchaseAmount = Integer.parseInt(text);
					} catch (Exception e) {
						showToast("请输入数字");
						mTvRechargePurchase.setText("");
						return;
					}
					mTvGetIntegral.setText(String.valueOf(purchaseAmount * 365));
				}
				@Override
				public void onNegative() {
				}
			});
			mEditTextDialog.show();
			break;
		case R.id.tv_recharge_commit:
			String purchaseStr = mTvRechargePurchase.getText().toString();
			if(TextUtils.isEmpty(purchaseStr)) {
				showToast("请输入购买金额!");
				return;
			}
			BigDecimal purchase = new BigDecimal(purchaseStr);
			//设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)   
			purchase = purchase.setScale(2, BigDecimal.ROUND_HALF_UP);
			int delta = purchase.compareTo(mAmount);
			if(delta > 0) { // purchase > mAmount
				// 金额不足，需要充值
				showToast("金额不足，需要充值接口!!");
			} else {
				// 直接购买 
				showToast("购买金币!!");
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_RECHARGE:
			Logger.i("RechargeActivity----onRequestSuccessed=" + response);
			try {
				handleRecharge(response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_RECHARGE:
			mLoadingView.setVisibility(View.VISIBLE);
			mLoadingIcon.clearAnimation();
			mLoadingIcon.setImageResource(R.drawable.ic_error);
			mLoadingText.setText(R.string.error_message);
			break;
		default:
			break;
		}
	}
	private void handleRecharge(String response) throws Exception {
		if(TextUtils.isEmpty(response)) {
			mLoadingView.setVisibility(View.VISIBLE);
			mLoadingIcon.clearAnimation();
			mLoadingIcon.setImageResource(R.drawable.ic_error);
			mLoadingText.setText(R.string.error_message);
			
			Log.e("FaceValue", " ***** response is empty!");
			return;
		}
		ReturnJsonModel json = JsonParseUtil.parse(response);
		if(null != json && json.getIsSuccess()) {
			if(!TextUtils.isEmpty(json.getReturnValue())) {
				RechargeInfo info = JsonParseUtil.getRechargeInfo(json.getReturnValue());
				if(null != info) {
					mAmount = new BigDecimal(info.money);
					//设置小数位数，第一个变量是小数位数，第二个变量是取舍方法(四舍五入)   
					mAmount = mAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
					
					mTvRechargeAmount.setText(getString(R.string.item_rmb_price, info.money));
					mTvIntegralTotal.setText(String.valueOf(info.integral));
					mLoadingIcon.clearAnimation();
					mLoadingView.setVisibility(View.GONE);
					return;
				}
			}
		}
		mLoadingView.setVisibility(View.VISIBLE);
		mLoadingIcon.clearAnimation();
		mLoadingIcon.setImageResource(R.drawable.ic_error);
		mLoadingText.setText(R.string.error_message);
	}

}
