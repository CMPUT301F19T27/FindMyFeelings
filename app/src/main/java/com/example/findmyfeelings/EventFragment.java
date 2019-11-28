package com.example.findmyfeelings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.grpc.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * This class allows us to add/edit/delete our Moods
 *
 */

public class EventFragment extends DialogFragment  {
    private static final String ARG_MOOD = "mood";
    private static final String ARG_INDEX = "index";
    private static final int PICK_IMAGE = 1;

    private ImageView happy;
    private ImageView sad;
    private ImageView angry;
    private ImageView disgusted;
    private ImageView surprised;
    private ImageView scared;
    private String moodSelected = "";
//    private String situationSelected = "Alone";
    private EditText moodType;
    private EditText moodDate;
    private EditText moodTime;
    private EditText moodReason;
    private Spinner situation_spinner;

    private ImageView previewImage;
    private Button uploadPhotoButton;
    private Button removePhotoButton;
    private Uri selectedImage;

    private LinearLayout dateTimePickerGroup;
    private RadioGroup radioSituationGroup;
    private RadioButton aloneSituationButton;
    private RadioButton twoSituationButton;
    private RadioButton groupSituationButton;

    private EditText moodSituation;
    private CheckBox checkLocation;
    private CheckBox checkCustomDate;


    private DatePicker datePicker;
    private TimePicker timePicker;

    private OnFragmentInteractionListener listener;
    private Mood currentMood;
    private int index;

    /**
     * This interface allows us to use the methods add, edit, & delete
     *
     */
    public interface OnFragmentInteractionListener {
        void onEventAdded(Mood newMood, boolean checked, Uri image);
        void onEventEdited(Mood editedMood, int index, boolean checked, Uri image);
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

        checkLocation = view.findViewById(R.id.location_check);
        checkCustomDate = view.findViewById(R.id.date_check);

        moodReason = view.findViewById(R.id.mood_reason_editText);

        datePicker = view.findViewById(R.id.date_picker);
        timePicker = view.findViewById(R.id.time_picker);


        dateTimePickerGroup = view.findViewById(R.id.date_time_picker_group);
//        radioSituationGroup = view.findViewById(R.id.situation_selector);
//        aloneSituationButton = view.findViewById(R.id.radio_alone);
//        twoSituationButton = view.findViewById(R.id.radio_two);
//        groupSituationButton = view.findViewById(R.id.radio_group);
        situation_spinner = view.findViewById(R.id.situation_selector);

        uploadPhotoButton = view.findViewById(R.id.upload_photo_button);
        removePhotoButton = view.findViewById(R.id.remove_photo_button);
        previewImage = view.findViewById(R.id.preview_image);


        timePicker.setIs24HourView(true);

        Bundle args = getArguments();


        ArrayAdapter<CharSequence> situation_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.situationOptions, android.R.layout.simple_spinner_item);

        situation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        situation_spinner.setAdapter(situation_adapter);



        if (args != null) {

            currentMood = (Mood) args.getSerializable(ARG_MOOD);
            index = args.getInt(ARG_INDEX);

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
                    situation_spinner.setSelection(0);
                    break;
                case "With Someone":
                    situation_spinner.setSelection(1);
                    break;
                case "Group":
                    situation_spinner.setSelection(2);
                    break;
                default:
                    Toast.makeText(getContext(), "Failure Loading Situation", Toast.LENGTH_SHORT).show();
                    break;
            }

            moodReason.setText(currentMood.getReason());

            if (currentMood.getLocation() != null) {
                checkLocation.setChecked(true);
            } else {
                checkLocation.setChecked(false);
            }

            if (currentMood.getImageURL() != null) {
                previewImage.setVisibility(View.VISIBLE);
                Picasso.get().load(currentMood.getImageURL()).into(previewImage);
            }

            // Hide the "Use Custom Date" checkbox and just use a custom date because you have to use a custom date
            checkCustomDate.setVisibility(View.INVISIBLE);
            dateTimePickerGroup.setVisibility(View.GONE);
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

        checkCustomDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCustomDate.isChecked()) {
                    dateTimePickerGroup.setVisibility(View.VISIBLE);
                }
                else {
                    dateTimePickerGroup.setVisibility(View.GONE);
                }
            }
        });

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage = null;
                previewImage.setImageURI(selectedImage);
                previewImage.setVisibility(View.GONE);
                removePhotoButton.setVisibility(View.INVISIBLE);
            }
        });

        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Add/Edit Mood")
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

                        if (moodSelected.equals("")) {
                            incompleteData = true;
                            Toast.makeText(getContext(), "Please select a mood!", Toast.LENGTH_SHORT).show();
                        }
                        if (!incompleteData) {

                            Date dateTime = null;

                            int year = 0;
                            int month = 0;
                            int day = 0;
                            int hour = 0;
                            int minute = 0;
                            String dateTimeString = null;

                            if (!(checkCustomDate.isChecked())) {
                                dateTime = Calendar.getInstance().getTime();
                            }
                            else {
                                year = datePicker.getYear();
                                month = datePicker.getMonth();
                                day = datePicker.getDayOfMonth();

                                hour = timePicker.getHour();
                                minute = timePicker.getMinute();

                                dateTimeString = year +"-"+month+"-"+day+" "+hour+":"+minute;

                                try {
                                    dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateTimeString);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            String situation = situation_spinner.getSelectedItem().toString();
                            String newMood = moodSelected;
                            String reason = moodReason.getText().toString();
                            String moodId = newMood + System.currentTimeMillis();

                            GeoPoint location = null;
                            boolean checked = false;

                            if (checkLocation.isChecked()) {
                                checked = true;
                            }

                            Mood mood = new Mood(moodId,"" ,dateTime, newMood, reason, situation, location, null);

                            if (currentMood != null) {
                                listener.onEventEdited(mood, index, checked, selectedImage);
                            } else {
                                listener.onEventAdded(mood, checked, selectedImage);
                            }
                            builder.hide();
                        }
                    }
                });
            }
        });
        return builder;
    }


    private void openGallery()  {
        // Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        // We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, PICK_IMAGE);

    }


    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch(requestCode) {
                case PICK_IMAGE:
                    //data.getData returns the content URI for the selected Image
                    selectedImage = data.getData();
                    previewImage.setImageURI(selectedImage);
                    previewImage.setVisibility(View.VISIBLE);
                    removePhotoButton.setVisibility(View.VISIBLE);
                    break;
            }
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