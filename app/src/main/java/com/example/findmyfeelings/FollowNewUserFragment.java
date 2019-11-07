package com.example.findmyfeelings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FollowNewUserFragment extends DialogFragment implements SearchCustomList.RecyclerViewListener {
//    private static final String ARG_MOOD = "ride";
//    private static final String ARG_INDEX = "index";

    private EditText searchEditText;
    private Button searchButton;
    private ArrayList<User> searchResultsList;
    private ArrayList<User> allUsersList;
    private RecyclerView searchList;
    private RecyclerView.Adapter searchAdapter;
    private RecyclerView.LayoutManager searchLayoutManager;
    private TextView hintText;
    private TextView errorText;
    private OnFragmentInteractionListener listener;
    private User selectedUserToFollow;


    public interface OnFragmentInteractionListener {
        void onUserFollowed(User user);
    }

    static EventFragment newInstance() {
        Bundle args = new Bundle();

        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
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

//        Bundle args = getArguments();


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


        /* ** Custom List Implementation ** */
        searchList = view.findViewById(R.id.user_search_list);
        searchResultsList = new ArrayList<>();

        // Use a linear layout manager
        searchLayoutManager = new LinearLayoutManager(this.getContext());
        searchList.setLayoutManager(searchLayoutManager);

        // Specify an adapter
        searchAdapter = new SearchCustomList(searchResultsList, this);
        searchList.setAdapter(searchAdapter);

        // Test data
//        searchResultsList.add(new User("123@456.ca", "Sup", "test", "input"));


        /* ** Search Bar Implementation ** */
        searchEditText = view.findViewById(R.id.search_editText);
        allUsersList = new ArrayList<>();

        // Test data // TODO Replace with the set of all users from firebase
        allUsersList.add(new User("myemail0@gmail.com", "childebr", "Cameron", "Hildebrandt"));
        allUsersList.add(new User("myemail1@gmail.com", "jwwhite", "Josh", "White"));
        allUsersList.add(new User("myemail2@gmail.com", "ramy", "Ramy", "Issa"));
        allUsersList.add(new User("myemail3@gmail.com", "kandathi", "Nevil", "Kandathil"));
        allUsersList.add(new User("myemail4@gmail.com", "sandy6", "Sandy", "Huang"));
        allUsersList.add(new User("myemail5@gmail.com", "wentao3", "Travis", "Zhao"));
        allUsersList.add(new User("myemail0@gmail.com", "childebr", "2Cameron", "Hildebrandt"));
        allUsersList.add(new User("myemail1@gmail.com", "jwwhite", "2Josh", "White"));
        allUsersList.add(new User("myemail2@gmail.com", "ramy", "2Ramy", "Issa"));
        allUsersList.add(new User("myemail3@gmail.com", "kandathi", "N2evil", "Kandathil"));
        allUsersList.add(new User("myemail4@gmail.com", "sandy6", "2Sandy", "Huang"));
        allUsersList.add(new User("myemail5@gmail.com", "wentao3", "2Travis", "Zhao"));


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResultsList.clear();
                searchList.setVisibility(View.VISIBLE);
                hintText.setVisibility(View.INVISIBLE);
                errorText.setVisibility(View.INVISIBLE);

                String query = searchEditText.getText().toString();

                // Populate the list with all matching results
                for (User currentUser : allUsersList) {
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

        return builder;
    }

    @Override
    public void onRecyclerViewClickListener(int position) {
        Toast.makeText(getContext(), "Selected " + searchResultsList.get(position).getUsername(), Toast.LENGTH_SHORT).show();
        selectedUserToFollow = searchResultsList.get(position);
    }

}