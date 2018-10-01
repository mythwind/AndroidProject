package me.youxiong.person.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.text.TextUtils;

public class BasicDataItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private String code;
	private int count;
	private String enName;
	private String latitude;
	private String longitude;
	private String name;
	private ArrayList<BasicDataItem> sublist;

	public boolean equals(Object obj) {
		if ((obj instanceof BasicDataItem)) {
			try {
				if (((BasicDataItem) obj).getCode() == null) {
					if ((((BasicDataItem) obj).getLatitude() == null) && (!TextUtils.isEmpty(((BasicDataItem) obj).getName())))
						return ((BasicDataItem) obj).getName().equals(getName());
					if ((!((BasicDataItem) obj).getLatitude().equals(getLatitude())) || (!((BasicDataItem) obj).getLongitude().equals(getLongitude())))
						return false;
					return true;
				}
				return ((BasicDataItem) obj).getCode().equals(getCode());
			} catch (Exception localException) {
				return false;
			}
		}
		return super.equals(obj);
	}

	public String getCode() {
		return this.code;
	}

	public int getCount() {
		return this.count;
	}

	public String getEnName() {
		return this.enName;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<BasicDataItem> getSublist() {
		return this.sublist;
	}

	public void setCode(String paramString) {
		this.code = paramString;
	}

	public void setCount(int paramInt) {
		this.count = paramInt;
	}

	public void setEnName(String paramString) {
		this.enName = paramString;
	}

	public void setLatitude(String paramString) {
		this.latitude = paramString;
	}

	public void setLongitude(String paramString) {
		this.longitude = paramString;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setSublist(ArrayList<BasicDataItem> paramArrayList) {
		this.sublist = paramArrayList;
	}
}
