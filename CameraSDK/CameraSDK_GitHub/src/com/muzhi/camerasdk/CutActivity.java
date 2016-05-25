package com.muzhi.camerasdk;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.muzhi.camerasdk.library.utils.PhotoUtils;
import com.muzhi.camerasdk.model.Constants;
import com.muzhi.camerasdk.view.CropImageView;



public class CutActivity extends BaseActivity implements OnClickListener {
	
    private CropImageView mCropView;

    private RadioGroup layout_crop,layout_tab;
    private LinearLayout layout_rotation;
    private Bitmap sourceMap;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camerasdk_activity_cut);
        showLeftIcon();
        setActionBarTitle(getString(R.string.camerasdk_tab_cropper));
        findViews();
        
        sourceMap=Constants.bitmap;
        mCropView.setImageBitmap(sourceMap);		
        
    }

    private void findViews() {
        mCropView = (CropImageView) findViewById(R.id.cropImageView);
        
        layout_crop=(RadioGroup)findViewById(R.id.layout_crop);
        layout_tab=(RadioGroup)findViewById(R.id.layout_tab);
        layout_rotation=(LinearLayout)findViewById(R.id.layout_rotation);
        initEvent();
    }
	
	private void initEvent() {
		//TODO 替换右边的文字，改成图片 
		showRightIcon(0, new OnClickListener() {
			@Override
			public void onClick(View v) {
				done();
			}
		});
		findViewById(R.id.button1_1).setOnClickListener(this);
		findViewById(R.id.button3_4).setOnClickListener(this);
		findViewById(R.id.button4_3).setOnClickListener(this);
		findViewById(R.id.button9_16).setOnClickListener(this);
		findViewById(R.id.button16_9).setOnClickListener(this);

		/*findViewById(R.id.buttonFree).setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				mCropView.setCropMode(CropImageView.CropMode.RATIO_FREE); //自由裁剪
			}
		});*/
		
		//*******************************************************************************
		
		layout_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				if (arg1 == R.id.button_crop) {
					layout_crop.setVisibility(View.VISIBLE);
					layout_rotation.setVisibility(View.GONE);
				} else {
					layout_crop.setVisibility(View.GONE);
					layout_rotation.setVisibility(View.VISIBLE);
				}
			}
		});
		
		findViewById(R.id.ratation_left).setOnClickListener(this);
		findViewById(R.id.ratation_right).setOnClickListener(this);
		findViewById(R.id.ratation_vertical).setOnClickListener(this);
		findViewById(R.id.ratation_updown).setOnClickListener(this);
	}
    
    private void done(){
    	Constants.bitmap=mCropView.getCroppedBitmap();
    	setResult(Constants.RequestCode_Croper);
        finish();
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.button1_1) {
			mCropView.setCropMode(CropImageView.CropMode.RATIO_1_1);
		} else if(id == R.id.button3_4) {
			mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
		} else if(id == R.id.button4_3) {
			mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
		} else if(id == R.id.button9_16) {
			mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
		} else if(id == R.id.button16_9) {
			mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
		}
		else if(id == R.id.ratation_left) {
			sourceMap = PhotoUtils.rotateImage(sourceMap, -90);
			mCropView.setImageBitmap(sourceMap);
		} else if(id == R.id.ratation_right) {
			sourceMap = PhotoUtils.rotateImage(sourceMap, 90);
			mCropView.setImageBitmap(sourceMap);
		} else if(id == R.id.ratation_vertical) {
			sourceMap = PhotoUtils.reverseImage(sourceMap, -1, 1);
			mCropView.setImageBitmap(sourceMap);
		} else if(id == R.id.ratation_updown) {
			sourceMap = PhotoUtils.reverseImage(sourceMap, 1, -1);
			mCropView.setImageBitmap(sourceMap);
		}
	}
    
}
