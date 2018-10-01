package me.youxiong.person.utils;

import android.util.Log;

public final class Logger {

	private static final boolean DEBUG = true;
	
	private static final String TAG = "findphone";
	private static final String TAG_PREFIX = "wangss--->";
	
	public static void i(String msg) {
		if(DEBUG) {
			Log.i(TAG, TAG_PREFIX + msg);
		}
	}
	public static void i(String tag, String msg) {
		if(DEBUG) {
			Log.i(tag, TAG_PREFIX + msg);
		}
	}
	public static void d(String msg) {
		if(DEBUG) {
			Log.d(TAG, TAG_PREFIX + msg);
		}
	}
	public static void d(String tag, String msg) {
		if(DEBUG) {
			Log.d(tag, TAG_PREFIX + msg);
		}
	}
	public static void e(String msg) {
		if(DEBUG) {
			Log.e(TAG, TAG_PREFIX + msg);
		}
	}
	public static void e(String tag, String msg) {
		if(DEBUG) {
			Log.e(tag, TAG_PREFIX + msg);
		}
	}
	
}
