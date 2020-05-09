package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.test.viewModel.ActivityDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ImageButton save;
    ImageButton delete;
    Button buttonDate;
    Button buttonTime;
    EditText activityName;
    EditText address1;
    EditText address2;
    EditText city;
    EditText country;
    EditText reporter;
    EditText activityDate;
    EditText activityTime;
    DatabaseReference ref;
    ActivityDetails act;
    FirebaseDatabase database;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        save = (ImageButton)findViewById(R.id.btnSave);
        save.setOnClickListener(save_click);

        delete = (ImageButton)findViewById(R.id.btnDelete);
        delete.setOnClickListener(delete_click);

        buttonDate = (Button)findViewById(R.id.btnCalendar);
        buttonDate.setOnClickListener(activityDate_click);

        buttonTime = (Button)findViewById(R.id.btnTime);
        buttonTime.setOnClickListener(activityTime_click);

        activityDate = (EditText)findViewById(R.id.dateActivityDate);
        activityTime = (EditText)findViewById(R.id.timeActivityTime);

        ref = FirebaseDatabase.getInstance().getReference().child("Activity");

    }

    private View.OnClickListener save_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activityName = (EditText)findViewById(R.id.txtActivityName);
            address1 = (EditText)findViewById(R.id.txtAddress1);
            address2 = (EditText)findViewById(R.id.txtAddress2);
            city = (EditText)findViewById(R.id.txtCity);
            country = (EditText)findViewById(R.id.txtCountry);
            reporter = (EditText)findViewById(R.id.txtReporter);
            activityDate = (EditText)findViewById(R.id.dateActivityDate);
            activityTime = (EditText)findViewById(R.id.timeActivityTime);

            String ActivityName = activityName.getText().toString();
            String Address1 = address1.getText().toString();
            String Address2 = address2.getText().toString();
            String City = city.getText().toString();
            String Country = country.getText().toString();
            String Reporter = reporter.getText().toString();
            String Date = activityDate.getText().toString();
            String Time = activityTime.getText().toString();

            act = new ActivityDetails();
            act.setActivityName(ActivityName);
            act.setAddress1(Address1);
            act.setAddress2(Address2);
            act.setCity(City);
            act.setCountry(Country);
            act.setReporter(Reporter);
            act.setDate(Date);
            act.setTime(Time);

            database = FirebaseDatabase.getInstance();
            ref = database.getReference().child("Activity").child("123");
            ref.push().setValue(act)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(MainActivity.this,"Activity created",Toast.LENGTH_LONG).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Activity failed",Toast.LENGTH_LONG).show();
                        }
                    });

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String value = dataSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

        }
    };

    private View.OnClickListener delete_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText activityName = (EditText)findViewById(R.id.txtActivityName);
            String ActivityName = activityName.getText().toString();
        }
    };

    private  View.OnClickListener activityTime_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showClock();
        }
    };

    private View.OnClickListener activityDate_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShowCalender();
        }
    };

    private void ShowCalender(){
        DatePickerDialog datePicker = new DatePickerDialog(
                MainActivity.this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }

    private void showClock() {
        TimePickerDialog timePicker = new TimePickerDialog(
                MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        activityTime.setText(time);
                    }
                },
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );
        timePicker.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateString = year + "-" + ++month + "-" + dayOfMonth;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        AwesomeValidation dateValidation = new AwesomeValidation(ValidationStyle.BASIC);
//        dateValidation.addValidation(this, R.id.activityDate, RegexTemplate.NOT_EMPTY, R.string.activity_date_required);
//        dateValidation.clear();

        try {
            Date date = dateFormat.parse(dateString);
            activityDate.setText(dateFormat.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void basicReadWrite() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
