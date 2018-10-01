package me.youxiong.person.model;

import java.io.Serializable;

/**
 *  用户的详细信息列表
 * @author mythwind
 *
 */
public class PersonDetailInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int user_id; //1  用户 ID ----- 基本信息
	private String user_pic; // 用户图片
	private String user_type; // 用户类型,1--普通用户，2--法人用户
	private int user_level = 1;  // 用户等级
	
	private String real_name;  //2 姓名    ----- 基本信息
	private int card_id;  // 3 名片ID     ----- 基本信息
	private String card_title; // 4  从事工作    ----- 基本信息
	private String login_name; // 登陆名称
	
	private int sex; // 1(男)/2(女)
	private String trade_id;  // 7    职业        ----- 基本信息
	private String photo; // 8   图片的 URL      ----- 基本信息
	private String soft_name;
	private String soft_number;
	
	private String integral; // 积分。。。。
	private String deduct_integral;  // 产品预设扣除积分
	private String message_integral;  // 陌生人来邮预设扣除积分
	private String last_mood;   // 最近心情
	
	private int credit; // 5  信用度        ----- 基本信息
	private int credit_total;  // 信用度(包括行业信用度)
	private int profession;  // 普通专业度
	private int profession_total; //  6      总专业度(包括行业专业度)    ----- 基本信息
	private String face_value_potential; // 潜在脸值
	private String face_value_real;  // 脸实际价值
	private String money; // 账户余额
	
	private String first_flag; //  第一次头像标识
	private String photo_status; // 头像状态，1--初始，2--审核通过，3--审核不通过
	
	private String is_online;  //  该用户是否已成功登录。1为在线，0为不在线
	
	// 手机号码
	private String mobile; // 登录手机号
	private String fix_phone;
	private String email;
	private String qq;
	private String worktime;
	private String address_detail;
	private int attention_flag; // 是否加为关注1/0
	private int friend_flag; // 是否加为好友1/0
	private int black_flag; // 是否加为黑名单1/0
	private float distance; // 这个是与自己的距离
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_pic() {
		return user_pic;
	}
	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public int getUser_level() {
		if(user_level == 0) {
			user_level = 1;
		}
		return user_level;
	}
	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public int getCard_id() {
		return card_id;
	}
	public void setCard_id(int card_id) {
		this.card_id = card_id;
	}
	public String getCard_title() {
		return card_title;
	}
	public void setCard_title(String card_title) {
		this.card_title = card_title;
	}
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getSoft_name() {
		return soft_name;
	}
	public void setSoft_name(String soft_name) {
		this.soft_name = soft_name;
	}
	public String getSoft_number() {
		return soft_number;
	}
	public void setSoft_number(String soft_number) {
		this.soft_number = soft_number;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	public String getDeduct_integral() {
		return deduct_integral;
	}
	public void setDeduct_integral(String deduct_integral) {
		this.deduct_integral = deduct_integral;
	}
	public String getMessage_integral() {
		return message_integral;
	}
	public void setMessage_integral(String message_integral) {
		this.message_integral = message_integral;
	}
	public String getLast_mood() {
		return last_mood;
	}
	public void setLast_mood(String last_mood) {
		this.last_mood = last_mood;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public int getCredit_total() {
		return credit_total;
	}
	public void setCredit_total(int credit_total) {
		this.credit_total = credit_total;
	}
	public int getProfession() {
		return profession;
	}
	public void setProfession(int profession) {
		this.profession = profession;
	}
	public int getProfession_total() {
		return profession_total;
	}
	public void setProfession_total(int profession_total) {
		this.profession_total = profession_total;
	}
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
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	public String getFirst_flag() {
		return first_flag;
	}
	public void setFirst_flag(String first_flag) {
		this.first_flag = first_flag;
	}
	public String getPhoto_status() {
		return photo_status;
	}
	public void setPhoto_status(String photo_status) {
		this.photo_status = photo_status;
	}
	public String getIs_online() {
		return is_online;
	}
	public void setIs_online(String is_online) {
		this.is_online = is_online;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFix_phone() {
		return fix_phone;
	}
	public void setFix_phone(String fix_phone) {
		this.fix_phone = fix_phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWorktime() {
		return worktime;
	}
	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}
	public String getAddress_detail() {
		return address_detail;
	}
	public void setAddress_detail(String address_detail) {
		this.address_detail = address_detail;
	}
	public int getAttention_flag() {
		return attention_flag;
	}
	public void setAttention_flag(int attention_flag) {
		this.attention_flag = attention_flag;
	}
	public int getFriend_flag() {
		return friend_flag;
	}
	public void setFriend_flag(int friend_flag) {
		this.friend_flag = friend_flag;
	}
	public int getBlack_flag() {
		return black_flag;
	}
	public void setBlack_flag(int black_flag) {
		this.black_flag = black_flag;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	@Override
	public String toString() {
		return "PersionDetailInfo [user_id=" + user_id + ", user_pic=" + user_pic + ", user_type=" + user_type
				+ ", user_level=" + user_level + ", real_name=" + real_name + ", card_id=" + card_id + ", card_title="
				+ card_title + ", login_name=" + login_name + ", sex=" + sex + ", photo=" + photo + ", soft_name="
				+ soft_name + ", soft_number=" + soft_number + ", integral=" + integral + ", deduct_integral="
				+ deduct_integral + ", message_integral=" + message_integral + ", last_mood=" + last_mood + ", credit="
				+ credit + ", credit_total=" + credit_total + ", profession=" + profession + ", profession_total="
				+ profession_total + ", face_value_potential=" + face_value_potential + ", face_value_real="
				+ face_value_real + ", money=" + money + ", trade_id=" + trade_id + ", first_flag=" + first_flag
				+ ", photo_status=" + photo_status + ", is_online=" + is_online + ", mobile=" + mobile + ", fix_phone="
				+ fix_phone + ", email=" + email + ", qq=" + qq + ", worktime=" + worktime + ", address_detail="
				+ address_detail + ", attention_flag=" + attention_flag + ", friend_flag=" + friend_flag
				+ ", black_flag=" + black_flag + ", distance=" + distance + "]";
	}
	
	

//	private String password;
//	private String status;  // 0 未激活， 1 正常，2 零状态， 3 删除
//	private String create_time;
//	private String token;  // 令牌
//	private String search_field; // 搜索字段
//	private upload_time 最新头像上传时间	audit_time 最新头像审核时间	register 最新头像审核人
//	private security_code 安全码
	
	// private strangers_forward_art 是否接受陌生人转发。0表示不接受，1表示接受	strangers_forward_ques 是否接受陌生人转发(问答)。0表示不接受，1表示接受	
	// strangers_forward_doc 是否接受陌生人转发(文档)。0表示不接受，1表示接受	strangers_forward_card 是否接受陌生人转发(名片)。0表示不接受，1表示接受	
	// forwarding_point 转发到市场每人次扣多少分	promotion_man 推广人id	to_card_num 好友转发来的未读名片数量	
	// to_article_num 好友转发来的未读日志数量	to_document_num 好友转发来的未读文档数量	to_question_num 好友转发来的未读问答数量
	
	
}
