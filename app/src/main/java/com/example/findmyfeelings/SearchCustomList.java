package com.example.findmyfeelings;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * displays a custom list for searched users
 */
public class SearchCustomList extends RecyclerView.Adapter<SearchCustomList.SearchViewHolder> implements Filterable {


    private ArrayList<FollowUser> users;
    private ArrayList<FollowUser> filteredUsers;
    private RecyclerViewListener mRecyclerViewListener;
    private ValueFilter valueFilter;
    int index = -1;

    
    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchCustomList(ArrayList<FollowUser> inputUserDataset, RecyclerViewListener recyclerViewListener) {
        this.users = inputUserDataset;
        this.mRecyclerViewListener = recyclerViewListener;
        this.filteredUsers = inputUserDataset;
    }


    public static class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View view;
        RecyclerViewListener recyclerViewListener;
        TextView dateValue;
        TextView timeValue;
        TextView moodString;
        LinearLayout container;

        private SearchViewHolder(@NonNull View v, RecyclerViewListener recyclerViewListener) {
            super(v);
            this.view = v;
            this.recyclerViewListener = recyclerViewListener;

            dateValue = v.findViewById(R.id.first_name_text);
            timeValue = v.findViewById(R.id.last_name_text);
            moodString = v.findViewById(R.id.username_text);
            container = v.findViewById(R.id.search_list_content_background);
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



        holder.dateValue.setText(fUser.getFirstName());
        holder.timeValue.setText(fUser.getLastName());
        holder.moodString.setText(fUser.getUsername());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                notifyDataSetChanged();
            }
        });

        if(index==position){
            holder.container.setBackgroundColor(Color.parseColor("#FFDFDF"));
//            holder.country.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.container.setBackgroundColor(Color.parseColor("#FFFFFF"));
//            holder.country.setTextColor(Color.parseColor("#000000"));
        }

    }


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<FollowUser> filterList = new ArrayList<FollowUser>();
                for (int i = 0; i < users.size(); i++) {
                    if ((users.get(i).getFirstName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        FollowUser fUser = users.get(i);

                        FollowUser followUser = new FollowUser(fUser.getEmail(), fUser.getUsername(),
                                fUser.getFirstName(), fUser.getLastName());
                        filterList.add(followUser);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = users.size();
                results.values = users;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results.values != null){

                filteredUsers = (ArrayList<FollowUser>) results.values;

            }

            else {
                filteredUsers.clear();
            }
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return filteredUsers.size()>15?15:filteredUsers.size();
    }


    public interface RecyclerViewListener {
        void onRecyclerViewClickListener(int position);
    }





}
