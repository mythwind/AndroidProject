package me.youxiong.person.ui.fragment.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;

public class BaseListFragment extends ListFragment {
	protected FragmentActivity mActivity;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		
		mActivity = getActivity();
	}

	public void startActivity(Intent paramIntent) {
		super.startActivity(paramIntent);
	}

	public void startActivityForResult(Intent paramIntent, int paramInt) {
		super.startActivityForResult(paramIntent, paramInt);
	}
}
