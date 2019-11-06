package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.math.RoundingMode;
import java.net.DatagramPacket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import java.util.ArrayList;


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

    BottomNavigationView bottomNavigationView;
  
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

//        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);      // disable default navigation bar animation
        bottomNavigationView.setSelectedItemId(R.id.ic_feed);   // sets default selected item on opening
        bottomNavigationView.setItemIconTintList(null);         // disables icon tint

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

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
                            Mood rMood = new Mood(moodId, dateTime, mood, reason);

                            myMoodDataList.add(rMood);
                        }
                        moodAdapter.notifyDataSetChanged();
                    }
                });



        myMoodListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            @Override
            public void onClick(View view) {
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

        /**

        // Test data
 /*       myMoodDataList.add(new Mood(22,10,19, 16,20, "Angry", "Null pointer exception happened"));
        myMoodDataList.add(new Mood(23,10,19, 16,20, "Happy", "The code is working"));
        myMoodDataList.add(new Mood(24,10,19, 16,20, "Sad", "I don't know why this error is happening"));
        myMoodDataList.add(new Mood(25,10,19, 16,20, "Surprised", "Only 2 errors!"));
        myMoodDataList.add(new Mood(26,10,19, 16,20, "Scared", ""));
        myMoodDataList.add(new Mood(27,10,19, 16,20, "Disgusted", ""));
        myMoodDataList.add(new Mood(28,10,19, 16,20, "Happy", ""));
        myMoodDataList.add(new Mood(29,10,19, 16,20, "Happy", ""));
        myMoodDataList.add(new Mood(30,10,19, 16,20, "Sad", ""));
        myMoodDataList.add(new Mood(31,10,19, 16,20, "Surprised", "It compiled"));
        myMoodDataList.add(new Mood(1,11,19, 16,20, "Surprised", ""));
        myMoodDataList.add(new Mood(2,11,19, 16,20, "Disgusted", ""));
*/

        /*Date date1 = null;

        SimpleDateFormat tFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            date1 = tFormat.parse("2019-12-12 13:02");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date = new Date();
        //System.out.println(date1.toString());

        followingMoodDataList.add(new Mood(date1, "Sad", ""));
        myMoodDataList.add(new Mood(date1, "Happy", "asdf"));*/

        /*followingMoodDataList.add(new Mood("2019/11/12", "12:17", "Angry", ""));
        followingMoodDataList.add(new Mood("2019/10/13", "14:02", "Disgusted", ""));
        followingMoodDataList.add(new Mood("2019/09/21", "15:32", "Happy", ""));
        followingMoodDataList.add(new Mood("2019/07/01", "18:12", "Surprised", ""));
        followingMoodDataList.add(new Mood(new Date(),, "Disgusted", ""));
       

        
        followingMoodDataList.add(new Mood(12,10,19, 16,20, "Sad", ""));
        followingMoodDataList.add(new Mood(12,10,19, 16,20, "Angry", ""));
        followingMoodDataList.add(new Mood(13,10,19, 16,20, "Disgusted", ""));
        followingMoodDataList.add(new Mood(13,10,19, 16,20, "Happy", ""));
        followingMoodDataList.add(new Mood(13,10,19, 16,20, "Surprised", ""));
        followingMoodDataList.add(new Mood(14,11,19, 16,20, "Disgusted", ""));
*/

    }

    @Override
    public void onEventAdded(Mood newMood) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

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

    // NOT WORKING
    @Override
    public void onEventEdited(Mood editedMood, int index) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

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

        moodMap.put("moodId", mood.getMoodId());
        moodMap.put("dateTime", mood.getDateTime());
        moodMap.put("mood", mood.getMood());
        moodMap.put("reason", mood.getReason());

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

