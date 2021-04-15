package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

public class ControlCarActivity extends AppCompatActivity {

    private Button forwardButton;
    private Button backwardButton;
    private Button leftButton;
    private Button rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setTitle("Control Car");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_car);
    }
}