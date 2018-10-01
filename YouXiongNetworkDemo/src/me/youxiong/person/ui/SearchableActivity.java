package me.youxiong.person.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.BaseListViewAdapter;
import me.youxiong.person.cache.CacheItem;
import me.youxiong.person.cache.RelateViewItem;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.NetworkUtil;
import me.youxiong.person.utils.PreferencesUtility;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchableActivity extends BaseFragmentActivity implements OnClickListener {
	public static final String EXTRA_DATA_KEYWORDS = "keywords";
	
	private EditText mSearchEditText;
	private ListView mRelateListView;
	private ImageView mClearImage;
	
	private TextView mCancel;
//	private View mRootVIew;

	private BaseListViewAdapter mAdapter;
	private ArrayList<CacheItem> mCacheList;

	private String mQueryKeywords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		hideTitleLayout(true);
		appendMainBody(R.layout.layout_search_result);
		
	}
	
	@Override
	protected void initViewComponent() {
		mSearchEditText = (EditText) findViewById(R.id.searchView);
		mRelateListView = (ListView) findViewById(R.id.relateList);
		mClearImage = (ImageView) findViewById(R.id.clear);
		
		// mRootVIew = findViewById(R.id.view);
		mCancel = (TextView) findViewById(R.id.cancelTV);
	}

	@Override
	protected void bindData() {
		mClearImage.setVisibility(View.GONE);
		
		mCacheList = new ArrayList<CacheItem>();
	    mAdapter = new BaseListViewAdapter(mCacheList, mRelateListView);
	    mRelateListView.setAdapter(mAdapter);
	    
	    LinkedHashMap<String, Long> hashMap = PreferencesUtility.getSearchKeywords(this);
	    if(hashMap != null) {
	    	showRelateSearch(true);
	    	Iterator<String> iter = hashMap.keySet().iterator();
	    	RelateViewItem locale;
	    	while (iter.hasNext()) {
	    		String key = iter.next(); 
	    	    // Long val = hashMap.get(key); 
	    	    locale = new RelateViewItem(this);
	    	    locale.keyName = key;
				locale.query = key;
	    	    mCacheList.add(locale);
	    	} 
	    }
	    // 反序排列
	    Collections.reverse(mCacheList);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void registerListener() {
		mSearchEditText.addTextChangedListener(mWatcher);
		mSearchEditText.setOnEditorActionListener(mEditorActionListener);
		mRelateListView.setOnItemClickListener(mItemClickListener);
		
		mCancel.setOnClickListener(this);
		mClearImage.setOnClickListener(this);
		
	}
	
	private void handleIntent(Intent intent) {
		if (!NetworkUtil.isNetworkAvailable(SearchableActivity.this)) {
			showToast(R.string.network_unavailable);
		} else {
			mQueryKeywords = intent.getStringExtra("query");
    	}
	}
	
	private void showRelateSearch(boolean show) {
		if (show) {
			mRelateListView.setVisibility(View.VISIBLE);
		} else {
			mRelateListView.setVisibility(View.GONE);
		}
	}

	private void addListFirst() {
		mCacheList.clear();
		if (!TextUtils.isEmpty(mQueryKeywords)) {
			RelateViewItem locale = new RelateViewItem(this);
			locale.keyName = mQueryKeywords;
			locale.query = mQueryKeywords;
			mCacheList.add(locale);
			mAdapter.notifyDataSetChanged();
		} else {
			showRelateSearch(false);
		}
	}
	private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEARCH
					|| actionId == EditorInfo.IME_ACTION_DONE) {
				mSearchEditText.requestFocus();
				
				CommonUtils.hideSoftInputFromWindow(SearchableActivity.this, mSearchEditText);
				
				if (!NetworkUtil.isNetworkAvailable(SearchableActivity.this)) {
					showToast(R.string.network_unavailable);
					return true;
				} else {
					if(!TextUtils.isEmpty(mQueryKeywords)){
						turnToSearchResult(mQueryKeywords);
                	}
				}
				return true;
			}
			return false;
		}
	};
	
	private void turnToSearchResult(String name) {
		PreferencesUtility.saveSearchKeywords(this, System.currentTimeMillis(), name);
		Intent itt = new Intent(this, SearchResultActivity.class);
		itt.putExtra(SearchResultActivity.EXTRA_NAME, name);
		startActivity(itt);
		finish();
		overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
	}
	private TextWatcher mWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			 Intent intent = getIntent();
			 mQueryKeywords = s.toString().trim();
		        if (!TextUtils.isEmpty(s.toString().trim())) {
		        	mClearImage.setVisibility(View.VISIBLE);
		        	showRelateSearch(true);
		        	addListFirst();
		        	intent.putExtra("query", mQueryKeywords);
					handleIntent(intent);
		        } else {
			        mClearImage.setVisibility(View.GONE);
					mCacheList.clear();
					mAdapter.notifyDataSetChanged();
					showRelateSearch(false);
		        }
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	
	
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			RelateViewItem item = (RelateViewItem) mCacheList.get(position);
			turnToSearchResult(item.keyName);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelTV:
			finish();
			break;
		case R.id.clear:
			mSearchEditText.setText(null);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
	}
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}
}
