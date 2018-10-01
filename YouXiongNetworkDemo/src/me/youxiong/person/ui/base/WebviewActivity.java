package me.youxiong.person.ui.base;

import com.umeng.analytics.MobclickAgent;

import me.youxiong.person.R;
import me.youxiong.person.utils.config.YXConstants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * 展示web页面
 * @author mythwind
 */
public class WebviewActivity extends BaseFragmentActivity {

	private WebView mWebView;
	private LinearLayout mLoadingView;
	@SuppressWarnings("unused")
	private String url;
//	private String webUrl;
	private String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appendMainBody(R.layout.activity_webview);
		
	}
	@Override
	protected void initViewComponent() {
		mWebView = (WebView) findViewById(R.id.webView);
		mLoadingView = (LinearLayout) findViewById(R.id.webview_loading);
		initWebView();
		
	}

	@Override
	protected void bindData() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			url = bundle.getString("url");
			title = bundle.getString("title");
		}
		setTileText(TextUtils.isEmpty(title) ? getString(R.string.agreement_title) : title);
		
	}

	@Override
	protected void registerListener() {
		
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mLoadingView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mLoadingView.setVisibility(View.GONE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				showToast(R.string.network_unavailable);
			}
		});
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setVerticalScrollBarEnabled(false);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(false);
		settings.setUseWideViewPort(true);//關鍵點
		settings.setLoadWithOverviewMode(true);
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		// mWebView.loadUrl(TextUtils.isEmpty(webUrl) ? Constants.WEB_URL : webUrl);
		
		mWebView.loadUrl("file:///android_asset/html/agreement.html");
		
	}
	
	public static void startActivity(Context context, String url, String title) {
		Bundle bundle = new Bundle();
		bundle.putString("url", url);
		bundle.putString("title", title);
		Intent intent = new Intent(context, WebviewActivity.class);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(YXConstants.UMENG_ENABLE) {
			//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
			MobclickAgent.onPageEnd(WebviewActivity.class.getSimpleName());
			MobclickAgent.onPause(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(YXConstants.UMENG_ENABLE) {
			//SDK已经禁用了基于Activity 的页面统计，所以需要再次重新统计页面
			MobclickAgent.onPageStart(WebviewActivity.class.getSimpleName());
			MobclickAgent.onResume(this);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWebView != null) {
			mWebView.destroy();
		}
	}
	@Override
	protected void onRequestSuccessed(int requestType, String response) {
	}
	@Override
	protected void onRequestFailed(int requestType, String errMsg) {
	}

}