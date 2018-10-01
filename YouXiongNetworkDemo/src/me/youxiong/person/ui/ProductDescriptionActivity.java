package me.youxiong.person.ui;

import me.youxiong.person.R;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.Logger;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ProductDescriptionActivity extends BaseFragmentActivity implements OnClickListener {

	private EditText mDescriptionEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_product_description);
		
	}
	
	@Override
	protected void initViewComponent() {
		mDescriptionEditText = (EditText) findViewById(R.id.et_product_desc);
		
	}

	@Override
	protected void bindData() {
		setTileText(getIntent().getStringExtra("title"));
		mTextOption.setText(R.string.complete);
		mTextOption.setVisibility(View.VISIBLE);
		
		String content = getIntent().getStringExtra("content");
		if(!TextUtils.isEmpty(content)) {
			mDescriptionEditText.setText(content);
		}
		
	}

	@Override
	protected void registerListener() {
		mTextOption.setOnClickListener(this);
		setBackViewListener(mBackViewListener);
		
	}

	@Override
	public void onBackPressed() {
		
		Logger.d("--------ProductDescriptionActivity-----onBackPressed----");
		Intent data = new Intent();
		data.putExtra("content_result", mDescriptionEditText.getText().toString());
		setResult(RESULT_OK, data);
		
		super.onBackPressed();
		
	}
	private OnClickListener mBackViewListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			exitActivity();
		}
	};
	
	@Override
	public void onClick(View v) {
		if(v == mTextOption) {
			Intent data = new Intent();
			data.putExtra("content_result", mDescriptionEditText.getText().toString());
			setResult(RESULT_OK, data);
			
			exitActivity();
		}
	}
	
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}

	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

}
