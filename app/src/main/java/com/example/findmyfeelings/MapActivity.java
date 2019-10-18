package com.example.findmyfeelings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // disable default navigation bar animation
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // disables icon tint
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_profile:
                        Intent intent1 = new Intent(MapActivity.this, ProfileActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_feed:
                        Intent intent2 = new Intent(MapActivity.this, HomePageActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
    }
}
