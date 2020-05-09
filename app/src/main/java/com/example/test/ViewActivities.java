package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.test.viewModel.ActivityDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewActivities extends AppCompatActivity {
    ImageButton delete;
    ImageButton view;
    TextView activityName;
    TextView address1;
    TextView address2;
    TextView city;
    TextView country;
    TextView reporter;
    TextView activityDate;
    TextView activityTime;
    FirebaseDatabase database;
    DatabaseReference rff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_activities);

        activityName = (TextView) findViewById(R.id.lblActivityName);
//        address1 = (TextView) findViewById(R.id.txtAddress1);
//        address2 = (TextView) findViewById(R.id.txtAddress2);
//        city = (TextView) findViewById(R.id.txtCity);
//        country = (TextView) findViewById(R.id.txtCountry);
        reporter = (TextView) findViewById(R.id.lblReporter);
        activityDate = (TextView) findViewById(R.id.lblActivityDate);
        activityTime = (TextView) findViewById(R.id.lblActivityTime);

        database = FirebaseDatabase.getInstance();
        rff = database.getReference().child("Activity");

        ActivityDetails acc = new ActivityDetails();
    }
}
