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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Document;

import java.math.RoundingMode;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements EventFragment.OnFragmentInteractionListener, MoodCustomList.RecyclerViewListener {
  
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
        myMoodDataList = new ArrayList<>();
        followingMoodDataList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionRef = db.collection("Users");

        /* ** Custom List Implementation ** */
        // use a linear layout manager
        moodLayoutManager = new LinearLayoutManager(this);
        moodList.setLayoutManager(moodLayoutManager);

        // Specify an adapter

        moodAdapter = new MoodCustomList(myMoodDataList, this); // Set to default list
        moodList.setAdapter(moodAdapter);

        //User cUser = new User();

       /* collectionRef
                .document(currentUserEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();

                            Mood m = (Mood)document.get("my_moods");
                            moodAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Sample", "Data read failed" + e.toString());
                    }
                });*/



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
        followingMoodDataList.add(new Mood(12,10,19, 16,20, "Sad", ""));
        followingMoodDataList.add(new Mood(12,10,19, 16,20, "Angry", ""));
        followingMoodDataList.add(new Mood(13,10,19, 16,20, "Disgusted", ""));
        followingMoodDataList.add(new Mood(13,10,19, 16,20, "Happy", ""));
        followingMoodDataList.add(new Mood(13,10,19, 16,20, "Surprised", ""));
        followingMoodDataList.add(new Mood(14,11,19, 16,20, "Disgusted", ""));

    }

    @Override
    public void onEventAdded(Mood newMood) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

        docRef.update("my_moods", FieldValue.arrayUnion(newMood));

        myMoodDataList.add(newMood);
        moodAdapter.notifyDataSetChanged();


    }

    // NOT WORKING
    @Override
    public void onEventEdited(Mood editedMood, int index) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

        Mood currentMood = myMoodDataList.get(index);

        docRef.update("my_moods", FieldValue.arrayRemove(currentMood));
        docRef.update("my_moods", FieldValue.arrayUnion(editedMood));

        myMoodDataList.set(index, editedMood);
        moodAdapter.notifyDataSetChanged();

    }

    @Override
    public void onEventDeleted(Mood deletedMood) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Users").document(currentUserEmail);

        docRef.update("my_moods", FieldValue.arrayRemove(deletedMood));

        myMoodDataList.remove(deletedMood);
        moodAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecyclerViewClickListener(int position) {
        Mood selectedMood = myMoodDataList.get(position);
        EventFragment.newInstance(selectedMood, position).show(getSupportFragmentManager(), "EDIT_EVENT");
    }
}

