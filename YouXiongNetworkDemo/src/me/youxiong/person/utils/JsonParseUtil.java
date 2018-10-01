package me.youxiong.person.utils;

import java.util.ArrayList;

import me.youxiong.person.model.DeductionLogResult;
import me.youxiong.person.model.FriendGroupList;
import me.youxiong.person.model.IntegralInfoResult;
import me.youxiong.person.model.PersionBriefInfo;
import me.youxiong.person.model.PersionCardInfo;
import me.youxiong.person.model.PersionInfoResultValue;
import me.youxiong.person.model.PersonDetailInfo;
import me.youxiong.person.model.ProductDetailInfo;
import me.youxiong.person.model.ProductListInfo;
import me.youxiong.person.model.RechargeInfo;
import me.youxiong.person.model.ReturnJsonModel;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonParseUtil {
	private static final String TAG_ISSUCCESS = "IsSuccess";
	private static final String TAG_STATUSCODE = "StatusCode";
	private static final String TAG_DESCRIPTION = "Description";
	private static final String TAG_RETURNVALUE = "ReturnValue";

	/**
	 * 解析 json 数据，获取字符串
	 * @param jsonString
	 * @return
	 */
	public static ReturnJsonModel parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim())) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			boolean isSuccess = jsonObject.getBoolean(TAG_ISSUCCESS);
			int statusCode = jsonObject.getInt(TAG_STATUSCODE);
			String description = jsonObject.getString(TAG_DESCRIPTION);
			String returnValue = jsonObject.getString(TAG_RETURNVALUE);

			ReturnJsonModel result = new ReturnJsonModel();
			result.setIsSuccess(isSuccess);
			result.setStatusCode(statusCode);
			result.setDescription(description);
			// returnValue = returnValue.replaceAll("\\[([^\\]]*)\\]", "$1");
			result.setReturnValue(returnValue);
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取登陆信息
	 * @param jsonString
	 * @return
	 */
	public static PersonDetailInfo getLoginResult(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim()))
			return null;
		try {
			Gson gson = new Gson();
			PersonDetailInfo persionInfo = (PersonDetailInfo) gson.fromJson(jsonString.trim(), PersonDetailInfo.class);
			return persionInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取搜索结果并解析。
	 * @param jsonString
	 * @return
	 */
	public static ArrayList<PersionBriefInfo> getSearch(String jsonString) {
		if (TextUtils.isEmpty(jsonString) || TextUtils.isEmpty(jsonString.trim())) {
			return null;
		}
		try {
			Gson gson = new Gson();
			ArrayList<PersionBriefInfo> briefInfos = gson.fromJson(jsonString.trim(), new TypeToken<ArrayList<PersionBriefInfo>>(){}.getType());

            // PersionBriefInfo briefInfo = (PersionBriefInfo) gson.fromJson(jsonString.trim(), PersionBriefInfo.class);
			Logger.i("JsonParseUtil----briefInfos=" + briefInfos);
			return briefInfos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static PersionCardInfo getPersionCardInfo(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim()))
			return null;
		try {
			Gson gson = new Gson();
			PersionCardInfo persionCardInfo = (PersionCardInfo) gson.fromJson(jsonString.trim(), PersionCardInfo.class);

			Logger.i("JsonParseUtil----persionCardInfo=" + persionCardInfo);
			return persionCardInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static PersionInfoResultValue getPersionInfo(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim()))
			return null;
		try {
			Gson gson = new Gson();
			PersionInfoResultValue persionResultValue = (PersionInfoResultValue) gson.fromJson(jsonString.trim(), PersionInfoResultValue.class);

			Logger.i("JsonParseUtil----persionResultValue=" + persionResultValue);
			return persionResultValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取用户产品列表信息
	 * @param jsonString
	 * @return
	 */
	public static ProductListInfo getPersionProductList(String jsonString) {
		Logger.i("JsonParseUtil----getPersionProductList.jsonString=" + jsonString);
		
		if (TextUtils.isEmpty(jsonString.trim()))
			return null;
		try {
			Gson gson = new Gson();
			ProductListInfo productList = gson.fromJson(jsonString.trim(), ProductListInfo.class);
			Logger.i("JsonParseUtil----productList=" + productList);
			return productList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public static ArrayList<ProductDetailInfo> getPersionProductList(String jsonString) {
//		if (TextUtils.isEmpty(jsonString.trim()))
//			return null;
//		try {
//			Gson gson = new Gson();
//			
//			ArrayList<ProductDetailInfo> productLists = gson.fromJson(jsonString.trim(), new TypeToken<ArrayList<ProductDetailInfo>>(){}.getType());
//
//			Logger.i("JsonParseUtil----productList=" + productLists);
//			return productLists;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	"product_status": "2",
    "product_trades": "Array ",
    "product_icon": "http://alloong.com.cn/",
    "original_product_price": "12.00",
    "product_price": "11.00",
    "product_content": "姐",
    "product_name": "姐",
	 */
	public static ProductDetailInfo getPersionProductDetail(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim()))
			return null;
		try {
			Gson gson = new Gson();
			ProductDetailInfo detailInfo = (ProductDetailInfo) gson.fromJson(jsonString.trim(), ProductDetailInfo.class);

			Logger.i("JsonParseUtil----detailInfo=" + detailInfo);
			return detailInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	public static DeductionLogResult getProductDetailEditInfo(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim()))
			return null;
		try {
			Gson gson = new Gson();
			DeductionLogResult info = (DeductionLogResult) gson.fromJson(jsonString.trim(), DeductionLogResult.class);

			Logger.i("JsonParseUtil----IntegralInfoResult=" + info);
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	public static IntegralInfoResult getIntegralInfoResult(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim()))
			return null;
		try {
			Gson gson = new Gson();
			IntegralInfoResult info = (IntegralInfoResult) gson.fromJson(jsonString.trim(), IntegralInfoResult.class);

			Logger.i("JsonParseUtil----IntegralInfoResult=" + info);
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static DeductionLogResult getDeductionIntegralLog(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim()))
			return null;
		try {
			Gson gson = new Gson();
			DeductionLogResult info = (DeductionLogResult) gson.fromJson(jsonString.trim(), DeductionLogResult.class);

			Logger.i("JsonParseUtil----IntegralInfoResult=" + info);
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 进入充值页面
	 * @param jsonString
	 * @return
	 */
	public static RechargeInfo getRechargeInfo(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim()))
			return null;
		try {
			Gson gson = new Gson();
			RechargeInfo rechargeInfo = (RechargeInfo) gson.fromJson(jsonString.trim(), RechargeInfo.class);

			Logger.i("JsonParseUtil----getRechargeInfo=" + rechargeInfo);
			return rechargeInfo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static FriendGroupList getFriendGroup(String jsonString) {
		if (TextUtils.isEmpty(jsonString.trim())) {
			return null;
		}
		try {
			Gson gson = new Gson();
			FriendGroupList groupList = (FriendGroupList) gson.fromJson(jsonString.trim(), FriendGroupList.class);
			return groupList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
