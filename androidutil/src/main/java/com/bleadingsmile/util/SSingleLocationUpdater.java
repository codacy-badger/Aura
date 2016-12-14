package com.bleadingsmile.util;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;


/**
 * One-shot location updater.
 * using {@link LocationListener} for callback, it will call {@code onLocationChanged}
 * Created by 1500242 on 2015/6/5.
 */
public class SSingleLocationUpdater {
    private final static String SINGLE_LOCATION_UPDATE_ACTION =
            "com.softmobile.location.SINGLE_LOCATION_UPDATE_ACTION";
    private final static long   WAITING_MAXIMUM_MILLIS        = 30000;

    public interface ILocationListener {
        void onLocationChanged(Location location);
        void onFailed();
    }

    private final PendingIntent m_updateIntent;
    private ILocationListener m_locationListener = null;
    private LocationManager   m_locationManager  = null;
    private Criteria          m_criteria         = null;
    private Context           m_context          = null;
    private Location          m_resultLocation   = null;
    private boolean           m_requested        = false;
    private Handler           m_handler          = null;
    private Runnable          m_cancelTask       = null;

    public SSingleLocationUpdater(Context context,
                                  ILocationListener locationListener) {
        this.m_context = context.getApplicationContext();
        this.m_locationManager = (LocationManager) m_context.getSystemService(Context.LOCATION_SERVICE);
        this.m_locationListener = locationListener;

        /** specific low-accuracy for faster possible result, for low-level permission require*/
        this.m_criteria = new Criteria();
        this.m_criteria.setAccuracy(Criteria.ACCURACY_LOW);
        this.m_handler = new Handler();

        Intent updateIntent = new Intent(SINGLE_LOCATION_UPDATE_ACTION);
        this.m_updateIntent = PendingIntent.getBroadcast(context,
                0,
                updateIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void requestUpdate() {
        if (PERMISSION_GRANTED != ContextCompat.checkSelfPermission(m_context, ACCESS_COARSE_LOCATION) || PERMISSION_GRANTED != ContextCompat.checkSelfPermission(m_context, ACCESS_FINE_LOCATION)) {
            return;
        }
        if (m_requested == true) {
            return;
        }
        m_requested = true;
        IntentFilter locIntentFilter = new IntentFilter(SINGLE_LOCATION_UPDATE_ACTION);
        m_context.registerReceiver(m_singleUpdateReceiver, locIntentFilter);
        m_locationManager.requestSingleUpdate(m_criteria, m_updateIntent);
        m_handler.postDelayed(m_cancelTask = new Runnable() {
            @Override
            public void run() {
                cancel();
                m_locationListener.onFailed();
            }
        }, WAITING_MAXIMUM_MILLIS);
    }


    private final BroadcastReceiver m_singleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            m_resultLocation = (Location) intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
            unregisterComponnents();
            if (null != m_resultLocation) {
                m_locationListener.onLocationChanged(m_resultLocation);
            } else {
                m_locationListener.onFailed();
            }
        }
    };

    private void unregisterComponnents() {
        if (false == m_requested) {
            return;
        }
        m_requested = false;
        m_handler.removeCallbacks(m_cancelTask);
        m_cancelTask = null;
        m_context.unregisterReceiver(m_singleUpdateReceiver);
        m_locationManager.removeUpdates(m_updateIntent);
    }

    public void cancel() {
        unregisterComponnents();
    }
}