package me.youxiong.person.model;

/**
 * 登陆的 json 数据返回的信息列表
 * @author mythwind
 *
 */
public class LoginResultValue {

	private PersonDetailInfo MyPersonDetail;

	public PersonDetailInfo getMyPersonDetail() {
		return MyPersonDetail;
	}

	public void setMyPersonDetail(PersonDetailInfo myPersonDetail) {
		MyPersonDetail = myPersonDetail;
	}

	@Override
	public String toString() {
		return "LoginResultValue [MyPersonDetail=" + MyPersonDetail + "]";
	}
	
}
