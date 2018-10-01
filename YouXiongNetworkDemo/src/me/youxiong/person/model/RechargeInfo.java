package me.youxiong.person.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 进入充值页面
 * 
 * @author mythwind
 * 
 */
public final class RechargeInfo implements Parcelable {
	
	public int integral = 0;
	public String money;
	
	public RechargeInfo() {
	}

	private RechargeInfo(Parcel in) {
		integral = in.readInt();
		money = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(integral);
		dest.writeString(money);
	}

	public static final Parcelable.Creator<RechargeInfo> CREATOR = new Parcelable.Creator<RechargeInfo>() {
		public RechargeInfo createFromParcel(Parcel in) {
			return new RechargeInfo(in);
		}

		public RechargeInfo[] newArray(int size) {
			return new RechargeInfo[size];
		}
	};
}
