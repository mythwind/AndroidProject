package me.youxiong.person.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 用户产品列表详情
 * @author mythwind
 */
@SuppressWarnings("serial")
public class ProductDetailInfo implements Serializable {
	/*
	 {"product_icon":"http:\/\/alloong.com.cn\/",
	 
	 "keywords":[
	 {"key_word":"手机",
	 "rank":3,
	 "hit_times":"0",
	 "product_id":"129",
	 "keyword_id":"1244",
	 "create_time":"1430457488",
	 "del":"0",
	 "user_id":"2",
	 "activation":"1",
	 "integral":"10"}
	 ],
	 "original_product_price":"999.00",
	 "product_id":"129",
	 "product_price":"1999.00",
	 "product_content":"手机测试介绍",
	 "trade_id":"0",
	 "product_name":"手机测试",
	 "product_catetory":"6",
	 "sub_trade_id":"0",
	 "deduct_integral":"0",
	 "user_id":"2",
	 "integral":"22378"}
	
	 */
	private String user_id;  // 用户 ID
	private String product_id;
	private String product_name;  // 产品名称
	private String product_icon;  // 产品图片地址
	private int product_status;  //////////// 产品状态   product_status,0-- 初始，1-- 发布，2--下架,3--删除
	private String product_catetory; // 产品类别
	private String product_content;  // 产品描述
	private String product_price;   // 产品价格
	private String original_product_price;   // 原始价格
	private String product_trades;  // 
	private String profession_value;  // 职业专业
	
	// 
	private String trade_id;
	private String sub_trade_id;
	//
	
	private String deduct_integral; // 关键字预扣总龙币数
	private String integral; // 账户总龙币
	private ArrayList<KeywordsInfo> keywords;
//	private String keywords; // 关键字
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_icon() {
		return product_icon;
	}
	public void setProduct_icon(String product_icon) {
		this.product_icon = product_icon;
	}
	public int getProduct_status() {
		return product_status;
	}
	public void setProduct_status(int product_status) {
		this.product_status = product_status;
	}
	public String getProduct_catetory() {
		return product_catetory;
	}
	public void setProduct_catetory(String product_catetory) {
		this.product_catetory = product_catetory;
	}
	public String getProduct_content() {
		return product_content;
	}
	public void setProduct_content(String product_content) {
		this.product_content = product_content;
	}
	public String getProduct_price() {
		return product_price;
	}
	public void setProduct_price(String product_price) {
		this.product_price = product_price;
	}
	public String getOriginal_product_price() {
		return original_product_price;
	}
	public void setOriginal_product_price(String original_product_price) {
		this.original_product_price = original_product_price;
	}
	public String getProduct_trades() {
		return product_trades;
	}
	public void setProduct_trades(String product_trades) {
		this.product_trades = product_trades;
	}
	public String getProfession_value() {
		return profession_value;
	}
	public void setProfession_value(String profession_value) {
		this.profession_value = profession_value;
	}
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	public String getSub_trade_id() {
		return sub_trade_id;
	}
	public void setSub_trade_id(String sub_trade_id) {
		this.sub_trade_id = sub_trade_id;
	}
	public String getDeduct_integral() {
		return deduct_integral;
	}
	public void setDeduct_integral(String deduct_integral) {
		this.deduct_integral = deduct_integral;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public ArrayList<KeywordsInfo> getKeywords() {
		return keywords;
	}
	public void setKeywords(ArrayList<KeywordsInfo> keywords) {
		this.keywords = keywords;
	}
	
//	public String getKeywords() {
//		return keywords;
//	}
//	public void setKeywords(String keywords) {
//		this.keywords = keywords;
//	}
	
}
