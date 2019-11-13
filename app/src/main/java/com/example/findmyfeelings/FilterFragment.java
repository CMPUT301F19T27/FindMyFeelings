


package com.example.findmyfeelings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


/**
 * This class filters out a selected Mood
 *
 */
public class FilterFragment extends DialogFragment {
    private static final String ARG_FILTER = "filter";

    private ImageView happy;
    private ImageView sad;
    private ImageView angry;
    private ImageView disgusted;
    private ImageView surprised;
    private ImageView scared;
    private Button removeFilters;
    private String filter = "";

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onFilterAdded(String filter);
    }

    /**
     * This interface allows the use of the filtering tab above HomepageActivity
     *
     */
    static FilterFragment newInstance(String filter){
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILTER, filter);

        FilterFragment fragment = new FilterFragment();
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

    /**
     * This functions returns a filter fragment which filter the moods based on the filter result
     * @param savedInstanceState
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_filter, null);

        happy = view.findViewById(R.id.happy_emoticon);
        sad = view.findViewById(R.id.sad_emoticon);
        angry = view.findViewById(R.id.angry_emoticon);
        disgusted = view.findViewById(R.id.disgusted_emoticon);
        surprised = view.findViewById(R.id.surprised_emoticon);
        scared = view.findViewById(R.id.scared_emoticon);
        removeFilters = view.findViewById(R.id.remove_filters_button);

        Bundle args = getArguments();

        if (args!=null){
            filter = (String) args.getSerializable(ARG_FILTER);

            switch(filter) {
                case "Happy":
                    happy.setImageResource(R.drawable.happy_face);
                    break;
                case "Angry":
                    angry.setImageResource(R.drawable.angry_face);
                    break;
                case "Disgusted":
                    disgusted.setImageResource(R.drawable.disgust_face);
                    break;
                case "Scared":
                    scared.setImageResource(R.drawable.fear_face);
                    break;
                case "Sad":
                    sad.setImageResource(R.drawable.sad_face);
                    break;
                case "Surprised":
                    surprised.setImageResource(R.drawable.surprised_face);
                    break;
            }
        }

        happy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                happy.setImageResource(R.drawable.happy_face);
                sad.setImageResource(R.drawable.sad_face_bw);
                angry.setImageResource(R.drawable.angry_face_bw);
                disgusted.setImageResource(R.drawable.disgust_face_bw);
                surprised.setImageResource(R.drawable.surprised_face_bw);
                scared.setImageResource(R.drawable.fear_face_bw);

                filter = "Happy";
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

                filter = "Sad";
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

                filter = "Angry";
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

                filter = "Disgusted";
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

                filter = "Surprised";
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

                filter = "Scared";
            }
        });

        removeFilters.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                happy.setImageResource(R.drawable.happy_face_bw);
                sad.setImageResource(R.drawable.sad_face_bw);
                angry.setImageResource(R.drawable.angry_face_bw);
                disgusted.setImageResource(R.drawable.disgust_face_bw);
                surprised.setImageResource(R.drawable.surprised_face_bw);
                scared.setImageResource(R.drawable.fear_face_bw);

                filter = "";
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Filter by mood")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onFilterAdded(filter);
                    }
                }).create();

    }






}

