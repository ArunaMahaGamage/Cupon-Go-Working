package com.coupon.go.module.maps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import com.coupon.go.R;
import com.coupon.go.module.arcamera.ARCameraActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;
import java.util.ArrayList;

public class FusedLocationService implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LocationRequest locationRequest;
    public GoogleApiClient googleApiClient;
    private Location location;
    public FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private LocationUpdateListener locUpdateListener;
    private Context context;
    private float distance = 0;


    public interface LocationUpdateListener {
        void onLocationUpdated(Location newLoc, long newTime, long gps_time, float accuracy, double alt, float distance);
    }

    //------------------------ Time Interval to get Location --------------------------//
    public static final long IDLE_GET_LOCATION_INTERVAL = 1000 * 60 * 5;//30 Seconds
    public static final long IDLE_FASTEST_GET_LOCATION_INTERVAL = IDLE_GET_LOCATION_INTERVAL;
    public static final long WORKING_GET_LOCATION_INTERVAL = 1000 * 60;//1 minutes
    public static final long WORKING_FASTEST_GET_LOCATION_INTERVAL = WORKING_GET_LOCATION_INTERVAL;

    public FusedLocationService(Context context) {
        this.context = context;
        buildGoogleApiClient();
    }


    synchronized void buildGoogleApiClient() {
        try {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            if (googleApiClient != null) {
                googleApiClient.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(IDLE_GET_LOCATION_INTERVAL); // Update location every second
            locationRequest.setFastestInterval(WORKING_GET_LOCATION_INTERVAL);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,  this);
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if(location == null)
                location = fusedLocationProviderApi.getLastLocation(googleApiClient);

            /*try{
                Util.printLog("lat = " + location.getLatitude());
                Util.printLog("lng = " + location.getLongitude());
            }catch (Exception e){
                e.printStackTrace();
            }*/

            startLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        try {

            if (location != null) {
                /*if (this.location != null && location != null) {
                distance = this.location.distanceTo(location);
            }*/
                this.location = location;
                long now = System.currentTimeMillis();
                long gps_time = location.getTime();
                float accuracy = location.getAccuracy();
                double alt = location.getAltitude();
                if (locUpdateListener != null) {
                    locUpdateListener.onLocationUpdated(location, now, gps_time, accuracy, alt, distance);
                }


                SharedPreferences sharedPref = context.getSharedPreferences(
                        context.getString(R.string.preference_location), Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(context.getString(R.string.saved_getLatitude), String.valueOf(location.getLatitude()));
                editor.putString(context.getString(R.string.saved_getLongitude), String.valueOf(location.getLongitude()));
                editor.putString(context.getString(R.string.saved_getBearing), String.valueOf(location.getBearing()));
                editor.commit();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Location getLocation() {
        try {
            if (this.location == null) {
                return fusedLocationProviderApi.getLastLocation(googleApiClient);
            } else {
                return this.location;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.location;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    public void setLocUpdateListener(LocationUpdateListener locUpdateListener) {
        this.locUpdateListener = locUpdateListener;
    }

    public void killFusedLocation() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    public interface CurrentLocation {
        public void currentLocation(Location location);
    }


}
