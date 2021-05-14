package com.example.androidappcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DeliveryActivity extends AppCompatActivity {

    ArrayList<Delivery> deliveryList = new ArrayList<Delivery>();

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_main);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navViewPatient);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navHomeMenuP:
                    Intent intent1 = new Intent(DeliveryActivity.this, PatientMainActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.navDeliveries:
                    break;

                case R.id.navLogOutP:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent3 = new Intent(DeliveryActivity.this, LoginActivity.class);
                    startActivity(intent3);
                    finish();
                    break;
            }

            return false;
        });

        drawer = findViewById(R.id.drawerLayoutPatient);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
