package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button viewActivity = (Button)findViewById(R.id.btnViewActivity);
        viewActivity.setOnClickListener(viewActivity_click);
        Button addActivity = (Button)findViewById(R.id.btnAddActivity);
        addActivity.setOnClickListener(addActivity_click);
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

}
