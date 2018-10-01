package me.youxiong.person.dialog;

import me.youxiong.person.R;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditTextViewDialog {
	private Context mContext;
	private CustomSelectDialog mDialog;
	private IDialogDismiss mDialogDismiss;
	
	private EditText mEditText;
	private TextView mTextView;
	private boolean mCanBeNull = false;
	
	public interface IDialogDismiss {
		public void onPositive(String text);
		public void onNegative();
		
	}
	public EditTextViewDialog(Context context, String title, TextView textView) {
		mContext = context;
		mTextView = textView;
		mDialog = new CustomSelectDialog(context);
		mDialog.setTitleText(title);
		mEditText = new EditText(context);
		mEditText.setText(textView.getText());
		
		mDialog.setView(mEditText);
		mDialog.setRightButtonListener(R.string.positive_button, clickListener);
		mDialog.setLeftButtonListener(R.string.negative_button, null);
	}
	public EditTextViewDialog(Context context, int titleRes, TextView textView) {
		this(context, context.getString(titleRes), textView);
	}

	public void show() {
		mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); 
		mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		mEditText.requestFocus();
		mEditText.addTextChangedListener(watcher);
		mDialog.show();
		
	}
	public void dismiss() {
		mDialog.dismiss();
	}
	
	public void setCanBeNull(boolean canBeNull) {
		mCanBeNull = canBeNull;
	}
	public boolean isCanBeNull() {
		return mCanBeNull;
	}
	// EditorInfo.TYPE_CLASS_PHONE
	public void setInputType(int type) {
		mEditText.setInputType(type);
	}
	public String getEditText() {
		return mEditText.getText().toString().trim();
	}
	public void setEditText(String text) {
		mEditText.setText(text);
	}
	
	private TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		@Override
		public void afterTextChanged(Editable s) {
			if(!mCanBeNull) {
				if(TextUtils.isEmpty(s.toString())) {
					mDialog.setRightButtonEnable(false);
					Toast.makeText(mContext, "该项不能为空", Toast.LENGTH_SHORT).show();
				} else {
					mDialog.setRightButtonEnable(true);
				}
			} else {
				mDialog.setRightButtonEnable(true);
			}
		}
	};
	
	public void setDialogDismiss(IDialogDismiss dismiss) {
		mDialogDismiss = dismiss;
	}
	private DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			String text = mEditText.getText().toString();
			mTextView.setText(text);
			mDialog.dismiss();
			if(null != mDialogDismiss) {
				mDialogDismiss.onPositive(text);
			}
		}
	};
}

