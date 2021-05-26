package com.example.androidappcar;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Calendar;

public class RegisterDeliveryActivity extends DeliveryActivity {

    private static final String TAG = "RegisterDeliveryActivity";

    EditText patient, parcel, location;
    TextView date, time;
    Button createDelivery;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

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

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterDeliveryActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String dateS = day + "/" + month + "/" + year;
                date.setText(dateS);
            }
        };

        time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hrs = cal.HOUR_OF_DAY;
                int min = cal.MINUTE;
                TimePickerDialog dialog = new TimePickerDialog(
                        RegisterDeliveryActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener,
                        min, hrs, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String minuteS;
                minuteS = String.valueOf(minute);
                if(minuteS.length() ==  1){
                    minuteS = "0"+minuteS;
                }
                String timeS = hourOfDay + ":" + minuteS;
                time.setText(timeS);
            }
        };

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navViewStaff);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

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
           Toast.makeText(RegisterDeliveryActivity.this, "You have created a delivery for " + patientS, Toast.LENGTH_SHORT).show();
           patient.setText("");
           parcel.setText("");
           location.setText("");
           date.setText("");
           time.setText("");
       }
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