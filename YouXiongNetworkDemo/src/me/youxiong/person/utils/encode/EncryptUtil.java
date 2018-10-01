package me.youxiong.person.utils.encode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.text.TextUtils;
import android.util.Base64;

/**
 * 加密方式工具类
 * @author mythwind
 */
public class EncryptUtil {
	private static final int BUFFER_SIZE = 1024;

	public static String encryptMD5(String password) {
		return encryptMD5(password, "MD5");
	}
	
	public static String encryptMD5(String password, String algorithm) {
		String result = "";
		if(TextUtils.isEmpty(password) || TextUtils.isEmpty(algorithm)) {
			return result;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			byte[] bytes = messageDigest.digest(password.getBytes());
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				if((0xff & bytes[i]) < 0x10) {
					builder.append("0");
				}
				builder.append(Integer.toHexString(0xff & bytes[i]));
			}
			result = builder.toString();
		} catch (Exception e) {
			return "";
		}
		return result;
	}
	
	/**
	 * BASE64 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encryptBASE64(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		try {
			byte[] encode = str.getBytes("UTF-8");
			// base64 加密
			return new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * BASE64 解密
	 * 
	 * @param str
	 * @return
	 */
	public static String decryptBASE64(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		try {
			byte[] encode = str.getBytes("UTF-8");
			// base64 解密
			return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * GZIP 加密
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] encryptGZIP(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}

		try {
			// gzip压缩
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(baos);
			gzip.write(str.getBytes("UTF-8"));

			gzip.close();

			byte[] encode = baos.toByteArray();

			baos.flush();
			baos.close();

			// base64 加密
			return encode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * GZIP 解密
	 * 
	 * @param str
	 * @return
	 */
	public static String decryptGZIP(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		try {
			byte[] decode = str.getBytes("UTF-8");
			// gzip 解压缩
			ByteArrayInputStream bais = new ByteArrayInputStream(decode);
			GZIPInputStream gzip = new GZIPInputStream(bais);
			byte[] buf = new byte[BUFFER_SIZE];
			int len = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = gzip.read(buf, 0, BUFFER_SIZE)) != -1) {
				baos.write(buf, 0, len);
			}
			gzip.close();
			baos.flush();

			decode = baos.toByteArray();
			baos.close();
			return new String(decode, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 十六进制字符串 转换为 byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
		// return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * byte[] 转换为 十六进制字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
