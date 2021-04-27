package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends AppCompatActivity {

    private Button languageButton;
    private Button buttonLayoutButton;
    private Button supportUsButton;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_mainMenu:
                        Intent intent1 = new Intent(SettingsActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_connectCar:
                        Intent intent2 = new Intent(SettingsActivity.this, ConnectCarActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_controlCar:
                        Intent intent3 = new Intent(SettingsActivity.this, ControlCarActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_settings:
                        break;
                }

                return false;
            }
        });

        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        languageButton = findViewById(R.id.language);
        languageButton.setOnClickListener(v -> openLanguageMenu());

        buttonLayoutButton = findViewById(R.id.buttonLayout);
        buttonLayoutButton.setOnClickListener(v -> openButtonLayoutMenu());

        supportUsButton = findViewById(R.id.supportUs);
        supportUsButton.setOnClickListener(v -> openSupportUsMenu());

    }

    public void openLanguageMenu() {
        Intent intent = new Intent(this, SettingsLanguageActivity.class);
        startActivity(intent);
    }

    public void openButtonLayoutMenu() {
        Intent intent = new Intent(this, SettingsButtonActivity.class);
        startActivity(intent);
    }

    public void openSupportUsMenu() {
        Intent intent = new Intent(this, SettingsSupportActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}