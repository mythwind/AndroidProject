package me.youxiong.person.utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	/**
	 * POST 提交方式
	 * 
	 * @param posturl需要的请求地址
	 * @param nameValuePairs传递的参数集合
	 *            ，把参数都放到这个集合内上传
	 * @return 返回成功后服务器封装的数据，如果请求失败这是返回null
	 */

	public static String postData(String posturl, List<NameValuePair> nameValuePairs) {
		String strResult = null;
		try {
			HttpParams parms = new BasicHttpParams();
			parms.setParameter("content_Type", "application/json;charset=utf-8");
			HttpClient httpclient = new DefaultHttpClient(parms);
			// 你的URL
			HttpPost httppost = new HttpPost(posturl);
			httppost.addHeader("content_Type", "application/json;charset=utf-8");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			
			HttpResponse response = httpclient.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				/* 读返回数据 */
				strResult = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strResult;
	}

}
