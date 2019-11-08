package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity implements FragmentSendRequest.ActionsendReq, FragmentRecvRequest.ActionrecvReq {

    BottomNavigationView bottomNavigationView;
    TextView usernameText;
    TextView listHintText;
    Button logoutButton;
    Button followerButton;
    Button followingButton;
    Button requests_button;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    RelativeLayout adduser_bar, middle_bar;
    FragmentSendRequest fragSendReq;
    FragmentRecvRequest fragRecvReq;

    private FirebaseAuth.AuthStateListener authStateListener;

    private ArrayList<User> followingDataList;
    private ArrayList<User> followerDataList;
    private RecyclerView followList;
    private RecyclerView.Adapter followAdapter;
    private RecyclerView.LayoutManager followLayoutManager;
    private FirebaseFirestore db;
    private CollectionReference crs;
    ArrayList<String> reqs;

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

        requests_button= findViewById(R.id.requests_button);

        adduser_bar = findViewById(R.id.adduser_bar);
        middle_bar  = findViewById(R.id.middle_bar);;


        bottomNavigationView.setSelectedItemId(R.id.ic_profile);

        // disable default navigation bar animation
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

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


        FloatingActionButton followButton = (FloatingActionButton)findViewById(R.id.follow_button);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                middle_bar.setVisibility(View.INVISIBLE);
                adduser_bar.setVisibility(View.VISIBLE);

                if (fragSendReq == null) {
                    fragSendReq = new FragmentSendRequest();
                }
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.adduser_bar, fragSendReq, "fragSendReq").commit();

            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
//        usernameText.setText(firebaseUser.getEmail()); //TODO make this the current user's username

        db = FirebaseFirestore.getInstance();
        crs = db.collection("Users");

        final DocumentReference drs = crs.document(firebaseUser.getEmail());

        drs.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot dss, @Nullable FirebaseFirestoreException e) {
                reqs =( ArrayList<String>)dss.get("requests");
                if (reqs==null) {
                    requests_button.setText("Requests:0");
                } else {
                    requests_button.setText("Requests:"+reqs.size());
                }
            }
        });

        requests_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reqs.size()==0)
                    return;

                middle_bar.setVisibility(View.INVISIBLE);
                adduser_bar.setVisibility(View.VISIBLE);

                if (fragRecvReq == null) {
                    fragRecvReq = new FragmentRecvRequest();
                }
                fragRecvReq.setReqUserName( reqs.get(0));
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.adduser_bar, fragRecvReq, "fragRecvReq").commit();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void sendReq(String userName) {




        try {
            final DocumentReference drs = crs.document(userName);

            drs.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot dss, @Nullable FirebaseFirestoreException e) {

                    ArrayList<String> ss = (ArrayList<String>) dss.get("requests");
                    if (ss == null) {
                        ss = new ArrayList<String>();
                        ss.add(firebaseUser.getEmail());

                        Map<String, Object> data = new HashMap<>();
                        data.put("requests", ss);

                        drs.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Sample", "Data addition successfull");
                                backFromRequest();
                                Toast.makeText(ProfileActivity.this, "Request sent!", Toast.LENGTH_LONG).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Sample", "Data addition failed");
                                        Toast.makeText(ProfileActivity.this, "failed to send request", Toast.LENGTH_LONG).show();
                                        backFromRequest();
                                    }
                                });

                    } else {
                        String s = firebaseUser.getEmail();
                        if (!ss.contains(s)) {
                            ss.add(s);
                            drs.update("requests", ss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Sample", "Data addition successfull");
                                    backFromRequest();
                                    Toast.makeText(ProfileActivity.this, "Request sent!", Toast.LENGTH_LONG).show();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Sample", "Data addition failed");
                                            Toast.makeText(ProfileActivity.this, "failed to send request", Toast.LENGTH_LONG).show();
                                            backFromRequest();
                                        }
                                    });


                        } else {
                            Toast.makeText(ProfileActivity.this, "request was already sent!", Toast.LENGTH_LONG).show();
                            backFromRequest();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(ProfileActivity.this, "invalid user name?", Toast.LENGTH_LONG).show();
            backFromRequest();
        }



    }

    @Override
    public void backFromRequest() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragSendReq).addToBackStack(null).commit();
        adduser_bar.setVisibility(View.GONE);
        middle_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void acceptReq(final String userName) {

        handleReq(userName, true);
    }

    private void handleReq(final String userName, final boolean addReq) {
        final DocumentReference drs = crs.document(firebaseUser.getEmail());

        drs.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot dss, @Nullable FirebaseFirestoreException e) {

                final ArrayList<String> followers =( ArrayList<String>)dss.get("followers");
                final ArrayList<String> ss =( ArrayList<String>)dss.get("requests");
                if (ss==null || followers==null) {
                    backFromRequest();
                    return;
                }

                ss.remove(userName);
                if (!followers.contains(userName)) followers.add(userName);

                Map<String, Object> data = new HashMap<>();
                if (addReq) {
                    data.put("followers", followers);
                }

                data.put("requests", ss);

                    drs.set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Sample", "Data addition successfull");
                            backFromRequest();
                            Toast.makeText(ProfileActivity.this, "Request handled!", Toast.LENGTH_LONG).show();

                            requests_button.setText("Request:" + ss.size());
                            followerButton.setText(""+followers.size());
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Sample", "Data addition failed");
                                    Toast.makeText(ProfileActivity.this, "failed to handle request", Toast.LENGTH_LONG).show();
                                    backFromRequest();
                                }
                            });

            }

        });
    }


    @Override
    public void rejectReq(String userName) {
        handleReq(userName, false);
    }

    @Override
    public void backFromRecvPage() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragRecvReq).addToBackStack(null).commit();
        adduser_bar.setVisibility(View.GONE);
        middle_bar.setVisibility(View.VISIBLE);
    }
}
