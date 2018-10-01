package me.youxiong.person.utils.config;

/**
 * 存放一些全局变量
 * @author mythwind
 */
public final class YXConstants {
	// 是否开启友盟接口
	public static final boolean UMENG_ENABLE = true;
	
	public static final String WEB_URL = "http://www.youxiong.me/";
	
	public static final String AGREEMENT_URL = "http://www.youxiong.me/auth/lyhk";
	
	public static final String PREFS_USERNAME = "prefs_username";
	public static final String PREFS_PASSWORD = "prefs_password";
	public static final String PREFS_USER_INFO = "prefs_user_info";
	public static final String PREFS_SPLAH_GUIDE = "prefs_splash_guide"; // 是否显示引导页面
	public static final String PREFS_SAVED_VCODE = "prefs_saved_vcode";  // 保存当前的版本号
	public static final String PREFS_SEARCH_KEYWORDS = "prefs_search_keywords";
	public static final String PREFS_KEY_TOTAL_INTEGRAL = "prefs_key_total_integral";  // 保存当前总龙币数
	public static final String PREFS_KEY_CARD_CHECK_LEVEL = "prefs_key_card_check_level";  // 保存当前用户名片设置的等级
	
	public static final String PREFS_LATITUDE = "prefs_latitude";
	public static final String PREFS_LONTITUDE = "prefs_lontitude";
	
	public static final int LIMIT_SELECT_TRADE_NUMBER = 7;
	
	// ==========================================================
	//  请求参数的名称  start 
	// ==========================================================
	public static final String REQUEST_TOKEN_KEY = "TokenKey";
	public static final String REQUEST_TOKEN_SECRET = "TokenSecret";
	public static final String REQUEST_LATITUDE = "Latitude";
	public static final String REQUEST_LONTITUDE = "Lontitude";
	public static final String REQUEST_IMSI = "Imsi";
	public static final String REQUEST_SOFT_VERSION = "SoftVersion";
	public static final String REQUEST_MOBILE_INFO ="MobileInfo";
	public static final String REQUEST_EQUIPMENT_TYPE = "EquipmentType";
	// ==========================================================
	//  请求参数的名称  end 
	// ==========================================================
	
	public static final String EQUIPMENT_TYPE_IOS = "1";
	public static final String EQUIPMENT_TYPE_ANDROID = "2";
	
	//分享
	public static final String QQ_APP_NAME = "com.tencent.mobileqq";
	public static final String WeChat_APP_NAME = "com.tencent.mm";
	public static final String Weibo_APP_NAME = "com.sina.weibo";

	public static final boolean QQ_LOGIN = true;//qq
	public final static String QQ_APP_ID = "1101508244";//QQ登录id
	public final static String QQ_APP_KEY = "1EJgCrufHIUNBh68";
	
	public static final boolean WEIXIN_LOGIN = true;//weixin
	public final static String WEIXIN_APP_ID = "wx8f762484520ef183";
	public final static String WEIXIN_APP_SECRET = "02c707f8fefaf7188adf1cadb4a23e4c";
	
	public static final boolean WEIBO_LOGIN = true;//
	public static final String SINA_APP_KEY = "2316333178";//新浪微博
	public final static String SINA_APP_SECRET = "67b99ac4cc1cacb0bf47a9fa6b05602b";
	
	public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";//新浪微博
	
}
