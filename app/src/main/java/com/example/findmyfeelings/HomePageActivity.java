package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Document;

import java.math.RoundingMode;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements EventFragment.OnFragmentInteractionListener{
  
    private String currentUserEmail;
    private ArrayList<Mood> moodDataList;
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
      
        /* Custom List Implementation */
        moodList = findViewById(R.id.my_mood_list);
        moodDataList = new ArrayList<>();
        followersDataList = new ArrayList<>();
        followingDataList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

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

        /* ** Custom List Implementation ** */
        // use a linear layout manager
        moodLayoutManager = new LinearLayoutManager(this);
        moodList.setLayoutManager(moodLayoutManager);

        // Specify an adapter
        moodAdapter = new MoodCustomList(moodDataList);
        moodList.setAdapter(moodAdapter);


        addMoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EventFragment().show(getSupportFragmentManager(), "ADD_EVENT");
            }
        });

        // MOOD VIEW/EDIT FRAGMENT (NOT IMPLEMENTED)

       /* moodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mood selectedMood = moodDataList.get(i);
                EventFragment.newInstance(selectedMood, i).show(getSupportFragmentManager(), "EDIT_EVENT");
            }
        });*/

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
}

