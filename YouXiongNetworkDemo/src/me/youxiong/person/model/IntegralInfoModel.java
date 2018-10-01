package me.youxiong.person.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 我的龙币的详细信息列表
 * @author mythwind
 *
 */
public class IntegralInfoModel implements Parcelable {
	public String remark;
	public String change_value;
	public String change_time;
	

	public IntegralInfoModel() {
	}

	private IntegralInfoModel(Parcel in) {
		remark = in.readString();
		change_value = in.readString();
		change_time = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(remark);
		dest.writeString(change_value);
		dest.writeString(change_time);
	}

	public static final Parcelable.Creator<IntegralInfoModel> CREATOR = new Parcelable.Creator<IntegralInfoModel>() {
		public IntegralInfoModel createFromParcel(Parcel in) {
			return new IntegralInfoModel(in);
		}

		public IntegralInfoModel[] newArray(int size) {
			return new IntegralInfoModel[size];
		}
	};
	
	@Override
	public String toString() {
		return "IntegralInfoModel [remark=" + remark + ", change_value=" + change_value + ", change_time="
				+ change_time + "]";
	}
	
	
}
