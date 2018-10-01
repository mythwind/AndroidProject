package me.youxiong.person.ui;

import me.youxiong.person.R;
import me.youxiong.person.request.MyRequestFactory;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 *  分享
 *  
 * @author mythwind
 */
public class ShareActivity extends BaseFragmentActivity implements OnClickListener {
	
	private TextView mTvWeixinFriend;
	private TextView mTvWeixinCircle;
	private TextView mTvQQFriend;
	private TextView mTvQQZone;
	private TextView mTvSinaWeibo;
	private TextView mTvEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_share);
		
	}
	
	@Override
	protected void initViewComponent() {
		mTvWeixinFriend = (TextView) findViewById(R.id.tv_share_weixin_friend);
		mTvWeixinCircle = (TextView) findViewById(R.id.tv_share_weixin_circle);
		mTvQQFriend = (TextView) findViewById(R.id.tv_share_qq_friend);
		mTvQQZone = (TextView) findViewById(R.id.tv_share_qq_zone);
		mTvSinaWeibo = (TextView) findViewById(R.id.tv_share_sina_weibo);
		mTvEmail = (TextView) findViewById(R.id.tv_share_link);
		
	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		
	}

	@Override
	protected void registerListener() {
		mTvWeixinFriend.setOnClickListener(this);
		mTvWeixinCircle.setOnClickListener(this);
		mTvQQFriend.setOnClickListener(this);
		mTvQQZone.setOnClickListener(this);
		mTvSinaWeibo.setOnClickListener(this);
		mTvEmail.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_INTEGRAL:
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
		switch (requestType) {
		case MyRequestFactory.REQUEST_TYPE_GET_INTEGRAL:
			
			break;
		default:
			break;
		}
	}

}
