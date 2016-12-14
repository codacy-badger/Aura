package com.bleadingsmile.util.view;

import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by 1500242A on 2016/10/19.
 */

public class ToastControl {
    private final Context context;
    private Toast previousToast;

    public ToastControl(Context context) {
        this.context = context;
    }

    @MainThread
    public void showToast(String message) {
        if (previousToast != null) {
            previousToast.cancel();
        }
        previousToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        previousToast.show();
    }

    @MainThread
    public void showToast(@StringRes int messageRes) {
        showToast(context.getString(messageRes));
    }
}
