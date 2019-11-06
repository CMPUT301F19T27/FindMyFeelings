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
import androidx.annotation.Nullable;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ArrayList<Mood> myMoodDataList;
    Location mLastKnownLocation;

    private boolean mLocationPermissionGranted;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();

        myMoodDataList = new ArrayList<>();

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

        firebaseAuth = FirebaseAuth.getInstance();
        String currentUserEmail = firebaseAuth.getCurrentUser().getEmail();


        int indexEnd = currentUserEmail.indexOf("@");
        final String username = currentUserEmail.substring(0 , indexEnd);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionRef = db.collection("Users");
        collectionRef
                .document(currentUserEmail)
                .collection("My Moods")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        myMoodDataList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Timestamp timestamp = (Timestamp) doc.getData().get("dateTime");
                            Date dateTime = timestamp.toDate();

                            String moodId = doc.getId();
                            String mood = doc.getData().get("mood").toString();
                            String reason = doc.getData().get("reason").toString();

                            GeoPoint location = (GeoPoint) doc.getData().get("location");

                            Mood rMood = new Mood(moodId, username,dateTime, mood, reason, location);

                            myMoodDataList.add(rMood);
                        }
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

        updateLocationUI();

        // disable map toolbar on the bottom right corner
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //Get Location Data
        //Intent i = getIntent();
        //LatLng newLocation = i.getParcelableExtra("LatLng_data");

        for(Mood mood : myMoodDataList) {
            GeoPoint location = mood.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (location !=  null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 100));

                BitmapDescriptor icon;
                switch(mood.getMood()) {
                    case "Happy":
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.happy_face);
                        break;
                    case "Angry":
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.angry_face);
                        break;
                    case "Disgusted":
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.disgust_face);
                        break;
                    case "Scared":
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.fear_face);
                        break;
                    case "Sad":
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.sad_face);
                        break;
                    case "Surprised":
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.surprised_face);
                        break;
                    default:
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.null_face);
                }

                //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.angry_face);
                MarkerOptions marker = new MarkerOptions().position(latLng).title("Current Location").icon(icon);
                mMap.addMarker(marker);
            }
        }



        // If we got a location then we will add it to the map
       /* if (i != null && newLocation != null) {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(newLocation.latitude, newLocation.longitude), 20));

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.angry_face);
            MarkerOptions marker = new MarkerOptions().position(newLocation).title("Current Location").icon(icon);
            mMap.addMarker(marker);
            //LatLng defaultLoc = new LatLng(newLocation.latitude, newLocation.longitude);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLoc));
        }
*/


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






