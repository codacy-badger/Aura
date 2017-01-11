package com.bleadingsmile.util.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SUIDefine {
    private static SUIDefine s_instance = null;

    private int   m_originalHeight  = 0;
    private int   m_originalWidth   = 0;
    private float m_heightScale     = 0;
    private float m_widthScale      = 0;
    private float m_fontScale       = 0;
    private int   m_statusBarHeight = 0;

    private final List<Class<? extends ViewGroup>> m_excludeView;
    private final List<Integer> m_excludeIds;
    private final int m_textSizeUnit;
    private boolean keepRataioIfSquare = true;

    private float m_fScreenScaleDensity = 0;

    private DisplayMetrics m_Dm = null;

    public static SUIDefine getInstance() {
        if (null == s_instance) {
            throw new RuntimeException("please invoke SUIDefine.initialize() first");
        }
        return s_instance;
    }

    public static SUIDefine getInstance(Context context) {
        return getInstance();
    }

    public static void initialize(Context context, boolean systemTextSize, int width, int height) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int tmpWidth = width;
            //noinspection SuspiciousNameCombination
            width = height;
            //noinspection SuspiciousNameCombination
            height = tmpWidth;
        }
        s_instance = new SUIDefine(context.getApplicationContext(), systemTextSize, height, width);

    }

    public static void initialize(Context context, int width, int height) {
        initialize(context, true, width, height);
    }

    private SUIDefine(Context context, boolean systemTextSize, int height, int width) {
        m_originalHeight = height;
        m_originalWidth = width;
        m_textSizeUnit = systemTextSize ? TypedValue.COMPLEX_UNIT_SP : TypedValue.COMPLEX_UNIT_PX;

        m_Dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(m_Dm);
        m_heightScale = (float) m_Dm.heightPixels / m_originalHeight;
        m_widthScale = (float) m_Dm.widthPixels / m_originalWidth;
        m_fontScale = Math.min(m_heightScale, m_widthScale);

        m_fScreenScaleDensity = m_Dm.scaledDensity; //snoykuo 2014/05/02

        m_excludeView = new ArrayList<>();
        m_excludeView.add(ListView.class);
        m_excludeView.add(Spinner.class);
        m_excludeView.add(ViewPager.class);

        m_excludeIds = new ArrayList<>();

        m_statusBarHeight = getStatusBarHeight(context);
    }

    public int getLayoutHeight(double dWeight) {
        return (int) (dWeight * m_heightScale);
    }

    public int getLayoutWidth(double dWeight) {
        double width = (dWeight * m_widthScale);
        return width > 0 && width < 1 ? 1 : (int) width;
    }

    public int getLayoutHeightByTextSize(double dTextSizeDefine, int iTextLength) {
        if (iTextLength <= 0) {
            return 0;
        }

        int iLayoutHeight    = getTextSize(dTextSizeDefine) * iTextLength;
        int iMaxLayoutHeight = getLayoutHeight(m_originalHeight);
        if (iMaxLayoutHeight <= iLayoutHeight) {
            return iMaxLayoutHeight;
        }

        return iLayoutHeight;
    }

    public int getLayoutWidthByTextSize(double dTextSizeDefine, int iTextLength) {
        if (iTextLength <= 0) {
            return 0;
        }

        int iLayoutWidth    = getTextSize(dTextSizeDefine) * iTextLength;
        int iMaxLayoutWidth = getLayoutWidth(m_originalWidth);
        if (iMaxLayoutWidth <= iLayoutWidth) {
            return iMaxLayoutWidth;
        }

        return iLayoutWidth;
    }

    public int getTextSize(double dTextSizeDefine) {
        return (int) (dTextSizeDefine * m_fontScale);
    }

    public void setTextSize(double dTextSizeDefine, TextView textView) {
        textView.setIncludeFontPadding(false);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    public void setTextSize(double dTextSizeDefine, Button button) {
        button.setIncludeFontPadding(false);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    public void setTextSize(double dTextSizeDefine, EditText editText) {
        editText.setIncludeFontPadding(false);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize(dTextSizeDefine));
    }

    public int getScreenHeigh() {
        return m_Dm.heightPixels;
    }

    public int getScreenWidth() {
        return m_Dm.widthPixels;
    }

    public DisplayMetrics getDisplayMetrics() {
        return m_Dm;
    }

    public float getScaleDensity() {
        return m_fScreenScaleDensity;
    }

    public void setMarginStart(View view, int start) {
        setMargins(view, start, 0, 0, 0);
    }

    public void setMarginEnd(View view, int end) {
        setMargins(view, 0, 0, end, 0);
    }

    public void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN && (left != 0 && top == 0 && right == 0 && bottom == 0)) {
                ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMarginStart(getLayoutWidth(left));
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN && (left == 0 && top == 0 && right != 0 && bottom == 0)) {
                ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMarginEnd(getLayoutWidth(right));
                return;
            }

            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(
                    getLayoutWidth(left),
                    getLayoutHeight(top),
                    getLayoutWidth(right),
                    getLayoutHeight(bottom));
        } else {
            throw new RuntimeException("This view can not be set margins");
        }
    }

    public void setPadding(View view, int left, int top, int right, int bottom) {
        view.setPadding(
                getLayoutWidth(left),
                getLayoutHeight(top),
                getLayoutWidth(right),
                getLayoutHeight(bottom));
    }

    public void setViewSize(View view, int width, int height) {
        //某些手機才會發現這個問題...
        if (width >= 0) {
            view.getLayoutParams().width = getLayoutWidth(width);
        } else {
            view.getLayoutParams().width = width;
        }
        if (height >= 0) {
            view.getLayoutParams().height = getLayoutHeight(height);
        } else {
            view.getLayoutParams().height = height;
        }
    }

    public void selfAdjustAllDrawnView(final View rootView) {
        ViewTreeObserver observer = rootView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }else{
                            rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        selfAdjustAllView(rootView);
                    }
                });
    }

    public void selfAdjustAllView(View rootView) {
        if (true == m_excludeIds.contains(rootView.getId())) {
            return;
        }

        if (true == isNormalViewGroup(rootView)) {
            for (int i = 0; i < ((ViewGroup) rootView).getChildCount(); i++) {
                selfAdjustAllView(((ViewGroup) rootView).getChildAt(i));
            }
        }

        adjustMargin(rootView);
        adjustViewSize(rootView);
        adjustTextSize(rootView);
        adjustPadding(rootView);
    }

    private void adjustViewSize(View rootView) {
        setViewSize(
                rootView,
                rootView.getLayoutParams().width,
                rootView.getLayoutParams().height);
    }

    private void adjustTextSize(View rootView) {
        if (rootView instanceof TextView) {
            TextView textView = (TextView) rootView;
            textView.setIncludeFontPadding(false);
            textView.setTextSize(m_textSizeUnit, getTextSize(textView.getTextSize()) / m_fScreenScaleDensity);
        }

    }

    private void adjustMargin(View rootView) {
        if (rootView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) rootView.getLayoutParams())
                    .setMargins(
                            getLayoutWidth(((ViewGroup.MarginLayoutParams) rootView.getLayoutParams()).leftMargin),
                            getLayoutHeight(((ViewGroup.MarginLayoutParams) rootView.getLayoutParams()).topMargin),
                            getLayoutWidth(((ViewGroup.MarginLayoutParams) rootView.getLayoutParams()).rightMargin),
                            getLayoutHeight(((ViewGroup.MarginLayoutParams) rootView.getLayoutParams()).bottomMargin)
                    );
        }
    }

    public boolean isNormalViewGroup(View rootView) {
        if (false == rootView instanceof ViewGroup) {
            return false;
        }
        for (Class<? extends ViewGroup> excludeView : m_excludeView) {
            if (rootView.getClass() == excludeView) {
                return false;
            }
        }
        return true;
    }

    public SUIDefine addExcludeView(Class<? extends ViewGroup> adapterView) {
        if (false == m_excludeView.contains(adapterView)) {
            m_excludeView.add(adapterView);
        }
        return this;
    }

    public SUIDefine addExcludeView(@IdRes int viewId) {
        if (false == m_excludeIds.contains(viewId)) {
            m_excludeIds.add(viewId);
        }
        return this;
    }

    private void adjustPadding(View rootView) {
        if (false == rootView instanceof Spinner && false == rootView instanceof EditText) {
            setPadding(
                    rootView,
                    rootView.getPaddingLeft(),
                    rootView.getPaddingTop(),
                    rootView.getPaddingRight(),
                    rootView.getPaddingBottom());
        }
    }

    /**
     * 取得StatusBar的高度
     */
    private static int getStatusBarHeight(Context context) {
        if (null == context)
            return 0;

        int height = 0;

        // 法一
        // http://blog.csdn.net/devilnov/article/details/9309659
        Rect frame = new Rect();
        try {
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            height = frame.top;
        } catch (Exception e) {

        }

        if (0 == height) {// 法二
            // http://stackoverflow.com/questions/3407256/height-of-status-bar-in-android
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                height = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return height;
    }

    /**
     * 取得StatusBar的高度
     */
    public int getStatusBarHeight() {
        return m_statusBarHeight;
    }
}