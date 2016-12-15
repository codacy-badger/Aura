package com.bleadingsmile.util.resource;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by Larry Hsiao on 2016/11/2.
 */
public class StringResourceUtil {
    public static String getString(Context context, Locale locale, @StringRes int stringRes) {
        Resources resources = getResources(context, locale);
        return resources.getString(stringRes);
    }

    public static String getString(Context context, Locale locale, @StringRes int stringRes, Object... args) {
        Resources resources = getResources(context, locale);
        return resources.getString(stringRes, args);
    }

    public static Resources getResources(Context context, Locale locale) {
        Resources standardResources = context.getResources();
        AssetManager assets = standardResources.getAssets();
        DisplayMetrics metrics = standardResources.getDisplayMetrics();
        Configuration configuration = new Configuration(standardResources.getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        }else{
            configuration.locale = locale;
        }
        return new Resources(assets, metrics, configuration);
    }
}
