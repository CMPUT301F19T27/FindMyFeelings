package com.example.findmyfeelings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EventFragment extends DialogFragment  {
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

    private LocationManager locationManager;

    public interface OnFragmentInteractionListener {
        void onEventAdded(Mood newMood);
        void onEventEdited(Mood editedMood, int index);
        void onEventDeleted(Mood deletedMood);
    }

    static EventFragment newInstance(Mood mood, int index) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOOD, mood);
        args.putSerializable(ARG_INDEX, index);

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
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.event_fragment, null);
        moodType=view.findViewById(R.id.mood_type_editText);
        moodDate=view.findViewById(R.id.mood_date_editText);
        moodTime=view.findViewById(R.id.mood_time_editText);
        moodReason=view.findViewById(R.id.mood_reason_editText);
        checkLocation=view.findViewById(R.id.location_check);


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
        }

        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add or Edit Mood")
                .setNeutralButton("Cancel", null)
                .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (currentMood != null) {
                            listener.onEventDeleted(currentMood);
                        }
                    }
                })
                .setPositiveButton("OK", null)
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button buttonPositive = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean flag = false;
                        String[] moods = new String[]{"Happy", "Sad", "Angry", "Disgusted", "Surprised", "Scared"};
                        List<String> validMoods = Arrays.asList(moods);

                        if (moodType.getText().toString().length() == 0) {
                            flag = true;
                            moodType.setError("Enter a mood!");
                        }
                        if (!(validMoods.contains(moodType.getText().toString()))) {
                            flag = true;
                            moodType.setError("Please enter a valid mood");
                        }

                        if (moodDate.getText().toString().length() == 0) {
                            flag = true;
                            moodDate.setError("Enter a date!");
                        }
                        if (!isValidFormat("yyyy-MM-dd", moodDate.getText().toString())) {
                            flag = true;
                            moodDate.setError("Enter a valid date (yyyy-MM-dd)!");
                        }
                        if (moodTime.getText().toString().length() == 0) {
                            flag = true;
                            moodTime.setError("Enter a time!");
                        }
                        if (!isValidFormat("HH:mm", moodTime.getText().toString())) {
                            flag = true;
                            moodTime.setError("Enter a valid time (HH:mm)!");
                        }







                        if (flag == false) {
                            System.out.println(moodDate+" "+ moodTime);

                            Date dateTime = null;
                            try {
                                dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(moodDate.getText().toString() + " " + moodTime.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            String newMood = moodType.getText().toString();

                            String reason = moodReason.getText().toString();
                            String moodId = newMood + dateTime.toString();

                            GeoPoint location = null;

                            if (checkLocation.isChecked()) {
                                location = new GeoPoint(0,0);
                            }

                            Mood mood = new Mood(moodId,"" ,dateTime, newMood, reason, location);

                            if (currentMood != null) {
                                listener.onEventEdited(mood, index);
                            } else {
                                listener.onEventAdded(mood);
                            }
                            builder.hide();
                            // Add Marker if Location is checked
                            itemClicked(checkLocation);
                        }

                    }
                });
            }
        });
        return builder;
    }


    // Check if location is checked
    public void itemClicked(View v) {
        // Check if checkbox if checked
        CheckBox checkBox = (CheckBox)v;
        if (checkBox.isChecked()){
            double newLat = 53.5444;
            double newLong = -113.4909;

            LatLng fromPosition = new LatLng(newLat, newLong);
            Bundle args = new Bundle();

            //args.putParcelable("LatLng_data", fromPosition);

            Intent i = new Intent(getActivity(), MapActivity.class);
            //i.putExtras(args);
            startActivity(i);

        }


    }



    public static boolean isValidFormat(String format, String value) {  // used stackoverflow, attributions shown in README.txt
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }






}