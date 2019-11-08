package com.example.findmyfeelings;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * displays a custom list for searched users
 */
public class SearchCustomList extends RecyclerView.Adapter<SearchCustomList.SearchViewHolder> {

    private List<FollowUser> users;
    private RecyclerViewListener mRecyclerViewListener;
    
    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchCustomList(List<FollowUser> inputUserDataset, RecyclerViewListener recyclerViewListener) {
        this.users = inputUserDataset;
        this.mRecyclerViewListener = recyclerViewListener;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View view;
        RecyclerViewListener recyclerViewListener;
        private SearchViewHolder(@NonNull View v, RecyclerViewListener recyclerViewListener) {
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
    public SearchCustomList.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_content, parent, false);

        SearchViewHolder searchViewHolder = new SearchViewHolder(view, mRecyclerViewListener);
        return searchViewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        FollowUser fUser = users.get(position);

        TextView dateValue = holder.view.findViewById(R.id.first_name_text);
        TextView timeValue = holder.view.findViewById(R.id.last_name_text);
        TextView moodString = holder.view.findViewById(R.id.username_text);

        dateValue.setText(fUser.getFirstName());
        timeValue.setText(fUser.getLastName());
        moodString.setText(fUser.getUsername());

    }

    public interface RecyclerViewListener {
        void onRecyclerViewClickListener(int position);
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
