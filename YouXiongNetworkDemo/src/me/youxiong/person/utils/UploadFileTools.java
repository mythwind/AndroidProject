package me.youxiong.person.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpStatus;

import android.content.Context;
import android.text.TextUtils;

public class UploadFileTools {
	
	public interface UploadFileListener {
		 
		public void onUploadProcess(int size);
	 
		public void onUploadFinished(String msg);
		
		public void onUploadFailed(String errMsg);
	}
	
	
	private static final int TIME_OUT = 10 * 1000; //超时时间 
	private static final int DEFAULT_BUFF_SIZE = 8192;
	private static final String TWO_HYPHENS = "--";
	private static final String BOUNDARY = "******";
	private static final String LINE_END = "\r\n";
	
	private UploadFileListener mUploadFileListener;
	private Map<String, String> mParams;
	@SuppressWarnings("unused")
	private Context mContext;
	
	public UploadFileTools(Context context, Map<String, String> params) {
		mContext = context;
		mParams = params;
	}
	public void setOnUploadFileListener(UploadFileListener uploadFileListener) {
		mUploadFileListener = uploadFileListener;
	}
	
	// http://www.beyondcompare.cn/android-shang-chuan-wen-jian-he-can-shu-dao-web-fu-wu-qi-php-jie-shou-bing-bao-cun-php.html
	/* 上传文件至Server，uploadUrl：接收文件的处理页面 */
	public void uploadFile(String uploadUrl, String name, String filepath) throws IOException {
		DataOutputStream dos = null;

		URL url = new URL(uploadUrl);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		// 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
		// 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
		httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
		httpURLConnection.setDoInput(true); // 允许输入输出流
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setUseCaches(false); // 不允许使用缓存
		httpURLConnection.setReadTimeout(TIME_OUT);
		httpURLConnection.setConnectTimeout(TIME_OUT);
		// 使用POST方法
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
		httpURLConnection.setRequestProperty("Charset", "UTF-8");
		httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

		dos = new DataOutputStream(httpURLConnection.getOutputStream());
		
		dos.writeBytes(getStringParams(mParams));
		if(!TextUtils.isEmpty(filepath)) {
			// 上传图片
			dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
			dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\""
					+ filepath.substring(filepath.lastIndexOf("/") + 1) + "\"" + LINE_END);
			dos.writeBytes(LINE_END);
	
			FileInputStream fis = new FileInputStream(filepath);
			byte[] buffer = new byte[DEFAULT_BUFF_SIZE]; // 8k
			int counter = 0;
			int count = 0;
			// 读取文件
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
				counter += count;
				if (null != mUploadFileListener) {
					mUploadFileListener.onUploadProcess(counter);
				}
			}
			fis.close();
		}

		dos.writeBytes(LINE_END);
		dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END); // 最后多出"--"作为结束
		dos.flush();

		/**
		 * 获取响应码 200=成功 当响应成功，获取响应的流
		 */
		if (httpURLConnection.getResponseCode() == HttpStatus.SC_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()), 8192);// 8k
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			if (null != mUploadFileListener) {
				mUploadFileListener.onUploadFinished(sb.toString());
			}
			reader.close();
		} else {
			if (null != mUploadFileListener) {
				mUploadFileListener.onUploadFailed(httpURLConnection.getResponseMessage());
			}
		}
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
		dos.close();
	}
	
	private String getStringParams(Map<String, String> map) {
		if(null == map || map.size() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			Logger.d("key= " + entry.getKey() + " and value= " + entry.getValue());
			sb.append(TWO_HYPHENS).append(BOUNDARY).append(LINE_END);
			sb.append("Content-Disposition: form-data; name=\"");
			sb.append(entry.getKey());
			sb.append("\"" + LINE_END);
			sb.append(LINE_END);
			sb.append(entry.getValue());
			sb.append(LINE_END);
		}
		return sb.toString();
	}
}
