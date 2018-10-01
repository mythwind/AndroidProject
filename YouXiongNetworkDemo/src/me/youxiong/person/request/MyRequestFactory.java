package me.youxiong.person.request;

import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

public class MyRequestFactory extends AbstractRequest {
	
	public static MyStringRequest execute(final MyRequestOperation operation, VolleyResponseListener listener) {
		String url = getRequestUrlByType(operation.type);
		// Logger.e("MyRequestFactory ===== url=" + url);
		if(TextUtils.isEmpty(url)) {
			Log.e("MyRequestFactory", " ****** url is empty!");
		}
		MyStringRequest request = new MyStringRequest(
				url, 
				operation.type, 
				listener.getResponseListener(), 
				listener.getResponseErrorListener()) {
			/*
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
				return headers;
			}
			*/
			@Override
			protected Map<String, String> getParams() {
				return operation.params;
			}
		};
		return request;
	}
	
	private static String getRequestUrlByType(int type) {
		String url = "";
		switch (type) {
		case REQUEST_TYPE_GET_LOGIN:
			url = LOGIN_URL;
			break;
		case REQUEST_TYPE_GET_REGISTE:
			url = REGISTER_URL;
			break;
		case REQUEST_TYPE_GET_SEARCH_LIST:
			url = SEARCH_URL;
			break;
			
		case REQUEST_TYPE_GET_CARD_INFO:
			url = GET_CARD_INFO_URL;
			break;
		case REQUEST_TYPE_GET_PRODUCT_LIST:
			url = PERSION_PRODUCT_LIST_URL;
			break;
		case REQUEST_TYPE_GET_PRODUCT_DETAIL:
			url = PERSION_PRODUCT_DETAIL_URL;
			break;
			
		case REQUEST_TYPE_GET_EDIT_CARD: // 16
			url = PERSION_EDIT_GET_CARD_URL;
			break;
		case REQUEST_TYPE_EDIT_CARD_INFO: // 17
			url = PERSION_EDIT_CARD_URL;
			break;
		case REQUEST_TYPE_CARD_SETTING: // 18 名片设置
			url = PERSION_CARD_SETTING_URL;
			break;
		case REQUEST_TYPE_ADD_PRODUCT: // 30
			url = PRODUCT_ADD_URL;
			break;
		case REQUEST_TYPE_KEYWORD_RAND: // 31
			url = PRODUCT_KEYWORD_RAND;
			break;
		case REQUEST_TYPE_PRODUCT_STATUS: // 32 用户下产品的操作
			url = PRODUCT_STATUS;
			break;
		case REQUEST_TYPE_PRODUCT_EDIT:  // 33 修改产品
			url = PRODUCT_EDIT;
			break;
		case REQUEST_TYPE_PRODUCT_EDIT_COMMIT:
			url = PRODUCT_EDIT_COMMIT;
			break;
		case REQUEST_TYPE_KEYWORD_INTEGRAL_LOG: // 35 扣分统计
			url = PRODUCT_KEYWORD_INTEGRAL_LOG;
			break;
		case REQUEST_TYPE_RECHARGE: // 36 进入充值界面
			url = PRODUCT_INTEGRAL_RECHARGE;
			break;
			
		case REQUEST_TYPE_GET_FACEVALUE: // 50
			url = GET_FACE_VALUE;
			break;
		case REQUEST_TYPE_GET_INTEGRAL:
			url = GET_INTEGRAL;
			break;
			
		case REQUEST_TYPE_UPLOAD_PHOTO: // 70 上传个人图像
			url = USER_UPLOAD_PHOTO;
			break;
		case REQUEST_TYPE_SIGN:
			url = USER_SIGN;
			break;
		case REQUEST_TYPE_SETTING_EDIT_PASSWORD:
			url = SETTING_EDIT_PASSWORD;
			break;
		case REQUEST_TYPE_SETTING_EDIT_EMAIL:// 102
			url = SETTING_EDIT_EMAIL;
			break;
			
		case REQUEST_TYPE_SETTING_FRIEND_GROUP:// 104
			url = SETTING_GET_FRIEND_GROUP;
			break;
		case REQUEST_TYPE_SETTING_FRIEND_GROUP_EDIT: // 105 商友分类修改
			url = SETTING_EDIT_FRIEND_GROUP;
			break;
		}
		return url;
	}
}
