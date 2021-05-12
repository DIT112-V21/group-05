package com.example.androidappcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DeliveryActivity extends AppCompatActivity {
    ArrayList<Delivery> deliveryList = new ArrayList<Delivery>();
    Button addDelivery;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_main);
        addDelivery = findViewById(R.id.addDelivery);
        addDelivery.setOnClickListener(v -> addDeliveryPage());

        ListView mListView = findViewById(R.id.listView);
        loadData();

        DeliveryListAdapter adapter = new DeliveryListAdapter(this, R.layout.delivery_card, deliveryList);
        mListView.setAdapter(adapter);
    }


    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("delivery list", null);
        Type type = new TypeToken<ArrayList<Delivery>>() {}.getType();
        deliveryList = gson.fromJson(json,type);
        if(deliveryList == null){
            deliveryList = new ArrayList<>();
        }
    }
    public void addDeliveryPage() {
        Intent intent = new Intent(this, RegisterDeliveryActivity.class);
        startActivity(intent);
    }
}
