package com.bleadingsmile.util.permission;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashSet;
import java.util.Set;

/**
 * Class saving the permissions which user denied permission`s promotion with "not shown again" checkbox checked.
 * Created by Larry Hsiao on 2016/10/31.
 */
public final class PermissionGranterPreference {
    private static final String PERMISSION_GRANTER_PREFERENCE = "PERMISSION_GRANTER_PREFERENCE";
    private static final String ARG_STRING_SET_PROMOTION_SKIPPED_PERMISSIONS =
            "ARG_STRING_SET_PROMOTION_SKIPPED_PERMISSIONS";

    private final SharedPreferences preferences;

    public PermissionGranterPreference(Context context) {
        preferences = context.getSharedPreferences(PERMISSION_GRANTER_PREFERENCE, Context.MODE_PRIVATE);
    }

    public boolean shouldShowRequestPermissionRationale(String permissionsToCheck) {
        Set<String> skippedPermissions = getSkippedPermissions();
        return !skippedPermissions.contains(permissionsToCheck);
    }

    public void skipPromotion(String permission) {
        Set<String> skippedPermissions = getSkippedPermissions();
        if (!skippedPermissions.contains(permission)) {
            skippedPermissions.add(permission);
        }
        savePreference(skippedPermissions);
    }

    private Set<String> getSkippedPermissions() {
        return preferences.getStringSet(ARG_STRING_SET_PROMOTION_SKIPPED_PERMISSIONS,new HashSet<String>());
    }

    private void savePreference(Set<String> promotionSkippedPermissions) {
        Editor editor = preferences.edit();
        editor.putStringSet(ARG_STRING_SET_PROMOTION_SKIPPED_PERMISSIONS, promotionSkippedPermissions);
        editor.apply();
    }
}
