package com.example.findmyfeelings;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FragmentSendRequest extends Fragment {

    ActionsendReq action;

    public interface ActionsendReq {
        void sendReq(String userName);
        void backFromRequest();
    }

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof ActionsendReq) {
                action = (ActionsendReq) activity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sendrequest, container, false);
        Button addUser = (Button) view.findViewById(R.id.button_add);
        final EditText user = (EditText) view.findViewById(R.id.username);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (action != null) {

                    action.sendReq(user.getText().toString());
                }
            }
        });

        Button back = (Button) view.findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (action != null) {
                    action.backFromRequest();
                }
            }
        });
        return view;
    }
}
