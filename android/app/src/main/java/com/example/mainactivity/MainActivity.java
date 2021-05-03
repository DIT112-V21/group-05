package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    private Button connectCarButton;
    private Button controlCarButton;
    private Button settingsButton;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_mainMenu:
                        break;

                    case R.id.nav_connectCar:
                        Intent intent1 = new Intent(MainActivity.this, ConnectCarActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_controlCar:
                        Intent intent2 = new Intent(MainActivity.this, ControlCarActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_settings:
                        Intent intent3 = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent3);
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}