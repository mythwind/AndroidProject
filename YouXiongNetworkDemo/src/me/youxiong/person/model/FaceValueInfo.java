package me.youxiong.person.model;

import java.io.Serializable;

/**
 * 脸值对应的实体类
 * @author mythwind
 *
 */
public class FaceValueInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/*
	 "face_value_potential": "1088855761",
        "face_value_real": "1010015967",
        "face_value_add": 1,
        "photo": "http://alloong.com.cn/data/upload/header/20131212/pic_210_240/412903466db108ef2a8607bc14aab71a.jpg",
        "real_name": "姓名1",
        "card_title": "从事工作"
	 */
	
	private String face_value_potential;
	private String face_value_real;
	private String face_value_add;
	private String photo;
	private String real_name;
	private String card_title;
	
	public String getFace_value_potential() {
		return face_value_potential;
	}
	public void setFace_value_potential(String face_value_potential) {
		this.face_value_potential = face_value_potential;
	}
	public String getFace_value_real() {
		return face_value_real;
	}
	public void setFace_value_real(String face_value_real) {
		this.face_value_real = face_value_real;
	}
	public String getFace_value_add() {
		return face_value_add;
	}
	public void setFace_value_add(String face_value_add) {
		this.face_value_add = face_value_add;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getCard_title() {
		return card_title;
	}
	public void setCard_title(String card_title) {
		this.card_title = card_title;
	}
	@Override
	public String toString() {
		return "FaceValueInfo [face_value_potential=" + face_value_potential + ", face_value_real=" + face_value_real
				+ ", face_value_add=" + face_value_add + ", photo=" + photo + ", real_name=" + real_name
				+ ", card_title=" + card_title + "]";
	}
	
}
