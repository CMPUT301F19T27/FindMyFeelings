package com.example.findmyfeelings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FollowingEventFragment extends DialogFragment {
    private static final String ARG_MOOD = "mood";
    private static final String ARG_INDEX = "index";

    private TextView moodType;
    private TextView moodDate;
    private TextView moodTime;
    private TextView moodReason;

    private TextView moodSituation;
    private TextView moodLocation;

    private EventFragment.OnFragmentInteractionListener listener;
    private Mood currentMood;
    private int index;

    static FollowingEventFragment newInstance(Mood mood, int index) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOOD, mood);
        args.putSerializable(ARG_INDEX, index);

        FollowingEventFragment fragment = new FollowingEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EventFragment.OnFragmentInteractionListener) {
            listener = (EventFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.following_event_fragment, null);
        moodType = view.findViewById(R.id.mood_type_Text);
        moodDate = view.findViewById(R.id.mood_date_Text);
        moodTime = view.findViewById(R.id.mood_time_Text);
        moodReason = view.findViewById(R.id.mood_reason_Text);
        moodSituation = view.findViewById(R.id.mood_situation_Text);

        moodLocation = view.findViewById(R.id.mood_location_Text);

        Bundle args = getArguments();

        if (args != null) {
            currentMood = (Mood) args.getSerializable(ARG_MOOD);
            index = args.getInt(ARG_INDEX);

            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(currentMood.getDateTime());

            @SuppressLint("SimpleDateFormat")
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String time = timeFormat.format(currentMood.getDateTime());

            moodType.setText(currentMood.getMood());
            moodDate.setText(date);
            moodTime.setText(time);
            moodReason.setText(currentMood.getReason());
            moodSituation.setText(currentMood.getSituation());

            String geoPoint = currentMood.getLocation().toString();
            int start = geoPoint.indexOf("{");
            int end = geoPoint.indexOf("}");

            moodLocation.setText(geoPoint.substring(start+2, end));
        }

        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle(currentMood.getUsername()+"'s Mood")
                .setNeutralButton("Close", null)
                .create();

        return builder;
    }
}
