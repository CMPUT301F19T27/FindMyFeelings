package com.example.findmyfeelings;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * displays a custom list for searched users
 */
public class SearchCustomList extends RecyclerView.Adapter<SearchCustomList.SearchViewHolder> implements Filterable {


    private List<FollowUser> users;
    private List<FollowUser> filteredUsers;
    private RecyclerViewListener mRecyclerViewListener;
    
    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchCustomList(ArrayList<FollowUser> inputUserDataset, RecyclerViewListener recyclerViewListener) {
        this.users = inputUserDataset;
        this.mRecyclerViewListener = recyclerViewListener;
        this.filteredUsers = inputUserDataset;
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
        FollowUser fUser = filteredUsers.get(position);

        TextView dateValue = holder.view.findViewById(R.id.first_name_text);
        TextView timeValue = holder.view.findViewById(R.id.last_name_text);
        TextView moodString = holder.view.findViewById(R.id.username_text);

        dateValue.setText(fUser.getFirstName());
        timeValue.setText(fUser.getLastName());
        moodString.setText(fUser.getUsername());

    }

    public void X() {

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String Key = charSequence.toString();
                if (Key.isEmpty()) {
                    filteredUsers = users;
                }
                else {
                    List<FollowUser> lstFiltered = new ArrayList<>();

                    for(FollowUser user : users) {
                        if (user.getFirstName().toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(user);
                        }
                    }
                    filteredUsers = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterResults;
                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                filteredUsers = (List<FollowUser>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {

        return filteredUsers.size();
//        if(users != null) {
//            return users.size();
//        } else {
//            return 0;
//        }
    }
    public interface RecyclerViewListener {
        void onRecyclerViewClickListener(int position);
    }





}
