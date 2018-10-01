package me.youxiong.person.share;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import me.youxiong.person.R;
import me.youxiong.person.model.ShareInfo;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.config.YXConstants;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * 分享
 * @author QY
 *
 */
public class ShareDialogFragment extends DialogFragment {
	
	public static final String KEY_SHAREINFO = "key_shareinfo";
	
	boolean hasWechat = false;
	boolean hasWebo = false;
	boolean hasQQ = false;
	boolean isAdd = false;
	
	private ShareInfo info;
	@SuppressWarnings("unused")
	private Bitmap capture;
	@SuppressWarnings("unused")
	private Uri uri;
	
	private String KEY_ICON = "icon";
	private String KEY_TITLE = "title";
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
	private HashMap<String, Object> item = new HashMap<String, Object>();
	
	public static final String TAG = ShareDialogFragment.class.getSimpleName();
	
	public static ShareDialogFragment newInstance(ShareInfo info) {
		ShareDialogFragment fragment = new ShareDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_SHAREINFO, info);
		Logger.e(TAG + "---->info= " + info.desc);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Activity activity = getActivity();
		Dialog dialog = new Dialog(activity, R.style.TransparentDialog);
		dialog.setContentView(R.layout.share_layout);
		ListView listView = (ListView) dialog.findViewById(R.id.list);
		
		Bundle bundle = getArguments();
		info = bundle.getParcelable(KEY_SHAREINFO);
		
		File file = new File(info.bmpPathSaved);
		if (file.exists()) {
			uri = Uri.fromFile(file);
			capture = BitmapFactory.decodeFile(info.bmpPathSaved);
		}
		else{
			Logger.e(TAG+"->file path is wrong!");
		}
		
		Logger.e(TAG+"->1"+info.desc);
		
		if(info.needFixUrl){
			if(info.desc.contains("http://")){
				info.desc = info.desc.substring(0,info.desc.indexOf("http://"));
			}
		}
		
		info.desc = info.desc +" "+ info.linkUrl;		
		Logger.e(TAG+"->2"+info.desc);
		addShareItem(activity);
		
		String[] from = new String[] { KEY_ICON, KEY_TITLE };
		int[] to = new int[] { R.id.icon, R.id.title };
		SimpleAdapter adapter = new SimpleAdapter(activity, list, R.layout.item_share_info, from, to);

		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(getOnItemClick(activity));
		
		WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值  
		p.width = CommonUtils.per2px(activity.getResources(), 0.8);
		dialog.getWindow().setAttributes(p);
		
		return dialog;
	}

//	private OnItemClickListener getOnItemClick(final Activity activity) {
//		return new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				switch (position) {
//				case 0:
//					if(hasQQ){
//						// QQ分享
//						final Bundle params = new Bundle();
//						
//						String str = info.desc;
//						Logger.e(TAG+ "---------------hasQQ------->1desc:"+str);
//						
//						if(null != str){
//							if(str.length() > 20){
//								if(str.contains("#")){
//									str = str.substring(0, str.indexOf("#"));
//								}
//							}
//						}
//						Logger.e(TAG+ "->2desc:"+str);
//	                	params.putString(QQShare.SHARE_TO_QQ_TITLE, info.name);//标题
//	                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,info.linkUrl);//url
//	                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, str);//摘要
//	                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, info.bmpPathSaved);//本地图片
//	                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME,getString(R.string.app_name));//appname
//	                    doShareToQQ(params);
//	                    
//					} else if(!hasQQ && hasWechat){
//						boolean result = CommonUtils.shareByWechat(activity, uri, info.desc);
//						if (!result){
//							if(!CommonUtils.shareByWechatSDK(activity, YXConstants.WEIXIN_APP_ID, info.linkUrl, capture, info.desc)){
//								CommonUtils.send2share(activity, uri, info.desc,info.linkUrl);
//							}
//						}
//						
//					} else if(!hasQQ && !hasWechat && hasWebo){
//						CommonUtils.shareByWeibo(activity, uri, info.desc);
//						
//					} else {
//						CommonUtils.send2share(activity, uri, info.desc,info.linkUrl);
//					}
//					break;
//				
//				case 1:
//					if(hasQQ && hasWechat){
//						boolean result = CommonUtils.shareByWechat(activity, uri, info.desc);
//						if (!result){
//							if(!CommonUtils.shareByWechatSDK(activity, YXConstants.WEIXIN_APP_ID, info.linkUrl, capture, info.desc)){
//								CommonUtils.send2share(activity, uri, info.desc,info.linkUrl);
//							}
//						}
//						
//					} else if (hasWebo){
//						CommonUtils.shareByWeibo(activity, uri, info.desc);
//						
//					} else {
//						CommonUtils.send2share(activity, uri, info.desc,info.linkUrl);
//					}
//						
//					break;
//					
//				case 2:
//					if (hasQQ && hasWechat && hasWebo){
//						CommonUtils.shareByWeibo(activity, uri, info.desc);
//						
//					} else {
//						CommonUtils.send2share(activity, uri, info.desc,info.linkUrl);
//					} 
//					break;
//					
//				case 3:
//					if(hasQQ && hasWechat && hasWebo){
//						CommonUtils.send2share(activity, uri, info.desc,info.linkUrl);
//					}
//					break;
//					
//				default:
//					break;
//				}
//				dismiss();
//				MyApplication.getTencent().releaseResource();
//			}
//
//		};
//	}

//	private void doShareToQQ(final Bundle params) {
//		MyApplication.getTencent().shareToQQ(getActivity(), params, qqShareListener);    
//    }	
//	
//	 IUiListener qqShareListener = new IUiListener() {
//	        @Override
//	        public void onCancel() {
//	        }
//	        @Override
//	        public void onComplete(Object response) {
//	        }
//	        @Override
//	        public void onError(UiError e) {
//	        }
//	    };
	
	private void addShareItem(final Activity activity) {
		if (CommonUtils.exitsApp(activity, YXConstants.QQ_APP_NAME)) {
			item = new HashMap<String, Object>();
			item.put(KEY_ICON, R.drawable.logo_qq);
			item.put(KEY_TITLE, "QQ");
			list.add(item);
			
			hasQQ = true;
		}
		
		if (CommonUtils.exitsApp(activity, YXConstants.WeChat_APP_NAME)) {
			item = new HashMap<String, Object>();
			item.put(KEY_ICON, R.drawable.logo_share_comments_icon);
			item.put(KEY_TITLE, getString(R.string.share_comments));
			list.add(item);
			
			hasWechat = true;
		}
		
		if (CommonUtils.exitsApp(activity, YXConstants.Weibo_APP_NAME)) {
			item = new HashMap<String, Object>();
			item.put(KEY_ICON, R.drawable.logo_weibo);
			item.put(KEY_TITLE,getString(R.string.share_sina_weibo));
			list.add(item);
			
			hasWebo = true;
		}
		
		//手机自带分享
		item = new HashMap<String, Object>();
		item.put(KEY_ICON, R.drawable.logo_share);
		item.put(KEY_TITLE, getString(R.string.share_others));
		list.add(item);
	}

}
