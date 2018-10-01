package me.youxiong.person.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.youxiong.person.utils.encode.EncryptUtil;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtils {

	/**
	 * 判断 SDCard 是否存在
	 * @return
	 */
	public static boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	/**
	 * 获取根目录的路径  /sdcard/Android/data/【packname】/
	 * @param context
	 * @return
	 */
	public static String getRootPath(Context context) {
		return context.getExternalFilesDir(null).getParent();
	}
	
	public static String getImageCachePath(Context context) {
		File file = new File(getRootPath(context) + File.separator + "image");
		if(!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}
	public static String getThumbsCachePath(Context context) {
		File file = new File(getRootPath(context) + File.separator + "thumbs");
		if(!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath();
	}
	
	public static final String ADDRESS_DB_NAME = "china_province_city_zone.db3";
	public static final String PROFESSION_DB_NAME = "profession.db";
	
	public static String getDatabasePath(Context context) {
		return "data/data/" + context.getPackageName() + "/databases";
	}
	public static String getDatabaseAddressPath(Context context) {
		return new File(getDatabasePath(context), ADDRESS_DB_NAME).getAbsolutePath();
	}
	public static String getDatabaseTradePath(Context context) {
		return new File(getDatabasePath(context), PROFESSION_DB_NAME).getAbsolutePath();
	}
	
	/**
	 * 复制 raw 文件到 file
	 * @param resId
	 * @param file
	 * @param resources
	 */
	public static void loadDBFile(int resId, File file, Resources resources) {
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = resources.openRawResource(resId);
			outputStream = new FileOutputStream(file);
			byte[] buffer = new byte[7168];
			int count = 0;
			// 开始复制.db文件
			while ((count = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, count);
			}
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
				if (null != outputStream) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * convert the string to another string which is build by the char value
	 * 将字符串的每个字符以ASCLL码值来表示
	 * @param str
	 * @return
	 */
	public static String toCharString(String str) {
		Logger.d("---url= " + str);
		if (TextUtils.isEmpty(str)) {
			str = "null";
		}
		String strBuf = "";
		for (int i = 0; i < str.length(); i++) {
			int a = str.charAt(i);
			strBuf += Integer.toHexString(a).toUpperCase();
		}
		Logger.d("--------------------strBuf= " + strBuf);
		return strBuf;
	}
	public static String toMD5String(String str) {
		if (TextUtils.isEmpty(str)) {
			str = "null";
		}
		return EncryptUtil.encryptMD5(str);
	}
	
	
	public static byte[] getBytes(String filePath) {
		int bufsize = 1024;
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(new File(filePath));
			bos = new ByteArrayOutputStream(bufsize);
			byte[] b = new byte[bufsize];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			bos.flush();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != bos) {
					bos.close();
				}
				if(null != fis) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}
	
}
