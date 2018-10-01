package me.youxiong.person.utils;

import java.io.File;

import me.youxiong.person.ui.fragment.base.BaseFragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * 用户上传图像工具类
 * @author mythwind
 */
public final class UploadPhotoUtils {
	private static final String INTENT_ACTION_CAMERA_CROP = "com.android.camera.action.CROP";
	private static final int PICTURE_WIDTH = 210;
	private static final int PICTURE_HEIGHT = 240;
	
	// private static final int REQUEST_CODE_CAMERA = 1000;// 相机
	public static final int REQUEST_CODE_ALBUM = 1001;// 文件
	public static final int REQUEST_CODE_UPLOAD = 1002;// 上传
	
	// 切图的缓存路径
	private static final String mCropPath = "/crop_picture.jpg";;
	
	private String mCachePath;

	private IUploadPhotoLinstener mUploadPhotoLinstener;

	public UploadPhotoUtils(Activity context) {
		if(null != context){
			mCachePath = FileUtils.getRootPath(context);
		}
	}
	
	public interface IUploadPhotoLinstener {
		public void startUpload(String photoPath);
	}
	
	public void setUploadPhotoLinstener(IUploadPhotoLinstener linstener) {
		mUploadPhotoLinstener = linstener;
	}
	/**
	 * activity 上传图像
	 */
	public void choosePhoto(Activity context) {
		if (!Utility.isExternalStorageMounted()) {
			Toast.makeText(context, "no sdcard", Toast.LENGTH_LONG).show();
			return;
		}
		if(null == context){
			return ;
		}
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		try {
			context.startActivityForResult(intent, REQUEST_CODE_ALBUM);
		} catch (Exception e) {
		}
	}
	public void choosePhoto(BaseFragment fragment) {
		if(null == fragment){
			return ;
		}
		if (!Utility.isExternalStorageMounted()) {
			Toast.makeText(fragment.getActivity(), "no sdcard", Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		try {
			fragment.startActivityForResult(intent, REQUEST_CODE_ALBUM);
		} catch (Exception e) {
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data, Activity act) {
		if (requestCode == REQUEST_CODE_ALBUM) {
			if (data == null) {
				Logger.e("处理失败");
			} else {
				startPhotoZoom(data.getData(), act);
			}
		} else if (requestCode == REQUEST_CODE_UPLOAD) {
			if(null != mUploadPhotoLinstener) {
				mUploadPhotoLinstener.startUpload(mCachePath + mCropPath);
			}
		}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data, BaseFragment fragment) {
		if (requestCode == REQUEST_CODE_ALBUM) {
			if (data == null) {
				Logger.e("处理失败");
			} else {
				startPhotoZoom(data.getData(), fragment);
			}
		} else if (requestCode == REQUEST_CODE_UPLOAD) {
			if(null != mUploadPhotoLinstener) {
				mUploadPhotoLinstener.startUpload(mCachePath + mCropPath);
			}
		}
	}

	/**
	 * Activity 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri, Activity act) {
		if(null == act){
			return ;
		}
		Intent intent = new Intent(INTENT_ACTION_CAMERA_CROP);
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", PICTURE_WIDTH); // 这个是裁剪时候的 裁剪框的 X 方向的比例。
		intent.putExtra("aspectY", PICTURE_HEIGHT);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", PICTURE_WIDTH);
		intent.putExtra("outputY", PICTURE_HEIGHT);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		intent.putExtra("return-data", false);
		File f = new File(mCachePath + mCropPath);
		if (f.exists()) {
			f.delete();
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		try {
			act.startActivityForResult(intent, REQUEST_CODE_UPLOAD);
		} catch (Exception e) {
		}
	}
	/**
	 * Fragment 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri, BaseFragment fragment) {
		if(null == fragment){
			return ;
		}
		Intent intent = new Intent(INTENT_ACTION_CAMERA_CROP);
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", PICTURE_WIDTH); // 这个是裁剪时候的 裁剪框的 X 方向的比例。
		intent.putExtra("aspectY", PICTURE_HEIGHT);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", PICTURE_WIDTH);
		intent.putExtra("outputY", PICTURE_HEIGHT);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		intent.putExtra("return-data", false);
		File f = new File(mCachePath + mCropPath);
		if (f.exists()) {
			f.delete();
		}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		try {
			fragment.startActivityForResult(intent, REQUEST_CODE_UPLOAD);
		} catch (Exception e) {
		}
	}
	
	/** 结果处理 */
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == REQUEST_CODE_CAMERA) {
//			startPhotoZoom(Uri.fromFile(new File(cachePath + photoBuf)));
//		} else if (requestCode == REQUEST_CODE_ALBUM) {
//			if (data == null) {
//			} else
//				startPhotoZoom(data.getData());
//		} else if (requestCode == REQUEST_CODE_UPLOAD) {
//			Bitmap bitmap = BitmapFactory.decodeFile(cachePath + cropBuf);
//			if (context instanceof UserInfoActivity) {
//				UserInfoActivity act = (UserInfoActivity) context;
//				act.launchRequest(StoreRequestFactory.getUploadAvatarRequest(
//						bitmap, cachePath + cropBuf));
//			} else if (context instanceof HomeActivity) {
//				HomeActivity act = (HomeActivity) context;
//				act.launchRequest(StoreRequestFactory.getUploadAvatarRequest(
//						bitmap, cachePath + cropBuf));
//			}
//			bitmap = null;
//		}
//	}

}
