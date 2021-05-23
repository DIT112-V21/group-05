package com.example.androidappcar;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button controlCar, register, delivery, speedLimit;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlCar = findViewById(R.id.controlCar);
        controlCar.setOnClickListener(v -> openControlCarPage());

        register = findViewById(R.id.registerPage);
        register.setOnClickListener(v -> openRegisterPage());

        delivery = findViewById(R.id.deliveries);
        delivery.setOnClickListener(v -> openDeliveryPage());

    }

    public void openControlCarPage()  {
        Intent intent = new Intent(this, ControlCarActivity.class);
        startActivity(intent);
    }

    public void openRegisterPage()  {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    public void openDeliveryPage(){
        Intent intent = new Intent(this, DeliveryActivity.class);
        startActivity(intent);
    }
}