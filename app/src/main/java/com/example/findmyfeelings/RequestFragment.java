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
import android.widget.Toast;

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

/**
 * this class displays the request fragment
 */
public class RequestFragment extends DialogFragment implements SearchCustomList.RecyclerViewListener {
    private static final String ARG_MOOD = "ride";
    private static final String ARG_INDEX = "index";

    private ArrayList<FollowUser> requestDataList;
    private RecyclerView requestList;
    private RecyclerView.Adapter requestAdapter;
    private RecyclerView.LayoutManager requestLayoutManager;

    private FirebaseFirestore db;



    private RequestFragment.OnFragmentInteractionListener listener;
    private FollowUser currentUser;
    private int index;

    private String currentUserEmail;
    private ArrayList<FollowUser> followingDataList;

    public RequestFragment(String currentUserEmail, ArrayList<FollowUser> requestDataList) {
        this.currentUserEmail = currentUserEmail;
        this.requestDataList = requestDataList;
    }


    public interface OnFragmentInteractionListener {
        void onRequestAccepted(FollowUser fUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RequestFragment.OnFragmentInteractionListener) {
            listener = (RequestFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_request, null);

        requestList = view.findViewById(R.id.user_request_list);

        /* ** Custom List Implementation ** */
        requestDataList = new ArrayList<>();

        // use a linear layout manager
        requestLayoutManager = new LinearLayoutManager(this.getContext());
        requestList.setLayoutManager(requestLayoutManager);

        // Specify an adapter
        requestAdapter = new SearchCustomList(requestDataList, this);
        requestList.setAdapter(requestAdapter);

        db = FirebaseFirestore.getInstance();
        CollectionReference cRef = db.collection("Users");

        cRef
                .document(currentUserEmail)
                .collection("Requests")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        requestDataList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String email = doc.getId();
                            String username = (String) doc.getData().get("username");
                            String firstName = (String) doc.getData().get("first_name");
                            String lastName = (String) doc.getData().get("last_name");

                            FollowUser fUser = new FollowUser(email, username, firstName, lastName);
                            requestDataList.add(fUser);
                        }
                        requestAdapter.notifyDataSetChanged();
                    }
                });

        final AlertDialog builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("Requests")
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FollowUser fUser = requestDataList.get(index);
                        Toast.makeText(getContext(), "Accept request from " + fUser.getUsername(), Toast.LENGTH_SHORT).show();
                        listener.onRequestAccepted(fUser);
                    }
                })
                .create();
        return builder;
    }

    @Override
    public void onRecyclerViewClickListener(int position) {
        Toast.makeText(getContext(), "Selected " + requestDataList.get(position).getUsername(), Toast.LENGTH_SHORT).show();
        index = position;
    }
}
