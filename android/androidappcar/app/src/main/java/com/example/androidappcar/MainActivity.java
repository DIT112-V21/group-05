package com.example.androidappcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button controlCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlCar = findViewById(R.id.controlCar);
        controlCar.setOnClickListener(v -> openControlCarPage());
    }

    public void openControlCarPage()  {
        Intent intent = new Intent(this, ControlCarActivity.class);
        startActivity(intent);
    }
}