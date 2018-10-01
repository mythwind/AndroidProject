package me.youxiong.person.ui.fragment;

import me.youxiong.person.R;
import me.youxiong.person.ui.fragment.base.BaseFragment;
import me.youxiong.person.utils.Logger;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 市场设置
 * @author mythwind
 *
 */
public class ProductSettingFragment extends BaseFragment implements OnClickListener {

	private View mView = null;
	private EditText mSearchKeyEditText;
	private TextView mSearchTextView;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindData();
		registeListener();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_product_setting, null);
		mSearchKeyEditText = (EditText) mView.findViewById(R.id.product_setting_et_search_key);
		mSearchTextView = (TextView) mView.findViewById(R.id.product_setting_tv_search);
		return mView;
	}
	private void bindData() {
		
	}
	
	private void registeListener() {
		mSearchTextView.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.product_setting_tv_search:
			String s = mSearchKeyEditText.getText().toString();
			Logger.d("s==" + s);
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
