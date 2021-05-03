package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ControlCarActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private static final String TAG = "SafePill";
    private static final String MQTT_BROKER = "aerostun.dev";
    private static final String LOCAL_HOST = "";
    private static final String MQTT_SERVER = "tcp://" + MQTT_BROKER + ":1883";
    private static final String THROTTLE_CONTROL = "/smartcar/control/throttle";
    private static final String STEERING_CONTROL = "/smartcar/control/steering";
    private static final int MOVEMENT_SPEED = 70;
    private static final int STRAIGHT_ANGLE = 0;
    private static final int STEERING_ANGLE = 50;
    private static final int QOS = 1;

    final String successfulConnection = "Connected to MQTT broker.";
    final String failedConnection = "Failed to connect to MQTT broker.";
    final String notConnected = "Not connected yet";

    private MqttClient mMqttClient;
    private boolean isConnected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setTitle("Control Car");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_car);

        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_mainMenu:
                        Intent intent1 = new Intent(ControlCarActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_connectCar:
                        Intent intent2 = new Intent(ControlCarActivity.this, ConnectCarActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_controlCar:
                        break;

                    case R.id.nav_settings:
                        Intent intent3 = new Intent(ControlCarActivity.this, SettingsActivity.class);
                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });

        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        connectToMqttBroker();
    }

    @Override
    protected void onResume() {
        super.onResume();

        connectToMqttBroker();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMqttClient.disconnect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i(TAG, "Disconnected from MQTT broker");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            }
        });

        connectToMqttBroker();
    }

    private void connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient.connect(TAG, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;

                    Log.i(TAG, successfulConnection);
                    Toast.makeText(getApplicationContext(), successfulConnection, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    Log.e(TAG, failedConnection);
                    Toast.makeText(getApplicationContext(), failedConnection, Toast.LENGTH_SHORT).show();
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;

                    final String connectionLost = "Connection to MQTT broker lost";
                    Log.w(TAG, connectionLost);
                    Toast.makeText(getApplicationContext(), connectionLost, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }

            });
        }
    }

    void drive(int throttleSpeed, int steeringAngle, String actionDescription) {
        if (!isConnected) {

            Log.e(TAG, notConnected);
            Toast.makeText(getApplicationContext(), notConnected, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG, actionDescription);
        mMqttClient.publish(THROTTLE_CONTROL, Integer.toString(throttleSpeed), QOS, null);
        mMqttClient.publish(STEERING_CONTROL, Integer.toString(steeringAngle), QOS, null);
    }

    public void moveForward(View view) {
        drive(MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving forward");
    }

    public void moveForwardLeft(View view) {
        drive(MOVEMENT_SPEED, -STEERING_ANGLE, "Moving forward left");
    }

    public void moveForwardRight(View view) {
        drive(MOVEMENT_SPEED, STEERING_ANGLE, "Moving forward left");
    }

    public void moveBackward(View view) {
        drive(-MOVEMENT_SPEED, STRAIGHT_ANGLE, "Moving backward");
    }
}