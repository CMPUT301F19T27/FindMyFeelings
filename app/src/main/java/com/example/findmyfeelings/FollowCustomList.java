package com.example.findmyfeelings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// CUSTOM LSI FOR DISPLAYING FOLLOWERS AND FOLLOWING
public class FollowCustomList extends RecyclerView.Adapter<FollowCustomList.FollowViewHolder> {
    private List<FollowUser> fUsers;


    public static class FollowViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private FollowViewHolder(@NonNull View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FollowCustomList(List<FollowUser> inputMoodDataset) {
        fUsers = inputMoodDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    @NonNull
    public FollowCustomList.FollowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_list_content, parent, false);

        FollowViewHolder followViewHolder = new FollowViewHolder(view);
        return followViewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FollowViewHolder holder, int position) {
        FollowUser fUser = fUsers.get(position);

        TextView dateValue = holder.view.findViewById(R.id.first_name_text);
        TextView timeValue = holder.view.findViewById(R.id.last_name_text);
        TextView moodString = holder.view.findViewById(R.id.username_text);

        dateValue.setText(fUser.getFirstName());
        timeValue.setText(fUser.getLastName());
        moodString.setText(fUser.getUsername());

    }


    @Override
    public int getItemCount() {
        if(fUsers != null) {
            return fUsers.size();
        } else {
            return 0;
        }
    }

}
