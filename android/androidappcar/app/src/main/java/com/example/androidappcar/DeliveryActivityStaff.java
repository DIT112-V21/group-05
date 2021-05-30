package com.example.androidappcar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DeliveryActivityStaff extends AppCompatActivity {

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
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_tab);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.view_pager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Delivery List"));



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==1)
                    loadData();
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



        mListView = findViewById(R.id.listView);

        navigationView = findViewById(R.id.navViewStaff);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navControlCar:
                    Intent intent2 = new Intent(DeliveryActivityStaff.this, ControlCarActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.navCreateDel:
                    Intent intent5 = new Intent(DeliveryActivityStaff.this, RegisterDeliveryActivity.class);
                    startActivity(intent5);
                    break;

                case R.id.navDelList:
                    break;

                case R.id.navLogOutS:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent3 = new Intent(DeliveryActivityStaff.this, LoginActivity.class);
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
        String uid = fAuth.getCurrentUser().getUid();
        loadData();
        checkUserName(uid);
    }



    void saveData(ArrayList<Delivery> saveList, String listTitle){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(saveList);
        editor.putString(listTitle, json);
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
        json = sharedPreferences.getString("confirmed list", null);
        type = new TypeToken<ArrayList<Delivery>>() {
        }.getType();
        confirmed = gson.fromJson(json, type);
        if (confirmed == null) {
            confirmed = new ArrayList<>();
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
                confirmBtn = findViewById(R.id.confirmBtn);
                Toast.makeText(DeliveryActivityStaff.this, "Welcome back " + documentSnapshot.getString("FullName"), Toast.LENGTH_SHORT).show();
                personalDeliveries(documentSnapshot.getString("FullName"));
                DeliveryListAdapter adapter = new DeliveryListAdapter(this, R.layout.delivery_card, userDeliveryList);
                mListView.setAdapter(adapter);


                confirm();


            } else {
                DeliveryListAdapter adapter = new DeliveryListAdapter(this, R.layout.delivery_card, deliveryList);
                mListView.setAdapter(adapter);
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
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Confirmed", Toast.LENGTH_LONG).show();
                            delivered();

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
            saveData(deliveryList, "delivery list");
            saveData(confirmed, "confirmed list");
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


