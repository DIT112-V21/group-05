package com.example.androidappcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
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
    ArrayList<Delivery> confirmed = new ArrayList<Delivery>();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();



    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private ListView mListView;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_main);
        String uid = fAuth.getCurrentUser().getUid();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListView = findViewById(R.id.listView);
        confirmBtn = findViewById(R.id.confirmBtn);

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

    void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(deliveryList);
        editor.putString("delivery list", json);
        editor.apply();
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

        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess" + documentSnapshot.getData());

            if (documentSnapshot.getString("isPatient") != null) {
                Toast.makeText(DeliveryActivity.this, "Welcome back " + documentSnapshot.getString("FullName"), Toast.LENGTH_SHORT).show();
                personalDeliveries(documentSnapshot.getString("FullName"));
                DeliveryListAdapter adapter = new DeliveryListAdapter(this, R.layout.delivery_card, userDeliveryList);
                mListView.setAdapter(adapter);
                //startActivity(new Intent(getApplicationContext(), StaffMainActivity.class));
                //finish();

                confirm();


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


    public void confirm() {
        confirmBtn.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(confirmBtn.getContext());
            builder.setMessage("Confirm delivery?")
                    //.setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Confirmed", Toast.LENGTH_LONG).show();
                            delivered();

                            mListView.setAdapter(null);
                            mListView.setEmptyView(v);  //to remove confirmbtn

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }

    public void delivered() {
        for ( Delivery parcel : copyDeliveryList(deliveryList)) {
            confirmed.add(parcel);
            deliveryList.remove(parcel);
            saveData();
        }
    }

    public ArrayList<Delivery> copyDeliveryList(ArrayList<Delivery> objects){
        ArrayList<Delivery> userDeliveryListTem = new ArrayList<Delivery>();
        for(Delivery item : objects){
            userDeliveryListTem.add(item);
        }
        return userDeliveryListTem;
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
