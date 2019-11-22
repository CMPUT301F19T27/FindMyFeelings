package com.example.findmyfeelings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This class allows us to add/edit/delete our Moods
 *
 */

public class EventFragment extends DialogFragment  {
    private static final String ARG_MOOD = "mood";
    private static final String ARG_INDEX = "index";

    private ImageView happy;
    private ImageView sad;
    private ImageView angry;
    private ImageView disgusted;
    private ImageView surprised;
    private ImageView scared;
    private String moodSelected = "";
    private String situationSelected = "Alone";
    private EditText moodType;
    private EditText moodDate;
    private EditText moodTime;
    private EditText moodReason;

    private RadioGroup radioSituationGroup;
    private RadioButton aloneSituationButton;
    private RadioButton twoSituationButton;
    private RadioButton groupSituationButton;

    private EditText moodSituation;
    private CheckBox checkLocation;
    private CheckBox checkCustomDate;

    private OnFragmentInteractionListener listener;
    private Mood currentMood;
    private int index;

    /**
     * This interface allows us to use the methods add, edit, & delete
     *
     */
    public interface OnFragmentInteractionListener {
        void onEventAdded(Mood newMood, boolean checked);
        void onEventEdited(Mood editedMood, int index, boolean checked);
        void onEventDeleted(Mood deletedMood);
    }

    /**
     * This method creates an Instance of EventFragment
     */
    static EventFragment newInstance(Mood mood, int index) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOOD, mood);
        args.putSerializable(ARG_INDEX, index);

        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initialises the context for listener
     * @param context
     */
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


    /**
     * This method creates a dialog and shows a dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){

        View view= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_event, null);

        happy = view.findViewById(R.id.happy_emoticon);
        sad = view.findViewById(R.id.sad_emoticon);
        angry = view.findViewById(R.id.angry_emoticon);
        disgusted = view.findViewById(R.id.disgusted_emoticon);
        surprised = view.findViewById(R.id.surprised_emoticon);
        scared = view.findViewById(R.id.scared_emoticon);
        moodReason = view.findViewById(R.id.mood_reason_editText);

        checkLocation = view.findViewById(R.id.location_check);
        checkCustomDate = view.findViewById(R.id.date_check);

        radioSituationGroup = view.findViewById(R.id.situation_selector);
        aloneSituationButton = view.findViewById(R.id.radio_alone);
        twoSituationButton = view.findViewById(R.id.radio_two);
        groupSituationButton = view.findViewById(R.id.radio_group);


//        moodSituation = view.findViewById(R.id.mood_situation_editText);
//        moodType = view.findViewById(R.id.mood_type_editText);
//        moodDate = view.findViewById(R.id.mood_date_editText);
//        moodTime = view.findViewById(R.id.mood_time_editText);

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

            // Set the selected mood
            switch(currentMood.getMood()) {
                case "Happy":
                    happy.setImageResource(R.drawable.happy_face);
                    moodSelected = "Happy";
                    break;
                case "Angry":
                    angry.setImageResource(R.drawable.angry_face);
                    moodSelected = "Angry";
                    break;
                case "Disgusted":
                    disgusted.setImageResource(R.drawable.disgust_face);
                    moodSelected = "Disgusted";
                    break;
                case "Scared":
                    scared.setImageResource(R.drawable.fear_face);
                    moodSelected = "Scared";
                    break;
                case "Sad":
                    sad.setImageResource(R.drawable.sad_face);
                    moodSelected = "Sad";
                    break;
                case "Surprised":
                    surprised.setImageResource(R.drawable.surprised_face);
                    moodSelected = "Surprised";
                    break;
            }

            switch(currentMood.getSituation()) {
                case "Alone":
                    aloneSituationButton.setChecked(true);
                    situationSelected = "Alone";
                    break;
                case "With Someone":
                    twoSituationButton.setChecked(true);
                    situationSelected = "With Someone";
                    break;
                case "Group":
                    groupSituationButton.setChecked(true);
                    situationSelected = "Group";
                    break;
                default:
                    Toast.makeText(getContext(), "Failure Loading Situation", Toast.LENGTH_SHORT).show();
                    break;
            }

//            moodDate.setText(date); //TODO make it set the custom date
//            moodTime.setText(time); //TODO make it set the custom time
            moodReason.setText(currentMood.getReason());
//            moodSituation.setText(currentMood.getSituation());
            if (currentMood.getLocation() != null) {
                checkLocation.setChecked(true);
            }

            // Hide the "Use Custom Date" checkbox and just use a custom date because you have to use a custom date
            checkCustomDate.setVisibility(View.INVISIBLE);
            checkCustomDate.setChecked(true);
        }

        happy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                happy.setImageResource(R.drawable.happy_face);
                sad.setImageResource(R.drawable.sad_face_bw);
                angry.setImageResource(R.drawable.angry_face_bw);
                disgusted.setImageResource(R.drawable.disgust_face_bw);
                surprised.setImageResource(R.drawable.surprised_face_bw);
                scared.setImageResource(R.drawable.fear_face_bw);

                moodSelected = "Happy";
            }
        });

        sad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                happy.setImageResource(R.drawable.happy_face_bw);
                sad.setImageResource(R.drawable.sad_face);
                angry.setImageResource(R.drawable.angry_face_bw);
                disgusted.setImageResource(R.drawable.disgust_face_bw);
                surprised.setImageResource(R.drawable.surprised_face_bw);
                scared.setImageResource(R.drawable.fear_face_bw);

                moodSelected = "Sad";
            }
        });

        angry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                happy.setImageResource(R.drawable.happy_face_bw);
                sad.setImageResource(R.drawable.sad_face_bw);
                angry.setImageResource(R.drawable.angry_face);
                disgusted.setImageResource(R.drawable.disgust_face_bw);
                surprised.setImageResource(R.drawable.surprised_face_bw);
                scared.setImageResource(R.drawable.fear_face_bw);

                moodSelected = "Angry";
            }
        });

        disgusted.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                happy.setImageResource(R.drawable.happy_face_bw);
                sad.setImageResource(R.drawable.sad_face_bw);
                angry.setImageResource(R.drawable.angry_face_bw);
                disgusted.setImageResource(R.drawable.disgust_face);
                surprised.setImageResource(R.drawable.surprised_face_bw);
                scared.setImageResource(R.drawable.fear_face_bw);

                moodSelected = "Disgusted";
            }
        });

        surprised.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                happy.setImageResource(R.drawable.happy_face_bw);
                sad.setImageResource(R.drawable.sad_face_bw);
                angry.setImageResource(R.drawable.angry_face_bw);
                disgusted.setImageResource(R.drawable.disgust_face_bw);
                surprised.setImageResource(R.drawable.surprised_face);
                scared.setImageResource(R.drawable.fear_face_bw);

                moodSelected = "Surprised";
            }
        });

        scared.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                happy.setImageResource(R.drawable.happy_face_bw);
                sad.setImageResource(R.drawable.sad_face_bw);
                angry.setImageResource(R.drawable.angry_face_bw);
                disgusted.setImageResource(R.drawable.disgust_face_bw);
                surprised.setImageResource(R.drawable.surprised_face_bw);
                scared.setImageResource(R.drawable.fear_face);

                moodSelected = "Scared";
            }
        });

        radioSituationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb != null) {
                    situationSelected = rb.getText().toString();
                }

            }
        });


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

                        boolean incompleteData = false;
//                        String[] moods = new String[]{"Happy", "Sad", "Angry", "Disgusted", "Surprised", "Scared"};
//                        List<String> validMoods = Arrays.asList(moods);

//                        String[] situations = new String[]{"alone", "Alone", "with 1", "with 2", "With 1", "With 2", "crowd", "Crowd"};

//                        List<String> validSituations = Arrays.asList(situations);

                        if (moodSelected.equals("")) {
                            incompleteData = true;
                            moodType.setError("Select a mood!");
                        }

//                        if (moodDate.getText().toString().length() == 0) {
//                            flag = true;
//                            moodDate.setError("Enter a date!");
//                        }
//                        if (!isValidFormat("yyyy-MM-dd", moodDate.getText().toString())) {
//                            flag = true;
//                            moodDate.setError("Enter a valid date (yyyy-MM-dd)!");
//                        }

//                        if (moodTime.getText().toString().length() == 0) {
//                            flag = true;
//                            moodTime.setError("Enter a time!");
//                        }
//                        if (!isValidFormat("HH:mm", moodTime.getText().toString())) {
//                            flag = true;
//                            moodTime.setError("Enter a valid time (HH:mm)!");
//                        }

                        if (!incompleteData) {
//                            System.out.println(moodDate+" "+ moodTime);

//                            Toast.makeText(getContext(), Calendar.getInstance().getTime().toString(), Toast.LENGTH_SHORT).show();

                            Date dateTime = null;

                            // Adapter to Feed in Current Date/Time
                            String dateTimeString = Calendar.getInstance().getTime().toString();
                            String[] dateTimeStringSplit = dateTimeString.split(" ");

                            String dateTimeYear = dateTimeStringSplit[5];

                            String dateTimeMonth = dateTimeStringSplit[1];
                            switch(dateTimeMonth) {
                                case "Jan": dateTimeMonth = "1"; break;
                                case "Feb": dateTimeMonth = "2"; break;
                                case "Mar": dateTimeMonth = "3"; break;
                                case "Apr": dateTimeMonth = "4"; break;
                                case "May": dateTimeMonth = "5"; break;
                                case "Jun": dateTimeMonth = "6"; break;
                                case "Jul": dateTimeMonth = "7"; break;
                                case "Aug": dateTimeMonth = "8"; break;
                                case "Sep": dateTimeMonth = "9"; break;
                                case "Oct": dateTimeMonth = "10"; break;
                                case "Nov": dateTimeMonth = "11"; break;
                                case "Dec": dateTimeMonth = "12"; break;
                            }

                            String dateTimeDay = dateTimeStringSplit[2];

                            String dateTimeTime = dateTimeStringSplit[3];

                            String[] dateTimeTimeSplit = dateTimeTime.split(":");
                            String dateTimeTimeHour = dateTimeTimeSplit[0];
                            String dateTimeTimeMinute = dateTimeTimeSplit[1];

                            String inputDate = dateTimeYear + "-" + dateTimeMonth + "-" + dateTimeDay;
                            String inputTime = dateTimeTimeHour + ":" + dateTimeTimeMinute;

                            try {
                                dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse( inputDate + " " + inputTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String situation = situationSelected;
                            String newMood = moodSelected;
                            String reason = moodReason.getText().toString();
                            String moodId = newMood + dateTime.toString();

                            GeoPoint location = null;
                            boolean checked = false;

                            if (checkLocation.isChecked()) {
                                checked = true;
                            }

                            Mood mood = new Mood(moodId,"" ,dateTime, newMood, reason, situation, location);


                            if (currentMood != null) {
                                listener.onEventEdited(mood, index, checked);
                            } else {
                                listener.onEventAdded(mood, checked);
                            }
                            builder.hide();
                        }
                    }
                });
            }
        });
        return builder;
    }


    /**
     * This method checks whether valid format was entered in our dialog
     */
    public static boolean isValidFormat(String format, String value) {  // used stackoverflow, assignment 1
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