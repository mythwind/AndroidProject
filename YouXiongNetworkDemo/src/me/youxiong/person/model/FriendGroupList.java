package me.youxiong.person.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 关键字设置的实体类
 * 
 * @author mythwind
 * 
 */
public final class FriendGroupList implements Parcelable {
	
	public List<FriendGroupInfo> my_friend_group_list = new ArrayList<FriendGroupInfo>();
	
	
	public FriendGroupList() {
	}

	@SuppressWarnings("unchecked")
	private FriendGroupList(Parcel in) {
		my_friend_group_list = in.readArrayList(FriendGroupInfo.class.getClassLoader());
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(my_friend_group_list);
	}

	public static final Parcelable.Creator<FriendGroupList> CREATOR = new Parcelable.Creator<FriendGroupList>() {
		public FriendGroupList createFromParcel(Parcel in) {
			return new FriendGroupList(in);
		}

		public FriendGroupList[] newArray(int size) {
			return new FriendGroupList[size];
		}
	};
	
}
