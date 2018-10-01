package me.youxiong.person.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import android.content.Context;
import android.graphics.Typeface;

public final class FontsOverride {
	
	public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
		final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
		// final Typeface regular = Typeface.createFromFile(Environment.getExternalStorageDirectory() + File.separator + "huawenxingkai.ttf");
		replaceFont(staticTypefaceFieldName, regular);
	}
	/*
	public static Typeface getFontTypeface() {
		return Typeface.createFromFile(Environment.getExternalStorageDirectory() + File.separator + "huawenxingkai.ttf");
		
	}
	*/
	private static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
		try {
			final Field field = Typeface.class.getDeclaredField(staticTypefaceFieldName);
			field.setAccessible(true);
			
			Field modifiersField = Field.class.getDeclaredField("modifiers");  
			modifiersField.setAccessible(true);  
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			
			field.set(null, newTypeface);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	/*
	public static void setAppFont(ViewGroup mContainer, Typeface mFont, boolean reflect){
		if (mContainer == null || mFont == null)
			return;
		final int mCount = mContainer.getChildCount(); 
		// Loop through all of the children. 
		for (int i = 0; i < mCount; ++i) {
			final View mChild = mContainer.getChildAt(i);
			if (mChild instanceof TextView) {
				// Set the font if it is a TextView.
				((TextView) mChild).setTypeface(mFont);
			} else if (mChild instanceof ViewGroup) {
				// Recursively attempt another ViewGroup.
				setAppFont((ViewGroup) mChild, mFont, true);
			} else if (reflect) {
				try {
					Method mSetTypeface = mChild.getClass().getMethod("setTypeface", Typeface.class);
					mSetTypeface.invoke(mChild, mFont);
				} catch (Exception e) {  Do something... 
				}
			}
		}
	}
	*/
	
}
