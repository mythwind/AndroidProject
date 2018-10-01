package me.youxiong.person.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 职业选择的基本信息
 * @author mythwind
 */
public class BaseDaoModel implements Parcelable {

	private int id;
	private int parentId;
	private String name;
	private String zipcode;
	private boolean isChecked;
	private ArrayList<BaseDaoModel> subList;
	
	public BaseDaoModel() {
		
	}

	public BaseDaoModel(int id, int parentId, String name, String zipcode, boolean isChecked) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.zipcode = zipcode;
		this.isChecked = isChecked;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public ArrayList<BaseDaoModel> getSubList() {
		return subList;
	}
	public void setSubList(ArrayList<BaseDaoModel> subList) {
		this.subList = subList;
	}
	@Override
	public String toString() {
		return name;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return false;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		BaseDaoModel other = (BaseDaoModel) obj;
//		if (name == null) {
//			if (other.name != null) {
//				return false;
//			}
//		} else if (!name.equals(other.name)) {
//			return false;
//		}
//		if (id != other.id)
//			return false;
//		return true;
//	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(parentId);
		dest.writeString(name);
		dest.writeInt(isChecked ? 1 : 0);
		dest.writeList(subList);
	}
	
	public static final Parcelable.Creator<BaseDaoModel> CREATOR = new Parcelable.Creator<BaseDaoModel>() {
		public BaseDaoModel createFromParcel(Parcel in) {
			return new BaseDaoModel(in);
		}

		public BaseDaoModel[] newArray(int size) {
			return new BaseDaoModel[size];
		}
	};

	@SuppressWarnings("unchecked")
	private BaseDaoModel(Parcel in) {
		id = in.readInt();
		parentId = in.readInt();
		name = in.readString();
		isChecked = in.readInt() == 1;
		subList = in.readArrayList(BaseDaoModel.class.getClassLoader());
	}
	
}
