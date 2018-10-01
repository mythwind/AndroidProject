package me.youxiong.person.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户产品列表详情
 * @author mythwind
 */
public class ProductListInfo implements Parcelable {
	
	public ArrayList<ProductDetailInfo> my_product_list = new ArrayList<ProductDetailInfo>();
	public String tottal_page;
	

	public ProductListInfo() {
	}

	@SuppressWarnings("unchecked")
	private ProductListInfo(Parcel in) {
		my_product_list = in.readArrayList(ProductDetailInfo.class.getClassLoader());
		tottal_page = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(my_product_list);
		dest.writeString(tottal_page);
	}

	public static final Parcelable.Creator<ProductListInfo> CREATOR = new Parcelable.Creator<ProductListInfo>() {
		public ProductListInfo createFromParcel(Parcel in) {
			return new ProductListInfo(in);
		}

		public ProductListInfo[] newArray(int size) {
			return new ProductListInfo[size];
		}
	};

}
