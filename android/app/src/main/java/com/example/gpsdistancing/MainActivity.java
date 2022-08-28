package com.example.gpsdistancing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {

    private static final String CHANNEL = "com.chinonso.dev/gpsdistancing";

    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;
    String cordinate;

    int
            interval = 1000 * 60 * 1,
            fastestInterval = 1000 * 50;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler((call, result) -> {
                    if (call.method.equals("getCordinate")) {

                        try {
                            GoogleApiClient
                                    googleApiClient = new GoogleApiClient.Builder( this )
                                    .addApi( LocationServices.API )
                                    .build();

                            googleApiClient.connect();

                            //LocationRequest locationRequest = new LocationRequest.Builder(interval).build();
                            com.google.android.gms.location.LocationRequest locationRequest = com.google.android.gms.location.LocationRequest.create()
                                    .setPriority( LocationRequest.QUALITY_BALANCED_POWER_ACCURACY )
                                    .setInterval( interval )
                                    .setFastestInterval( fastestInterval );

                            LocationSettingsRequest.Builder
                                    locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                                    .addLocationRequest( locationRequest );

                            locationSettingsRequestBuilder.setAlwaysShow( false );

                            PendingResult<LocationSettingsResult>
                                    locationSettingsResult = LocationServices.SettingsApi.checkLocationSettings(
                                    googleApiClient, locationSettingsRequestBuilder.build() );

                            if( locationSettingsResult.await().getStatus().getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                                locationSettingsResult.await().getStatus().startResolutionForResult( this, 0 );
                            }

                        } catch( Exception exception ) {
                            // Log exception
                            Log.d("GPS ERROR::", exception.getMessage());
                        }

                        String cordinate = getCordinate();

                        if (cordinate != "last location unknown") {
                            result.success(cordinate);
                        } else {
                            result.error("UNAVAILABLE", cordinate, null);
                        }
                    } else {
                        result.notImplemented();
                    }
                });
    }

    private String getCordinate(){
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
            return "0";
        }


        lm.requestLocationUpdates(lm.GPS_PROVIDER, 0, 0, Loclist);
        @SuppressLint("MissingPermission") Location loc = lm.getLastKnownLocation(lm.GPS_PROVIDER);

        if(loc==null){
            cordinate = "last location unknown";
            Log.d("LOG: ", cordinate);
        }
        else{
            //set Current latitude and longitude
            currentLon=loc.getLongitude();
            currentLat=loc.getLatitude();
            cordinate = "Longitude: " + currentLon + "\n" + "Latitude: " + currentLat;
            Log.d("LOG: ", cordinate);
        }
        return cordinate;
    }

    LocationListener Loclist = new LocationListener(){
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

            //start location manager
            LocationManager lm =(LocationManager) getSystemService(LOCATION_SERVICE);

            //Get last location
            @SuppressLint("MissingPermission") Location loc = lm.getLastKnownLocation(lm.GPS_PROVIDER);

            //Request new location
            lm.requestLocationUpdates(lm.GPS_PROVIDER, 0,0, Loclist);

            //Get new location
            Location loc2 = lm.getLastKnownLocation(lm.GPS_PROVIDER);

            //get the current lat and long
            currentLat = loc.getLatitude();
            currentLon = loc.getLongitude();

            //getCordinate();


            Location locationA = new Location("point A");
            locationA.setLatitude(lastLat);
            locationA.setLongitude(lastLon);

            Location locationB = new Location("point B");
            locationB.setLatitude(currentLat);
            locationB.setLongitude(currentLon);

            double distanceMeters = locationA.distanceTo(locationB);

            double distanceKm = distanceMeters / 1000f;

        }



        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }



        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }



        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub



        }

    };
}
