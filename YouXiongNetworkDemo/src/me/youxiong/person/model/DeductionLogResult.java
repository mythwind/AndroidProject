package me.youxiong.person.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class DeductionLogResult implements Parcelable {

	public List<PersionBriefInfo> keyword_integral_log_list = new ArrayList<PersionBriefInfo>();
	public String sum_integral;
	public String sum_month_integral;
	public String sum_week_integral;
	public String sum_day_integral;
	public String tottal_page;

	public DeductionLogResult() {
	}

	@SuppressWarnings("unchecked")
	private DeductionLogResult(Parcel in) {
		keyword_integral_log_list = in.readArrayList(IntegralInfoModel.class.getClassLoader());
		sum_integral = in.readString();
		sum_month_integral = in.readString();
		sum_week_integral = in.readString();
		sum_day_integral = in.readString();
		tottal_page = in.readString();
		
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(keyword_integral_log_list);
		dest.writeString(sum_integral);
		dest.writeString(sum_month_integral);
		dest.writeString(sum_week_integral);
		dest.writeString(sum_day_integral);
		dest.writeString(tottal_page);
	}

	public static final Parcelable.Creator<DeductionLogResult> CREATOR = new Parcelable.Creator<DeductionLogResult>() {
		public DeductionLogResult createFromParcel(Parcel in) {
			return new DeductionLogResult(in);
		}

		public DeductionLogResult[] newArray(int size) {
			return new DeductionLogResult[size];
		}
	};

	
}
