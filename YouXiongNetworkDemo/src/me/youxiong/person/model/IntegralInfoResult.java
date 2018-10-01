package me.youxiong.person.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 我的龙币的详细信息列表
 * @author mythwind
 *
 */
public class IntegralInfoResult implements Parcelable {

	public List<IntegralInfoModel> my_integral_list = new ArrayList<IntegralInfoModel>();
	public String tottal_page;
	public String total_integral;
	public String one_yuan_buy_integral;
	
	public IntegralInfoResult() {
	}

	@SuppressWarnings("unchecked")
	private IntegralInfoResult(Parcel in) {
		my_integral_list = in.readArrayList(IntegralInfoModel.class.getClassLoader());
		tottal_page = in.readString();
		total_integral = in.readString();
		one_yuan_buy_integral = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(my_integral_list);
		dest.writeString(tottal_page);
		dest.writeString(total_integral);
		dest.writeString(one_yuan_buy_integral);
	}

	public static final Parcelable.Creator<IntegralInfoResult> CREATOR = new Parcelable.Creator<IntegralInfoResult>() {
		public IntegralInfoResult createFromParcel(Parcel in) {
			return new IntegralInfoResult(in);
		}

		public IntegralInfoResult[] newArray(int size) {
			return new IntegralInfoResult[size];
		}
	};

	@Override
	public String toString() {
		return "IntegralInfoResult [my_integral_list=" + my_integral_list + ", tottal_page=" + tottal_page
				+ ", total_integral=" + total_integral + ", one_yuan_buy_integral=" + one_yuan_buy_integral + "]";
	}
	
}
