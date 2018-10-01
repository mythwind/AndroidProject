package me.youxiong.person.ui.fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterProductDeduction;
import me.youxiong.person.dialog.EditTextViewDialog;
import me.youxiong.person.model.DeductionLogResult;
import me.youxiong.person.model.PersionBriefInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.PersonalInfoActivity;
import me.youxiong.person.ui.fragment.base.LazyLoadFragment;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.DateUtility;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.NetworkUtil;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import me.youxiong.person.view.HorizontalListView;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 *  扣分统计
 * @author mythwind
 *
 */
public class ProductDeductionFragment extends LazyLoadFragment implements OnClickListener {

	private View mView;
	private TextView mTvTimeStart;
	private TextView mTvTimeEnd;
	private TextView mTvKeywords;
	private TextView mConfirmButton;
	private HorizontalListView mHorizontalListView;
	
	private TextView mCurrentDay;
	private TextView mCurrentWeek;
	private TextView mCurrentMonth;
	private TextView mDeductionTotal;
	private TextView mTvEmptyResult;
	private LinearLayout mLayoutSearchView;
	
	private Calendar mCalendar;
	private int mStartYear, mEndYear;
	private int mStartMonth, mEndMonth;
	private int mStartDay, mEndDay;
	private String mDateString;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindData();
		registeListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_product_deduction, null);
		
		mLayoutSearchView = (LinearLayout) mView.findViewById(R.id.deduction_ll_root);
		mCurrentDay = (TextView) mView.findViewById(R.id.deduction_tv_current_day);
		mCurrentWeek = (TextView) mView.findViewById(R.id.deduction_tv_current_week);
		mCurrentMonth = (TextView) mView.findViewById(R.id.deduction_tv_current_month);
		mDeductionTotal = (TextView) mView.findViewById(R.id.deduction_tv_total);
		mTvTimeStart = (TextView) mView.findViewById(R.id.deduction_tv_time_start);
		mTvTimeEnd = (TextView) mView.findViewById(R.id.deduction_tv_time_end);
		mTvKeywords = (TextView) mView.findViewById(R.id.deduction_ed_keywords);
		mConfirmButton = (TextView) mView.findViewById(R.id.confirm);
		mHorizontalListView = (HorizontalListView) mView.findViewById(R.id.deduction_lv_horizontal);
		mTvEmptyResult = (TextView) mView.findViewById(R.id.deduction_tv_empty);
		return mView;
	}
	
	private void bindData() {
		mCalendar = Calendar.getInstance();
		mStartYear = mEndYear = mCalendar.get(Calendar.YEAR);
		mStartMonth = mEndMonth = mCalendar.get(Calendar.MONTH);
		mStartDay = 1;// 每个月的第一天
		mEndDay = mCalendar.get(Calendar.DAY_OF_MONTH);
		mDateString = mStartYear + "-" + DateUtility.completeDate(mStartMonth + 1) + "-" + DateUtility.completeDate(mStartDay);
		mTvTimeStart.setText(DateUtility.getFirstDayOfMonth());
		mTvTimeEnd.setText(DateUtility.formatStrDate(new Date(), "yyyy-MM-dd"));
		
	}

	private void registeListener() {
		mConfirmButton.setOnClickListener(this);
		if(null != mView) {
			mView.findViewById(R.id.view_deduction_time_start).setOnClickListener(this);
			mView.findViewById(R.id.view_deduction_time_end).setOnClickListener(this);
			mView.findViewById(R.id.view_deduction_keywords).setOnClickListener(this);
		}
	}

	@Override
	protected void firstVisiable() {
		// 首次进入，会展示当月所有的搜索记录，如果没有，则返回空
		launchRequestLog(true);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			// 发送检索请求
			launchRequestLog(false);
			break;
		case R.id.view_deduction_time_start:
			new DatePickerDialog(mActivity, mStartListener, mStartYear, mStartMonth, mStartDay).show();
			break;
		case R.id.view_deduction_time_end:
			new DatePickerDialog(mActivity, mEndListener, mEndYear, mEndMonth, mEndDay).show();
			break;
		case R.id.view_deduction_keywords:
			// 弹出编辑框输入
			EditTextViewDialog dialog = new EditTextViewDialog(mActivity, getString(R.string.keywords), mTvKeywords);
			dialog.setCanBeNull(true);
			dialog.show();
			break;
		default:
			break;
		}
	}
	
	/**
	 *  起始日期的弹出框
	 */
	private DatePickerDialog.OnDateSetListener mStartListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mStartYear = year;
			mStartMonth = monthOfYear;
			mStartDay = dayOfMonth;
			mDateString = mStartYear + "-" + DateUtility.completeDate(mStartMonth + 1) + "-" + DateUtility.completeDate(dayOfMonth);
			mTvTimeStart.setText(mDateString);
		}
	};
	/**
	 *  终止日期的弹出框
	 */
	private DatePickerDialog.OnDateSetListener mEndListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			mEndYear = year;
			mEndMonth = monthOfYear;
			mEndDay = dayOfMonth;
			String temp = mEndYear + "-" + DateUtility.completeDate(mEndMonth + 1) + "-" + DateUtility.completeDate(mEndDay);
			if (!DateUtility.formatDate(mDateString).after(DateUtility.formatDate(temp))) {
				mTvTimeEnd.setText(temp);
			} else {
				showToast(R.string.date_interval_prompt);
				//  filterStop.setText(dateString);
			}
		}
	};
	private void launchRequestLog(boolean first) {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_KEYWORD_INTEGRAL_LOG, getRequestParams(first));
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	
	private Map<String, String> getRequestParams(boolean first) {
		Map<String, String> params = new HashMap<String, String>();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		String strUserName = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String strPassword = preferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		params.put(YXConstants.REQUEST_TOKEN_KEY, strUserName);
		params.put(YXConstants.REQUEST_TOKEN_SECRET, strPassword);
		params.put(YXConstants.REQUEST_IMSI, PackageUtil.getDeviceImsi(mActivity));
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		String keyword = mTvKeywords.getText().toString();
		if(null == keyword) {
			keyword = "";
		}
		if(first) {
			params.put("Keyword", "");
			params.put("StartDate", "1970-01-01");
			params.put("StopDate", mTvTimeEnd.getText().toString());
		} else {
			params.put("Keyword", keyword);
			params.put("StartDate", mTvTimeStart.getText().toString());
			params.put("StopDate", mTvTimeEnd.getText().toString());
		}
		params.put("Page", "1");
		params.put("PerPage", "15");
		return params;
	}
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_KEYWORD_INTEGRAL_LOG:
			Logger.d("Deduction----onRequestSuccessed.response=" +  response);
			if(TextUtils.isEmpty(response)) {
				mTvEmptyResult.setVisibility(View.VISIBLE);
				mLayoutSearchView.setVisibility(View.GONE);
				mTvEmptyResult.setText(R.string.hint_deduction_empty);
				return ;
			}
			ReturnJsonModel json = JsonParseUtil.parse(response);
			if(null != json) {
				if(json.getIsSuccess() && !TextUtils.isEmpty(json.getReturnValue())) {
					DeductionLogResult dedLog = JsonParseUtil.getDeductionIntegralLog(json.getReturnValue());
					handleDeductionLog(dedLog);
				} else {
					mTvEmptyResult.setVisibility(View.VISIBLE);
					mLayoutSearchView.setVisibility(View.GONE);
					mTvEmptyResult.setText(R.string.hint_deduction_empty);
				}
			} else {
				mTvEmptyResult.setVisibility(View.VISIBLE);
				mLayoutSearchView.setVisibility(View.GONE);
				mTvEmptyResult.setText(R.string.hint_deduction_empty);
			}
			break;
		default:
			break;
		}
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_KEYWORD_INTEGRAL_LOG:
			mTvEmptyResult.setVisibility(View.VISIBLE);
			if(NetworkUtil.isNetworkAvailable(mActivity)) {
				mTvEmptyResult.setText(R.string.network_server_error);
			} else {
				mTvEmptyResult.setText(R.string.network_timeout);
			}
			break;
		}
	}
	
	private void handleDeductionLog(DeductionLogResult dedLog) {
		if(null == dedLog.keyword_integral_log_list || dedLog.keyword_integral_log_list.size() == 0) {
			// showToast("暂无数据，请更换搜索关键字或者调整搜所时间");
			mTvEmptyResult.setVisibility(View.VISIBLE);
			mLayoutSearchView.setVisibility(View.GONE);
			mTvEmptyResult.setText(R.string.hint_deduction_empty);
			return;
		}
		mLayoutSearchView.setVisibility(View.VISIBLE);
		mCurrentDay.setText(dedLog.sum_day_integral);
		mCurrentWeek.setText(dedLog.sum_week_integral);
		mCurrentMonth.setText(dedLog.sum_month_integral);
		mDeductionTotal.setText(dedLog.sum_integral);
		
		LayoutParams lp = (LayoutParams) mHorizontalListView.getLayoutParams();
		lp.height = CommonUtils.dpToPx(getResources(), 200);
		// lp.height = ScreenUtil.getScreenSize(_instance).x / 4 - ScreenUtil.dpToPx(_instance.getResources(), 15);
		mHorizontalListView.setLayoutParams(lp);
		
		final AdapterProductDeduction adapter = new AdapterProductDeduction(mActivity, dedLog.keyword_integral_log_list);
		mHorizontalListView.setAdapter(adapter);
		mHorizontalListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showToast("点击了位置" + position);
				PersionBriefInfo info = (PersionBriefInfo) adapter.getItem(position);
				Logger.e("--------" + position + ",, userID=" + info.getUser_id());
				Intent intent = new Intent(mActivity, PersonalInfoActivity.class);
				intent.putExtra("info", info);
				intent.putExtra("islogin", true);
				startActivity(intent);
			}
		});
		
	}

}
