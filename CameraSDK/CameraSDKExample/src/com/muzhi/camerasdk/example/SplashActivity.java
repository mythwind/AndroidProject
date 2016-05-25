package com.muzhi.camerasdk.example;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.muzhi.camerasdk.example.R;
import com.muzhi.camerasdk.example.utils.BitmapUtils;
import com.muzhi.camerasdk.example.utils.CommonUtils;
import com.muzhi.camerasdk.model.CameraSdkParameterInfo;
import com.muzhi.camerasdk.utils.FileUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;

public class SplashActivity extends Activity implements OnClickListener {
	private CameraSdkParameterInfo mCameraSdkParameterInfo=new CameraSdkParameterInfo();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (VERSION.SDK_INT >= 19) {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		setContentView(R.layout.activity_splash);

		initViewComponent();
		registerListener();
	}

	@SuppressWarnings("deprecation")
	private void initViewComponent() {
		Point screenSize = CommonUtils.getScreenSizePix(this);
		Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.splash, screenSize.x,
				screenSize.y);
		if (VERSION.SDK_INT >= 196) {
			findViewById(R.id.root_view).setBackground(new BitmapDrawable(getResources(), bitmap));
		} else {
			findViewById(R.id.root_view).setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
		}
	}

	private void registerListener() {
		findViewById(R.id.iv_camera).setOnClickListener(this);
		findViewById(R.id.iv_album).setOnClickListener(this);
	}

	private void animateClickView(final View v, final ClickAnimation callback) {
		float factor = 1.5f;
		animate(v).scaleX(factor).scaleY(factor).alpha(0).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				ViewHelper.setScaleX(v, 1);
				ViewHelper.setScaleY(v, 1);
				ViewHelper.setAlpha(v, 1);
				if (callback != null) {
					callback.onClick(v);
				}
				super.onAnimationEnd(animation);
			}
		});
	}

	@Override
	public void onClick(View v) {
		initSettings();
		animateClickView(v, new ClickAnimation() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.iv_camera:
					showCameraAction();
					break;
				case R.id.iv_album:
					turnToAlbum();
					break;
				default:
					break;
				}
			}
		});
	}

	private void initSettings() {
		// 开启单选模式
		mCameraSdkParameterInfo.setSingle_mode(true);
		mCameraSdkParameterInfo.setMax_image(9);
		// 显示相机
		mCameraSdkParameterInfo.setShow_camera(true);
		
		mCameraSdkParameterInfo.setCroper_image(false);
		// 开启滤镜模式
		mCameraSdkParameterInfo.setFilter_image(true);
	}
	
	private File mTmpFile;
	/**
     * 选择相机
     */
	private void showCameraAction() {
		// 跳转到系统照相机
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (cameraIntent.resolveActivity(getPackageManager()) != null) {
			// 设置系统相机拍照后的输出路径
			// 创建临时文件
			mTmpFile = FileUtils.createTmpFile(this);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
			startActivityForResult(cameraIntent, CameraSdkParameterInfo.TAKE_PICTURE_FROM_CAMERA);
		} else {
			Toast.makeText(this, R.string.camerasdk_msg_no_camera, Toast.LENGTH_SHORT).show();
		}
	}
	private void turnToAlbum() {
		Intent intent = new Intent();
		intent.setClassName(getApplication(), "com.muzhi.camerasdk.PhotoPickActivity");
		// intent.setClassName(getApplication(), "com.muzhi.camerasdk.CameraActivity");
		
		Bundle b = new Bundle();
		b.putSerializable(CameraSdkParameterInfo.EXTRA_PARAMETER, mCameraSdkParameterInfo);
		intent.putExtras(b);
		startActivityForResult(intent, CameraSdkParameterInfo.TAKE_PICTURE_FROM_GALLERY);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == CameraSdkParameterInfo.TAKE_PICTURE_FROM_GALLERY) {
			System.out.println("=====================onActivityResult.ResultActivity==========================");
			//TODO 图片保存完成之后的回调，跳转到 分享的结果页面
//			if (data != null) {
//				Intent intent = new Intent(this, ResultActivity.class);
//				intent.putExtras(data.getExtras());
//				startActivity(intent);
//			}
		} else if (requestCode == CameraSdkParameterInfo.TAKE_PICTURE_FROM_CAMERA) {
			System.out.println("=====================onActivityResult.Save==========================");
			if(resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                	turnToAlbum();
                	//加入content provider
                    ContentValues values = new ContentValues(7);
                    values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, "");
                    values.put(MediaStore.Images.Media.DATE_TAKEN, "");
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.Media.ORIENTATION, 0);
                    values.put(MediaStore.Images.Media.DATA, mTmpFile.getPath());
                    values.put(MediaStore.Images.Media.SIZE, mTmpFile.length());
                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    
                }
			} else {
				if (mTmpFile != null && mTmpFile.exists()) {
					mTmpFile.delete();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	public interface ClickAnimation {
		public void onClick(View v);
	}

}
