package me.youxiong.person.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class PackageUtil {

	/**
	 * 获取手机 IMEI 号
	 * @param context
	 * @return
	 */
	public static String getDeviceImei(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String defaultImei = "0123456789abcde";
		String imei = mTelephonyMgr.getDeviceId();
		imei = (imei == null) ? defaultImei : imei;
		return imei;
	}

	/**
	 * 获取手机 IMSI 号
	 * @param context
	 * @return
	 */
	public static String getDeviceImsi(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String defaultImsi = "123456789abcdef";
		String imsi = mTelephonyMgr.getSubscriberId();
		imsi = (imsi == null) ? defaultImsi : imsi;
		return imsi;
	}

	/**
	 * 获取手机版本名称
	 * @param context
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		String result = "1.0";
		if (context == null)
			return result;
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			if(null != packageInfo) {
				result = packageInfo.versionName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger.d("getAppVersionName=" + result);
		return result;
	}
	
	/**
	 * 获取手机的版本号
	 * @param context
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		int result = 1;
		if (context == null)
			return result;
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			if(null != packageInfo) {
				result = packageInfo.versionCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取 meta_data 的数据
	 * @param context
	 * @param metaDataName
	 * @return
	 */
	public static String getMetaDataValue(Context context, String metaDataName) {
		String metaDataValue = "";
		if (context == null || TextUtils.isEmpty(metaDataName))
			return metaDataValue;
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			if(null != appInfo && null != appInfo.metaData) {
				metaDataValue = appInfo.metaData.getString(metaDataName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Logger.d("getMetaDataValue=" + metaDataValue);
		return metaDataValue;
	}
	
	// test_Android|手机型号:MI2S|SDK版本:16|系统版本:4.1.1|软件版本名称:v2.0|软件版本号:4 
	public static String getMobileInfo(Context context) {
		String result = "";
		StringBuilder sb = new StringBuilder("test_Android");
		sb.append("|os_model:").append(Build.MODEL);
		sb.append("|id:").append(Build.ID);
		sb.append("|os_sdk:").append(Build.VERSION.SDK_INT);
		sb.append("|os_version:").append(Build.VERSION.RELEASE);
		sb.append("|app_version_code").append(getAppVersionCode(context));
		try {
			result = URLEncoder.encode(sb.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return sb.toString();
		}
		return result;
	}

	public static String getDeviceInfo(Context context) {
		try {
			JSONObject json = new JSONObject();
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = tm.getDeviceId();
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);
			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
