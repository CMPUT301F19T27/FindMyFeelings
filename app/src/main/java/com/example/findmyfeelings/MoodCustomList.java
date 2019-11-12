package com.example.findmyfeelings;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoodCustomList extends RecyclerView.Adapter<MoodCustomList.MoodViewHolder> {

    private ArrayList<Mood> moods;
    private RecyclerViewListener mRecyclerViewListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoodCustomList(ArrayList<Mood> inputMoodDataset, RecyclerViewListener recyclerViewListener) {
        this.moods = inputMoodDataset;
        this.mRecyclerViewListener = recyclerViewListener;
    }

    public static class MoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View view;
        RecyclerViewListener recyclerViewListener;
        private MoodViewHolder(@NonNull View v, RecyclerViewListener recyclerViewListener) {
            super(v);
            this.view = v;
            this.recyclerViewListener = recyclerViewListener;

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerViewListener.onRecyclerViewClickListener(getAdapterPosition());
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public MoodCustomList.MoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_list_content, parent, false);

        MoodViewHolder moodViewHolder = new MoodViewHolder(view, mRecyclerViewListener);
        return moodViewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MoodViewHolder holder, int position) {
        Mood mood = moods.get(position);

        ImageView moodImage = holder.view.findViewById(R.id.mood_emoticon);
        TextView dateValue = holder.view.findViewById(R.id.date_text);
        TextView timeValue = holder.view.findViewById(R.id.time_text);
        TextView moodString = holder.view.findViewById(R.id.mood_name_text);
        TextView usernameString = holder.view.findViewById(R.id.username_text);

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(mood.getDateTime());
        Log.d("Sample", date);
        @SuppressLint("SimpleDateFormat")
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String time = timeFormat.format(mood.getDateTime());

        dateValue.setText(date);
        timeValue.setText(time);
        moodString.setText(mood.getMood());
        moodImage.setImageResource(getEmoji(mood));



        String username = mood.getUsername(); // TODO change to user.getName()

        if(username.length() > 16) {
            usernameString.setText(username.substring(0,16) + "...");
        } else {
            usernameString.setText(username);
        }
        System.out.println(usernameString.getText().toString()+"################################################3");
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
    public interface RecyclerViewListener {
        void onRecyclerViewClickListener(int position);
    }

    @Override
    public int getItemCount() {
        if(moods != null) {
            return moods.size();
        } else {
            return 0;
        }
    }

}
