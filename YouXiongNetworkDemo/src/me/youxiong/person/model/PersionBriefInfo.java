package me.youxiong.person.model;

import java.io.Serializable;

/**
 * 查看 persion 简要信息的请求(AdapterMain)
 * @author mythwind
 *
 */
public class PersionBriefInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/*
	 		"user_id": "257",
            "real_name": "我是谁",
            "login_name": "我是谁",
            "photo": "http://alloong.com.cn/data/upload/header/20150107/pic_210_240/075875e198fde265fe81608f14680241.jpg",
            "card_title": "为人民服务",
            "face_value_real": "8485",
            "profession": "销售代表 大客户销售 葡萄牙语翻译",
            "distance": "100M"
	 */
	
	private int user_id;
	private String real_name;
	private String login_name;
	private String photo;
	private String card_title;
	private String face_value_real;
	private String profession;
	private String distance;
	
	private String key_word;
	private String create_time;
	private String photo_status;
	private String user_level;
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCard_title() {
		return card_title;
	}
	public void setCard_title(String card_title) {
		this.card_title = card_title;
	}
	public String getFace_value_real() {
		return face_value_real;
	}
	public void setFace_value_real(String face_value_real) {
		this.face_value_real = face_value_real;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public String getKey_word() {
		return key_word;
	}
	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getPhoto_status() {
		return photo_status;
	}
	public void setPhoto_status(String photo_status) {
		this.photo_status = photo_status;
	}
	public String getUser_level() {
		return user_level;
	}
	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}
	
}
