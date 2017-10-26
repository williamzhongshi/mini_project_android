package com.example.william.miniprojectconnexus;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;


public class NearbyPictures extends AppCompatActivity implements View.OnClickListener {
    private static final int INITIAL_REQUEST = 1337;
    public int image_offset = 0;
    private LocationManager locationmanager;
    private LocationListener locationlistener;
    double latitude;
    double longitude;

//    private static final String[] LOCATION_PERMS={
//            Manifest.permission.ACCESS_FINE_LOCATION
//    };
//
//    if(!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
//    {
//        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
//    }
//
//    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;


//    String locationProvider = LocationManager.GPS_PROVIDER;

//
//    /*----Method to Check GPS is enable or disable ----- */
//    private Boolean displayGpsStatus() {
//        ContentResolver contentResolver = getBaseContext()
//                .getContentResolver();
//        boolean gpsStatus = Settings.Secure
//                .isLocationProviderEnabled(contentResolver,
//                        LocationManager.GPS_PROVIDER);
//        if (gpsStatus) {
//            return true;
//
//        } else {
//            return false;
//        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                Log.i("Debug", "My location changed to f" + Double.toString(latitude) + " " + Double.toString(longitude));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        configure_button();
        setContentView(R.layout.activity_nearby_pictures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        image_offset = 0;
        findViewById(R.id.more_result).setOnClickListener(this);
        findViewById(R.id.button_to_view_stream).setOnClickListener(this);
    }
//        locationmanager.requestLocationUpdates("gps", 30000, 100, locationlistener);
//
//        if (locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            double lat = LocationManagerHelper.getLatitude();
//            double lng = LocationManagerHelper.getLongitude();
//
//            Log.i("MyLocation1",Double.toString(LocationManagerHelper.getLatitude())+" "+Double.toString(LocationManagerHelper.getLongitude()));
//
//        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    private void configure_button() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        Location cur_location = locationmanager.getLastKnownLocation("gps");
        if (cur_location == null) {
            locationmanager.requestLocationUpdates("gps", 0, 0, locationlistener);
        }else
        {
            latitude = cur_location.getLatitude();
            longitude = cur_location.getLongitude();
            Log.d("Debug", "Current location " + cur_location.getLatitude() + cur_location.getLongitude());
        }


    }


//
//    public static class LocationManagerHelper implements LocationListener {
//
//    private static double latitude;
//    private static double longitude;
//


//    @Override
//    public void onProviderDisabled(String provider) { }//not used
//
//    @Override
//    public void onProviderEnabled(String provider) { }//not used
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) { }//not used
//
//}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_stream, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        image_offset = 0;
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
//                        , 10);
//            }
//            return;
//        }
//        Location cur_location = locationmanager.getLastKnownLocation("gps");
//        Log.d("Debug", "Current location " + cur_location.getLatitude() + cur_location.getLongitude());
        new Thread(new NearbyPicturesBackend(this, latitude, longitude, image_offset)).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.AllStream) {
            startActivity(new Intent(this, AllStream.class));
            return true;
        }
        if (id == R.id.SearchStream) {
            startActivity(new Intent(this, SearchStream.class));
            return true;
        }
        if (id == R.id.NearbyPictures) {
            startActivity(new Intent(this, NearbyPictures.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_result:
                //TextView tv2 = (TextView) findViewById(R.id.more_result);
                //String text2 = tv2.getText().toString();
                Log.d("Debug", "Button Clicked, more results ");
                image_offset += 8;
                new Thread(new NearbyPicturesBackend(this, latitude, longitude, image_offset)).start();
                break;
            case R.id.button_to_view_stream:
                Log.d("Debug", "Button Clicked, go to view all streams ");
                Intent i = new Intent(this, AllStream.class);
                startActivity(i);
                setContentView(R.layout.activity_all_stream);
                break;
        }
    }

}
