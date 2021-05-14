package com.example.androidappcar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PatientMainActivity extends AppCompatActivity {

    Button ongoingDelBtn, delConfirmBtn, logOutBtn;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navViewPatient);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navHomeMenuS:
                    break;

                case R.id.navDeliveries:
                    Intent intent1 = new Intent(PatientMainActivity.this, DeliveryActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.navLogOutP:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent3 = new Intent(PatientMainActivity.this, LoginActivity.class);
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

        ongoingDelBtn = findViewById(R.id.ongoingDel);
        ongoingDelBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DeliveryActivity.class))); //we might fix the activity and change it here

        logOutBtn = findViewById(R.id.logOut1);
        logOutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
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