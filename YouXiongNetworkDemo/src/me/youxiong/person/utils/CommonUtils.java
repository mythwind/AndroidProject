package me.youxiong.person.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.youxiong.person.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class CommonUtils {

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Resources resources, float dpValue) {  
        final float scale = resources.getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }
    
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Resources resources, float pxValue) {  
        final float scale = resources.getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    
    /**
	 * 百分比转换为实际的像素值
	 * 
	 * @param per
	 * @return
	 */
	public static int per2px(Resources resources, double per) {
		if (per < 0 || per > 1) {
			return 0;
		}
		DisplayMetrics dm = resources.getDisplayMetrics();
		return (int) Math.ceil(dm.widthPixels * per);
	}
	
	public static int parseInt(String str) {
		if(TextUtils.isEmpty(str)) {
			return 0;
		}
		int result = 0;
		try {
			result = Integer.parseInt(str);
		} catch (Exception e) {
			result = 0;
		}
		return result;
	}
	public static boolean isEmail(String email){     
		String emailReg = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern pattern = Pattern.compile(emailReg);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	public static void hideSoftInputFromWindow(Context context, View v) {
		try {
			((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Utility", e.toString());
		}
	}
	public static void showSoftInputFromWindow(Context context, View v) {
		try {
			((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(v, InputMethodManager.SHOW_FORCED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Utility", e.toString());
		}
	}
	
	public static void toggleSoftInput(Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Utility", e.toString());
		}
	}
	
	static final Point screenSize = new Point();
	/** 获取屏幕信息 */
	public static Point getScreenSize(Context context) {
		if (context == null) {
			return screenSize;
		}
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		if (wm != null) {
			DisplayMetrics mDisplayMetrics = new DisplayMetrics();
			Display diplay = wm.getDefaultDisplay();
			if (diplay != null) {
				if (Build.VERSION.SDK_INT > 16)// Build.VERSION_CODES.JELLY_BEAN
				{
					diplay.getRealMetrics(mDisplayMetrics);
				} else {
					diplay.getMetrics(mDisplayMetrics);
				}
				int W = mDisplayMetrics.widthPixels;
				int H = mDisplayMetrics.heightPixels;
				if (W * H > 0 && (W > screenSize.x || H > screenSize.y)) {
					screenSize.set(W, H);
				}
			}
		}
		return screenSize;
	}
	public static boolean exitsApp(Context ctt, String packagename) {
		boolean exists = false;
		if (ctt == null || TextUtils.isEmpty(packagename)) {
			return exists;
		}
		PackageManager packageMgr = ctt.getPackageManager();
		if (packageMgr != null) {
			List<PackageInfo> list = packageMgr.getInstalledPackages(0);
			for (int i = 0; i < list.size(); i++) {
				PackageInfo info = list.get(i);
				String temp = info.packageName;
				if (temp.equals(packagename)) {
					exists = true;
					break;
				}
			}
		}
		return exists;
	}
	/**
	 * 限定字符串字节数不多余某字节数
	 * 
	 * @param str
	 * @param bytes
	 * @return
	 */
	public static String limitString(String str, int bytes) {
		if (bytes == 0) {
			return "";
		}
		if (bytes < 1) {
			bytes = 1;
		}
		if (str == null || str.getBytes().length <= bytes) {
			return str;
		}

		if (str.length() > (bytes+1)) {
			str = str.substring(0, (bytes+1));
		}
		return limitString(str.substring(0, str.length() - 1), bytes);
	}
	public static boolean isIntentAvailable(Context context, Intent intent) {
		if (context != null && intent != null) {
			final PackageManager packageManager = context.getPackageManager();
			List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		}
		return false;
	}
	public static byte[] compressImageAsByteArray(Bitmap image, int kb) {
		if (image == null) {
			return null;
		}
		if (kb < 0) {
			kb = 1;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		image.recycle();
		return baos.toByteArray();
	}

	public static Bitmap compressImage(Bitmap image,int kb) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	public static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	/**
	 * 分享信息
	 * @param context
	 * @param uri
	 * @param description
	 * @param url
	 */
	public static void send2share(Context context, Uri uri, String description, String url) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, description);
		shareIntent.putExtra(Intent.EXTRA_TITLE, description);
		shareIntent.putExtra(Intent.EXTRA_TEXT, description);
		if (context != null) {
			Intent itt = Intent.createChooser(shareIntent, context.getString(R.string.share_title));
			itt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if(isIntentAvailable(context, itt)) {
				context.startActivity(itt);
			}
		}
	}

//	public static boolean shareByWechatSDK(Context context, String appid, String url, Bitmap bmp,String desc) {
//		if(context==null || TextUtils.isEmpty(appid)) {
//			return false;
//		}
//		
//		final int THUMB_SIZE = 96;
//		
//		WXWebpageObject wpObj = new WXWebpageObject();
//		wpObj.webpageUrl = url;
//		
//		IWXAPI api = WXAPIFactory.createWXAPI(context, appid/*, false*/);
//		Bitmap screenshot = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//		
//		WXMediaMessage msg = new WXMediaMessage();
//		msg.mediaObject = wpObj;
//		msg.description = CommonUtils.limitString(desc, 1000);;//消息描述 注意：限制长度不超过1KB
//		msg.title = CommonUtils.limitString(desc, 512);//消息标题 注意：限制长度不超过512Bytes
//		//msg.thumbData = bmpToByteArray(screenshot, true);
//		msg.thumbData = CommonUtils.compressImageAsByteArray(screenshot, 32);//缩略图的二进制数据 注意：限制内容大小不超过32KB
//		
//		//Toast.makeText(getActivity(), "size:"+msg.thumbData.length, Toast.LENGTH_LONG).show();
//
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = buildTransaction("img");
//		req.message = msg;
//		req.scene = SendMessageToWX.Req.WXSceneTimeline;
//		if (api != null && api.isWXAppInstalled()) {
//			api.registerApp(appid);
//			if (!api.sendReq(req)) {
//				Log.e("Test", "发送请求失败!");
//				return false;
//			} else {
//				//Log.e("Test", "发送请求成功!");
//				return true;
//			}
//		}
//		return false;
//	}
	
	public static boolean shareByWechat(Context context,Uri uri,String description) {
		if (context == null) {
			return false;
		}
		final String wechatPkg = "com.tencent.mm";
		
		Intent intent = new Intent();
        intent.setComponent(new ComponentName(wechatPkg,"com.tencent.mm.ui.tools.ShareToTimeLineUI"));
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra("Kdescription", description);//默认的分享的文字描述
        intent.putExtra(Intent.EXTRA_SUBJECT,description);
        intent.putExtra(Intent.EXTRA_TITLE,description);
        intent.putExtra(Intent.EXTRA_TEXT, description);
        intent.setType("image/*");
        //intent.setFlags(0x3000001);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        
        //shareByWeibo(description);
        
		if(context.getPackageManager().resolveActivity(intent, 0) != null) {  
			//分享到微信
			context.startActivity(intent);
	        return true;
		}
		return false;
	}
	
	public static boolean shareByWeibo(Context context,Uri uri,String description) {
		if(context==null)
		{
			return false;
		}
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, description);
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		final String weiboPkg = "com.sina.weibo";
        intent.setComponent(new ComponentName(weiboPkg,"com.sina.weibo.EditActivity"));

        if(context.getPackageManager().resolveActivity(intent, 0) != null) {  
			//分享到微信
        	context.startActivity(intent);
	        return true;
		}
        return false;
        /*
		try {
			Context context = createPackageContext(weiboPkg, Context.CONTEXT_IGNORE_SECURITY);
			intent.setClassName(context, "com.sina.weibo.EditActivity");
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			//Toast.makeText(getApplicationContext(), "您的手机没有安装新浪微博客户端",Toast.LENGTH_LONG).show();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		*/
	}
	
	
}
