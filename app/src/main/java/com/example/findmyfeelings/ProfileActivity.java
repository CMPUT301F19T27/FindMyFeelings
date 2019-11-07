package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity implements FollowNewUserFragment.OnFragmentInteractionListener {

    BottomNavigationView bottomNavigationView;
    TextView usernameText;
    TextView listHintText;
    Button logoutButton;
    Button followerButton;
    Button followingButton;
    private FloatingActionButton floatingFollowButton;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    private FirebaseAuth.AuthStateListener authStateListener;

    private ArrayList<User> followingDataList;
    private ArrayList<User> followerDataList;
    private RecyclerView followList;
    private RecyclerView.Adapter followAdapter;
    private RecyclerView.LayoutManager followLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        logoutButton = findViewById(R.id.logout_button);
        followerButton = findViewById(R.id.follower_tab_button); //TODO make this not just a button, but a clickable listview
        followingButton = findViewById(R.id.following_tab_button);
        listHintText = findViewById(R.id.list_hint_text);
        usernameText = findViewById(R.id.username_text);
        floatingFollowButton = findViewById(R.id.follow_floating_button);

        bottomNavigationView.setSelectedItemId(R.id.ic_profile);

        // disable default navigation bar animation
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // disables icon tint
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_map:
                        Intent intent1 = new Intent(ProfileActivity.this, MapActivity.class);
                        //intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                       // intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;

                    case R.id.ic_feed:
                        Intent intent2 = new Intent(ProfileActivity.this, HomePageActivity.class);
                        //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(ProfileActivity.this, MainActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 //               loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
            }
        });

        followerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followAdapter = new FollowCustomList(followerDataList);
                followList.setAdapter(followAdapter);
                listHintText.setText("Users who are following you:");
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followAdapter = new FollowCustomList(followingDataList);
                followList.setAdapter(followAdapter);
                listHintText.setText("Users you are following:");
            }
        });

        floatingFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowNewUserFragment().show(getSupportFragmentManager(), "ADD_EVENT");
            }
        });


        /* ** Custom List Implementation ** */
        followList = findViewById(R.id.follow_list);

        followingDataList = new ArrayList<>();
        followerDataList = new ArrayList<>();

        // use a linear layout manager
        followLayoutManager = new LinearLayoutManager(this);
        followList.setLayoutManager(followLayoutManager);

        // Specify an adapter
        followAdapter = new FollowCustomList(followingDataList); // Set to the default
        followList.setAdapter(followAdapter);

        // Test data
        followingDataList.add(new User("myemail0@gmail.com", "childebr", "Cameron", "Hildebrandt"));
        followingDataList.add(new User("myemail1@gmail.com", "jwwhite", "Josh", "White"));
        followingDataList.add(new User("myemail2@gmail.com", "ramy", "Ramy", "Issa"));
        followingDataList.add(new User("myemail3@gmail.com", "kandathi", "Nevil", "Kandathil"));
        followingDataList.add(new User("myemail4@gmail.com", "sandy6", "Sandy", "Huang"));
        followingDataList.add(new User("myemail5@gmail.com", "wentao3", "Travis", "Zhao"));

        followerDataList.add(new User("myemail1@gmail.com", "jwwhite", "Josh", "White"));
        followerDataList.add(new User("myemail2@gmail.com", "ramy", "Ramy", "Issa"));
        followerDataList.add(new User("myemail3@gmail.com", "kandathi", "Nevil", "Kandathil"));

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
//        usernameText.setText(firebaseUser.getEmail()); //TODO make this the current user's username

    }

    @Override
    public void onUserFollowed(User newUser) {
        // TODO add the current user (the person using the app) to the newUser's request list
    }

}
