package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.test.viewModel.ActivityDetails;
import com.example.test.viewModel.ActivityDetailsList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ViewActivities extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference rff;
    ListView activityList;

    private static final String TAG = "View Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_activities);
        database = FirebaseDatabase.getInstance();
        rff = database.getReference().child("Activity");

        activityList = (ListView)findViewById(R.id.lstActivity);
        ArrayList<Map<String,ActivityDetails>> arrayList = new ArrayList<Map<String,ActivityDetails>>();
        ActivityDetailsList adapter = new ActivityDetailsList(ViewActivities.this,arrayList);
        activityList.setAdapter(adapter);

        rff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,ActivityDetails> value = (Map <String,ActivityDetails>) dataSnapshot.getValue();
                arrayList.add(value);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
