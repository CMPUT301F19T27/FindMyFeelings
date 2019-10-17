package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    TextView signUpTextView;
    EditText emailEditText, passwordEditText;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.sign_up_textView);
        emailEditText = findViewById(R.id.username_editText);
        passwordEditText = findViewById(R.id.password_editText);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(MainActivity.this, HomePageActivity.class);
                    startActivity(loginIntent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please Login", Toast.LENGTH_SHORT).show();

                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty()) {
                    emailEditText.setError("Enter an email id");
                    emailEditText.requestFocus();
                }
                else if (password.isEmpty()) {
                    passwordEditText.setError("Enter a password");
                    passwordEditText.requestFocus();
                }
                else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty fields!", Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && password.isEmpty())) {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Sign Up failed. Please try again", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }

            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();


        //firebaseAuth.addAuthStateListener(authStateListener);
    }


}