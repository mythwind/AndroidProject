package me.youxiong.person.ui;

import java.io.File;

import me.youxiong.person.R;
import me.youxiong.person.utils.FileUtils;
import me.youxiong.person.utils.Logger;
import me.youxiong.person.utils.PackageUtil;
import me.youxiong.person.utils.PreferencesUtility;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * 
 * 闪屏，以后会从服务器获取来更改闪屏图片
 * @author mythwind
 *
 */
public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final int currVCode = PackageUtil.getAppVersionCode(this);
		final int savedVCode = PreferencesUtility.getSavedVersionCode(this);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				copyDBFile(currVCode > savedVCode);
			}
		}).start();
		
		if(currVCode > savedVCode) {
			Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
			startActivity(intent);
			exitActivity();
			return;
		} else {
			setContentView(R.layout.activity_splash);
		}

		View mLayout = findViewById(R.id.splash_layout);
		// 渐变展示启动屏
		AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
		animation.setDuration(2000);
		mLayout.startAnimation(animation);
		/*
		final RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f, 
				Animation.RELATIVE_TO_SELF, .5f);
		animation.setDuration(1500);		
		animation.setInterpolator(new LinearInterpolator());
		mLayout.startAnimation(animation);
		*/
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				redirectTo();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
	}

	// 复制和加载区域数据库中的数据
    private void copyDBFile(boolean override) {
        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>
    	File dir = new File(FileUtils.getDatabasePath(this));
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        
        File file = new File(dir, FileUtils.ADDRESS_DB_NAME);
        if(override) {
        	file.delete();
        }
        if (!file.exists()) {
           FileUtils.loadDBFile(R.raw.china_province_city_zone, file, getResources());
           Logger.i("DataBase address Load Successfully");
        }
        file = new File(dir, FileUtils.PROFESSION_DB_NAME);
        if(override) {
        	file.delete();
        }
        if (!file.exists()) {
           FileUtils.loadDBFile(R.raw.profession, file, getResources());
           Logger.i("DataBase profession Load Successfully");
        }
    }
	private void redirectTo() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		exitActivity();
	}

	private void exitActivity() {
		finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
}
