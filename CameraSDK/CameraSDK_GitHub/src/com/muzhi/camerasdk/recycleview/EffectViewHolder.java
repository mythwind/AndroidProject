package com.muzhi.camerasdk.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.muzhi.camerasdk.R;

public class EffectViewHolder extends RecyclerView.ViewHolder {

	ImageView mImageView;
	GridView mGridView;
	
	public EffectViewHolder(View v) {
		super(v);
		mImageView = (ImageView) v.findViewById(R.id.camerasdk_item_parent);
		mImageView.setTag(-2);
		mGridView = (GridView) v.findViewById(R.id.camerasdk_item_gridview);
		//mGridView.setVisibility(View.GONE);
	}
	
}
