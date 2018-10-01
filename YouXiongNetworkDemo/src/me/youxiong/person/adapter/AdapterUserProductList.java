package me.youxiong.person.adapter;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import me.youxiong.person.model.ProductDetailInfo;
import me.youxiong.person.utils.LoadImageUtils;
import me.youxiong.person.utils.Logger;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 用户产品列表信息
 * @author mythwind
 */
public class AdapterUserProductList extends MyBaseAdapter {

	public AdapterUserProductList(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		
		if(convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.item_user_product, null);
			viewHodler = new ViewHodler();
			viewHodler.icon = (ImageView) convertView.findViewById(R.id.product_icon);
			viewHodler.name = (TextView) convertView.findViewById(R.id.product_name);
			viewHodler.occupation = (TextView) convertView.findViewById(R.id.product_occupation);
			viewHodler.major = (TextView) convertView.findViewById(R.id.product_major);
			viewHodler.price = (TextView) convertView.findViewById(R.id.product_price);
			convertView.setTag(viewHodler);
			
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		ProductDetailInfo product =  (ProductDetailInfo) getItem(position);
		Logger.d("PersonInfoActivity  info.getProduct_name()=" +  product.getProduct_name());
			
		viewHodler.name.setText(product.getProduct_name());
		viewHodler.occupation.setText(product.getProduct_catetory());
		viewHodler.major.setText(product.getProduct_trades());
		String rmbPrice = getContext().getString(R.string.item_rmb_price, product.getProduct_price());
		viewHodler.price.setText(rmbPrice);
		
		if(!TextUtils.isEmpty(product.getProduct_icon())) {
			LoadImageUtils.displayImage(product.getProduct_icon(), viewHodler.icon);
		}
		
		return convertView;
	}
	private static final class ViewHodler {
		ImageView icon;
		TextView name;
		TextView occupation;
		TextView major;
		TextView price;
	}
}
