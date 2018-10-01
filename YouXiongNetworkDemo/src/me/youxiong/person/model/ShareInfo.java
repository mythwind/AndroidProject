package me.youxiong.person.model;

import android.os.Parcel;
import android.os.Parcelable;

/** 
 * 分享信息
 * @author mythwind
 * 
 */
public final class ShareInfo implements Parcelable {

	public int id;
	public String name;
	public String linkUrl;
	public String desc;
	public String bmpPathSaved;
	public boolean needFixUrl;

	public ShareInfo() {
	}

	private ShareInfo(Parcel in) {
		id = in.readInt();
		name = in.readString();
		linkUrl = in.readString();
		desc = in.readString();
		bmpPathSaved = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(linkUrl);
		dest.writeString(desc);
		dest.writeString(bmpPathSaved);
	}

	public static final Parcelable.Creator<ShareInfo> CREATOR = new Parcelable.Creator<ShareInfo>() {
		public ShareInfo createFromParcel(Parcel in) {
			return new ShareInfo(in);
		}

		public ShareInfo[] newArray(int size) {
			return new ShareInfo[size];
		}
	};
}
