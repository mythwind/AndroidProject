package com.muzhi.camerasdk.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ScreenHelper {


	public static int dpToPx(Resources res, int dp) {
		if (null == res) {
			return 0;
		}
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	public static int dip2px(Resources res, float dipValue) {
		if (null == res) {
			return 0;
		}
		final float scale = res.getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Resources res, float pxValue) {
		if (null == res) {
			return 0;
		}
		final float scale = res.getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 百分比转换为实际的像素值（宽度）
	 * 
	 * @param per
	 * @return
	 */
	public static int per2px(Resources res, double per) {
		if (null == res) {
			return 0;
		}
		if (per < 0 || per > 1) {
			return 0;
		}
		DisplayMetrics dm = res.getDisplayMetrics();
		return (int) Math.ceil(dm.widthPixels * per);
	}

	/**
	 * 百分比转换为实际的像素值（高度）
	 * 
	 * @param per
	 * @return
	 */
	public static int perH2px(Resources res, double per) {
		if (null == res) {
			return 0;
		}
		if (per < 0 || per > 1) {
			return 0;
		}
		DisplayMetrics dm = res.getDisplayMetrics();
		return (int) Math.ceil(dm.heightPixels * per);
	}
	
}
