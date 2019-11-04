


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

public class FilterFragment extends DialogFragment {

    private ImageView happy;
    private ImageView sad;
    private ImageView angry;
    private ImageView disgusted;
    private ImageView surprised;
    private ImageView scared;
    private Button removeFilters;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener{
        void onFilterAdded(String filter);
    }

    static FilterFragment newInstance(){
        Bundle args = new Bundle();

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
            removeFilters.setVisibility(view.VISIBLE);
        } else {
            removeFilters.setVisibility(view.INVISIBLE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Filter by mood.")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String filter = "Happy";

                        listener.onFilterAdded(filter);
                    }
                }).create();


    }






}

