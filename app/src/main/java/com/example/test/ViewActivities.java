package com.example.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.viewModel.ActivityDetailsList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewActivities extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference rff;
    ListView activityList;
    ActivityDetailsList adapter;
    ArrayList<Map<String,String>> arrayList;
    DatabaseReference ref;
    private static final String TAG = "View Activity";
    SearchView searchView;
    EditText txtSearch;
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_activities);
        database = FirebaseDatabase.getInstance();
        rff = database.getReference().child("Activity");

        activityList = (ListView)findViewById(R.id.lstActivity);
        arrayList = new ArrayList<Map<String,String>>();
        adapter = new ActivityDetailsList(ViewActivities.this,arrayList);
        activityList.setAdapter(adapter);

        txtSearch = (EditText)findViewById(R.id.txtSearch);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(searchActivity);

        rff.addValueEventListener(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Map<String,Map<String,String>> val = (Map<String, Map<String,String>>)dataSnapshot.getValue();
            if(val != null){
                for(Map.Entry<String,Map<String,String>> entry : val.entrySet()){
                    HashMap<String,String> key = new HashMap<String, String>(){{
                        put("id",entry.getKey());
                    }};
                    entry.getValue().put("id",entry.getKey());
                    arrayList.add(entry.getValue());
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Value is: " + entry);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.w(TAG, "Failed to read value.", databaseError.toException());
        }
    };

    private View.OnClickListener searchActivity = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String query = txtSearch.getText().toString().trim().toLowerCase();
            database = FirebaseDatabase.getInstance();
            rff = database.getReference().child("Activity");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map<String,Map<String,String>> val = (Map<String, Map<String,String>>)dataSnapshot.getValue();
                    for(Map.Entry<String,Map<String,String>> entry : val.entrySet()){

                        if(entry.getValue().get("activityName") == query){
                            HashMap<String,String> key = new HashMap<String, String>(){{
                                put("id",entry.getKey());
                            }};
                            entry.getValue().put("id",entry.getKey());
                            arrayList.add(entry.getValue());
                            adapter.notifyDataSetChanged();
                            Log.d(TAG, "Value is: " + entry);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };

    public void deleteActivity(String id){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("Activity").child(id);
        ref.removeValue();
    }
}
