package com.muzhi.camerasdk.model;

import android.graphics.Bitmap;

public class ItemTextrueInfo {
	
	private int resId;
	private String name;
	private String assetPath;
	private Bitmap thumbnail;
	private boolean expand = false;
	
	public int getResId() {
		return resId;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAssetPath() {
		return assetPath;
	}
	public void setAssetPath(String assetPath) {
		this.assetPath = assetPath;
	}
	public Bitmap getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(Bitmap thumbnail) {
		this.thumbnail = thumbnail;
	}
	public boolean isExpand() {
		return expand;
	}
	public void setExpand(boolean expand) {
		this.expand = expand;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ItemTextrueInfo [resId=");
		builder.append(resId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", assetPath=");
		builder.append(assetPath);
		builder.append(", thumbnail=");
		builder.append(thumbnail);
		builder.append(", expand=");
		builder.append(expand);
		builder.append("]");
		return builder.toString();
	}
}
