package me.youxiong.person.ui.fragment;

import me.youxiong.person.R;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideFirstFragment extends BaseFragment {
	private static final String EXTRA_RESID = "resId";
	
	private static GuideFirstFragment mInstance;
	private int mResId = 0;
	
	private GuideFirstFragment() {}
	
	public synchronized static GuideFirstFragment getInstannce(int resId) {
		if(mInstance == null) {
			mInstance = new GuideFirstFragment();
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
		TextView tv = (TextView) view.findViewById(R.id.tv_into);
		tv.setVisibility(View.GONE);
		if(mResId > 0) {
			iv.setImageResource(mResId);
		}
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
