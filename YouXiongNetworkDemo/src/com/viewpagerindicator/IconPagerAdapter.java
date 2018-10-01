package com.viewpagerindicator;

public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
    
//    int getPaddingLeft();
//    int getPaddingRight();
//    int getPaddingTop();
//    int getPaddingBottom();
}
