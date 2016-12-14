package com.bleadingsmile.util.log;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by 1500242A on 2016/8/31.
 */
public class BroadcastLogger extends Logger {
    public static final String ACTION_LOG  = "ACTION_LOG";
    public static final String BUNDLE_MESSAGE = "BUNDLE_MESSAGE";
    public static final String  TAG_DEFAULT = "";

    private Context      mContext;
    private final String mTag;

    public BroadcastLogger(Context context) {
        this(context,TAG_DEFAULT);
    }

    public BroadcastLogger(Context context, String tag){
        this.mContext = context;
        this.mTag = tag;
    }

    @Override
    public void info(String message) {
        sendIntent(message);
    }

    @Override
    public void error(String message) {
        sendIntent(message);
    }

    @Override
    public void debug(String message) {
        sendIntent(message);
    }

    @Override
    public void warning(String message) {
        sendIntent(message);
    }

    private void sendIntent(String message){
        Intent intent = new Intent(ACTION_LOG);
        intent.putExtra(BUNDLE_MESSAGE, message);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }
}
