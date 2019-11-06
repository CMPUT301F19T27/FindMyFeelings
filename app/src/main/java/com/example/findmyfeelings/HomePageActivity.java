package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.widget.Button;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Date;
import java.util.HashMap;

public class HomePageActivity extends AppCompatActivity implements EventFragment.OnFragmentInteractionListener, MoodCustomList.RecyclerViewListener, FilterFragment.OnFragmentInteractionListener {
    private String currentUserEmail;
    private ArrayList<Mood> myMoodDataList;
    private ArrayList<Mood> followingMoodDataList;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private RecyclerView moodList;
    private RecyclerView.Adapter moodAdapter;
    private RecyclerView.LayoutManager moodLayoutManager;
    private FloatingActionButton addMoodButton;
    private Button myMoodListButton;
    private Button followingMoodListButton;
    private Button filterButton;
    private String filter = "";
    private ArrayList<Mood> filteredMyMoodDataList;
    private ArrayList<Mood> filteredFollowingMoodDataList;
    private boolean onMyMoodList;
    private String username;
    BottomNavigationView bottomNavigationView;
    private GPSTracker mGPS = new GPSTracker(this);
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        moodList = findViewById(R.id.my_mood_list);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        addMoodButton = findViewById(R.id.add_mood_button);
        myMoodListButton = findViewById(R.id.my_mood_button);
        followingMoodListButton = findViewById(R.id.following_mood_button);
        filterButton = findViewById(R.id.filter_button);


        /* ** Bottom Navigation Bar ** */
        // from stackoverflow : https://stackoverflow.com/questions/41649494/how-to-remove-icon-animation-for-bottom-navigation-view-in-android

//      BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);      // disable default navigation bar animation
        bottomNavigationView.setSelectedItemId(R.id.ic_feed);   // sets default selected item on opening
        bottomNavigationView.setItemIconTintList(null);         // disables icon tint

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

        int indexEnd = currentUserEmail.indexOf("@");
        username = currentUserEmail.substring(0 , indexEnd);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_map:
                        Intent intent1 = new Intent(HomePageActivity.this, MapActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;

                    case R.id.ic_profile:
                        Intent intent2 = new Intent(HomePageActivity.this, ProfileActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new FilterFragment().show(getSupportFragmentManager(), "ADD_EVENT");
                FilterFragment.newInstance(filter).show(getSupportFragmentManager(), "EDIT_EVENT");
            }
        });
      
        /* Custom List Implementation */
        moodList = findViewById(R.id.my_mood_list);
        myMoodDataList = new ArrayList<>();
        followingMoodDataList = new ArrayList<>();

        filteredMyMoodDataList = new ArrayList<>();
        filteredFollowingMoodDataList = new ArrayList<>();


        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionRef = db.collection("Users");

        /* ** Custom List Implementation ** */
        // use a linear layout manager
        moodLayoutManager = new LinearLayoutManager(this);
        moodList.setLayoutManager(moodLayoutManager);

        // Specify an adapter
        onMyMoodList = true;
        moodAdapter = new MoodCustomList(myMoodDataList, this); // Set to default list
        moodList.setAdapter(moodAdapter);


        // READS MY MOODS FROM DATABASE
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
                        moodAdapter.notifyDataSetChanged();
                    }
                });

        myMoodListButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                addMoodButton.setVisibility(View.VISIBLE);
                onMyMoodList = true;

                // Apply filters
                if(filter == "" ) {
                    moodAdapter = new MoodCustomList(myMoodDataList, HomePageActivity.this);
                } else {
                    moodAdapter = new MoodCustomList(filteredMyMoodDataList, HomePageActivity.this);
                }
                moodList.setAdapter(moodAdapter);

                // Change GUI to reflect that we are on a different tab
                myMoodListButton.setBackgroundResource(R.drawable.selected_bar_left);
                myMoodListButton.setTextColor(Color.parseColor("#FFFFFF"));
                followingMoodListButton.setBackgroundResource(R.drawable.unselected_bar_right);
                followingMoodListButton.setTextColor(Color.parseColor("#000000"));
            }
        });

        followingMoodListButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                addMoodButton.setVisibility(View.INVISIBLE);
                onMyMoodList = false;

                // Apply filters
                if(filter == "" ) {
                    moodAdapter = new MoodCustomList(followingMoodDataList, HomePageActivity.this);
                } else {
                    moodAdapter = new MoodCustomList(filteredFollowingMoodDataList, HomePageActivity.this);
                }
                moodList.setAdapter(moodAdapter);

                // Change GUI to reflect that we are on a different tab
                myMoodListButton.setBackgroundResource(R.drawable.unselected_bar_left);
                myMoodListButton.setTextColor(Color.parseColor("#000000"));
                followingMoodListButton.setBackgroundResource(R.drawable.selected_bar_right);
                followingMoodListButton.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

        addMoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EventFragment().show(getSupportFragmentManager(), "ADD_EVENT");
            }
        });
    }

    @Override
    public void onEventAdded(Mood newMood) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

        if (newMood.getLocation() != null) {
            mGPS.getLocation();
            GeoPoint currentLoc = new GeoPoint(mGPS.getLatitude(), mGPS.getLongitude());
            newMood.setLocation(currentLoc);
        }

        HashMap<String, Object> moodData = moodToMap(newMood);

        docRef
                .collection("My Moods")
                .document(newMood.getMoodId())
                .set(moodData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Sample", "Data addition successfull");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Sample", "Data addition failed");
                    }
                });

        myMoodDataList.add(newMood);
        moodAdapter.notifyDataSetChanged();

    }

    @Override
    public void onEventEdited(Mood editedMood, int index) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);


        if (editedMood.getLocation() != null) {
            mGPS.getLocation();
            GeoPoint currentLoc = new GeoPoint(mGPS.getLatitude(), mGPS.getLongitude());
            editedMood.setLocation(currentLoc);
        }

        HashMap<String, Object> moodData = moodToMap(editedMood);

        docRef
                .collection("My Moods")
                .document(editedMood.getMoodId())
                .set(moodData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Sample", "Data edited successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Sample", "Data editing failed");
                    }
                });

        myMoodDataList.set(index, editedMood);
        moodAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEventDeleted(Mood deletedMood) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

        docRef
                .collection("My Moods")
                .document(deletedMood.getMoodId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Sample", "Data deletion successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Sample", "Data deletion failed");
                    }
                });

        myMoodDataList.remove(deletedMood);
        moodAdapter.notifyDataSetChanged();
    }

    public HashMap<String, Object> moodToMap(Mood mood) {
        HashMap<String, Object> moodMap = new HashMap<>();

        mood.setUsername(username);

        moodMap.put("moodId", mood.getMoodId());
        moodMap.put("username", mood.getUsername());
        moodMap.put("dateTime", mood.getDateTime());
        moodMap.put("mood", mood.getMood());
        moodMap.put("reason", mood.getReason());
        moodMap.put("location", mood.getLocation());

        return moodMap;
    }

    @Override
    public void onFilterAdded(String newFilter) {
        filter = newFilter;

        filteredMyMoodDataList.clear();
        filteredFollowingMoodDataList.clear();

        for(Mood mood : myMoodDataList) {
            if(mood.getMood() == filter) {
                filteredMyMoodDataList.add(mood);
            }
        }

        for(Mood mood : followingMoodDataList) {
            if(mood.getMood() == filter) {
                filteredFollowingMoodDataList.add(mood);
            }
        }

        if(filter == "" ) {
            if(onMyMoodList) {
                moodAdapter = new MoodCustomList(myMoodDataList, HomePageActivity.this);
            } else {
                moodAdapter = new MoodCustomList(followingMoodDataList, HomePageActivity.this);
            }
        } else {
            if(onMyMoodList) {
                moodAdapter = new MoodCustomList(filteredMyMoodDataList, HomePageActivity.this);
            } else {
                moodAdapter = new MoodCustomList(filteredFollowingMoodDataList, HomePageActivity.this);
            }
        }
        moodList.setAdapter(moodAdapter);
    }

    @Override
    public void onRecyclerViewClickListener(int position) {
        Mood selectedMood = myMoodDataList.get(position);
        EventFragment.newInstance(selectedMood, position).show(getSupportFragmentManager(), "EDIT_EVENT");
    }
}

