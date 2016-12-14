package com.bleadingsmile.utildemo;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.bleadingsmile.util.SLocationUtil;

/**
 * Created by larryhsiao on 2016/12/14.
 */

public class EntryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Location location = SLocationUtil.getLastBestLocation(this);
        Log.i("EntryActivity","" + location);
    }
}
