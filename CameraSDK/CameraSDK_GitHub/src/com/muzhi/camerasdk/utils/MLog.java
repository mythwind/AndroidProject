package com.muzhi.camerasdk.utils;

import android.util.Log;

public final class MLog {
	
	private static final boolean DEBUG = true;
	private static final String TAG = "camerasdk";
	private static final String PRIFIX_TAG = "wwwww==============";
	
	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, PRIFIX_TAG + msg);
		}
	}
	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, PRIFIX_TAG + msg);
		}
	}
}
