package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

public class ConnectCarActivity extends AppCompatActivity {
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setTitle("Connect Car");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_car);
    }
}