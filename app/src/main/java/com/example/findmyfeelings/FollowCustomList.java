package com.example.findmyfeelings;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.List;

public class FollowCustomList extends RecyclerView.Adapter<FollowCustomList.FollowViewHolder> {
    private List<User> users;


    public static class FollowViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private FollowViewHolder(@NonNull View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FollowCustomList(List<User> inputMoodDataset) {
        users = inputMoodDataset;
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
        User user = users.get(position);

        TextView dateValue = holder.view.findViewById(R.id.first_name_text);
        TextView timeValue = holder.view.findViewById(R.id.last_name_text);
        TextView moodString = holder.view.findViewById(R.id.username_text);

        dateValue.setText(user.getFirstName());
        timeValue.setText(user.getLastName());
        moodString.setText(user.getUsername());
    }


    @Override
    public int getItemCount() {
        if(users != null) {
            return users.size();
        } else {
            return 0;
        }
    }

}
