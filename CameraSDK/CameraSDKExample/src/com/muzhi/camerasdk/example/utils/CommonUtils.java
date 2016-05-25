package com.muzhi.camerasdk.example.utils;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;

public class CommonUtils {

	public static Point getScreenSizePix(Activity act) {
		Point point = new Point();
		DisplayMetrics dm = act.getResources().getDisplayMetrics();
		point.x = dm.widthPixels;
		point.y = dm.heightPixels;
		return point;
	}

}
