package me.youxiong.person.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class MyDashedLine extends View {
	private int mWidthPixels;
	
	public MyDashedLine(Context context) {
		super(context);
		
		initView(context);
	}

	public MyDashedLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView(context);
	}
	
	public MyDashedLine(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		initView(context);
	}
	
	private void initView(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mWidthPixels = dm.widthPixels;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.GRAY);
		Path path = new Path();
		path.moveTo(0, 10);
		path.lineTo(mWidthPixels, 10);
		PathEffect effects = new DashPathEffect(new float[] {5, 5, 5, 5}, 1);
		paint.setPathEffect(effects);
		canvas.drawPath(path, paint);
	}
	
}

