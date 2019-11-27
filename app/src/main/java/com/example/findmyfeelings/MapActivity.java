package com.example.findmyfeelings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.io.Resources;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * this class displays the users mood on map
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    //private GoogleMap fMap;
    private ArrayList<Mood> myMoodDataList;
    private ArrayList<String> followerDataList;
    Location mLastKnownLocation;
    private String currentUserEmail;
    private String username;
    private Button myMapButton;
    private Button followingMapButton;
    private boolean myMapSelected = true;

    private boolean mLocationPermissionGranted;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        myMapButton = findViewById(R.id.my_map_button);
        followingMapButton = findViewById(R.id.following_map_button);

        getLocationPermission();

        myMoodDataList = new ArrayList<>();
        followerDataList = new ArrayList<>();

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
                        //intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;

                    case R.id.ic_feed:
                        Intent intent2 = new Intent(MapActivity.this, HomePageActivity.class);
                        //intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);

                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

        int indexEnd = currentUserEmail.indexOf("@");
        username = currentUserEmail.substring(0 , indexEnd);



        myMapButton.setOnClickListener(new View.OnClickListener() {
            //@SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                // Change GUI to show we are alternating User Map and FollowingMap
                myMapButton.setBackgroundResource(R.drawable.user_map_selected);
                followingMapButton.setBackgroundResource(R.drawable.following_map_unselected);

                myMapSelected = true;
                mMap.clear();
                onMapReady(mMap);
            }
        });

        followingMapButton.setOnClickListener(new View.OnClickListener() {
            //@SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {

                // Change GUI to show we are alternating User Map and FollowingMap
                myMapButton.setBackgroundResource(R.drawable.user_map_unselected);
                followingMapButton.setBackgroundResource(R.drawable.following_map_selected);

                myMapSelected = false;
                mMap.clear();
                onMapReady(mMap);

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

        // pans the camera to current location
        updateLocationUI();


        // disable map toolbar on the bottom right corner
        mMap.getUiSettings().setMapToolbarEnabled(false);


        if (myMapSelected) {

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
                                String situation = doc.getData().get("situation").toString();
                                GeoPoint location = (GeoPoint) doc.getData().get("location");

                                Mood rMood = new Mood(moodId, username, dateTime, mood, reason, situation, location);

                                myMoodDataList.add(rMood);
                            }

                            // Offset for clustering
                            double clusterIndex = 0;

                            for (Mood mood : myMoodDataList) {
                                GeoPoint location = mood.getLocation();
                                if (location != null) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 100));

                                    Bitmap bitmap;
                                    switch (mood.getMood()) {
                                        case "Happy":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.happy_face);
                                            break;
                                        case "Angry":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.angry_face);
                                            break;
                                        case "Disgusted":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.disgust_face);
                                            break;
                                        case "Scared":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fear_face);
                                            break;
                                        case "Sad":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sad_face);
                                            break;
                                        case "Surprised":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.surprised_face);
                                            break;
                                        default:
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.null_face);
                                    }

                                    //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.angry_face);
                                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sad_face);
                                    Bitmap bitmapRescale = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                                    BitmapDescriptor aw = BitmapDescriptorFactory.fromBitmap(bitmapRescale);
                                    double offset = clusterIndex / 60000d;
                                    MarkerOptions marker = new MarkerOptions()
                                            .position(new LatLng((location.getLatitude() + offset), (location.getLongitude() + offset)))
                                            .title(mood.getUsername())
                                            .icon(aw)
                                            .flat(false);


                                    mMap.addMarker(marker);
                                    clusterIndex++; // Increment clusterIndex for Offset

                                }
                            }
                        }
                    });
        }

        else if (!myMapSelected){

            db = FirebaseFirestore.getInstance();
            final CollectionReference collectionRef = db.collection("Users");
            collectionRef
                    .document(currentUserEmail)
                    .collection("Following")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        // get follower
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            followerDataList.clear();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                String userName = doc.getData().get("username").toString();

                                followerDataList.add(userName);
                                //System.out.println(userName);
                                
                            }



                        }
                    });

            // Match the follower with the users in the database and get the recent moods of each matched
            final CollectionReference collectionRef2 = db.collection("Users");
            collectionRef2
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            myMoodDataList.clear();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                String follower = doc.getData().get("username").toString();


                                if (followerDataList.contains(follower)){
                                    System.out.println("**ANALYZING**:" + follower); // check which follower is being analyzed
                                    //DocumentReference docRef = db.collection(follower).document("recent_mood");

                                   // Mood rMood = new Mood();

                                    HashMap<String, Object> data = (HashMap<String, Object>)doc.getData().get("recent_mood");

                                    Timestamp timestamp = (Timestamp) data.get("dateTime");
                                    Date dateTime = timestamp.toDate();
                                    String moodId = data.get("moodId").toString();
                                    String mood = data.get("mood").toString();
                                    String reason = data.get("reason").toString();
                                    String situation = data.get("situation").toString();
                                    GeoPoint location = (GeoPoint) data.get("location");

                                    Mood rMood = new Mood(moodId, follower, dateTime, mood, reason, situation, location);
                                    myMoodDataList.add(rMood);

                                }

                            }

                            // Offset for clustering
                            double clusterIndex = 0;

                            for (Mood mood : myMoodDataList) {
                                GeoPoint location = mood.getLocation();
                                if (location != null) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 100));

                                    Bitmap bitmap;
                                    switch (mood.getMood()) {
                                        case "Happy":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.happy_face);
                                            break;
                                        case "Angry":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.angry_face);
                                            break;
                                        case "Disgusted":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.disgust_face);
                                            break;
                                        case "Scared":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fear_face);
                                            break;
                                        case "Sad":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sad_face);
                                            break;
                                        case "Surprised":
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.surprised_face);
                                            break;
                                        default:
                                            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.null_face);
                                    }

                                    //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.angry_face);
                                    //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sad_face);
                                    Bitmap bitmapRescale = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                                    BitmapDescriptor aw = BitmapDescriptorFactory.fromBitmap(bitmapRescale);
                                    double offset = clusterIndex / 60000d;
                                    MarkerOptions marker = new MarkerOptions()
                                            .position(new LatLng((location.getLatitude() + offset), (location.getLongitude() + offset)))
                                            .title(mood.getUsername())
                                            .icon(aw)
                                            .flat(false);


                                    mMap.addMarker(marker);
                                    clusterIndex++; // Increment clusterIndex for Offset

                                }
                            }


                        }
                    });




        }




    }




}






