package me.youxiong.person.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 关键字设置的实体类
 * 
 * @author mythwind
 * 
 */
public final class FriendGroupInfo implements Parcelable {
	
	public int group_id;
	public String group_name;
	
	public FriendGroupInfo() {
	}

	private FriendGroupInfo(Parcel in) {
		group_id = in.readInt();
		group_name = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(group_id);
		dest.writeString(group_name);
	}

	public static final Parcelable.Creator<FriendGroupInfo> CREATOR = new Parcelable.Creator<FriendGroupInfo>() {
		public FriendGroupInfo createFromParcel(Parcel in) {
			return new FriendGroupInfo(in);
		}

		public FriendGroupInfo[] newArray(int size) {
			return new FriendGroupInfo[size];
		}
	};
	
}
