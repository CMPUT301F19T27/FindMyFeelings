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

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FollowNewUserFragment extends DialogFragment {
    private static final String ARG_MOOD = "ride";
    private static final String ARG_INDEX = "index";

    private EditText searchEditText;
    private Button searchButton;

    private OnFragmentInteractionListener listener;
    private Mood currentMood;
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

        Bundle args = getArguments();


        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Follow")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (currentMood != null) {
                            User user = new User("1234@gmail.com", "username", "Firstname", "Lastname");
                            listener.onUserFollowed(user);
                        }
                    }
                })
                .create();
        return builder;
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