package com.example.findmyfeelings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MoodCustomList extends RecyclerView.Adapter<MoodCustomList.MoodViewHolder> {
    private List<Mood> moods;


    public static class MoodViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private MoodViewHolder(@NonNull View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoodCustomList(List<Mood> inputMoodDataset) {
        moods = inputMoodDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public MoodCustomList.MoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_list_content, parent, false);

        MoodViewHolder moodViewHolder = new MoodViewHolder(view);
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

        switch(mood.getMood()) {
            case "Happy":
                moodImage.setImageResource(R.drawable.happy_face);
                break;
            case "Angry":
                moodImage.setImageResource(R.drawable.angry_face);
                break;
            case "Disgusted":
                moodImage.setImageResource(R.drawable.disgust_face);
                break;
            case "Scared":
                moodImage.setImageResource(R.drawable.fear_face);
                break;
            case "Sad":
                moodImage.setImageResource(R.drawable.sad_face);
                break;
            case "Surprised":
                moodImage.setImageResource(R.drawable.surprised_face);
                break;
            default:
                moodImage.setImageResource(R.drawable.null_face);
        }

        dateValue.setText(mood.getDateString());
        timeValue.setText(mood.getTimeString());
        moodString.setText(mood.getMood());

        String username = "LongGenericUsernameThatWillBeTruncated"; // TODO change to user.getName()

        if(username.length() > 10) {
            usernameString.setText(username.substring(0,10) + "...");
        } else {
            usernameString.setText(username);
        }

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
