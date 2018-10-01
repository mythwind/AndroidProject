package me.youxiong.person.ui.fragment;

import me.youxiong.person.R;
import me.youxiong.person.ui.MainActivity;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideSecondFragment extends BaseFragment {
	private static final String EXTRA_RESID = "resId";
	
	private static GuideSecondFragment mInstance;
	private int mResId = 0;
	
	private GuideSecondFragment() {}
	public synchronized static GuideSecondFragment getInstannce(int resId) {
		if(mInstance == null) {
			mInstance = new GuideSecondFragment();
		}
		Bundle bundle = new Bundle();
		bundle.putInt(EXTRA_RESID, resId);
		mInstance.setArguments(bundle);
		return mInstance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mResId = getArguments().getInt(EXTRA_RESID);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_guide_image, null);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_guide);
		View tv = view.findViewById(R.id.tv_into);
		if(mResId > 0) {
			iv.setImageResource(mResId);
		}
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int currVCode = PackageUtil.getAppVersionCode(mActivity);
				PreferencesUtility.setSavedVersionCode(mActivity, currVCode);
				
				mActivity.startActivity(new Intent(mActivity, MainActivity.class));
				mActivity.finish();
				mActivity.overridePendingTransition(R.anim.fade, R.anim.hold);
			}
		});
		
		
		return view;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		bindData();
		registeListener();
	}

	private void bindData() {
	}

	private void registeListener() {
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

}
