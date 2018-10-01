package me.youxiong.person.application;

import java.io.File;

import me.youxiong.person.utils.FileUtils;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class MyApplication extends Application {
	// QQ使用原生登录方式
//	private static Tencent mTencent;
//	
//
//	public static Tencent getTencent() {
//		return mTencent;
//	}

	@Override
	public void onCreate() {
		super.onCreate();

//		mTencent = Tencent.createInstance(YXConstants.QQ_APP_ID, this);
		
		/*
		FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Candelita.ttf");
		FontsOverride.setDefaultFont(this, "DEFAULT_BOLD", "fonts/Candelita.ttf");
		FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Candelita.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Candelita.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Candelita.ttf");
		*/
		initImageLoader(getApplicationContext());

	}
	
	public static final Typeface DEFAULT_TYPEFACE;
	static {
		DEFAULT_TYPEFACE = Typeface.DEFAULT;
		System.out.println(DEFAULT_TYPEFACE);
	}

	/**
	 * 初始化 imageloader
	 * 
	 * @param context
	 */
	public void initImageLoader(Context context) {
		boolean hasSDCard = FileUtils.hasSDCard();
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(hasSDCard)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();
		
		File cacheDir = null;
		if(hasSDCard) {
			cacheDir = new File(FileUtils.getImageCachePath(context));
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
					.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
					.tasksProcessingOrder(QueueProcessingType.LIFO).diskCacheFileNameGenerator(new Md5FileNameGenerator())
					.diskCacheSize(200 * 1024 * 1024).diskCacheFileCount(500)
					.memoryCache(new LruMemoryCache(10 * 1024 * 1024)).memoryCacheSize(20 * 1024 * 1024)
					.diskCache(new UnlimitedDiscCache(cacheDir))
					.imageDownloader(new BaseImageDownloader(context)) // default
					.imageDecoder(new BaseImageDecoder(false)) // default
					.defaultDisplayImageOptions(imageOptions) // default
					.build();
			// Initialize ImageLoader with configuration.
			ImageLoader.getInstance().init(config);
		} else {
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
					.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					.memoryCache(new LruMemoryCache(10 * 1024 * 1024))
					.memoryCacheSize(20 * 1024 * 1024)
					.imageDownloader(new BaseImageDownloader(context)) // default
					.imageDecoder(new BaseImageDecoder(false)) // default
					.defaultDisplayImageOptions(imageOptions) // default
					.build();
			// Initialize ImageLoader with configuration.
			ImageLoader.getInstance().init(config);
		}
	}

	// public class Md5FileNameGenerator implements FileNameGenerator {
	//
	// @Override
	// public String generate(String imageUri) {
	// return null;
	// }
	//
	// }
	// public class HashCodeFileNameGenerator implements FileNameGenerator {
	// @Override
	// public String generate(String imageUri) {
	// // return FileUtils.toCharString(imageUri);
	//
	// return FileUtils.toMD5String(imageUri);
	// }
	// }

}
