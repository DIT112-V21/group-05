package com.example.androidappcar;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

public class Connectivity extends Activity {

    Button setButton, closeButton;
    TextInputLayout mqttInputLayout, topicInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectivity_window);

        mqttInputLayout = findViewById(R.id.mqttInputLayout);
        topicInputLayout = findViewById(R.id.topicInputLayout);

        setButton = findViewById(R.id.setButton);
        setButton.setOnClickListener(e -> {
            ControlCarActivity.setExternalMqttBroker(mqttInputLayout.getEditText().getText().toString());
            ControlCarActivity.setMainTopic(topicInputLayout.getEditText().getText().toString());
        });

        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(e -> finish());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));
    }
}
