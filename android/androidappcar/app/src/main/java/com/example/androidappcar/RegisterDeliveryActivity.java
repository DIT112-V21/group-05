package com.example.androidappcar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class RegisterDeliveryActivity extends DeliveryActivity {

    EditText patient, parcel, location, date, time;
    Button createDelivery;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_delivery);
        setTitle("Add Delivery");

        patient = findViewById(R.id.patientName);
        parcel = findViewById(R.id.parcel);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        createDelivery = findViewById(R.id.createDeliveryBtn);

        createDelivery.setOnClickListener(v -> createDelivery());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navViewStaff);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navHomeMenuS:
                    Intent intent1 = new Intent(RegisterDeliveryActivity.this, StaffMainActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.navControlCar:
                    Intent intent2 = new Intent(RegisterDeliveryActivity.this, ControlCarActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.navCreateDel:
                    break;

                case R.id.navLogOutS:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent3 = new Intent(RegisterDeliveryActivity.this, LoginActivity.class);
                    startActivity(intent3);
                    finish();
                    break;
            }

            return false;
        });

        drawer = findViewById(R.id.drawerLayoutStaff);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void createDelivery() {
        String patientS = patient.getText().toString();
        String parcelS = parcel.getText().toString();
        String locationS = location.getText().toString();
        String dateS = date.getText().toString();
        String timeS = time.getText().toString();

       if (patientS != null && parcelS != null && locationS != null && dateS != null && timeS != null) {
            Delivery del = new Delivery(patientS, parcelS, locationS, dateS, timeS);
            deliveryList.add(del);
            saveData();
       }
    }

    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(deliveryList);
        editor.putString("delivery list", json);
        editor.apply();
    }

    private void clear(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
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