package me.youxiong.person.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 工具类
 * 
 * @author QY
 * 
 */
public final class Utility {
	
	public static final String TAG = "Utility";

	/** 隐藏软键盘 */
	public static void hideSoftInputFromWindow(Context context, View view) {
		try {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.RESULT_UNCHANGED_SHOWN);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}
	
	/**开启软键盘*/
	public static void showSoftInputFromWindow(Context context, View view){
		((InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.showSoftInput(view, 0);
	}
	
	/**隐藏键盘*/
	public static boolean hideSoftKeyBoard(Activity activity) {
		InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		//没有聚焦控件时软键盘一定不显示； 不加该行 则下一行可能会崩溃
		if(activity.getCurrentFocus() == null) {
			return false;
		}
		boolean result = inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		return result;
	}
	
	public static boolean getSoftKeyBoardIsShow(Activity activity){
		 if(activity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED){
			 return true;
		 }
		 return false;
	}
	
	

	

	/** 验证邮箱地址 */
	public static boolean isValidEmailAddress(String email) {
		if (TextUtils.isEmpty(email)) {
			return false;
		}
		java.util.regex.Pattern p = java.util.regex.Pattern
				.compile(".+@.+\\.[a-z]+");
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}


	/** 缩放图片 */
	public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
		int src_w = bitmap.getWidth();
		int src_h = bitmap.getHeight();
		float scale_w = ((float) dst_w) / src_w;
		float scale_h = ((float) dst_h) / src_h;
		Matrix matrix = new Matrix();
		matrix.postScale(scale_w, scale_h);
		Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,true);
		if(bitmap !=null && !bitmap.isRecycled()){
			bitmap.recycle();
			System.gc();
		}
		return dstbmp;
	}
	
	public static Bitmap bitmapScaleByMatrix(Bitmap bitmap,Matrix matrix){
		int src_w = bitmap.getWidth();
		int src_h = bitmap.getHeight();
		return Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,true);
	}

	/** 缩放图片 */
	public static Bitmap bitmapScale(Bitmap bitmap, float dst_w, float dst_h) {
		int src_w = bitmap.getWidth();
		int src_h = bitmap.getHeight();
		float scale_w = dst_w / src_w;
		float scale_h = dst_h / src_h;
		Matrix matrix = new Matrix();
		matrix.postScale(scale_w, scale_h);
		Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
				true);
		
		return dstbmp;
	}

	public static Matrix imageScaleMatrix(Bitmap bitmap, int dst_w, int dst_h) {
		int src_w = bitmap.getWidth();
		int src_h = bitmap.getHeight();
		float scale_w = ((float) dst_w) / src_w;
		float scale_h = ((float) dst_h) / src_h;
		Matrix matrix = new Matrix();
		matrix.postScale(scale_w, scale_h);
		return matrix;
	}

	/**
	 * 将图片缩放在一定范围
	 * 
	 * @param bitmap
	 * @param maxW
	 * @param maxH
	 * @return
	 */
	public static Bitmap imageScaleLimited(Bitmap bitmap, int maxW, int maxH,
			boolean recycle) {
		if (bitmap == null) {
			return bitmap;
		}
		int src_w = bitmap.getWidth();
		int src_h = bitmap.getHeight();
		float scale_w = ((float) maxW) / src_w;
		float scale_h = ((float) maxH) / src_h;
		scale_h = Math.min(scale_w, scale_h);
		scale_w = scale_h;
		Matrix matrix = new Matrix();
		matrix.postScale(scale_w, scale_h);
		Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
				true);
		if (recycle && bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		return dstbmp;
	}

	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/** 格式化时间 */
	public static String date2StringDateHHmm(long ms) {
		Date date = new Date(ms);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				"HH:mm", Locale.getDefault());
		return sdf.format(date);
	}

	/** 把view转陈bitmap */
	public static Bitmap convertViewToBitmap(View view, int bitmapWidth,
			int bitmapHeight) {
		Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight,
				Bitmap.Config.ARGB_8888);
		view.draw(new Canvas(bitmap));

		return bitmap;
	}

	public static Bitmap createReflectedImage(View view, float scale,
			int retHeight) {
		if (view == null || view.getMeasuredWidth() <= 0
				|| view.getMeasuredHeight() <= 0)
			return null;

		Bitmap originalImage = convertViewToBitmap(view,
				view.getMeasuredWidth(), view.getMeasuredHeight());
		if (originalImage == null)
			return null;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		if (retHeight == -1) {
			retHeight = (int) (height * scale);
		}
		if (width == 0 || height == 0) {
			return null;
		}
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height
				- retHeight, width, retHeight, matrix, false);

		Bitmap reflection = Bitmap.createBitmap(width, retHeight,
				Config.ARGB_8888);
		Canvas rCanvas = new Canvas(reflection);
		rCanvas.drawBitmap(reflectionImage, 0, 0, null);
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, reflection.getHeight(),
				0x50ffffff, 0x00ffffff, TileMode.MIRROR);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		rCanvas.drawRect(0, 0, width, reflection.getHeight(), paint);

		return reflection;
	}

	/** 判断url地址是否有效 */
	public static boolean exitsHttpUrl(String url) {
		boolean exits = false;
		HttpHead head = new HttpHead(url);

		final int HTTP_TIME_OUT = 30 * 1000;// 毫秒
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, HTTP_TIME_OUT);
		HttpConnectionParams.setSoTimeout(params, HTTP_TIME_OUT);

		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(head);
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				exits = true;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}

		return exits;
	}


	/**
	 * Returns MAC address of the given interface name.
	 * 
	 * @param interfaceName
	 *            eth0, wlan0 or NULL=use first interface
	 * @return mac address or empty string
	 */
	public static String getMACAddress(String interfaceName) {
		Log.i(TAG, "Utils.getMACAddress: " + interfaceName);
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				if (interfaceName != null) {
					if (!intf.getName().equalsIgnoreCase(interfaceName))
						continue;
				}
				byte[] mac = intf.getHardwareAddress();
				if (mac == null)
					return "";
				StringBuilder buf = new StringBuilder();
				for (int idx = 0; idx < mac.length; idx++)
					buf.append(String.format("%02X:", mac[idx]));
				if (buf.length() > 0)
					buf.deleteCharAt(buf.length() - 1);
				return buf.toString();
			}
		} catch (Exception ex) {
			Log.e(TAG, "" + ex);
		}
		return "";
	}

	/** 判断app是否存在 */
	public static boolean exitsApp(Context ctt, String pkg) {
		PackageManager pm = ctt.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	public final static String GOOGLE_PLAY_PACKAGENAME = "com.android.vending";

	public static void go2GooglePlay(Context ctt, String packageName,
			PackageManager packageMgr) {
		try {
			Intent intent = packageMgr
					.getLaunchIntentForPackage(GOOGLE_PLAY_PACKAGENAME);
			intent.setAction(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(Uri.parse("market://details?id=" + packageName));
			if (isIntentAvailable(ctt, intent)) {
				ctt.startActivity(intent);
			}
		} catch (Exception e) {
			// 在1.5及以前版本会要求catch(android.content.pm.PackageManager.NameNotFoundException)异常，该异常在1.5以后版本已取消。
			e.printStackTrace();
		}
	}

	/** 获取壁纸服务的类名 */
	public static String getLiveWallpaperServiceClassName(Context ctt,
			String packageName) {
		String clsName = null;
		if (ctt != null && !TextUtils.isEmpty(packageName)) {
			PackageManager manager = ctt.getPackageManager();
			final Intent intent = new Intent(WallpaperService.SERVICE_INTERFACE);
			intent.setPackage(packageName);
			List<ResolveInfo> apps = null;
			apps = manager.queryIntentServices(intent,
					PackageManager.GET_INTENT_FILTERS);
			if (apps != null && apps.size() > 0) {
				ResolveInfo ri = apps.get(0);
				clsName = ri.serviceInfo.name;
			}
		}
		return clsName;
	}
	
	/** 全屏 */
	public static void fullScreen(boolean full, Activity activity) {
		Window window = activity.getWindow();
		if (full) {
			WindowManager.LayoutParams params = window.getAttributes();
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
		} else {
			WindowManager.LayoutParams params = window.getAttributes();
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
		}
	}

	static final Point screenSize = new Point();

	/** 获取屏幕信息 */
	public static Point getScreenSize(Context ctt) {
		if (ctt == null) {
			return screenSize;
		}
		WindowManager wm = (WindowManager) ctt
				.getSystemService(Context.WINDOW_SERVICE);
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
					// Log.i(TAG, "screen size:" + screenSize.toString());
				}
			}
		}
		return screenSize;
	}

	/**
	 * 手机信息
	 * 
	 * @return
	 */
	public static String getMobilInforStr(Context ctt) {
		HashMap<String, String> MobileInfor = new HashMap<String, String>();
		TelephonyManager tm = null;
		if (ctt != null) {
			tm = ((TelephonyManager) ctt
					.getSystemService(Context.TELEPHONY_SERVICE));
			if (tm != null) {
				MobileInfor.put("DeviceId", tm.getDeviceId());// IMEI
				MobileInfor.put("Line1Number", tm.getLine1Number());
				MobileInfor.put("DeviceSoftwareVersion",
						tm.getDeviceSoftwareVersion());
				MobileInfor.put("SimOperatorName", tm.getSimOperatorName());
				MobileInfor.put("SimSerialNumber", tm.getSimSerialNumber());

				MobileInfor.put("NetworkType", "" + tm.getNetworkType());
				MobileInfor.put("PhoneType", "" + tm.getPhoneType());

				MobileInfor.put("Operator", tm.getSimOperator());
				MobileInfor.put("NetworkOperatorName",
						tm.getNetworkOperatorName());
				MobileInfor.put("SubId", tm.getSubscriberId());// IMSI
				MobileInfor.put("Country", tm.getNetworkCountryIso());
			}
		}

		MobileInfor.put("BOARD", Build.BOARD);
		MobileInfor.put("BRAND", Build.BRAND);
		MobileInfor.put("DEVICE", Build.DEVICE);
		MobileInfor.put("DISPLAY", Build.DISPLAY);
		MobileInfor.put("FINGERPRINT", Build.FINGERPRINT);
		MobileInfor.put("ID", Build.ID);
		MobileInfor.put("MODEL", Build.MODEL);
		MobileInfor.put("PRODUCT", Build.PRODUCT);
		MobileInfor.put("INCREMENTAL", Build.VERSION.INCREMENTAL);
		MobileInfor.put("RELEASE", Build.VERSION.RELEASE);
		MobileInfor.put("SDK", "" + Build.VERSION.SDK_INT);
		MobileInfor.put("HOST", Build.HOST);
		MobileInfor.put("TAGS", Build.TAGS);
		MobileInfor.put("TYPE", Build.TYPE);
		MobileInfor.put("USER", Build.USER);
		MobileInfor.put("TIME", "" + Build.TIME);
		// MobileInfor.put("MANUFACTURER", ""+Build.MANUFACTURER);

		WifiManager wifi = (WifiManager) ctt
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		MobileInfor.put("wifiMacAddress", info.getMacAddress());// MAC?
		MobileInfor.put("wifiState", "" + wifi.getWifiState());
		MobileInfor.put("IP", getLocalIpAddress());
		// MobileInfor.put("UserAgent", new
		// WebView(ctt).getSettings().getUserAgentString());

		MobileInfor.put("versionName", getVersionName(ctt));
		MobileInfor.put("versionCode", "" + getVersionCode(ctt));

		StringBuffer sb = new StringBuffer("A:");

		WindowManager wm = (WindowManager) ctt
				.getSystemService(Context.WINDOW_SERVICE);

		if (wm != null) {
			Point size = getScreenSize(ctt);
			int W = size.x;
			int H = size.y;
			sb.append(W + "*" + H + "#");
		}

		for (String key : MobileInfor.keySet()) {
			String v = MobileInfor.get(key);
			if (v != null && v.length() > 0) {
				sb.append(key).append(':').append(v).append('#');
			}
		}
		String mobileInforStr;
		if (sb.length() > 0)
			mobileInforStr = sb.substring(0, sb.length() - 1);
		else {
			mobileInforStr = sb.toString();
		}
		// mobileInforStr += ("\nLength:" +
		// (mobileInforStr.length()-mobileInforStr.split("#").length));
		// log(null, mobileInforStr);
		return mobileInforStr;
	}

	/** 获取当前版本名 */
	public static String getVersionName(Context ctt) {
		StringBuffer sb = new StringBuffer();
		PackageManager manager = ctt.getPackageManager();
		String pkgName = ctt.getPackageName();
		try {
			PackageInfo info = manager.getPackageInfo(pkgName, 0);
			// sb.append(info.packageName);
			sb.append(info.versionName);// 版本名
			// sb.append(info.versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	/** 获取版本号 */
	public static int getVersionCode(Context ctt) {
		int verCode = 1;
		PackageManager manager = ctt.getPackageManager();
		String pkgName = ctt.getPackageName();
		try {
			PackageInfo info = manager.getPackageInfo(pkgName, 0);
			verCode = info.versionCode;// 开发版本名
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return verCode;
	}

	/** 格式化时间 */
	public static String toTimeStr(long lastPlayTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());// TODO getLocale yyyy-MM-dd
		String ret = sdf.format(lastPlayTime);
		return ret;
	}

	/** 格式化时间 */
	public static String toLocalString(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault());// TODO getLocale yyyy-MM-dd
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date sDate = sdf.parse(time);
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault())).format(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

	/** 获取本地IP地址 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, ex.toString());
		}
		return null;
	}
	
	/**
	 * @param context
	 * @param intent
	 * @return
	 */
	public static boolean isIntentAvailable(Context context, Intent intent) {
		if (context != null && intent != null) {
			final PackageManager packageManager = context.getPackageManager();
			List<ResolveInfo> list = packageManager.queryIntentActivities(
					intent, PackageManager.MATCH_DEFAULT_ONLY);
			return list != null && list.size() > 0;
		}
		return false;
	}


	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	public static Address getAddressForLocation(Context context,
			double latitude, double longitude) throws IOException {
		int maxResults = 1;

		Geocoder gc = new Geocoder(context, Locale.getDefault());
		List<Address> addresses = gc.getFromLocation(latitude, longitude,
				maxResults);

		if (addresses.size() > 0) {
			return addresses.get(0);
		} else {
			return null;
		}
	}

	/** 发送邮件 */
	public static void sendEmail(Context context, String email, String subject) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND);// 通过Intent来发送邮件
		emailIntent.setType("plain/text");// 设置邮件格式为plain/text
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});  
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);  
		if (isIntentAvailable(context, emailIntent)) {
			context.startActivity(emailIntent);
		}
	}

	/** 打开url */
	public static void openUrl(Context context, String url) {
		if (url != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			// intent.setClassName("com.android.browser",
			// "com.android.browser.BrowserActivity");
			intent.addCategory(Intent.CATEGORY_BROWSABLE);
			if (isIntentAvailable(context, intent)) {
				context.startActivity(intent);
			}
		}
	}

	public static int dpToPx(Resources res, float dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	// px 转 dip
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	

	/** 判断是否存在外部存储 */
	public static boolean isExternalStorageMounted() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}
	
	/**
	 * 从Assets中读取图片
	 */
	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
		Bitmap image = null;
		if (context != null) {
			AssetManager am = context.getAssets();
			try {
				InputStream is = am.open(fileName);
				if (is != null) {
					image = BitmapFactory.decodeStream(is);
					is.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return image;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean delFile(String path) {
		boolean ret = false;
		try {
			File f = new File(path);
			if (f != null && f.exists()) {
				f.delete();
				ret = true;
			}
		} catch (Exception e) {
			return false;
		}
		return ret;
	}

	public static Spanned toHtml(String text) {
		if (TextUtils.isEmpty(text)) {
			return null;
		}
		text = text.replace("\n", "<br>");
		return Html.fromHtml(text);
	}
	
	/**
	 * 使用文件通道的方式复制文件
	 * 
	 * @param s
	 *            源文件
	 * @param t
	 *            复制到的新文件
	 */
	public static void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isMIUIOrXiaomi() {
		boolean isMIUI = false;

		String line;
		BufferedReader input = null;

		try {
			Process p = Runtime.getRuntime().exec("getprop");
			input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				if (line != null) {
					line = line.toLowerCase(Locale.getDefault());
					if (line.contains("miui") || line.contains("xiaomi")) {
						isMIUI = true;
						break;
					}
				}
			}
		} catch (IOException ex) {
			return isMIUI;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Log.e(TAG, "Exception while closing InputStream", e);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return isMIUI;
	}

	/** 获取状态栏的高度 */
	public static int getStateBarH(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sbar;
	}
	
	/** 日期(时间)转化为字符串.
	 * 
	 * @param formater
	 *            日期或时间的格式.
	 * @param aDate
	 *            java.util.Date类的实例.
	 * @return 日期转化后的字符串.
	 */
	public static String date2String(String formater, Date aDate) {
		if (TextUtils.isEmpty(formater))
			return null;
		if (aDate == null)
			return null;
		return (new SimpleDateFormat(formater,Locale.CHINESE)).format(aDate);
	}
	
	/**设置EditText光标*/
	public static void setCursorEnd(EditText editText){
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
		    Spannable spanText = (Spannable)text;
		    Selection.setSelection(spanText, text.length());
		 }
	}
	
	private static long lastClickTime;  
	//判断是否快速点击
	public static boolean isFastDoubleClick(){
		long time = System.currentTimeMillis();  
        long timeD = time - lastClickTime;  
        if ( 0 < timeD && timeD < 5000) { 
        	lastClickTime = time;   
        	return true; 
        } 
        return false; 
    }
	
}