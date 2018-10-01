package me.youxiong.person.ui.fragment.base;

public enum FragmentEnum {

	ERROR_VALUE(-1),
	USER_LEVEL(0),
	FRIEND_CATEGORY(1),
	UPDATE_SAFECODE(2),
	UPDATE_PASSWORD(3),
	UPDATE_EMAIL(4);

	public final int value;

	private FragmentEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
