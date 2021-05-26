package com.example.androidappcar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DeliveryActivity extends AppCompatActivity {

    ArrayList<Delivery> deliveryList = new ArrayList<Delivery>();
    ArrayList<Delivery> userDeliveryList = new ArrayList<Delivery>();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();



    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_main);
        String uid = fAuth.getCurrentUser().getUid();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navViewPatient);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

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

        loadData();
        checkUserName(uid);
    }


    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("delivery list", null);
        Type type = new TypeToken<ArrayList<Delivery>>() {
        }.getType();
        deliveryList = gson.fromJson(json, type);
        if (deliveryList == null) {
            deliveryList = new ArrayList<>();
        }
    }


    public void addDeliveryPage() {
        Intent intent = new Intent(this, RegisterDeliveryActivity.class);
        startActivity(intent);
    }
    private void checkUserName(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        ListView mListView = findViewById(R.id.listView);
        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess" + documentSnapshot.getData());

            if (documentSnapshot.getString("isPatient") != null) {
                Toast.makeText(DeliveryActivity.this, "Welcome back " + documentSnapshot.getString("FullName"), Toast.LENGTH_SHORT).show();
                personalDeliveries(documentSnapshot.getString("FullName"));
                DeliveryListAdapter adapter = new DeliveryListAdapter(this, R.layout.delivery_card, userDeliveryList);
                mListView.setAdapter(adapter);
                //startActivity(new Intent(getApplicationContext(), StaffMainActivity.class));
                //finish();
            } else {
                //Toast.makeText(DeliveryActivity.this, "Something Went Wrong " + documentSnapshot.getString("FullName"), Toast.LENGTH_SHORT).show();
                DeliveryListAdapter adapter = new DeliveryListAdapter(this, R.layout.delivery_card, deliveryList);
                //startActivity(new Intent(getApplicationContext(), PatientMainActivity.class));
                //finish();
            }
        });
    }
    
    private void personalDeliveries(String patientName) {
        for (Delivery delivery : deliveryList) {
           if (delivery.getPatient().equals(patientName)) {
                userDeliveryList.add(delivery);
            } 
        }
    }
}
