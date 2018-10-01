package me.youxiong.person.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import me.youxiong.person.R;
import me.youxiong.person.ui.base.BaseFragmentActivity;
import me.youxiong.person.utils.LoadImageUtils;

public class PersonProductPhotoActivity extends BaseFragmentActivity {
	
	private ImageView mImageView;
	
	public static final String EXTRA_ICON_URL = "icon_url";
	public static void startPhotoActivity(Activity ac, String iconUrl) {
		Intent itt = new Intent(ac, PersonProductPhotoActivity.class);
		itt.putExtra(EXTRA_ICON_URL, iconUrl);
		ac.startActivity(itt);
		ac.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.simple_imageview);
		hideTitleLayout(true);
		setBackAnimEnable(false);
		
	}
	
	@Override
	protected void initViewComponent() {
		mImageView = (ImageView) findViewById(R.id.simple_imageview);
	}

	@Override
	protected void bindData() {
		String iconUrl = getIntent().getStringExtra(EXTRA_ICON_URL);
		if (!TextUtils.isEmpty(iconUrl)) {
			LoadImageUtils.displayImageNoDefault(iconUrl, mImageView);
		}
	}

	/**
	* 从服务器取图片
	*http://bbs.3gstdy.com
	* @param url
	* @return
	*/
	public Bitmap getHttpBitmap(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		Bitmap bitmap = null;
		InputStream inputStream = null;
		try {
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
			conn.connect();
			inputStream = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
			} catch (IOException e2) {
			}
		}
		return bitmap;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
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
