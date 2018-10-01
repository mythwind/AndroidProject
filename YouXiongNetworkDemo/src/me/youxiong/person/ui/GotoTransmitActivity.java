package me.youxiong.person.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import me.youxiong.person.R;
import me.youxiong.person.ui.base.BaseFragmentActivity;

public class GotoTransmitActivity extends BaseFragmentActivity implements OnClickListener {

	private TextView mFriendView;
	private TextView mMarketView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_goto_transmit);
		
	}
	
	@Override
	protected void initViewComponent() {
		mFriendView = (TextView) findViewById(R.id.goto_transmit_friend);
		mMarketView = (TextView) findViewById(R.id.goto_transmit_market);
		
	}

	@Override
	protected void bindData() {
		setTileText(getString(R.string.card_transmit));
		
	}

	@Override
	protected void registerListener() {
		mFriendView.setOnClickListener(this);
		mMarketView.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_transmit_friend: {
			Intent intent = new Intent();
			intent.setClass(GotoTransmitActivity.this, SelectFriendsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.goto_transmit_market: {
			Intent intent = new Intent();
			intent.setClass(GotoTransmitActivity.this, SelectFriendsActivity.class);
			startActivity(intent);
			break;
		}
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
