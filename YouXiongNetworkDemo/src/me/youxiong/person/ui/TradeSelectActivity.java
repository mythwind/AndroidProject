package me.youxiong.person.ui;

import java.util.ArrayList;
import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.database.TradeDAO;
import me.youxiong.person.database.impl.TradeDAOImpl;
import me.youxiong.person.model.BaseDaoModel;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.ui.fragment.TradeContentFragment;
import me.youxiong.person.ui.fragment.TradeHeadFragment;
import me.youxiong.person.utils.FileUtils;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.config.YXConstants;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 选择职业
 * @author mythwind
 */
public class TradeSelectActivity extends BaseFragmentActivity {
	private static final String TAG = TradeSelectActivity.class.getSimpleName();
	private static final int MSG_LOAD_FINISHED = 0x2015;
	
	private TextView mChoicedNumView;
	private ImageView mIconBoultView;
	private View mHeadView;
	private TradeHeadFragment mHeadFragment;
	private TradeContentFragment mContentFragment;

	private TradeDAO mTradeDAO;
	private List<BaseDaoModel> mTradeList;
	private List<BaseDaoModel> mSelectedList;
	
	private ProgressDialog mProgressDialog;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOAD_FINISHED:
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				addFragments();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSelectedList = getIntent().getParcelableArrayListExtra("SelectedTrades");
		
		appendMainBody(R.layout.activity_trade_select);

	}

	@Override
	protected void initViewComponent() {
		mChoicedNumView = (TextView) findViewById(R.id.trade_num_choiced);
		mIconBoultView = (ImageView) findViewById(R.id.trade_icon_boult);

	}

	@Override
	protected void bindData() {
		setTileText(getString(R.string.trade_title));

		mTradeDAO = new TradeDAOImpl();

		mProgressDialog = ProgressDialog.show(this, "", "正在卖力加载...", true, true);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<BaseDaoModel> tmpTradeList = mTradeDAO.getTradeList(FileUtils.getDatabaseTradePath(TradeSelectActivity.this));
				if(null != tmpTradeList && tmpTradeList.size() > 0) {
					for (BaseDaoModel parent : tmpTradeList) {
						List<BaseDaoModel> tmpChildTradeList = parent.getSubList();
						if(null == tmpChildTradeList || tmpChildTradeList.size() == 0
								|| null == mSelectedList || mSelectedList.size() == 0)
							return;
						for (int i = 0; i < tmpChildTradeList.size() && mSelectedList.size() < YXConstants.LIMIT_SELECT_TRADE_NUMBER; i++) {
							BaseDaoModel child = tmpChildTradeList.get(i);
							Logger.d(TAG + "---child.id" + child.getId());
							for (BaseDaoModel childSelected : mSelectedList) {
								Logger.d(TAG + "|||||---childSelected.id" + childSelected.getId());
								
								if(child.getId() == childSelected.getId()) {
									child.setChecked(true);
									break;
								}
							}
						}
					}
				}
				mTradeList = tmpTradeList;
				mHandler.sendEmptyMessage(MSG_LOAD_FINISHED);
			}
		}).start();
	}

	@Override
	protected void registerListener() {
		setBackViewListener(mBackViewListener);
	}

	private void addFragments() {
		mHeadFragment = TradeHeadFragment.getInstance(mSelectedList);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.popshow_anim, R.anim.pophidden_anim);
		transaction.replace(R.id.trade_choiced_content, mHeadFragment).commitAllowingStateLoss();
		
		mContentFragment = TradeContentFragment.getInstance(mTradeList);
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.trade_condition_content, mContentFragment).commitAllowingStateLoss();
		
		mHeadView = findViewById(R.id.trade_choiced_view);
		setChoiceNumber();
		addShowHideListener(mHeadFragment);
		hideHead();
	}

	private void addShowHideListener(final Fragment fragment) {
		mHeadView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
				if (fragment.isHidden()) {
					transaction.show(fragment);
					mIconBoultView.setImageResource(R.drawable.ico_sanjiao_up);
				} else {
					transaction.hide(fragment);
					mIconBoultView.setImageResource(R.drawable.ico_sanjiao_down);
				}
				transaction.commit();
			}
		});
	}

	public void hideHead() {
		FragmentManager localFragmentManager = getSupportFragmentManager();
		if ((mHeadFragment == null) || (mHeadFragment.isHidden()))
			return;
		FragmentTransaction localFragmentTransaction = localFragmentManager.beginTransaction();
//		localFragmentTransaction.setCustomAnimations(17432576, 17432577);
		localFragmentTransaction.hide(mHeadFragment);
		localFragmentTransaction.commitAllowingStateLoss();
		mIconBoultView.setImageResource(R.drawable.ico_sanjiao_down);
	}

	/**
	 * @param object
	 * @param addOrRemove   if true add object, or otherwise
	 */
	public void noticeHeadChanged(BaseDaoModel object, boolean addOrRemove) {
		if (mHeadFragment != null) {
			mHeadFragment.noticeHeadChanged(object, addOrRemove);
		}
	}

	public void noticeListChanged(BaseDaoModel object) {
		if (mContentFragment != null) {
			mSelectedList.remove(object);
			mContentFragment.noticeListChanged(object);
		}
	}

	public void addSelectedItem(BaseDaoModel object) {
		if(!mSelectedList.contains(object)) {
			mSelectedList.remove(object);
			mSelectedList.add(object);
			noticeHeadChanged(object, true);
			setChoiceNumber();
		}
	}
	public void removeSelectedItem(BaseDaoModel object) {
		if(mSelectedList.contains(object)) {
			mSelectedList.remove(object);
			noticeHeadChanged(object, false);
			setChoiceNumber();
		}
	}
	
	public void setChoiceNumber() {
		int selectedCount = mSelectedList.size();
		mChoicedNumView.setText(selectedCount + "/" + YXConstants.LIMIT_SELECT_TRADE_NUMBER);
	}
	
	public List<BaseDaoModel> getSelectedList() {
		return mSelectedList;
	}
	
	
	@Override
	public void onBackPressed() {
		
		Logger.d("--------TradeSeectedActiviy-----onBackPressed----");
		Intent data = new Intent();
		data.putParcelableArrayListExtra("selectedList", (ArrayList<? extends Parcelable>) mSelectedList);
		setResult(RESULT_OK, data);
		
		super.onBackPressed();
		
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
	
	private OnClickListener mBackViewListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Logger.d("--------TradeSeectedActiviy-----mBackViewListener----");
			
			Intent data = new Intent();
			data.putParcelableArrayListExtra("selectedList", (ArrayList<? extends Parcelable>) mSelectedList);
			setResult(RESULT_OK, data);
			
			exitActivity();
		}
	};


	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

}
