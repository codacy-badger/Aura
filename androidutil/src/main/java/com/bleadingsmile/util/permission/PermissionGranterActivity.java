package com.bleadingsmile.util.permission;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;


import com.bleadingsmile.util.SSystemUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

/**
 * Created by Larry Hsiao on 2016/10/26.
 */
public abstract class PermissionGranterActivity extends Activity {
    private static final int REQUEST_CODE = 99123;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissionRequest = getPreferPermissions();
        if (permissionRequest == null) {
            throw new RuntimeException("no permission in argument");
        }
        final List<String> permissionsNeedProvide = new ArrayList<>();
        for (String permission : permissionRequest) {
            if (!SSystemUtil.isPermissionGrated(this, permission)) {
                permissionsNeedProvide.add(permission);
            }
        }

        if (permissionsNeedProvide.size() == 0) {
            onSuccess();
        } else {
            ActivityCompat.requestPermissions(this, permissionsNeedProvide.toArray(new String[permissionsNeedProvide.size()]), REQUEST_CODE);
            if (hasPromotionSkipped(permissionsNeedProvide)) {
                onDenied();
            }
        }
    }

    private boolean hasPromotionSkipped(List<String> permissionsNeedProvide) {
        PermissionGranterPreference preference = new PermissionGranterPreference(this);
        boolean hasSkippedPermission = false;
        for (String permission : permissionsNeedProvide) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                preference.skipPromotion(permission);
                hasSkippedPermission = true;
            }
        }
        return hasSkippedPermission;
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult == PERMISSION_DENIED) {
                    onDenied();
                    return;
                }
            }
            onSuccess();
        }
    }

    protected abstract void onDenied();
    protected abstract void onSuccess();
    protected abstract String[] getPreferPermissions();
}
