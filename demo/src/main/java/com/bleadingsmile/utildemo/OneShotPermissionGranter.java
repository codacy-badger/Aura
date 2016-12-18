package com.bleadingsmile.utildemo;

import android.Manifest;
import android.content.Context;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.bleadingsmile.util.permission.OneShotPermissionGranterActivity.grantPermission;

/**
 * Created by Larry Hsiao on 2016/12/18.
 */
public class OneShotPermissionGranter extends DemoItem {
    private Context context;

    public OneShotPermissionGranter(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "OneShotGrantPermission";
    }

    @Override
    public void triggerDemo() {
        grantPermission(context, Manifest.permission.ACCESS_WIFI_STATE);
    }
}