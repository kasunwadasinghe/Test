package com.example.test;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.test.viewModel.ActivityDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    ImageButton save,delete;
    Button buttonDate,buttonTime;
    EditText activityName,address1,address2,city,country,reporter,activityDate,activityTime;
    ActivityDetails act;

    // views for button
    private Button btnSelect;

    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference ref;
    StorageReference storageReference;
    FirebaseAuth mAuth;

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

        // initialise views
        btnSelect = findViewById(R.id.btnChoose);
        imageView = findViewById(R.id.imgView);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });
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

            boolean isValid = true;

            if(ActivityName.isEmpty()){
                activityName.setHintTextColor(Color.argb(100,255,0,0));
                isValid = false;
            }else {
                activityName.setHintTextColor(Color.argb(100,255,255,255));
            }

            if(Reporter.isEmpty()){
                reporter.setHintTextColor(Color.argb(100,255,0,0));
                isValid =false;
            }else {
                reporter.setHintTextColor(Color.argb(100,255,255,255));
            }

            if(Date.isEmpty()){
                activityDate.setHintTextColor(Color.argb(100,255,0,0));
                isValid = false;
            }else {
                activityDate.setHintTextColor(Color.argb(100,255,255,255));
            }

            act = new ActivityDetails();
            act.setActivityName(ActivityName);
            act.setAddress1(Address1);
            act.setAddress2(Address2);
            act.setCity(City);
            act.setCountry(Country);
            act.setReporter(Reporter);
            act.setDate(Date);
            act.setTime(Time);

            if(isValid){
                uploadImage();

                database = FirebaseDatabase.getInstance();
                ref = database.getReference().child("Activity");
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
            }else {
                Toast.makeText(MainActivity.this,"Please fill mandatory fields",Toast.LENGTH_LONG).show();
            }
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateString = year + "-" + ++month + "-" + dayOfMonth;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = dateFormat.parse(dateString);
            activityDate.setText(dateFormat.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null&& data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

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

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private String getExtention(Uri file){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(file));
    }

    private void uploadImage()
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            signInAnonymously();
        }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
//            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

            String extention = getExtention(filePath);
            StorageReference riversRef = storageReference.child("images/"+ UUID.randomUUID().toString()+"."+extention);

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
//                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            progressDialog.dismiss();
                        }
                    });

            // adding listeners on upload
            // or failure of image
//            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                 @Override
//                 public void onSuccess(
//                         UploadTask.TaskSnapshot taskSnapshot)
//                 {
//                     // Image uploaded successfully
//                     // Dismiss dialog
//                     progressDialog.dismiss();
//                     Toast.makeText(MainActivity.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
//                 }
//            }).addOnFailureListener(new OnFailureListener() {
//                  @Override
//                  public void onFailure(@NonNull Exception e)
//                  {
//                      // Error, Image not uploaded
//                      progressDialog.dismiss();
//                      Toast.makeText(MainActivity.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
//                  }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                     // Progress Listener for loading
//                     // percentage on the dialog box
//                     @Override
//                     public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
//                     {
//                         double progress = (100.0 * taskSnapshot.getBytesTransferred()/ taskSnapshot.getTotalByteCount());
//                         progressDialog.setMessage( "Uploaded " + (int)progress + "%");
//                     }
//             });
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }
}
