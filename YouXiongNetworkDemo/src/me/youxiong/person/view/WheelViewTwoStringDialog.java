package me.youxiong.person.view;

import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.database.AddressDAO;
import me.youxiong.person.database.impl.AddressDAOImpl;
import me.youxiong.person.dialog.CustomSelectDialog;
import me.youxiong.person.model.BaseDaoModel;
import me.youxiong.person.utils.FileUtils;
import me.youxiong.person.view.wheel.ArrayWheelAdapter;
import me.youxiong.person.view.wheel.OnWheelChangedListener;
import me.youxiong.person.view.wheel.WheelView;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

public class WheelViewTwoStringDialog {
	private Context mContext;
	private CustomSelectDialog mDialog;
	private AddressDAO mAddressDAO;
	private List<BaseDaoModel> provinceList;
	private List<BaseDaoModel> cityList;
	private TextView mTextView;
	private TextView mRelateTextView;
	
	private boolean mWheelViewChanged = false;
	
	private OnPositiveButtonClickListener mPositiveButtonClickListener;
	public interface OnPositiveButtonClickListener {
		public void onClick(BaseDaoModel groupModel, BaseDaoModel childModel);
	}
	
	public WheelViewTwoStringDialog(Context context, String title, TextView textView, int proviceId, int cityId) {
		mContext = context;
		mTextView = textView;
		mDialog = new CustomSelectDialog(context);
		mDialog.setTitleText(title);
		mDialog.setView(R.layout.wheelview_two_string_layout);
		WheelView wheelView01 = (WheelView) mDialog.findViewById(R.id.string_item1);
		WheelView wheelView02 = (WheelView) mDialog.findViewById(R.id.string_item2);
		wheelView01.setWheelViewWidth(100);
		wheelView02.setWheelViewWidth(100);
		
		mAddressDAO = new AddressDAOImpl();
		provinceList = mAddressDAO.getProvince(FileUtils.getDatabaseAddressPath(mContext));
		BaseDaoModel[] array = new BaseDaoModel[provinceList.size()];
		
		wheelView01.setViewAdapter(new ArrayWheelAdapter<BaseDaoModel>(mContext, provinceList.toArray(array)));
		
		wheelView01.setCurrentItem(Math.max(proviceId - 1, 0));
		wheelView01.addChangingListener(new MyWheelChangedListener(this, wheelView01, wheelView02));
		
		mDialog.setRightButtonListener(R.string.positive_button, new MyPositiveButtonClickListener(this, wheelView01, wheelView02));
		mDialog.setLeftButtonListener(R.string.negative_button, null);
		
		wheelViewBigChanged(wheelView01, wheelView02);
		setCurrentItem(wheelView02, Math.max(cityId, 0));
	}

	public void show() {
		mDialog.show();
	}
	
	public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener positiveButtonClickListener) {
		mPositiveButtonClickListener = positiveButtonClickListener;
	}
	
	public void setRelateTextView(TextView textView) {
		mRelateTextView = textView;
	}

	protected void setCurrentItem(WheelView wheelView, int currentItem) {
		if (currentItem < 0)
			currentItem = 0;
		wheelView.setCurrentItem(currentItem);
	}

	public void a(WheelView groupView, WheelView childView) {
		String str = getTwoStringText(groupView, childView);
		
		BaseDaoModel groupModel = provinceList.get(groupView.getCurrentItem());
		BaseDaoModel childModel = cityList.get(childView.getCurrentItem());
		if(mWheelViewChanged) {
			mTextView.setText(str);
			if(null != mRelateTextView) {
				mRelateTextView.setText(str);
			}
		}
		mPositiveButtonClickListener.onClick(groupModel, childModel);
	}
	
	private String getTwoStringText(WheelView groupView, WheelView childView) {
		int i = groupView.getCurrentItem();
		int j = childView.getCurrentItem();  // d();
		if (j >= cityList.size())
			j = 0;
		if (i >= provinceList.size())
			i = 0;
		String str = provinceList.get(i).getName();
		if(cityList.size() > 1) {
			str += cityList.get(j).getName();
		}
		return str;
	}

//	public abstract void a(String paramString, int paramInt1, int paramInt2);

	public void wheelViewBigChanged(final WheelView groupView, final WheelView childView) {
		int item = groupView.getCurrentItem();
		cityList = mAddressDAO.getCityByProvinceId(FileUtils.getDatabaseAddressPath(mContext), provinceList.get(item).getId());
		BaseDaoModel[] array = new BaseDaoModel[cityList.size()];
		childView.setViewAdapter(new ArrayWheelAdapter<BaseDaoModel>(mContext, cityList.toArray(array)));
		setCurrentItem(childView, 0);
		
		childView.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				mWheelViewChanged = true;
			}
		});
	}
	
	class MyWheelChangedListener implements OnWheelChangedListener {
		WheelView a, b;
		MyWheelChangedListener(WheelViewTwoStringDialog paramcw, WheelView wheelView1, WheelView wheelView2) {
			a = wheelView1;
			b = wheelView2;
		}
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			mWheelViewChanged = true;
			wheelViewBigChanged(this.a, this.b);
		}
	}

	class MyPositiveButtonClickListener implements DialogInterface.OnClickListener {
		WheelView a, b;
		MyPositiveButtonClickListener(WheelViewTwoStringDialog paramcw, WheelView paramWheelView1, WheelView paramWheelView2) {
			a = paramWheelView1;
			b= paramWheelView2;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			a(this.a, this.b);
		}
	}
}

