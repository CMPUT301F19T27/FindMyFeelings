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
    private static final String ARG_MOOD = "ride";
    private static final String ARG_INDEX = "index";

    private EditText searchEditText;
    private Button searchButton;
    private List<User> searchResultsList;
    private RecyclerView searchList;
    private RecyclerView.Adapter searchAdapter;
    private RecyclerView.LayoutManager searchLayoutManager;


    private OnFragmentInteractionListener listener;
    private User currentUser;
    private int index;


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

        Bundle args = getArguments();


        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Follow")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Follow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (currentUser != null) {
                            User user = new User("1234@gmail.com", "username", "Firstname", "Lastname");
                            listener.onUserFollowed(user);
                        }
                    }
                })
                .create();

        /* ** Custom List Implementation ** */
        searchList = view.findViewById(R.id.user_search_list);
        searchResultsList = new ArrayList<>();

        // use a linear layout manager
        searchLayoutManager = new LinearLayoutManager(this.getContext());
        searchList.setLayoutManager(searchLayoutManager);

        // Specify an adapter
        searchAdapter = new SearchCustomList(searchResultsList, this);
        searchList.setAdapter(searchAdapter);

        // Test data
        searchResultsList.add(new User("myemail0@gmail.com", "childebr", "Cameron", "Hildebrandt"));
        searchResultsList.add(new User("myemail1@gmail.com", "jwwhite", "Josh", "White"));
        searchResultsList.add(new User("myemail2@gmail.com", "ramy", "Ramy", "Issa"));
        searchResultsList.add(new User("myemail3@gmail.com", "kandathi", "Nevil", "Kandathil"));
        searchResultsList.add(new User("myemail4@gmail.com", "sandy6", "Sandy", "Huang"));
        searchResultsList.add(new User("myemail5@gmail.com", "wentao3", "Travis", "Zhao"));
        searchResultsList.add(new User("myemail0@gmail.com", "childebr", "2Cameron", "Hildebrandt"));
        searchResultsList.add(new User("myemail1@gmail.com", "jwwhite", "2Josh", "White"));
        searchResultsList.add(new User("myemail2@gmail.com", "ramy", "2Ramy", "Issa"));
        searchResultsList.add(new User("myemail3@gmail.com", "kandathi", "N2evil", "Kandathil"));
        searchResultsList.add(new User("myemail4@gmail.com", "sandy6", "2Sandy", "Huang"));
        searchResultsList.add(new User("myemail5@gmail.com", "wentao3", "2Travis", "Zhao"));

        return builder;
    }

    @Override
    public void onRecyclerViewClickListener(int position) {
//        Mood selectedMood = myMoodDataList.get(position);
//        EventFragment.newInstance(selectedMood, position).show(getSupportFragmentManager(), "EDIT_EVENT");
    }


//    public static boolean isValidFormat(String format, String value) {  // used stackoverflow, attributions shown in README.txt
//        Date date = null;
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(format);
//            date = sdf.parse(value);
//            if (!value.equals(sdf.format(date))) {
//                date = null;
//            }
//        } catch (ParseException ex) {
//            ex.printStackTrace();
//        }
//        return date != null;
//    }

}