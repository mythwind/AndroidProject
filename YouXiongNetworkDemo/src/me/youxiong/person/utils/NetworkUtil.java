package me.youxiong.person.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetworkUtil {

	/** 判断网络是否可用 */
	public static boolean isNetworkAvailable(Context context) {
		NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (networkInfo != null)
			return networkInfo.isAvailable();
		return false;
	}

	/** 判断是否是wifi */
	public static boolean isWiFiNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	/** 获取wifi信号强度 */
	public static int getWiFiLevel(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		int level = 0;
		if (netinfo != null && netinfo.getType() == ConnectivityManager.TYPE_WIFI) {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifiManager.getConnectionInfo();
			if (info.getBSSID() != null) {
				// 链接信号强度
				level = WifiManager.calculateSignalLevel(info.getRssi(), 5);
				// 链接速度
				int speed = info.getLinkSpeed();
				// Wifi源名称
				String ssid = info.getSSID();
				Log.i("getWiFiLevel", ssid + level + ":" + speed);
			}
		}
		return level;
	}
	
	/** 获取网速 */
	public static long getNetworkSpeed(Context context) {
		ProcessBuilder cmd;
		long readBytes = 0;
		BufferedReader rd = null;
		try {
			String[] args = { "/system/bin/cat", "/proc/net/dev" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			rd = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			// int linecount = 0;
			while ((line = rd.readLine()) != null) {
				// linecount++;
				if (line.contains("wlan0") && isWiFiNetworkAvailable(context)) {
					// L.E("获取流量整条字符串",line);
					String[] delim = line.split(":");
					if (delim.length >= 2) {
						String values = delim[1].trim();
						values = nSpace2one(values);
						String[] numbers = values.split(",");// 提取数据
						// Log.i(TAG, "wifi l:"+numbers.length);
						readBytes = Long.parseLong(numbers[0].trim());//
						// Log.i(TAG, "wifi in:"+readBytes);
						readBytes += Long.parseLong(numbers[8].trim());//
						// Log.i(TAG, "wifi total:"+readBytes);
						break;
					}
				}
			}
			rd.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return readBytes;
	}
	
	/** 格式化字符串中间的空格 */
	private static String nSpace2one(String s) {
		String regEx = "[' ']+"; // 一个或多个空格
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(s);
		String ret = m.replaceAll(",").trim();
		return ret;
	}

}
