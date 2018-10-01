package me.youxiong.person.model;

import java.io.Serializable;

/**
 * 查看 persion 简要信息的请求(AdapterMain)
 * @author mythwind
 *
 */
public class PersionCardInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	{"work_time_edit":{"stop_time":"08:00:00","stop_week":"周五","start_time":"08:00:00","start_week":"周一"},
	"address_city_ode":87,
	"phone":"057188096666",
	"sex":"男",
	"address":"浙江省杭州市城站",
	"email":"46107125@qq.com","profession":"销售代表","real_name":"姓名",
	"job":"从事工作","address_province_ode":11,"qq":"333454455656","work_time":"周一 08:00:00 - 周五 08:00:00","mobile":"13575730020"}
	 */
	private String real_name;
	private String card_title;
	private String photo;
	private String sex;
	private int credit_total;
	private int profession_total;
	private String distance;
	private String mobile;
	private String phone;
	private String email;
	private String soft_name;
	private String soft_number;
	private String trade_ids;
	
	private int user_level;
	private int address_province_ode;
	private int address_city_ode;
	private String address;
	private String job; // 从事工作
	private String profession; // 
	
	private String work_time;
	private WorkTimeInfo work_time_edit;
	
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getCredit_total() {
		return credit_total;
	}
	public void setCredit_total(int credit_total) {
		this.credit_total = credit_total;
	}
	public int getProfession_total() {
		return profession_total;
	}
	public void setProfession_total(int profession_total) {
		this.profession_total = profession_total;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getTrade_ids() {
		return trade_ids;
	}
	public void setTrade_ids(String trade_ids) {
		this.trade_ids = trade_ids;
	}
	public int getUser_level() {
		return user_level;
	}
	public void setUser_level(int user_level) {
		this.user_level = user_level;
	}
	public int getAddress_province_ode() {
		return address_province_ode;
	}
	public void setAddress_province_ode(int address_province_ode) {
		this.address_province_ode = address_province_ode;
	}
	public int getAddress_city_ode() {
		return address_city_ode;
	}
	public void setAddress_city_ode(int address_city_ode) {
		this.address_city_ode = address_city_ode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getWork_time() {
		return work_time;
	}
	public void setWork_time(String work_time) {
		this.work_time = work_time;
	}
	public WorkTimeInfo getWork_time_edit() {
		return work_time_edit;
	}
	public void setWork_time_edit(WorkTimeInfo work_time_edit) {
		this.work_time_edit = work_time_edit;
	}
	@Override
	public String toString() {
		return "PersionCardInfo [real_name=" + real_name + ", card_title=" + card_title + ", photo=" + photo + ", sex="
				+ sex + ", credit_total=" + credit_total + ", profession_total=" + profession_total + ", distance="
				+ distance + ", mobile=" + mobile + ", phone=" + phone + ", email=" + email + ", trade_ids=" + trade_ids
				+ ", address_province_ode=" + address_province_ode + ", address_city_ode=" + address_city_ode
				+ ", address=" + address + ", job=" + job + ", profession=" + profession + ", work_time=" + work_time
				+ ", work_time_edit=" + work_time_edit + "]";
	}
}
