package com.example.androidappcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class StaffMainActivity extends AppCompatActivity {

    Button controlCarBtn, createDelBtn, logOutBtn;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);
        setTitle("Home");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navViewStaff);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navHomeMenuS:
                    break;

                case R.id.navControlCar:
                    Intent intent1 = new Intent(StaffMainActivity.this, ControlCarActivity.class);
                    startActivity(intent1);
                    break;

                case R.id.navCreateDel:
                    Intent intent2 = new Intent(StaffMainActivity.this, RegisterDeliveryActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.navLogOutS:
                   FirebaseAuth.getInstance().signOut();
                   Intent intent3 = new Intent(StaffMainActivity.this, LoginActivity.class);
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

        controlCarBtn = findViewById(R.id.controlCar);
        controlCarBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ControlCarActivity.class)));

        createDelBtn = findViewById(R.id.createDelivery);
        createDelBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterDeliveryActivity.class)));

        logOutBtn = findViewById(R.id.logOut);
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