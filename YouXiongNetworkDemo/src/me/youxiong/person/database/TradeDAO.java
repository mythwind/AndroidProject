package me.youxiong.person.database;

import java.util.List;

import me.youxiong.person.model.BaseDaoModel;

public interface TradeDAO {
	
	public List<BaseDaoModel> getTradeGroupList(String filePath);
	
	/**
	 * 获取到职业分类信息列表
	 * @param filePath
	 * @return
	 */
	public List<BaseDaoModel> getTradeList(String filePath);
	
	/**
	 * 根据分类的 ID 获取到具体职业的信息列表
	 * @param filePath
	 * @return
	 */
	public List<BaseDaoModel> getTradeDetailListByParentId(String parentId, String filePath);
	
	public List<BaseDaoModel> getTradeDetailByIds(String[] tradeIds, String filePath);
	
	public BaseDaoModel getTradeDetailById(String tradeId, String filePath);
	
}
