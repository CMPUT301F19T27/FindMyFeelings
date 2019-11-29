package com.example.findmyfeelings;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


/**
 * this class helps user to sign up to the app
 */
public class    SignUpActivity extends AppCompatActivity {

    private String TAG = "Display";
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, usernameEditText;
    private TextView loginTextView;
    private Button signUpButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginTextView = findViewById(R.id.login_textView);
        firstNameEditText = findViewById(R.id.sign_up_firstname_editText);
        lastNameEditText  = findViewById(R.id.sign_up_lastname_editText);
        usernameEditText = findViewById(R.id.username_editText);
        emailEditText = findViewById(R.id.sign_up_email_editText);
        passwordEditText = findViewById(R.id.sign_up_password_editText);
        signUpButton = findViewById(R.id.sign_up_button);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionreference = db.collection("Users");

        firebaseAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String firstName = firstNameEditText.getText().toString();
                final String lastName = lastNameEditText.getText().toString();
                final String username = usernameEditText.getText().toString();

                if (email.isEmpty()) {
                    emailEditText.setError("Enter your email!");
                    emailEditText.requestFocus();
                }
                if (password.isEmpty()) {
                    passwordEditText.setError("Enter a password!");
                    passwordEditText.requestFocus();
                }

                if (firstName.isEmpty()) {
                    firstNameEditText.setError("Enter your first name!");
                    firstNameEditText.requestFocus();
                }
                if (lastName.isEmpty()) {
                    lastNameEditText.setError("Enter your last name!");
                    lastNameEditText.requestFocus();
                }
                if (username.isEmpty()) {
                    usernameEditText.setError("Enter a username!");
                    usernameEditText.requestFocus();
                }
                if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Sign Up failed. Please try again", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                User newUser = new User(email, username, firstName, lastName);

                                HashMap<String, Object> newUserData;

                                newUserData = newUser.userToMap();

                                collectionreference
                                        .document(email)
                                        .set(newUserData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG,"Data addition to firestore successful");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Data addition to firestore failed");
                                            }
                                        });
                                //FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
