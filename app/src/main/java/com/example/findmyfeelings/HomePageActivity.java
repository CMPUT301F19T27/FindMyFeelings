package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

//    private ListView moodList;
    private RecyclerView moodList;
    private RecyclerView.Adapter moodAdapter;
    private RecyclerView.LayoutManager moodLayoutManager;
    private List<Mood> moodDataList = new ArrayList<>();

    private FloatingActionButton addMoodButton;
    private Button myMoodListButton;
    private Button followingMoodListButton;

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


        /* ** Bottom Navigation Bar ** */
        // from stackoverflow : https://stackoverflow.com/questions/41649494/how-to-remove-icon-animation-for-bottom-navigation-view-in-android
        // disable default navigation bar animation
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // sets default selected item on opening
        bottomNavigationView.setSelectedItemId(R.id.ic_feed);
        // disables icon tint
        bottomNavigationView.setItemIconTintList(null);

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


//        bottom_nav_bar.setItemIconTintList(null);

        /* ** Mood List Switcher Implementation ** */



        /* ** Custom List Implementation ** */
        // use a linear layout manager
        moodLayoutManager = new LinearLayoutManager(this);
        moodList.setLayoutManager(moodLayoutManager);

        // Specify an adapter
        moodAdapter = new MoodCustomList(moodDataList);
        moodList.setAdapter(moodAdapter);


        // Test data
        moodDataList.add(new Mood(22,10,19, 16,20, "Angry", ""));
        moodDataList.add(new Mood(23,10,19, 16,20, "Happy", ""));
        moodDataList.add(new Mood(24,10,19, 16,20, "Sad", ""));
        moodDataList.add(new Mood(25,10,19, 16,20, "Surprised", ""));
        moodDataList.add(new Mood(26,10,19, 16,20, "Scared", ""));
        moodDataList.add(new Mood(27,10,19, 16,20, "Disgusted", ""));
        moodDataList.add(new Mood(28,10,19, 16,20, "Happy", ""));
        moodDataList.add(new Mood(29,10,19, 16,20, "Happy", ""));
        moodDataList.add(new Mood(30,10,19, 16,20, "Sad", ""));
        moodDataList.add(new Mood(31,10,19, 16,20, "Surprised", ""));
        moodDataList.add(new Mood(1,11,19, 16,20, "Surprised", ""));
        moodDataList.add(new Mood(2,11,19, 16,20, "Disgusted", ""));


        addMoodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                new AddMoodFragment().show(getSupportFragmentManager(), "ADD_MOOD");
            }
        });
    }
}

