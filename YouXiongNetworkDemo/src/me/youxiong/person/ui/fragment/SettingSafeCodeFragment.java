package me.youxiong.person.ui.fragment;

import me.youxiong.person.R;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class SettingSafeCodeFragment extends BaseFragment implements OnClickListener {
	private static SettingSafeCodeFragment mInstance;
	private EditText mEditText;
	private TextView mCommitTextView;
	
	private SettingSafeCodeFragment() {}
	public synchronized static SettingSafeCodeFragment getInstannce() {
		if(mInstance == null) {
			mInstance = new SettingSafeCodeFragment();
		}
		return mInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View  view = inflater.inflate(R.layout.fragment_setting_safecode, null);
		mEditText = (EditText) view.findViewById(R.id.setting_et_safecode_edittext);
		mCommitTextView = (TextView) view.findViewById(R.id.setting_tv_safecode_commit);
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
		mCommitTextView.setOnClickListener(this);

	}
	
	private boolean validate() {
		String strSafeCode = mEditText.getText().toString();
		
		if (TextUtils.isEmpty(strSafeCode)) {
			
		}
		return true;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_tv_safecode_commit:
			if(validate()) {
				
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

}
