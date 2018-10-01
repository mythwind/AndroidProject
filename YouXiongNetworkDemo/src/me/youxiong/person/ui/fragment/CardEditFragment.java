package me.youxiong.person.ui.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.database.AddressDAO;
import me.youxiong.person.database.TradeDAO;
import me.youxiong.person.database.impl.AddressDAOImpl;
import me.youxiong.person.database.impl.TradeDAOImpl;
import me.youxiong.person.dialog.EditTextViewDialog;
import me.youxiong.person.model.BaseDaoModel;
import me.youxiong.person.model.PersionCardInfo;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.model.WorkTimeInfo;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.CardEditActivity;
import me.youxiong.person.ui.TradeSelectActivity;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.FileUtils;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import me.youxiong.person.view.WheelViewTwoStringDialog;
import me.youxiong.person.view.WheelViewTwoStringDialog.OnPositiveButtonClickListener;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * 名片编辑功能（主要是修改用户名片）
 * @author mythwind
 *
 */
public class CardEditFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = CardEditActivity.class.getSimpleName();
	
	private View mView = null;
	private EditTextViewDialog mEditTextViewDialog;
	private AlertDialog mAlertDialog;

	private TextView mPersonName;  //姓名
	private TextView mPersonCardTitle;  //职业
	private TextView mPersonMobile;
	private TextView mPersonTelephone;
	private TextView mPersonEmail;
	private TextView mPersonSoftName;
	private TextView mPersonSoftNum;
	private TextView mPersonWeekStart;
	private TextView mPersonWeekStop;
	private TextView mPersonTimeStart;
	private TextView mPersonTimeStop;
	private TextView mPersonAddress;
	private TextView mPersonProvince;
	private TextView mPersonTrades;
	private TextView mPersonCommit;
	private RadioButton mRadioButtonMale;
	private RadioButton mRadioButtonFemale;
	
	private String mUserName;
	private String mPassword;
	
	private AddressDAO mAddressDAO;
	private int mProvinceId;
	private int mCityId;
	private int mUserLevel = 1;
	
	private TradeDAO mTradeDAO;
	private List<BaseDaoModel> mSelectedTrades;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindData();
		registeListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_card_edit, null);
		mPersonName = (TextView) mView.findViewById(R.id.frag_person_name);
		mRadioButtonMale = (RadioButton) mView.findViewById(R.id.frag_person_male);
		mRadioButtonFemale = (RadioButton) mView.findViewById(R.id.frag_person_female);
		
		mPersonCardTitle = (TextView) mView.findViewById(R.id.frag_person_card_title);
		mPersonMobile = (TextView) mView.findViewById(R.id.frag_person_mobile);
		mPersonTelephone = (TextView) mView.findViewById(R.id.frag_person_telephone);
		mPersonEmail = (TextView) mView.findViewById(R.id.frag_person_email);
		mPersonSoftName = (TextView) mView.findViewById(R.id.frag_person_soft_name);
		mPersonSoftNum = (TextView) mView.findViewById(R.id.frag_person_soft_num);
		mPersonWeekStart = (TextView) mView.findViewById(R.id.frag_person_week_start);
		mPersonWeekStop = (TextView) mView.findViewById(R.id.frag_person_week_stop);
		mPersonTimeStart = (TextView) mView.findViewById(R.id.frag_person_time_start);
		mPersonTimeStop = (TextView) mView.findViewById(R.id.frag_person_time_stop);
		mPersonAddress = (TextView) mView.findViewById(R.id.frag_person_address);
		mPersonProvince = (TextView) mView.findViewById(R.id.frag_person_province);
		mPersonTrades = (TextView) mView.findViewById(R.id.frag_person_trade_title);
		mPersonCommit = (TextView) mView.findViewById(R.id.card_edit_commit);
		
		return mView;
	}
	
	private void bindData() {
		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		mUserName = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		mPassword = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_TOKEN_KEY, mUserName);
		params.put(YXConstants.REQUEST_TOKEN_SECRET, mPassword);
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_GET_EDIT_CARD, params);
		launchRequest(MyRequestFactory.execute(operation, this));
		
		mAddressDAO = new AddressDAOImpl();
		mTradeDAO = new TradeDAOImpl();
		
		PersonDetailInfo userinfo = PreferencesUtility.getUserInfo(mActivity);
		mUserLevel = userinfo.getUser_level();
		if(mUserLevel >= 3) {
			mRadioButtonMale.setVisibility(View.GONE);
			mRadioButtonFemale.setCompoundDrawables(null, null, null, null);
			mView.findViewById(R.id.frag_view_person_name).setAlpha(0.7f);
			mView.findViewById(R.id.view_frag_person_gender).setAlpha(0.7f);
		}
	}
	private void registeListener() {
		mPersonCommit.setOnClickListener(this);
		mPersonTrades.setOnClickListener(this);
		
		if(mUserLevel < 3) {
			mView.findViewById(R.id.frag_view_person_name).setOnClickListener(this);
		}
		mView.findViewById(R.id.frag_view_person_card_title).setOnClickListener(this);
		mView.findViewById(R.id.frag_view_person_mobile).setOnClickListener(this);
		mView.findViewById(R.id.frag_view_person_telephone).setOnClickListener(this);
		mView.findViewById(R.id.frag_view_person_email).setOnClickListener(this);
		mView.findViewById(R.id.frag_view_person_soft_name).setOnClickListener(this);
		mView.findViewById(R.id.frag_view_person_soft_num).setOnClickListener(this);
		mView.findViewById(R.id.frag_view_person_address).setOnClickListener(this);
		mView.findViewById(R.id.frag_view_person_province).setOnClickListener(this);
		mView.findViewById(R.id.frag_view_person_trade_title).setOnClickListener(this);
		mView.findViewById(R.id.frag_person_week_start).setOnClickListener(this);
		mView.findViewById(R.id.frag_person_week_stop).setOnClickListener(this);
		mView.findViewById(R.id.frag_person_time_start).setOnClickListener(this);
		mView.findViewById(R.id.frag_person_time_stop).setOnClickListener(this);
	}
	// 处理职业身份的功能
	private List<BaseDaoModel> handleTradeIdToList(String trade_id) {
		if(TextUtils.isEmpty(trade_id)) {
			return null;
		}
		String[] tradeIds = trade_id.split(",");
		List<BaseDaoModel> result = mTradeDAO.getTradeDetailByIds(tradeIds, FileUtils.getDatabaseTradePath(mActivity));
		return result;
	}
	private String handleTradeIdToString(List<BaseDaoModel> selectedTrades) {
		if(null == selectedTrades || selectedTrades.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (BaseDaoModel model : selectedTrades) {
			sb.append(",");
			sb.append(model.getId());
		}
		sb.append(",");
		return sb.toString();
	}
	
	private void launchRequestEditCard() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_EDIT_CARD_INFO, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_TOKEN_KEY, mUserName.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, mPassword.trim());
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("RealName", convertNull(mPersonName.getText().toString()));
		params.put("Sex", mRadioButtonMale.isChecked() ? "1" : "2");  // 男 1    女  2
		params.put("CardTitle", convertNull(mPersonCardTitle.getText().toString()));
		params.put("FixPhone", convertNull(mPersonTelephone.getText().toString()));
		params.put("MobilePhone", convertNull(mPersonMobile.getText().toString()));
		params.put("Email", convertNull(mPersonEmail.getText().toString()));
		params.put("StartWeek", convertNull(mPersonWeekStart.getText().toString()));
		params.put("StopWeek", convertNull(mPersonWeekStop.getText().toString()));
		params.put("StartTime", convertNull(mPersonTimeStart.getText().toString()));
		params.put("StopTime", convertNull(mPersonTimeStop.getText().toString()));
		params.put("SoftName", convertNull(mPersonSoftName.getText().toString()));
		params.put("SoftNumber", convertNull(mPersonSoftNum.getText().toString()));
		params.put("AddressDetail", convertNull(mPersonAddress.getText().toString()));
		
		params.put("ProvinceCode", String.valueOf(mProvinceId));
		params.put("CityCode", String.valueOf(mCityId));
		params.put("TradeId", handleTradeIdToString(mSelectedTrades));
		
		Logger.d(TAG + "----Sex=" + (mRadioButtonMale.isChecked() ? "1" : "0"));
		Logger.d(TAG + "----mProvinceId=" + mProvinceId);
		Logger.d(TAG + "----mCityId=" + mCityId);
		Logger.d(TAG + "----mUserName=" + mUserName.trim());
		Logger.d(TAG + "----mPassword=" + mPassword.trim());
		return params;
	}
	
	private String convertNull(String str) {
		if(null == str) {
			str = "";
		}
		return str;
	}
	private void notifyTradesChanged(List<BaseDaoModel> selectedTrades) {
		if(null == selectedTrades || selectedTrades.size() == 0) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (BaseDaoModel item : selectedTrades) {
			sb.append(item.getName()).append("\t");
		}
		mPersonTrades.setText(sb.toString());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.card_edit_commit:
			// 提交信息
			if(checkNotNull()) {
				commitCardInfo();
			}
			break;
		case R.id.frag_view_person_trade_title:
			Intent intent = new Intent(mActivity, TradeSelectActivity.class);
			intent.putParcelableArrayListExtra("SelectedTrades", (ArrayList<BaseDaoModel>) mSelectedTrades);
			startActivityForResult(intent, 0);
			break;
		case R.id.frag_view_person_name:
			mEditTextViewDialog = new EditTextViewDialog(mActivity, getString(R.string.person_name), mPersonName);
			mEditTextViewDialog.show();
			break;
		case R.id.frag_view_person_card_title:
			mEditTextViewDialog = new EditTextViewDialog(mActivity, getString(R.string.person_card_title), mPersonCardTitle);
			mEditTextViewDialog.show();
			break;
		case R.id.frag_view_person_mobile:
			mEditTextViewDialog = new EditTextViewDialog(mActivity, getString(R.string.person_mobile), mPersonMobile);
			mEditTextViewDialog.setCanBeNull(true);
			mEditTextViewDialog.show();
			break;
		case R.id.frag_view_person_telephone:
			mEditTextViewDialog = new EditTextViewDialog(mActivity, getString(R.string.person_telephone), mPersonTelephone);
			mEditTextViewDialog.setCanBeNull(true);
			mEditTextViewDialog.show();
			break;
		case R.id.frag_view_person_email:
			mEditTextViewDialog = new EditTextViewDialog(mActivity, getString(R.string.person_email), mPersonEmail);
			mEditTextViewDialog.setCanBeNull(true);
			mEditTextViewDialog.show();
			break;
		case R.id.frag_view_person_soft_name:
			mEditTextViewDialog = new EditTextViewDialog(mActivity, getString(R.string.person_soft_name), mPersonSoftName);
			mEditTextViewDialog.setCanBeNull(true);
			mEditTextViewDialog.show();
			break;
		case R.id.frag_view_person_soft_num:
			mEditTextViewDialog = new EditTextViewDialog(mActivity, getString(R.string.person_soft_num), mPersonSoftNum);
			mEditTextViewDialog.setCanBeNull(true);
			mEditTextViewDialog.show();
			break;
		case R.id.frag_view_person_address:
			mEditTextViewDialog = new EditTextViewDialog(mActivity, getString(R.string.person_address), mPersonAddress);
			mEditTextViewDialog.setCanBeNull(true);
			mEditTextViewDialog.show();
			break;
		case R.id.frag_view_person_province:
			WheelViewTwoStringDialog addressDialog = new WheelViewTwoStringDialog(mActivity, getString(R.string.person_choose_city), mPersonProvince, mProvinceId, mCityId);
			addressDialog.setRelateTextView(mPersonAddress);
			addressDialog.show();
			addressDialog.setOnPositiveButtonClickListener(new OnPositiveButtonClickListener() {
				@Override
				public void onClick(BaseDaoModel groupModel, BaseDaoModel childModel) {
					mProvinceId = groupModel.getId();
					mCityId = childModel.getId();
				}
			});
			break;
			
		case R.id.frag_person_week_start:
			chooseWeekStart();
			break;
		case R.id.frag_person_week_stop:
			chooseWeekStop();
			break;
		case R.id.frag_person_time_start: {
			String timeStart = mPersonTimeStart.getText().toString();
			int hourOfDay = 0;
			int minute = 0;
			if(!TextUtils.isEmpty(timeStart)) {
				String[] times = timeStart.split(":");
				if(times.length == 3) {
					hourOfDay = Integer.valueOf(times[0]);
					minute = Integer.valueOf(times[1]);
					new TimePickerDialog(mActivity, mStartTimeListener, hourOfDay, minute, true).show();
					return;
				}
			}
			Calendar calendar = Calendar.getInstance();
			hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);
			new TimePickerDialog(mActivity, mStartTimeListener, hourOfDay, minute, true).show();
			
			break;
		}
		case R.id.frag_person_time_stop: {
			String timeStop = mPersonTimeStop.getText().toString();
			int hourOfDay = 0;
			int minute = 0;
			if(!TextUtils.isEmpty(timeStop)) {
				String[] times = timeStop.split(":");
				if(times.length == 3) {
					hourOfDay = Integer.valueOf(times[0]);
					minute = Integer.valueOf(times[1]);
					new TimePickerDialog(mActivity, mStopTimeListener, hourOfDay, minute, true).show();
					return;
				}
			}
			Calendar calendar = Calendar.getInstance();
			hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);
			new TimePickerDialog(mActivity, mStopTimeListener, hourOfDay, minute, true).show();
			break;
		}
		default:
			break;
		}
	}
	
	private void chooseWeekStart() {
		if(null != mAlertDialog && mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
		}
		final String[] categories = getResources().getStringArray(R.array.week_info);
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("请选择开始星期");
		ListView view = (ListView) View.inflate(mActivity, R.layout.simple_listview, null);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.simple_textview_2, categories);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mPersonWeekStart.setText(categories[position % categories.length]);
				
				if(null != mAlertDialog && mAlertDialog.isShowing()) {
					mAlertDialog.dismiss();
				}
			}
		});
		builder.setView(view);
		mAlertDialog = builder.create();
		mAlertDialog.show();
	}
	private void chooseWeekStop() {
		if(null != mAlertDialog && mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
		}
		final String[] categories = getResources().getStringArray(R.array.week_info);
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("请选择结束星期");
		ListView view = (ListView) View.inflate(mActivity, R.layout.simple_listview, null);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.simple_textview_2, categories);
		view.setAdapter(adapter);
		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mPersonWeekStop.setText(categories[position % categories.length]);
				
				if(null != mAlertDialog){
					mAlertDialog.dismiss();
				}
			}
		});
		builder.setView(view);
		mAlertDialog = builder.create();
		mAlertDialog.show();
	}
	
	private OnTimeSetListener mStartTimeListener = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			StringBuilder sb = new StringBuilder();
			if(hourOfDay < 10) {
				sb.append("0").append(hourOfDay);
			} else {
				sb.append(hourOfDay);
			}
			sb.append(":");
			if(minute < 10) {
				sb.append("0").append(minute);
			} else {
				sb.append(minute);
			}
			sb.append(":00");
			mPersonTimeStart.setText(sb.toString());
		}
	};
	private OnTimeSetListener mStopTimeListener = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			StringBuilder sb = new StringBuilder();
			if(hourOfDay < 10) {
				sb.append("0").append(hourOfDay);
			} else {
				sb.append(hourOfDay);
			}
			sb.append(":");
			if(minute < 10) {
				sb.append("0").append(minute);
			} else {
				sb.append(minute);
			}
			sb.append(":00");
			mPersonTimeStop.setText(sb.toString());
		}
	};
	/**
	 * 不能为空的字段检查
	 * @return
	 */
	private boolean checkNotNull() {
		String real_name = mPersonName.getText().toString();
		if(TextUtils.isEmpty(real_name)) {
			showToast("姓名不能为空");
			return false;
		}
//		String email = mPersonEmail.getText().toString();
//		if(TextUtils.isEmpty(email)) {
//			showToast("邮箱不能为空");
//			return false;
//		}
//		
//		mDetailInfo.setReal_name(real_name);
//		// params.put("sex", "");
//		// 电话
//		mDetailInfo.setFix_phone(mPersonTelephone.getText().toString());
//		
//		// params.put("fax", "");
//		mDetailInfo.setMobile(mPersonMobile.getText().toString());
//		mDetailInfo.setEmail(mPersonEmail.getText().toString());
//		mDetailInfo.setQq(mPersonQQ.getText().toString());
//		mDetailInfo.setSoft_name("QQ");
//		mDetailInfo.setSoft_number(mPersonQQ.getText().toString());
//		// mDetailInfo.setWorktime(worktime);
//		mDetailInfo.setAddress_detail(mPersonAddress.getText().toString());
//		
//		// 上传职业 （trade）ids
//		mDetailInfo.setTrade_id(handleTradeIdToString(mSelectedTrades));
		return true;
	}

	/**
	 * 提交修改后的个人资料
	 */
	private void commitCardInfo() {
		launchRequestEditCard();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 0) {
			mSelectedTrades = data.getParcelableArrayListExtra("selectedList");
			notifyTradesChanged(mSelectedTrades);
		}
	};

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_EDIT_CARD:
			handleGetEditCard(response);
			break;
		case MyRequestFactory.REQUEST_TYPE_EDIT_CARD_INFO:
			handleEditCard(response);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
	
	private void handleGetEditCard(String response) {
		// response
		ReturnJsonModel json = JsonParseUtil.parse(response);
        if (null != json) {
        	Logger.i(TAG + "----json=" +  json.getReturnValue());
        	
        	if(json.getIsSuccess() && (null != json.getReturnValue())) {
        		PersionCardInfo cardInfo = JsonParseUtil.getPersionCardInfo(json.getReturnValue());
        		if(null != cardInfo) {
        			mPersonName.setText(cardInfo.getReal_name());
        			if("男".equals(cardInfo.getSex())) {
        				mRadioButtonMale.setChecked(true);
        			} else {
        				mRadioButtonFemale.setChecked(true);
        			}
        			
        			mPersonCardTitle.setText(cardInfo.getJob());
        			mPersonMobile.setText(cardInfo.getMobile());
        			mPersonTelephone.setText(cardInfo.getPhone());
        			mPersonEmail.setText(cardInfo.getEmail());
        			// mPersonQQ.setText(cardInfo.getQq());
        			// mPersonWorktime.setText(cardInfo.getWorktime());
        			mPersonSoftName.setText(cardInfo.getSoft_name());
        			mPersonSoftNum.setText(cardInfo.getSoft_number());
        			
        			BaseDaoModel model = mAddressDAO.getProvinceById(FileUtils.getDatabaseAddressPath(mActivity), 
        					cardInfo.getAddress_province_ode());
        			if(null != model) {
        				String addressArea = model.getName();
            			mProvinceId = model.getId();
            			model = mAddressDAO.getCityById(FileUtils.getDatabaseAddressPath(mActivity), 
            					cardInfo.getAddress_city_ode());
            			addressArea += model.getName();
            			mCityId = model.getId();
            			mPersonProvince.setText(addressArea);
        			}
        			
        			mPersonAddress.setText(cardInfo.getAddress());
        			
        			WorkTimeInfo timeInfo = cardInfo.getWork_time_edit();
        			
        			mPersonWeekStart.setText(timeInfo.getStart_week());
        			mPersonWeekStop.setText(timeInfo.getStop_week());
        			mPersonTimeStart.setText(timeInfo.getStart_time());
        			mPersonTimeStop.setText(timeInfo.getStop_time());
        			
        			// mPersonTrades.setText(mDetailInfo.getTrades());
        			mSelectedTrades = handleTradeIdToList(cardInfo.getTrade_ids());
        			
        			notifyTradesChanged(mSelectedTrades);
        			
        			PersonDetailInfo info = PreferencesUtility.getUserInfo(mActivity);
        			info.setReal_name(cardInfo.getReal_name());
        			PreferencesUtility.saveUserInfo(mActivity, info);
        			
        		}
        	}
		} else {
			showToast("名片编辑出错啦！");
		}
	}
	private void handleEditCard(String response) {
		Logger.i(TAG + "----response=" +  response);
		// response
		ReturnJsonModel json = JsonParseUtil.parse(response);
        if (null != json) {
        	Logger.i(TAG + "----json=" +  json.getDescription());
			if (json.getIsSuccess()) {
    			PersonDetailInfo info = PreferencesUtility.getUserInfo(mActivity);
    			info.setReal_name(mPersonName.getText().toString());
    			PreferencesUtility.saveUserInfo(mActivity, info);
    			
				showToast("名片编辑成功！");
			} else {
				showToast(json.getDescription());
			}
		} else {
			showToast("名片编辑出错啦！");
		}
	}
}
