package com.example.findmyfeelings;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentRecvRequest  extends Fragment {

    FragmentRecvRequest.ActionrecvReq action;
    String userName;

    public void setReqUserName(String userName) {
        this.userName = userName;
    }

    public interface ActionrecvReq {
        void acceptReq(String userName);
        void rejectReq(String userName);
        void backFromRecvPage();
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof FragmentRecvRequest.ActionrecvReq) {
                action = (FragmentRecvRequest.ActionrecvReq) activity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_recvrequest, container, false);
        Button addUser = (Button) view.findViewById(R.id.button_add);
        Button delUser = (Button) view.findViewById(R.id.button_delete);
        final TextView user = (TextView) view.findViewById(R.id.username);
        user.setText(userName);

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (action != null) {

                    action.acceptReq(userName);
                }
            }
        });
        delUser.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (action != null) {

                    action.rejectReq(userName);
                }
            }
        });

        Button back = (Button) view.findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (action != null) {
                    action.backFromRecvPage();
                }
            }
        });
        return view;
    }
}
