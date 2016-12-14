package com.bleadingsmile.util.log;

import android.util.Log;

/**
 * Created by 1500242A on 2016/8/31.
 */
public class NativeLogger extends Logger {
    private static final String TAG_DEFAULT = "";
    private String mTag;

    public NativeLogger() {
        this(TAG_DEFAULT);
    }

    public NativeLogger(String tag) {
        this.mTag = tag;
    }

    @Override
    public void info(String message) {
        Log.i(mTag, message);
    }

    @Override
    public void error(String message) {
        Log.e(mTag, message);
    }

    @Override
    public void debug(String message) {
        Log.d(mTag, message);
    }

    @Override
    public void warning(String message) {
        Log.w(mTag, message);
    }
}
