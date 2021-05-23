package com.example.androidappcar;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SpeedLimit extends Activity {


    Button saveButton, exitButton;
    SeekBar forwardBar, reverseBar;
    TextView speedForwardInteger, speedReverseInteger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speed_limit_window);

        speedForwardInteger = findViewById(R.id.speedForwardInteger);
        speedForwardInteger.setText(ControlCarActivity.getSpeedLimitForward() + "%");

        speedReverseInteger = findViewById(R.id.speedReverseInteger);
        speedReverseInteger.setText(ControlCarActivity.getSpeedLimitBackwards() + "%");

        forwardBar = findViewById(R.id.forwardBar);
        forwardBar.setProgress(ControlCarActivity.getSpeedLimitForward() / 10);
        forwardBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                speedForwardInteger.setText(forwardBar.getProgress() * 10 + "%");

            }
        });

        reverseBar = findViewById(R.id.reverseBar);
        reverseBar.setProgress(ControlCarActivity.getSpeedLimitBackwards() / 10);
        reverseBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                speedReverseInteger.setText(reverseBar.getProgress() * 10 + "%");
            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(e -> {
            ControlCarActivity.setSpeedLimitForward(forwardBar.getProgress() * 10);
            ControlCarActivity.setSpeedLimitBackwards(reverseBar.getProgress() * 10);
        });

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(e -> finish());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.8));
    }
}
