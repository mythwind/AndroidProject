package com.foound.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;

public class AmazingExpandableListView extends ExpandableListView {

	public AmazingExpandableListView(Context context) {
		super(context);
	}
	
	public AmazingExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public AmazingExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int paramInt1, int paramInt2) {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		super.onMeasure(
				View.MeasureSpec.makeMeasureSpec(dm.widthPixels, MeasureSpec.AT_MOST),
				View.MeasureSpec.makeMeasureSpec(dm.heightPixels * dm.densityDpi, MeasureSpec.AT_MOST));
	}
}
