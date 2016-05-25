package com.muzhi.camerasdk.recycleview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.muzhi.camerasdk.R;
import com.muzhi.camerasdk.adapter.ChildEffectAdapter;
import com.muzhi.camerasdk.adapter.ChildStickerAdapter;
import com.muzhi.camerasdk.model.ItemTextrueInfo;
import com.muzhi.camerasdk.utils.FilterUtils;
import com.muzhi.camerasdk.utils.MLog;
import com.muzhi.camerasdk.utils.ScreenHelper;

public class EffectRecyclerViewAdapter extends RecyclerView.Adapter<EffectViewHolder> {
	public static final int TYPE_EFFECT = 0x00000010;
	public static final int TYPE_SRICKER = 0x00000020;
	
	private Context mContext;
	private List<ItemTextrueInfo> mItemTextrueInfos;
	private List<String> mExpandPositions = new ArrayList<String>();
	private int mType = TYPE_EFFECT;
	private String mImgPath;
	
	private OnClickScrollListener mListener;
	private OnEffectItemClickListener mItemClickListener;
	
	public abstract interface OnClickScrollListener {
		public abstract void onExpanded(int distance);
		public abstract void onContract(int position);
	}
	public abstract interface OnEffectItemClickListener {
		public abstract void onItemClick(ItemTextrueInfo info, int position, int type);
	}
	
	public EffectRecyclerViewAdapter(Context context, List<ItemTextrueInfo> infos, int type, String imgPath) {
		mContext = context;
		mItemTextrueInfos = infos;
		mType = type;
		mImgPath = imgPath;
	}
	
	public void setOnClickScrollListener(OnClickScrollListener listener) {
		mListener = listener;
	}
	
	public void setOnEffectItemClickListener(OnEffectItemClickListener listener) {
		mItemClickListener = listener;
	}
	@Override
	public int getItemCount() {
		return mItemTextrueInfos == null ? 0 : mItemTextrueInfos.size();
	}

	@Override
	public void onBindViewHolder(final EffectViewHolder holder, final int position) {
		// MLog.i("onBindViewHolder===================");
		final ItemTextrueInfo info = mItemTextrueInfos.get(position);
		// holder.mImageView.setImageResource(mImageIds[position]);
		Bitmap bmp = FilterUtils.getImageFromAssetsFile(mContext, info.getAssetPath() + "/thumbnail.png");
		holder.mImageView.setImageBitmap(bmp);
		holder.mImageView.setTag(position);
		holder.mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int index = (Integer) v.getTag();//事件处理前要判断状态
				if (index == -2) {
					return;
				}
				final int[] location = new int[2];
				v.getLocationOnScreen(location);
				// MLog.i("===================" + location[0] + "," + location[1]);
				if (info.isExpand()) {
					holder.mGridView.setVisibility(View.GONE);
					mExpandPositions.remove(String.valueOf(position));
					info.setExpand(false);
					if (null != mListener) {
						mListener.onContract(position);
					}
				} else {
					holder.mGridView.setVisibility(View.VISIBLE);
					mExpandPositions.add(String.valueOf(position));
					info.setExpand(true);
					if (null != mListener) {
						new Handler().post(new Runnable() {
							@Override
							public void run() {
								mListener.onExpanded(location[0]);
							}
						});
					}
				}
			}
		});
		ArrayList<ItemTextrueInfo> lists = null;
		BaseAdapter childAdapter = null;
		int count = 0;
		MLog.i("========================mType:" + mType + ",, path:" + info.getAssetPath());
		if(mType == TYPE_EFFECT) {
			lists = FilterUtils.getACVItemFromAsset(mContext, info.getAssetPath());
			childAdapter = new ChildEffectAdapter(mContext, lists, mImgPath);
			count = childAdapter.getCount();
		} else if(mType == TYPE_SRICKER) {
			lists = FilterUtils.getStickerFromAsset(mContext, info.getAssetPath());
			childAdapter = new ChildStickerAdapter(mContext, lists);
			count = childAdapter.getCount();
		}
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
	    float density = dm.density;
	    // 72 dp 转化为 px
	    int gridviewWidth = (int) (80 * count * density);
	    int itemWidth = ScreenHelper.dpToPx(mContext.getResources(), 80);
	    
	    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.mGridView.getLayoutParams();
	    params.width = gridviewWidth;
	    params.height = LinearLayout.LayoutParams.MATCH_PARENT;
	    params.gravity = Gravity.CENTER_VERTICAL;
	    holder.mGridView.setLayoutParams(params); //重点
	    holder.mGridView.setColumnWidth(itemWidth); //重点
	    holder.mGridView.setHorizontalSpacing(1); //间距
	    // holder.mGridView.setStretchMode(GridView.NO_STRETCH);
	    holder.mGridView.setNumColumns(count); //重点
	    holder.mGridView.setAdapter(childAdapter);
	    MLog.i("\nonBindViewHolder===================position:" + position);
	    if (mExpandPositions.contains(String.valueOf(position))) {
	    	holder.mGridView.setVisibility(View.VISIBLE);
		} else {
			holder.mGridView.setVisibility(View.GONE);
		}
	    
	    final BaseAdapter mAdapter = childAdapter;
	    holder.mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ItemTextrueInfo info = (ItemTextrueInfo) mAdapter.getItem(position);
				if(null != mItemClickListener) {
					mItemClickListener.onItemClick(info, position, mType);
				}
			}
		});
	}
	
	@Override
	public EffectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.camerasdk_item_recycleview, parent, false);
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.camerasdk_item_recycleview, null);
		EffectViewHolder holder = new EffectViewHolder(v);
	    return holder;
	}
	
	@Override
	public void onViewRecycled(EffectViewHolder holder) {
		holder.mImageView.setTag(-2); 
		super.onViewRecycled(holder);
	}
}
