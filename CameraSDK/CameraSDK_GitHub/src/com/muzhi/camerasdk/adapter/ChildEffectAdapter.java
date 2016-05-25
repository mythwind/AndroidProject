package com.muzhi.camerasdk.adapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muzhi.camerasdk.R;
import com.muzhi.camerasdk.library.filter.GPUImageToneCurveFilter;
import com.muzhi.camerasdk.library.filter.GPUImageView;
import com.muzhi.camerasdk.model.ItemTextrueInfo;
import com.muzhi.camerasdk.utils.MLog;
import com.muzhi.camerasdk.utils.ScreenHelper;

public class ChildEffectAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<ItemTextrueInfo> mItemTextrueInfos;
	private Bitmap mBitmap;
	
	public ChildEffectAdapter(Context context, List<ItemTextrueInfo> infos, String imgPath) {
		mContext = context;
		mItemTextrueInfos = infos;
		// 72 dp תΪ px
	    int reqWidth = ScreenHelper.dpToPx(mContext.getResources(), 65);
		mBitmap = getBitmapByPath(imgPath, reqWidth);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.camerasdk_simple_item, null);
			holder = new ViewHolder();
			// holder.icon = (ImageView) convertView.findViewById(R.id.camerasdk_simple_icon);
			holder.icon = (GPUImageView) convertView.findViewById(R.id.camerasdk_simple_icon);
			holder.name = (TextView) convertView.findViewById(R.id.camerasdk_simple_name);
			// BitmapFactory.decodeStream(new FileInputStream(mImgPath));
			holder.icon.setImage(mBitmap);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ItemTextrueInfo info = getItem(position);
		// holder.icon.setImageResource(info.getResId());
		// holder.icon.setImageDrawable(mContext.getResources().getDrawable(info.getResId()));
		// String filename = info.getAssetPath() + "/" + info.getName() + ".acv";
//		String filename = info.getAssetPath() + "/" + FilterUtils.THUMBNAIL_NAME;
//		MLog.i("filename===" + filename);
//		holder.icon.setImageBitmap(FilterUtils.getImageFromAssetsFile(mContext, filename));
		
		GPUImageToneCurveFilter filter = new GPUImageToneCurveFilter();
		MLog.i("===============onItemClick:" + info);
		InputStream is = null;
		try {
			String path = info.getAssetPath() + File.separator + info.getName() + ".acv";
			MLog.i("========OnEffectItemClickListener======path:" + path);
			is = mContext.getAssets().open(path);
			filter.setFromCurveFileInputStream(is);
			holder.icon.setFilter(filter);
			holder.icon.requestRender();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		holder.name.setVisibility(View.VISIBLE);
		holder.name.setText(info.getName());
		return convertView;
	}
	
	private static class ViewHolder {
		// ImageView icon;
		GPUImageView icon;
		TextView name;
	}

	private BitmapFactory.Options getOptions(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;// ֻ��ߣ�����ȡ����
		BitmapFactory.decodeFile(path, options);
		return options;
	}
	private Bitmap getBitmapByPath(String path, int reqWidth) {
		BitmapFactory.Options options = getOptions(path);
		options.inSampleSize = calculateInSampleSize(options, reqWidth);
		// ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {   
	    // ԴͼƬ�ĸ߶ȺͿ��   
	    // final int height = options.outHeight;   
	    final int width = options.outWidth;   
	    int inSampleSize = 1;   
	    if (width > reqWidth) {
	        // �����ʵ�ʿ�ߺ�Ŀ���ߵı���   
	        // final int heightRatio = Math.round((float) height / (float) reqHeight);   
	        final int widthRatio = Math.round((float) width / (float) reqWidth);   
	        // ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�   
	        // һ��������ڵ���Ŀ��Ŀ�͸ߡ�   
	        // inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio; 
	        inSampleSize = widthRatio;
	    }   
	    return inSampleSize;   
	} 
}

