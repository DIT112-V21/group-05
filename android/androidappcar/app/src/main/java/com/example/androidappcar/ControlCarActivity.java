package com.example.androidappcar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ControlCarActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    private static final String TAG = "Group05 Smartcar";
    private static String EXTERNAL_MQTT_BROKER = "aerostun.dev";
    private static final String LOCALHOST = "10.0.2.2";
    private static String MQTT_SERVER = "tcp://" + EXTERNAL_MQTT_BROKER + ":1883";
    private static final String THROTTLE_CONTROL = "/group05/control/handleInput";
    private static String MAIN_TOPIC = "/group/05";

    private Integer forward = 0;
    private Integer reverse = 0;
    private Integer right = 0;
    private Integer left = 0;
    private static int speedLimitForward = 70;
    private static int speedLimitBackwards = 30;
    private static final int TURN_LIMIT = 100;
    private static final String FORWARD = "2";
    private static final String REVERSE = "3";
    private static final String RIGHT = "4";
    private static final String LEFT = "5";
    private static final String STOP_TURN = "6";
    private static final String STOP_THROTTLE = "7";
    private static final String POWER_ON = "1";
    private static final String POWER_OFF = "0";
    private static final int QOS = 1;
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;
    private boolean powerOn = true;

    private MqttClient mMqttClient;
    private boolean isConnected = false;
    private ImageView mCameraView;
    private TextView mTextViewSpeed;
    private TextView mTextViewTurnLeft;
    private TextView mTextViewTurnRight;
    private ImageView speedButton, connectionButton;
    private ImageView carStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_car);
        setTitle("Control Car");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navViewStaff);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navControlCar:
                    break;

                case R.id.navCreateDel:
                    Intent intent2 = new Intent(ControlCarActivity.this, RegisterDeliveryActivity.class);
                    startActivity(intent2);
                    break;

                case R.id.navLogOutS:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent3 = new Intent(ControlCarActivity.this, LoginActivity.class);
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

        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        mCameraView = findViewById(R.id.imageView);
        mTextViewSpeed = findViewById(R.id.textview_speed);
        mTextViewTurnLeft = findViewById(R.id.textview_angleLeft);
        mTextViewTurnRight  = findViewById(R.id.textview_angleRight);
        speedButton = findViewById(R.id.speed_button);
        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlCarActivity.this, SpeedLimit.class));
            }
        });
        carStatus = findViewById(R.id.carStatus);
        connectToMqttBroker();
        doTheAutoRefresh();
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
                Log.i(TAG, "Disconnected from broker");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            }
        });
    }

    private void connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient.connect(TAG, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;

                    final String successfulConnection = "Connected to MQTT broker";
                    Log.i(TAG, successfulConnection);
                    Toast.makeText(getApplicationContext(), successfulConnection, Toast.LENGTH_SHORT).show();

                    mMqttClient.subscribe(MAIN_TOPIC + "/camera", QOS, null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    final String failedConnection = "Failed to connect to MQTT broker";
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

                    if (topic.equals(MAIN_TOPIC + "/camera")) {
                        final Bitmap bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

                        final byte[] payload = message.getPayload();
                        final int[] colors = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
                        for (int ci = 0; ci < colors.length; ++ci) {
                            final byte r = payload[3 * ci];
                            final byte g = payload[3 * ci + 1];
                            final byte b = payload[3 * ci + 2];
                            colors[ci] = Color.rgb(r, g, b);
                        }
                        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

                        mCameraView.setImageBitmap(bm);
                    } else {
                        Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }

    //method taken from: https://stackoverflow.com/questions/18268218/change-screen-orientation-programmatically-using-a-button
    public void changeScreenOrientation(View view) {
        int orientation = ControlCarActivity.this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }, 4000);
        }
    }

    void drive(String commandToSend, String actionDescription) {
        if (!isConnected) {
            final String notConnected = "Not connected (yet)";
            Log.e(TAG, notConnected);
            Toast.makeText(getApplicationContext(), notConnected, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, actionDescription);
        mMqttClient.publish(MAIN_TOPIC + "/control/handleInput", commandToSend, QOS, null);
    }

    private String increment(String pointer) {
        final int INCREMENT_BY = 10;
        String result = " ";
        if(powerOn) {
            switch(pointer) {
                case "2": // go forward
                    if(reverse != 0){
                        reverse = reverse - INCREMENT_BY;
                        result = "3 " + reverse.toString();
                        mTextViewSpeed.setText(reverse.toString());
                        break;
                    }
                    if(forward != speedLimitForward) {
                        forward = forward + INCREMENT_BY;
                    }
                    result = "2 " + forward.toString();
                    mTextViewSpeed.setText(forward.toString());
                    break;
                case "3": // go backwards
                    if(forward != 0){
                        forward = forward - INCREMENT_BY;
                        result = "2 " + forward.toString();
                        mTextViewSpeed.setText(forward.toString());
                        break;
                    }
                    if(reverse != speedLimitBackwards) {
                        reverse = reverse + INCREMENT_BY;
                    }
                    result = "3 " + reverse.toString();
                    mTextViewSpeed.setText(reverse.toString());
                    break;
                case "4": // go right
                    if(left != 0){
                        left = left - INCREMENT_BY;
                        result = "5 " + left.toString();
                        mTextViewTurnLeft.setText(left.toString());
                        break;
                    }
                    if (right != TURN_LIMIT) {
                        right = right + INCREMENT_BY;
                    }
                    result = "4 " + right.toString();
                    mTextViewTurnRight.setText(right.toString());
                    break;
                case "5": // go left
                    if(right != 0){
                        right = right - INCREMENT_BY;
                        result = "4 " + right.toString();
                        mTextViewTurnRight.setText(right.toString());
                        break;
                    }
                    if (left != TURN_LIMIT) {
                        left = left + INCREMENT_BY;
                    }
                    result = "5 " + left.toString();
                    mTextViewTurnLeft.setText(left.toString());
                    break;
            }
        }
        return result;
    }

    public void forwardThrottle(View view) {
        drive(FORWARD, "Moving forward");
    }

    public void reverseThrottle(View view) {
        drive(increment(REVERSE), "Moving backwards");
    }

    public void turnRight(View view) {
        drive(increment(RIGHT), "Turning right");
    }

    public void turnLeft(View view) {
        drive(increment(LEFT), "Turning left");
    }

    public void stop(View view) {
        forward = 0;
        reverse = 0;
        drive(STOP_THROTTLE, "Stopping");
        mTextViewSpeed.setText("0");
    }

    public void stopTurn(View view) {
        left = 0;
        right = 0;
        drive(STOP_TURN, "Straighten Angle");
        mTextViewTurnLeft.setText("0");
        mTextViewTurnRight.setText("0");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void doTheAutoRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 1000);
    }

    public void powerOn(View view) {
        powerOn = true;
        drive(POWER_ON, "Turn on");
        carStatus.setImageResource(R.drawable.car_on);
    }

    public void powerOff(View view) {
        powerOn = false;
        drive(POWER_OFF, "Turn off");
        carStatus.setImageResource(R.drawable.car_off);
        forward = 0;
        reverse = 0;
        mTextViewSpeed.setText("0");
        left = 0;
        right = 0;
        mTextViewTurnLeft.setText("0");
        mTextViewTurnRight.setText("0");
    }

    public static Integer getSpeedLimitForward() {
        return speedLimitForward;
    }

    public static Integer getSpeedLimitBackwards() {
        return speedLimitBackwards;
    }

    public static void setSpeedLimitForward(Integer speedLimitForward2) {
        speedLimitForward = speedLimitForward2;
    }

    public static void setSpeedLimitBackwards(Integer speedLimitBackwards2) {
        speedLimitBackwards = speedLimitBackwards2;
    }
}
