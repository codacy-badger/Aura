package com.bleadingsmile.util.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * One-shot permission granter activity, finish no matter what use choose.
 * Created by Larry Hsiao on 2016/10/26.
 */
public class OneShotPermissionGranterActivity extends PermissionGranterActivity {
    private static final String ARG_STRING_ARRAY_PERMISSIONS = "ARG_STRING_ARRAY_PERMISSIONS";

    @Override
    protected void onSuccess() {
        finish();
    }

    @Override
    protected void onDenied() {
        finish();
    }

    @Override
    protected String[] getPreferPermissions() {
        return getIntent().getStringArrayExtra(ARG_STRING_ARRAY_PERMISSIONS);
    }

    /**
     * Valid permissions see {@link Manifest.permission}
     */
    public static void grantPermission(Context context, @NonNull String... permission) {
        Intent intent = new Intent(context, OneShotPermissionGranterActivity.class);
        intent.putExtra(ARG_STRING_ARRAY_PERMISSIONS, permission);
        context.startActivity(intent);
    }
}
