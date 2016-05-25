package com.muzhi.camerasdk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.muzhi.camerasdk.R;
import com.muzhi.camerasdk.library.filter.util.ImageFilterTools.FilterType;
import com.muzhi.camerasdk.model.Filter_Effect_Info;
import com.muzhi.camerasdk.model.ItemTextrueInfo;


/**
 * 特效文件
 */
public class FilterUtils {
	public static final String THUMBNAIL_NAME = "thumbnail.png";
	public static final String EFFECT_PRIFIX = "effect";
	
	
	/**
	 * 获取特效列表
	 * @return
	 */
	public static ArrayList<Filter_Effect_Info> getEffectList(){
		
		ArrayList<Filter_Effect_Info> effect_list = new ArrayList<Filter_Effect_Info>();
		
		effect_list.add(new Filter_Effect_Info("原图", R.drawable.camerasdk_filter_normal,FilterType.I_1977));
		effect_list.add(new Filter_Effect_Info("创新", R.drawable.camerasdk_filter_in1977,FilterType.I_1977));
		effect_list.add(new Filter_Effect_Info("流年", R.drawable.camerasdk_filter_amaro,FilterType.I_AMARO));
		effect_list.add(new Filter_Effect_Info("淡雅", R.drawable.camerasdk_filter_brannan,FilterType.I_BRANNAN));
		effect_list.add(new Filter_Effect_Info("怡尚", R.drawable.camerasdk_filter_early_bird,FilterType.I_EARLYBIRD));
		effect_list.add(new Filter_Effect_Info("优格", R.drawable.camerasdk_filter_hefe,FilterType.I_HEFE));
		effect_list.add(new Filter_Effect_Info("胶片", R.drawable.camerasdk_filter_hudson,FilterType.I_HUDSON));
		effect_list.add(new Filter_Effect_Info("黑白", R.drawable.camerasdk_filter_inkwell,FilterType.I_INKWELL));
		effect_list.add(new Filter_Effect_Info("个性", R.drawable.camerasdk_filter_lomo,FilterType.I_LOMO));
		effect_list.add(new Filter_Effect_Info("回忆", R.drawable.camerasdk_filter_lord_kelvin,FilterType.I_LORDKELVIN));
		effect_list.add(new Filter_Effect_Info("不羁", R.drawable.camerasdk_filter_nashville,FilterType.I_NASHVILLE));
		effect_list.add(new Filter_Effect_Info("森系", R.drawable.camerasdk_filter_rise,FilterType.I_NASHVILLE));
		effect_list.add(new Filter_Effect_Info("清新", R.drawable.camerasdk_filter_sierra,FilterType.I_SIERRA));
		effect_list.add(new Filter_Effect_Info("摩登", R.drawable.camerasdk_filter_sutro,FilterType.I_SUTRO));
		effect_list.add(new Filter_Effect_Info("绚丽", R.drawable.camerasdk_filter_toaster,FilterType.I_TOASTER));
		effect_list.add(new Filter_Effect_Info("优雅", R.drawable.camerasdk_filter_valencia,FilterType.I_VALENCIA));
		effect_list.add(new Filter_Effect_Info("日系", R.drawable.camerasdk_filter_walden,FilterType.I_WALDEN));
		effect_list.add(new Filter_Effect_Info("新潮", R.drawable.camerasdk_filter_xproii,FilterType.I_XPROII));
		
		return effect_list;
		
	}
	
    /**
	 * 获取所有贴纸
	 * @return
	 */
	public static void initSticker(Context mContext){
		
		
		/*String fileName="sticker/sticker.txt";
    	try{
    		 InputStreamReader inputReader = new InputStreamReader(mContext.getAssets().open(fileName),Charset.defaultCharset()); 
    		 BufferedReader bufReader = new BufferedReader(inputReader);
             String line="";
             String Result="";
             while((line = bufReader.readLine()) != null){
            	 Result += line.trim(); 
             }
             bufReader.close();
             inputReader.close(); 
             if(!"".equals(Result)){                 	
             	emotions=getGson().fromJson(Result, new TypeToken<List<EmotionInfo>>(){}.getType());
             	getEmotionsTask();
             }
    	}
    	catch(Exception e){
    		 e.printStackTrace(); 
    	} */
		
	}
    
	/*private static void getEmotionsTask() {
    	
    	AssetManager assetManager = AppData.getInstance().getAssets();
    	InputStream inputStream;
    	for(EmotionInfo em : emotions){
    		String fileName="smiley/smileys/"+em.getFileName();
    		//String smileyName=em.getSmileyString();
    		try{
    			inputStream = assetManager.open(fileName);
    			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
    			if(bitmap!=null){
    				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, CommonUtils.dip2px(20), CommonUtils.dip2px(20), true);
    				if(bitmap != scaledBitmap){
    					bitmap.recycle();
    					bitmap = scaledBitmap;
    				}	    				
    				em.setEmotionImage(bitmap);
    			} 
    			inputStream.close();
    		}
    		catch (IOException ignored) {

            }
    	}    	
    }*/
    
	public static ArrayList<ItemTextrueInfo> getEffectParentListFromAsset(Context context) {
		ArrayList<ItemTextrueInfo> results = new ArrayList<ItemTextrueInfo>();
		String[] arr = getFilesFromAsset(context, "");
		MLog.i("==========getEffectParentListFromAsset=========" + Arrays.toString(arr));
		ArrayList<String> effects = new ArrayList<String>();
		for (String s : arr) {
			if(s.startsWith(EFFECT_PRIFIX)) {
				effects.add(s);
			}
		}
		ItemTextrueInfo info = null;
		for (String s : effects) {
			info = new ItemTextrueInfo();
			info.setAssetPath(s);
			results.add(info);
		}
		return results;
	}
	
	
	public static String[] getFilesFromAsset(Context context, String path) {
		String[] results = null;
		AssetManager assetManager = context.getResources().getAssets();
		try {
			if (TextUtils.isEmpty(path)) {
				results = assetManager.list("");
			} else {
				results = assetManager.list(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * 获取 asset 目录下的 acv 文件
	 * @param context
	 * @param path
	 * @return
	 */
//	public static ArrayList<String> getACVFilesFromAsset(Context context, String path) {
//		ArrayList<String> results = new ArrayList<String>();
//		AssetManager assetManager = context.getResources().getAssets();
//		try {
//			String[] arr = null;
//			if (TextUtils.isEmpty(path)) {
//				arr = assetManager.list("");
//			} else {
//				arr = assetManager.list(path);
//			}
//			for (String string : arr) {
//				if (string.endsWith(".acv")) {
//					results.add(string);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return results;
//	}
	
	public static ArrayList<ItemTextrueInfo> getACVItemFromAsset(Context context, String path) {
		ArrayList<ItemTextrueInfo> results = new ArrayList<ItemTextrueInfo>();
		// ArrayList<String> list = getACVFilesFromAsset(context, path);
		AssetManager assetManager = context.getResources().getAssets();
		try {
			String[] list = assetManager.list(path);
			if (null != list && list.length > 0) {
				ItemTextrueInfo info = null;
				for (String name : list) {
					if(name.contains(THUMBNAIL_NAME)) {
						continue;
					}
					info = new ItemTextrueInfo();
					info.setName(name);
					info.setAssetPath(path + "/" + name);
					// info.setResId(IMAGE_EFFECT_IDS[i % IMAGE_EFFECT_IDS.length]);
					results.add(info);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	/**
	 * 获取贴纸资源父级列表
	 * @param context
	 * @param path
	 * @return
	 */
	public static ArrayList<ItemTextrueInfo> getStickerParentFromAsset(Context context) {
		ArrayList<ItemTextrueInfo> results = new ArrayList<ItemTextrueInfo>();
		String[] arr = getFilesFromAsset(context, "sticker");
		ItemTextrueInfo info = null;
		MLog.i("============================getStickerParentFromAsset.arr:" + Arrays.toString(arr));
		for (String string : arr) {
			info = new ItemTextrueInfo();
			info.setName(string);
			info.setAssetPath("sticker/" + string);
			results.add(info);
		}
		return results;
	}
	/**
	 *  从 Asset 文件夹取sticker图片
	 * @param context
	 * @param path
	 * @return
	 */
	public static ArrayList<ItemTextrueInfo> getStickerFromAsset(Context context, String path) {
		ArrayList<ItemTextrueInfo> results = new ArrayList<ItemTextrueInfo>();
		ItemTextrueInfo info = null;
		try {
			String[] arr = context.getAssets().list(path);
			if(TextUtils.isEmpty(path) || arr == null || arr.length == 0) {
				return null;
			}
			for (int i = 0; i < arr.length; i ++) {
				if(arr[i].contains(THUMBNAIL_NAME)) {
					continue;
				}
				info = new ItemTextrueInfo();
				// info.setResId(IMAGE_STICKER_IDS[i % IMAGE_STICKER_IDS.length]);
				info.setAssetPath(path + "/" + arr[i]);
				results.add(info);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}
	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
}
