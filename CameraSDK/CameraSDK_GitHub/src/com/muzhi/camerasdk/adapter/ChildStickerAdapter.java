package com.muzhi.camerasdk.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.muzhi.camerasdk.R;
import com.muzhi.camerasdk.model.ItemTextrueInfo;
import com.muzhi.camerasdk.utils.FilterUtils;
import com.muzhi.camerasdk.utils.MLog;

public class ChildStickerAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<ItemTextrueInfo> mItemTextrueInfos;
	
	public ChildStickerAdapter(Context context, List<ItemTextrueInfo> infos) {
		mContext = context;
		mItemTextrueInfos = infos;
	}
	
	@Override
	public int getCount() {
		return mItemTextrueInfos == null ? 0 : mItemTextrueInfos.size();
	}

	@Override
	public ItemTextrueInfo getItem(int position) {
		return mItemTextrueInfos == null ? null : mItemTextrueInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.camerasdk_simple_imageview, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.camerasdk_simple_imageview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ItemTextrueInfo info = getItem(position);
		// holder.icon.setImageResource(info.getResId());
		MLog.i("======================child.getAssetPath:" + info.getAssetPath());
		holder.icon.setImageBitmap(FilterUtils.getImageFromAssetsFile(mContext, info.getAssetPath()));
		
		// holder.icon.setImageDrawable(mContext.getResources().getDrawable(info.getResId()));
		//@dimen/bottom_image_height
//		int dimen = (int) mContext.getResources().getDimensionPixelSize(R.dimen.sticker_image_height);
//		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.icon.getLayoutParams();
//		lp.height = dimen;
//		lp.gravity = Gravity.CENTER_VERTICAL;
//		holder.icon.setLayoutParams(lp);
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView icon;
	}
	
}

