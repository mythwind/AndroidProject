package me.youxiong.person.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.model.PersionBriefInfo;
import me.youxiong.person.model.PersionCardInfo;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.LoadImageUtils;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonInfoFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = "PersonInfoActivity";
	private ImageView mPersonIcon;
	
	private TextView mPersonName;
	private TextView mPersonJob;
	private TextView mTvPersonFaceValue;
	private TextView mPersonCreditTotal;
	private TextView mPersonProfessionTotal;
	private TextView mPersonDistance;
	private TextView mTvPersonLevel;
	private TextView mPersonMobile;
	private TextView mPersonTelephone;
	private TextView mPersonEmail;
	private TextView mPersonSoftName;
	private TextView mPersonSoftNum;
	private TextView mPersonWorktime;
	private TextView mPersonAddress;
	private TextView mPersonProfession;
	
	private PersionBriefInfo mBriefInfo;
	private PersionCardInfo mCardInfo;
	private boolean mIsLogined;
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_personinfo, null);
		initViewComponent(root);
		return root;
		
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindData();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		// 重新获取数据
		String userName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
			mIsLogined = true;
		}
		
	}
	
	private void initViewComponent(View root) {
		mPersonIcon = (ImageView) root.findViewById(R.id.frag_person_icon);
		
		mPersonName = (TextView) root.findViewById(R.id.frag_person_name);
		mPersonJob = (TextView) root.findViewById(R.id.frag_person_job);
		mTvPersonFaceValue = (TextView) root.findViewById(R.id.frag_tv_person_face_value);
		
		mPersonCreditTotal = (TextView) root.findViewById(R.id.person_credit_total);
		mPersonProfessionTotal = (TextView) root.findViewById(R.id.person_profession_total);
		mPersonDistance = (TextView) root.findViewById(R.id.person_distance);
		mTvPersonLevel = (TextView) root.findViewById(R.id.frag_tv_person_level);
		
		// mPersonCredit = (TextView) root.findViewById(R.id.frag_person_credit);
		mPersonMobile = (TextView) root.findViewById(R.id.person_mobile);
		mPersonTelephone = (TextView) root.findViewById(R.id.person_telephone);
		mPersonEmail = (TextView) root.findViewById(R.id.person_email);
		mPersonSoftName = (TextView) root.findViewById(R.id.person_soft_name);
		mPersonSoftNum = (TextView) root.findViewById(R.id.person_soft_num);
		
		mPersonWorktime = (TextView) root.findViewById(R.id.person_worktime);
		mPersonAddress = (TextView) root.findViewById(R.id.person_address);
		mPersonProfession = (TextView) root.findViewById(R.id.person_profession);
		
	}
	private void bindData() {
		// 获取传递过来的参数, 得到用户的详细信息
		Bundle bundle = getArguments();
		mBriefInfo = (PersionBriefInfo) bundle.getSerializable("info");
		mIsLogined = bundle.getBoolean("islogin");
		
		if(!mIsLogined) {
			if(null != mBriefInfo) {
	        	if(!TextUtils.isEmpty(mBriefInfo.getPhoto())) {
	    			LoadImageUtils.displayImage(mBriefInfo.getPhoto(), mPersonIcon);
	    		}
	        	mPersonName.setText(mBriefInfo.getReal_name());
	        }
			mPersonJob.setText(R.string.hint_view_after_login);
        	mTvPersonFaceValue.setText(R.string.hint_view_after_login);
        	
        	mPersonCreditTotal.setText(R.string.hint_view_after_login);
        	mPersonProfessionTotal.setText(R.string.hint_view_after_login);
        	mPersonDistance.setText(R.string.hint_view_after_login);
        	mTvPersonLevel.setText(R.string.hint_view_after_login);
			mPersonMobile.setText(R.string.hint_view_after_login);
			mPersonTelephone.setText(R.string.hint_view_after_login);
			mPersonEmail.setText(R.string.hint_view_after_login);
			mPersonSoftName.setText(R.string.hint_view_after_login);
			mPersonSoftNum.setText(R.string.hint_view_after_login);
			mPersonWorktime.setText(R.string.hint_view_after_login);
			mPersonAddress.setText(R.string.hint_view_after_login);
			mPersonProfession.setText(R.string.hint_view_after_login);
		} else {
			launchRequestPerson();
		}
	}
	private void launchRequestPerson() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_CARD_INFO, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	private Map<String, String> getRequestParams() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		// 重新获取数据
		String username = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("UserId", String.valueOf(mBriefInfo.getUser_id()));
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		return params;
	}
	
	@Override
	public void onClick(View v) {
	}
	
	private void handlePersonInfoRequestSucess(String response) {
		if(TextUtils.isEmpty(response)) {
			Log.e(TAG, TAG + " **** response is null, request error!!!");
			return ;
		}
        ReturnJsonModel json = JsonParseUtil.parse(response);
        if(null == json || TextUtils.isEmpty(json.getReturnValue())) {
        	Log.e(TAG, TAG + " **** json object is null, request error!!!");
        	return ;
        }
        mCardInfo = JsonParseUtil.getPersionCardInfo(json.getReturnValue());
        
        if(null == mCardInfo) {
        	Log.e(TAG, TAG + " **** mCardInfo is null, parse error!!!");
        	return ;
        }
    	if(!TextUtils.isEmpty(mCardInfo.getPhoto())) {
			LoadImageUtils.displayImage(mCardInfo.getPhoto(), mPersonIcon);
		}
    	mPersonName.setText(mCardInfo.getReal_name());
    	mPersonJob.setText(mCardInfo.getCard_title());
    	
    	String s = mBriefInfo.getFace_value_real();
    	if(TextUtils.isEmpty(s)) {
    		s = "0";
    	}
    	mTvPersonFaceValue.setText(getString(R.string.person_face_value, s));
    	
    	mPersonCreditTotal.setText(String.valueOf(mCardInfo.getCredit_total()));
    	mPersonProfessionTotal.setText(String.valueOf(mCardInfo.getProfession_total()));
    	mPersonDistance.setText(mCardInfo.getDistance());
    	
    	PersonDetailInfo currUser = PreferencesUtility.getUserInfo(mActivity);
    	if(currUser.getUser_level() >= 6) {
    		// 显示用户等级
    		mTvPersonLevel.setText(String.valueOf(mCardInfo.getUser_level()));
    	} else {
    		String limitHint = getResources().getString(R.string.user_level_limit);
    		Spannable wordtoSpan = new SpannableString(limitHint);  
    		wordtoSpan.setSpan(new TextAppearanceSpan(mActivity, R.style.STextStyleOne), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
    		wordtoSpan.setSpan(new TextAppearanceSpan(mActivity, R.style.STextStyleTwo), 2, limitHint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    		mTvPersonLevel.setText(wordtoSpan);
    	}
    	
		mPersonMobile.setText(mCardInfo.getMobile() == null ? "" : mCardInfo.getMobile());
		mPersonTelephone.setText(mCardInfo.getPhone() == null ? "" : mCardInfo.getPhone());
		mPersonEmail.setText(mCardInfo.getEmail() == null ? "" : mCardInfo.getEmail());
		mPersonSoftName.setText(mCardInfo.getSoft_name() == null ? "" : mCardInfo.getSoft_name());
		mPersonSoftNum.setText(mCardInfo.getSoft_number() == null ? "" : mCardInfo.getSoft_number());
		
		mPersonWorktime.setText(mCardInfo.getWork_time() == null ? "" : mCardInfo.getWork_time());
		mPersonAddress.setText(mCardInfo.getAddress() == null ? "" : mCardInfo.getAddress());
		mPersonProfession.setText(mCardInfo.getProfession());
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		Logger.i(TAG + "----response:" + response);
		// response
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_CARD_INFO:
			handlePersonInfoRequestSucess(response);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
	
}
