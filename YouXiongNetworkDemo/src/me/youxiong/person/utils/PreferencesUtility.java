package me.youxiong.person.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.utils.config.YXConstants;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;

public final class PreferencesUtility {
	
	/**
	 * 保存版本号, 版本升级之后的操作
	 * @param context
	 * @param vcode
	 */
	public static void setSavedVersionCode(Context context, int vcode) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(YXConstants.PREFS_SAVED_VCODE, vcode);
		editor.commit();
	}
	public static int getSavedVersionCode(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(YXConstants.PREFS_SAVED_VCODE, 0);
	}
	/**
	 * 保存/获取用户名片设置的等级数
	 * @param context
	 * @param integral
	 */
	public static void setCardCheckLevel(Context context, int checklevel) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(YXConstants.PREFS_KEY_CARD_CHECK_LEVEL, checklevel);
		editor.commit();
	}
	public static int getCardCheckLevel(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(YXConstants.PREFS_KEY_CARD_CHECK_LEVEL, 0);
	}
	/**
	 * 保存/获取用户当前总龙币数
	 * @param context
	 * @param integral
	 */
	public static void setTotalIntegral(Context context, int integral) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(YXConstants.PREFS_KEY_TOTAL_INTEGRAL, integral);
		editor.commit();
	}
	public static int getTotalIntegral(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getInt(YXConstants.PREFS_KEY_TOTAL_INTEGRAL, 0);
	}
	
	/**
	 * 保存用户信息
	 * @param context
	 * @param persionDetailInfo
	 */
	public static void saveUserInfo(Context context, PersonDetailInfo personDetailInfo) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		String value = object2String(personDetailInfo);
		editor.putString(YXConstants.PREFS_USER_INFO, value);
		editor.commit();
	}
	/**
	 * 提取用户信息
	 * @param context
	 * @return
	 */
	public static PersonDetailInfo getUserInfo(Context context) {
		PersonDetailInfo personDetailInfo = null;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(YXConstants.PREFS_USER_INFO, "");
		if(!TextUtils.isEmpty(value)) {
			personDetailInfo = (PersonDetailInfo) string2Object(value);
		}
		return personDetailInfo;
	}
	/**
	 * 保存/获取搜索列表的记录并且排序
	 * @param context
	 * @param searchTime
	 * @param keyword
	 */
	@SuppressWarnings("unchecked")
	public static void saveSearchKeywords(Context context, long searchTime, String keyword) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(YXConstants.PREFS_SEARCH_KEYWORDS, "");
		LinkedHashMap<String, Long> hashMap = (LinkedHashMap<String, Long>) string2Object(value);
		
		if(null != hashMap) {
		for (Map.Entry<String, Long> entry : hashMap.entrySet()) {
			System.out.println(entry.getKey());
		}
		}
		
		if(hashMap == null || hashMap.size() == 0) {
			hashMap = new LinkedHashMap<String, Long>();
			hashMap.put(keyword, searchTime);
		} else {
			if(hashMap.get(keyword) == null) {
				if(hashMap.size() >= 9) {
					String firstKey = hashMap.keySet().iterator().next();
					hashMap.remove(firstKey);
				}
				hashMap.put(keyword, searchTime);
			}
		}
		
		for (Map.Entry<String, Long> entry : hashMap.entrySet()) {
			System.out.println(entry.getKey());
		}
		
		String newValue = object2String(hashMap);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(YXConstants.PREFS_SEARCH_KEYWORDS, newValue);
		editor.commit();
	}
	
	@SuppressWarnings("unchecked")
	public static LinkedHashMap<String, Long> getSearchKeywords(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String value = preferences.getString(YXConstants.PREFS_SEARCH_KEYWORDS, "");
		LinkedHashMap<String, Long> result = null;
		try {
			result = (LinkedHashMap<String, Long>) string2Object(value);
		} catch (Exception e) {
			preferences.edit().clear().commit();
		}
		return result;
	}
	
	private static String object2String(Object obj) {
		if(obj == null) {
			return null;
		}
		//将map转换为byte[]   
		ByteArrayOutputStream toByte = new ByteArrayOutputStream();  
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(toByte);
			oos.writeObject(obj);
			//对byte[]进行Base64编码   
			return Base64.encodeToString(toByte.toByteArray(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(toByte != null) {
					toByte.close();
				}
				if(oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static Object string2Object(String str) {
		if(TextUtils.isEmpty(str)) {
			return null;
		}
		byte[] base64Bytes = Base64.decode(str, 0);
		ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(bais != null) {
					bais.close();
				}
				if(ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
