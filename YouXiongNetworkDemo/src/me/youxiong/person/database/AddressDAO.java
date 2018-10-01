package me.youxiong.person.database;

import java.util.List;

import me.youxiong.person.model.BaseDaoModel;

public interface AddressDAO {

	/**
	 * 根据 id 获取省的名称
	 * @param filePath
	 * @param provinceId
	 * @return
	 */
	public BaseDaoModel getProvinceById(String filePath, int provinceId);

	/**
	 * 根据 id 获取城市的名称
	 * @param filePath
	 * @param cityId
	 * @return
	 */
	public BaseDaoModel getCityById(String filePath, int cityId);

	
	/**
	 * 获取所以的省信息
	 * @param filePath
	 * @return
	 */
	public List<BaseDaoModel> getProvince(String filePath);

	/**
	 * 获取所以的城市信息
	 * @param filePath
	 * @param provinceId
	 * @return
	 */
	public List<BaseDaoModel> getCityByProvinceId(String filePath, int provinceId);

}
