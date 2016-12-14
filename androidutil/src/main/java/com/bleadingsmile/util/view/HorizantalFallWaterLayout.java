package com.bleadingsmile.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Source: http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/1104/1899.html
 */
public class HorizantalFallWaterLayout extends ViewGroup {
    private int maxWidth;

    public HorizantalFallWaterLayout(Context context) {
        super(context);
    }

    public HorizantalFallWaterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizantalFallWaterLayout(Context context, AttributeSet attrs,
                                     int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int containorHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        setMeasuredDimension(maxWidth, containorHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int row = 1;
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        int p = getPaddingLeft();
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            left = p + right;
            right = left + width;
            top = p * row + height * (row - 1);
            bottom = top + height;
            if (right > maxWidth) {
                row++;
                left = 0;
                right = 0;
                left = p + right;
                right = left + width;
                top = p * row + height * (row - 1);
                bottom = top + height;
            }
            view.layout(left, top, right, bottom);
        }
    }
}