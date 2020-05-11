package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

public class MainDashboard extends AppCompatActivity {

    Button viewActivity,addActivity,tweetActivity,notification,exit;
    private static final int VIBRATE_PERMITION_CODE = 1;
    Vibrator vibrator;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        viewActivity = (Button)findViewById(R.id.btnViewActivity);
        viewActivity.setOnClickListener(viewActivity_click);

        addActivity = (Button)findViewById(R.id.btnAddActivity);
        addActivity.setOnClickListener(addActivity_click);

        tweetActivity = (Button)findViewById(R.id.btnTwitterActivity);
        tweetActivity.setOnClickListener(tweetActivity_click);

        notification = (Button)findViewById(R.id.btnViewNotification);
        notification.setOnClickListener(notification_click);

        exit = (Button)findViewById(R.id.btnExit);
        exit.setOnClickListener(exit_click);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this,R.raw.bell_sound);
    }

    private View.OnClickListener addActivity_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent addActivity = new Intent(MainDashboard.this, MainActivity.class);
            startActivity(addActivity);
        }
    };

    private View.OnClickListener viewActivity_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent addActivity = new Intent(MainDashboard.this, ViewActivities.class);
            startActivity(addActivity);
        }
    };

    private View.OnClickListener tweetActivity_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent tweetActivity = new Intent(MainDashboard.this, twitter.class);
            startActivity(tweetActivity);
        }
    };

    private  View.OnClickListener exit_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            System.exit(0);
        }
    };

    private View.OnClickListener notification_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainDashboard.this);
            builder.setMessage("Do you want to vibrate?")
                    .setPositiveButton("Vibrate", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            VibrationEffect vibe = VibrationEffect.createOneShot(1000,10);
                            vibrator.vibrate(vibe);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Ring", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mediaPlayer.start();
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };
}