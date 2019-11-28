package com.example.vybe;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.vybe.util.Constants.ERROR_DIALOG_REQUEST;
import static com.example.vybe.util.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.vybe.util.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class LocationController {

    public static Location getUserLocation(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Please Enable GPS", Toast.LENGTH_SHORT).show();
            return null;
        }
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            Toast.makeText(context, "Sorry, we were unable to find your location", Toast.LENGTH_SHORT).show();
        }
        return location;
    }

}
