package me.youxiong.person.model;

/**
 * 查看 persion 详细信息的请求
 * @author mythwind
 *
 */
public class PersionInfoResultValue {
	private PersonDetailInfo PersonDetail;

	public PersonDetailInfo getPersonDetail() {
		return PersonDetail;
	}

	public void setPersonDetail(PersonDetailInfo personDetail) {
		PersonDetail = personDetail;
	}

	@Override
	public String toString() {
		return "PersionResultValue [PersonDetail=" + PersonDetail + "]";
	}
	
}
