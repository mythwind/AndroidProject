package me.youxiong.person.ui.fragment.base;

import me.youxiong.person.ui.fragment.CardEditFragment;
import me.youxiong.person.ui.fragment.CardSettingFragment;
import me.youxiong.person.ui.fragment.FriendsBlackFragment;
import me.youxiong.person.ui.fragment.FriendsCareFragment;
import me.youxiong.person.ui.fragment.FriendsListFragment;
import me.youxiong.person.ui.fragment.FriendsRequestsFragment;
import me.youxiong.person.ui.fragment.FriendsVisitorFragment;
import me.youxiong.person.ui.fragment.PersonInfoFragment;
import me.youxiong.person.ui.fragment.PersonProductFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FragmentFactory {
	// private static HashMap<FragmentKey, Fragment> mCachedFragments = new HashMap<FragmentKey, Fragment>(10);

	private static Fragment getFragment(FragmentKey key, Bundle bundle) {
		/// Fragment fragment = (Fragment) mCachedFragments.get(key);
		
		Fragment fragment = null;
		switch (key.ordinal()) {
		case 0:
			fragment = new PersonInfoFragment();
			break;
		case 1:
			fragment = new PersonProductFragment();
			break;
			
		case 2:
			fragment = new FriendsListFragment();
			break;
		case 3:
			fragment = new FriendsVisitorFragment();
			break;
		case 4:
			fragment = new FriendsRequestsFragment();
			break;
		case 5:
			fragment = new FriendsCareFragment();
			break;
		case 6:
			fragment = new FriendsBlackFragment();
			break;
		case 7:
			fragment = new CardEditFragment();
			break;
		case 8:
			fragment = new CardSettingFragment();
			break;
		default:
			break;
		}
		if(null != bundle) {
			fragment.setArguments(bundle);
		}
		// mCachedFragments.put(key, fragment);
		return fragment;
	}

	public static Fragment newInstance(FragmentKey fragmentKey, Bundle bundle) {
		return getFragment(fragmentKey, bundle);
	}

	public static enum FragmentKey {
		PERSON_INFO("PERSON_INFO", 0, 0),
		PERSON_PRODUCT("PERSON_PRODUCT", 1, 1), 
		
		FRIENDS_LIST("FRIENDS_LIST", 2, 2), 
		FRIENDS_VISITOR("FRIENDS_VISITOR", 3, 3), 
		FRIENDS_REQUESTS("FRIENDS_REQUESTS", 4, 4),
		FRIENDS_CARE("FRIENDS_CARE", 5, 5),
		FRIENDS_BLACK("FRIENDS_BLACK", 6, 6),
		
		CARD_EDIT("CARD_EDIT", 7, 7),
		CARD_SETTING("CARD_SETTING", 8, 8);
		
		private FragmentKey(String key, int arg2, int arg3) {
		}
		
	}
}
