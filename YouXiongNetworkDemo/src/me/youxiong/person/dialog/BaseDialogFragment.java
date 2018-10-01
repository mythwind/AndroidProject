package me.youxiong.person.dialog;

import me.youxiong.person.R;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;

public abstract class BaseDialogFragment extends DialogFragment {

	public BaseDialogFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	private WaitDialog dialog;

	protected void waiting(String msg, boolean cancelable) {
		dialog = new WaitDialog(getActivity(), R.style.TransparentDialog);
		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(false);
		// Disable the back button
		dialog.setOnKeyListener(new Dialog.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				return keyCode == KeyEvent.KEYCODE_BACK;
			}
		});
		dialog.setWaitText(msg);
		dialog.show();
		
	}

	public void waiting(String msg) {
		waiting(msg, true);
	}

	public void stopWaiting() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

}
