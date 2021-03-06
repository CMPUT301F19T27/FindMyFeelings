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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class supports to sent and accept requests
 */
public class ProfileActivity extends AppCompatActivity implements FollowNewUserFragment.OnFragmentInteractionListener, RequestFragment.OnFragmentInteractionListener {

    private BottomNavigationView bottomNavigationView;
    private TextView usernameText;
    private TextView listHintText;
    private Button logoutButton;
    private Button followerButton;
    private Button followingButton;
    private Button requestButton;
    private TextView requestBadge;
    private TextView followHintText;

    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton floatingFollowButton;

    private ArrayList<FollowUser> followingDataList;
    private ArrayList<FollowUser> followerDataList;
    private ArrayList<FollowUser> requestDataList;

    private RecyclerView followList;
    private RecyclerView.Adapter followAdapter;
    private RecyclerView.LayoutManager followLayoutManager;
    private FollowUser currentUser;
    private String currentUserEmail;
    private String username;
    private ImageView moodImage;
    private String moodType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        logoutButton = findViewById(R.id.logout_button);
        followerButton = findViewById(R.id.follower_tab_button);
        followingButton = findViewById(R.id.following_tab_button);
        listHintText = findViewById(R.id.list_hint_text);
        followHintText = findViewById(R.id.follow_hint_text);
        usernameText = findViewById(R.id.username_text);
        floatingFollowButton = findViewById(R.id.follow_floating_button);
        requestButton = findViewById(R.id.requests_button);
        moodImage = findViewById(R.id.mood_emoticon);
        requestBadge = findViewById(R.id.requests_badge);

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
                        //intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                        finish();
                        break;

                    case R.id.ic_feed:
                        Intent intent2 = new Intent(ProfileActivity.this, HomePageActivity.class);
                        //intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                        finish();
                        break;
                }
                return false;
            }
        });

        // READ FOLLOWING AND FOLLOWER DATA

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();
//
//        int indexEnd = currentUserEmail.indexOf("@");
//        String username = currentUserEmail.substring(0 , indexEnd);
//
//        usernameText.setText(username);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionRef = db.collection("Users");

        /* ** Custom List Implementation ** */
        followList = findViewById(R.id.follow_list);

        followingDataList = new ArrayList<>();
        followerDataList = new ArrayList<>();
        requestDataList = new ArrayList<>();

        // Use a linear layout manager
        followLayoutManager = new LinearLayoutManager(this);
        followList.setLayoutManager(followLayoutManager);

        // Show the default data
        followAdapter = new FollowCustomList(followingDataList);
        followList.setAdapter(followAdapter);

        // Specify an adapter
        //followAdapter = new FollowCustomList(followingDataList); // Set to the default
        //followList.setAdapter(followAdapter);

        collectionRef
                .document(currentUserEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        String email = document.getId();
                        String username = (String) document.getData().get("username");
                        usernameText.setText(username);
                        String firstName = (String) document.getData().get("first_name");
                        String lastName = (String) document.getData().get("last_name");

                        HashMap<String, Object> userMap = (HashMap<String, Object>) document.getData().get("recent_mood");

//                        usernameText.setText(username);

                        moodType = (String) userMap.get("mood");

                        moodImage.setImageResource(getEmoji(moodType));

                        currentUser = new FollowUser(email, username, firstName, lastName);
                    }
                });

        // RETRIEVES FOLLOWER USER DATA
        collectionRef
                .document(currentUserEmail)
                .collection("Follower")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        followerDataList.clear();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String email = doc.getId();
                            String username = (String) doc.getData().get("username");
                            String firstName = (String) doc.getData().get("first_name");
                            String lastName = (String) doc.getData().get("last_name");

                            FollowUser followingUser = new FollowUser(email, username, firstName, lastName);
                            followerDataList.add(followingUser);
                        }
                        followerButton.setText(String.valueOf(followerDataList.size()));
                    }
                });

        // RETRIEVES FOLLOWING USERS DATA
        collectionRef
                .document(currentUserEmail)
                .collection("Following")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        followingDataList.clear();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String email = doc.getId();
                            String username = (String) doc.getData().get("username");
                            String firstName = (String) doc.getData().get("first_name");
                            String lastName = (String) doc.getData().get("last_name");

                            FollowUser followingUser = new FollowUser(email, username, firstName, lastName);
                            followingDataList.add(followingUser);
                        }
                        followingButton.setText(String.valueOf(followingDataList.size()));

                        // Hide the hint
                        if(followingDataList.size() > 0) {
                            followHintText.setVisibility(View.GONE);
                        }
                    }
                });

        collectionRef
                .document(currentUserEmail)
                .collection("Requests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        requestDataList.clear();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String email = doc.getId();
                            String username = (String) doc.getData().get("username");
                            String firstName = (String) doc.getData().get("first_name");
                            String lastName = (String) doc.getData().get("last_name");

                            FollowUser followingUser = new FollowUser(email, username, firstName, lastName);
                            requestDataList.add(followingUser);
                        }
                        // Request Badge Implementation
                        if(requestDataList.isEmpty()) {
                            requestBadge.setVisibility(View.INVISIBLE);
                        } else {
                            requestBadge.setVisibility(View.VISIBLE);
                            requestBadge.setText(String.valueOf(requestDataList.size()));
                        }
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

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followAdapter = new FollowCustomList(followingDataList);
                followList.setAdapter(followAdapter);
                listHintText.setText("Users you are following:");

                followingButton.setTextColor(Color.parseColor("#000000"));
                followerButton.setTextColor(Color.parseColor("#9B9B9B"));

                // Control the hint
                if(followingDataList.size() > 0) {
                    followHintText.setVisibility(View.GONE);
                } else {
                    followHintText.setText("You are not following anyone! \n Tap on the '+' button below to search for someone to follow!");
                    followHintText.setVisibility(View.VISIBLE);
                }

            }
        });

        followerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followAdapter = new FollowCustomList(followerDataList);
                followList.setAdapter(followAdapter);
                listHintText.setText("Users who are following you:");

                followingButton.setTextColor(Color.parseColor("#9B9B9B"));
                followerButton.setTextColor(Color.parseColor("#000000"));

                // Control the hint
                if(followerDataList.size() > 0) {
                    followHintText.setVisibility(View.GONE);
                } else {
                    followHintText.setText("No one is following you yet!");
                    followHintText.setVisibility(View.VISIBLE);
                }

            }
        });

        floatingFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowNewUserFragment(currentUserEmail).show(getSupportFragmentManager(), "ADD_EVENT");
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(requestDataList.size() > 0) {
                    new RequestFragment(currentUserEmail, requestDataList).show(getSupportFragmentManager(), "REQUESTS");
                } else {
                    Toast.makeText(ProfileActivity.this, "You have no requests", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    /**
     * adds a request to followed user's request list
     * @param fUser
     */
    @Override
    public void onUserFollowed(FollowUser fUser) {
        db = FirebaseFirestore.getInstance();
        CollectionReference cRef = db.collection("Users");

        HashMap<String, Object> currentUserMap = currentUser.userToMap();
        cRef
                .document(fUser.getEmail())
                .collection("Requests")
                .document(currentUserEmail)
                .set(currentUserMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Request", "Request added successfully");
                    }
                });
    }

    /**
     * adds follower to user following list and adds user to followed user's follower list
     * @param fUser
     */
    @Override
    public void onRequestAccepted(FollowUser fUser) {

        db = FirebaseFirestore.getInstance();
        CollectionReference cRef = db.collection("Users");

        // delete request from user
        cRef
                .document(currentUserEmail)
                .collection("Requests")
                .document(fUser.getEmail())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Request", "Request delete/accepted successfully");
                    }
                });

        HashMap<String, Object> newUserMap = fUser.userToMap();

        // update follower in user
        cRef
                .document(currentUserEmail)
                .collection("Follower")
                .document(fUser.getEmail())
                .set(newUserMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Follow", "Following updated");
                    }
                });

        HashMap<String, Object> currentUserMap = currentUser.userToMap();

        cRef
                .document(fUser.getEmail())
                .collection("Following")
                .document(currentUserEmail)
                .set(currentUserMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Sample", "Data add to other follower data");
                    }
                });

    }

    /**
     * returns the image value of mood type
     * @param mood
     * @return moodImage
     */
    public int getEmoji(String mood) {
        int moodImage;

        if (mood == null) {
            moodImage = R.drawable.null_face;
            return moodImage;
        }
        switch(mood) {
            case "Happy":
                moodImage = R.drawable.happy_face;
                break;
            case "Angry":
                moodImage = R.drawable.angry_face;
                break;
            case "Disgusted":
                moodImage = R.drawable.disgust_face;
                break;
            case "Scared":
                moodImage = R.drawable.fear_face;
                break;
            case "Sad":
                moodImage = R.drawable.sad_face;
                break;
            case "Surprised":
                moodImage = R.drawable.surprised_face;
                break;
            default:
                moodImage = R.drawable.null_face;
        }

        return moodImage;
    }
}
