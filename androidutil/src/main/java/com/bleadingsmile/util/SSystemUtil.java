package com.bleadingsmile.util;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by 1500242A on 2016/5/13.
 */
public class SSystemUtil {
    private SSystemUtil() {
        // non-instance
    }

    public static boolean isPermissionGrated(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isStoragePermissionGrated(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED;
    }

    public static boolean isServiceRunning(Context context, Class classType) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (classType.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLocationPermissionGrated(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;
    }


    /**
     * Uri.parse("content://com.sonyericsson.alarm/alarm")
     * Uri.parse("content://com.asus.deskclock/alarm")
     * Uri.parse("content://com.samsung.sec.android.clockpackage/alarm")
     * Uri.parse("content://com.lge.clock.alarmclock.ALProvider/calcus")
     */
    public static List<SAlarm> getAllSetNormalAlarms(Context context, Uri uri) {
        try (Cursor c = context.getContentResolver().query(uri, null, "enabled=1", null, null)) {
            List<SAlarm> alarms = new ArrayList<>();
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        int hour = c.getInt(c.getColumnIndex("hour"));
                        int minutes = c.getInt(c.getColumnIndex("minutes"));
                        int daysofweek = c.getInt(c.getColumnIndex("daysofweek"));
                        if (c.getColumnIndex("repeaton") != -1) { // for ASUS
                            if (c.getInt(c.getColumnIndex("repeaton")) == 0) {
                                daysofweek = 0;
                            }
                        }
                        alarms.add(new SAlarm(hour, minutes, daysofweek));
                    } while (c.moveToNext());
                }
            }
            return alarms;
        } catch (Exception ignore) {
            ignore.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<SAlarm> getAllSetSamsungAlarms(Context context) {
        try (Cursor c = context.getContentResolver().query(Uri.parse("content://com.samsung.sec.android.clockpackage/alarm"), null, "active=1", null, null)) {
            List<SAlarm> alarms = new ArrayList<>();
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String alarmtime = c.getString(c.getColumnIndex("alarmtime"));
                        String daysOfWeek = "";
                        String binaryDaysOfWeekInDB = Integer.toBinaryString(c.getInt(c.getColumnIndex("repeattype")));
                        String daysOfWeekInDB = "";
                        if (binaryDaysOfWeekInDB.length() < 29) {
                            for (int i = 0; i < (29 - binaryDaysOfWeekInDB.length()); i++) {
                                daysOfWeekInDB += "0";
                            }
                        }
                        daysOfWeekInDB += binaryDaysOfWeekInDB;
                        if (daysOfWeekInDB.charAt(26) == '1') {
                            daysOfWeek += daysOfWeekInDB.charAt(0);
                            daysOfWeek += daysOfWeekInDB.charAt(24);
                            daysOfWeek += daysOfWeekInDB.charAt(20);
                            daysOfWeek += daysOfWeekInDB.charAt(16);
                            daysOfWeek += daysOfWeekInDB.charAt(12);
                            daysOfWeek += daysOfWeekInDB.charAt(8);
                            daysOfWeek += daysOfWeekInDB.charAt(4);
                        } else {
                            daysOfWeek = "0";
                        }
                        int hour = Integer.valueOf(alarmtime.substring(0, alarmtime.length() == 4 ? 2 : 1));
                        int minutes = Integer.valueOf(alarmtime.substring(alarmtime.length() == 4 ? 2 : 1, alarmtime.length() == 4 ? 3 : 2));
                        alarms.add(new SAlarm(hour, minutes, Integer.parseInt(daysOfWeek, 2)));
                    } while (c.moveToNext());
                }
            }
            return alarms;
        } catch (Exception ignore) {
            ignore.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static class SAlarm {
        public final int hour;
        public final int minutes;

        /**
         * - 二進制code轉為整數
         * - 二進制順序為 Sun, Sat, Fir, Thu, Wed, Tue, Mon共七個數字 啟用為1,否則為0
         * - 0 為沒有定義星期幾
         * <p>
         * Example
         * 工作日 一，二，三，四，五：
         * 11111 實際值為 31
         * <p>
         * 週末 六，日：
         * 1100000 實際值為 96
         */
        public final int daysofweek;

        public SAlarm(int hour, int minutes, int daysofweek) {
            this.hour = hour;
            this.minutes = minutes;
            this.daysofweek = daysofweek;
        }
    }
}