package me.youxiong.person.share;

import java.io.File;

import me.youxiong.person.R;
import me.youxiong.person.model.ShareInfo;
import me.youxiong.person.utils.BitmapUtils;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.FileUtils;
import me.youxiong.person.utils.config.YXConstants;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

/***
 * 分享工具类
 * 
 * @author zhangqy
 *
 */
public class ShareUtil {
	
	//分享公用操作
	private static void commentOpt(FragmentActivity context,ShareInfo info) {
		info.linkUrl = YXConstants.WEB_URL;
		
		if (CommonUtils.exitsApp(context, YXConstants.QQ_APP_NAME)
				|| CommonUtils.exitsApp(context, YXConstants.WeChat_APP_NAME)
				|| CommonUtils.exitsApp(context, YXConstants.Weibo_APP_NAME)) {
			 
			ShareDialogFragment.newInstance(info).show(context.getSupportFragmentManager(), "sharedialogfragment");
			
		} else {
			File file = new File(info.bmpPathSaved);
			if(null != file && file.exists()){
				Uri uri = Uri.fromFile(file);
				CommonUtils.send2share(context, uri, info.desc, null);
			}
		}
		
	}
	
	/**来自侧边栏,分类*/
	public static void share(FragmentActivity context) {
		Bitmap bmp = ScreenShot.takeScreenShot(context);
		if (null != bmp  ) {
			File file = new File(FileUtils.getThumbsCachePath(context), "local_" + System.currentTimeMillis() + ".jpg");
			String bmpPathSaved = file.getAbsolutePath();
			
			if (!BitmapUtils.saveBitmap(bmp, bmpPathSaved)) {
				bmpPathSaved = null;
				
			} else {
				ShareInfo info = new ShareInfo();
				info.name = context.getString(R.string.app_name);
				info.desc = info.name + " " + YXConstants.WEB_URL;
				info.bmpPathSaved = bmpPathSaved;
				info.needFixUrl = true;
				
				commentOpt(context, info);
			}
		}
	}
	
}