package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    private Button languageButton;
    private Button buttonLayoutButton;
    private Button supportUsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setTitle("Settings");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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