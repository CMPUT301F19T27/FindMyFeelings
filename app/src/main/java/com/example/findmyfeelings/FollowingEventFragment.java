package com.example.findmyfeelings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FollowingEventFragment extends DialogFragment {
    private static final String ARG_MOOD = "mood";
    private static final String ARG_INDEX = "index";

    private TextView moodType;
    private TextView moodDate;
    private TextView moodTime;
    private TextView moodReason;
    private TextView moodUsername;
    private ImageView moodImage;

    private TextView moodSituation;
    private TextView moodLocation;

    private Button swapViewButton;
    private TextView display_map;
    private ImageView display_image;
    private TextView titleViewText;
    private boolean showingImage = true;

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
        moodType = view.findViewById(R.id.mood_type_text);
        moodDate = view.findViewById(R.id.mood_date_text);
        moodTime = view.findViewById(R.id.mood_time_text);
        moodUsername = view.findViewById(R.id.mood_username_text);
        moodReason = view.findViewById(R.id.mood_reason_text);
        moodSituation = view.findViewById(R.id.mood_situation_Text);
        moodImage = view.findViewById(R.id.mood_emoticon);

        swapViewButton = view.findViewById(R.id.view_swap_button);
        display_map = view.findViewById(R.id.display_map);
        display_image = view.findViewById(R.id.display_image);
        titleViewText = view.findViewById(R.id.view_title_text);

//        moodLocation = view.findViewById(R.id.mood_location_Text);

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
            moodSituation.setText(currentMood.getSituation());
            moodImage.setImageResource(getEmoji(currentMood));

            if(currentMood.getUsername().length() > 10) {
                moodUsername.setText(currentMood.getUsername().substring(0,10) + "...");
            } else {
                moodUsername.setText(currentMood.getUsername());
            }

            if(!currentMood.getReason().equals("")) {
                moodReason.setText(currentMood.getReason());
            }

            if(currentMood.getLocation() != null) {
                String geoPoint = currentMood.getLocation().toString();
                int start = geoPoint.indexOf("{");
                int end = geoPoint.indexOf("}");
            }

            boolean hasPhoto = true; // TODO make this the photo object from the database

            if(currentMood.getLocation() == null && hasPhoto) {
                swapViewButton.setVisibility(View.GONE);
            } else if(currentMood.getLocation() != null && !hasPhoto) {
                display_map.setVisibility(View.VISIBLE);
                display_image.setVisibility(View.GONE);
                titleViewText.setText("Location: ");
                swapViewButton.setVisibility(View.GONE);
                showingImage = false;
            } else if(currentMood.getLocation() == null && !hasPhoto) {
                display_map.setVisibility(View.GONE);
                display_image.setVisibility(View.GONE);
                swapViewButton.setVisibility(View.GONE);
                titleViewText.setVisibility(View.GONE);
            }




//            moodLocation.setText(geoPoint.substring(start+2, end));
        }


        swapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showingImage) {
                    display_map.setVisibility(View.VISIBLE);
                    display_image.setVisibility(View.GONE);
                    titleViewText.setText("Location: ");
                    swapViewButton.setText("View Photo");
                    showingImage = false;
                } else {
                    display_map.setVisibility(View.GONE);
                    display_image.setVisibility(View.VISIBLE);
                    titleViewText.setText("Photo: ");
                    swapViewButton.setText("View Map");
                    showingImage = true;
                }
            }
        });


        String displayUsername = currentMood.getUsername();
        displayUsername = displayUsername.substring(0, 1).toUpperCase() + displayUsername.substring(1);

        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle(displayUsername+"'s Mood")
                .setNeutralButton("Close", null)
                .create();

        return builder;
    }

    public int getEmoji(Mood mood) {
        int moodImage;

        switch(mood.getMood()) {
            case "Happy":
                moodImage = R.drawable.happy_face;
                break;
            case "Angry":
                moodImage = R.drawable.angry_face;
                break;
            case "Disgusted":
                moodImage = R.drawable.disgust_face;
                break;
            case "Scared":
                moodImage = R.drawable.fear_face;
                break;
            case "Sad":
                moodImage = R.drawable.sad_face;
                break;
            case "Surprised":
                moodImage = R.drawable.surprised_face;
                break;
            default:
                moodImage = R.drawable.null_face;
        }

        return moodImage;
    }
}
