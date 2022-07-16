package com.example.gpsdistancing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

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

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler((call, result) -> {
                    if (call.method.equals("getCordinate")) {
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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


            Location locationA = new Location("point A");
            locationA.setLatitude(lastLat);
            locationA.setLongitude(lastLon);

            Location locationB = new Location("point B");
            locationB.setLatitude(currentLat);
            locationB.setLongitude(currentLon);

            double distanceMeters = locationA.distanceTo(locationB);

            double distanceKm = distanceMeters / 1000f;

            //display.setText(String.format("%.2f Km",distanceKm ));

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
