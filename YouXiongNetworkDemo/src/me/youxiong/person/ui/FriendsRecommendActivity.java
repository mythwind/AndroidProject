package me.youxiong.person.ui;

import java.util.ArrayList;
import java.util.Random;

import me.youxiong.person.R;
import me.youxiong.person.adapter.AdapterFriendsRecommend;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.kanak.emptylayout.EmptyLayout;

public class FriendsRecommendActivity extends BaseFragmentActivity {

	private EmptyLayout mEmptyLayout;
	private ListView mRecommendListView;
	private AdapterFriendsRecommend mAdapter;
	private ArrayList<String> mArrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.simple_listview);
	}
	
	@Override
	protected void initViewComponent() {
		mRecommendListView = (ListView) findViewById(R.id.simple_listview);
		
	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		mEmptyLayout = new EmptyLayout(this, mRecommendListView);
		mEmptyLayout.setErrorButtonClickListener(mErrorClickListener);
		mEmptyLayout.showLoading();
		/*
		LinearLayout.LayoutParams lp = (LayoutParams) mRecommendListView.getLayoutParams();
		lp.gravity = Gravity.CENTER;
		mRecommendListView.setLayoutParams(lp);
		*/
//		LinearLayout mainBody = (LinearLayout) findViewById(R.id.layout_container);
//		LinearLayout.LayoutParams lp = (LayoutParams) mainBody.getLayoutParams();
//		lp.gravity = Gravity.CENTER;
//		mainBody.setLayoutParams(lp);
		
		mAdapter = new AdapterFriendsRecommend(this);
		mArrayList = new ArrayList<String>();
		mAdapter.setList(mArrayList);
		mRecommendListView.setAdapter(mAdapter);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mHandler.sendEmptyMessage(0);
				
			}
		}).start();
		 
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Random random = new Random();
			int x = random.nextInt(2);
			if(x == 0) {
				for (int i = 0; i < 5; i++) {
					mArrayList.add("测试" + (i + 1));
				}
				mAdapter.notifyDataSetChanged();
			} else {
				mArrayList.clear();
				mEmptyLayout.showEmpty();
			}
		}
	};

	private View.OnClickListener mErrorClickListener = new OnClickListener() {            
        @Override  
        public void onClick(View v) {  
            Toast.makeText(FriendsRecommendActivity.this, "Try again button clicked", Toast.LENGTH_LONG).show();              
        }
    };
    
	@Override
	protected void registerListener() {
		
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
