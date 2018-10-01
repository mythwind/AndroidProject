package me.youxiong.person.dialog;

import me.youxiong.person.R;
import me.youxiong.person.utils.CommonUtils;
import me.youxiong.person.utils.config.YXConstants;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CustomSelectDialog extends Dialog implements View.OnClickListener {
	private Button mRightButton; // a
	private Button mLeftButton;  // b
	private Button mCenterButton; // c
//	private View mProgressView;  // progress_view
	private Context mContext; // e
	
	public CustomSelectDialog(Context context) {
		super(context, R.style.CustomDialog);
		mContext = context;
		setContentView(R.layout.custom_dialog);
		initViewComponent();
		setCanceledOnTouchOutside(true);
	}

	public CustomSelectDialog(Context context, int layoutResId) {
		super(context, R.style.CustomDialog);
		mContext = context;
		setContentView(layoutResId);
		initViewComponent();
		setCanceledOnTouchOutside(true);
	}
	
	
	
	private void initViewComponent() {
		mRightButton = ((Button) findViewById(R.id.button_right));
		mLeftButton = ((Button) findViewById(R.id.button_left));
		mCenterButton = ((Button) findViewById(R.id.button_center));
//		mProgressView = findViewById(R.id.progress_view);
		if (mRightButton != null)
			mRightButton.setOnClickListener(this);
		if (mLeftButton != null)
			mLeftButton.setOnClickListener(this);
		if (mCenterButton != null)
			mCenterButton.setOnClickListener(this);
	}

	public static void a(Context paramContext, int paramInt1, int paramInt2) {
		HandlerThread localHandlerThread = new HandlerThread("HandlerThread");
		localHandlerThread.start();
		new Handler(localHandlerThread.getLooper()).post(new z(paramContext, paramInt1, paramInt2));
	}

	/**
	 * a(ListView paramListView)
	 * @param paramListView
	 */
	private void setListView(ListView listView) {
		findViewById(R.id.contentPanel).setVisibility(View.GONE);
		findViewById(R.id.custom).setVisibility(View.GONE);
		
		FrameLayout localFrameLayout = (FrameLayout) findViewById(R.id.custom);
		if (listView.getCount() < YXConstants.LIMIT_SELECT_TRADE_NUMBER) {
			localFrameLayout.addView(listView, new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT));
			return;
		}
		localFrameLayout.addView(listView, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, 
				(int) (6.7d * CommonUtils.dip2px(mContext.getResources(), 48.0F))));
	}

	public CustomSelectDialog a() {
		mRightButton.setTag(mRightButton.getId(), Boolean.valueOf(true));
		return this;
	}

	public CustomSelectDialog a(int paramInt) {
		return a(paramInt, -1);
	}
	
	public CustomSelectDialog a(int paramInt1, int paramInt2) {
		CharSequence localCharSequence = getContext().getText(paramInt1);
		if (paramInt2 != -1) {
			TextView localTextView1 = (TextView) findViewById(R.id.alertSubTitle);
			localTextView1.setVisibility(0);
			localTextView1.setText(paramInt2);
			TextView localTextView2 = (TextView) findViewById(R.id.alertTitle);
			int i = CommonUtils.dip2px(mContext.getResources(), 10.0F);
			localTextView2.setPadding(i, i, 0, 0);
		}
		return setTitleText(localCharSequence);
	}

	public CustomSelectDialog setView(View view) {
		findViewById(R.id.contentPanel).setVisibility(View.GONE);
		findViewById(R.id.custom).setVisibility(View.VISIBLE);
		((FrameLayout) findViewById(R.id.custom)).addView(view, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT));
		return this;
	}

	/**
	 * a(CharSequence title)
	 * 设置对话框的标题
	 * @param title
	 * @return
	 */
	public CustomSelectDialog setTitleText(CharSequence title) {
		findViewById(R.id.title_layout).setVisibility(0);
		((TextView) findViewById(R.id.alertTitle)).setText(title);
		return this;
	}

	public CustomSelectDialog setRightButtonListener(int textId, DialogInterface.OnClickListener clickListener) {
		return setRightButtonListener(getContext().getText(textId), clickListener);
	}
	public CustomSelectDialog setRightButtonListener(CharSequence text, DialogInterface.OnClickListener clickListener) {
		Button rightBtn = (Button) findViewById(R.id.button_right);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setText(text);
		rightBtn.setTag(clickListener);
		return this;
	}

	/**
	 * 确定按钮是否可以点击
	 * @param enable
	 * @return
	 */
	public CustomSelectDialog setRightButtonEnable(boolean enable) {
		mRightButton.setEnabled(enable);
		return this;
	}

	public CustomSelectDialog a(CharSequence[] charSequence, final DialogInterface.OnClickListener clickListener) {
		ListView localListView = (ListView) View.inflate(getContext(), R.layout.custom_dialog_list, null);
		
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.custom_dialog_list_item, R.id.text, charSequence);
		
		localListView.setAdapter(adapter);
		localListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				dismiss();
				clickListener.onClick(CustomSelectDialog.this, position);
			}
		});
		
		if (findViewById(R.id.alertTitle).getVisibility() == 0) {
			FrameLayout localFrameLayout = (FrameLayout) findViewById(R.id.custom);
			localFrameLayout.setPadding(localFrameLayout.getPaddingLeft(), 0, localFrameLayout.getPaddingRight(),
					localFrameLayout.getPaddingBottom());
		}
		//2131165254  button_layout
		findViewById(R.id.button_layout).setPadding(0, 0, 0, 0);
		setListView(localListView);
		return this;
	}

	public CustomSelectDialog b() {
		mLeftButton.setTag(mLeftButton.getId(), Boolean.valueOf(true));
		return this;
	}

	public View getRootView() {
		return findViewById(R.id.dialog_panel);
	}
	public CustomSelectDialog setMessage(int textResId) {
		return setMessage(getContext().getText(textResId));
	}

	public CustomSelectDialog setMessage(CharSequence message) {
		findViewById(R.id.custom).setVisibility(View.GONE);
		findViewById(R.id.contentPanel).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.message)).setText(message);
		return this;
	}
	
	public CustomSelectDialog setLeftButtonListener(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
		return setLeftButtonListener(getContext().getText(paramInt), paramOnClickListener);
	}
	public CustomSelectDialog setLeftButtonListener(CharSequence text, DialogInterface.OnClickListener clickListener) {
		Button leftBtn = (Button) findViewById(R.id.button_left);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setText(text);
		leftBtn.setTag(clickListener);
		return this;
	}

	public void b(boolean paramBoolean) {
		if (paramBoolean)
			findViewById(R.id.dialog_panel).setBackgroundResource(0);
	}

	public CustomSelectDialog c() {
		mCenterButton.setTag(mCenterButton.getId(), Boolean.valueOf(true));
		return this;
	}

	public CustomSelectDialog setView(int layoutResId) {
		return setView(((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(layoutResId, null));
	}

	public CustomSelectDialog setCenterButtonListener(int textResId, DialogInterface.OnClickListener clickListener) {
		return setCenterButtonListener(getContext().getText(textResId), clickListener);
	}

	public CustomSelectDialog setCenterButtonListener(CharSequence text, DialogInterface.OnClickListener clickListener) {
		Button centerBtn = (Button) findViewById(R.id.button_center);
		centerBtn.setVisibility(View.VISIBLE);
		centerBtn.setText(text);
		centerBtn.setTag(clickListener);
		return this;
	}

	/**
	 * dialog 点击事件处理
	 */
	public void onClick(View view) {
		DialogInterface.OnClickListener listener = (DialogInterface.OnClickListener) view.getTag();
		Boolean isShow = (Boolean) view.getTag(view.getId());
		if (!Boolean.TRUE.equals(isShow)) {
			dismiss();
		}
		if (listener != null) {
			listener.onClick(this, view.getId());
		}
	}
	@Override
	public void dismiss() {
		if(null != getCurrentFocus() && null != getCurrentFocus().getWindowToken()) {
			((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)) .hideSoftInputFromWindow(
					getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		super.dismiss();
		
	}
	
	private static final class z implements Runnable {
		Context zContext;
		int  b, c;
		z(Context paramContext, int paramInt1, int paramInt2) {
			zContext = paramContext;
			b = paramInt1;
			c = paramInt2;
		}

		public void run() {
			try {
				CustomSelectDialog localx = new CustomSelectDialog(zContext);
				if (this.b != -1)
					localx.a(this.b);
				localx.setMessage(this.c);
				localx.setRightButtonListener(R.string.positive_button, null);
				localx.setLeftButtonListener(R.string.negative_button, null);
				localx.show();
				return;
			} catch (WindowManager.BadTokenException e) {
				e.printStackTrace();
			}
		}
	}
	

	class y implements AdapterView.OnItemClickListener {
		CustomSelectDialog b;
		DialogInterface.OnClickListener a;
		y(CustomSelectDialog paramx, DialogInterface.OnClickListener paramOnClickListener) {
			b = paramx;
			a = paramOnClickListener;
		}

		public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
			this.b.dismiss();
			this.a.onClick(this.b, paramInt);
		}
	}
}
