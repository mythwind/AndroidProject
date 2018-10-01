package me.youxiong.person.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public final class DevicesInfo {
	private String manufacturer; // 厂商代码（从手机获取，如samsung）
	private String model; // 机型代码（从手机获取，如s4）
	
	private String romVersion; // Rom版本（从手机获取，缺省）
	private String clientMac; // 客户端MAC（从手机获取，如00:08:22:f6:bd:fb）
	private Integer ramSize; // ram容量（单位：M）（从手机获取，如482）
	private Integer romSize; // rom容量（单位：M）（从手机获取，如883）
	private Integer SDKVersion; // sdk版本（从手机获取，如19）
	
	private static DevicesInfo mDevicesInfo;
	
	private DevicesInfo(Context context) {
		init(context);
	}
	
	public static synchronized DevicesInfo getInstance(Context context) {
		if(mDevicesInfo == null) {
			mDevicesInfo = new DevicesInfo(context);
		}
		return mDevicesInfo;
	}
	
	@SuppressWarnings("deprecation")
	private void init(Context context) {
		setManufacturer(Build.MANUFACTURER); // 厂商代码
		setModel(Build.MODEL); // 机型代码
		setSDKVersion(Build.VERSION.SDK_INT); // sdk版本
		setRomVersion(Build.VERSION.RELEASE);// 获取rom版本
		
		// ram容量
		BufferedReader br = null;
		try {
			FileReader fr = new FileReader("/proc/meminfo");
			br = new BufferedReader(fr);
			String line = br.readLine();
			String[] arrayOfString = line.split("\\s+");
			int ramSize = Integer.valueOf(arrayOfString[1]).intValue() / 1024;
			setRamSize(ramSize);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// rom容量
		File root = Environment.getDataDirectory();
		StatFs sf = new StatFs(root.getPath());
		long total = sf.getBlockCount() * sf.getBlockSize();
		int romSize = (int) (total / 1024 / 1024);
		setRomSize(romSize);
		
		// 客户端mac
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifi != null) {
			WifiInfo info = wifi.getConnectionInfo();
			if (info != null) {
				setClientMac(info.getMacAddress());
			}
		}
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public String getRomVersion() {
		return romVersion;
	}

	public void setRomVersion(String romVersion) {
		this.romVersion = romVersion;
	}

	public String getClientMac() {
		return clientMac;
	}

	public void setClientMac(String clientMac) {
		this.clientMac = clientMac;
	}

	public Integer getRamSize() {
		return ramSize;
	}

	public void setRamSize(Integer ramSize) {
		this.ramSize = ramSize;
	}

	public Integer getRomSize() {
		return romSize;
	}

	public void setRomSize(Integer romSize) {
		this.romSize = romSize;
	}

	public Integer getSDKVersion() {
		return SDKVersion;
	}

	public void setSDKVersion(Integer sDKVersion) {
		SDKVersion = sDKVersion;
	}

}
