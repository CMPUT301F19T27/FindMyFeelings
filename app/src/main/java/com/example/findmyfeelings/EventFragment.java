package com.example.findmyfeelings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EventFragment extends DialogFragment {
    private static final String ARG_MOOD = "ride";
    private static final String ARG_INDEX = "index";

    private EditText moodType;
    private EditText moodDate;
    private EditText moodTime;
    private EditText moodReason;
    private CheckBox checkLocation;

    private OnFragmentInteractionListener listener;
    private Mood currentMood;
    private int index;


    public interface OnFragmentInteractionListener{
        void onEventAdded(Mood newMood);
        void onEventEdited(Mood editedMood, int index);
        void onEventDeleted(Mood deletedMood);
    }

    static EventFragment newInstance(Mood mood, int index){
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOOD, mood);
        args.putSerializable(ARG_INDEX, index);

        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement OnFragmentInteractionListener");
        }

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.event_fragment, null);
        moodType=view.findViewById(R.id.mood_type_editText);
        moodDate=view.findViewById(R.id.mood_date_editText);
        moodTime=view.findViewById(R.id.mood_time_editText);
        moodReason=view.findViewById(R.id.mood_reason_editText);
        checkLocation=view.findViewById(R.id.location_check);

        Bundle args = getArguments();

        if (args!=null){
            currentMood=(Mood)args.getSerializable(ARG_MOOD);
            index = args.getInt(ARG_INDEX);
            moodType.setText(currentMood.getMood());
            moodDate.setText(currentMood.getDateString());
            moodTime.setText(currentMood.getTimeString());
            moodReason.setText(currentMood.getReason());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add or Edit Mood")
                .setNeutralButton("Cancel", null)
                .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(currentMood!= null){
                            listener.onEventDeleted(currentMood);
                        }
                    }
                })

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newMood = moodType.getText().toString();
                        String[] date = moodDate.getText().toString().split(":");
                        int day = Integer.parseInt(date[0].trim());
                        int month = Integer.parseInt(date[1].trim());
                        int year = Integer.parseInt(date[2].trim());
                        String[] time = moodTime.getText().toString().split(":");
                        int hour = Integer.parseInt((time[0].trim()));
                        int minute = Integer.parseInt(time[1].trim());
                        String reason = moodReason.getText().toString();

                        Mood mood = new Mood(day, month, year, hour, minute, newMood, reason);

                        if(currentMood != null){
                            listener.onEventEdited(mood, index);
                        } else {
                            listener.onEventAdded(mood);
                        }
                    }
                }).create();

    }






}

