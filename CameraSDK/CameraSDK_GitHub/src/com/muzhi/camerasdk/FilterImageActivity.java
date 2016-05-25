package com.muzhi.camerasdk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.muzhi.camerasdk.adapter.FragmentViewPagerAdapter;
import com.muzhi.camerasdk.adapter.SmallThumbAdapter;
import com.muzhi.camerasdk.library.filter.GPUImageToneCurveFilter;
import com.muzhi.camerasdk.library.views.HorizontalListView;
import com.muzhi.camerasdk.model.CameraSdkParameterInfo;
import com.muzhi.camerasdk.model.Constants;
import com.muzhi.camerasdk.model.Filter_Sticker_Info;
import com.muzhi.camerasdk.model.ItemTextrueInfo;
import com.muzhi.camerasdk.recycleview.EffectRecyclerViewAdapter;
import com.muzhi.camerasdk.recycleview.EffectRecyclerViewAdapter.OnClickScrollListener;
import com.muzhi.camerasdk.recycleview.EffectRecyclerViewAdapter.OnEffectItemClickListener;
import com.muzhi.camerasdk.ui.fragment.EfectFragment;
import com.muzhi.camerasdk.utils.FilterUtils;
import com.muzhi.camerasdk.utils.MLog;
import com.muzhi.camerasdk.view.CustomViewPager;

public class FilterImageActivity extends BaseActivity implements OnClickListener {

	private CameraSdkParameterInfo mCameraSdkParameterInfo = new CameraSdkParameterInfo();

	// private HorizontalListView effect_listview, sticker_listview, images_listview;
	private HorizontalListView images_listview;
	private RecyclerView effect_listview, sticker_listview;

	private TextView tab_effect, tab_sticker, txt_cropper, txt_enhance, txt_graffiti;
	// private RadioButton tab_effect,
	// tab_sticker,txt_cropper,txt_enhance,txt_graffiti;
	private ImageView mIvBtnDone;

	private RelativeLayout loading_layout;// 等待框
	private SeekBar mSeekBar;

	private SmallThumbAdapter iAdapter;
//	private Filter_Effect_Adapter eAdapter;
//	private Filter_Sticker_Adapter sAdapter;
	
	private EffectRecyclerViewAdapter eAdapter;
	private EffectRecyclerViewAdapter sAdapter;

//	private ArrayList<Filter_Effect_Info> effect_list = new ArrayList<Filter_Effect_Info>(); // 特效
//	private ArrayList<Filter_Sticker_Info> stickerList = new ArrayList<Filter_Sticker_Info>();
	
	private ArrayList<ItemTextrueInfo> effect_list = new ArrayList<ItemTextrueInfo>(); // 特效
	private ArrayList<ItemTextrueInfo> stickerList = new ArrayList<ItemTextrueInfo>();
	
	private ArrayList<String> imageList;
	
	public static Filter_Sticker_Info mSticker = null; // 从贴纸库过来的贴纸

	private CustomViewPager mViewPager;
	private ArrayList<Fragment> fragments;

	private int current = 0;

	private static final int HANDLE_SAVED = 0x0004;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLE_SAVED:
				String path = (String) msg.obj;
				Toast.makeText(FilterImageActivity.this, getString(R.string.camerasdk_save_path, path), Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camerasdk_filter_image);
		showLeftIcon();

		try {
			mCameraSdkParameterInfo = (CameraSdkParameterInfo) getIntent().getSerializableExtra(
					CameraSdkParameterInfo.EXTRA_PARAMETER);
			imageList = mCameraSdkParameterInfo.getImage_list();
		} catch (Exception e) {
		}

		initView();

		TextView tv_title = (TextView) findViewById(R.id.camerasdk_actionbar_title);
		if (mCameraSdkParameterInfo.isSingle_mode()) {
			// setActionBarTitle(getString(R.string.camerasdk_tab_filter));
			setActionBarTitle(getString(R.string.camerasdk_save));
		} else {
			tv_title.setVisibility(View.GONE);
			findViewById(R.id.images_layout).setVisibility(View.VISIBLE);
		}
		// 默认选中
		// tab_effect.setTextColor(getResources().getColor(R.color.camerasdk_txt_selected));
		tab_effect.setSelected(true);
		
		initRecycleView();
		initEvent();
		initData();
	}

	private void initView() {
		mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
		mViewPager.setPagingEnabled(false);

		// txt_cropper = (RadioButton) findViewById(R.id.txt_cropper);
		// tab_effect = (RadioButton) findViewById(R.id.txt_effect);
		// tab_sticker = (RadioButton) findViewById(R.id.txt_sticker);
		// txt_enhance = (RadioButton) findViewById(R.id.txt_enhance);
		// txt_graffiti = (RadioButton) findViewById(R.id.txt_graffiti);

		txt_cropper = (TextView) findViewById(R.id.txt_cropper);
		tab_effect = (TextView) findViewById(R.id.txt_effect);
		tab_sticker = (TextView) findViewById(R.id.txt_sticker);
		txt_enhance = (TextView) findViewById(R.id.txt_enhance);
		txt_graffiti = (TextView) findViewById(R.id.txt_graffiti);

		mIvBtnDone = (ImageView) findViewById(R.id.camerasdk_title_imgv_right_icon);
		mIvBtnDone.setImageResource(R.drawable.camerasdk_selector_edit_save);
		mIvBtnDone.setVisibility(View.VISIBLE);

		mSeekBar = (SeekBar) findViewById(R.id.seekBar);
//		effect_listview = (HorizontalListView) findViewById(R.id.effect_listview);
//		sticker_listview = (HorizontalListView) findViewById(R.id.sticker_listview);
		effect_listview = (RecyclerView) findViewById(R.id.effect_content_list);
		sticker_listview = (RecyclerView) findViewById(R.id.sticker_content_list);
		images_listview = (HorizontalListView) findViewById(R.id.images_listview);
		loading_layout = (RelativeLayout) findViewById(R.id.loading);

	}

	private void initRecycleView() {
		effect_listview.setVisibility(View.VISIBLE);
		effect_listview.setHasFixedSize(true);
		
		// Set layout manager
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayout.HORIZONTAL);
		effect_listview.setLayoutManager(layoutManager);
		effect_listview.setItemAnimator(new DefaultItemAnimator());
		
		effect_list = FilterUtils.getEffectParentListFromAsset(mContext);
		eAdapter = new EffectRecyclerViewAdapter(this, effect_list, EffectRecyclerViewAdapter.TYPE_EFFECT, imageList.get(current));
		effect_listview.setAdapter(eAdapter);
		eAdapter.setOnClickScrollListener(new OnClickScrollListener() {
			@Override
			public void onExpanded(int distance) {
				effect_listview.smoothScrollBy(distance, 0);
				
			}
			@Override
			public void onContract(int position) {
				effect_listview.smoothScrollBy(1,0);
			}
		});
		
		sticker_listview.setHasFixedSize(true);
		LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
		layoutManager1.setOrientation(LinearLayout.HORIZONTAL);
		sticker_listview.setLayoutManager(layoutManager1);
		sticker_listview.setItemAnimator(new DefaultItemAnimator());
		
		stickerList = FilterUtils.getStickerParentFromAsset(mContext);
		sAdapter = new EffectRecyclerViewAdapter(this, stickerList, EffectRecyclerViewAdapter.TYPE_SRICKER, imageList.get(current));
		sticker_listview.setAdapter(sAdapter);
		sAdapter.setOnClickScrollListener(new OnClickScrollListener() {
			@Override
			public void onExpanded(int distance) {
				sticker_listview.smoothScrollBy(distance, 0);
			}
			@Override
			public void onContract(int position) {
				sticker_listview.smoothScrollBy(1,0);
			}
		});
	}
	private void initEvent() {
		tab_effect.setOnClickListener(this);
		tab_sticker.setOnClickListener(this);
		txt_cropper.setOnClickListener(this);
		txt_enhance.setOnClickListener(this);
		txt_graffiti.setOnClickListener(this);

		mIvBtnDone.setOnClickListener(this);

		eAdapter.setOnEffectItemClickListener(new OnEffectItemClickListener() {
			@Override
			public void onItemClick(ItemTextrueInfo info, int position, int type) {
				// TODO Auto-generated method stub
				GPUImageToneCurveFilter filter = new GPUImageToneCurveFilter();
				MLog.i("===============onItemClick:" + info);
				InputStream is = null;
				try {
					String path = info.getAssetPath() + File.separator + info.getName() + ".acv";
					MLog.i("========OnEffectItemClickListener======path:" + path);
					is = getAssets().open(path);
					filter.setFromCurveFileInputStream(is);
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// GPUImageFilter filter = ImageFilterTools.createFilterForType(mContext, info.getFilterType());
				((EfectFragment) fragments.get(current)).addEffect(filter);
			}
		});
		sAdapter.setOnEffectItemClickListener(new OnEffectItemClickListener() {
			@Override
			public void onItemClick(ItemTextrueInfo info, int position, int type) {
				// TODO Auto-generated method stub
				// int drawableId = info.getResId();
				String path = info.getAssetPath();
				
				((EfectFragment) fragments.get(current)).addSticker(FilterUtils.getImageFromAssetsFile(mContext, path));
			}
		});
//		effect_listview.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				eAdapter.setSelectItem(arg2);
//
//				final int tmpint = arg2;
//				final int tmpitem = arg1.getWidth();
//				new Handler().postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						effect_listview.scrollTo(tmpitem * (tmpint - 1) - tmpitem / 4);
//					}
//				}, 200);
//
//				Filter_Effect_Info info = effect_list.get(arg2);
//				GPUImageFilter filter = ImageFilterTools.createFilterForType(mContext, info.getFilterType());
//				((EfectFragment) fragments.get(current)).addEffect(filter);
//			}
//		});
//
//		sticker_listview.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				String path = stickerList.get(arg2).getLocal_path();
//				int drawableId = stickerList.get(arg2).getDrawableId();
//				((EfectFragment) fragments.get(current)).addSticker(drawableId, path);
//
//			}
//		});
		images_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mViewPager.setCurrentItem(position, false);
				fragments.get(position).onResume();
				fragments.get(current).onPause();
				current = position;

				iAdapter.setSelected(position);
				final int tmpint = position;
				final int tmpitem = view.getWidth();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						images_listview.scrollTo(tmpitem * (tmpint - 1) - tmpitem / 4);
					}
				}, 200);

			}
		});
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				/*
				 * if (mFilterAdjuster != null) {
				 * mFilterAdjuster.adjust(progress); }
				 */
				// effect_main.requestRender();
			}
		});

	}

	private void initData() {
		boolean flag = false;
		if (mCameraSdkParameterInfo.isSingle_mode() && mCameraSdkParameterInfo.isCroper_image()) {
			flag = true;
		}
		fragments = new ArrayList<Fragment>();
		for (int i = 0; i < imageList.size(); i++) {
			EfectFragment ef1 = EfectFragment.newInstance(imageList.get(i), flag);
			fragments.add(ef1);
		}

		FragmentViewPagerAdapter fAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mViewPager, fragments);
		mViewPager.setAdapter(fAdapter);
		mViewPager.setCurrentItem(0);
		// mViewPager.setOffscreenPageLimit(imageList.size());
		
		iAdapter = new SmallThumbAdapter(mContext, imageList);
		images_listview.setAdapter(iAdapter);
		iAdapter.setSelected(0);
	}

	private void complate(int quality) {
		loading_layout.setVisibility(View.VISIBLE);
		complate_runnable(3 * 1000, quality);
	}

	private void complate_runnable(long delayMillis, final int quality) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				ArrayList<String> list = new ArrayList<String>();
				MLog.e("========complate_runnable:" + mCameraSdkParameterInfo.getRet_type());
				if (mCameraSdkParameterInfo.getRet_type() == 0) {
					// 返回一个路径
					for (int i = 0; i < imageList.size(); i++) {
						Fragment mFragment = fragments.get(i);
						if (mFragment.isAdded()) {
							String path = ((EfectFragment) fragments.get(i)).getFilterImage(quality);
							list.add(path);
							Message msg = handler.obtainMessage();
							msg.what = HANDLE_SAVED;
							msg.obj = path;
							handler.sendMessage(msg);
						} else {
							list.add(imageList.get(i));
						}
					}
				} else {
					// 保存bitmap
					CameraSdkParameterInfo.bitmap_list.clear();
					for (int i = 0; i < imageList.size(); i++) {
						Fragment mFragment = fragments.get(i);
						if (mFragment.isAdded()) {
							Bitmap bitmap = ((EfectFragment) fragments.get(i)).getFilterBitmap();
							CameraSdkParameterInfo.bitmap_list.add(bitmap);
						}
					}
				}
				MLog.e("========complate_runnable.is_net_path:" + mCameraSdkParameterInfo.is_net_path);
				// 如果是网络图片则直接返回
				if (mCameraSdkParameterInfo.is_net_path) {

					Bundle b = new Bundle();
					b.putSerializable(CameraSdkParameterInfo.EXTRA_PARAMETER, mCameraSdkParameterInfo);

					Intent intent = new Intent();
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					PhotoPickActivity.instance.getFilterComplate(list);
				}
				finish();
			}
		};
		new Handler().postDelayed(runnable, delayMillis);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.RequestCode_Croper) {
			// 截图返回
			((EfectFragment) fragments.get(current)).setBitMap();
		} else if (resultCode == Constants.RequestCode_Sticker) {
			if (data != null) {
				Filter_Sticker_Info info = (Filter_Sticker_Info) data.getSerializableExtra("info");
				((EfectFragment) fragments.get(current)).addSticker(0, info.getImage());
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.txt_effect) {
			effect_listview.setVisibility(View.VISIBLE);
			sticker_listview.setVisibility(View.INVISIBLE);
//			setActionBarTitle(getString(R.string.camerasdk_tab_filter));
			tab_effect.setSelected(true);
			tab_sticker.setSelected(false);
			// tab_effect.setTextColor(getResources().getColor(R.color.camerasdk_txt_selected));
			// tab_sticker.setTextColor(getResources().getColor(R.color.camerasdk_txt_normal));
		} else if (id == R.id.txt_sticker) {
			effect_listview.setVisibility(View.INVISIBLE);
			sticker_listview.setVisibility(View.VISIBLE);
//			setActionBarTitle(getString(R.string.camerasdk_tab_sticker));
			tab_effect.setSelected(false);
			tab_sticker.setSelected(true);
			// tab_effect.setTextColor(getResources().getColor(R.color.camerasdk_txt_normal));
			// tab_sticker.setTextColor(getResources().getColor(R.color.camerasdk_txt_selected));
		} else if (id == R.id.txt_cropper) {
			// TODO 裁剪图片
			Constants.bitmap = ((EfectFragment) fragments.get(current)).getCurrentBitMap();
			Intent intent = new Intent();
			intent.setClassName(getApplication(), "com.muzhi.camerasdk.CutActivity");
			startActivityForResult(intent, Constants.RequestCode_Croper);
		} else if (id == R.id.txt_enhance) {
			// TODO 图片增强
			Constants.bitmap = ((EfectFragment) fragments.get(current)).getCurrentBitMap();
			Intent intent = new Intent();
			intent.setClassName(getApplication(), "com.muzhi.camerasdk.PhotoEnhanceActivity");
			startActivityForResult(intent, Constants.RequestCode_Croper);
		} else if (id == R.id.txt_graffiti) {
			// TODO 涂鸦
			Constants.bitmap = ((EfectFragment) fragments.get(current)).getCurrentBitMap();
			Intent intent = new Intent();
			intent.putExtra("path", imageList.get(0));
			intent.setClassName(getApplication(), "com.muzhi.camerasdk.GraffitiActivity");
			startActivityForResult(intent, Constants.RequestCode_Croper);
		} else if (id == R.id.camerasdk_title_imgv_right_icon) {
			// TODO 编辑完成， 保存图片
			// complate();
			saveBmpQualityDialog();
			
		} else if (id == R.id.dialog_quality_high) {
			dismissDialog();
			complate(100);
		} else if (id == R.id.dialog_quality_normal) {
			dismissDialog();
			complate(80);
		} else if (id == R.id.dialog_quality_small) {
			dismissDialog();
			complate(60);
		}
	}
	private Dialog mDialog;
	private void saveBmpQualityDialog() {
		mDialog = new Dialog(this, R.style.camerasdk_custom_dialog);
		View v = View.inflate(mContext, R.layout.camerasdk_dialog_bmp_quality, null);
		TextView high = (TextView) v.findViewById(R.id.dialog_quality_high);
		TextView normal = (TextView) v.findViewById(R.id.dialog_quality_normal);
		TextView small = (TextView) v.findViewById(R.id.dialog_quality_small);
		high.setOnClickListener(this);
		normal.setOnClickListener(this);
		small.setOnClickListener(this);
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(v);
		mDialog.show();
	}
	private void dismissDialog() {
		if(null != mDialog && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
}
