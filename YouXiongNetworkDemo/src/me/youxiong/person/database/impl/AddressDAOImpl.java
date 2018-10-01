package me.youxiong.person.database.impl;

import java.util.ArrayList;
import java.util.List;

import me.youxiong.person.database.AddressDAO;
import me.youxiong.person.model.BaseDaoModel;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressDAOImpl implements AddressDAO {
	
	private SQLiteDatabase getAddressDatabase(String path) {
		return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READONLY);
	}
	
	// 获取省的地址列表,//file-->数据库文件
	@Override
	public List<BaseDaoModel> getProvince(String filePath) {
		// SELECT province_id, province_name FROM t_province
		String sql = "SELECT province_id, province_name FROM t_province";
		SQLiteDatabase db = getAddressDatabase(filePath);
		List<BaseDaoModel> provinceList = new ArrayList<BaseDaoModel>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			BaseDaoModel baseDaoModel;
			while (cursor.moveToNext()) {
				baseDaoModel = new BaseDaoModel();
				baseDaoModel.setId(cursor.getInt(cursor.getColumnIndex("province_id")));
				baseDaoModel.setName(cursor.getString(cursor.getColumnIndex("province_name")));
				provinceList.add(baseDaoModel);
			}
		} catch (Exception e) {
			Log.e("WineStock", "getProvince:" + e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				db.close();
			}
		}
		return provinceList;
	}

	// 获取对应省下面城市的列表,//file-->数据库文件,id-->指对应省的ID
	@Override
	public List<BaseDaoModel> getCityByProvinceId(String filePath, int provinceId) {
		// SELECT city_id, city_name, zip_code FROM t_city WHERE province_id=1
		String sql = "SELECT city_id, city_name, zip_code FROM t_city WHERE province_id=" + provinceId;
		SQLiteDatabase db = getAddressDatabase(filePath);
		List<BaseDaoModel> cityList = new ArrayList<BaseDaoModel>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			BaseDaoModel baseDaoModel;
			while (cursor.moveToNext()) {
				baseDaoModel = new BaseDaoModel();
				baseDaoModel.setId(cursor.getInt(cursor.getColumnIndex("city_id")));
				baseDaoModel.setName(cursor.getString(cursor.getColumnIndex("city_name")));
				cityList.add(baseDaoModel);
			}
		} catch (Exception e) {
			Log.e("WineStock", "getCity:" + e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				db.close();
			}
		}
		return cityList;
	}

	@Override
	public BaseDaoModel getProvinceById(String filePath, int provinceId) {
		// SELECT province_id, province_name FROM t_province WHERE province_id=11
		String sql = "SELECT province_id, province_name FROM t_province WHERE province_id=" + provinceId;
		SQLiteDatabase db = getAddressDatabase(filePath);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			BaseDaoModel baseDaoModel;
			while (cursor.moveToNext()) {
				baseDaoModel = new BaseDaoModel();
				baseDaoModel.setId(cursor.getInt(cursor.getColumnIndex("province_id")));
				baseDaoModel.setName(cursor.getString(cursor.getColumnIndex("province_name")));
				return baseDaoModel;
			}
		} catch (Exception e) {
			Log.e("WineStock", "getProvinceById:" + e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				db.close();
			}
		}
		return null;
	}

	@Override
	public BaseDaoModel getCityById(String filePath, int cityId) {
		// SELECT city_id, city_name, province_id, zip_code FROM t_city WHERE city_id=11
		String sql = "SELECT city_id, city_name, province_id, zip_code FROM t_city WHERE city_id=" + cityId;
		SQLiteDatabase db = getAddressDatabase(filePath);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			BaseDaoModel baseDaoModel;
			while (cursor.moveToNext()) {
				baseDaoModel = new BaseDaoModel();
				baseDaoModel.setId(cursor.getInt(cursor.getColumnIndex("city_id")));
				baseDaoModel.setParentId(cursor.getInt(cursor.getColumnIndex("province_id")));
				baseDaoModel.setName(cursor.getString(cursor.getColumnIndex("city_name")));
				baseDaoModel.setZipcode(cursor.getString(cursor.getColumnIndex("zip_code")));
				return baseDaoModel;
			}
		} catch (Exception e) {
			Log.e("WineStock", "getProvinceById:" + e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				db.close();
			}
		}
		return null;
	}
}
