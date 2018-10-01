package me.youxiong.person.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

public class BitmapUtils {

	public static byte[] bmpToByteArr(Bitmap bitmap) {
		if(null == bitmap) {
			return null;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArray;
	}
	
	/**
	 * 从本地读取图片
	 * @param context
	 * @param filepath  图片文件的路径
	 * @return
	 */
	public static Bitmap getBitmapByPath(Context context, String filepath) {
		// FileInputStream fis = new FileInputStream("/sdcard/test.png");
		if(TextUtils.isEmpty(filepath)) {
			return null;
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
    	opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
    	opt.inPurgeable = true;
    	opt.inInputShareable = true;
    	opt.inSampleSize = 2;
    	
    	Bitmap bitmap = null;
    	FileInputStream fis = null;
    	try {
			// 获取资源图片
			fis = new FileInputStream(filepath);
			bitmap = BitmapFactory.decodeStream(fis, null, opt);
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if(null != fis) {
					fis.close();
				}
			} catch (IOException e) {
			}
		}
    	return bitmap;
	}
	public static Bitmap getBitmapByResource(Context context, String resourceName) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
    	opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
    	opt.inPurgeable = true;
    	opt.inInputShareable = true;
    	// 获取资源图片  /res/drawable/test.png
    	// InputStream is = context.getClass().getResourceAsStream(" /res/drawable/test.png");
    	InputStream is = context.getClass().getResourceAsStream(resourceName);
    	Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
    	try {
			is.close();
		} catch (IOException e) {
			// e.printStackTrace();
			return null;
		}
    	return bitmap;
		
	}
	
	public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels) {
		if (null == bitmap) {
			return null;
		}
		Bitmap roundConcerImage = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(roundConcerImage);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, null, rect, paint);
		return roundConcerImage;
	}
	
	public static Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
		if (null == bitmap) {
			return null;
		}
		int curWidth = bitmap.getWidth();
		int curHeight = bitmap.getHeight();
		if (curWidth <= 0 || curHeight <= 0) {
			return null;
		}
		if (curWidth == newWidth && curHeight == newHeight) {
			return null;
		}
		float scaleX = (float)newWidth / curWidth;
		float scaleY = (float)newHeight / curHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, curWidth, curHeight, matrix, true);
		return resizeBmp;
	}
	
	public static boolean saveBitmap(Bitmap bitmap, String filename) {
		return saveBitmap(bitmap, filename, Bitmap.CompressFormat.JPEG, 100);
	}
	
	public static boolean saveBitmap(Bitmap bitmap, String filename, CompressFormat format, int quality) {
		if (null == bitmap || null == filename) {
			return false;
		}
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null; 
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(format, quality, baos);
			byte[] byteArray = baos.toByteArray();	
			
			File file = new File(filename);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(byteArray);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}	
	}
}
