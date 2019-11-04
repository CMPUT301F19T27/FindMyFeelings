package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class HomePageActivity extends AppCompatActivity implements EventFragment.OnFragmentInteractionListener, MoodCustomList.RecyclerViewListener, FilterFragment.OnFragmentInteractionListener {
    private String currentUserEmail;
    private ArrayList<Mood> myMoodDataList;
    private ArrayList<Mood> followingMoodDataList;
    private ArrayList<Follower> followersDataList;
    private ArrayList<Following> followingDataList;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private RecyclerView moodList;
    private RecyclerView.Adapter moodAdapter;
    private RecyclerView.LayoutManager moodLayoutManager;
    private FloatingActionButton addMoodButton;
    private Button myMoodListButton;
    private Button followingMoodListButton;
    private Button filterButton;
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

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);      // disable default navigation bar animation
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
                new FilterFragment().show(getSupportFragmentManager(), "ADD_EVENT");
            }
        });
      
        /* Custom List Implementation */
        moodList = findViewById(R.id.my_mood_list);
        myMoodDataList = new ArrayList<>();
        followingMoodDataList = new ArrayList<>();
        followersDataList = new ArrayList<>();
        followingDataList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

        /* ** Custom List Implementation ** */
        // use a linear layout manager
        moodLayoutManager = new LinearLayoutManager(this);
        moodList.setLayoutManager(moodLayoutManager);

        // Specify an adapter

        moodAdapter = new MoodCustomList(myMoodDataList, this); // Set to default list
        moodList.setAdapter(moodAdapter);

        /*docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            HashMap<String, Object> map = task.getData();
                            for (HashMap.Entry<String, Object> entry : map.entrySet()) {
                                if (entry.getKey().equals("dungeon_group")) {
                                    Log.d("TAG", entry.getValue().toString());
                                }
                            }
                        }

                    }
                })*/


        myMoodListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moodAdapter = new MoodCustomList(myMoodDataList, HomePageActivity.this);
                moodList.setAdapter(moodAdapter);

                myMoodListButton.setBackgroundResource(R.drawable.selected_bar_left);
                myMoodListButton.setTextColor(Color.parseColor("#FFFFFF"));
                followingMoodListButton.setBackgroundResource(R.drawable.unselected_bar_right);
                followingMoodListButton.setTextColor(Color.parseColor("#000000"));
            }
        });


        followingMoodListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moodAdapter = new MoodCustomList(followingMoodDataList, HomePageActivity.this);
                moodList.setAdapter(moodAdapter);

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

        // Test data
        myMoodDataList.add(new Mood(22,10,19, 16,20, "Angry", "Null pointer exception happened"));
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

        followingMoodDataList.add(new Mood(12,10,19, 16,20, "Sad", ""));
        followingMoodDataList.add(new Mood(12,10,19, 16,20, "Angry", ""));
        followingMoodDataList.add(new Mood(13,10,19, 16,20, "Disgusted", ""));
        followingMoodDataList.add(new Mood(13,10,19, 16,20, "Happy", ""));
        followingMoodDataList.add(new Mood(13,10,19, 16,20, "Surprised", ""));
        followingMoodDataList.add(new Mood(14,11,19, 16,20, "Disgusted", ""));

        addMoodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                new EventFragment().show(getSupportFragmentManager(), "ADD_MOOD");
            }
        });
    }

    @Override
    public void onEventAdded(Mood newMood) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

    }

    @Override
    public void onEventEdited(Mood editedMood, int index) {

    }

    @Override
    public void onEventDeleted(Mood deletedMood) {

    }

    @Override
    public void onFilterAdded(String filter) {

    }

    @Override
    public void onRecyclerViewClickListener(int position) {
        Mood selectedMood = myMoodDataList.get(position);
        EventFragment.newInstance(selectedMood, position).show(getSupportFragmentManager(), "EDIT_EVENT");
    }
}

