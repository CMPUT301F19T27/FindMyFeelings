package com.example.findmyfeelings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.*;

import java.util.List;

public class MoodCustomList extends ArrayAdapter<Mood> {
    private List<Mood> moods;
    private Context context;

    public MoodCustomList(Context context, List<Mood> moods){
        super(context,0, moods);
        this.moods = moods;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.mood_list_content, parent,false);
        }

        Mood mood = moods.get(position);

        TextView dateValue = view.findViewById(R.id.date_text);
        TextView timeValue = view.findViewById(R.id.time_text);
        TextView moodString = view.findViewById(R.id.mood_name_text);
        TextView usernameString = view.findViewById(R.id.username_text);


        dateValue.setText(mood.getDateString());
        timeValue.setText(mood.getTimeString());
        moodString.setText(mood.getMood());

        return view;

    }
}
