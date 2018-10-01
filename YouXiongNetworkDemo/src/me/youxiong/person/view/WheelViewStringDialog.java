package me.youxiong.person.view;

import java.util.List;

import me.youxiong.person.R;
import me.youxiong.person.database.AddressDAO;
import me.youxiong.person.database.impl.AddressDAOImpl;
import me.youxiong.person.dialog.CustomSelectDialog;
import me.youxiong.person.model.BaseDaoModel;
import me.youxiong.person.utils.FileUtils;
import me.youxiong.person.view.wheel.ArrayWheelAdapter;
import me.youxiong.person.view.wheel.WheelView;
import android.content.Context;

public class WheelViewStringDialog {
	protected Context mContext;
	protected CustomSelectDialog mDialog;

	public WheelViewStringDialog(Context context, String title, int currentItem) {
		mContext = context;
		mDialog = new CustomSelectDialog(context);
		mDialog.setTitleText(title);
		mDialog.setView(R.layout.wheelview_string_layout);
		WheelView wheelView = (WheelView) mDialog.findViewById(R.id.string_item);
		wheelView.setWheelViewWidth(110);
		
		AddressDAO mAddressDAO = new AddressDAOImpl();
		List<BaseDaoModel> provinceList = mAddressDAO.getProvince(FileUtils.getDatabaseAddressPath(mContext));
		BaseDaoModel[] array = new BaseDaoModel[provinceList.size()];
		
		wheelView.setViewAdapter(new ArrayWheelAdapter<BaseDaoModel>(mContext, provinceList.toArray(array)));
		wheelView.setCurrentItem(0); // a(int, false)
		mDialog.setRightButtonListener(R.string.positive_button, null);
		mDialog.setLeftButtonListener(R.string.negative_button, null);
		
	}

	public void show() {
		mDialog.show();
	}

}

