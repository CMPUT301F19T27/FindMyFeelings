package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.grpc.Compressor;
import io.grpc.Context;

/**
 * This class keeps track of events happening on the homepage
 *
 */

public class HomePageActivity extends AppCompatActivity implements EventFragment.OnFragmentInteractionListener, MoodCustomList.RecyclerViewListener, FilterFragment.OnFragmentInteractionListener, Serializable {
    private String currentUserEmail;
    private ArrayList<Mood> myMoodDataList;
    private ArrayList<String> followingDataList;
    private ArrayList<Mood> followingMoodDataList;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
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

    private String username = "Unknown";
    BottomNavigationView bottomNavigationView;

    private StorageTask storageTask;
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

        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);      // disable default navigation bar animation
        bottomNavigationView.setSelectedItemId(R.id.ic_feed);   // sets default selected item on opening
        bottomNavigationView.setItemIconTintList(null);         // disables icon tint

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

//        int indexEnd = currentUserEmail.indexOf("@");
//        username = currentUserEmail.substring(0 , indexEnd);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_map:
                        Intent intent1 = new Intent(HomePageActivity.this, MapActivity.class);
                        //intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        //finish();
                        break;

                    case R.id.ic_profile:
                        Intent intent2 = new Intent(HomePageActivity.this, ProfileActivity.class);
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


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new FilterFragment().show(getSupportFragmentManager(), "ADD_EVENT");
                FilterFragment.newInstance(filter).show(getSupportFragmentManager(), "EDIT_EVENT");
            }
        });


        /* ** Firebase Linkage ** */
        db = FirebaseFirestore.getInstance();
        final CollectionReference cRef = db.collection("Users");

        // Set the current user's username
        cRef
                .document(currentUserEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        username = (String) document.getData().get("username");
                    }
                });
      
        /* Custom List Implementation */
        moodList = findViewById(R.id.my_mood_list);
        myMoodDataList = new ArrayList<>();
        followingMoodDataList = new ArrayList<>();
        followingDataList = new ArrayList<>();

        filteredMyMoodDataList = new ArrayList<>();
        filteredFollowingMoodDataList = new ArrayList<>();

        // Use a linear layout manager
        moodLayoutManager = new LinearLayoutManager(this);
        moodList.setLayoutManager(moodLayoutManager);

        // Specify an adapter
        onMyMoodList = true;
        moodAdapter = new MoodCustomList(myMoodDataList, this); // Set to default list
        moodList.setAdapter(moodAdapter);

        System.out.println("*************************************** TEST 1*********************");
        cRef
                .document(currentUserEmail)
                .collection("My Moods")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        myMoodDataList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Timestamp timestamp = (Timestamp) doc.getData().get("dateTime");
                            Date dateTime = timestamp.toDate();
                            String moodId = doc.getId();
                            String mood = doc.getData().get("mood").toString();
                            String username = doc.getData().get("username").toString();
                            String reason = doc.getData().get("reason").toString();
                            String situation = doc.getData().get("situation").toString();
                            GeoPoint location = (GeoPoint) doc.getData().get("location");
                            String imageURL = (String) doc.getData().get("imageURL");

                            Mood rMood = new Mood(moodId, username, dateTime, mood, reason, situation, location, imageURL);

                            System.out.println("*************************************** TEST 2*********************");
                            myMoodDataList.add(rMood);
                        }

                        // UPDATE RECENT MOOD
                        if(myMoodDataList.size() != 0) {
                            HashMap<String, Object> data = moodToMap(myMoodDataList.get(0));
                            data.put("imageURL", myMoodDataList.get(0).getImageURL());
                            HashMap<String, Object> recentMoodMap = new HashMap<>();
                            recentMoodMap.put("recent_mood", data);

                            cRef
                                    .document(currentUserEmail)
                                    .update(recentMoodMap);
                        }


                        moodAdapter.notifyDataSetChanged();
                    }
                });

        // READ FOLLOWING USERS

        cRef
                .document(currentUserEmail)
                .collection("Following")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        followingDataList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String email = doc.getId();
                            followingDataList.add(email);
                        }
                    }
                });




        cRef
                .orderBy("recent_mood.dateTime", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        followingMoodDataList.clear();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (followingDataList.contains(doc.getId())) {
                                HashMap<String, Object> recentMoodMap = (HashMap<String, Object>) doc.getData().get("recent_mood");

                                Date dateTime = null;
                                try {
                                    dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse( "1970-01-01" + " " + "00:00");
                                } catch (ParseException f) {
                                    f.printStackTrace();
                                }
                                String moodId = "HappyThu Jan 1 00:00:00 UTC 1970";
                                String mood = "Happy";
                                String uName = "Unknown";
                                String reason = "";
                                String situation = "Alone";
                                GeoPoint location = new GeoPoint(0,0);
                                String imageURL = null;

                                if(recentMoodMap.get("moodId") != null) {
                                    Timestamp timestamp = (Timestamp) recentMoodMap.get("dateTime");
                                    dateTime = timestamp.toDate();
                                    moodId = recentMoodMap.get("moodId").toString();
                                    mood = recentMoodMap.get("mood").toString();
                                    uName = recentMoodMap.get("username").toString();
                                    reason = recentMoodMap.get("reason").toString();
                                    situation = recentMoodMap.get("situation").toString();
                                    location = (GeoPoint) recentMoodMap.get("location");
                                    imageURL = (String) recentMoodMap.get("imageURL");

                                }

                                Mood rMood = new Mood(moodId, uName, dateTime, mood, reason, situation, location, imageURL);

                                followingMoodDataList.add(rMood);
                            }
                        }
                    }
                });

        myMoodListButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                addMoodButton.setVisibility(View.VISIBLE);
                onMyMoodList = true;

                // Apply filters
                if(filter.equals("")) {
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
                if(filter.equals("")) {
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

    /**
     * This method adds a Mood to MoodCustomList
     * @param newMood
     */

    @Override
    public void onEventAdded(Mood newMood, boolean checked) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

        newMood.setMoodId(username + newMood.getMoodId());
        newMood.setUsername(username);


        if (checked) {
            mGPS.getLocation();
            GeoPoint currentLoc = new GeoPoint(mGPS.getLatitude(), mGPS.getLongitude());
            newMood.setLocation(currentLoc);
        }
        else {
            newMood.setLocation(null);
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

        // If the view is filtered then make sure to update the filtered view
        if(!filter.equals("")) {
            onFilterAdded(filter);
            Toast.makeText(this, "Make sure to remove the filter to view recently added moods", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * This method edits a Mood from MoodCustomList
     * @param editedMood
     * @param index
     */


    // RECENT MOOD UPDATED ON ADDITION. ASSUMES IT IS THE MOST RECENT MOOD
    @Override
    public void onEventEdited(Mood editedMood, int index, boolean checked) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

        editedMood.setMoodId(username + editedMood.getMoodId());
        editedMood.setUsername(username);

        if (checked) {
            mGPS.getLocation();
            GeoPoint currentLoc = new GeoPoint(mGPS.getLatitude(), mGPS.getLongitude());
            editedMood.setLocation(currentLoc);
        }
        else {
            editedMood.setLocation(null);
        }

        HashMap<String, Object> moodData = moodToMap(editedMood);

        docRef
                .collection("My Moods")
                .document(myMoodDataList.get(index).getMoodId())
                .delete();

        docRef
                .collection("My Moods")
                .document(editedMood.getMoodId())
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

        myMoodDataList.set(index, editedMood);
        moodAdapter.notifyDataSetChanged();

        // If the view is filtered then make sure to update the filtered view
        if(!filter.equals("")) {
            onFilterAdded(filter);
            Toast.makeText(this, "Make sure to remove the filter to view recently added moods", Toast.LENGTH_LONG).show();
        }
    }

//    /**
//     * this function adds mood data to the database
//     * @param mood
//     * @param moodData
//     */
//    public void addData (Mood mood, HashMap<String, Object> moodData) {
//        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);
//
//        docRef
//                .collection("My Moods")
//                .document(mood.getMoodId())
//                .set(moodData)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("Sample", "Data addition successfull");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("Sample", "Data addition failed");
//                    }
//                });
//
//    }

    /**
     * This method deletes a Mood from MoodCustomList
     * @param deletedMood
     *
     */

    // DOES NOT UPDATE RECENT MOOD
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

        if (myMoodDataList.isEmpty()) {
            Mood mood = new Mood(null, null, null, null, null, null, null, null);

            HashMap<String, Object> recentMoodMap = moodToMap(mood);

            docRef
                    .update("recent_mood", recentMoodMap);
        }

        // If the view is filtered then make sure to update the filtered view
        if(!filter.equals("")) {
            onFilterAdded(filter);
            Toast.makeText(this, "Make sure to remove the filter to view recently added moods", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * this function converts the mood data into a Hashmap object
     * @param mood
     * @return
     */
    public HashMap<String, Object> moodToMap(Mood mood) {
        HashMap<String, Object> moodMap = new HashMap<>();

//        mood.setUsername(username);

        moodMap.put("moodId", mood.getMoodId());
        moodMap.put("username", mood.getUsername());
        moodMap.put("dateTime", mood.getDateTime());
        moodMap.put("mood", mood.getMood());
        moodMap.put("reason", mood.getReason());
        moodMap.put("situation", mood.getSituation());
        moodMap.put("location", mood.getLocation());
        moodMap.put("imageURL", mood.getImageURL());

        return moodMap;
    }


    /**
     * This method filters out a selected mood
     * @param newFilter
     */

    @Override
    public void onFilterAdded(String newFilter) {
        filter = newFilter;

        filteredMyMoodDataList.clear();
        filteredFollowingMoodDataList.clear();

        for(Mood mood : myMoodDataList) {
            if(mood.getMood().equals(filter)) {
                filteredMyMoodDataList.add(mood);
            }
        }

        for(Mood mood : followingMoodDataList) {
            if(mood.getMood().equals(filter)) {
                filteredFollowingMoodDataList.add(mood);
            }
        }

        if(filter.equals("")) {
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

    /**
     * This method allows the user to select an item in the RecyclerView onClick
     * @param position
     */

    @Override
    public void onRecyclerViewClickListener(int position) {
        if (onMyMoodList) {
            Mood selectedMood = myMoodDataList.get(position);
            EventFragment.newInstance(selectedMood, position).show(getSupportFragmentManager(), "EDIT_EVENT");
        }
        else {
            Mood selectedMood = followingMoodDataList.get(position);
            FollowingEventFragment.newInstance(selectedMood, position).show(getSupportFragmentManager(), "VIEW_EVENT");
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

//        if (image != null) {
//            StorageReference fileReference = storageReference.child(username).child(System.currentTimeMillis()+"."+getFileExtension(image));
//
//            storageTask = fileReference.putFile(image)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            fileReference.getDownloadUrl()
//                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            String url = null;
//                                            url = uri.toString();
//
//                                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!             " +url);
//
//                                            moodData.put("imageURL", url);
//
//                                            addData(editedMood, moodData);
//                                        }
//                                    });
//                        }
//                    });
//
//        }
//        else {
//            System.out.println("image is NULLLLLLLLLLLLLLLLLLL");
//            moodData.put("imageURL", null);
//            addData(editedMood, moodData);
//        }















//        if (image != null) {
//            StorageReference fileReference = storageReference.child(username).child(System.currentTimeMillis()+"."+getFileExtension(image));
//
//            storageTask =  fileReference.putFile(image)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            fileReference.getDownloadUrl()
//                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            String url = uri.toString();
//                                            moodData.put("imageURL", url);
//
//                                            addData(newMood, moodData);
//                                        }
//                                    });
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(HomePageActivity.this, "Image Upload failed", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//        }
//        else {
//            moodData.put("imageURL", null);
//            addData(newMood, moodData);
//
//        }