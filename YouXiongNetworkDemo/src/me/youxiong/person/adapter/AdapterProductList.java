package me.youxiong.person.adapter;

import me.youxiong.person.R;
import me.youxiong.person.adapter.base.MyBaseAdapter;
import me.youxiong.person.model.ProductDetailInfo;
import me.youxiong.person.utils.LoadImageUtils;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterProductList extends MyBaseAdapter {
	public AdapterProductList(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHodler viewHodler;
		if(convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.item_product_list, null);
			viewHodler = new ViewHodler();
			viewHodler.icon = (ImageView) convertView.findViewById(R.id.item_product_icon);
			viewHodler.name = (TextView) convertView.findViewById(R.id.item_product_name);
			viewHodler.price = (TextView) convertView.findViewById(R.id.item_product_price);
			viewHodler.original_price = (TextView) convertView.findViewById(R.id.item_product_original_price);
			
			convertView.setTag(viewHodler);
			
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		ProductDetailInfo productInfo = (ProductDetailInfo) getItem(position);
		
		viewHodler.name.setText(getContext().getString(R.string.item_product_name, productInfo.getProduct_name()));
		Spanned price = Html.fromHtml(getContext().getString(R.string.item_product_price, productInfo.getProduct_price()));
		viewHodler.price.setText(price);
		viewHodler.original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		viewHodler.original_price.setText(getContext().getString(R.string.item_rmb_price, productInfo.getOriginal_product_price()));
		
		if(!TextUtils.isEmpty(productInfo.getProduct_icon())) {
			LoadImageUtils.displayImage(productInfo.getProduct_icon(), viewHodler.icon, R.drawable.ico_product_default);
		}
		return convertView;
	}
	private static final class ViewHodler {
		ImageView icon;
		TextView name;
		TextView price;
		TextView original_price;
	}
}
