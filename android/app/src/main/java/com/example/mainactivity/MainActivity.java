package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button connectCarButton;
    private Button controlCarButton;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setTitle("Main Menu");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectCarButton = findViewById(R.id.connectCarButton);
        connectCarButton.setOnClickListener(v -> openConnectCarMenu());

        controlCarButton = findViewById(R.id.controlCarButton);
        controlCarButton.setOnClickListener(v -> openControlCarMenu());

        settingsButton = findViewById(R.id.SettingsButton);
        settingsButton.setOnClickListener(v -> openSettingsMenu());
    }

    public void openConnectCarMenu() {
        Intent intent = new Intent(this, ConnectCarActivity.class);
        startActivity(intent);
    }

    public void openControlCarMenu() {
        Intent intent = new Intent(this, ControlCarActivity.class);
        startActivity(intent);
    }

    public void openSettingsMenu() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}