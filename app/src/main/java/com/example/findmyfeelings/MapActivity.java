package com.example.findmyfeelings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.io.Resources;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    Location mLastKnownLocation;

    private boolean mLocationPermissionGranted;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);


        // disable default navigation bar animation
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // disables icon tint
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_profile:
                        Intent intent1 = new Intent(MapActivity.this, ProfileActivity.class);
                        //intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;

                    case R.id.ic_feed:
                        Intent intent2 = new Intent(MapActivity.this, HomePageActivity.class);
                        //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });


    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        // disable map toolbar on the bottom right corner
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //Get Location Data
        Intent i = getIntent();
        LatLng newLocation = i.getParcelableExtra("LatLng_data");

        // If we got a location then we will add it to the map
        if (i != null && newLocation != null) {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newLocation.latitude, newLocation.longitude), 20));

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.angry_face);
            MarkerOptions marker = new MarkerOptions().position(newLocation).title("Current Location").icon(icon);
            mMap.addMarker(marker);
            //LatLng defaultLoc = new LatLng(newLocation.latitude, newLocation.longitude);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));



        }



    /**
        // Add a marker in Sydney and move the camera
        LatLng cLocation = new LatLng(53.5444, -113.4909);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.angry_face);

        MarkerOptions marker = new MarkerOptions().position(cLocation).title("Current Location").icon(icon);
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cLocation));
**/

    }




}






