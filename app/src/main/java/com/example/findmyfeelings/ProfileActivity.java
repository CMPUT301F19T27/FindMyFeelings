package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.protobuf.StringValue;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;


public class ProfileActivity extends AppCompatActivity implements FollowNewUserFragment.OnFragmentInteractionListener, RequestFragment.OnFragmentInteractionListener {

    private BottomNavigationView bottomNavigationView;
    private TextView usernameText;
    private TextView listHintText;
    private Button logoutButton;
    private Button followerButton;
    private Button followingButton;
    private Button requestButton;

    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton floatingFollowButton;


    private FirebaseAuth.AuthStateListener authStateListener;

    private ArrayList<FollowUser> followingDataList;
    private ArrayList<FollowUser> followerDataList;
    private ArrayList<FollowUser> requestDataList;

    private RecyclerView followList;
    private RecyclerView.Adapter followAdapter;
    private RecyclerView.LayoutManager followLayoutManager;
    private FollowUser currentUser;
    private String currentUserEmail;
    private ImageView moodImage;
    private String moodType;

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
        requestButton = findViewById(R.id.requests_button);
        moodImage = findViewById(R.id.mood_emoticon);
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

        // READ FOLLOWING AND FOLLOWER DATA

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

        int indexEnd = currentUserEmail.indexOf("@");
        String username = currentUserEmail.substring(0 , indexEnd);

        usernameText.setText(username);

        db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Users");


        /* ** Custom List Implementation ** */
        followList = findViewById(R.id.follow_list);

        followingDataList = new ArrayList<>();
        followerDataList = new ArrayList<>();
        requestDataList = new ArrayList<>();

        // use a linear layout manager
        followLayoutManager = new LinearLayoutManager(this);
        followList.setLayoutManager(followLayoutManager);

        // Specify an adapter
        //followAdapter = new FollowCustomList(followingDataList); // Set to the default
        //followList.setAdapter(followAdapter);


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
                        //followAdapter.notifyDataSetChanged();
                    }
                });


        // RETRIEVES FOLLOWER USER DATA

        collectionRef
                .document(currentUserEmail)
                .collection("Followers")
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
                    }
                });


        collectionRef
                .document(currentUserEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        String email = document.getId();
                        String username = (String) document.getData().get("username");
                        String firstName = (String) document.getData().get("first_name");
                        String lastName = (String) document.getData().get("last_name");

                        HashMap<String, Object> userMap = (HashMap<String, Object>) document.getData().get("recent_mood");

                        moodType = (String) userMap.get("mood");

                        moodImage.setImageResource(getEmoji(moodType));

                        currentUser = new FollowUser(email, username, firstName, lastName);
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

        floatingFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowNewUserFragment(currentUserEmail, followingDataList).show(getSupportFragmentManager(), "ADD_EVENT");
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RequestFragment(currentUserEmail, requestDataList).show(getSupportFragmentManager(), "REQUESTS");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
//        usernameText.setText(firebaseUser.getEmail()); //TODO make this the current user's username

    }

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

    @Override
    public void onRequestAccepted(FollowUser fUser) {

        db = FirebaseFirestore.getInstance();
        CollectionReference cRef = db.collection("Users");

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

        HashMap<String, Object> currentUserMap = fUser.userToMap();

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
