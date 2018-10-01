package me.youxiong.person.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import me.youxiong.person.R;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ReturnJsonModel;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.request.MyRequestOperation;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.JsonParseUtil;
import me.youxiong.person.utils.PreferencesUtility;
import me.youxiong.person.utils.config.YXConstants;
import me.youxiong.person.utils.encode.EncryptUtil;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *  名片设置功能
 * @author mythwind
 *
 */
public class CardSettingFragment extends BaseFragment implements OnClickListener {

	private TextView mTvUserLevelLimit;
	private TextView mSettingCommit;
	private RelativeLayout mViewAllowSearch;
	private CheckBox mCbAllowSearch;
	// private RadioGroup mCardTypeGroup;

	private String[] mUserLevels;
	private int mCurrUserLevel = 1;
	private int mChoosedLevel = 1;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindData();
		registeListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_card_setting, null);
		
		// mCardTypeGroup = (RadioGroup) mView.findViewById(R.id.card_type_group);
		mTvUserLevelLimit = (TextView) root.findViewById(R.id.tv_card_setting_level_limit);
		mViewAllowSearch = (RelativeLayout) root.findViewById(R.id.view_allow_search);
		mCbAllowSearch = (CheckBox) root.findViewById(R.id.cb_card_setting_allow);
		mSettingCommit = (TextView) root.findViewById(R.id.card_setting_commit);
		return root;
	}
	
	private void bindData() {
		mUserLevels = getResources().getStringArray(R.array.user_level_info);
		
		PersonDetailInfo info = PreferencesUtility.getUserInfo(mActivity);
		mCurrUserLevel = info.getUser_level();
		int checklevel = PreferencesUtility.getCardCheckLevel(mActivity);
		updateCheckLevel(checklevel);
	}
	private void updateCheckLevel(int checklevel) {
		checklevel = checklevel <= 0 ? 1 : checklevel;
		Spanned str = Html.fromHtml(getResources().getString(R.string.card_setting_check_level, 
				mUserLevels[(mCurrUserLevel - 1) % mUserLevels.length], mUserLevels[(checklevel - 1) % mUserLevels.length]));
		mTvUserLevelLimit.setText(str);
	}
	private void registeListener() {
		// mCardSearchGroup.setOnCheckedChangeListener(listener);
		// mCardTypeGroup.setOnCheckedChangeListener(listener);
		mTvUserLevelLimit.setOnClickListener(this);
		mViewAllowSearch.setOnClickListener(this);
		mSettingCommit.setOnClickListener(this);
	}
	
//	private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
//		@Override
//		public void onCheckedChanged(RadioGroup group, int checkedId) {
//			switch (group.getId()) {
//			case R.id.card_type_group:
//				groupTypeChanged(checkedId);
//				break;
//			default:
//				break;
//			}
//		}
//	};
//
//	private void groupTypeChanged(int checkedId) {
//		switch (checkedId) {
//		case R.id.card_personal:
//			
//			break;
//		case R.id.card_business:
//			
//			break;
//		default:
//			break;
//		}
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_card_setting_level_limit:
			showSelectLevelDialog();
			break;
		case R.id.view_allow_search:
			showSearchDialog();
			break;
		case R.id.card_setting_commit:
			launchRequestCommit();
			break;
		default:
			break;
		}
	}

	private void showSelectLevelDialog() {
		String[] dstLecels = new String[mCurrUserLevel];
		System.arraycopy(mUserLevels, 0, dstLecels, 0, mCurrUserLevel);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("选择用户查看等级");
        //    设置一个下拉的列表选择项
		builder.setItems(dstLecels, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mChoosedLevel = which + 1;
				showToast("你选择的城市为：" + mUserLevels[which]);
				updateCheckLevel(mChoosedLevel);
			}
		});
        builder.show();
	}
	private void showSearchDialog() {
		View layout = View.inflate(mActivity, R.layout.dialog_card_allow_search, null);
		// 2. 新建对话框对象
		final Dialog dialog = new AlertDialog.Builder(mActivity).create();
		dialog.show();
		dialog.getWindow().setContentView(layout);
        
		TextView tvTitle = (TextView) layout.findViewById(R.id.dialog_tv_title);
		TextView tvMsg = (TextView) layout.findViewById(R.id.dialog_tv_message);
		TextView tvConfirm = (TextView) layout.findViewById(R.id.dialog_tv_confirm);
		TextView tvCancel = (TextView) layout.findViewById(R.id.dialog_tv_cancel);
		tvTitle.setText(R.string.prompt);
		tvMsg.setText(R.string.hint_card_setting_promp);
		tvCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		tvConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// 调转到开通 vip 设置界面
				
			}
		});
	}
	
	private void launchRequestCommit() {
		MyRequestOperation operation = new MyRequestOperation(MyRequestFactory.REQUEST_TYPE_CARD_SETTING, getRequestParams());
		launchRequest(MyRequestFactory.execute(operation, this));
	}
	private Map<String, String> getRequestParams() {
		SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		String username = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_USERNAME), "");
		String password = mPreferences.getString(EncryptUtil.encryptMD5(YXConstants.PREFS_PASSWORD), "");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(YXConstants.REQUEST_TOKEN_KEY, username.trim());
		params.put(YXConstants.REQUEST_TOKEN_SECRET, password.trim());
		params.put(YXConstants.REQUEST_EQUIPMENT_TYPE, YXConstants.EQUIPMENT_TYPE_ANDROID);
		params.put("AccessLevel", String.valueOf(mChoosedLevel));
		params.put("IsSearch", mCbAllowSearch.isChecked() ? "1" : "0");
		return params;
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		if(TextUtils.isEmpty(response)) {
			Log.e(getTag(), " ******** response is null ! result error!");
			return ;
		}
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_CARD_SETTING:
			ReturnJsonModel json = JsonParseUtil.parse(response);
			if(null != json) {
				if(json.getIsSuccess()) {
					showToast(R.string.card_setting_success);
				} else {
					showToast(json.getDescription());
				}
			} else {
				showToast(R.string.card_setting_failed);
			}
			break;
		default:
			break;
		}
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
