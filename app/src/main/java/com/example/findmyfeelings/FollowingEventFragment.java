package com.example.findmyfeelings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FollowingEventFragment extends DialogFragment implements OnMapReadyCallback {
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
    private GoogleMap gMap;
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
        display_image = view.findViewById(R.id.display_image);
        titleViewText = view.findViewById(R.id.view_title_text);

        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.following_event_view_map);
        mapFragment.getMapAsync(this);
        //onMapReady(gMap);

        Bundle args = getArguments();

        if (args != null) {
            currentMood = (Mood) args.getSerializable(ARG_MOOD);
            index = args.getInt(ARG_INDEX);

            if (currentMood.getImageURL() != null) {
                Picasso.get().load(currentMood.getImageURL()).into(display_image);
            }

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

            // Set the default data display mode
            try {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.hide(mapFragment).commit();
            }
            catch (Exception e) {
                e.printStackTrace ();
            }
            display_image.setVisibility(View.VISIBLE);

            boolean hasPhoto = currentMood.getImageURL() != null;

            // Determine what data we have to display and how to display it
            if(currentMood.getLocation() == null && hasPhoto) {
                swapViewButton.setVisibility(View.GONE);
            }
            else if(currentMood.getLocation() != null && !hasPhoto) {
//                display_map.setVisibility(View.VISIBLE);
                try {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.show(mapFragment).commit();
                }
                catch (Exception e) {
                    e.printStackTrace ();
                }

                display_image.setVisibility(View.GONE);
                titleViewText.setText("Location: ");
                swapViewButton.setVisibility(View.GONE);
                showingImage = false;
            }
            else if(currentMood.getLocation() == null && !hasPhoto) {
//                display_map.setVisibility(View.GONE);
                try {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.hide(mapFragment).commit();
                }
                catch (Exception e) {
                    e.printStackTrace ();
                }

                display_image.setVisibility(View.GONE);
                swapViewButton.setVisibility(View.GONE);
                titleViewText.setVisibility(View.GONE);
            }

        }


        swapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Swap to map because image is showing
                if(showingImage) {
//                    display_map.setVisibility(View.VISIBLE);
                    try {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.show(mapFragment).commit();
                    }
                    catch (Exception e) {
                        e.printStackTrace ();
                    }

                    display_image.setVisibility(View.GONE);
                    titleViewText.setText("Location: ");
                    swapViewButton.setText("View Photo");
                    showingImage = false;

                    onMapReady(gMap);
                }

                // Swap to image because map is showing
                else {
//                    display_map.setVisibility(View.GONE);
                    try {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.hide(mapFragment).commit();
                    }
                    catch (Exception e) {
                        e.printStackTrace ();
                    }

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




    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if(gMap == null) {
            return;
        }

        // disable map toolbar on the bottom right corner
        gMap.getUiSettings().setMapToolbarEnabled(false);


        GeoPoint location = currentMood.getLocation();
        if (location != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 100));

            Bitmap bitmap;
            switch (currentMood.getMood()) {
                case "Happy":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.happy_face);
                    break;
                case "Angry":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.angry_face);
                    break;
                case "Disgusted":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.disgust_face);
                    break;
                case "Scared":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fear_face);
                    break;
                case "Sad":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sad_face);
                    break;
                case "Surprised":
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.surprised_face);
                    break;
                default:
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.null_face);
            }

            //BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.angry_face);
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sad_face);

            Bitmap bitmapRescale = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            BitmapDescriptor aw = BitmapDescriptorFactory.fromBitmap(bitmapRescale);
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng((location.getLatitude()), (location.getLongitude())))
                    .title(currentMood.getUsername())
                    .icon(aw)
                    .flat(false);

            //Set the default zoom
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f));

            gMap.addMarker(marker);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.following_event_view_map);
        if (mapFragment != null)
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }
}
