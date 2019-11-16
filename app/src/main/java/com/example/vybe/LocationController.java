package com.example.vybe;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class LocationController {

    public Location getUserLocation(Context context) {
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
