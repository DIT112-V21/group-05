package com.example.androidappcar;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


import com.google.gson.Gson;

public class RegisterDeliveryActivity extends DeliveryActivity {

    EditText patient, parcel, location, date, time;
    Button createDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_delivery);
        patient = findViewById(R.id.patientName);
        parcel = findViewById(R.id.parcel);
        location = findViewById(R.id.location);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        createDelivery = findViewById(R.id.createDeliveryBtn);
        createDelivery.setOnClickListener(v -> createDelivery());
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
}