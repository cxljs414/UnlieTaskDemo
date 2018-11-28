package com.example.cx.unlietaskdemo.service;

/**
 * Created by yao.zhai on 2017/6/19.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import java.util.List;

public class LocationService implements LocationListener {

    private AppCompatActivity mActivity;

    Location location;
    double latitude, longitude;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 2 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 500; // 1 second

    protected LocationManager locationManager;

    public static int locationSettingActivity=11;

    public LocationService(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    public Location getLocation() {
        //运行时定位权限检查
        if (ActivityCompat.checkSelfPermission(this.mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        List<String> list =locationManager.getAllProviders();
        return location;
    }

    public void startGPSUpdater() {
        locationManager = (LocationManager) mActivity
                .getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        //运行时定位权限检查
        if (ActivityCompat.checkSelfPermission(this.mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (!isGPSEnabled) {
                showSettingsAlert();
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
    }

    public void stopGPSUpdater() {
        if (locationManager != null) {
            locationManager.removeUpdates(LocationService.this);
        }
    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

        alertDialog.setTitle("通知");
        alertDialog.setMessage("请开启GPS定位");
        alertDialog.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mActivity.startActivityForResult(intent,locationSettingActivity);
                    }
                });

        alertDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getAltitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
