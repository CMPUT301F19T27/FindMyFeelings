package com.example.findmyfeelings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FollowNewUserFragment extends DialogFragment implements SearchCustomList.RecyclerViewListener {

    private EditText searchEditText;
    private Button searchButton;
    private ArrayList<FollowUser> allUsersList;
    private TextView hintText;
    private TextView errorText;
  
    private FollowUser selectedUserToFollow;

    private ArrayList<FollowUser> searchResultsList;
    private RecyclerView searchList;
    private RecyclerView.Adapter searchAdapter;
    private RecyclerView.LayoutManager searchLayoutManager;

    private FirebaseFirestore db;

    private OnFragmentInteractionListener listener;
    private int index;

    private String currentUserEmail;
    private ArrayList<FollowUser> followingDataList;

    public FollowNewUserFragment(String currentUserEmail, ArrayList<FollowUser> followingDataList) {
        this.currentUserEmail = currentUserEmail;
        this.followingDataList = followingDataList;
    }


    public interface OnFragmentInteractionListener {
        void onUserFollowed(FollowUser fUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_follow_new_user, null);
        searchEditText = view.findViewById(R.id.search_editText);
        searchButton = view.findViewById(R.id.search_button);
        searchList = view.findViewById(R.id.user_search_list);
        hintText = view.findViewById(R.id.hint_text);
        errorText = view.findViewById(R.id.error_text);
        searchEditText = view.findViewById(R.id.search_editText);
        allUsersList = new ArrayList<>();

        searchList.setVisibility(View.INVISIBLE);
        hintText.setVisibility(View.VISIBLE);
        errorText.setVisibility(View.INVISIBLE);


        /* ** Database Instantiation ** */
        db = FirebaseFirestore.getInstance();
        CollectionReference cRef = db.collection("Users");

        cRef
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        allUsersList.clear();

                        Log.d("Sample", "************* DATABASE SZ: " + queryDocumentSnapshots.size());

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String email = doc.getId();
                            String username = (String) doc.getData().get("username");
                            String firstName = (String) doc.getData().get("first_name");
                            String lastName = (String) doc.getData().get("last_name");


                            // REQUIRE that all fields are non-null
                            if (email != null && username != null && firstName != null && lastName != null) {
                                FollowUser fUser = new FollowUser(email, username, firstName, lastName);
                                allUsersList.add(fUser);
                            }

                        }
//                        searchAdapter.notifyDataSetChanged();
                    }
                });


        /* ** Custom List Implementation ** */
        searchList = view.findViewById(R.id.user_search_list);
        searchResultsList = new ArrayList<>();

        // Use a linear layout manager
        searchLayoutManager = new LinearLayoutManager(this.getContext());
        searchList.setLayoutManager(searchLayoutManager);

        // Specify an adapter
        searchAdapter = new SearchCustomList(searchResultsList, this);
        searchList.setAdapter(searchAdapter);


    // CAMERON: SEARCH LIST IMPLEMENTATION
        /* ** Search Bar Implementation ** */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResultsList.clear();
                searchList.setVisibility(View.VISIBLE);
                hintText.setVisibility(View.INVISIBLE);
                errorText.setVisibility(View.INVISIBLE);

                String query = searchEditText.getText().toString();

                
                // Populate the list with all matching results
                for (FollowUser currentUser : allUsersList) {
                    String fullName = currentUser.getLastName().toLowerCase() + " " + currentUser.getLastName().toLowerCase();

                    if (currentUser.getUsername().toLowerCase().contains(query.toLowerCase())) {
                        searchResultsList.add(currentUser);
                    } else if (currentUser.getFirstName().toLowerCase().contains(query.toLowerCase())) {
                        searchResultsList.add(currentUser);
                    } else if (currentUser.getLastName().toLowerCase().contains(query.toLowerCase())) {
                        searchResultsList.add(currentUser);
                    } else if(fullName.contains(query.toLowerCase())) {
                        searchResultsList.add(currentUser);
                    }
                }

                // If the user isn't found, display the error message
                if (searchResultsList.size() == 0) {
                    searchList.setVisibility(View.INVISIBLE);
                    errorText.setVisibility(View.VISIBLE);
                }

                // Reset the list
                searchList.setAdapter(searchAdapter);
            }
        });



       /* final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Follow")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Follow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println();
                        FollowUser fUser = searchResultsList.get(index);
                        listener.onUserFollowed(fUser);
                    }
                })
                .create();*/
      
      final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Follow")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Follow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedUserToFollow != null) {
                            listener.onUserFollowed(selectedUserToFollow);
                            Toast.makeText(getContext(), "Sent a request to " + selectedUserToFollow.getUsername(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();

      
      
        return builder;
    }

    @Override
    public void onRecyclerViewClickListener(int position) {
        Toast.makeText(getContext(), "Selected " + searchResultsList.get(position).getUsername(), Toast.LENGTH_SHORT).show();
        selectedUserToFollow = searchResultsList.get(position);
        index = position;
    }

}