package me.youxiong.person.utils;

import me.youxiong.person.R;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class LoadImageUtils {

	public static void displayImage(String url, ImageView imageView) {
		if(TextUtils.isEmpty(url) || null == imageView) {
			return ;
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.default_head)
				.showImageForEmptyUri(R.drawable.default_head)
				.showImageOnLoading(R.drawable.default_head)
				.cacheOnDisk(true)
				.cacheInMemory(false)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.build();
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}
	
	public static void displayImage(String url, ImageView imageView, int defaultResId) {
		if(TextUtils.isEmpty(url) || null == imageView) {
			return ;
		}
		if(defaultResId == 0) {
			defaultResId = R.drawable.default_head;
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnFail(defaultResId)
				.showImageForEmptyUri(defaultResId)
				.showImageOnLoading(defaultResId)
				.cacheOnDisk(true)
				.cacheInMemory(false)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.build();
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}
	
	public static void displayImageNoDefault(String url, ImageView imageView) {
		if(TextUtils.isEmpty(url) || null == imageView) {
			return ;
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheOnDisk(true)
				.cacheInMemory(false)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.build();
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}
	
}
