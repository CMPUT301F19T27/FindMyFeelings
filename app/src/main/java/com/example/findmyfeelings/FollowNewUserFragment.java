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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FollowNewUserFragment extends DialogFragment implements SearchCustomList.RecyclerViewListener {

    private EditText searchEditText;
    private Button searchButton;
    private ArrayList<FollowUser> searchResultsList;
    private RecyclerView searchList;
    private RecyclerView.Adapter searchAdapter;
    private RecyclerView.LayoutManager searchLayoutManager;

    private FirebaseFirestore db;

    private OnFragmentInteractionListener listener;
    private int index;

    private String currentUserEmail;
    private ArrayList<FollowUser> followingDataList;

    public FollowNewUserFragment(String currentUserEmail, ArrayList<FollowUser> followingDataList) {
        this.currentUserEmail = currentUserEmail;
        this.followingDataList = followingDataList;
    }


    public interface OnFragmentInteractionListener {
        void onUserFollowed(FollowUser fUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_follow_new_user, null);
        searchEditText = view.findViewById(R.id.search_editText);
        searchButton = view.findViewById(R.id.search_button);
        searchList = view.findViewById(R.id.user_search_list);


        db = FirebaseFirestore.getInstance();
        CollectionReference cRef = db.collection("Users");

        /* ** Custom List Implementation ** */
        searchList = view.findViewById(R.id.user_search_list);
        searchResultsList = new ArrayList<>();

        // use a linear layout manager
        searchLayoutManager = new LinearLayoutManager(this.getContext());
        searchList.setLayoutManager(searchLayoutManager);

        // Specify an adapter
        searchAdapter = new SearchCustomList(searchResultsList, this);
        searchList.setAdapter(searchAdapter);

        cRef
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        searchResultsList.clear();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String email = doc.getId();
                            String username = (String) doc.getData().get("username");
                            String firstName = (String) doc.getData().get("first_name");
                            String lastName = (String) doc.getData().get("last_name");

                            FollowUser fUser = new FollowUser(email, username, firstName, lastName);
                            searchResultsList.add(fUser);
                        /*if (!(followingDataList.contains(fUser))) {
                                searchResultsList.add(fUser);
                            }*/
                        }
                        searchAdapter.notifyDataSetChanged();
                    }
                });

        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Follow")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Follow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println();
                        FollowUser fUser = searchResultsList.get(index);
                        listener.onUserFollowed(fUser);
                    }
                })
                .create();
        return builder;
    }

    @Override
    public void onRecyclerViewClickListener(int position) {
        index = position;
    }

}