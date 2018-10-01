package me.youxiong.person.request;

public abstract class AbstractRequest {
	public static final int REQUEST_TYPE_GET_LOGIN = 1; // 登陆信息请求
	public static final int REQUEST_TYPE_GET_REGISTE = 2;  // 注册信息请求
	public static final int REQUEST_TYPE_FORGET_PASSWORD = 3; // 忘记密码请求
	
	
	public static final int REQUEST_TYPE_GET_SEARCH_LIST = 4;
	
	public static final int REQUEST_TYPE_GET_CARD_INFO = 10; // 请求用户名片详情信息
	public static final int REQUEST_TYPE_GET_PRODUCT_LIST = 11; // 请求用户的产品列表
	public static final int REQUEST_TYPE_GET_PRODUCT_DETAIL = 12; // 请求用户的产品详情信息
	
	public static final int REQUEST_TYPE_GET_EDIT_CARD = 16; // 名片编辑进入请求
	public static final int REQUEST_TYPE_EDIT_CARD_INFO = 17; // 名片编辑处理请求
	public static final int REQUEST_TYPE_CARD_SETTING = 18;  // 名片设置功能请求
	
	// http://alloong.com.cn/appUser/actionInsertProduct/
	public static final int REQUEST_TYPE_ADD_PRODUCT = 30;
	public static final int REQUEST_TYPE_KEYWORD_RAND = 31;
	// http://alloong.com.cn/appUser/actionSetProductStatus/
	public static final int REQUEST_TYPE_PRODUCT_STATUS = 32;
	// 修改产品 	http://alloong.com.cn/appUser/actionEditProduct/
	public static final int REQUEST_TYPE_PRODUCT_EDIT = 33;
	public static final int REQUEST_TYPE_PRODUCT_EDIT_COMMIT = 34;
	public static final int REQUEST_TYPE_PRODUCT_UPLOAD_IMAGE = 35;
	
	// 扣分统计日志    http://alloong.com.cn/appUser/actionGetKeywordIntegralLog/
	public static final int REQUEST_TYPE_KEYWORD_INTEGRAL_LOG = 35;
	// http://alloong.com.cn/appUser/actionRecharge/  
	public static final int REQUEST_TYPE_RECHARGE = 36;
	
	/** 脸值 请求的接口   */
	public static final int REQUEST_TYPE_GET_FACEVALUE = 50;
	/** 我的龙币   */
	public static final int REQUEST_TYPE_GET_INTEGRAL = 52;
	
	// 个人相关的接口  http://alloong.com.cn/appUser/actionUploadPhoto/
	public static final int REQUEST_TYPE_UPLOAD_PHOTO = 70;
	// 个人签到的接口  http://alloong.com.cn/appUser/actionSign/
	public static final int REQUEST_TYPE_SIGN = 71;
	
	/* 100 以上，设置的功能  */
	public static final int REQUEST_TYPE_SETTING_EDIT_PASSWORD = 101;
	public static final int REQUEST_TYPE_SETTING_EDIT_EMAIL = 102;
	
	public static final int REQUEST_TYPE_SETTING_FRIEND_GROUP = 104;
	public static final int REQUEST_TYPE_SETTING_FRIEND_GROUP_EDIT = 105;
	
	public static final String BASE_URL = "http://alloong.com.cn/";
	
	// 登陆的  URL     http://alloong.com.cn/appMember/actionLogin/
	public static final String LOGIN_URL = BASE_URL + "appMember/actionLogin/";
	// 注册的  URL     http://alloong.com.cn/appMember/actionRegister/
	public static final String REGISTER_URL = BASE_URL + "appMember/actionRegister/";
	
	// 搜索的  URL     http://alloong.com.cn/appSearch/actionIndex/
	public static final String SEARCH_URL = BASE_URL + "appSearch/actionIndex/";
	
	// 用户名片详细的  URL     http://alloong.com.cn/appCard/actionGetCardInfo/
	public static final String GET_CARD_INFO_URL = BASE_URL + "appCard/actionGetCardInfo/";
	
	// 用户产品列表的  URL     http://alloong.com.cn/appUser/actionGetProductList/
	public static final String PERSION_PRODUCT_LIST_URL = BASE_URL + "appUser/actionGetProductList/";
	// 用户产品详情的  URL     http://alloong.com.cn/appUser/actionGetProductDetail/
	public static final String PERSION_PRODUCT_DETAIL_URL = BASE_URL + "appUser/actionGetProductDetail/";
	
	// 用户名片编辑进入的  URL     http://alloong.com.cn/appCard/actionEditCardGetCardInfo/
	public static final String PERSION_EDIT_GET_CARD_URL = BASE_URL + "appCard/actionEditCardGetCardInfo/";
	// 用户名片编辑处理的  URL     http://alloong.com.cn/appCard/actionEditCardEditingCardInfo/
	public static final String PERSION_EDIT_CARD_URL = BASE_URL + "appCard/actionEditCardEditingCardInfo/";
	// 用户名片设置请求的 URL     http://alloong.com.cn/appCard/actionSetCard/
	public static final String PERSION_CARD_SETTING_URL = BASE_URL + "appCard/actionSetCard/";
	
	// http://alloong.com.cn/appUser/actionInsertProduct/
	public static final String PRODUCT_ADD_URL = BASE_URL + "appUser/actionInsertProduct/";
	// 关键字排名     http://alloong.com.cn/appUser/actionGetKeywordRand/
	public static final String PRODUCT_KEYWORD_RAND = BASE_URL + "appUser/actionGetKeywordRand/";
	// 对产品的操作 （发布，下架，删除）    http://alloong.com.cn/appUser/actionSetProductStatus/
	public static final String PRODUCT_STATUS = BASE_URL + "appUser/actionSetProductStatus/";
	// http://alloong.com.cn/appUser/actionEditProduct/
	public static final String PRODUCT_EDIT = BASE_URL + "appUser/actionEditProduct/";
	// http://alloong.com.cn/appUser/actionEditingProduct/
	public static final String PRODUCT_EDIT_COMMIT = BASE_URL + "appUser/actionEditingProduct/";
	// http://alloong.com.cn/appUser/actionaddProductImage/
	public static final String PRODUCT_UPLOAD_IMAGE = BASE_URL + "appUser/actionaddProductImage/";
		
		
	// 扣分统计日志    http://alloong.com.cn/appUser/actionGetKeywordIntegralLog/
	public static final String PRODUCT_KEYWORD_INTEGRAL_LOG = BASE_URL + "appUser/actionGetKeywordIntegralLog/";
	// http://alloong.com.cn/appUser/actionRecharge/
	public static final String PRODUCT_INTEGRAL_RECHARGE = BASE_URL + "appUser/actionRecharge/";

	/*********/
	// http://alloong.com.cn/appUser/actionGetFaceValue/
	public static final String GET_FACE_VALUE = BASE_URL + "appUser/actionGetFaceValue/";
	
	// 我的龙币 URL 	 http://alloong.com.cn/appUser/actionGetMyIntegralList/
	public static final String GET_INTEGRAL = BASE_URL + "appUser/actionGetMyIntegralList/";
	
	/** =======  个人相关的接口  ===== */
	// http://alloong.com.cn/appUser/actionUploadPhoto/
	public static final String USER_UPLOAD_PHOTO = BASE_URL + "appUser/actionUploadPhoto/";
	//  http://alloong.com.cn/appUser/actionSign/
	public static final String USER_SIGN = BASE_URL + "appUser/actionSign/";
	
	/* 100 以上，设置的功能  */
	// http://alloong.com.cn/appUser/actionEditPassword/  密码修改。。。
	public static final String SETTING_EDIT_PASSWORD = BASE_URL + "appUser/actionEditPassword/";
	// http://alloong.com.cn/appUser/actionModifyEmail/
	public static final String SETTING_EDIT_EMAIL = BASE_URL + "appUser/actionModifyEmail/";

	// http://alloong.com.cn/appSoftoreSetting/actionGetMyFriendGroup/
	public static final String SETTING_GET_FRIEND_GROUP = BASE_URL + "appSoftoreSetting/actionGetMyFriendGroup/";
	// http://alloong.com.cn/appSoftoreSetting/actionEditMyFriendGroup/
	public static final String SETTING_EDIT_FRIEND_GROUP = BASE_URL + "appSoftoreSetting/actionEditMyFriendGroup/";
	
}
