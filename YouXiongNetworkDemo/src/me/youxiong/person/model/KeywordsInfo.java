package me.youxiong.person.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 关键字设置的实体类
 * 
 * @author mythwind
 * 
 */
public final class KeywordsInfo implements Parcelable {
	
	// "keywordId":"62","keywords":"\u4e13\u5bb6","deductCurrency":"12","clickCount":"1","ranking":2
	public String keywordId = "";
	public String keywords = "";
	public int deductCurrency = 0;
	public int ranking = 0;
	public int clickCount = 0;
	/*
	public int user_id;
	public int product_id;
	public int keyword_id;
	public String key_word;
	public int rank;
	public int hit_times; // 点击次数
	public int integral; // 
	public long create_time; // 创建时间
	public String activation;
	public String del;
	*/
	public KeywordsInfo() {
	}

	private KeywordsInfo(Parcel in) {
		keywordId = in.readString();
		keywords = in.readString();
		deductCurrency = in.readInt();
		ranking = in.readInt();
		clickCount = in.readInt();
		
		/*
		user_id = in.readInt();
		product_id = in.readInt();
		keyword_id = in.readInt();
		key_word = in.readString();
		rank = in.readInt();
		hit_times = in.readInt();
		integral = in.readInt();
		create_time = in.readLong();
		activation = in.readString();
		del = in.readString();
		*/
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(keywordId);
		dest.writeString(keywords);
		dest.writeInt(deductCurrency);
		dest.writeInt(ranking);
		dest.writeInt(clickCount);
		
		/*
		dest.writeInt(user_id);
		dest.writeInt(product_id);
		dest.writeInt(keyword_id);
		dest.writeString(key_word);
		dest.writeInt(rank);
		dest.writeInt(hit_times);
		dest.writeInt(integral);
		dest.writeLong(create_time);
		dest.writeString(activation);
		dest.writeString(del);
		*/
	}

	public static final Parcelable.Creator<KeywordsInfo> CREATOR = new Parcelable.Creator<KeywordsInfo>() {
		public KeywordsInfo createFromParcel(Parcel in) {
			return new KeywordsInfo(in);
		}

		public KeywordsInfo[] newArray(int size) {
			return new KeywordsInfo[size];
		}
	};
}
