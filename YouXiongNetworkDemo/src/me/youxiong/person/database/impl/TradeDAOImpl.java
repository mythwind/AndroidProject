package me.youxiong.person.database.impl;

import java.util.ArrayList;
import java.util.List;

import me.youxiong.person.database.TradeDAO;
import me.youxiong.person.model.BaseDaoModel;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class TradeDAOImpl implements TradeDAO {

	private static final String TAG = TradeDAOImpl.class.getSimpleName();
	
	private SQLiteDatabase getTradeDatabase(String path) {
		return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READONLY);
	}
	@Override
	public List<BaseDaoModel> getTradeGroupList(String filePath) {
		String sql = "SELECT trade_id, trade_name FROM trade";
		SQLiteDatabase db = getTradeDatabase(filePath);
		List<BaseDaoModel> tradeList = new ArrayList<BaseDaoModel>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			BaseDaoModel baseDaoModel;
			while (cursor.moveToNext()) {
				baseDaoModel = new BaseDaoModel();
				baseDaoModel.setId(cursor.getInt(cursor.getColumnIndex("trade_id")));
				baseDaoModel.setName(cursor.getString(cursor.getColumnIndex("trade_name")));
				
				tradeList.add(baseDaoModel);
			}
		} catch (Exception e) {
			Log.e(TAG, "getTradeList:" + e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				db.close();
			}
		}
		return tradeList;
	}
	@Override
	public List<BaseDaoModel> getTradeList(String filePath) {
		String sql = "SELECT trade_id, trade_name FROM trade";
		SQLiteDatabase db = getTradeDatabase(filePath);
		List<BaseDaoModel> tradeList = new ArrayList<BaseDaoModel>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			BaseDaoModel baseDaoModel;
			while (cursor.moveToNext()) {
				baseDaoModel = new BaseDaoModel();
				baseDaoModel.setId(cursor.getInt(cursor.getColumnIndex("trade_id")));
				baseDaoModel.setName(cursor.getString(cursor.getColumnIndex("trade_name")));
				
				ArrayList<BaseDaoModel> subList = (ArrayList<BaseDaoModel>) getTradeDetailListByParentId(String.valueOf(baseDaoModel.getId()), filePath);
				baseDaoModel.setSubList(subList);
				
				tradeList.add(baseDaoModel);
			}
		} catch (Exception e) {
			Log.e(TAG, "getTradeList:" + e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				db.close();
			}
		}
		return tradeList;
	}

	@Override
	public List<BaseDaoModel> getTradeDetailListByParentId(String parentId, String filePath) {
		String sql = "SELECT trade_id, trade_name, parent_id FROM trade_classify WHERE parent_id=?";
		SQLiteDatabase db = getTradeDatabase(filePath);
		List<BaseDaoModel> tradeDetailList = new ArrayList<BaseDaoModel>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, new String[] { parentId });
			BaseDaoModel baseDaoModel;
			while (cursor.moveToNext()) {
				baseDaoModel = new BaseDaoModel();
				baseDaoModel.setId(cursor.getInt(cursor.getColumnIndex("trade_id")));
				baseDaoModel.setParentId(cursor.getInt(cursor.getColumnIndex("parent_id")));
				baseDaoModel.setName(cursor.getString(cursor.getColumnIndex("trade_name")));
				tradeDetailList.add(baseDaoModel);
			}
		} catch (Exception e) {
			Log.e(TAG, "getTradeList:" + e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				db.close();
			}
		}
		return tradeDetailList;
	}
	public List<BaseDaoModel> getTradeDetailByIds(String[] tradeIds, String filePath) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tradeIds.length; i++) {
			String tradeName = tradeIds[i].trim();
			if(!TextUtils.isEmpty(tradeName)) {
				sb.append(tradeName);
				if(i < tradeIds.length - 1) {
					sb.append(",");
				}
			}
		}
		String sql = "SELECT trade_id, trade_name, parent_id FROM trade_classify WHERE trade_id IN (" + sb.toString() + ")";
		SQLiteDatabase db = getTradeDatabase(filePath);
		List<BaseDaoModel> tradeDetailList = new ArrayList<BaseDaoModel>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, null);
			BaseDaoModel baseDaoModel;
			while (cursor.moveToNext()) {
				baseDaoModel = new BaseDaoModel();
				baseDaoModel.setId(cursor.getInt(cursor.getColumnIndex("trade_id")));
				baseDaoModel.setParentId(cursor.getInt(cursor.getColumnIndex("parent_id")));
				baseDaoModel.setName(cursor.getString(cursor.getColumnIndex("trade_name")));
				tradeDetailList.add(baseDaoModel);
			}
		} catch (Exception e) {
			Log.e(TAG, "getTradeList:" + e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				db.close();
			}
		}
		return tradeDetailList;
	}
	@Override
	public BaseDaoModel getTradeDetailById(String tradeId, String filePath) {
		String sql = "SELECT trade_id, trade_name, parent_id FROM trade_classify WHERE trade_id=?";
		SQLiteDatabase db = getTradeDatabase(filePath);
		BaseDaoModel baseDaoModel = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, new String[] { tradeId });
			if (cursor.moveToFirst()) {
				baseDaoModel = new BaseDaoModel();
				baseDaoModel.setId(cursor.getInt(cursor.getColumnIndex("trade_id")));
				baseDaoModel.setParentId(cursor.getInt(cursor.getColumnIndex("parent_id")));
				baseDaoModel.setName(cursor.getString(cursor.getColumnIndex("trade_name")));
				return baseDaoModel;
			}
		} catch (Exception e) {
			Log.e(TAG, "getTradeList:" + e.getMessage());
		} finally {
			if (null != cursor) {
				cursor.close();
			}
			if (null != db) {
				db.close();
			}
		}
		return baseDaoModel;
	}

}
