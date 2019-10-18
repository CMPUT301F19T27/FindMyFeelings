package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.math.RoundingMode;

public class HomePageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // from stackoverflow : https://stackoverflow.com/questions/41649494/how-to-remove-icon-animation-for-bottom-navigation-view-in-android
        // disable default navigation bar animation
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // sets default selected item on opening
        bottomNavigationView.setSelectedItemId(R.id.ic_feed);
        // disables icon tint
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_map:
                        Intent intent1 = new Intent(HomePageActivity.this, MapActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_profile:
                        Intent intent2 = new Intent(HomePageActivity.this, ProfileActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });


//        bottom_nav_bar.setItemIconTintList(null);
    }
}

