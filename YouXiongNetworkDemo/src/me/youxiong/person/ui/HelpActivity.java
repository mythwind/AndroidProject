package me.youxiong.person.ui;

import android.os.Bundle;
import me.youxiong.person.R;
import me.youxiong.person.ui.base.BaseFragmentActivity;

public class HelpActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_help);
		
	}
	
	@Override
	protected void initViewComponent() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		
	}

	@Override
	protected void registerListener() {
		
	}

	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		// TODO Auto-generated method stub

	}

}
