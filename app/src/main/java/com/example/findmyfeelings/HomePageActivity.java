package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements EventFragment.OnFragmentInteractionListener{

    private ListView moodList;
    private ArrayAdapter<Mood> moodAdapter;
    private List<Mood> moodDataList;
    private FloatingActionButton addButton;
    private FirebaseFirestore db;



    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        bottomNavigationView = findViewById(R.id.bottom_nav_bar);

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


        /* Custom List Implementation */
        moodList = findViewById(R.id.my_mood_list);
        moodDataList = new ArrayList<>();

        moodAdapter = new MoodCustomList(this, moodDataList);
        moodList.setAdapter(moodAdapter);

        addButton = findViewById(R.id.add_event_button);
        final FloatingActionButton addButton = findViewById(R.id.add_event_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EventFragment().show(getSupportFragmentManager(), "ADD_EVENT");
            }
        });

        moodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mood selectedMood = moodDataList.get(i);
                EventFragment.newInstance(selectedMood, i).show(getSupportFragmentManager(), "EDIT_EVENT");
            }
        });




        // Test data
        moodDataList.add(new Mood(22,10,19, 16,20, "Angry", ""));
        moodDataList.add(new Mood(23,10,19, 16,20, "Happy", ""));
        moodDataList.add(new Mood(24,10,19, 16,20, "Sad", ""));
        moodDataList.add(new Mood(25,10,19, 16,20, "Surprised", ""));
        moodDataList.add(new Mood(26,10,19, 16,20, "Scared", ""));
        moodDataList.add(new Mood(27,10,19, 16,20, "Disgusted", ""));


    }

    @Override
    public void onEventAdded(Mood newMood) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionreference = db.collection("Users");

    }

    @Override
    public void onEventEdited(Mood editedMood, int index) {

    }

    @Override
    public void onEventDeleted(Mood deletedMood) {

    }
}

