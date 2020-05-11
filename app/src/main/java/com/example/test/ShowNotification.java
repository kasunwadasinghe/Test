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

public class ShowNotification extends AppCompatActivity {

    Button notification,back;
    private static final int VIBRATE_PERMITION_CODE = 1;
    Vibrator vibrator;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);

        notification = (Button)findViewById(R.id.btnNotify);
        notification.setOnClickListener(showNotification);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this,R.raw.bell_sound);

        back = (Button)findViewById(R.id.btnBackNotification);
        back.setOnClickListener(click_back);
    }

    private View.OnClickListener showNotification = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowNotification.this);
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

    private View.OnClickListener click_back=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent notification = new Intent(ShowNotification.this, MainDashboard.class);
            startActivity(notification);
        }
    };
}
